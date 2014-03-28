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

<!-- Container begin -->
<div class="container">
<sec:authorize access="hasRole('ROLE_AUTHOR')">
	<h4><a href="${addNewsUrl}"><spring:message code="home.addNewsUrl" /></a></h4><hr>
</sec:authorize>

<!-- Show news per page -->
<c:choose>
	<c:when test="${not empty newsByPage}">
		<div class="row">
		<c:forEach var="curNews" items="${newsByPage}">
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

<!-- Page-navigation -->
<div class="pagination pagination-centered">
  <ul>
  	<!-- Show links to the first and prev pages -->
  	<c:choose>
  		<c:when test="${currentPage > 1}">
  			<li>
  				<a href="<c:url value="/"></c:url>">
  					&laquo; <spring:message code="home.firstPage" /></a></li>
  			<li><a href="<c:url value="/page/${currentPage-1}"></c:url>">&laquo;</a></li>
  		</c:when>
  	</c:choose>
  	
	<!-- Numbers of pages to show -->
  	<c:if test="${currentPage-2 > 0}">
  		<li class="disabled"><span>...</span></li>
  	</c:if>
  	<c:forEach var="pageNum" begin="${currentPage-1}" end="${currentPage+2}">
  		<c:choose>
	  		<c:when test="${pageNum eq currentPage}">
  				<li class="active"><span><c:out value="${pageNum}" /></span></li>
	  		</c:when>
	  		<c:otherwise>
	  			<c:if test="${pageNum > 0 && pageNum <= pagesCount}">
  					<li>
  						<a href="<c:url value="/page/${pageNum}">
  							</c:url>"><c:out value="${pageNum}" /></a></li>
  				</c:if>
	  		</c:otherwise>
  		</c:choose>
  	</c:forEach>
  	<c:if test="${currentPage+2 < pagesCount}">
  		<li class="disabled"><span>...</span></li>
  	</c:if>
  	
  	<!-- Show links to the last and next pages -->
  	<c:choose>
  		<c:when test="${currentPage < pagesCount}">
  			<li><a href="<c:url value="/page/${currentPage+1}"></c:url>">&raquo;</a></li>
  			<li>
  				<a href="<c:url value="/page/${pagesCount}">
  					</c:url>"><spring:message code="home.lastPage" /> &raquo;</a></li>
  		</c:when>
  	</c:choose>
  </ul>
</div> <!-- Page-navigation end -->
</div> <!-- Container end -->

<jsp:include page="common/footer.jsp" />
