package ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import db.DBConnection;

public class Addnewproduct {
	void loadProductsIntoComboBox(JComboBox<String> comboBox) {
	    comboBox.removeAllItems(); // Clear existing items

	    try (Connection conn = DBConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT name FROM products ORDER BY name")) {

	        while (rs.next()) {
	            comboBox.addItem(rs.getString("name"));
	        }

	    } catch (SQLException e) {
	        JOptionPane.showInputDialog(this, "Failed to load products: " + e.getMessage());
	    }
	}
	
	
	// ✅ New method to return list of product names for search
    public List<String> getProductList() {
        List<String> productNames = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM products ORDER BY name")) {

            while (rs.next()) {
                productNames.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching product list: " + e.getMessage());
        }

        return productNames;
    }


}
