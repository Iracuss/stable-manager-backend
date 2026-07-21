package com.starace.stable_manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.StableRequest;
import com.starace.stable_manager.dto.StableResponse;
import com.starace.stable_manager.model.Membership;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.repository.StableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StableService {
    private final StableRepository stableRepository;
    private final MembershipService membershipService;

    public StableResponse getStableById(Long stableId) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        // Check if member of stable
        if(!membershipService.checkMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        Stable stable = optStable.get();

        StableResponse response = new StableResponse();
        response.setId(stableId);
        response.setName(stable.getName());
        response.setPreferences(stable.getPreferences());

        return response;
    }

    public List<StableResponse> getAllUserStables() {
        List<StableResponse> responses = new ArrayList<>();

        for(Membership membership : membershipService.getAllUserMemberships()) {
            Stable stable = membership.getStable();

            StableResponse response = new StableResponse();
            response.setId(stable.getId());
            response.setName(stable.getName());
            response.setPreferences(stable.getPreferences());

            responses.add(response);
        }

        return responses;
    }

    public StableResponse createStable(StableRequest request) {
        Stable stable = new Stable();
        stable.setPreferences(request.getPreferences());
        stable.setName(request.getName());
        Stable savedStable = stableRepository.save(stable); // Should give the stable an ID

        // Create OWNER membership
        membershipService.createOwnerMembership(savedStable.getId());

        StableResponse response = new StableResponse();
        response.setId(savedStable.getId());
        response.setName(savedStable.getName());
        response.setPreferences(savedStable.getPreferences());

        return response;
    }

    public StableResponse updateStable(Long stableId, StableRequest request) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        Stable stable = optStable.get();

        // Check if member of stable and owner/manager
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }

        if(request.getName() != null) stable.setName(request.getName());
        if(request.getPreferences() != null) stable.setPreferences(request.getPreferences());

        StableResponse response = new StableResponse();
        response.setId(stable.getId());
        response.setName(stable.getName());
        response.setPreferences(stable.getPreferences());
        stableRepository.save(stable);

        return response;
    }

    public void deleteStable(Long stableId) {
        // Check if member of stable and owner/manager
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }

        stableRepository.deleteById(stableId);
    }
}
