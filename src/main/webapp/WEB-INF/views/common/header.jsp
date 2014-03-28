<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css"/>" />
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap-responsive.min.css"/>" />
<link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css"/>" />
<link rel="stylesheet" href="<c:url value="/resources/css/summernote.css"/>" />

<title><spring:message code="header.title" /> ${param.pageTitle}</title>
</head>
<body>

<c:url value="/" var="homeUrl" />
<c:url value="/about" var="aboutUrl" />
<c:url value="/contacts" var="contactsUrl" />
<c:url value="/signin" var="signinUrl" />
<c:url value="/register" var="registerUrl" />
<c:url value="/logout" var="logoutUrl" />
<c:url value="/user" var="profileUrl" />

<div class="navbar navbar-static-top">
	<div class="navbar-inner">
		<a class="brand" href="${homeUrl}"><spring:message code="project.title" /></a>
		<ul class="nav">
			<li><a href="${homeUrl}"><spring:message code="header.homeUrl" /></a></li>
			<li class="divider-vertical"></li>
			<li><a href="${aboutUrl}"><spring:message code="header.aboutUrl" /></a></li>
			<li class="divider-vertical"></li>
			<li><a href="${contactsUrl}"><spring:message code="header.contactsUrl" /></a></li>
		</ul>
		<ul class="nav pull-right">
			<sec:authorize access="isAnonymous()">
				<li><a href="${registerUrl}"><spring:message code="header.register" /></a></li>
				<li><a href="${signinUrl}"><spring:message code="header.signin" /></a></li>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<strong>${loggedUser.login}</strong>
						<b class="caret"></b>
					</a>
					<ul class="dropdown-menu">
						<li><a href="${profileUrl}/${loggedUser.id}"><spring:message code="header.profile" /></a></li>
						<li><a href="${logoutUrl}"><spring:message code="header.logout" /></a></li>
					</ul>
				</li>
			</sec:authorize>
			<li class="divider-vertical"></li>
			<li><a href="?lang=en"><img alt="English" src="<c:url value="/resources/img/en.png"/>"></a></li>
			<li><a href="?lang=ru"><img alt="Русский" src="<c:url value="/resources/img/ru.png"/>"></a></li>
		</ul>
	</div>
</div>

<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.10.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap-maxlength.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/summernote.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/summernote-ru-RU.js"/>"></script>

<script>
var url = window.location;
$('ul.nav a').filter(function() {
	var regexp = new RegExp(this.href + "\\?.+$");
	return (this.href == url & this.href.indexOf("?lang") == -1) | regexp.test(url);
}).parent().addClass('active');
</script>