Newsportal
===========

Simple project using Spring MVC, Spring Security, Hibernate, Twitter Bootstrap, H2 db

**Work in progress**

[![](http://s28.postimg.org/8s8xl0obd/Home.jpg)](http://s28.postimg.org/egf8bwsnx/Home.png)
[![](http://s11.postimg.org/sxeurrm9b/View_article.jpg)](http://s11.postimg.org/3rdwkxkz7/View_article.png)
[![](http://s10.postimg.org/sxxqhk1h1/Edit_article.jpg)](http://s10.postimg.org/riw5su0e1/Edit_article.png)

### How to run
1. mvn install
2. mvn tomcat7:run
3. http://localhost:8080/newsportal/

Available users: admin (admin), author (author), user (user)

### Features
- Localization (en, ru)
- Generic JPA repository
- Tags autocomplete
- js-validation
- more...

### Libraries used
- Spring MVC 3.2, Spring Security 3.1, Hibernate 4.2
- [Twitter Bootstrap 3](http://getbootstrap.com/), [Summernote WYSIWYG editor](http://hackerwins.github.io/summernote/)
- H2 db