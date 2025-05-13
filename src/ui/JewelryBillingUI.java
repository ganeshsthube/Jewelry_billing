package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import customer.PurchaseItem;
import customer.SearchPurchaseHistory;
import db.DBConnection;
import print.*;

public class JewelryBillingUI extends JFrame {

    /**
	 * 
	 */
	private double totalAmount = 0;
	private double returnAmount = 0;
	private double finalBill = 0;

	private static final long serialVersionUID = 1L;
	private List<PurchaseItem> purchaseList = new ArrayList<>();
//	private double totalAmount = 0.0;
//	private double finalBill = 0.0;
//	private double returnAmount =0.0;
	

	public JewelryBillingUI() {
        setTitle("Jewelry Billing System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ Main container panel with vertical layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // ========== Top Static Info (like header) ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(3, 1));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Shop Info"));
//        headerPanel.add(new JLabel("SHREE SAI JEWELLERS"));
        
        JLabel shopNameLabel = new JLabel("Ghodke Saraf", SwingConstants.CENTER);
        shopNameLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Optional: make it bold & bigger
        headerPanel.add(shopNameLabel);
        
        headerPanel.add(new JLabel("Address: Kanhur Pathar, Ahmednagar, 414304"));
        headerPanel.add(new JLabel("Phone: 1234567890   GSTIN: XXYYZZ1234Z9A"));

        contentPanel.add(headerPanel);

        // ========== Customer Info Panel ==========
        JPanel customerPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        customerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField invoiceField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField panField = new JTextField();
        JTextField gstField = new JTextField();

        customerPanel.add(new JLabel("Name:")); customerPanel.add(nameField);
        customerPanel.add(new JLabel("Address:")); customerPanel.add(addressField);
        customerPanel.add(new JLabel("State:")); customerPanel.add(stateField);
        customerPanel.add(new JLabel("Phone:")); customerPanel.add(phoneField);
        customerPanel.add(new JLabel("Invoice No:")); customerPanel.add(invoiceField);
        customerPanel.add(new JLabel("Date:")); customerPanel.add(dateField);
        customerPanel.add(new JLabel("PAN No:")); customerPanel.add(panField);
        customerPanel.add(new JLabel("GSTIN:")); customerPanel.add(gstField);

        contentPanel.add(customerPanel);
        
        
        
//        auto generated invoice number
//        String generatedInvoiceNumber = "INV-" + System.currentTimeMillis();
//        invoiceField.setText(generatedInvoiceNumber);
        
        
        
        
        
        
        
        

        // ========== Purchase Panel ==========
        JPanel purchasePanel = new JPanel(new BorderLayout());
        purchasePanel.setBorder(BorderFactory.createTitledBorder("Purchase Details"));
//        purchasePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JPanel entryPanel = new JPanel(new GridLayout(2, 8, 5, 5));
        
        
        
        Searchproduct dropdown = new Searchproduct();
        JComboBox<String> searchPanel = dropdown.createSearchableComboBox();
        purchasePanel.add(searchPanel); // Or wherever you’re placing it
        JComboBox<String> productBox = dropdown.getProductBox();
//        purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));


        
//        String[] products = { "Gold Ring", "Necklace", "Bracelet", "Earrings" };
//        JComboBox<String> productBox = new JComboBox<>(products);
        
//        JComboBox<String> productBox = new JComboBox<>();
//        Addnewproduct addnewproduct = new Addnewproduct();
//		addnewproduct.loadProductsIntoComboBox(productBox);
        
        
        
        JTextField hsnField = new JTextField();
        JTextField piecesField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField rateField = new JTextField();
        JTextField labourField = new JTextField();
        JTextField amountField = new JTextField();
        amountField.setEditable(false);
        JButton addItemBtn = new JButton("Add Item");

        entryPanel.add(new JLabel("Product"));
        entryPanel.add(new JLabel("HSN"));
        entryPanel.add(new JLabel("Pieces"));
        entryPanel.add(new JLabel("Net Weight"));
        entryPanel.add(new JLabel("Rate"));
        entryPanel.add(new JLabel("Labour"));
        entryPanel.add(new JLabel("Amount"));
        entryPanel.add(new JLabel(""));

