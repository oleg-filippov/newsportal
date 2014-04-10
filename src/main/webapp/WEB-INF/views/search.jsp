<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="search.pageTitle" /> | <spring:message
		code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<t:newslist newsByPage="${newsByPage}" />
		<t:pagination currentPage="${currentPage1}" pagesCount="${pagesCount}" />
	</jsp:body>
</t:template>