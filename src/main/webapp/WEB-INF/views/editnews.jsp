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

<!-- Container begin -->
<div class="container">
<form:form method="post" commandName="news">
	<table>
		<tr>
			<td><form:errors path="title" class="label label-important" /></td>
		</tr>
		<tr>
			<td>
				<spring:bind path="title">
					<input name="title" type="text" class="span12" maxlength="100"
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
					<textarea class="span12" name="preview" rows="3" maxlength="250"
						placeholder="<spring:message code="news.preview" />">${news.preview}</textarea>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td><form:errors path="content" class="label label-important" />
			<span id="resp" class="label label-important"></span>
			</td>
		</tr>
		<tr>
			<td>
				<spring:bind path="content">
					<div class="summernote container" id="ddd">
						<textarea id="summernote" class="span12" name="content">${news.content}</textarea>
  					</div>
				</spring:bind>
			</td>
		</tr>
	</table>
  	<button class="btn btn-primary" type="submit"><spring:message code="editnews.saveButton" /></button>
  	
  	<c:set var="newsId" value="${news.id}"></c:set>
  	<c:if test="${empty newsId}"><c:set var="newsId" value="0"></c:set></c:if>
  	<a class="btn btn-default" href="${pageContext.request.contextPath}/news/${newsId}/cancel">
  		<spring:message code="editnews.cancelButton" />
  	</a>
</form:form>

</div> <!-- Container end -->

<script>
$('input[maxlength], textarea[maxlength]').maxlength({threshold:20});
$(document).ready(function() {
	var lang = ("${pageContext.response.locale.language}" === "ru")
		? "ru-RU"
		: "en-US";
	$('#summernote').summernote({
		height: 500,
		lang: lang,
		onImageUpload: function(files, editor, welEditable) {
            sendFile(files[0],editor,welEditable);
        }
	});
});

function sendFile(file,editor,welEditable) {
	data = new FormData();
	data.append("file", file);
	$.ajax({
		data: data,
		type: "POST",
		url: "${pageContext.request.contextPath}/news/uploadimage",
		cache: false,
		contentType: false,
		processData: false,
		success: function(response) {
			if (/^images/.test(response)) {
				editor.insertImage(welEditable, response);
				$("#resp").hide();
			} else {
				$("#resp").text(response).show();
			}
		}
	});
}
</script>

<jsp:include page="common/footer.jsp" />