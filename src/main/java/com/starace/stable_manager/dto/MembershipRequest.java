package com.starace.stable_manager.dto;

import com.starace.stable_manager.enums.MembershipRole;

import lombok.Data;

@Data
public class MembershipRequest {
    private MembershipRole membershipRole;
    private Long stableId;
    private Long userId;
}
