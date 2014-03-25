News-portal
===========

Simple project using Spring MVC 3.2, Spring Security, Hibernate 4.1, Twitter Bootstrap 2.2, H2 db, Summernote WYSIWYG editor.
*Work in progress

### How to run
1. Start H2 db
2. Run INIT.sql [DUMP NEWS.sql]
- Available users: admin, (admin), author (author), user (user)
3. mvn clean install
4. mvn tomcat7:run
5. http://localhost:8080/news-portal/

### H2 Database Config
- url: jdbc:h2:tcp://localhost/newsportal
- username: sa
- password: