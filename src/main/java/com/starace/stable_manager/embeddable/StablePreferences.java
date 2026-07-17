package com.starace.stable_manager.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class StablePreferences {
    private int overdueCogginsDays;
    private int overdueFarrierDays;
}
