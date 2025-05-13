package print;

import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Print {
	
	public static void printInvoice(JPanel panelToPrint) {
	    PrinterJob printerJob = PrinterJob.getPrinterJob();
	    printerJob.setJobName("Invoice Print");

	    printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
	        if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

	        Graphics2D g2d = (Graphics2D) graphics;
	        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	        panelToPrint.paint(g2d);
	        return Printable.PAGE_EXISTS;
	    });

	    if (printerJob.printDialog()) {
	        try {
	            printerJob.print();
	        } catch (PrinterException ex) {
	            JOptionPane.showMessageDialog(null, "Print Failed: " + ex.getMessage());
	        }
	    }
	}


}