        entryPanel.add(productBox);
        entryPanel.add(hsnField);
        entryPanel.add(piecesField);
        entryPanel.add(weightField);
        entryPanel.add(rateField);
        entryPanel.add(labourField);
        entryPanel.add(amountField);
        entryPanel.add(addItemBtn);

        String[] columnNames = { "Product", "HSN", "Pieces", "Net Weight", "Rate", "Labour", "Amount" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable purchaseTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(purchaseTable);
        tableScroll.setPreferredSize(new Dimension(900, 150)); // Adjustable starting height

        purchasePanel.add(entryPanel, BorderLayout.NORTH);
        purchasePanel.add(tableScroll, BorderLayout.CENTER);

        contentPanel.add(purchasePanel);
        
        purchaseList.clear();

        
        

        for (PurchaseItem item : purchaseList) {
            totalAmount += item.getAmount();
        }
        
        
        
        
     // ========== Return Jewelry Section ==========
        JPanel returnPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        returnPanel.setBorder(BorderFactory.createTitledBorder("Old Jewelry Return"));
        returnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JTextField returnWeightField = new JTextField();
        JTextField returnRateField = new JTextField();
        JTextField returnAmountField = new JTextField();
        returnAmountField.setEditable(false);

        JButton calculateReturnBtn = new JButton("Calculate Return");

        returnPanel.add(new JLabel("Return Weight:"));
        returnPanel.add(new JLabel("Return Rate:"));
        returnPanel.add(new JLabel("Return Amount:"));
        returnPanel.add(returnWeightField);
        returnPanel.add(returnRateField);
        returnPanel.add(returnAmountField);

        contentPanel.add(returnPanel);
        contentPanel.add(calculateReturnBtn);
        
        
        
        
        
     // ===== Final Calculation Section =====
        JPanel finalPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        finalPanel.setBorder(BorderFactory.createTitledBorder("Final Bill Summary"));

        JTextField totalAmountField = new JTextField();
        totalAmountField.setEditable(false);

        JTextField finalBillField = new JTextField();
        finalBillField.setEditable(false);

        JTextField amountInWordsField = new JTextField();
        amountInWordsField.setEditable(false);

        JButton calculateFinalBtn = new JButton("Calculate Final Bill");

        finalPanel.add(new JLabel("Total Purchase Amount:"));
        finalPanel.add(totalAmountField);

        finalPanel.add(new JLabel("Final Bill (with 3% GST - Return):"));
        finalPanel.add(finalBillField);

        finalPanel.add(new JLabel("Amount in Words:"));
        finalPanel.add(amountInWordsField);

        contentPanel.add(finalPanel);
        contentPanel.add(calculateFinalBtn);
        
        

        
        
        //buttons section
        
     // Your bottom buttons area
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton addButton = new JButton("Add Item");
        JButton saveButton = new JButton("Save");
        JButton printButton = new JButton("Print");
        JButton clearButton = new JButton("Clear");
        JButton viewHistoryBtn = new JButton("Purchase History");
        JButton viewSummaryButton = new JButton("Invoice Summary");
        

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(printButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(viewHistoryBtn);
        buttonPanel.add(viewSummaryButton);
        
        // Add this panel to your frame
        add(buttonPanel, BorderLayout.SOUTH); // or wherever you're placing buttons
        
        
        
        
        
        
        
        
        
        

        
        
        

        // ✅ Wrap in Scroll Pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        
        
        
        // ====== Button Action ======

        
        
//      BUTTON FOR RETURN JEWELARY ADD ITOM
        
      calculateReturnBtn.addActionListener(e -> {
          try {
              double returnWeight = Double.parseDouble(returnWeightField.getText());
              double returnRate = Double.parseDouble(returnRateField.getText());
              double returnAmount = returnWeight * returnRate;
              returnAmountField.setText(String.format("%.2f", returnAmount));
          } catch (NumberFormatException ex) {
              JOptionPane.showMessageDialog(this, "Enter valid numeric values for return jewelry.", "Input Error", JOptionPane.ERROR_MESSAGE);
          }
      });
      
      
      
      
      //        calculate final bill
      calculateFinalBtn.addActionListener(e -> {
          try {
          	
          	totalAmount = 0; // remove 'double'
          	for (int i = 0; i < tableModel.getRowCount(); i++) {
          	    totalAmount += Double.parseDouble(tableModel.getValueAt(i, 6).toString());
          	}
          	returnAmount = 0;
          	try {
          	    returnAmount = Double.parseDouble(returnAmountField.getText());
          	} catch (NumberFormatException ignored) {
          	    
          	}

          	double gst = totalAmount * 0.03;
          	finalBill = totalAmount + gst - returnAmount;

              totalAmountField.setText(String.format("%.2f", totalAmount));
              finalBillField.setText(String.format("%.2f", finalBill));
              amountInWordsField.setText(convertToWords((long) finalBill));

          } catch (Exception ex) {
              JOptionPane.showMessageDialog(this, "Error calculating final bill.", "Error", JOptionPane.ERROR_MESSAGE);
          }
      });
      
      
        
        
        // logic for add purchase itom .
        addItemBtn.addActionListener(e -> {
            try {
                String productName = (String) productBox.getSelectedItem();
                String hsn = hsnField.getText();
                int pieces = Integer.parseInt(piecesField.getText());
                double weight = Double.parseDouble(weightField.getText());
                double rate = Double.parseDouble(rateField.getText());
                double labour = Double.parseDouble(labourField.getText());

                // Create PurchaseItem and calculate amount
                PurchaseItem item = new PurchaseItem(productName, hsn, pieces, weight, rate, labour);
                purchaseList.add(item); // Add to the list

                // Add row to table
                tableModel.addRow(new Object[]{
                    item.getProductName(),
                    item.getHsnCode(),
                    item.getPieces(),
                    item.getNetWeight(),
                    item.getRate(),
                    item.getLabourCost(),
                    item.getAmount()
                });

                // Show calculated amount in field
                amountField.setText(String.format("%.2f", item.getAmount()));

                // Optionally clear fields for next entry
                hsnField.setText("");
                piecesField.setText("");
                weightField.setText("");
                rateField.setText("");
                labourField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid input for all fields.");
            }
        });

        
        
      
        
//        to save data to db 
        
        saveButton.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false); // So we can rollback if anything fails

                // 1. Save Customer Info
                String insertCustomer = "INSERT INTO customers (invoice_number, name, address, phone, state, pan_number, gstin, invoice_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertCustomer)) {
                    ps.setString(1, invoiceField.getText());
                    ps.setString(2, nameField.getText());
                    ps.setString(3, addressField.getText());
                    ps.setString(4, phoneField.getText());
                    ps.setString(5, stateField.getText());
                    ps.setString(6, panField.getText());
                    ps.setString(7, gstField.getText());
                    ps.setDate(8, java.sql.Date.valueOf(LocalDate.now())); // or use a date picker
                    ps.executeUpdate();
                }

