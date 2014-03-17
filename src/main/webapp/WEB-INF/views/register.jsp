<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="title"> <spring:message code="register.pageTitle" /></c:set>
<jsp:include page="common/header.jsp">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<div class="container">
	<h2><spring:message code="register.pageTitle" /></h2>
	<form:form method="post" commandName="user">
		<table>
			<tr>
				<td><form:errors path="login" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td><font color='red'>*</font>
					<spring:bind path="login">
						<input type="text" name="login" class="field span3" maxlength="20"
							placeholder="<spring:message code="user.login" />" value="${user.login}">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><form:errors path="password" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td><font color='red'>*</font>
					<spring:bind path="password">
						<input type="password" name="password" class="field span3" maxlength="60"
							placeholder="<spring:message code="user.password" />" value="${user.password}">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><form:errors path="email" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td><font color='red'>*</font>
					<spring:bind path="email">
						<input type="text" name="email" class="field span3" maxlength="30"
							placeholder="<spring:message code="user.email" />" value="${user.email}">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><form:errors path="name" class="label label-important"></form:errors></td>
			</tr>
			<tr>
				<td><font color='red'>*</font>
					<spring:bind path="name">
						<input type="text" name="name" class="field span3" maxlength="50"
							placeholder="<spring:message code="user.name" />" value="${user.name}">
					</spring:bind>
				</td>
			</tr>
		</table>
		<button class="btn" type="submit"><spring:message code="register.submitButton" /></button>
  		<button class="btn" type="reset"><spring:message code="viewnews.resetButton" /></button>
	</form:form>
</div>

<script>$('input[maxlength]').maxlength();</script>

<jsp:include page="common/footer.jsp" />