<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="signup.pageTitle" /> | <spring:message
		code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>

<form:form name="form-s" class="form-signin" method="post"
			commandName="user">
	<h2 class="form-signin-heading">
				<spring:message code="signup.pageTitle" />
			</h2>
	<h4>
				<form:errors path="login" class="label label-danger"></form:errors>
			</h4>
	<spring:bind path="login">
		<input type="text" name="login" class="form-control" maxlength="30"
					autofocus placeholder="<spring:message code="user.login" />"
					value="${user.login}">
	</spring:bind>
	<h4>
				<form:errors path="password" class="label label-danger"></form:errors>
			</h4>
	<spring:bind path="password">
		<input type="password" name="password" class="form-control"
					maxlength="60"
					placeholder="<spring:message code="user.password" />"
					value="${user.password}">
	</spring:bind>
	<h4>
				<form:errors path="email" class="label label-danger"></form:errors>
			</h4>
	<spring:bind path="email">
		<input type="text" name="email" class="form-control" maxlength="50"
					placeholder="<spring:message code="user.email" />"
					value="${user.email}">
	</spring:bind>
	<h4>
				<form:errors path="name" class="label label-danger"></form:errors>
			</h4>
	<spring:bind path="name">
		<input type="text" name="name" class="form-control" maxlength="50"
					placeholder="<spring:message code="user.name" />"
					value="${user.name}">
	</spring:bind>
	<button class="btn btn-lg btn-primary btn-block" type="submit">
				<spring:message code="signup.submitButton" />
			</button>
</form:form>

<script>
$('input[maxlength]').maxlength();
</script>

</jsp:body>
</t:template>