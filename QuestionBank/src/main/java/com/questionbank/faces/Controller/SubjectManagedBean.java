package com.questionbank.faces.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.questionbank.spring.model.QuestionType;
import com.questionbank.spring.model.Subject;
import com.questionbank.spring.service.QuestionService;
import com.questionbank.spring.service.QuestionTypeService;
import com.questionbank.spring.service.SubjectService;

@ManagedBean(name="subMB")
@ViewScoped
public class SubjectManagedBean implements Serializable{

	private static final Logger logger = Logger.getLogger(SubjectManagedBean.class);

	private static final long serialVersionUID = 1L;

	private String selectedExamType;
	private String examYear;
	private int selectedSubjectId;
	private String subjectName;
	private int departmentId;
	private String message;
	private String subjectCode;
	private StreamedContent questionPaperFile;
	private int selectedYear;
	private int selectedSem;
	private List<QuestionType> questionTypesList;
	private int fileUploadCount;

	@ManagedProperty(value="#{subjectService}")
	SubjectService subjectService;

	@ManagedProperty(value="#{questionService}")
	QuestionService questionService;

	@ManagedProperty(value="#{questionTypeService}")
	QuestionTypeService questionTypeService;

	private List<Subject> subjectList;
	private List<QuestionType> questionTypeList;

	public void onRowEdit(RowEditEvent event) {  
		Subject subject = (Subject) event.getObject();
		try {
			subjectService.updateSubject(subject);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Course Updated!",
					subject.getSubjectName() +" Updated.."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Course Updation Failed!",
					subject.getSubjectName() +" Not Updated.."));
		} 
	}  

	public void onRowCancel(RowEditEvent event) {   
	}

	public void addSubject() {
		if(getSubjectName().isEmpty() || getSubjectName() == null || (getSubjectName().length() > 45)) {
			logger.info("Course cannot be empty");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Course Subject!",
					"Course cannot be empty.."));
		} else {
			try {
				Subject subject = new Subject();
				subject.setDepartmentId(getDepartmentId());
				subject.setSubjectName(getSubjectName());
				subjectService.addSubject(subject);

				setDefaultQuestionPattern(subject.getSubjectId());

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Course Added!",
						getSubjectName() + " added to database.."));
				subjectName = "";
				subjectCode = "";
				getSubjectByDepartmentId();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setDefaultQuestionPattern(int subjectId) {
		QuestionType questionType = new QuestionType();
		questionType.setMark(1);
		questionType.setNoOfQuestions(15);
		questionType.setSubjectId(subjectId);
		questionType.setqType("MCQ");
		questionTypeService.addQuestionType(questionType);

		questionType = new QuestionType();
		questionType.setMark(3);
		questionType.setNoOfQuestions(5);
		questionType.setSubjectId(subjectId);
		questionType.setqType("EITHER_OR");
		questionTypeService.addQuestionType(questionType);

		questionType = new QuestionType();
		questionType.setMark(6);
		questionType.setNoOfQuestions(5);
		questionType.setSubjectId(subjectId);
		questionType.setqType("EITHER_OR");
		questionTypeService.addQuestionType(questionType);
	}

	public String deleteSubject(Subject subject) {
		try {
			getSubjectService().deleteSubject(subject);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Course Deleted!",
					subject.getSubjectName() +" deleted"));
		} catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Course Not Deleted!",
					"Unable to delete " + subject.getSubjectName() +" as it is linked to questions in database.."));
		}

		getSubjectByDepartmentId();
		return null;
	}

	public int getSelectedSubjectId() {
		return selectedSubjectId;
	}

	public void setSelectedSubjectId(int selectedSubjectId) {
		this.selectedSubjectId = selectedSubjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public SubjectService getSubjectService() {
		return subjectService;
	}

	public void setSubjectService(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public List<Subject> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}

	public void getSubjectByDepartmentId() {
		subjectList = new ArrayList<Subject>();
		subjectList.addAll(getSubjectService().getSubjectsByDepartmentId(getDepartmentId()));
	}


	public StreamedContent getQuestionPaperFile() {
		logger.info("SubjectManagedBean.getQuestionPaperFile Begins..");
		try {
			message = questionService.prepareQuestionPaper(getSubjectCode(), getSelectedSubjectId(), getSelectedExamType(), getExamYear(), String.valueOf(getSelectedYear()), String.valueOf(getSelectedSem()));
			if(message.contains("SUCCESS")) {
				String temp = message.split(":file_full_Name:")[1];
				String questionFileFullName = temp.split(":file_Name:")[0];
				String questionFileName = temp.split(":file_Name:")[1];
				InputStream stream = new FileInputStream(questionFileFullName);
				questionPaperFile = new DefaultStreamedContent(stream, "application/pdf", questionFileName);	
			} else {
				questionPaperFile = null;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			}

		} catch(Exception e) {
			logger.error("Exception in SubjectManagedBean.getQuestionPaperFile.. ", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Question generation failed!!",
					"Error generation question paper.."));
			e.printStackTrace();
		}
		logger.info("SubjectManagedBean.getQuestionPaperFile Ends..");
		return questionPaperFile;
	}

	public void setQuestionPaperFile(StreamedContent questionPaperFile) {
		this.questionPaperFile = questionPaperFile;
	}

	public void handleFileUpload(FileUploadEvent event) {
		InputStream file;
		String message = null;
		if (event.getFile().equals(null)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File is null", null));
		}

		try {
			if(getSelectedSubjectId() != 0) {
				file = event.getFile().getInputstream();
				message = getQuestionService().uploadQuestions(file, getSelectedSubjectId(), String.valueOf(getSelectedYear()), String.valueOf(getSelectedSem()));
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						message,
						message));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select course Id", null));
			}
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error reading file" + e, null));
		}
	}
	
	public void getQuestiontypeBySelectedSubjectId() {
		questionTypesList = new ArrayList<QuestionType>();
		questionTypesList.addAll(getQuestionTypeService().getQuestionTypeBySubjectId(getSelectedSubjectId()));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExamYear() {
		return examYear;
	}

	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public int getFileUploadCount() {
		return fileUploadCount;
	}

	public void setFileUploadCount(int fileUploadCount) {
		this.fileUploadCount = fileUploadCount;
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}

	public int getSelectedSem() {
		if(selectedSem > 0) {
			fileUploadCount = 1;
		} else {
			fileUploadCount = 0;
		}
		return selectedSem;
	}

	public void setSelectedSem(int selectedSem) {
		this.selectedSem = selectedSem;
	}

	public String getSelectedExamType() {
		return selectedExamType;
	}

	public void setSelectedExamType(String selectedExamType) {
		this.selectedExamType = selectedExamType;
	}

	public QuestionTypeService getQuestionTypeService() {
		return questionTypeService;
	}

	public void setQuestionTypeService(QuestionTypeService questionTypeService) {
		this.questionTypeService = questionTypeService;
	}

	public List<QuestionType> getQuestionTypeList() {
		return questionTypeList;
	}

	public void setQuestionTypeList(List<QuestionType> questionTypeList) {
		this.questionTypeList = questionTypeList;
	}

	public List<QuestionType> getQuestionTypesList() {
		return questionTypesList;
	}

	public void setQuestionTypesList(List<QuestionType> questionTypesList) {
		this.questionTypesList = questionTypesList;
	}
}
