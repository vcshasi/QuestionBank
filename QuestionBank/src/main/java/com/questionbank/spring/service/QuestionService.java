package com.questionbank.spring.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.questionbank.spring.dao.DepartmentDAO;
import com.questionbank.spring.dao.QuestionDAO;
import com.questionbank.spring.dao.QuestionTypeDao;
import com.questionbank.spring.dao.SubjectDAO;
import com.questionbank.spring.model.DepartmentCustom;
import com.questionbank.spring.model.QuestionCustom;
import com.questionbank.spring.model.QuestionType;
import com.questionbank.spring.model.Subject;

@Service("questionService")
@Transactional(readOnly = true)
public class QuestionService {

	private static final Logger logger = Logger.getLogger(QuestionService.class);

	@Autowired
	QuestionDAO questionDAO;

	@Autowired
	QuestionTypeDao questionTypeDao;

	@Autowired
	SubjectDAO subjectDAO;

	@Autowired
	DepartmentDAO departmentDAO;

	@Transactional(readOnly = false)
	public void addQuestions(QuestionCustom question) {
		getQuestionDAO().addQuestion(question);
	}

	@Transactional(readOnly = false)
	public String uploadQuestions(InputStream fileInputStream, int subjectId, String year, String semester) {
		String message = " Failed to upload file";
		QuestionCustom questionCustom = null;
		try {
			HSSFWorkbook workBook = new HSSFWorkbook(fileInputStream);
			List<QuestionType> questionTypes = getQuestionTypeDao().getQuestionTypeBySubjectId(subjectId);
			int noOfWorkbooks = workBook.getNumberOfSheets();
			QuestionType currentQuestionType = new QuestionType();
			int matchMarkCount = 0;
			if(noOfWorkbooks == questionTypes.size()) {
				for(int sheetNo = 0;sheetNo < noOfWorkbooks; sheetNo ++) {
					String sheetName = workBook.getSheetName(sheetNo);
					float mark = Float.parseFloat(sheetName);
					for(QuestionType questionType : questionTypes) {
						if(mark == questionType.getMark()) {
							matchMarkCount++;
						}
					}

				}
			}

			if(matchMarkCount == questionTypes.size()) {
				for(int sheetNo = 0;sheetNo < noOfWorkbooks; sheetNo ++) {

					String sheetName = workBook.getSheetName(sheetNo);
					float mark = Float.parseFloat(sheetName);


					for(QuestionType questionType : questionTypes) {
						if(mark == questionType.getMark()) {
							currentQuestionType = questionType;
						}
					}


					HSSFSheet sheet = workBook.getSheetAt(sheetNo);
					Iterator<Row> rowIterator = sheet.iterator();
					boolean isMCQ = false;
					if(currentQuestionType.getqType().equalsIgnoreCase("MCQ")) {
						isMCQ = true;
					}

					int questionTypeId = currentQuestionType.getQuestionTypeId();		
					deleteOldQuestionsByMarkandSubject(currentQuestionType, mark, subjectId);

					if(rowIterator.hasNext()) {
						Row row1 = rowIterator.next();
					}
					while (rowIterator.hasNext()) {
						questionCustom = new QuestionCustom();
						questionCustom.setYear(year);
						questionCustom.setSemester(semester);
						Row row = rowIterator.next();
						Iterator<Cell> cellIterator = row.cellIterator();
						DataFormatter formater = new DataFormatter();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();

							if(cell.getColumnIndex() == 1) {
								String unit = formater.formatCellValue(cell);
								unit = unit.trim();
								if(!unit.equals("") || !unit.isEmpty()) {
									questionCustom.setUnit(unit);
								}
							}

							if(cell.getColumnIndex() == 2) {
								String content = formater.formatCellValue(cell);
								content = content.trim();
								if(!content.equals("") || !content.isEmpty()) {
									questionCustom.setQuestionTypeId(questionTypeId);
									questionCustom.setQuestion(content);	
									questionCustom.setSubjectId(subjectId);
								}
							}

							if(cell.getColumnIndex() == 3) {
								String imageLink = formater.formatCellValue(cell);
								imageLink = imageLink.trim();
								if(!imageLink.equals("") || !imageLink.isEmpty()) {
									questionCustom.setQuestion(questionCustom.getQuestion() + "<image_link>" +  imageLink);
								} else {
									questionCustom.setQuestion(questionCustom.getQuestion());
								}
							}

							if(isMCQ == true) {
								if(cell.getColumnIndex() == 4) {
									String content = formater.formatCellValue(cell);
									if(!content.equals("") || !content.isEmpty()) {
										questionCustom.setOptionA(content);
									}
								}
								if(cell.getColumnIndex() == 5) {
									String content = formater.formatCellValue(cell);
									if(!content.equals("") || !content.isEmpty()) {
										questionCustom.setOptionB(content);
									}
								}
								if(cell.getColumnIndex() == 6) {
									String content = formater.formatCellValue(cell);
									if(!content.equals("") || !content.isEmpty()) {
										questionCustom.setOptionC(content);
									}
								}
								if(cell.getColumnIndex() == 7) {
									String content = formater.formatCellValue(cell);
									if(!content.equals("") || !content.isEmpty()) {
										questionCustom.setOptionD(content);
									}
								}
							}
						}
						getQuestionDAO().addQuestion(questionCustom);
					}
				}
				message = "File uploaded successfully";
			} else {
				message = "Please make sure the question template matches with the Sheets in uploaded exel file"; 	
			}

		}catch(Exception e)
		{
			message = "Error uploading question paper!!";
			logger.error("Exception in QuestionService.uploadQuestions", e);
			e.printStackTrace();
		}
		return message;
	}

	@Transactional(readOnly = false)
	private void deleteOldQuestionsByMarkandSubject(QuestionType questionType, float mark, int subjectId) {
		int questionTypeId = questionType.getQuestionTypeId();
		getQuestionDAO().deleteByQuestionTypeandSubjectId(questionTypeId, subjectId);
	}

	@Transactional(readOnly = false)
	public void deleteByQuestionType(QuestionType questionType) {
		int questionTypeId = questionType.getQuestionTypeId();
		getQuestionDAO().deleteByQuestionType(questionTypeId);
	}

	@Transactional(readOnly = false)
	public String prepareQuestionPaper(String subjectCode, int subjectId, String examType, String examYear, String year, String sem) {
		logger.info("QuestionService.prepareQuestionPaper method begins...");
		String message = "Unable to download file";

		try {
			Map<Float, List<String>> sortedQuestionsMap = getQuestionsBySubject(subjectId, year, sem, examType);
			if(sortedQuestionsMap.containsKey(0f)) {
				List<String> questionsRetrivalError = sortedQuestionsMap.get(0f);
				message = questionsRetrivalError.get(0) ;
			} else {
				message = createQuestionPaperPdf(sortedQuestionsMap, subjectId, examType, examYear, subjectCode, year, sem);
			} 
		}
		catch (Exception e) {
			logger.error("Exception occured in QuestionService.prepareQuestionPaper.. ", e);
			e.printStackTrace();
		}
		logger.info("QuestionService.prepareQuestionPaper method ends...");
		return message;
	}

	private String createQuestionPaperPdf( Map<Float, List<String>> sortedQuestionsMap, int subjectId, String examType, String examYear, String subjectCode, String year, String sem) {
		logger.info("QuestionService.createQuestionPaperPdf method begins...");
		Subject subject = getSubjectDAO().getSubjectById(subjectId);
		String questionPaperOnlyString =  subject.getSubjectName() +"_" + examType + "_" + subjectCode + "_" + year + "_" + sem + ".pdf";
		String questionPaperString = "C:\\temp\\" + questionPaperOnlyString;
		try {	
			Document questionPaperDocument = new Document();
			PdfWriter writer = PdfWriter.getInstance(questionPaperDocument, new FileOutputStream(questionPaperString));

			Font times11BU = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC | Font.UNDERLINE | Font.BOLD);
			Font times11NOR = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
			Font times11I = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC);

			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);

			questionPaperDocument.open();

			Paragraph registrationNumberpara = new Paragraph("Registration Number : _ _ _ _ _ _ _ _ _ ", times11I);
			registrationNumberpara.setAlignment(Element.ALIGN_RIGHT);
			questionPaperDocument.add(registrationNumberpara);

			Paragraph emptyPara = new Paragraph(" ");
			emptyPara.setAlignment(Element.ALIGN_MIDDLE);
			questionPaperDocument.add(emptyPara);


			DepartmentCustom department = getDepartmentDAO().getDepartmentById(subject.getDepartmentId());

			String questionTitle = department.getAssociatedCourse() + ", " + department.getDepartmentName() + "\u00a0" + examType + " EXAMINATIONS, " + examYear;
			Paragraph examSubjectPara = new Paragraph(questionTitle, times11NOR );
			examSubjectPara.setAlignment(Element.ALIGN_CENTER);
			questionPaperDocument.add(examSubjectPara);

			Paragraph coursePara = new Paragraph("COURSE TITLE : " + subject.getSubjectName(), times11NOR);
			questionPaperDocument.add(coursePara);

			Paragraph durationPara = new Paragraph("DURATION        : " + "3 Hours", times11NOR);
			questionPaperDocument.add(durationPara);

			Float totalMarks = 0f;
			for(Float key : sortedQuestionsMap.keySet()) {
				QuestionType questionType = getQuestionTypeDao().getQuestionByMarkAdSubject(key, subjectId);
				if(questionType.getqType().equalsIgnoreCase("EITHER_OR")) {
					totalMarks +=  (sortedQuestionsMap.get(key).size()/2) * key;
				} else if(questionType.getqType().equalsIgnoreCase("OPEN_CHOICE")){
					totalMarks +=  5 * key;
				} else {
					totalMarks +=  sortedQuestionsMap.get(key).size() * key;
				}
			}
			PdfContentByte contentByte = writer.getDirectContent();
			contentByte.beginText();
			contentByte.setFontAndSize(bf_times, 11);
			contentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, "COURSE CODE         : " + subjectCode, 420, 734, 0);
			contentByte.showTextAligned(PdfContentByte.ALIGN_LEFT, "MAXIMUM MARKS : " + totalMarks.toString().replaceAll("\\.?0*$", ""), 420, 716, 0);
			contentByte.endText();

			int questionNumber = 1;
			char sectionAlphabet = 'A';

			for(Float key : sortedQuestionsMap.keySet()) {
				QuestionType questionType = getQuestionTypeDao().getQuestionByMarkAdSubject(key, subjectId);

				Float sectionTotalMark = sortedQuestionsMap.get(key).size() * key;
				String markComputationText = null;
				if(!questionType.getqType().equalsIgnoreCase("EITHER_OR")) {
					markComputationText = "\t\t\t (" + sortedQuestionsMap.get(key).size() + "X" + key.toString().replaceAll("\\.?0*$", "") + " = " + sectionTotalMark.toString().replaceAll("\\.?0*$", "") + ")";
				}else if(questionType.getqType().equalsIgnoreCase("OPEN_CHOICE")){
					markComputationText = "\t\t\t (" + 5 + "X" + key.toString().replaceAll("\\.?0*$", "") + " = " + sectionTotalMark + ")";
				}
				else {
					sectionTotalMark = ((sortedQuestionsMap.get(key).size()/2) * key);
					markComputationText = "\t\t\t (" + (sortedQuestionsMap.get(key).size()/2) + "X" + key.toString().replaceAll("\\.?0*$", "") + " = " + sectionTotalMark.toString().replaceAll("\\.?0*$", "") + ")";
				}
				Paragraph markComputation = new Paragraph(markComputationText, times11I);
				markComputation.getFont().setFamily(BaseFont.TIMES_ROMAN);
				markComputation.setAlignment(Element.ALIGN_RIGHT);
				questionPaperDocument.add(markComputation);

				if(!questionType.getqType().equalsIgnoreCase("CASE_STUDY")) {
					Paragraph sectionPara = new Paragraph("Section " + sectionAlphabet , times11BU);
					sectionPara.getFont().setFamily(BaseFont.TIMES_ROMAN);
					sectionPara.setAlignment(Element.ALIGN_CENTER);
					questionPaperDocument.add(sectionPara);
				}

				sectionAlphabet ++;

				if(questionType.getqType().equalsIgnoreCase("OPEN_CHOICE")) {
					Paragraph allQuestionsPara = new Paragraph("Answer Any Five Questions" , times11BU);
					allQuestionsPara.getFont().setFamily(BaseFont.TIMES_ROMAN);
					allQuestionsPara.setAlignment(Element.ALIGN_LEFT);
					questionPaperDocument.add(allQuestionsPara);
				}else if(questionType.getqType().equalsIgnoreCase("MCQ")) {
					Paragraph allQuestionsPara = new Paragraph("Answer all Questions" , times11BU);
					allQuestionsPara.getFont().setFamily(BaseFont.TIMES_ROMAN);
					allQuestionsPara.setAlignment(Element.ALIGN_CENTER);
					questionPaperDocument.add(allQuestionsPara);
				}
				questionPaperDocument.add(new Paragraph(emptyPara));

				if(questionType.getqType().equalsIgnoreCase("EITHER_OR")) {
					char optionAlphabet = 'a';
					for(String questionString : sortedQuestionsMap.get(key)) {
						String imageURL = null;
						if(questionString.contains("<image_link>")) {
							String[] splitString = questionString.split("<image_link>");
							questionString = splitString[0];
							imageURL = splitString[1];
						}
						if(optionAlphabet == 'a') {
							if(imageURL != null) {
								String question = questionNumber + ". "+ optionAlphabet +") " + questionString + "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0";
								Paragraph questionPara = new Paragraph(question, times11NOR);
								questionPaperDocument.add(questionPara);
								optionAlphabet = 'b';

								if(!(new File(imageURL).exists())) {
									return "pdf creation failed as " + imageURL +" file not exists";
								} 
								Image questionImage = Image.getInstance(imageURL);
								questionImage = scaleImage(questionImage);
								questionImage.setAlignment(Element.ALIGN_CENTER);
								questionPaperDocument.add(questionImage);

								Paragraph ORPara = new Paragraph("(OR)", times11NOR);
								ORPara.setAlignment(Element.ALIGN_CENTER);
								questionPaperDocument.add(ORPara);
							} else {
								String question = questionNumber + ". "+ optionAlphabet +") " + questionString + "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0(OR)";
								Paragraph questionPara = new Paragraph(question, times11NOR);
								questionPaperDocument.add(questionPara);
								optionAlphabet = 'b';
							}
						} else {
							optionAlphabet = 'b';
							String question = "\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+ optionAlphabet +") " + questionString;
							Paragraph questionPara = new Paragraph(question, times11NOR);
							questionPara.setSpacingAfter(10);
							questionPaperDocument.add(questionPara);
							optionAlphabet = 'a';
							questionNumber++;
							if(imageURL != null) {
								if(!(new File(imageURL).exists())) {
									return "pdf creation failed as " + imageURL +" file not exists";
								}
								Image questionImage = Image.getInstance(imageURL);
								questionImage = scaleImage(questionImage);
								questionImage.setAlignment(Element.ALIGN_CENTER);
								questionPaperDocument.add(questionImage);
							}
						}

					}
				} else if(questionType.getqType().equalsIgnoreCase("MCQ")){
					for(String questionString : sortedQuestionsMap.get(key)) {
						if(questionString.contains("<qbank_options>")) {
							if(questionString.contains("<image_link>")) {
								String[] splitStringWithImage = questionString.split("<image_link>");
								String questionOnly = splitStringWithImage[0];
								String imageURLwithLink = splitStringWithImage[1];

								String[] splitStringWithOptions = imageURLwithLink.split("<qbank_options>");
								String imageURL = splitStringWithOptions[0];
								String optionsOnly = splitStringWithOptions[1];

								String question = questionNumber +". "+ questionOnly;
								Paragraph questionPara = new Paragraph(question, times11NOR);
								questionPaperDocument.add(questionPara);

								if(imageURL != null) {
									if(!(new File(imageURL).exists())) {
										return "pdf creation failed as " + imageURL +" file not exists";
									}
									Image questionImage = Image.getInstance(imageURL);
									questionImage = scaleImage(questionImage);
									questionImage.setAlignment(Element.ALIGN_CENTER);
									questionPaperDocument.add(questionImage);
								}

								Paragraph optionsPara = new Paragraph(optionsOnly, times11NOR);
								questionPaperDocument.add(optionsPara);
								questionNumber++;
							} else {
								questionString = questionString.replaceAll("<qbank_options>", "");
								String question = questionNumber +". "+ questionString;
								Paragraph questionPara = new Paragraph(question, times11NOR);
								questionPara.setSpacingAfter(8);
								questionPaperDocument.add(questionPara);
								questionNumber++;
							}
						}
					}
				} else {
					for(String questionString : sortedQuestionsMap.get(key)) {
						if(questionString.contains("<image_link>")) {
							String[] splitStringWithImage = questionString.split("<image_link>");
							String questionOnly = splitStringWithImage[0];
							String imageURLwithLink = splitStringWithImage[1];

							String[] splitStringWithOptions = imageURLwithLink.split("<qbank_options>");
							String imageURL = splitStringWithOptions[0];

							String question = questionNumber +". "+ questionOnly;
							Paragraph questionPara = new Paragraph(question, times11NOR);
							questionPaperDocument.add(questionPara);

							if(imageURL != null) {
								if(!(new File(imageURL).exists())) {
									return "pdf creation failed as " + imageURL +" file not exists";
								}
								Image questionImage = Image.getInstance(imageURL);
								questionImage = scaleImage(questionImage);
								questionImage.setAlignment(Element.ALIGN_CENTER);
								questionPaperDocument.add(questionImage);
							}
							questionNumber++;
						} else {
							questionString = questionString.replaceAll("<qbank_options>", "");
							String question = questionNumber +". "+ questionString;
							Paragraph questionPara = new Paragraph(question, times11NOR);
							questionPara.setSpacingAfter(8);
							questionPaperDocument.add(questionPara);
							questionNumber++;
						}
					}
				}
			}
			Paragraph starPara = new Paragraph("*****", times11NOR);
			starPara.setAlignment(Element.ALIGN_CENTER);
			questionPaperDocument.add(starPara);
			questionPaperDocument.close();
			logger.info("QuestionService.createQuestionPaperPdf method ends...");
			return "SUCCESS:file_full_Name:"+questionPaperString+":file_Name:"+questionPaperOnlyString;
		}catch(Exception e){
			logger.error("Exception in QuestionService.createQuestionPaperPdf " + e);
			e.printStackTrace();
		}
		logger.info("Failed to create pdf...!!");
		return "Failed to create PDF";
	}

	private Image scaleImage(Image questionImage) {
		if(questionImage.getHeight() > 150) {
			if(questionImage.getWidth() > 500) {
				questionImage.scaleAbsolute(500, 150);
			} else {
				questionImage.scaleAbsolute(questionImage.getWidth(), 150);
			}
		} else if(questionImage.getWidth() > 500) {
			questionImage.scaleAbsolute(500, questionImage.getHeight());
		}
		return questionImage;
	}

	private Map<Float, List<String>> getQuestionsBySubject(int subjectId, String year, String sem, String examType) {
		logger.info("QuestionService.getQuestionsBySubject method begins...");
		List<String> questionString = null;
		Map<Float, List<String>> sortedQuestionsMap = new TreeMap<Float, List<String>>();
		try {
			List<QuestionType> questionTypes = getQuestionTypeDao().getQuestionTypeBySubjectId(subjectId);
			if(questionTypes.size() > 0) {
				for (QuestionType questionType : questionTypes) {
					float questionMark = questionType.getMark();
					int noOfQuestions;
					if(questionType.getqType().equalsIgnoreCase("EITHER_OR")) {
						noOfQuestions = questionType.getNoOfQuestions() * 2;
					} else {
						noOfQuestions = questionType.getNoOfQuestions();
					}

					List<QuestionCustom> questionsByQuestionType = getRandomQuestionsByUnit(questionType, subjectId, noOfQuestions, year, sem, examType);
					int availableQuestions = questionsByQuestionType.size();
					if(availableQuestions >= noOfQuestions) {
						logger.info("Fetching " + noOfQuestions + " out of " + availableQuestions + " questions available in database for subject " + subjectId);
						//List<QuestionCustom> questions = getQuestionDAO().getRandomQuestionsByQuestionTypeandSubject(questionType.getQuestionTypeId(), subjectId, noOfQuestions);
						questionString = new ArrayList<String>();
						for(QuestionCustom question : questionsByQuestionType) {
							if(questionType.getqType().equalsIgnoreCase("MCQ")) {
								String mcqQuestion = question.getQuestion();
								mcqQuestion = mcqQuestion.concat("<qbank_options>");
								char optionCounter = 'a';
								if(question.getOptionA() != null ) {
									mcqQuestion = mcqQuestion.concat("\n\u00a0\u00a0("+ optionCounter ++ +") " + question.getOptionA());
								}
								if(question.getOptionB() != null ) {
									mcqQuestion = mcqQuestion.concat("\u00a0\u00a0(" + optionCounter ++ + ") " + question.getOptionB());
								}
								if(question.getOptionC() != null ) {
									mcqQuestion = mcqQuestion.concat("\u00a0\u00a0(" + optionCounter ++ + ") " + question.getOptionC());
								}
								if(question.getOptionD() != null ) {
									mcqQuestion = mcqQuestion.concat("\u00a0\u00a0(" + optionCounter ++ + ") " + question.getOptionD());
								}

								/*if(question.getOptionA() == null && question.getOptionB() == null && question.getOptionC() == null & question.getOptionD() == null ){
									logger.info("Pattern Mismatch in Database!!. Reset your pattern and try again");
									questionString = new ArrayList<String>();
									questionString.add("MCQ Pattern Mismatch in Database!!. Reset your pattern and try again!!");
									sortedQuestionsMap.put(0, questionString);
									return sortedQuestionsMap;
								}*/
								questionString.add(mcqQuestion);
							} else {
								questionString.add(question.getQuestion());
							}
						}
						sortedQuestionsMap.put(questionMark, questionString);
					} else {
						logger.info("Insufficient " + questionMark + " mark question in database for subject " + subjectId + " !!!");
						questionString = new ArrayList<String>();
						questionString.add("Insufficient questions in database to generate question paper");
						sortedQuestionsMap.put(0f, questionString);
						return sortedQuestionsMap;
					}
				}
			} else {
				logger.info("Question Pattern not set...!!");
				questionString = new ArrayList<String>();
				questionString.add("Please set the Question Pattern first");
				sortedQuestionsMap.put(0f, questionString);
			} 
		}
		catch (Exception e) {
			logger.error("Exception occured in QuestionService.getQuestionsBySubject ", e);
		}
		logger.info("QuestionService.getQuestionsBySubject method ends...");
		return sortedQuestionsMap;
	}

	private List<QuestionCustom> getRandomQuestionsByUnit(QuestionType questionType, int subjectId, int noOfQuestions, String year, String sem, String examType) {
		logger.info("QuestionService.getRandomQuestionsByUnit begins..");

		List<QuestionCustom> randomQuestions = new LinkedList<QuestionCustom>();
		List<String> distinctUnits = getQuestionDAO().getDistinctUnitByQuestionTypeandSubject(questionType.getQuestionTypeId(), subjectId,year, sem);

		if(distinctUnits.isEmpty()) {
			return randomQuestions;
		}

		Map<String, Integer> unitsWithAvailableQuestionsMap = new TreeMap<String, Integer>();
		Map<String, Integer> unitsWithRequestedQuestionsMap = new HashMap<String, Integer>();
		Map<String, Integer> remainingQuestionsMap = new HashMap<String, Integer>();
		List<Integer> remainingQuestionsList = new ArrayList<Integer>();
		Map<String, Integer> finalQuestionsMap = new TreeMap<String, Integer>();

		int questionInsertionCounter = 0;
		for(String distinctUnit: distinctUnits) {
			if(examType.equalsIgnoreCase("Internal")) {
				if(questionInsertionCounter < 3) {
					int questionsCount = getQuestionDAO().getQuestionCountByUnit(questionType.getQuestionTypeId(), subjectId,year, sem, distinctUnit);
					unitsWithAvailableQuestionsMap.put(distinctUnit, questionsCount);
				}
			} else {
				int questionsCount = getQuestionDAO().getQuestionCountByUnit(questionType.getQuestionTypeId(), subjectId,year, sem, distinctUnit);
				unitsWithAvailableQuestionsMap.put(distinctUnit, questionsCount);
			}
			questionInsertionCounter++;
		}

		int questionIncrement = 0;
		if(questionType.getqType().equalsIgnoreCase("EITHER_OR") == true) {
			questionIncrement = 2;
		} else {
			questionIncrement = 1;
		}
		int counter =0;
		int firstTime = 0;

		while(counter < noOfQuestions) {
			for(String unit : unitsWithAvailableQuestionsMap.keySet()) {
				if(firstTime == 0 && counter < noOfQuestions) {
					unitsWithRequestedQuestionsMap.put(unit, questionIncrement);
				} else if(counter < noOfQuestions) {
					unitsWithRequestedQuestionsMap.put(unit, unitsWithRequestedQuestionsMap.get(unit) + questionIncrement);
				}
				if(questionType.getqType().equalsIgnoreCase("EITHER_OR") == true) {
					counter+=2;
				}else {
					counter++;
				}
			}
			firstTime++;
		}


		for(String unit : unitsWithAvailableQuestionsMap.keySet()) {
			if(unitsWithRequestedQuestionsMap.containsKey(unit)) {
				remainingQuestionsMap.put(unit, (unitsWithAvailableQuestionsMap.get(unit) - unitsWithRequestedQuestionsMap.get(unit)));
			}
		}

		remainingQuestionsMap = sortByComparator(remainingQuestionsMap);
		int deficientQuestionsCount = 0;
		int deficientQuestions = 0;
		List<String> zeroUnits = new ArrayList<String>();
		for(String unit : remainingQuestionsMap.keySet()) {
			int finalValue = remainingQuestionsMap.get(unit);
			if(remainingQuestionsMap.get(unit) > 0) {
				remainingQuestionsList.add(finalValue);
			} else {
				zeroUnits.add(unit);
				deficientQuestionsCount++;
				deficientQuestions += finalValue;
			}
		}
		if(remainingQuestionsList == null || remainingQuestionsList.size() == 0) {
			return randomQuestions;
		}
		for(int i=0; deficientQuestions <0 ; i++) {
			if(i < remainingQuestionsList.size()) {
				if( remainingQuestionsList.get(i) > 0) {
					deficientQuestions+=1;
					remainingQuestionsList.set(i, remainingQuestionsList.get(i)-1);
				}
			} else {
				i=-1;
			}
		}

		int loopCount = 0;
		for(String unit : remainingQuestionsMap.keySet()) {
			if(loopCount < remainingQuestionsList.size()) {
				finalQuestionsMap.put(unit, unitsWithAvailableQuestionsMap.get(unit) - remainingQuestionsList.get(loopCount));
				loopCount++;
			}
		}

		if(zeroUnits.size() !=0) {
			for(String unit : zeroUnits) {
				finalQuestionsMap.put(unit, unitsWithAvailableQuestionsMap.get(unit));
			}
		}

		for(String unit : finalQuestionsMap.keySet()) {
			if(questionType.getqType().equalsIgnoreCase("MCQ") == true) {
				for(QuestionCustom question : getQuestionDAO().getRandomQuestionsByQuestionTypeandSubjectandUnitwithLimit(questionType.getQuestionTypeId(), subjectId, finalQuestionsMap.get(unit), unit, year, sem)) {
					randomQuestions.add(question);	
				}
			} else {
				List<QuestionCustom> questions = getQuestionDAO().getRandomQuestionsByQuestionTypeandSubjectandUnitwithLimit(questionType.getQuestionTypeId(), subjectId, finalQuestionsMap.get(unit), unit, year, sem);
				for(QuestionCustom question : questions) {
					randomQuestions.add(question);	
				}
			}
		}

		logger.info("QuestionService.getRandomQuestionsByUnit ends..");
		return randomQuestions;

	}

	private  Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
				new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
			{
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public QuestionDAO getQuestionDAO() {
		return questionDAO;
	}

	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	public QuestionTypeDao getQuestionTypeDao() {
		return questionTypeDao;
	}

	public void setQuestionTypeDao(QuestionTypeDao questionTypeDao) {
		this.questionTypeDao = questionTypeDao;
	}

	public SubjectDAO getSubjectDAO() {
		return subjectDAO;
	}

	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	public DepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}

	public void setDepartmentDAO(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}
}
