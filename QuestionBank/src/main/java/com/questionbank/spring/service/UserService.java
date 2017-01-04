package com.questionbank.spring.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.questionbank.spring.dao.UserDao;
import com.questionbank.spring.model.User;

@Service("userService")
public class UserService {

	public static final Logger logger = Logger.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Transactional(readOnly = false)
	public void changePassword(User user) {
		getUserDao().changePassword(user);
	}

	@Transactional(readOnly = false)
	public void addUser(User user) {
		getUserDao().addUser(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		getUserDao().deleteUser(user);
	}

	@Transactional(readOnly = false)
	public List<User> getUsers() {
		return getUserDao().getUsers();
	}
	
	@Transactional(readOnly = false)
	public List<User> getStaffs() {
		return getUserDao().getStaffs();
	}
	
	@Transactional(readOnly = false)
	public void updateUser(User user) {
		getUserDao().updateUser(user);
	}

	@Transactional(readOnly = false)
	public User authenticate(String userName, String password) {
		logger.info("UserService.authenticate method starts..");
		try {
			return getUserDao().getUser(userName, password);
		} catch (Exception ex) {
			logger.error("Login error" + ex.getMessage());
		}
		logger.info("UserService.authenticate method ends..");
		return null;
	}

}
