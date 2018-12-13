import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String DEST = "results/tables/nestedtable.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Main().createPdf(DEST, 250);
    }

    public void createPdf(String dest, int height) throws IOException, DocumentException {

        int width = height*2/3;
        Rectangle pageSize = new Rectangle(width, height);

        float height5 = height/20;
        float height15 = height5*3;
        float height25 = height5*5;

        float padding = height5/2;

        int largeFontSize = height/(450/12);
        int mediumFontSiz = height/(450/10);
        int smallFontSize = height/(450/8);


        Document document = new Document(pageSize,5,5,5,5);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        Font largeFont = FontFactory.getFont(FontFactory.HELVETICA, largeFontSize, Font.NORMAL);
        Font mediumFont = FontFactory.getFont(FontFactory.HELVETICA, mediumFontSiz, Font.NORMAL);
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, smallFontSize, Font.ITALIC);

        PdfPTable outerTable = new PdfPTable(1);

        //Section One
        Image image = Image.getInstance("drawable" + File.separator + "Royal Mail Top.jpg");
        PdfPCell labelTop = new PdfPCell(image, true);
        labelTop.setMinimumHeight(height15);
        labelTop.setPadding(padding);
        outerTable.addCell(labelTop);

        //Section Two
        PdfPTable innertable2 = new PdfPTable(2);
        innertable2.setWidths(new int[]{4,3});

        PdfPCell qrNumber = new PdfPCell(new Phrase("090-132-323-144-531-531", mediumFont));
        qrNumber.setPaddingLeft(padding);
        qrNumber.setMinimumHeight(height5);
        qrNumber.setVerticalAlignment(Element.ALIGN_BOTTOM);
        qrNumber.setBorder(Rectangle.NO_BORDER);
        innertable2.addCell(qrNumber);

        PdfPCell empty = new PdfPCell(new Phrase(""));
        empty.setBorder(Rectangle.NO_BORDER);
        innertable2.addCell(empty);

        BarcodeDatamatrix datamatrix = new BarcodeDatamatrix();
        datamatrix.generate("090-132-323-144-531-531");
        Image datamatrixImage = datamatrix.createImage();
        PdfPCell qrImage = new PdfPCell(datamatrixImage, true);
        qrImage.setBorder(Rectangle.NO_BORDER);
        qrImage.setFixedHeight(height25);
        qrImage.setPaddingLeft(height5);
        qrImage.setPaddingBottom(height5);
        qrImage.setPaddingRight(padding);
        qrImage.setPaddingTop(padding);
        innertable2.addCell(qrImage);

        Barcode128 barcode128 = new Barcode128();
        barcode128.setCode("501051467233");
        barcode128.setCodeType(Barcode128.CODE128);
        Image barcode128image = barcode128.createImageWithBarcode(contentByte, null, null);
        PdfPCell barcode = new PdfPCell(barcode128image, true);
        barcode.setBorder(Rectangle.NO_BORDER);
        barcode.setVerticalAlignment(Element.ALIGN_MIDDLE);
        barcode.setPaddingRight(padding);
        innertable2.addCell(barcode);

        outerTable.addCell(innertable2);

        //Section Three
        PdfPTable innertable3 = new PdfPTable(2);
        innertable3.setWidths(new int[]{3, 1});

        PdfPCell customerAddress = new PdfPCell(new Phrase(
                "Customer Address\nJoe Bloggs\n1 Some Lane\nSome Estate\nTowny Town\nLegion Region\nWD1 7TY", largeFont));
        customerAddress.setBorder(Rectangle.NO_BORDER);
        customerAddress.setPaddingLeft(padding);
        customerAddress.setVerticalAlignment(Element.ALIGN_MIDDLE);
        innertable3.addCell(customerAddress);

        PdfPCell senderAddress = new PdfPCell(new Phrase(
                "Sender Address: Joe Bloggs, 1 Some Lane, Some Estate, Towny Town, Legion Region, WD1 7TY", smallFont));
        senderAddress.setBorder(Rectangle.NO_BORDER);
        senderAddress.setRotation(90);
        senderAddress.setFixedHeight(height25);
        senderAddress.setPadding(padding);
        innertable3.addCell(senderAddress);

        outerTable.addCell(innertable3);

        //Section Four
        PdfPCell empty2 = new PdfPCell(new Phrase("Post Office - Scan the above right barcode", smallFont));
        empty2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        empty2.setPaddingLeft(padding);
        empty2.setMinimumHeight(height5);
        outerTable.addCell(empty2);

        //Section Five
        PdfPCell departmentRef = new PdfPCell(new Phrase("Department Reference", smallFont));
        departmentRef.setMinimumHeight(height15);
        departmentRef.setPaddingLeft(padding);
        departmentRef.setVerticalAlignment(Element.ALIGN_MIDDLE);
        outerTable.addCell(departmentRef);

        document.add(outerTable);
        document.close();
    }
}