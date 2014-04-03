<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="editnews.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
<jsp:body>

<form:form method="post" modelAttribute="news">
	<!-- News title -->
	<h4><form:errors path="title" class="label label-danger" /></h4>
	<spring:bind path="title">
		<input name="title" type="text" class="form-control" maxlength="100"
			placeholder="<spring:message code="news.title" />" value="${news.title}" >
	</spring:bind>
	
	<!-- News tags -->
	<h4><form:errors path="tags" class="label label-danger" /></h4>
	<input id="tags" type="text" name="tagsString" value="${tagsString}"
		placeholder="Add tags" />

	<!-- News preview -->
	<h4><form:errors path="preview" class="label label-danger" /></h4>
	<spring:bind path="preview">
		<textarea class="form-control" name="preview" rows="3" maxlength="250"
			placeholder="<spring:message code="news.preview" />">${news.preview}</textarea>
	</spring:bind>
	
	<!-- News content -->
	<h4><form:errors path="content" class="label label-danger" /></h4>
		<h4><span id="resp" class="label label-danger"></span></h4>
	<spring:bind path="content">
		<textarea id="summernote" name="content">${news.content}</textarea>
	</spring:bind>
	
	<!-- Buttons -->
  	<button class="btn btn-primary" type="submit"><spring:message code="editnews.saveButton" /></button>
  	<c:set var="newsId" value="${news.id}"></c:set>
  	<c:if test="${empty newsId}"><c:set var="newsId" value="0"></c:set></c:if>
  	<a class="btn btn-default" href="${pageContext.request.contextPath}/news/${newsId}/cancel">
  		<spring:message code="editnews.cancelButton" /></a>
</form:form>

<script type="text/javascript" src="<c:url value="/resources/js/tagmanager.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/typeahead.jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/summernote.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/summernote-ru-RU.js"/>"></script>
<script>
$('input[maxlength], textarea[maxlength]').maxlength({threshold:20});
$(document).ready(function() {
	var lang = ("${pageContext.response.locale.language}" === "ru")
		? "ru-RU"
		: "en-US";
	$('#summernote').summernote({
		height: 400,
		lang: lang,
		onImageUpload: function(files, editor, welEditable) {
            sendFile(files[0],editor,welEditable);
        }
	});
	
	
	var tagApi = jQuery("#tags").tagsManager();
	$("#tags").typeahead({
	    name: 'countries',
	    limit: 15,
	    prefetch: '${pageContext.request.contextPath}/news/tags/autocomplete'
	  }).on('typeahead:selected', function (e, d) {

	    tagApi.tagsManager("pushTag", d.value);

	  });
	
	var colors = ["red", "blue", "green", "yellow", "brown", "black"];
	 
	/* $('#tags').typeahead({source: colors}); */
	
	$.ajax({
  	  url: '${pageContext.request.contextPath}/news/tags/autocomplete',
  	  dataType: 'json',
  	  success: function (data) {
            /* alert(data); */
            var local = ["tag","tag2"];
            /* alert(local); */
      }
  	});

	

	/* alert($.get('${pageContext.request.contextPath}/news/tags/autocomplete')); */
	
		        
});

function sendFile(file,editor,welEditable) {
	data = new FormData();
	data.append("file", file);
	$.ajax({
		data: data,
		type: "POST",
		url: "${pageContext.request.contextPath}/uploadimage",
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

</jsp:body>
</t:template>