                // 2. Save Purchases
                String insertPurchase = "INSERT INTO purchases (invoice_number, product_name, hsn, pieces, net_weight, rate, labour, amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertPurchase)) {
//                    PurchaseItem[] purchaseList;gyf yugyugyt
					for (PurchaseItem item : purchaseList) {
                        ps.setString(1, invoiceField.getText());
                        ps.setString(2, item.getProductName());
                        ps.setString(3, item.getHsnCode());   //getHsn
                        ps.setInt(4, item.getPieces());
                        ps.setDouble(5, item.getNetWeight());
                        ps.setDouble(6, item.getRate());
                        ps.setDouble(7, item.getLabourCost());
                        ps.setDouble(8, item.getAmount());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

//                 3. Save Final Summary
                String insertSummary = "INSERT INTO invoice_summary (invoice_number, total_amount, return_amount, final_bill) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSummary)) {
                    ps.setString(1, invoiceField.getText());
                    ps.setDouble(2, totalAmount);
                    ps.setDouble(3, returnAmount);
                    ps.setDouble(4, finalBill);
                    ps.executeUpdate();
                }

                conn.commit();
                JOptionPane.showMessageDialog(this, "Invoice saved successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving invoice: " + ex.getMessage());
            }
        });
        
        
        
        

        
        
        
        
