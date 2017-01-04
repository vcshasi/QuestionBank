package com.tests;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class Test {
	public static void main(String[] args) {
		String questionPaperString = "C://temp//QuestionPaperDesign.pdf";
		try {	
			Document questionPaperDocument = new Document();
			PdfWriter writer = PdfWriter.getInstance(questionPaperDocument, new FileOutputStream(questionPaperString));
			questionPaperDocument.open();

			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);

			Paragraph registrationNumberpara = new Paragraph("Registration Number : _ _ _ _ _ _ _ _ _ ");
			registrationNumberpara.setAlignment(Element.ALIGN_RIGHT);
			registrationNumberpara.getFont().setFamily(BaseFont.TIMES_ROMAN);
			registrationNumberpara.getFont().setStyle(Font.ITALIC);
			questionPaperDocument.add(registrationNumberpara);

			Paragraph examSubjectPara = new Paragraph("{DEPARTMENT} DEGREE EXAMINATIONS, " + new SimpleDateFormat("MMM - YYYY").format(Calendar.getInstance().getTime()).toUpperCase() );
			examSubjectPara.setAlignment(Element.ALIGN_CENTER);
			examSubjectPara.getFont().setFamily(BaseFont.TIMES_ROMAN);
			questionPaperDocument.add(examSubjectPara);

			Paragraph coursePara = new Paragraph("COURSE TITLE : " + "{SUBJECT_NAME}");
			coursePara.getFont().setFamily(BaseFont.TIMES_ROMAN);
			questionPaperDocument.add(coursePara);

			Paragraph durationPara = new Paragraph("DURATION        : " + "{3 Hours}");
			durationPara.getFont().setFamily(BaseFont.TIMES_ROMAN);
			questionPaperDocument.add(durationPara);

			PdfContentByte contentByte = writer.getDirectContent();
			contentByte.beginText();
			contentByte.setFontAndSize(bf_times, 12);
			contentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, "COURSE CODE         :", 420, 753, 0);
			contentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, "MAXIMUM MARKS :", 420, 733, 0);
			contentByte.endText();

			Paragraph emptyPara = new Paragraph("");
			questionPaperDocument.add(emptyPara);

			Map<Integer, List<String>> sortedQuestionsMap = null;
			
			for(Integer key : sortedQuestionsMap.keySet()) {
				//for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) {
				char alphabet = 'A';
				int questionNumber = 1;
				Paragraph sectionPara = new Paragraph("Section " + alphabet );
				sectionPara.getFont().setFamily(BaseFont.TIMES_ROMAN);
				sectionPara.setAlignment(Element.ALIGN_CENTER);
				sectionPara.getFont().setStyle(Font.ITALIC);
				questionPaperDocument.add(sectionPara);
				alphabet ++;

				if(key != 1) {
					int count = 2;
					for(String questionString : sortedQuestionsMap.get(key)) {
						if(count % 2 == 0) {
							questionPaperDocument.add(new Paragraph(questionNumber + ". "+ "a)" + questionString));
							questionPaperDocument.add(new Paragraph(" (OR) "));
						} else {
							questionPaperDocument.add(new Paragraph(questionNumber + ". "+ "b)" + questionString));
						}
						questionNumber++;

					}
				} else {
					for(String questionString : sortedQuestionsMap.get(key)) {
						questionPaperDocument.add(new Paragraph(questionNumber +". "+ questionString));
						questionNumber++;
					}

				}
				questionPaperDocument.add(new Paragraph(" "));

			}

			questionPaperDocument.close();

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
