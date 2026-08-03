CREATE TABLE contractor_services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contractor_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uk_contractor_service (contractor_id, service_id),
    KEY idx_contractor_services_service_active (service_id, active)
);
