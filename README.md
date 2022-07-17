# DataStore Project

## Consumers


## Developers
The goal of the program is to convert Java objects into database tables. 
* An instance of a Java class is analogous to an item in a databsase table.
* A field of a Java class is an abtribute of an item.

## Java Persistence API (JPA) 
### There are three components to the Persisting Java Objects
#### 1) Entity
* An entity takes the object and converts it into a database table and its details. 
* The PenachieEntity interface simplies the process of creating entities because it generates the boiler plate code automatically.
#### 2) Repository
* A Repository encapsulate the database operations you can perform on entity objects. 
* It helps separate the logic from the database operations and improves the reusability of our code.
#### 3) Routes
* An API route gets the specific and necessary information to accomplish a webpage.
##### Create Read Update Delete (CRUD) Opperations
* Generally speaking, in HTTP, CRUD opperations are storage semantics that let users create and manipulate data. 
* However, there are two primary reasons why CRUD opperations are actually necessary:
  * Speed - The execution plan for a stored procedure is saved in SQL Server's procedure cache and reused for all subsequent calls to the procedure.  
  * Protects against SQL injection attacks - Since all SQL Statements employ stored procedures instead of string concatenation to generate dynamic queries from user input data, everything entered into a parameter is quoted.
* To learn more about CRUD opperations see https://www.atatus.com/glossary/crud/.

## H2 Database
* It is an in-memory database, which means that data will not persist on the disk. Accordingly, it is ideal for testing because it avoids storing test data on the disk.
* An open-source lightweight Java databse.
* It supports standard SQL and JDBC API. It can use PostgreSQL ODBC driver too.
* To learn more about H2, see https://www.tutorialspoint.com/h2_database/h2_database_introduction.htm.

## Operates using Quarkus.
* In practice, Quarkus allows a programer to host their Java program on their local machine (similar to hosting a Python program with Flask).
* Quarkus allows developers to make modifications to the code while the application is running. Accoridingly, they bypass the time consuming need to restart the application each time they wish to make some modification.
* To learn more about quarkus, see https://quarkus.io/.

### File

### What is the difference between the convetional Flask and Quarkus? 
* 

### Our Paradigm
Our goal was to persistce user and group Java objects into user and group tables. 

## Consumers
How can people use this application? 
Consumers will need to have a few software downloaded before properly using our program. 
### Java
* Because our program primarily runs on Java, you will need to have JDK 11 installed on your computer - newer verion should also be fine, but JDK 11 is prefered. 
* To learn more about downloading Java, see https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A.
### Maven
* Maven is a software that faciliates building other software: it organizes the file structure of the project and facilitates 
### Getting started with Quarkus
* After cloning our GitHub repo, cd into the 'app' folder 
* By running the command 