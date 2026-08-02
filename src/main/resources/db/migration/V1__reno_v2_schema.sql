CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(30),
    password_hash VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    state VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uk_location (state, city, postal_code)
);

CREATE TABLE services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL UNIQUE,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE budget_ranges (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    min_amount DECIMAL(14,2) NOT NULL,
    max_amount DECIMAL(14,2),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE contractors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,
    company_name VARCHAR(200) NOT NULL,
    license_number VARCHAR(100),
    phone VARCHAR(30),
    email VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_contractors_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE contractor_locations (
    contractor_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    PRIMARY KEY (contractor_id, location_id),
    FOREIGN KEY (contractor_id) REFERENCES contractors(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

CREATE TABLE contractor_services (
    contractor_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    PRIMARY KEY (contractor_id, service_id),
    FOREIGN KEY (contractor_id) REFERENCES contractors(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

CREATE TABLE leads (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT,
    service_id BIGINT NOT NULL,
    location_id BIGINT,
    budget_range_id BIGINT,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(30) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    description TEXT,
    status VARCHAR(40) NOT NULL DEFAULT 'NEW',
    source VARCHAR(50) NOT NULL DEFAULT 'WEB',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (service_id) REFERENCES services(id),
    FOREIGN KEY (location_id) REFERENCES locations(id),
    FOREIGN KEY (budget_range_id) REFERENCES budget_ranges(id),
    INDEX idx_leads_status (status),
    INDEX idx_leads_postal_code (postal_code),
    INDEX idx_leads_created_at (created_at)
);

CREATE TABLE lead_assignments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    contractor_id BIGINT NOT NULL,
    assigned_by BIGINT,
    status VARCHAR(40) NOT NULL DEFAULT 'ASSIGNED',
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_lead_contractor (lead_id, contractor_id),
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    FOREIGN KEY (contractor_id) REFERENCES contractors(id),
    FOREIGN KEY (assigned_by) REFERENCES users(id)
);

CREATE TABLE lead_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    created_by BIGINT,
    note TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE lead_follow_ups (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    assigned_to BIGINT,
    scheduled_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(id)
);

CREATE TABLE complaints (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT,
    contractor_id BIGINT,
    created_by BIGINT,
    subject VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lead_id) REFERENCES leads(id),
    FOREIGN KEY (contractor_id) REFERENCES contractors(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE lead_estimations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    contractor_id BIGINT,
    subtotal DECIMAL(14,2) NOT NULL DEFAULT 0,
    tax_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    FOREIGN KEY (contractor_id) REFERENCES contractors(id)
);

CREATE TABLE lead_estimation_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estimation_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity DECIMAL(12,2) NOT NULL DEFAULT 1,
    unit_price DECIMAL(14,2) NOT NULL DEFAULT 0,
    amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (estimation_id) REFERENCES lead_estimations(id) ON DELETE CASCADE
);

CREATE TABLE invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    contractor_id BIGINT,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(14,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    issued_at TIMESTAMP NULL,
    due_at TIMESTAMP NULL,
    paid_at TIMESTAMP NULL,
    FOREIGN KEY (lead_id) REFERENCES leads(id),
    FOREIGN KEY (contractor_id) REFERENCES contractors(id)
);

CREATE TABLE work_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    contractor_id BIGINT NOT NULL,
    work_order_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'CREATED',
    start_date DATE,
    completion_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lead_id) REFERENCES leads(id),
    FOREIGN KEY (contractor_id) REFERENCES contractors(id)
);

CREATE TABLE work_order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    work_order_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity DECIMAL(12,2) NOT NULL DEFAULT 1,
    unit_price DECIMAL(14,2) NOT NULL DEFAULT 0,
    amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE
);

CREATE TABLE documents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT,
    contractor_id BIGINT,
    uploaded_by BIGINT,
    document_type VARCHAR(100) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    content_type VARCHAR(150),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lead_id) REFERENCES leads(id),
    FOREIGN KEY (contractor_id) REFERENCES contractors(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    contractor_id BIGINT NOT NULL,
    plan_name VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NULL,
    FOREIGN KEY (contractor_id) REFERENCES contractors(id)
);

CREATE TABLE subscription_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_id BIGINT NOT NULL,
    transaction_reference VARCHAR(150) NOT NULL UNIQUE,
    amount DECIMAL(14,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

CREATE TABLE promotions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    discount_percent DECIMAL(5,2),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    starts_at TIMESTAMP,
    ends_at TIMESTAMP
);
