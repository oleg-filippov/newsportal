<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="sidebar-module">
	<h4>About</h4>
	<p>Some info about this portal.</p>
</div>
<hr>
<div class="panel panel-default">
	<div class="panel-heading">Categories</div>
	<div class="panel-body">
		<ol class="list-unstyled">
			<c:forEach var="categoryName" items="${categoryNames}">
				<c:url value="/category/${categoryName}" var="searchByCategoryUrl" />
				<li><a href="${searchByCategoryUrl}"><c:out
							value="${categoryName}" /></a></li>
			</c:forEach>
		</ol>
	</div>
</div>
<hr>
<div class="panel panel-default">
	<div class="panel-heading">Tags</div>
	<div class="panel-body">
		<c:forEach var="tagName" items="${tagNames}">
			<c:url value="/tags/${tagName}" var="searchByTagUrl" />
			<a href="${searchByTagUrl}"><c:out value="${tagName}" /></a>
		</c:forEach>
	</div>
</div>
<hr>
<div class="panel panel-default">
	<div class="panel-heading">Archives</div>
	<div class="panel-body">
		<ol class="list-unstyled">
			<li><a href="#">January 2014</a></li>
			<li><a href="#">December 2013</a></li>
			<li><a href="#">November 2013</a></li>
		</ol>
	</div>
</div>
<hr>
<div class="panel panel-default">
	<div class="panel-heading">My Links</div>
	<div class="panel-body">
		<ol class="list-unstyled">
			<li><a href="#">Link 1</a></li>
			<li><a href="#">Link 2</a></li>
			<li><a href="#">Link 3</a></li>
		</ol>
	</div>
</div>