<%@ tag language="java" description="Displays a list of news" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="pageCount" required="true" description="Number of pages"%>
<%@ attribute name="currentPage" required="true"%>

<c:set var="rootUrl" value="${pageContext.request.contextPath}${requestUrl}"></c:set>

<!-- Pagination -->
<c:if test="${pageCount > 1}">
	<div class="text-center">
		<ul class="pagination">
			<!-- Show link to the first page -->
			<c:if test="${currentPage > 3}">
				<li><a href="${rootUrl}">1</a></li>
				<!-- Show link to the second page or '...' -->
				<c:choose>
					<c:when test="${currentPage > 5}">
						<li class="disabled"><span>...</span></li>
					</c:when>
					<c:otherwise>
						<c:if test="${currentPage > 4}">
							<li><a href="${rootUrl}page/2">2</a></li>
						</c:if>
					</c:otherwise>
				</c:choose>
			</c:if>

			<!-- Numbers of pages to show -->
			<c:forEach var="pageNum" begin="${currentPage-1}" end="${currentPage+3}">
				<c:choose>
					<c:when test="${pageNum-1 eq currentPage}">
						<li class="active"><span>${pageNum-1}</span></li>
					</c:when>
					<c:otherwise>
						<c:if test="${pageNum-1 > 0 && pageNum-1 <= pageCount}">
							<li><a href="${rootUrl}page/${pageNum-1}">${pageNum-1}</a></li>
						</c:if>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<!-- Show link to the last page -->
			<c:if test="${currentPage < pageCount-2}">
				<!-- Show link to the pre-last page or '...' -->
				<c:choose>
					<c:when test="${currentPage < pageCount-4}">
						<li class="disabled"><span>...</span></li>
					</c:when>
					<c:otherwise>
						<c:if test="${currentPage < pageCount-3}">
							<li><a href="${rootUrl}page/${pageCount-1}">${pageCount-1}</a></li>
						</c:if>
					</c:otherwise>
				</c:choose>
				<li><a href="${rootUrl}page/${pageCount}">${pageCount}</a></li>
			</c:if>
		</ul>
	</div>
</c:if>