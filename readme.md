# Yoga Test Application

Yoga Application is a software that allows individuals to easily join or leave yoga classes, while enabling the admin to effectively plan sessions according to the availability of instructors.  
This application is a working support for tests (unit, integration and end to end).

## Setting up the Database

1. Verify the presence of a MySQL database instance installed and set up correctly on your system.
2. Run the script for creating the database found in the `resources/sql` directory to set up the required database schema.
3. Input your database connection details into the `application.properties` configuration file.

### Necessary Environment Variables for MySQL

- `MYSQL_URL`: Your MySQL database's URL. For instance, `jdbc:mysql://localhost:3306/`.
- `MYSQL_DATABASE`: The title of your MySQL database. For instance, `test`.
- `MYSQL_USER`: Your MySQL database username.
- `MYSQL_PASSWORD`: The password for your MySQL database.

## Installing the Application

1. Make sure you've installed all needed dependencies: Java, Node.js, Maven.
2. Clone this repo to your local environment.
3. Move to the back-end project folder and execute `mvn clean install` to fetch dependencies and build the project.
4. Go to the front-end project folder and use `npm install` to fetch front-end dependencies.

## Launching the Application

1. In the back-end project folder, run `mvn spring-boot:run` to initiate the back-end server.
2. In the front-end project folder, use `npm run start` to start the front-end interface.

## Executing Tests

### Front-end Unit and Integration Testing (Jest)

1. In the front-end project folder, execute `npm run test` to conduct front-end unit tests via Jest.
2. For the coverage report, utilize `npm run test:coverage`.
3. A `index.html` report will be created in the `front/coverage/jest/lcov-report/index.html` path.

### End-to-End Testing (Cypress)

1. In the front-end project area, execute `npm run e2e` for conducting end-to-end testing using Cypress.
2. For the coverage overview, utilize `npm run e2e:coverage`.
3. The `index.html` report will be found in the `front/coverage/lcov-report/index.html` folder.

### Back-end Unit and Integration Testing (JUnit and Mockito)

1. Make sure to execute the database creation script found in the `resources/sql` directory before running tests.
2. In the back-end project area, run `mvn clean test` to perform back-end unit and integration tests via JUnit and Mockito.
3. A coverage report will be accessible in the `back/target/site/jacoco/index.html` folder.

Make certain that the required versions of Java, Node.js, Maven, and Angular CLI (version 14.1.0) are installed for full compatibility with the project.

## Technologies Utilized in the Development of the Yoga App :
- NodeJS v16+
- Angular CLI v14+
- Java 11+
- SpringBoot 2.6.1
- jjwt 0.9.1
- MySQL v8.0
- H2 Database
- Maven 3.8.6
- Jest 29.7.0
- Cypress
- JUnit
- Mockito
- jacoco 0.8.5