package com.questionbank.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionbank.spring.dao.DepartmentDAO;
import com.questionbank.spring.model.DepartmentCustom;

@Service("departmentService")
@Transactional(readOnly = true)
public class DepartmentService {

	// DepartmentDAO is injected...
	@Autowired
	DepartmentDAO departmentDAO;

	@Transactional(readOnly = false)
	public void addDepartment(DepartmentCustom department) {
		getDepartmentDAO().addDepartment(department);
	}

	@Transactional(readOnly = false)
	public void deleteDepartment(DepartmentCustom department) {
		getDepartmentDAO().deleteDepartment(department);
	}

	@Transactional(readOnly = false)
	public void updateDepartment(DepartmentCustom department) {
		getDepartmentDAO().updateDepartment(department);
	}

	@Transactional(readOnly = false)
	public List<DepartmentCustom> getdepartments() {
		return getDepartmentDAO().getDepartments();
	}

	public DepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}

	public void setDepartmentDAO(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}
}