package com.narpavi.jsms.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.narpavi.common.model.CustomerDetails;
import com.narpavi.common.model.QualificationDetails;

public class PDFConverter extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		response.setHeader("Content-Disposition", "attachment; fileName=\"CV.pdf\"");
		@SuppressWarnings("unchecked")
		List<CustomerDetails> customerDetails = (List<CustomerDetails>) model.get("allJobSeekers");
		
		Table table = new Table(5);
		table.addCell("Qualification");
		table.addCell("Deparment");
		table.addCell("Institution");
		table.addCell("University/Board");
		table.addCell("Marks");
		
		for (CustomerDetails customerDetail : customerDetails) {
			
			for (QualificationDetails detail : customerDetail.getCustomerDetail().getQualificationDetails()) {
				table.addCell(detail.getQualificationName());
				table.addCell(detail.getDepartment());
				table.addCell(detail.getInstitution());
				table.addCell(detail.getUniversity());
				table.addCell(String.valueOf(detail.getMarks()));
			}
		}
		document.add(table);
		
	}
}
