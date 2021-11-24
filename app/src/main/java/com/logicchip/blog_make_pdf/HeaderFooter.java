package com.logicchip.blog_make_pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Akhil Ashok
 * akhilashok123@gmail.com
 */
public class HeaderFooter extends PdfPageEventHelper {
    private PdfPTable footer;
    public HeaderFooter(PdfPTable footer) {
        this.footer = footer;
    }
    public void onEndPage(PdfWriter writer, Document document) {
        footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
    }
}
