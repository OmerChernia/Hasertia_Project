package il.cshaifasweng.OCSFMediatorExample.client.util.generators;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PDFGenerator {

    private static Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    public static void createPDF(String filePath, PieChart pieChart) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // כותרת ראשית
        Paragraph title = new Paragraph("Statistics Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // רווח בין הכותרת לטבלה
        document.add(new Paragraph(" "));

        // יצירת טבלה
        PdfPTable table = new PdfPTable(2); // מספר העמודות בטבלה
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // הגדרת כותרות הטבלה
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Category", subFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Value", subFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        // הוספת הנתונים לטבלה
        for (PieChart.Data dataItem : pieChart.getData()) {
            table.addCell(new PdfPCell(new Phrase(dataItem.getName(), smallBold)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(dataItem.getPieValue()), smallBold)));
        }

        // הוספת הטבלה למסמך
        document.add(table);
        document.close();
    }

    public static void createPDF(String filePath, BarChart<String, Number> barChart) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // כותרת ראשית
        Paragraph title = new Paragraph("Statistics Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // רווח בין הכותרת לטבלה
        document.add(new Paragraph(" "));

        // יצירת טבלה
        PdfPTable table = new PdfPTable(3); // מספר העמודות בטבלה
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // הגדרת כותרות הטבלה
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Cinema", subFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Day", subFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Complaints", subFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        // הוספת הנתונים לטבלה
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            String cinema = series.getName();
            for (XYChart.Data<String, Number> dataItem : series.getData()) {
                table.addCell(new PdfPCell(new Phrase(cinema, smallBold)));
                table.addCell(new PdfPCell(new Phrase(dataItem.getXValue(), smallBold)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dataItem.getYValue()), smallBold)));
            }
        }

        // הוספת הטבלה למסמך
        document.add(table);
        document.close();
    }
}
