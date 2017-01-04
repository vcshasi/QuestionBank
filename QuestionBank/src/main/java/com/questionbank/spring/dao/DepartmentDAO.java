package com.questionbank.spring.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.questionbank.spring.model.DepartmentCustom;

@Repository
public class DepartmentDAO  {
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addDepartment(DepartmentCustom department) {
		getSessionFactory().getCurrentSession().save(department);
	}

	public void deleteDepartment(DepartmentCustom department) {
		getSessionFactory().getCurrentSession().delete(department);
	}

	public void updateDepartment(DepartmentCustom department) {
		getSessionFactory().getCurrentSession().update(department);
	}

	public List<DepartmentCustom> getDepartments() {
		@SuppressWarnings("unchecked")
		List<DepartmentCustom> departments = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.DepartmentCustom").list();
		return departments;
	}

	public DepartmentCustom getDepartmentById(int departmentId) {
		String getDepartmentByIdString ="FROM com.questionbank.spring.model.DepartmentCustom as department where department.departmentId = :departmentId";
		Query getDepartmentByIdQuery = getSessionFactory().getCurrentSession().createQuery(getDepartmentByIdString);
		getDepartmentByIdQuery.setParameter("departmentId", departmentId);
		@SuppressWarnings("unchecked")
		List<DepartmentCustom> departments = getDepartmentByIdQuery.list();
		if(!departments.isEmpty())
			return departments.get(0);
		else
			return null;
	}
}