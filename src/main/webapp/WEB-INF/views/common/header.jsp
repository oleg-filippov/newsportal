<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:url value="/" var="homeUrl" />
<c:url value="/about" var="aboutUrl" />
<c:url value="/contacts" var="contactsUrl" />
<c:url value="/signin" var="signinUrl" />
<c:url value="/signup" var="signupUrl" />
<c:url value="/logout" var="logoutUrl" />
<c:url value="/user" var="profileUrl" />
<c:url value="/search" var="searchUrl" />

<div class="navbar navbar-default">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="${homeUrl}">
				<spring:message code="project.title" />
			</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li>
					<a href="${homeUrl}">
						<spring:message code="header.homeUrl" />
					</a>
				</li>
				<li>
					<a href="${aboutUrl}">
						<spring:message code="header.aboutUrl" />
					</a>
				</li>
				<li>
					<a href="${contactsUrl}">
						<spring:message code="header.contactsUrl" />
					</a>
				</li>
			</ul>
			
			<!-- Search form -->
			<form class="navbar-form navbar-left" action="${searchUrl}">
				<div class="input-group">
					<input type="text" name="fragment" class="form-control"
						placeholder="<spring:message code="header.search" />">
					<span class="input-group-btn">
						<button class="btn btn-default" type="submit">Go!</button>
					</span>
				</div>
			</form>
			
			<!-- SignIn, SignUp, Profile, Logout -->
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="isAnonymous()">
					<li>
						<a href="${signupUrl}">
							<i class="glyphicon glyphicon-new-window"></i>
							<spring:message code="header.signup" />
						</a>
					</li>
					<li>
						<a href="${signinUrl}">
							<i class="glyphicon glyphicon-log-in"></i>
							<spring:message code="header.signin" />
						</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<strong>
								<sec:authentication property="principal.username" />
							</strong>
							<b class="caret"></b>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a href="${profileUrl}/${loggedUserId}">
									<i class="glyphicon glyphicon-user"></i>
									<spring:message code="header.profile" />
								</a>
							</li>
							<li>
								<a href="${logoutUrl}">
									<i class="glyphicon glyphicon-off"></i>
									<spring:message code="header.logout" />
								</a>
							</li>
						</ul>
					</li>
				</sec:authorize>
				
				<!-- Choose language -->
				<li class="divider-vertical"></li>
				<li>
					<a href="?lang=en">
						<img alt="English" src="<c:url value="/resources/img/en.png"/>">
					</a>
				</li>
				<li>
					<a href="?lang=ru">
						<img alt="Русский" src="<c:url value="/resources/img/ru.png"/>">
					</a>
				</li>
			</ul>
		</div>
	</div>
</div>

<script>
var url = window.location;
$('ul.nav a').filter(function() {
	var regexp = new RegExp(this.href + "\\?.+$");
	return (this.href == url & this.href.indexOf("?lang") == -1) | regexp.test(url);
}).parent().addClass('active');
</script>

