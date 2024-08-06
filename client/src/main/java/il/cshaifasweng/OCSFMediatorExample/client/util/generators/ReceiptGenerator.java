package il.cshaifasweng.OCSFMediatorExample.client.util.generators;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ReceiptGenerator {

    private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.BLACK);
    private static Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    private static Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

    public static void createReceiptPDF(String filePath, String totalPayment, String refNumber, String paymentTime,
                                        String paymentMethod, String senderName) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // כותרת ראשית
        Paragraph title = new Paragraph("Payment Success!", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // רווח בין הכותרת לטקסט הבא
        document.add(new Paragraph(" "));

        // טקסט נוסף
        Paragraph successText = new Paragraph("Your payment has been successfully done.", normalFont);
        successText.setAlignment(Element.ALIGN_CENTER);
        document.add(successText);

        // רווח בין הטקסט לפרטי התשלום
        document.add(new Paragraph(" "));

        // סכום תשלום כולל
        Paragraph totalPaymentParagraph = new Paragraph("Total Payment", boldFont);
        totalPaymentParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(totalPaymentParagraph);

        Paragraph totalPaymentAmount = new Paragraph(totalPayment, new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK));
        totalPaymentAmount.setAlignment(Element.ALIGN_CENTER);
        document.add(totalPaymentAmount);

        // רווח בין סכום התשלום לפרטים הנוספים
        document.add(new Paragraph(" "));

        // יצירת טבלה לפרטי התשלום
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        addTableCell(table, "Ref Number", refNumber);
        addTableCell(table, "Payment Time", paymentTime);
        addTableCell(table, "Payment Method", paymentMethod);
        addTableCell(table, "Sender Name", senderName);

        document.add(table);

        // רווח בין הטבלה לכפתור ה-PDF
        document.add(new Paragraph(" "));

        // יצירת כפתור הורדת PDF
        Paragraph pdfReceipt = new Paragraph("Get PDF Receipt", boldFont);
        pdfReceipt.setAlignment(Element.ALIGN_CENTER);
        document.add(pdfReceipt);

        document.close();
    }

    private static void addTableCell(PdfPTable table, String key, String value) {
        PdfPCell keyCell = new PdfPCell(new Phrase(key, normalFont));
        keyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        keyCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(keyCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, boldFont));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }
}
