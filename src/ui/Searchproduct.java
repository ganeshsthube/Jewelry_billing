package ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.ArrayList;

public class Searchproduct {

    private JComboBox<String> productBox;
    private java.util.List<String> allProducts = new ArrayList<>();

    public JComboBox<String> createSearchableComboBox() {
        productBox = new JComboBox<>();
        productBox.setEditable(true);

        // Load products
        Addnewproduct loader = new Addnewproduct();
        loader.loadProductsIntoComboBox(productBox);

        // Store all original items
        for (int i = 0; i < productBox.getItemCount(); i++) {
            allProducts.add(productBox.getItemAt(i));
        }

        JTextComponent editor = (JTextComponent) productBox.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String input = editor.getText().toLowerCase();
                    productBox.hidePopup();
                    productBox.removeAllItems();

                    for (String item : allProducts) {
                        if (item.toLowerCase().contains(input)) {
                            productBox.addItem(item);
                        }
                    }

                    editor.setText(input);
                    productBox.showPopup();
                });
            }
        });

        return productBox;
    }

    public JComboBox<String> getProductBox() {
        return productBox;
    }
}
