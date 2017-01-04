package com.questionbank.spring.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.questionbank.spring.model.Subject;

@Repository
public class SubjectDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addSubject(Subject subject) {
		getSessionFactory().getCurrentSession().save(subject);
	}

	public void deleteSubject(Subject subject) {
		getSessionFactory().getCurrentSession().delete(subject);
	}

	public void updateSubject(Subject subject) {
		getSessionFactory().getCurrentSession().update(subject);
	}

	public List<Subject> getSubjectByDepartmentId(int departmentId) {
		String getSubjectByDepartmentIdString ="FROM com.questionbank.spring.model.Subject as subject where subject.departmentId = :departmentId";
		Query getSubjectByDepartmentIdQuery = getSessionFactory().getCurrentSession().createQuery(getSubjectByDepartmentIdString);
		getSubjectByDepartmentIdQuery.setParameter("departmentId", departmentId);
		@SuppressWarnings("unchecked")
		List<Subject> subjects = getSubjectByDepartmentIdQuery.list();
		return subjects;
	}

	public Subject getSubjectById(int subjectId) {
		String getSubjectByIdString ="FROM com.questionbank.spring.model.Subject as subject where subject.subjectId = :subjectId";
		Query getSubjectByIdQuery = getSessionFactory().getCurrentSession().createQuery(getSubjectByIdString);
		getSubjectByIdQuery.setParameter("subjectId", subjectId);
		@SuppressWarnings("unchecked")
		List<Subject> subjects = getSubjectByIdQuery.list();
		if(!subjects.isEmpty())
			return subjects.get(0);
		else
			return null;
	}
}
