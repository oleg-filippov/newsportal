<%@ tag language="java" description="Displays category abd the list of tags"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="article" type="net.filippov.newsportal.domain.Article"
	required="true" description="Current article"%>

<div class="category-tags text-muted">
	<hr>
	<small>
		<c:url value="/user" var="profileUrl" />
		<a id="author" href="${profileUrl}/${article.author.id}">
			<strong>${article.author.login}</strong>
		</a>
		&nbsp;
		<fmt:formatDate value="${article.created}" type="date" dateStyle="long"/>
		&nbsp;|&nbsp;
		<c:url value="/category/${article.category.name}" var="searchByCategoryUrl" />
		<a href="${searchByCategoryUrl}">
			<c:out value="${article.category.name}" />
		</a>
		<c:if test="${not empty article.tags}">&nbsp;|&nbsp;</c:if>
		<c:forEach var="tag" items="${article.tags}">
			<c:url value="/tags/${tag.name}" var="searchByTagUrl" />
			<a href="${searchByTagUrl}">
				<c:out value="${tag.name}" />
			</a>&nbsp;
		</c:forEach>
	</small>
	<hr>
</div>