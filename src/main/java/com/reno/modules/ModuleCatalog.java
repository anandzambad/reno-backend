package com.reno.modules;

import java.util.List;

public final class ModuleCatalog {
    private ModuleCatalog() {}

    public static final List<String> V2_MODULES = List.of(
            "authentication", "users", "leads", "lead-assignment", "contractors",
            "contractor-applications", "complaints", "estimation", "invoices",
            "work-orders", "documents", "reports", "locations", "services",
            "subscriptions", "promotions", "admin"
    );
}
