package com.questionbank.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class QuestionBankUtils {

	public static final Logger logger = Logger.getLogger(QuestionBankUtils.class);
	public static String[] getCourseName() {
		logger.info("QuestionBankUtils.getCourseName begins");
		Properties propertyFile = new Properties();
		InputStream inputStream = null;
		String[] courses = null;
		try {
			//inputStream = QuestionBankUtils.class.getClassLoader().getResourceAsStream("questionbank.properties");
			inputStream = new FileInputStream("C:\\temp\\questionbank.properties");
			propertyFile.load(inputStream);
			courses = propertyFile.getProperty("courseName").split(",");
		} catch (Exception e) {
			logger.error("Error reading property file " + e);
			e.printStackTrace();
		}
		logger.info("QuestionBankUtils.getCourseName ends");
		return courses;
	}
	
	public static void main(String[] args) {
		String[] names = getCourseName();
		System.out.println(names.length);
	}
}
