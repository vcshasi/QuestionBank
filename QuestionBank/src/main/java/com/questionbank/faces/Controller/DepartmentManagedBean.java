package com.questionbank.faces.Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.questionbank.spring.model.DepartmentCustom;
import com.questionbank.spring.service.DepartmentService;
import com.questionbank.utils.QuestionBankUtils;

@ManagedBean(name="deptMB")
@ViewScoped
public class DepartmentManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String departmentName;

	private String selectedCourse;

	private String[] courses;

	@ManagedProperty(value="#{departmentService}")
	DepartmentService departmentService;

	private List<DepartmentCustom> departmentList;

	public void onRowEdit(RowEditEvent event) {
		DepartmentCustom departmentCustom = (DepartmentCustom) event.getObject();
		try {
			departmentService.updateDepartment(departmentCustom);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Department Updated!",
					departmentCustom.getDepartmentName() +" Updated.."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Department Updation Failed!",
					departmentCustom.getDepartmentName() +" Not Updated.."));
		}
	}  

	public void onRowCancel(RowEditEvent event) {  

	}

	public String addDepartment() {

		if(departmentName == null || departmentName.isEmpty() || (departmentName.length() > 45)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid Department!",
					"Department cannot be empty.."));
		} else {
			try {
				DepartmentCustom department = new DepartmentCustom();
				department.setDepartmentName(getDepartmentName());
				department.setAssociatedCourse(getSelectedCourse());
				departmentService.addDepartment(department);
				init();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Department Added!",
						getDepartmentName() + " added to database.."));
				departmentName="";
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "department?i=1";
	}

	public String deleteDepartment(DepartmentCustom department) {
		try {
			getDepartmentService().deleteDepartment(department);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Department Deleted!",
					department.getDepartmentName() +" deleted"));
		} catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Department Not Deleted!",
					"Unable to delete " + department.getDepartmentName() +" as it is linked to subjects in database.."));
		}
		init();
		return null;
	}

	public String getSubjectName() {
		return departmentName;
	}

	public void setSubjectName(String subjectName) {
		this.departmentName = subjectName;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<DepartmentCustom> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<DepartmentCustom> departmentList) {
		this.departmentList = departmentList;
	}

	public String[] getCourses() {
		return courses;
	}

	public void setCourses(String[] courses) {
		this.courses = courses;
	}

	@PostConstruct
	public void init() {
		departmentList = new ArrayList<DepartmentCustom>();
		departmentList.addAll(getDepartmentService().getdepartments());
		courses = QuestionBankUtils.getCourseName();
	}

	public String getSelectedCourse() {
		return selectedCourse;
	}

	public void setSelectedCourse(String selectedCourse) {
		this.selectedCourse = selectedCourse;
	}

}
