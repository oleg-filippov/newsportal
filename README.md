Newsportal
===========

Simple project using Spring MVC, Spring Security, Hibernate, Twitter Bootstrap, H2 db.

**Work in progress**.

### How to run
1. mvn install
2. mvn tomcat7:run
3. http://localhost:8080/newsportal/
   * Available users: admin (admin), author (author), user (user).

### H2 Config
- url: jdbc:h2:~/newsportal
- username: sa
- password:

### Libraries used
- Spring MVC 3.2, Spring Security 3.1, Hibernate 4.2
- Twitter Bootstrap 3, [Summernote WYSIWYG editor](http://hackerwins.github.io/summernote/)
- H2 db