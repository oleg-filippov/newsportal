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

<div class="navbar navbar-default">
	<div class="navbar-inner">
		<div class="container">
			<div style="float: right;">
				<a class="btn btn-small btn-info" href="?lang=en"><spring:message code="header.en" /></a>
				<a class="btn btn-small btn-info" href="?lang=ru"><spring:message code="header.ru" /></a><br>
				<sec:authorize access="isAnonymous()">
					<a href="${signinUrl}"><spring:message code="header.signin" /></a><br>
					<a href="${registerUrl}"><spring:message code="header.register" /></a>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<spring:message code="header.loggedIn" />
					<a href="${profileUrl}/${loggedUser.id}">
						<strong>${loggedUser.login}</strong>
					</a><br>
					<a href="${logoutUrl}"><spring:message code="header.logout" /></a>
				</sec:authorize>
			</div>

			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
			<a class="brand" href="${homeUrl}"><spring:message code="project.title" /></a>
			<div class="nav-collapse collapse">
				<ul class="nav">
					<li><a href="${homeUrl}"><spring:message code="header.homeUrl" /></a></li>
					<li><a href="${aboutUrl}"><spring:message code="header.aboutUrl" /></a></li>
					<li><a href="${contactsUrl}"><spring:message code="header.contactsUrl" /></a></li>
				</ul>
			</div><!-- nav-collapse -->
		</div> <!-- container -->
	</div>
</div>

<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.10.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/bootstrap-maxlength.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/summernote.min.js"/>"></script>
