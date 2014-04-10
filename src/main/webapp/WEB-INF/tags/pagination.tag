<%@ tag language="java" description="Displays a list of news"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="pagesCount" required="true"
	description="Number of pages"%>
<%@ attribute name="currentPage" required="true"%>

<!-- Pagination -->
<c:if test="${pagesCount > 1}">
	<div class="text-center">
		<ul class="pagination">
			<!-- Show links to the first and prev pages -->
			<c:choose>
				<c:when test="${currentPage > 1}">
					<li><a href="<c:url value="/"></c:url>"> &laquo; <spring:message
								code="home.firstPage" /></a></li>
					<li><a href="<c:url value="/page/${currentPage-1}"></c:url>">&laquo;</a></li>
				</c:when>
			</c:choose>

			<!-- Numbers of pages to show -->
			<c:if test="${currentPage-2 > 0}">
				<li class="disabled"><span>...</span></li>
			</c:if>
			<c:forEach var="pageNum" begin="${currentPage-1}"
				end="${currentPage+2}">
				<c:choose>
					<c:when test="${pageNum eq currentPage}">
						<li class="active"><span><c:out value="${pageNum}" /></span></li>
					</c:when>
					<c:otherwise>
						<c:if test="${pageNum > 0 && pageNum <= pagesCount}">
							<li><a
								href="<c:url value="/page/${pageNum}">
			  					</c:url>"><c:out
										value="${pageNum}" /></a></li>
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
					<li><a
						href="<c:url value="/page/${pagesCount}">
			  			</c:url>"><spring:message
								code="home.lastPage" /> &raquo;</a></li>
				</c:when>
			</c:choose>
		</ul>
	</div>
	<!-- Pagination -->
</c:if>