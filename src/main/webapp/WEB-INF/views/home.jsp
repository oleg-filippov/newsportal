<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="home.pageTitle" /> | <spring:message
		code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<c:url value="/news/add" var="addNewsUrl" />

		<sec:authorize access="hasRole('ROLE_AUTHOR')">
			<h4>
				<a href="${addNewsUrl}"><spring:message code="home.addNewsUrl" /></a>
			</h4>
			<hr>
		</sec:authorize>

		<t:newslist newsByPage="${newsByPage}" />
		<t:pagination currentPage="${currentPage}" pagesCount="${pagesCount}" />
	</jsp:body>
</t:template>