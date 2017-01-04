package com.questionbank.faces.Controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import com.questionbank.spring.model.User;
import com.questionbank.spring.service.UserService;

@ManagedBean(name="userMB")
@SessionScoped
public class UserManagedBean implements Serializable{

	public final static Logger logger = Logger.getLogger(UserManagedBean.class);

	private static final long serialVersionUID = 1L;

	private String userName;

	private String loggedInUserName;

	private String password;

	private String oldPassword;

	@ManagedProperty(value="#{userService}")
	UserService userService;

	private List<User> userList;

	public void onRowEdit(RowEditEvent event) {  
		User user = (User) event.getObject();
		user.setPassword(user.getUserName());
		try {
			userService.updateUser(user);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"User Name Updated!",
					user.getUserName() +" Updated.."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Subject Updation Failed!",
					user.getUserName() +" Not Updated.."));
		}   
	}  

	public void onRowCancel(RowEditEvent event) {  
	}

	public void addUser() {
		if(getUserName().isEmpty() || getUserName() == null || (getUserName().length() > 45) || getUserName().equalsIgnoreCase("ADMIN")) {
			logger.info("User Name cannot be empty");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid User Name!",
					"User Name cannot be empty.."));
		} else {
			try {
				User user = new User();
				user.setUserName(getUserName());
				user.setPassword(getUserName());
				user.setUserRole("STAFF");
				getUserService().addUser(user);
				init();
				userName="";
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String deleteUser(User user) {
		getUserService().deleteUser(user);
		init();
		return null;
	}

	public String authenticate() {
		logger.info("UserManagedBean.authenticate method starts..");
		if(getUserName().isEmpty() || getUserName() == null) {
			logger.info("User Name cannot be empty");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid User Name!",
					"User Name cannot be empty.."));
		} else if (getPassword().isEmpty() || getPassword() == null) {
			logger.info("Password cannot be empty");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Invalid Password!",
					"Password cannot be empty.."));
		} else {
			User user = getUserService().authenticate(getUserName(), getPassword());
			if(user != null) {
				HttpSession session = SessionBean.getSession();
				session.setAttribute("username", user.getUserName());
				session.setAttribute("userRole", user.getUserRole());
				if(user.getUserRole().equalsIgnoreCase("ADMIN")) 
				{
					logger.info("Admin Login Success");
					loggedInUserName = userName;
					userName = "";
					return "department";
				} else {
					logger.info("Staff Login Success");
					loggedInUserName = userName;
					userName = "";
					return "stafflogin";	
				}
			} else{
				logger.info("Invalid Login!!");
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Invalid Login!",
						"Please Try Again!"));
			}
			logger.info("UserManagedBean.authenticate method ends..");
		}
		return "index";
	}

	public void changePassword(ActionEvent event) {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isSuccess = false;

		User user = getUserService().authenticate(getLoggedInUserName(), getOldPassword());
		if(user != null) {
			isSuccess = true;
			user.setPassword(getPassword());
			getUserService().changePassword(user);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Password Updated!!",
					"Password has been updated successfully.."));
		}
		else {
			logger.info("Old Password is Incorrect!!");
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Old Password is Incorrect!!",
					"Please provide correct Old Password.."));
		}
		context.addCallbackParam("passwordChanged", isSuccess);
	}   


	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionBean.getSession();
		session.invalidate();
		return "index";
	}

	@PostConstruct
	public void init() {
		userList = new ArrayList<User>();
		try {
			userList.addAll(getUserService().getStaffs());
		} catch (Exception e) {
			logger.error("No Staff exists in database", e);
		}
		userName = "";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoggedInUserName() {
		return loggedInUserName;
	}

	public void setLoggedInUserName(String loggedInUserName) {
		this.loggedInUserName = loggedInUserName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
