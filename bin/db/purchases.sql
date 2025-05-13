CREATE TABLE purchases (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(50),
    product_name VARCHAR(100),
    hsn VARCHAR(20),
    pieces INT,
    net_weight DOUBLE,
    rate DOUBLE,
    labour DOUBLE,
    amount DOUBLE,
    FOREIGN KEY (invoice_number) REFERENCES customers(invoice_number)
);
