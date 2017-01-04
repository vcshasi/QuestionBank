package com.questionbank.spring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.questionbank.spring.model.QuestionType;

@Repository
public class QuestionTypeDao {

	private static final Logger logger = Logger.getLogger(QuestionTypeDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sesionFactory) {
		this.sessionFactory = sesionFactory;
	}

	public void addQuestionType(QuestionType questionType) {
		getSessionFactory().getCurrentSession().save(questionType);
	}

	public void deleteQuestionType(QuestionType questionType) {
		getSessionFactory().getCurrentSession().delete(questionType);
	}

	public void updateQuestionType(QuestionType questionType) {
		getSessionFactory().getCurrentSession().update(questionType);
	}

	@SuppressWarnings("unchecked")
	public List<QuestionType> getQuestionTypes() {
		List<QuestionType> questionTypes = null;
		try {
			questionTypes = new ArrayList<QuestionType>();
			questionTypes = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.QuestionType").list();
		} catch (Exception e) {
			logger.error("Exception occured in QuestionTypeDao.getQuestionTypes" + e);
		}
		return questionTypes;
	}

	public QuestionType getQuestionByMark(int mark) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.QuestionType as QuestionType where QuestionType.mark = :mark");
		query.setParameter("mark", mark);
		@SuppressWarnings("unchecked")
		List<QuestionType> questionType = query.list();
		if(!questionType.isEmpty())
			return questionType.get(0);
		else
			return null;
	}
	
	public QuestionType getQuestionByMarkAdSubject(Float mark, int subjectId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.QuestionType as QuestionType where QuestionType.mark = :mark and QuestionType.subjectId = :subjectId");
		query.setParameter("mark", mark);
		query.setParameter("subjectId", subjectId);
		@SuppressWarnings("unchecked")
		List<QuestionType> questionType = query.list();
		if(!questionType.isEmpty())
			return questionType.get(0);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<QuestionType> getQuestionTypeBySubjectId(int subjectId) {
		List<QuestionType> questionTypes = null;
		try {
			questionTypes = new ArrayList<QuestionType>();
			Query query = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.QuestionType as QuestionType where QuestionType.subjectId = :subjectId");
			query.setParameter("subjectId", subjectId);
			questionTypes = query.list();
		} catch (Exception e) {
			logger.error("Exception occured in QuestionTypeDao.getQuestionTypes" + e);
		}
		return questionTypes;
	}

}
