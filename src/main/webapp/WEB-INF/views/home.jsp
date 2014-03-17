<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:set var="title"> <spring:message code="home.pageTitle" /> </c:set>
<jsp:include page="common/header.jsp" flush="true">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<c:url value="/news/add" var="addNewsUrl" />

<div class="container">
<sec:authorize access="hasAnyRole('ROLE_AUTHOR', 'ROLE_ADMIN')">
	<h4><a href="${addNewsUrl}"><spring:message code="home.addNewsUrl" /></a></h4><hr>
</sec:authorize>

<c:choose>
	<c:when test="${not empty allNews}">
		<div class="row">
		<c:forEach var="curNews" items="${allNews}">
			<c:url value="/news/${curNews.id}" var="viewNewsUrl" />
			<div class="span4">
				<h2><a href="${viewNewsUrl}">
					<c:out value="${curNews.title}" /></a></h2>
				<small>
				<i class="icon-eye-open"></i> 
				<spring:message code="news.views" />: <c:out value="${curNews.viewsCount}" /><br>
				<i class="icon-comment"></i> 
				<spring:message code="news.comments" />: <c:out value="${curNews.commentsCount}" />
				</small><br><br>
				<p><c:out value="${curNews.preview}" />
				<a href="${viewNewsUrl}"><spring:message code="news.showNewsUrl" /> &raquo;</a></p>
			</div>
		</c:forEach>
		</div>
	</c:when>
	<c:otherwise>
		<h2><spring:message code="home.noNews" /></h2>
	</c:otherwise>
</c:choose>
</div> <!-- end of container -->

<jsp:include page="common/footer.jsp" />
