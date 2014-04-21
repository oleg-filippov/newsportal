<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="profile.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<h4>
			<spring:message code="profile.header"/><strong>${user.login}</strong>
		</h4>
		<hr>
		<table class="profile">
			<tr>
				<th><spring:message code="user.registered"/>:</th>
				<td><fmt:formatDate value="${user.registered}" type="both" /></td>
			</tr>
			<tr>
				<th><spring:message code="user.name"/>:</th>
				<td>${user.name}</td>
			</tr>
			<tr>
				<th><spring:message code="user.email"/>:</th>
				<td>${user.email}</td>
			</tr>
			<tr>
				<c:url var="articlesByUser" value="/user/${user.id}/articles"></c:url>
				<th><spring:message code="profile.articleCount" /></th>
				<td><a href="${articlesByUser}">${user.articleCount}</a></td>
			</tr>
			<tr>
				<th><spring:message code="profile.commentCount" /></th>
				<td>${user.commentCount}</td>
			</tr>
		</table>
	</jsp:body>
</t:template>