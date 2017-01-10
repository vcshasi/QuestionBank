# QuestionBank

This is a JAVA based web application which would generate question paper for school/college professionals according to the pattern set for a particular subject. This application would ease the work of a professor/staff who needs to spend time in preparing the question paper for their subjects. Also the random generation of questions maintains the integrity of the questions generated without disclosing it to anyone else.

You can also:
  - Save questions only one time for a specific subject for a specific semester and generate question papers multiple times
  - Export Question paper as PDF
  - Add Staffs to the portal so that they could upload and generate question paper whenever needed
  - Customized question pattern for each and every subject
  - Maintains integrity of the question paper generated as it uses a *Specific Random Logic*
  - Generate question paper for a specific subject semester wise

## Technologies Used

This project is developed using number of open source technologies like:

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

![Alt text](/QuestionBank/img/2017-01-10_11h53_54.png?raw=true "Department Page")

#### Generate Questions Page

![Alt text](/QuestionBank/img/2017-01-10_11h54_38.png?raw=true "Generate Questions")

### Change Password Page

![Alt text](/QuestionBank/img/2017-01-10_11h55_16.png?raw=true "Change Password")



 :smiley: **Happy Coding!** :smiley:


   [db_schema]: <https://github.com/vcshasi/QuestionBank/blob/master/QuestionBank/database/questionBank_schema.sql>
