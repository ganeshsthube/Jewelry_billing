CREATE TABLE invoice_summary (
    invoice_number VARCHAR(50),
    total_amount DOUBLE,
    return_amount DOUBLE,
    final_bill DOUBLE,
    FOREIGN KEY (invoice_number) REFERENCES customers(invoice_number)
);
