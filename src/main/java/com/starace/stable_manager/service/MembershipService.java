package com.starace.stable_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.MembershipRequest;
import com.starace.stable_manager.enums.MembershipRole;
import com.starace.stable_manager.model.Membership;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.MembershipRepository;
import com.starace.stable_manager.repository.StableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final CurrentUserService currentUserService;
    private final MembershipRepository membershipRepository;
    private final StableRepository stableRepository;

    public Membership getUserMembership(Long stableId) {
        User currentUser = currentUserService.getCurrentUser();
        return membershipRepository
            .findByUserIdAndStableId(currentUser.getId(), stableId)
            .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    public List<Membership> getAllUserMemberships() {
        User currentUser = currentUserService.getCurrentUser();
        return membershipRepository.findByUser(currentUser);
    }

    // Creating through creation of stable
    public Membership createOwnerMembership(Long stableId) {
        User currentUser = currentUserService.getCurrentUser();
        Membership membership = new Membership();
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("Stable does not exist");
        }

        Stable stable = optStable.get();

        membership.setUser(currentUser);
        membership.setStable(stable);
        membership.setInvitedBy(currentUser.getId());
        membership.setAcceptedInvite(true);
        membership.setMembershipRole(MembershipRole.OWNER);
        membership.setInvitedAt(LocalDateTime.now());
        membership.setJoinedAt(LocalDateTime.now());

        return membershipRepository.save(membership);
    }

    // Accepting the invite route (Sent email)
    // public Membership createJoinedMembership(MembershipRequest request) {
    //     User currentUser = currentUserService.getCurrentUser();
    //     Membership membership = new Membership();
    //     membership.setUser(currentUser);
    //     membership.setAcceptedInvite(true);
    //     // membership.setStable(); how do I get this?
    //     membership.setJoinedAt(LocalDateTime.now());
    //     membership.setMembershipRole(MembershipRole.STAFF);
    //     membership.setInvitedBy(null);
    //     // Not finished

    //     return membershipRepository.save(membership);
    // }

    public void deleteMembership(Long membershipId) {
        membershipRepository.deleteById(membershipId);
    }

    public boolean checkMembershipStatus(Long stableId) {
        Long currentUserId = currentUserService.getCurrentUser().getId();

        Optional<Membership> optMembership = membershipRepository.findByUserIdAndStableId(currentUserId, stableId);

        // Maybe throw an error and not a false?
        if(optMembership.isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean checkEditMembershipStatus(Long stableId) {
        Long currentUserId = currentUserService.getCurrentUser().getId();

        Optional<Membership> optMembership = membershipRepository.findByUserIdAndStableId(currentUserId, stableId);

        // Maybe throw an error and not a false?
        if(optMembership.isEmpty()) {
            return false;
        }

        Membership membership = optMembership.get();
        MembershipRole role = membership.getMembershipRole();

        if(role != MembershipRole.MANAGER && role != MembershipRole.OWNER) {
            return false;
        }

        return true;
    }
    
}
