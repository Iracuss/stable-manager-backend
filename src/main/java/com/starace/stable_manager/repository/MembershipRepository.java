package com.starace.stable_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.Membership;
import com.starace.stable_manager.model.Stable;
import com.starace.stable_manager.model.User;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long>{
    Optional<Membership> findByUserAndStable(User user, Stable stable);
    Optional<Membership> findByUserIdAndStableId(Long userId, Long stableId);

    List<Membership> findByUser(User user);
    List<Membership> findByUserId(Long userId);
}
