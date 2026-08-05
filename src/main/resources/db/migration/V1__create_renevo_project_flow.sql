CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_code VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(180),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_code VARCHAR(40) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    city VARCHAR(100),
    property_type VARCHAR(80),
    status VARCHAR(40) NOT NULL DEFAULT 'NEW',
    progress_percent DECIMAL(5,2) NOT NULL DEFAULT 0,
    estimated_amount DECIMAL(15,2),
    agreed_amount DECIMAL(15,2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE project_stages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    stage_order INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    progress_percent DECIMAL(5,2) NOT NULL DEFAULT 0,
    due_date DATE,
    CONSTRAINT fk_stage_project FOREIGN KEY (project_id) REFERENCES projects(id),
    UNIQUE KEY uk_project_stage_order (project_id, stage_order)
);

CREATE TABLE work_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    stage_id BIGINT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    assigned_to VARCHAR(150),
    due_date DATE,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_work_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_work_stage FOREIGN KEY (stage_id) REFERENCES project_stages(id)
);

CREATE TABLE material_requirements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    quantity DECIMAL(15,3),
    unit VARCHAR(30),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    supplier_name VARCHAR(200),
    expected_date DATE,
    delivered_date DATE,
    CONSTRAINT fk_material_project FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE project_payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    stage_id BIGINT,
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    payment_reference VARCHAR(120),
    due_date DATE,
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_payment_stage FOREIGN KEY (stage_id) REFERENCES project_stages(id)
);

CREATE TABLE project_issues (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    work_item_id BIGINT,
    issue_type VARCHAR(40) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    CONSTRAINT fk_issue_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_issue_work FOREIGN KEY (work_item_id) REFERENCES work_items(id)
);

CREATE TABLE crm_follow_ups (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    follow_up_day INT NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    channel VARCHAR(30),
    notes TEXT,
    completed_at TIMESTAMP NULL,
    CONSTRAINT fk_followup_project FOREIGN KEY (project_id) REFERENCES projects(id),
    UNIQUE KEY uk_project_followup_day (project_id, follow_up_day)
);

CREATE INDEX idx_project_customer ON projects(customer_id);
CREATE INDEX idx_work_project_status ON work_items(project_id, status);
CREATE INDEX idx_material_project_status ON material_requirements(project_id, status);
CREATE INDEX idx_payment_project_status ON project_payments(project_id, status);
CREATE INDEX idx_issue_project_status ON project_issues(project_id, status);
CREATE INDEX idx_followup_due ON crm_follow_ups(status, scheduled_at);
