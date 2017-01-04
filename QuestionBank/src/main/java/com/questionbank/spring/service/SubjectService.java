package com.questionbank.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionbank.spring.dao.QuestionTypeDao;
import com.questionbank.spring.dao.SubjectDAO;
import com.questionbank.spring.model.Subject;

@Service("subjectService")
public class SubjectService {

	@Autowired
	SubjectDAO subjectDAO;

	@Autowired
	QuestionTypeDao questionTypeDAO;

	@Transactional(readOnly = false)
	public void addSubject(Subject subject) {
		getSubjectDAO().addSubject(subject);
	}

	@Transactional
	public List<Subject> getSubjectsByDepartmentId(int departmentId) {
		List<Subject> subjectsList = getSubjectDAO().getSubjectByDepartmentId(departmentId);
		return subjectsList;
	}

	@Transactional
	public void updateSubject(Subject subject) {
		getSubjectDAO().updateSubject(subject);
	}

	@Transactional(readOnly = false)
	public void deleteSubject(Subject subject) {
		getSubjectDAO().deleteSubject(subject);
	}

	public SubjectDAO getSubjectDAO() {
		return subjectDAO;
	}

	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	public QuestionTypeDao getQuestionTypeDAO() {
		return questionTypeDAO;
	}

	public void setQuestionTypeDAO(QuestionTypeDao questionTypeDAO) {
		this.questionTypeDAO = questionTypeDAO;
	}
}
