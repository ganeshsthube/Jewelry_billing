package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import bill_calc.BillingCalculator;
import customer.Customer;
import customer.Invoice;
import customer.PurchaseItem;

public class DatabaseService {

    // Save or fetch customer by name
    public static int saveOrGetCustomer(Customer customer) throws SQLException {
        Connection conn = DBConnection.getConnection();

        // Check if customer already exists
        String checkQuery = "SELECT id FROM customers WHERE name = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setString(1, customer.getName());
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id"); // Existing customer ID
        }

        // Insert new customer
        String insertQuery = "INSERT INTO customers (name, address, state, phone, pan, gstin) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, customer.getName());
        insertStmt.setString(2, customer.getAddress());
        insertStmt.setString(3, customer.getState());
        insertStmt.setString(4, customer.getPhone());
        insertStmt.setString(5, customer.getPan());
        insertStmt.setString(6, customer.getGstin());

        insertStmt.executeUpdate();
        ResultSet keys = insertStmt.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    // Save full invoice
    public static void saveInvoice(Invoice invoice) throws SQLException {
        Connection conn = DBConnection.getConnection();
        conn.setAutoCommit(false); // Transaction

        try {
            int customerId = saveOrGetCustomer(invoice.getCustomer());

            // Insert invoice
            String invoiceSQL = "INSERT INTO invoices (invoice_number, customer_id, date, total_amount, gst, return_amount, final_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement invoiceStmt = conn.prepareStatement(invoiceSQL, Statement.RETURN_GENERATED_KEYS);
            invoiceStmt.setInt(1, invoice.getInvoiceNumber());
            invoiceStmt.setInt(2, customerId);
            invoiceStmt.setDate(3, new java.sql.Date(invoice.getDate().getTime()));
            invoiceStmt.setDouble(4, invoice.getTotalPurchaseAmount());
            invoiceStmt.setDouble(5, BillingCalculator.calculateGST(invoice.getTotalPurchaseAmount()));
            invoiceStmt.setDouble(6, invoice.getReturnAmount());
            invoiceStmt.setDouble(7, invoice.getFinalAmount());

            invoiceStmt.executeUpdate();
            ResultSet keys = invoiceStmt.getGeneratedKeys();
            keys.next();
            int invoiceId = keys.getInt(1);

            // Insert purchase items
            String itemSQL = "INSERT INTO purchases (invoice_id, product, hsn, pieces, net_weight, rate, labour, amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSQL);

            for (PurchaseItem item : invoice.getItems()) {
                itemStmt.setInt(1, invoiceId);
                itemStmt.setString(2, item.getProductName());
                itemStmt.setString(3, item.getHsnCode());
                itemStmt.setInt(4, item.getPieces());
                itemStmt.setDouble(5, item.getNetWeight());
                itemStmt.setDouble(6, item.getRate());
                itemStmt.setDouble(7, item.getLabourCost());
                itemStmt.setDouble(8, item.getAmount());
                itemStmt.addBatch();
            }

            itemStmt.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    // Search purchase history by customer name
    public static List<Invoice> searchPurchaseHistory(String customerName) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT i.* FROM invoices i " +
                     "JOIN customers c ON i.customer_id = c.id " +
                     "WHERE c.name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customerName);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Invoice invoice = new Invoice(
                rs.getInt("invoice_number"),
                rs.getDate("date"),
                null, // You can fetch customer info again if needed
                new ArrayList<>(), // Purchases will be fetched separately
                0, 0 // For simplicity in search
            );
            invoices.add(invoice);
        }

        return invoices;
    }
}

