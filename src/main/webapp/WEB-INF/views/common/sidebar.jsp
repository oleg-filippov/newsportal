<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!-- Add article url -->
<c:url value="/article/add" var="addArticleUrl" />
<sec:authorize access="hasRole('ROLE_AUTHOR')">
	<h4><a href="${addArticleUrl}"><spring:message code="home.addArticleUrl" /></a></h4>
	<hr>
</sec:authorize>

<!-- About -->
<div class="sidebar-module">
	<h4><spring:message code="sidebar.about" /></h4>
	<p>Some info about this portal.</p>
</div>
<hr>

<!-- Categories -->
<div class="panel panel-default">
	<div class="panel-heading"><spring:message code="sidebar.categories" /></div>
	<div class="panel-body">
		<ol class="list-unstyled">
			<c:forEach var="category" items="${categories}">
				<c:url value="/category/${category.name}" var="searchByCategoryUrl" />
				<li>
					<a class="sidebar-categories" href="${searchByCategoryUrl}">
						<c:out value="${category.name}" />
					</a>
					<c:out value=" (${category.articleCount})"></c:out>
				</li>
			</c:forEach>
		</ol>
	</div>
</div>
<hr>

<!-- Tags -->
<div class="panel panel-default">
	<div class="panel-heading"><spring:message code="sidebar.tags" /></div>
	<div class="panel-body">
		<c:forEach var="tag" items="${tags}">
			<c:url value="/tags/${tag.name}" var="searchByTagUrl" />
			<a class="sidebar-categories tag${tag.scale}" href="${searchByTagUrl}"
				title="${tag.articleCount} articles">
				<c:out value="${tag.name}" />
			</a>
		</c:forEach>
	</div>
</div>
<hr>

<!-- Links -->
<div class="panel panel-default">
	<div class="panel-heading"><spring:message code="sidebar.links" /></div>
	<div class="panel-body">
		<ol class="list-unstyled">
			<li><a href="https://github.com/oleg-filippov/newsportal">Project on GitHub</a></li>
			<li><a href="http://hackerwins.github.io/summernote/">Summernote WYSIWYG editor</a></li>
			<li><a href="http://twitter.github.io/typeahead.js/">Typeahead</a></li>
		</ol>
	</div>
</div>