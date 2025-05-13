package customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBConnection;

public class SearchPurchaseHistory extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchPurchaseHistory() {
        setTitle("Search Purchase History");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel();

        nameField = new JTextField(20);
        JButton searchBtn = new JButton("Search");

        topPanel.add(new JLabel("Customer Name:"));
        topPanel.add(nameField);
        topPanel.add(searchBtn);

        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        tableModel.setColumnIdentifiers(new String[] {
            "Invoice No", "Date", "Customer Name", "Total Amount", "Return Amount", "Final Bill"
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Button click
        searchBtn.addActionListener(e -> {
            String customerName = nameField.getText().trim();
            if (!customerName.isEmpty()) {
                fetchHistory(customerName);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a customer name.");
            }
        });

        // Double-click row to show item details
        resultTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && resultTable.getSelectedRow() != -1) {
                    String invoiceNo = resultTable.getValueAt(resultTable.getSelectedRow(), 0).toString();
                    showItemDetails(invoiceNo);
                }
            }
        });
    }

    private void fetchHistory(String name) {
        tableModel.setRowCount(0); // Clear old results

        try (Connection conn = DBConnection.getConnection()) {
            String query = """
                SELECT c.invoice_number, c.invoice_date, c.name,
                       s.total_amount, s.return_amount, s.final_bill
                FROM customers c
                JOIN invoice_summary s ON c.invoice_number = s.invoice_number
                WHERE c.name LIKE ?
                ORDER BY c.invoice_date DESC
            """;

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getString("invoice_number"),
                    rs.getDate("invoice_date"),
                    rs.getString("name"),
                    rs.getDouble("total_amount"),
                    rs.getDouble("return_amount"),
                    rs.getDouble("final_bill")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching history.", "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showItemDetails(String invoiceNo) {
        JFrame popup = new JFrame("Item Details - Invoice " + invoiceNo);
        popup.setSize(700, 300);
        popup.setLocationRelativeTo(this);

        String[] columnNames = {"Product", "HSN", "Pieces", "Net Weight", "Rate", "Labour", "Amount"};
        DefaultTableModel itemModel = new DefaultTableModel(columnNames, 0);
        JTable itemTable = new JTable(itemModel);

        try (Connection conn = DBConnection.getConnection()) {
            String itemQuery = "SELECT * FROM purchases WHERE invoice_number = ?";
            PreparedStatement ps = conn.prepareStatement(itemQuery);
            ps.setString(1, invoiceNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                itemModel.addRow(new Object[] {
                    rs.getString("product_name"),
                    rs.getString("hsn"),
                    rs.getInt("pieces"),
                    rs.getDouble("net_weight"),
                    rs.getDouble("rate"),
                    rs.getDouble("labour"),
                    rs.getDouble("amount")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load item details.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        popup.add(new JScrollPane(itemTable));
        popup.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchPurchaseHistory().setVisible(true));
    }
}
