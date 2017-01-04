package com.questionbank.spring.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.questionbank.spring.model.QuestionCustom;

@Repository
public class QuestionDAO  {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addQuestion(QuestionCustom question) {
		getSessionFactory().getCurrentSession().save(question);
	}

	public void addManyQuestions(List<QuestionCustom> questionList) {
		getSessionFactory().getCurrentSession().save(questionList);
	}

	public void deleteQuestion(QuestionCustom question) {
		getSessionFactory().getCurrentSession().delete(question);
	}

	public void updateQuestion(QuestionCustom question) {
		getSessionFactory().getCurrentSession().update(question);
	}

	public void deleteByQuestionType(int questionTypeId) {
		getSessionFactory().getCurrentSession().createQuery("delete com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId").setParameter("qTypeId", questionTypeId).executeUpdate();
	}

	public void deleteByQuestionTypeandSubjectId(int questionTypeId, int subjectId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("delete com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId and Question.subjectId = :subId");
		query.setParameter("qTypeId", questionTypeId);
		query.setParameter("subId", subjectId);
		query.executeUpdate();
	}

	public List<QuestionCustom> getRandomQuestionsByQuestionTypeandSubjectandUnitwithLimit(int questionTypeId, int subjectId, int limit, String unitNumber, String year, String sem) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId and Question.subjectId = :subId and Question.unit = :unitNumber and Question.year = :year and Question.semester = :sem order by RAND()");
		query.setParameter("qTypeId", questionTypeId);
		query.setParameter("subId", subjectId);
		query.setParameter("unitNumber", unitNumber);
		query.setParameter("year", year);
		query.setParameter("sem", sem);
		@SuppressWarnings("unchecked")
		List<QuestionCustom> questions = query.setMaxResults(limit).list();
		return questions;
	}
	
	public List<QuestionCustom> getRandomQuestionsByQuestionTypeandSubjectandUnit(int questionTypeId, int subjectId, String unitNumber, String year, String sem) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId and Question.subjectId = :subId and Question.unit = :unitNumber and Question.year = :year and Question.semester = :sem order by RAND()");
		query.setParameter("qTypeId", questionTypeId);
		query.setParameter("subId", subjectId);
		query.setParameter("unitNumber", unitNumber);
		query.setParameter("year", year);
		query.setParameter("sem", sem);
		@SuppressWarnings("unchecked")
		List<QuestionCustom> questions = query.list();
		return questions;
	}
	
	public List<String> getDistinctUnitByQuestionTypeandSubject(int questionTypeId, int subjectId, String year, String sem) {
		// TODO Yet to form correct query
		Query query = getSessionFactory().getCurrentSession().createQuery("select distinct Question.unit from com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId and Question.subjectId = :subId and Question.year = :year and Question.semester = :sem");
		query.setParameter("qTypeId", questionTypeId);
		query.setParameter("subId", subjectId);
		query.setParameter("year", year);
		query.setParameter("sem", sem);
		@SuppressWarnings("unchecked")
		List<String> questions = query.list();
		return questions;
	}
	
	public int getQuestionCountByUnit(int questionTypeId, int subjectId, String year, String sem, String unit) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId and Question.subjectId = :subId and Question.year = :year and Question.semester = :sem and Question.unit = :unit");
		query.setParameter("qTypeId", questionTypeId);
		query.setParameter("subId", subjectId);
		query.setParameter("year", year);
		query.setParameter("sem", sem);
		query.setParameter("unit", unit);
		int questionsSize= query.list().size();
		return questionsSize;
	}

	public int getQuestionsByQuestionTypeandSubject(int questionTypeId, int subjectId) {
		Query query = getSessionFactory().getCurrentSession().createQuery(" from com.questionbank.spring.model.QuestionCustom as Question where Question.questionTypeId = :qTypeId and Question.subjectId = :subId");
		query.setParameter("qTypeId", questionTypeId);
		query.setParameter("subId", subjectId);
		int availableQuestions = query.list().size();
		return availableQuestions;
	}

	public List<QuestionCustom> getQuestions() {
		@SuppressWarnings("unchecked")
		List<QuestionCustom> questions = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.QuestionCustom").list();
		return questions;
	}

}