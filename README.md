# JavaDev5FinalProject
This project is a service for creating and storing private or public notes with the ability to edit and send these notes to other users.

The application can be opened using the link: [Heroku.project](https://goit-final-group3.herokuapp.com)
There is also an option to run it on the local machine by cloning it from the repository.
You must use the following environment variables:

* server.port = ${PORT}
* spring.datasource.url = ${DB_URL}
* spring.datasource.username = ${DB_USERNAME}
* spring.datasource.password = ${DB_PASSWORD}

To start work you must complete registration (enter your unique name and password) and authentication (confirm name and password),
after which you will be taken to a page where you can create, edit, and send notes.  Next time only authentication is required. 
In addition, the application has a default user named *admin* and password *super_secret_password*.

The following technologies were used during the writing of the project:
Java, PostgreSQL, H2, Spring (MVC, Data, Security, Spring Boot), Hibernate, Lombok,  HTML, Commonmark, Bootstrap, JScript, Thymeleaf + Thymeleaf security, Flyway , Maven.
