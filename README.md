# QuestionBank

This is a JAVA based web application which would generate question paper for school/college professionals according to the pattern set for a particaular subject. This application would ease the work of a professor/staff who needs to spend time in preparing the question paper for their subjects. Also the random generation of questions maintains the integrity of the questions generated.

You can also:
  - Save questions for a particualr subject to the databse
  - Export Question paper as PDF
  - Generate random question paper for each subject

This text you see here is *actually* written in Markdown! To get a feel for Markdown's syntax, type some text into the left window and watch the results in the right.

## Technologies Used

Dillinger uses a number of open source projects to work properly:

* Java 7
* PrimeFaces 4.0
* Spring 3.2.5
* Hibernate 4.1
* MySQL Server 5.5.8
* Apache POI 3.9
* ITextPDF 5.5.4
* Maven 4.0
* Tomcat 7
* Eclipse

## Development

This project is built on maven. Hence dependencies could be resolved using the following command 

```sh
cd QuestionBank
mvn eclipse:clean eclipse:eclipse
```

## DataBase setup

MySQL 5.5 Sever with the following schema file to be available for the application to run

* [qb-db-schema] [db_schema]


## Building
For generating the war to be deployed in Tomct Server, issue the following command
```sh
mvn clean install
```

## QuestionBank at a glance :smiley:

### Login Page

![Alt text](/QuestionBank/img/2017-01-10_11h53_20.png?raw=true "Login Page")

### Department Page

![Alt text](/QuestionBank/img/2017-01-10_11h53_54.png?raw=true "Depeartment Page")

#### Generate Questions Page

![Alt text](/QuestionBank/img/2017-01-10_11h54_38.png?raw=true "Generate Questions")

### Change Password Page

![Alt text](/QuestionBank/img/2017-01-10_11h55_16.png?raw=true "Change Password")



 :smiley: **Happy Coding!** :smiley:


   [db_schema]: <https://github.com/vcshasi/QuestionBank/blob/master/QuestionBank/database/questionBank_schema.sql>