//        add Item button (to add new products)
        addButton.addActionListener(e -> {
            String newProduct = JOptionPane.showInputDialog(this, "Enter new product name:");
            if (newProduct != null && !newProduct.trim().isEmpty()) {
                newProduct = newProduct.trim();

                // Check if already in DB
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE name = ?")) {

                    checkStmt.setString(1, newProduct);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Product already exists.");
                        return;
                    }

                    // Insert new product
                    try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO products (name) VALUES (?)")) {
                        insertStmt.setString(1, newProduct);
                        insertStmt.executeUpdate();
                        productBox.addItem(newProduct);
                        JOptionPane.showMessageDialog(this, "Product \"" + newProduct + "\" added.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage());
                }
            }
        });


        
        
        
        
//        clear button Action 
        
        clearButton.addActionListener(e -> {
            // Clear customer fields
            nameField.setText("");
            addressField.setText("");
            phoneField.setText("");
            stateField.setText("");
            panField.setText("");
            gstField.setText("");

            // Clear purchase fields
            hsnField.setText("");
            piecesField.setText("");
            weightField.setText("");
            rateField.setText("");
            labourField.setText("");
            amountField.setText("");

            // Clear return fields
            returnWeightField.setText("");
            returnRateField.setText("");
            returnAmountField.setText("");

            // Clear summary fields
            totalAmountField.setText("");
            finalBillField.setText("");
            amountInWordsField.setText("");

            // Clear purchase table
            tableModel.setRowCount(0);
            purchaseList.clear();

            // Set new auto-incremented invoice number
            invoiceField.setText(InvoiceAutoincrement.getNextInvoiceNumber());
            invoiceField.setEditable(false);
        });
        
        
        
        //  search invoice summary.
        viewHistoryBtn.addActionListener(e -> new SearchPurchaseHistory().setVisible(true));
        
        
        
        // show invoice summary 
        viewSummaryButton.addActionListener(e -> {
            InvoiceSummaryPanel summaryPanel = new InvoiceSummaryPanel();

            JFrame summaryFrame = new JFrame("Invoice Summary");
            summaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            summaryFrame.setSize(800, 600);
            summaryFrame.setLocationRelativeTo(null);
            summaryFrame.add(summaryPanel);
            summaryFrame.setVisible(true);
        });
        
        
//      print button logic
        
      printButton.addActionListener(e -> Print.printInvoice(contentPanel));  // Assuming contentPanel holds the full bill
//      contentPanel.add(printButton);
      
        setVisible(true);
    }

	
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JewelryBillingUI::new);
    }
    
    
    
    
 // Convert number to words (simple version for INR)
    public static String convertToWords(long number) {
        if (number == 0) return "Zero";

        String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                           "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                           "Eighteen", "Nineteen" };

        String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

        StringBuilder result = new StringBuilder();

        if (number / 100000 > 0) {
            result.append(convertToWords(number / 100000)).append(" Lakh ");
            number %= 100000;
        }

        if (number / 1000 > 0) {
            result.append(convertToWords(number / 1000)).append(" Thousand ");
            number %= 1000;
        }

        if (number / 100 > 0) {
            result.append(convertToWords(number / 100)).append(" Hundred ");
            number %= 100;
        }

        if (number > 0) {
            if (number < 20) {
                result.append(units[(int) number]);
            } else {
                result.append(tens[(int) number / 10]);
                if (number % 10 > 0) {
                    result.append(" ").append(units[(int) number % 10]);
                }
            }
        }

        return result.toString().trim() ;  //+ " Rupees Only"
    }
    
    
    
    
}





