<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="title"> <spring:message code="profile.pageTitle" /></c:set>
<jsp:include page="common/header.jsp">
	<jsp:param name="pageTitle" value="${title}" />
</jsp:include>

<div class="container">
	<h3><spring:message code="profile.header"></spring:message> ${user.login}</h3>
	<hr>
	<h4><spring:message code="user.registered"></spring:message>: 
		<fmt:formatDate value="${user.registered}" type="both" /></h4>
	<h4><spring:message code="user.name"></spring:message>: ${user.name}</h4>
	<h4><spring:message code="user.email"></spring:message>: ${user.email}</h4>
</div>

<jsp:include page="common/footer.jsp" />