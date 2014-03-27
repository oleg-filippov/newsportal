<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="stylesheet" href="<c:url value="/resources/css/signin.css"/>" />

<c:set var="title"> <spring:message code="signin.pageTitle" /> </c:set>
<jsp:include page="common/header.jsp">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<div class="container">
	<form name="form-s" class="form-signin" action="signin/check" method="post">
		<h2 class="form-signin-heading"><spring:message code="signin.heading" /></h2>
		<c:if test="${not empty error}">
			<div class="label label-important"><spring:message code="error.signIn" /></div>
		</c:if>
		<input size="30" type="text" name="j_username" class="input-block-level"
			placeholder="<spring:message code="signin.username" />" autofocus="autofocus">
		<input size="30" type="password" name="j_password" class="input-block-level"
			placeholder="<spring:message code="signin.password" />">
		<label class="checkbox">
			<input type="checkbox" name="_spring_security_remember_me" value="true"> 
			<spring:message code="signin.rememberMe" />
		</label>
		<button class="btn btn-lg btn-primary btn-block" type="submit">
			<spring:message code="signin.signInUrl" />
		</button>
	</form>
</div>

<jsp:include page="common/footer.jsp" />