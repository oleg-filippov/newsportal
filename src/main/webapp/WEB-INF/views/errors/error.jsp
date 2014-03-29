<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="title"> <spring:message code="error.pageTitle" /></c:set>
<jsp:include page="../common/header.jsp">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<div class="container">
	<h2><spring:message code="${messageProperty}" /></h2>
	
	<p><spring:message code="error.status" /><c:out value=": ${statusCode}" /></p>
	<p><spring:message code="error.url" /><c:out value=": ${requestUrl}" /></p>
	
	<!--
		<c:out value="Message: ${exception.message}"></c:out>
		<c:forEach var="st" items="${exception.stackTrace}">
			<c:out value="${st}" />
		</c:forEach>
	-->
</div>

<jsp:include page="../common/footer.jsp" />