# DataStore Project
## The Goal of Phase One
The goal of the Phase One is to convert Java objects into database tables. 
* An instance of a Java class is analogous to an item in a databsase table.
* A field of a Java class is an abtribute of an item.

## Java Persistence API (JPA) - There are three components to the Persisting Java Objects
### 1) Entity
* An entity takes the object and converts it into a database table and its details. 
* The PenachieEntity interface simplies the process of creating entities because it generates the boiler plate code automatically.
### 2) Repository
* A Repository encapsulate the database operations you can perform on entity objects. 
* It helps separate the logic from the database operations and improves the reusability of our code.
### 3) Routes
* An API route gets the specific and necessary information to accomplish a webpage.
#### Create Read Update Delete (CRUD) Opperations
* Generally speaking, in HTTP, CRUD opperations are storage semantics that let users create and manipulate data. 
* However, there are two primary reasons why CRUD opperations are actually necessary:
  * 1) Speed - The execution plan for a stored procedure is saved in SQL Server's procedure cache and reused for all subsequent calls to the procedure.  
  * 2) Protects against SQL injection attacks - Since all SQL Statements employ stored procedures instead of string concatenation to generate dynamic queries from user input data, everything entered into a parameter is quoted.
For more, see https://www.atatus.com/glossary/crud/.

### Hibernate 
* Hibernate is one of the most mature JPA applications - it is a form the JPA. 

There are three parts to file structure and api

### Our Paradigm
Our goal was to persistce user and group Java objects into user and group tables. 

Operates using Quarkus. To learn more about quarkus https://quarkus.io/.

