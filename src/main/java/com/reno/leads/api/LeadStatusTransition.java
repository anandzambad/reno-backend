package com.reno.leads.api;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class LeadStatusTransition {
    private static final Map<LeadStatus, Set<LeadStatus>> ALLOWED = Map.of(
        LeadStatus.NEW, EnumSet.of(LeadStatus.CONTACTED, LeadStatus.CANCELLED),
        LeadStatus.CONTACTED, EnumSet.of(LeadStatus.QUALIFIED, LeadStatus.CANCELLED),
        LeadStatus.QUALIFIED, EnumSet.of(LeadStatus.QUOTED, LeadStatus.ASSIGNED, LeadStatus.CANCELLED),
        LeadStatus.QUOTED, EnumSet.of(LeadStatus.ASSIGNED, LeadStatus.CANCELLED),
        LeadStatus.ASSIGNED, EnumSet.of(LeadStatus.IN_PROGRESS, LeadStatus.CANCELLED),
        LeadStatus.IN_PROGRESS, EnumSet.of(LeadStatus.COMPLETED, LeadStatus.CANCELLED),
        LeadStatus.COMPLETED, EnumSet.noneOf(LeadStatus.class),
        LeadStatus.CANCELLED, EnumSet.noneOf(LeadStatus.class)
    );

    private LeadStatusTransition() {}
    public static boolean allowed(LeadStatus from, LeadStatus to) {
        return from != to && ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }
}
