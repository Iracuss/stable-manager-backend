package com.starace.stable_manager.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.starace.stable_manager.enums.MembershipRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Create one membership for the connection
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "stable_id"}
    )
)

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stable_id")
    @JsonBackReference
    private Stable stable;

    @Enumerated(EnumType.STRING)
    private MembershipRole membershipRole;

    private LocalDateTime joinedAt;
    private LocalDateTime invitedAt;
    private boolean acceptedInvite;
    private Long invitedBy;
}
