<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" />

<c:set var="title"> <spring:message code="signup.pageTitle" /></c:set>
<jsp:include page="common/header.jsp">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<div class="container">
	<form:form name="form-s" class="form-signin" method="post" commandName="user">
		<h2 class="form-signin-heading"><spring:message code="signup.pageTitle" /></h2>
		<table>
			<tr>
				<td><form:errors path="login" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td>
					<spring:bind path="login">
						<input size="40" type="text" name="login" class="input-block-level" maxlength="20"
							placeholder="<spring:message code="user.login" />" value="${user.login}">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><form:errors path="password" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td>
					<spring:bind path="password">
						<input size="40" type="password" name="password" class="input-block-level" maxlength="60"
							placeholder="<spring:message code="user.password" />" value="${user.password}">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><form:errors path="email" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td>
					<spring:bind path="email">
						<input size="40" type="text" name="email" class="input-block-level" maxlength="30"
							placeholder="<spring:message code="user.email" />" value="${user.email}">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><form:errors path="name" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td>
					<spring:bind path="name">
						<input size="40" type="text" name="name" class="input-block-level" maxlength="50"
							placeholder="<spring:message code="user.name" />" value="${user.name}">
					</spring:bind>
				</td>
			</tr>
		</table>
		<button class="btn btn-lg btn-primary btn-block" type="submit"><spring:message code="signup.submitButton" /></button>
	</form:form>
</div>

<script>
$('input[maxlength]').maxlength();
</script>

<jsp:include page="common/footer.jsp" />