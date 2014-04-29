<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="article-edit.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
	<jsp:body>
		<form:form method="post" modelAttribute="article" data-toggle="validator">
		
			<!-- Article title -->
			<div class="form-group">
				<h4><form:errors path="title" class="label label-danger" /></h4>
				<div class="help-block with-errors"></div>
				<spring:bind path="title">
					<input id="article-title" name="title" type="text" class="form-control" maxlength="100"
						placeholder="<spring:message code="article.title" />" value="${article.title}"
						required data-error="<spring:message code="validation.article.title" />">
				</spring:bind>
			</div>
			
			<!-- Article category -->
			<select class="form-control" name="categoryName">
				<option disabled="disabled" selected="selected">
					<spring:message code="article-edit.category" />
				</option>
				<c:forEach var="category" items="${categories}">
					<option><c:out value="${category.name}" /></option>
				</c:forEach>
			</select>
			
			<!-- Article tags -->
			<h4><form:errors path="tags" class="label label-danger" /></h4>
			<input id="tags" type="text" name="tagString" value="${tagString}"
				class="form-control" placeholder="<spring:message code="article-edit.tags" />" />
		
			<!-- Article preview -->
			<div class="form-group">
				<h4><form:errors path="preview" class="label label-danger" /></h4>
				<div class="help-block with-errors"></div>
				<spring:bind path="preview">
					<textarea class="form-control" name="preview" rows="2" maxlength="255"
						placeholder="<spring:message code="article.preview" />"
						required data-error="<spring:message code="validation.article.preview" />"
						>${article.preview}</textarea>
				</spring:bind>
			</div>
			
			<!-- Article content -->
			<div class="form-group">
				<h4><form:errors path="content" class="label label-danger" /></h4>
				<h4><span id="resp" class="label label-danger"></span></h4>
				<div class="help-block with-errors"></div>
				<spring:bind path="content">
					<textarea id="summernote" name="content">${article.content}</textarea>
				</spring:bind>
			</div>
			
			<!-- Buttons -->
		  	<button class="btn btn-primary" type="submit">
		  		<spring:message code="article-edit.saveButton" /></button>
		  	<c:set var="articleId" value="${article.id}" />
		  	<c:if test="${empty articleId}">
				<c:set var="articleId" value="0" />
			</c:if>
		  	<a class="btn btn-default" href="${pageContext.request.contextPath}/article/${articleId}/cancel">
		  		<spring:message code="article-edit.cancelButton" />
		  	</a>
		</form:form>

		<script type="text/javascript" src="<c:url value="/resources/js/app/inputs.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/app/article-edit.js"/>"></script>
		<script>
			// Summernote editor
			var lang = ("${pageContext.response.locale.language}" === "ru")
				? "ru-RU"
				: "en-US";
			$('#summernote').summernote({
				height: 400,
				lang: lang,
				onImageUpload: function (files, editor, welEditable) {
					sendFile(files[0], editor, welEditable);
				}
			});
		</script>
	</jsp:body>
</t:template>