<%@ tag language="java" description="Displays the list of articles"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ attribute name="articlesByPage" required="true"
	type="java.util.ArrayList" description="List of articles"%>
	
<!-- Show articles per page -->
<c:choose>
	<c:when test="${not empty articlesByPage}">
		<c:forEach var="curArticle" items="${articlesByPage}">
			<c:url value="/article/${curArticle.id}" var="viewArticleUrl" />
			
			<!-- Comment count -->
			<h4 class="pull-right">
				<a href="${viewArticleUrl}#comments">
					<i class="glyphicon glyphicon-comment"></i>
					<c:out value="${curArticle.commentCount}" />
				</a>
			</h4>
			
			<!-- Article title -->
			<h3><a href="${viewArticleUrl}"><c:out value="${curArticle.title}" /></a></h3>
			
			<!-- Article category and tags -->
			<t:category-tags article="${curArticle}" />

			<!-- Article preview -->
			<p class="article-preview">
				<c:out value="${curArticle.preview}" />
				<a href="${viewArticleUrl}">
					<spring:message code="article.showArticleUrl" /> &raquo;
				</a>
			</p>
			<hr>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<h4><spring:message code="home.noArticles" /></h4>
		<hr>
	</c:otherwise>
</c:choose>