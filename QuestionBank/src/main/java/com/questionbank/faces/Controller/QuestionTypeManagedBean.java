package com.questionbank.faces.Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

import com.questionbank.spring.model.QuestionType;
import com.questionbank.spring.service.QuestionService;
import com.questionbank.spring.service.QuestionTypeService;

@ManagedBean(name="qtypeMB")
@ViewScoped
public class QuestionTypeManagedBean implements Serializable{
	
	private static final Logger logger = Logger.getLogger(QuestionTypeManagedBean.class);

	private static final long serialVersionUID = 1L;

	private String mark;
	private String noOfQuestions;
	private int selectedSubjectId;
	private String selectedQuestionType;

	@ManagedProperty(value="#{questionTypeService}")
	QuestionTypeService questionTypeService;

	@ManagedProperty(value="#{questionService}")
	QuestionService questionService;

	private List<QuestionType> questionTypesList;

	public void onRowEdit(RowEditEvent event) {  
		QuestionType questionType = (QuestionType) event.getObject();
		try {
			questionTypeService.updateQuestionType(questionType);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Question Pattern Updated!",
					questionType.getMark() +" mark question Updated.."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Question Pattern Updation Failed!",
					questionType.getMark() +" mark question Not Updated.."));
			logger.error("Exception occured in QuestionTypeManagedBean.onRowEdit.. ", e);
			e.printStackTrace();
		} 
	}  

	public void onRowCancel(RowEditEvent event) {   
	}

	/*	@PostConstruct
	public void init() {
		questionTypesList = new ArrayList<QuestionType>();
		questionTypesList.addAll(getQuestionTypeService().getQuestionTypes());
	}*/

	public void addQuestionType() {
		if(getMark().isEmpty() || getMark() == null ) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid Question Type!",
					"Question Type cannot be empty.."));
		} else if (getNoOfQuestions().isEmpty() || getNoOfQuestions() == null) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid No of Questions!",
					"No of Questions cannot be empty.."));
		} else {

			try {
				QuestionType questionType= new QuestionType();
				questionType.setMark(Float.parseFloat(getMark()));
				questionType.setNoOfQuestions(Integer.parseInt(getNoOfQuestions()));
				questionType.setqType(getSelectedQuestionType());
				questionType.setSubjectId(getSelectedSubjectId());
				questionTypeService.addQuestionType(questionType);
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Question Pattern is set!",
						getNoOfQuestions() + " " + getMark() + " mark questions is included in question pattern .."));
				// init();
				setMark("");
				setNoOfQuestions("");
				setSelectedQuestionType("");
				getQuestiontypeBySelectedSubjectId();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String deleteQuestionType(QuestionType questionType) {
		try {
			getQuestionService().deleteByQuestionType(questionType);
			getQuestionTypeService().deleteQuestionType(questionType);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Question Pattern Deleted!",
					questionType.getMark() +"mark question deleted"));
		} catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Question Pattern Not Deleted!",
					"Unable to delete " + questionType.getMark() +" mark question as it is linked to questions in database.."));
			logger.error("Exception occured in QuestionTypeManagedBean.deleteQuestionType.. ", e);
		}
		getQuestiontypeBySelectedSubjectId();
		return null;
	}


	public void getQuestiontypeBySelectedSubjectId() {
		questionTypesList = new ArrayList<QuestionType>();
		questionTypesList.addAll(getQuestionTypeService().getQuestionTypeBySubjectId(getSelectedSubjectId()));
	}

	public QuestionTypeService getQuestionTypeService() {
		return questionTypeService;
	}

	public void setQuestionTypeService(QuestionTypeService questionTypeService) {
		this.questionTypeService = questionTypeService;
	}

	public List<QuestionType> getQuestionTypesList() {
		return questionTypesList;
	}

	public void setQuestionTypesList(List<QuestionType> questionTypesList) {
		this.questionTypesList = questionTypesList;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getNoOfQuestions() {
		return noOfQuestions;
	}

	public void setNoOfQuestions(String noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public int getSelectedSubjectId() {
		return selectedSubjectId;
	}

	public void setSelectedSubjectId(int selectedSubjectId) {
		this.selectedSubjectId = selectedSubjectId;
	}

	public String getSelectedQuestionType() {
		return selectedQuestionType;
	}

	public void setSelectedQuestionType(String selectedQuestionType) {
		this.selectedQuestionType = selectedQuestionType;
	}

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

}
