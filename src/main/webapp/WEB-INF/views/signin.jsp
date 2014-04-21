<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="signin.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<form name="form-s" class="form-signin" action="signin/check" method="post">
			
			<!-- Signin header -->
			<h2 class="form-signin-heading"><spring:message code="signin.heading" /></h2>
			<!-- Error message -->
			<c:if test="${not empty error}">
				<h4>
					<span class="label label-danger">
						<spring:message code="error.signIn" />
					</span>
				</h4>
			</c:if>
			
			<!-- Login -->
			<input type="text" class="form-control" name="j_username"
				placeholder="<spring:message code="signin.username" />" autofocus>
				
			<!-- Password -->
			<input type="password" name="j_password" class="form-control"
				placeholder="<spring:message code="signin.password" />">
				
			<!-- Remember-me checkbox -->
			<label class="checkbox">
				<input type="checkbox" name="_spring_security_remember_me"
					value="true" checked> <spring:message code="signin.rememberMe" />
			</label>
			
			<!-- Submit button -->
			<button class="btn btn-lg btn-primary btn-block" type="submit">
				<spring:message code="signin.signInUrl" />
			</button>
		</form>
	</jsp:body>
</t:template>