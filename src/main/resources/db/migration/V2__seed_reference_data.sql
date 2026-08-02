INSERT INTO roles (name) VALUES ('ADMIN'), ('CUSTOMER'), ('CONTRACTOR'), ('ESTIMATOR');

INSERT INTO services (name, description) VALUES
('Painting', 'Interior and exterior painting'),
('Flooring', 'Flooring installation and renovation'),
('Kitchen', 'Kitchen renovation and remodeling'),
('Roof', 'Roof repair and replacement'),
('Doors & Windows', 'Doors and windows installation'),
('Bathroom', 'Bathroom renovation');

INSERT INTO budget_ranges (name, min_amount, max_amount) VALUES
('Under 1 Lakh', 0, 100000),
('1 - 5 Lakh', 100000, 500000),
('5 - 10 Lakh', 500000, 1000000),
('10 - 25 Lakh', 1000000, 2500000),
('25 Lakh+', 2500000, NULL);
