Newsportal
===========

Simple project using Spring MVC, Spring Security, Hibernate, Twitter Bootstrap, H2 db.
    Work in progress

### How to run
1. Start H2 db (see H2 Config)
2. mvn install
3. mvn tomcat7:run
4. http://localhost:8080/newsportal/
    Available users: admin (admin), author (author), user (user)

### H2 Config
- url: jdbc:h2:tcp://localhost/newsportal
- username: sa
- password:

### Libraries used
- Spring MVC 3.2, Spring Security 3.1, Hibernate 4.2
- Twitter Bootstrap 2.2, Summernote WYSIWYG editor
- H2 db