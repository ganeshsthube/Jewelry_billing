//package ui;
//import javax.swing.*;
//import javax.swing.border.TitledBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//public class InvoiceSummaryPanel extends JPanel {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private JSpinner fromDatePicker;
//    private JSpinner toDatePicker;
//    private JButton fetchButton;
//
//    public InvoiceSummaryPanel() {
//        setLayout(new GridBagLayout());
//        setBorder(new TitledBorder("Invoice Summary Filter"));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//
//        // From Date
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        add(new JLabel("From Date:"), gbc);
//
//        gbc.gridx = 1;
//        fromDatePicker = createDatePicker();
//        add(fromDatePicker, gbc);
//
//        // To Date
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        add(new JLabel("To Date:"), gbc);
//
//        gbc.gridx = 1;
//        toDatePicker = createDatePicker();
//        add(toDatePicker, gbc);
//
//        // Fetch Button
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 2;
//        fetchButton = new JButton("Fetch Invoice Summary");
//        add(fetchButton, gbc);
//
//        fetchButton.addActionListener(this::onFetchClicked);
//    }
//
//    private JSpinner createDatePicker() {
//        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
//        JSpinner spinner = new JSpinner(model);
//        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
//        return spinner;
//    }
//
//    private void onFetchClicked(ActionEvent e) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String fromDate = sdf.format((Date) fromDatePicker.getValue());
//        String toDate = sdf.format((Date) toDatePicker.getValue());
//
//        // Replace this with your DB logic
//        JOptionPane.showMessageDialog(this, "Fetching invoices from " + fromDate + " to " + toDate);
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Invoice Summary");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setContentPane(new InvoiceSummaryPanel());
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//	
//}








package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import db.DBConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InvoiceSummaryPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable invoiceSummaryTable;
    private DefaultTableModel tableModel;
    private JSpinner fromDatePicker;
    private JSpinner toDatePicker;

    public InvoiceSummaryPanel() {
        setLayout(new BorderLayout());

        // Date pickers
        fromDatePicker = new JSpinner(new SpinnerDateModel());
        toDatePicker = new JSpinner(new SpinnerDateModel());
        fromDatePicker.setEditor(new JSpinner.DateEditor(fromDatePicker, "yyyy-MM-dd"));
        toDatePicker.setEditor(new JSpinner.DateEditor(toDatePicker, "yyyy-MM-dd"));

        JButton fetchButton = new JButton("Fetch Records");
        JButton exportButton = new JButton("Export to Excel");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("From:"));
        topPanel.add(fromDatePicker);
        topPanel.add(new JLabel("To:"));
        topPanel.add(toDatePicker);
        topPanel.add(fetchButton);
        topPanel.add(exportButton);

        add(topPanel, BorderLayout.NORTH);

        // Table to show invoice summaries
        tableModel = new DefaultTableModel();
        invoiceSummaryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(invoiceSummaryTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set table columns
        tableModel.addColumn("Invoice No");
        tableModel.addColumn("Customer Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Total Amount");
        tableModel.addColumn("Final Amount");

        // Button Actions
        fetchButton.addActionListener(this::fetchInvoiceSummary);
        exportButton.addActionListener(e -> exportInvoiceSummaryToExcel(invoiceSummaryTable));
    }

    private void fetchInvoiceSummary(ActionEvent e) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = sdf.format(fromDatePicker.getValue());
        String toDate = sdf.format(toDatePicker.getValue());

        tableModel.setRowCount(0); // Clear existing rows

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT invoice_number, total_amount, final_bill FROM invoice_summary WHERE date BETWEEN ? AND ?"
             )) {
            stmt.setString(1, fromDate);
            stmt.setString(2, toDate);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("invoice_number"),
//                        rs.getString("customer_name"),
//                        rs.getDate("date"),
                        rs.getDouble("total_amount"),
                        rs.getDouble("final_bill")
                };
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch invoices: " + ex.getMessage());
        }
    }

    private void exportInvoiceSummaryToExcel(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Invoice Summary");

                // Create Header Row
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                }

                // Write Table Data
                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object value = table.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Auto-size Columns
                for (int i = 0; i < table.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile() + ".xlsx")) {
                    workbook.write(fos);
                }

                JOptionPane.showMessageDialog(this, "Exported Successfully!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting to Excel: " + ex.getMessage());
            }
        }
    }
}

