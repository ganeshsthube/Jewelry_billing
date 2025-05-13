CREATE TABLE customers (
    invoice_number VARCHAR(50) AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(200),
    phone VARCHAR(20),
    state VARCHAR(50),
    pan_number VARCHAR(20),
    gstin VARCHAR(20),
    invoice_date DATE
);
