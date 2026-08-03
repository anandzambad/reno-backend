CREATE TABLE contractor_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contractor_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OFFLINE',
    latitude DECIMAL(10,7) NULL,
    longitude DECIMAL(10,7) NULL,
    service_radius_km DECIMAL(6,2) NOT NULL DEFAULT 10.00,
    last_seen_at TIMESTAMP(6) NULL,
    version BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_contractor_availability_contractor (contractor_id),
    KEY idx_contractor_availability_status (status),
    KEY idx_contractor_availability_last_seen (last_seen_at)
);

CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    contractor_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    address VARCHAR(500) NULL,
    scheduled_at TIMESTAMP(6) NULL,
    estimated_price DECIMAL(12,2) NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    accepted_at TIMESTAMP(6) NULL,
    started_at TIMESTAMP(6) NULL,
    completed_at TIMESTAMP(6) NULL,
    cancelled_at TIMESTAMP(6) NULL,
    version BIGINT NOT NULL DEFAULT 0,
    KEY idx_bookings_contractor_status (contractor_id, status),
    KEY idx_bookings_customer_created (customer_id, created_at)
);

CREATE TABLE booking_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    old_status VARCHAR(30) NULL,
    new_status VARCHAR(30) NOT NULL,
    changed_by BIGINT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    KEY idx_booking_history_booking_created (booking_id, created_at)
);
