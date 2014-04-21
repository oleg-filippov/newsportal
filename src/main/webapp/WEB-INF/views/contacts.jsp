<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="contacts.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<h2>Contacts...</h2>
	</jsp:body>
</t:template>


