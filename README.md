# Our DataStore
## How to use the application
First things first, make sure you have a few softwares downloaded and if not, download them before properly using our program.
### Java
* Because our program primarily runs on Java, you will need to have JDK 11 installed on your computer - newer verion should also be fine, but JDK 11 is prefered.
* To learn more about downloading Java, see https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A.
### Maven
* Maven is a software that faciliates building other software: it organizes the file structure of the project and facilitates the building of the program.
* To download maven, see - https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html.
* To learn more about Maven, see - https://www.simplilearn.com/tutorials/maven-tutorial/what-is-maven#:~:text=Maven%20is%20written%20in%20Java,build%20and%20testing%20automation%20setups.
### Getting started with Quarkus
* After cloning our GitHub repo, cd into the 'app' folder
* You can run the program simply with the following command in your terminal - 'mvn quarkus:dev'
* Now let's learn a bit more about the magic that goes on behind the scenes in our program and with Quarkus. 

## The Goal of our DataStore
The goal of our DataStore is to convert Java objects into database tables. 
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

## Operates using Quarkus
### In practice
* Quarkus allows a programer to host their Java program on their local machine (similar to hosting a Python program with Flask).
* Quarkus allows developers to make modifications to their code while the application is running. Accoridingly, they bypass the time-consuming need to restart the application each time they wish to make some modification.
* To learn more about quarkus, see https://quarkus.io/.

### Why we used Quarkus over Spring Boot and Helidon
* Quarkus provides faster hot reloads since it can automatically detect changes made to Java and other resource/configuration files, and transparently re-compile and deploy the changes.
* Quarkus provides more than an application framework - It provides some necessities around docker and kubernetes. However, we will not be applying those until the next phase.
* To learn more about the differences between the different services, see https://rollbar.com/blog/quarkus-vs-spring-boot/#:~:text=Quarkus%20provides%20faster%20hot%20reloads,running%20in%20a%20remote%20environment.

## File Structure
* We structured our files in accordance with our two classes because __we divided our work load based on classes (groups vs. users) and not by routes, methods, and database commands__. 
* There is two primary sections to our file structure within src/main/java/datastore - group_api and user_api. 
* Each section has its own database, entity, and route folders and files. 

### Our Paradigm
Our goal was to persistce user and group Java objects into user and group tables.