<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:set var="title" value="${news.title}" />
<jsp:include page="common/header.jsp">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<c:url value="/user" var="profileUrl" />
<c:url value="/signin" var="signinUrl" />
<c:url value="/news/${news.id}/edit" var="editNewsUrl" />
<c:url value="${contextPath}/news/${news.id}/delete" var="deleteNewsUrl" />
<c:url value="/news/${news.id}/addcomment" var="addCommentUrl" />

<div class="container">
<h2><c:out value="${news.title}" /></h2>
<hr>
<sec:authorize access="isAuthenticated()">
	<c:if test="${loggedUser.id == news.author.id}">
		<a href="${editNewsUrl}"><spring:message code="news.editNewsUrl" /></a> | 
		<a class="btn-link" onclick="deleteConfirm()"><spring:message code="news.deleteNewsUrl" /></a><br>
	</c:if>
</sec:authorize>

<small>
<i class="icon-user"></i>
<spring:message code="news.author" />: 
<a href="${profileUrl}/${news.author.id}"><strong>${news.author.login}</strong></a>
<%-- <c:out value="${news.author.login}" /> --%>
<br>
<i class="icon-time"></i>
<spring:message code="news.created" />: 
<fmt:formatDate value="${news.created}" type="both"
	pattern="dd.MM.y H:mm" />
<br>
<c:if test="${news.lastModified != null}">
	<i class="icon-time"></i>
	<spring:message code="news.lastModified" />: 
	<fmt:formatDate value="${news.lastModified}" type="both"
		pattern="dd.MM.y H:mm" />
	<br>
</c:if>
<i class="icon-comment"></i>
<spring:message code="news.comments" />: 
<c:out value="${news.commentsCount}" />
<br>

</small>
<br><br>
<c:out value="${news.content}" escapeXml="false" />

<hr>
<sec:authorize access="isAnonymous()">
	<a href="${signinUrl}"><spring:message code="viewnews.loginComments" /></a>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_USER')">
	<form:form action="${addCommentUrl}" method="post" commandName="comment" >
		<table>
			<tr>
				<td><form:errors path="content" class="label label-important" /></td>
			</tr>
			<tr>
				<td>
				<spring:bind path="content">
					<textarea id="redex" name="content" class="field span6" rows="4" maxlength="500"
						placeholder="<spring:message code="viewnews.leaveComment" />" ></textarea>
				</spring:bind>
				</td>
			</tr>
		</table>
		<button class="btn" type="submit"><spring:message code="viewnews.addCommentButton" /></button>
  		<button class="btn" type="reset"><spring:message code="viewnews.resetCommentButton" /></button>
	</form:form>
</sec:authorize>

<c:choose>
	<c:when test="${news.commentsCount > 0}">
		<h2><spring:message code="viewnews.commentsToNews" /></h2>
		<c:forEach var="comment" items="${comments}">
			<div class="well well-small">
				<small>
					<a href="${profileUrl}/${comment.author.id}"><strong>${comment.author.login}</strong></a> -
					<i class="icon-time"></i>
					<fmt:formatDate value="${comment.created}" type="both"
						pattern="dd.MM.y H:mm:ss" />
				</small><br>
				<c:out value="${comment.content}" />
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<h2><spring:message code="viewnews.noComments" /></h2>
	</c:otherwise>
</c:choose>
			
</div> <!-- end of container -->

<script>
	$('textarea[maxlength]').maxlength({threshold:20});
	$(function(){$('textarea').val('');});
	function deleteConfirm(url){
        if (confirm("Are you sure?")){
        	location.href="${deleteNewsUrl}";
        };
    };
</script>

<jsp:include page="common/footer.jsp" />