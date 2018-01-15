Solution
========

Requirements
------------
* Java 8
* Maven

Configuration
-------------

Configuration of the application can be done within the `src/main/resources/application.properties` file.

This allows datasource configuration (default is mysql). It uses Spring's driver auto detection based on the JDBC URL.

Building
--------
The application can be built using Maven with the command `mvn package`. 

This builds, runs unit and integration tests and outputs a runnable JAR into the `target` directory.


Running
-------

Once built, the jar can be run on the JVM with the following command.

`java -jar target/engage-expense-1.0.0.jar`


Usage
-----
The REST API exposes a single endpoint. 

**URL**: `/app/expenses`
* __GET__: Retrieves a JSON array of all expenses stored in the data store.
* __POST__: Parses the request body into an Expense and saves within the data store. It calculates VAT from the amount field and optionally does currency conversion if non-GBP is specified, ie. 12,00 EUR.


Docker
------
The application can be run on a docker container by running the included `dockerBuildAndRun.sh`. 

This file utilizes the Dockerfile to create a docker image which it then runs locally on port 8080. It features several environment variables to allow datasource configuration when running. By default it will run on a H2 in-memory database.


Notes
-----
* This is a Spring Boot application implementing the backend user stories from the coding challenge. Spring Boot offers a simplified, opinionated approach to application development to free developers from the need to define boilerplate configuration.

* I've updated the front end to output files to `solution/src/main/resources/static` rather than including its build it as part of the maven build job. The generated files are added as part of the solution in that folder.

* User stories
  - User story 1 is implemented through the ExpenseWriteController POST endpoint.
  - User story 2 is implemented through the ExpenseReadController GET endpoint.
  - User story 3 utilizes a public exchange rate service sy http://fixer.io/ and calculated the VAT through the backend CurrencyService. 
 - User story 4 is handled by the front-end, although is not shown if the user is using story 3's EU localised format or currency (ie. 12,00 EUR). The field is not used by the backend as it calculates VAT as of story 2.

* In regards to error messages (eg. bad request) from the backend to the frontend, I didn't see any implemented UX element to use so these are logged to console to expose them.
