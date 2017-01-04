package com.questionbank.spring.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.questionbank.spring.model.User;

@Repository
public class UserDao {

	public static final Logger logger = Logger.getLogger(UserDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addUser(User user) {
		getSessionFactory().getCurrentSession().save(user);
	}

	public void deleteUser(User user) {
		getSessionFactory().getCurrentSession().delete(user);
	}

	public void updateUser(User user) {
		getSessionFactory().getCurrentSession().update(user);
	}

	public List<User> getUsers() {
		@SuppressWarnings("unchecked")
		List<User> users = getSessionFactory().getCurrentSession().createQuery("from com.questionbank.spring.model.User").list();
		return users;
	}

	public List<User> getStaffs() {
		String getStaff ="from com.questionbank.spring.model.User as user where user.userRole = :urole ";
		Query getUserQuery = getSessionFactory().getCurrentSession().createQuery(getStaff);
		getUserQuery.setParameter("urole", "STAFF");
		@SuppressWarnings("unchecked")
		List<User> users = getUserQuery.list();
		if(!users.isEmpty()) {
			return users;
		} else {
			return null;
		}

	}

	public User getUser(String userName, String password) {
		logger.info("UserDao.getUser method starts..");
		String getUser ="from com.questionbank.spring.model.User as user where user.userName = :uname and user.password = :pwd";
		Query getUserQuery = getSessionFactory().getCurrentSession().createQuery(getUser);
		logger.info("getUser Query : " + getUserQuery.getQueryString());
		getUserQuery.setParameter("uname", userName);
		getUserQuery.setParameter("pwd", password);
		@SuppressWarnings("unchecked")
		List<User> users = getUserQuery.list();
		if(!users.isEmpty()) {
			logger.info("User is :" + users.get(0).getUserName());
			return users.get(0);
		}
		else {
			logger.info("No User found in database");
			return null;
		}
	}

	public int changePassword(User user) {
		logger.info("UserDao.changePassword method starts..");
		String changePwdQuery="update com.questionbank.spring.model.User as user set user.password = :pwd where user.userId = :uId";
		Query query = getSessionFactory().getCurrentSession().createQuery(changePwdQuery);
		query.setParameter("pwd", user.getPassword());
		query.setParameter("uId", user.getUserId());
		int result = query.executeUpdate();
		logger.info("UserDao.changePassword method ends..");
		return result;
	}
}
