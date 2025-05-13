package ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import db.DBConnection;

public class InvoiceAutoincrement {
	
	static String getNextInvoiceNumber() {
	    String nextInvoice = "INV001";
	    try (Connection conn = DBConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT invoice_number FROM customers ORDER BY id DESC LIMIT 1")) {

	        if (rs.next()) {
	            String lastInvoice = rs.getString("invoice_number");
	            int num = Integer.parseInt(lastInvoice.replaceAll("\\D+", "")) + 1;
	            nextInvoice = String.format("INV%03d", num);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return nextInvoice;
	}


}
