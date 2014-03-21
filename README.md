News-portal
===========

Simple project using Spring MVC, Spring Security, Hibernate, Bootstrap, H2 Database.
-Work in Progress-

H2 Database Config
==================

url: jdbc:h2:tcp://localhost/newsportal
username: sa
password:

Run
===

1) Run script INIT.sql [script DUMP NEWS.sql]
Available users: admin, (admin), author (author), user (user)
2) mvn clean install
3) mvn tomcat7:run