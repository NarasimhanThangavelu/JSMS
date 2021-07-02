package com.narpavi.jsms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.narpavi.common.model.AwardDetails;
import com.narpavi.common.model.CustomerDetail;
import com.narpavi.common.model.QualificationDetails;
import com.narpavi.common.model.WorkExperienceDetails;
import com.narpavi.common.util.StringUtils;
import com.narpavi.jsms.util.CVHeader;

@Component
public class ExportPdf {
	
	private static final String EMPTY_STRING = " ";
	private static final String STRING_COLON = ": ";
	
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	private static Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	
	  public String pdfWriter(CustomerDetail customerDetail) throws Exception {
		
		String path = getPath(customerDetail.getFirstName());
		CVHeader header = new CVHeader();
		
		Document document = new Document();
		PdfWriter pw = PdfWriter.getInstance(document, new FileOutputStream(path));
		pw.setPageEvent(header);  
		document.open();
		header.setHeader("Resume");
		header.setFooter("www.narpavi.com");
		document.add(new Paragraph(30, "\u00a0"));
		
		addCustomerDetails(customerDetail, document);
		addQualifiationDetails(customerDetail, document);
		addAwardDetails(customerDetail, document);
		addExperienceDetails(customerDetail, document);
		
		document.close();
		return path;
	  }

	private void addCustomerDetails(CustomerDetail customerDetail, Document document) throws DocumentException {
		int custHeaderwidths[] = { 30,70 };
	    PdfPTable customerTable = new PdfPTable(2);
	    customerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	    customerTable.setWidths(custHeaderwidths);
	    customerTable.setWidthPercentage(98);
	    customerTable.getDefaultCell().setPadding(8);
	    
	    String fullName = customerDetail.getFirstName();
	    
	    if (!StringUtils.isBlank(customerDetail.getLastName())) {
	    	fullName += "," + customerDetail.getLastName();
	    }
	    customerTable.addCell(createParagraph("Name "));
	    customerTable.addCell(new Paragraph(STRING_COLON + fullName));
	    customerTable.addCell(createParagraph("DOB "));
	    customerTable.addCell(new Paragraph(STRING_COLON + customerDetail.getDobStr()));
	    customerTable.addCell(createParagraph("Email"));
	    customerTable.addCell(new Paragraph(STRING_COLON + customerDetail.getEmail()));
	    customerTable.addCell(createParagraph("Contact "));
	    customerTable.addCell(new Paragraph(STRING_COLON + customerDetail.getContactNumber()));
	    customerTable.addCell(createParagraph("Sex "));
	    customerTable.addCell(new Paragraph(STRING_COLON + customerDetail.getSex()));
	    customerTable.addCell(createParagraph("Married "));
	    customerTable.addCell(new Paragraph(STRING_COLON + (customerDetail.isMaritalStatus() == true ? "Yes" : "No")));
	    
	    customerTable.addCell(createParagraph("Address "));
	    customerTable.addCell(new Paragraph(STRING_COLON + getAddress(customerDetail)));
	    
	    document.add(customerTable);
	}

	private void addExperienceDetails(CustomerDetail customerDetail, Document document) throws DocumentException {
		
		if (null != customerDetail.getWorkExperienceDetails() &&  customerDetail.getWorkExperienceDetails().size() > 0) {
			Paragraph preface = new Paragraph();
		    addEmptyLine(preface, 1);
		    preface.add(new Paragraph("Experience Details : ", catFont));
			
		    document.add(preface);
			
			int expHeaderwidths[] = { 20, 30,20, 30 };
			
			PdfPTable experienceTable = new PdfPTable(4);
			experienceTable.setSpacingBefore(20f);
			experienceTable.setWidths(expHeaderwidths);
			experienceTable.setWidthPercentage(98);
			experienceTable.getDefaultCell().setPadding(5);
			
			for (WorkExperienceDetails workExperienceDetails : customerDetail.getWorkExperienceDetails()) {
				experienceTable.addCell(createParagraph("Employer Name"));
				experienceTable.addCell(EMPTY_STRING + workExperienceDetails.getEmployerName());
				experienceTable.addCell(createParagraph("Role"));
				experienceTable.addCell(EMPTY_STRING + workExperienceDetails.getDesignation());
				
				experienceTable.addCell(createParagraph("Start Date"));
				experienceTable.addCell(EMPTY_STRING + workExperienceDetails.getStartDateStr());
				experienceTable.addCell(createParagraph("End Date"));
				experienceTable.addCell(EMPTY_STRING + workExperienceDetails.getEndDateStr());
				
				experienceTable.addCell(createParagraph("City"));
				experienceTable.addCell(EMPTY_STRING + workExperienceDetails.getAddress().getCity());
				experienceTable.addCell(createParagraph("State"));
				experienceTable.addCell(EMPTY_STRING + workExperienceDetails.getAddress().getState());
				experienceTable.addCell(createParagraph("Job Description"));
				PdfPCell cell = new PdfPCell(new Paragraph(EMPTY_STRING + workExperienceDetails.getJobDesc()));
				cell.setColspan(3);
				experienceTable.addCell(cell);
			}
			document.add(experienceTable);	
		}
	}

	private void addAwardDetails(CustomerDetail customerDetail, Document document) throws DocumentException {
		
		if (null != customerDetail.getAwardDetails() && customerDetail.getAwardDetails().size() > 0) {
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
		    
			awardTable.addCell(createParagraph("Award Name"));
			awardTable.addCell(createParagraph("Institution"));
			awardTable.addCell(createParagraph("Description"));
			for (AwardDetails awardDetail : customerDetail.getAwardDetails()) {
				awardTable.addCell(awardDetail.getAwardName());
				awardTable.addCell(awardDetail.getInstitution());
				awardTable.addCell(awardDetail.getAwardDesc());
			}
			document.add(awardTable);
		}
	}

