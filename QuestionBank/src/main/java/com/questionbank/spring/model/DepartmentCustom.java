package com.questionbank.spring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="department")
public class DepartmentCustom {

	@Id
	@Column(name="DEPARTMENT_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int departmentId;

	@Column(name="DEPARTMENT_NAME")
	private String departmentName;

	@Column(name="ASSOCIATED_COURSE")
	private String associatedCourse;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Date createdDate;

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAssociatedCourse() {
		return associatedCourse;
	}

	public void setAssociatedCourse(String associatedCourse) {
		this.associatedCourse = associatedCourse;
	}


}