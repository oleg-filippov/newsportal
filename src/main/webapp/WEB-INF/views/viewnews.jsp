<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">${news.title} | <spring:message code="project.title" /></c:set>

<t:template title="${title}">
<jsp:body>

<c:url value="/user" var="profileUrl" />
<c:url value="/signin" var="signinUrl" />
<c:url value="/signup" var="signupUrl" />
<c:url value="/news/${news.id}/edit" var="editNewsUrl" />
<c:url value="${contextPath}/news/${news.id}/delete" var="deleteNewsUrl" />
<c:url value="/news/${news.id}/addcomment" var="addCommentUrl" />

<!-- Logged user is admin -->
<c:set var="isAdmin" value="false" />
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="isAdmin" value="true" />
</sec:authorize>

<!-- Logged user is author of this news -->
<c:set var="isThisNewsAuthor" value="false" />
<sec:authorize access="hasRole('ROLE_AUTHOR')">
	<c:if test="${loggedUser.id == news.author.id}">
		<c:set var="isThisNewsAuthor" value="true" />
	</c:if>
</sec:authorize>

<h2><c:out value="${news.title}" /></h2>

<!-- Show Edit/Delete links -->
<sec:authorize access="hasRole('ROLE_AUTHOR')">
	<c:if test="${isThisNewsAuthor || isAdmin}">
		<a href="${editNewsUrl}" class="btn btn-info btn-xs" type="button">
			<i class="glyphicon glyphicon-edit"></i> <spring:message code="news.editNewsUrl" /></a>
		<a href="${deleteNewsUrl}" class="btn btn-danger btn-xs" type="button"
			onclick="return deleteConfirm();"><i class="glyphicon glyphicon-exclamation-sign">
				</i> <spring:message code="news.deleteNewsUrl" /></a><br>
	</c:if>
</sec:authorize>
<hr>

<small>
<i class="icon-user"></i>
<a href="${profileUrl}/${news.author.id}"><strong>${news.author.login}</strong></a>
<br>
<i class="icon-time"></i>
<spring:message code="news.created" />: 
<fmt:formatDate value="${news.created}" type="both" />
<br>
<c:if test="${news.lastModified != null}">
	<i class="icon-time"></i>
	<spring:message code="news.lastModified" />: 
	<fmt:formatDate value="${news.lastModified}" type="both" />
	<br>
</c:if>
<i class="icon-comment"></i>
<spring:message code="news.comments" />: 
<c:out value="${news.commentsCount}" />
<br>
</small>
<br>


<c:if test="${not empty news.tags}">
	<c:forEach var="curTag" items="${news.tags}">
		<c:out value="${curTag.name}" />
	</c:forEach>
</c:if>


<c:out value="${news.content}" escapeXml="false" />
<hr>

<!-- Message for not registered users -->
<sec:authorize access="isAnonymous()">
	<c:set var="signin">
		<a href="${signinUrl}"><spring:message code="viewnews.loginComments.signin" /></a>
	</c:set>
	<c:set var="signup">
		<a href="${signupUrl}"><spring:message code="viewnews.loginComments.signup" /></a>
	</c:set>
	<spring:message code="viewnews.loginComments" arguments="${signin},${signup}"/>
</sec:authorize>

<!-- Add comments form -->
<sec:authorize access="hasRole('ROLE_USER')">
	<form:form action="${addCommentUrl}" method="post" modelAttribute="comment" >
		<h4><form:errors path="content" class="label label-danger" /></h4>
		<spring:bind path="content">
			<textarea class="form-control" name="content" maxlength="500" rows="3"
				placeholder="<spring:message code="viewnews.leaveComment" />" ></textarea>
		</spring:bind>
		<button class="btn" type="submit"><spring:message code="viewnews.addCommentButton" /></button>
  		<button class="btn" type="reset"><spring:message code="viewnews.resetCommentButton" /></button>
	</form:form>
</sec:authorize>

<!-- List of comments -->
<c:choose>
	<c:when test="${news.commentsCount > 0}">
		<h2 id="comments"><spring:message code="viewnews.commentsToNews" /></h2>
		<c:forEach var="comment" items="${comments}">
			<div class="well well-small">
				<ul class="list-inline">
					<li><a href="${profileUrl}/${comment.author.id}">
						<strong>${comment.author.login}</strong></a></li>
					<li><i class="glyphicon glyphicon-time"></i>
						<fmt:formatDate value="${comment.created}" type="both" /></li>
				</ul>
				<c:out value="${comment.content}" escapeXml="false" />
			</div>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<h2><spring:message code="viewnews.noComments" /></h2>
	</c:otherwise>
</c:choose>

<script>
$('textarea[maxlength]').maxlength({threshold:20});
$(function(){$('textarea').val('');});

function deleteConfirm(url){
	var message;
	if ("${pageContext.response.locale.language}" === "ru")
		message = "Вы уверены?";
	else
		message = "Are you sure?";
	if (confirm(message)){
		location.href="${deleteNewsUrl}";
	};
	return false;
};
</script>

</jsp:body>
</t:template>