	private void addQualifiationDetails(CustomerDetail customerDetail, Document document) throws DocumentException {
		
		if (null != customerDetail.getQualificationDetails() && customerDetail.getQualificationDetails().size() > 0) {
			
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
		    
			qualificationTable.addCell(createParagraph("Qualification"));
			qualificationTable.addCell(createParagraph("Deparment"));
			qualificationTable.addCell(createParagraph("Institution"));
			qualificationTable.addCell(createParagraph("University/Board"));
			qualificationTable.addCell(createParagraph("Marks"));
		
			for (QualificationDetails detail : customerDetail.getQualificationDetails()) {
				qualificationTable.addCell(detail.getQualificationName());
				qualificationTable.addCell(detail.getDepartment());
				qualificationTable.addCell(detail.getInstitution());
				qualificationTable.addCell(detail.getUniversity());
				qualificationTable.addCell(String.valueOf(detail.getMarks()));
			}
			document.add(qualificationTable);
		}
	}

	private String getAddress(CustomerDetail customerDetail) {
		StringBuilder builder = new StringBuilder();
	    
	    if (!StringUtils.isBlank(customerDetail.getAddress().getAddressLine1())) {
	    	builder.append(customerDetail.getAddress().getAddressLine1());
	    	builder.append(",");
	    }
	    if (!StringUtils.isBlank(customerDetail.getAddress().getAddressLine2())) {
	    	builder.append(customerDetail.getAddress().getAddressLine2());
	    	builder.append(",");
	    }
	    
	    if (!StringUtils.isBlank(customerDetail.getAddress().getCity())) {
	    	builder.append(customerDetail.getAddress().getCity());
	    	builder.append(",");
	    }
	    
	    if (!StringUtils.isBlank(customerDetail.getAddress().getState())) {
	    	builder.append(customerDetail.getAddress().getState());
	    	builder.append(",");
	    }
	    
	    if (!StringUtils.isBlank(customerDetail.getAddress().getZip())) {
	    	builder.append(customerDetail.getAddress().getZip());
	    }
	    return builder.toString();
	}

	private Paragraph createParagraph(String name) {
		return new Paragraph(name, headerFont);
	}
  
	  private void addEmptyLine(Paragraph paragraph, int number) {
	      for (int i = 0; i < number; i++) {
	          paragraph.add(new Paragraph(EMPTY_STRING));
	      }
	  }
	  
	private String getPath(String fileName) {
		
		String folderName = "pdf";
	    Paths.get(FileSystemView.getFileSystemView()
	                .getHomeDirectory()
	                .getAbsolutePath(),
	              folderName).toFile().mkdir();
	    String basePath = System.getProperty("user.home") + "\\Desktop\\pdf\\";
	   
	    String newFile = fileName;
	    do{
	    newFile=newFile+ "_" +generateUniqueFileName() + "." + "_CV.pdf";
	    }
	    while(new File(basePath+newFile).exists());
		String path =  basePath+newFile;
		return path;
	}
	
	private String generateUniqueFileName() {
	    String filename = "";
	    long millis = System.currentTimeMillis();
	    String datetime = new Date().toGMTString();
	    datetime = datetime.replace(EMPTY_STRING, "");
	    datetime = datetime.replace(":", "");
//		    String rndchars = RandomStringUtils.randomAlphanumeric(16);
	    filename = "_1_" + millis;
	    return filename;
	}
/*	
	public String getCV(CustomerDetail customerDetail) {
		
		
		String path = getPath(customerDetail.getFirstName());
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter pw = PdfWriter.getInstance(document, new FileOutputStream(path));
			CVHeader header = new CVHeader();
			pw.setPageEvent(header);
			document.open();
			
			header.setHeader("Hello!");
//	        document.add(new Paragraph("Testing."));
	        document.newPage();
	        header.setHeader("There!");
//	        document.add(new Paragraph("Testing."));
	        document.add(new Paragraph(100, "\u00a0"));
	        PdfPTable customerTable = new PdfPTable(2);
	        customerTable.addCell(new Paragraph("Name : "));
	        customerTable.addCell(new Paragraph(customerDetail.getFirstName()));
	        
	        customerTable.setSpacingBefore(350f);
	        document.add(customerTable);
	        PdfPTable customerTable1 = new PdfPTable(2);
	        customerTable1.addCell(new Paragraph("Name1 : "));
	        customerTable1.addCell(new Paragraph(customerDetail.getFirstName()));
	        customerTable1.setSpacingAfter(550f);
	        document.add(customerTable1);
			PdfPTable table = new PdfPTable(5);
			table.addCell("Qualification");
			table.addCell("Deparment");
			table.addCell("Institution");
			table.addCell("University/Board");
			table.addCell("Marks");
			
				
			for (QualificationDetails detail : customerDetail.getQualificationDetails()) {
				table.addCell(detail.getQualificationName());
				table.addCell(detail.getDepartment());
				table.addCell(detail.getInstitution());
				table.addCell(detail.getUniversity());
				table.addCell(String.valueOf(detail.getMarks()));
			}
			
			
//			addTitlePage(document, customerDetail);
			Paragraph preface = new Paragraph();
			addEmptyLine(preface, 5);
			document.add(table);
			
			document.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
*/

}
