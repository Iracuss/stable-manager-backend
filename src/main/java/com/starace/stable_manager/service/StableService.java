package com.starace.stable_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.StableRequest;
import com.starace.stable_manager.model.Membership;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.repository.StableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StableService {
    private final StableRepository stableRepository;
    private final MembershipService membershipService;

    public Stable getStableById(Long stableId) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        // Check if member of stable
        if(membershipService.checkMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        Stable stable = optStable.get();

        return stable;
    }

    public List<Stable> getAllUserStables() {
        return membershipService.getAllUserMemberships()
            .stream()
            .map(membership -> membership.getStable())
            .toList();
    }

    public Stable createStable(StableRequest request) {
        Stable stable = new Stable();
        stable.setPreferences(request.getPreferences());
        stable.setName(request.getName());
        stable = stableRepository.save(stable); // Should give the stable an ID

        // Create OWNER membership
        List<Membership> ownerMembership = List.of(membershipService.createOwnerMembership(stable.getId()));

        stable.setMemberships(ownerMembership);

        return stableRepository.save(stable);
    }

    public Stable updateStable(Long stableId, StableRequest request) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        Stable stable = optStable.get();

        // Check if member of stable and owner/manager
        if(membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }

        if(request.getName() != null) stable.setName(request.getName());
        if(request.getPreferences() != null) stable.setPreferences(request.getPreferences());

        return stableRepository.save(stable);
    }

    public void deleteStable(Long stableId) {
        // Check if member of stable and owner/manager
        if(membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a owner/staff or member of this stable");
        }

        stableRepository.deleteById(stableId);
    }
}
