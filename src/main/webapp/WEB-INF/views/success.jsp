<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="success.pageTitle" /> | <spring:message code="project.title" />
</c:set>
<c:set var="RedirectUrl" value="${url}"></c:set>
<meta http-equiv="refresh" content="2; url=${RedirectUrl}" />

<t:template title="${title}">
	<jsp:body>
		<div class="alert alert-success">
			<h2><spring:message code="${messageProperty}" /></h2>
		</div>
	</jsp:body>
</t:template>