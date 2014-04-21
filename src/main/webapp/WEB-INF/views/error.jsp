<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="error.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<div class="alert alert-danger">
			<h2><spring:message code="${messageProperty}" /></h2>
		</div>
		<hr>
		<h4>
			<spring:message code="error.status" /><c:out value=": ${statusCode}" />
		</h4>
		<h4>
			<spring:message code="error.url" /><c:out value=": ${requestUrl}" />
		</h4>
		
		<!--
			<c:out value="Message: ${exception.message}"></c:out>
			<c:forEach var="st" items="${exception.stackTrace}">
				<c:out value="${st}" />
			</c:forEach>
		-->
	
	</jsp:body>
</t:template>