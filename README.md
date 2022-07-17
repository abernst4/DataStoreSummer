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
##### Create, Read, Update, Delete (CRUD) Opperations
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

### Why we used Quarkus over Spring Boot and Helidon
* Quarkus provides faster hot reloads since it can automatically detect changes made to Java and other resource/configuration files, and transparently re-compile and deploy the changes.
* Quarkus provides more than an application framework - It provides some niceities around docker and kubernetes. However, we will not be applying those until the next phase.

## File Structure
* 

### What is the difference between the convetional Flask and Quarkus? 
* 

### Our Paradigm
Our goal was to persistce user and group Java objects into user and group tables. 
