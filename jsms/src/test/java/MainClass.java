import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.narpavi.jsms.util.CVHeader;

public class MainClass {
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
	private static Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
	
  public static void main(String[] args) throws Exception {
	  
	CVHeader header = new CVHeader();
	
    Document document = new Document();
    PdfWriter pw = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\simma\\Desktop\\pdf\\2.pdf"));
    pw.setPageEvent(header);  
    document.open();
    header.setHeader("Resume");
    header.setFooter("wwww.narpavi.com");
    document.add(new Paragraph(30, "\u00a0"));
    int custHeaderwidths[] = { 30,70 };
    PdfPTable customerTable = new PdfPTable(2);
    customerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
    customerTable.setWidths(custHeaderwidths);
    customerTable.setWidthPercentage(98);
    customerTable.getDefaultCell().setPadding(8);
    
    customerTable.addCell(createParagraph("Name "));
    customerTable.addCell(new Paragraph(": Narasimhan"));
    customerTable.addCell(createParagraph("DOB "));
    customerTable.addCell(new Paragraph(": 05/03/1980"));
    customerTable.addCell(createParagraph("Email"));
    customerTable.addCell(new Paragraph(": simma.ofs@gmail.com"));
    customerTable.addCell(createParagraph("Contact "));
    customerTable.addCell(new Paragraph(": 3027660317"));
    customerTable.addCell(createParagraph("Sex "));
    customerTable.addCell(new Paragraph(": Male"));
    customerTable.addCell(createParagraph("Married "));
    customerTable.addCell(new Paragraph(": Yes"));
    customerTable.addCell(createParagraph("Address "));
    customerTable.addCell(new Paragraph(": 7 Church St, Mathalangudy, Kurumbagaram, Karaikal 609603"));
    
    document.add(customerTable);
    
    Paragraph qDetails = new Paragraph();
    addEmptyLine(qDetails, 1);
    qDetails.add(new Paragraph("Qualification Details : ", catFont));
    document.add(qDetails);
    
    int headerwidths[] = { 18, 20, 28, 28, 10 };
    
    PdfPTable qualificationTable = new PdfPTable(5);
    qualificationTable.setSpacingBefore(20f);
    qualificationTable.setWidths(headerwidths);
    qualificationTable.setWidthPercentage(98);
    qualificationTable.getDefaultCell().setPadding(5);
    qualificationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    qualificationTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
    
//    qualificationTable.setHeaderRows(2);

//    qualificationTable.getDefaultCell().setBorderWidth(1);
    
	qualificationTable.addCell(createParagraph("Qualification"));
	qualificationTable.addCell(createParagraph("Deparment"));
	qualificationTable.addCell(createParagraph("Institution"));
	qualificationTable.addCell(createParagraph("University/Board"));
	qualificationTable.addCell(createParagraph("Marks"));
	document.add(qualificationTable);
	
	Paragraph award = new Paragraph();
    addEmptyLine(award, 1);
    award.add(new Paragraph("Award Details : ", catFont));
    document.add(award);
	
	int awardHeaderwidths[] = { 20, 20, 70};
	
	PdfPTable awardTable = new PdfPTable(3);
	awardTable.setSpacingBefore(20f);
	awardTable.setWidths(awardHeaderwidths);
	awardTable.setWidthPercentage(98);
	awardTable.getDefaultCell().setPadding(5);
	awardTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	awardTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
    
//    qualificationTable.setHeaderRows(2);

//    qualificationTable.getDefaultCell().setBorderWidth(1);
    
	awardTable.addCell(createParagraph("Award Name"));
	awardTable.addCell(createParagraph("Institution"));
	awardTable.addCell(createParagraph("Description"));
	document.add(awardTable);
	
	
	Paragraph preface = new Paragraph();
    // We add one empty line
    addEmptyLine(preface, 1);
    // Lets write a big header
    preface.add(new Paragraph("Experience Details : ", catFont));
	
    document.add(preface);
	
	int expHeaderwidths[] = { 20, 30,20, 30 };
	
	PdfPTable experienceTable = new PdfPTable(4);
	experienceTable.setSpacingBefore(20f);
	experienceTable.setWidths(expHeaderwidths);
	experienceTable.setWidthPercentage(98);
	experienceTable.getDefaultCell().setPadding(5);
	
	experienceTable.addCell(createParagraph("Employer Name"));
	experienceTable.addCell(" HCL");
	experienceTable.addCell(createParagraph("Role"));
	experienceTable.addCell(" Lead Engineer");
	
	experienceTable.addCell(createParagraph("Start Date"));
	experienceTable.addCell(" 01/01/2013");
	experienceTable.addCell(createParagraph("End Date"));
	experienceTable.addCell(" 04/05/2015");
	
	experienceTable.addCell(createParagraph("City"));
	experienceTable.addCell(" Chennai");
	experienceTable.addCell(createParagraph("State"));
	experienceTable.addCell(" Tamil Nadu");
	experienceTable.addCell(createParagraph("Job Description"));
	 PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
	 cell.setColspan(3);
	 experienceTable.addCell(cell);
	 document.add(experienceTable);
    document.close();
  }

private static Paragraph createParagraph(String name) {
	return new Paragraph(name, headerFont);
}
  
  private static void addEmptyLine(Paragraph paragraph, int number) {
      for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
      }
  }
}