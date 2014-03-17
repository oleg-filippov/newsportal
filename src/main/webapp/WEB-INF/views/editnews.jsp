<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<c:set var="title"> <spring:message code="editnews.pageTitle" /></c:set>
<jsp:include page="common/header.jsp" flush="true">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<div class="container">
<form:form method="post" commandName="news">
	<table>
		<tr>
			<td><form:errors path="title" class="label label-important" /></td>
		</tr>
		<tr>
			<td>
				<spring:bind path="title">
					<input name="title" type="text" class="field span10" maxlength="100"
						placeholder="<spring:message code="news.title" />" value="${news.title}" >
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td><form:errors path="preview" class="label label-important" /></td>
		</tr>
		<tr>
			<td>
				<spring:bind path="preview">
					<textarea class="field span10" name="preview" rows="3" maxlength="250"
						placeholder="<spring:message code="news.preview" />">${news.preview}</textarea>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td><form:errors path="content" class="label label-important" /></td>
		</tr>
		<tr>
			<td>
				<spring:bind path="content">
					<textarea id="content" name="content" class="field span10" rows="18"
						placeholder="<spring:message code="news.content" />">${news.content}</textarea>
				</spring:bind>
			</td>
		</tr>
	</table>
  	<button class="btn btn-primary" type="submit"><spring:message code="editnews.saveButton" /></button>
  	<button class="btn btn-danger" type="reset"><spring:message code="editnews.resetButton" /></button>
  	<a class="btn btn-default" href="${pageContext.request.contextPath}/news/cancel">
  		<spring:message code="editnews.cancelButton" /></a>
</form:form>

</div> <!-- end of container -->

<script>$('input[maxlength], textarea[maxlength]').maxlength({threshold:20});</script>

<jsp:include page="common/footer.jsp" />