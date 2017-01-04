package com.questionbank.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionbank.spring.dao.QuestionTypeDao;
import com.questionbank.spring.model.QuestionType;
import com.questionbank.spring.model.Subject;

@Service("questionTypeService")
@Transactional(readOnly = true)
public class QuestionTypeService {

	@Autowired
	QuestionTypeDao qTypeDao;
	
	@Transactional(readOnly=false)
	public void addQuestionType(QuestionType questionType) {
		getqTypeDao().addQuestionType(questionType);
	}

	@Transactional(readOnly=false)
	public void deleteQuestionType(QuestionType questionType) {
		getqTypeDao().deleteQuestionType(questionType);
	}

	@Transactional(readOnly=false)
	public void updateQuestionType(QuestionType questionType) {
		getqTypeDao().updateQuestionType(questionType);
	}

	public List<QuestionType> getQuestionTypes() {
		List<QuestionType> questionTypes = getqTypeDao().getQuestionTypes();
		return questionTypes;
	}
	
	@Transactional
	public List<QuestionType> getQuestionTypeBySubjectId(int subjectId) {
		List<QuestionType> subjectsList = getqTypeDao().getQuestionTypeBySubjectId(subjectId);
		return subjectsList;
	}

	public QuestionTypeDao getqTypeDao() {
		return qTypeDao;
	}

	public void setqTypeDao(QuestionTypeDao qTypeDao) {
		this.qTypeDao = qTypeDao;
	}
}
