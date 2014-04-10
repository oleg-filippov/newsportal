<%@ tag language="java" description="Displays a list of news"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="newsByPage" required="true"
	type="java.util.ArrayList" description="List of news"%>

<!-- Show news per page -->
<c:choose>
	<c:when test="${not empty newsByPage}">
		<c:forEach var="curNews" items="${newsByPage}">
			<c:url value="/news/${curNews.id}" var="viewNewsUrl" />
			<h2>
				<a href="${viewNewsUrl}"> <c:out value="${curNews.title}" /></a>
			</h2>
			<p>
				Category:
				<c:out value="${curNews.category.name}" />
			</p>
			<p>
				<c:out value="${curNews.preview}" />
				<a href="${viewNewsUrl}"><spring:message code="news.showNewsUrl" />
					&raquo;</a>
			</p>
			<ul class="list-inline">
				<li><fmt:formatDate value="${curNews.created}" type="both" /></li>
				<li><i class="glyphicon glyphicon-check"></i> <spring:message
						code="news.views" />: <c:out value="${curNews.viewsCount}" /></li>
				<li><a href="${viewNewsUrl}#comments"><i
						class="glyphicon glyphicon-comment"></i> <c:out
							value="${curNews.commentsCount}" /> </a></li>

			</ul>
			<ul class="list-inline">
				<c:forEach var="tag" items="${curNews.tags}">
					<c:url value="/tags/${tag.name}" var="searchByTagUrl" />
					<li><a href="${searchByTagUrl}"><c:out value="${tag.name}" /></a></li>
				</c:forEach>
			</ul>
			<hr>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<h2>
			<spring:message code="home.noNews" />
		</h2>
		<hr>
	</c:otherwise>
</c:choose>