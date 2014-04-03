<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="profile.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<t:template title="${title}">
<jsp:body>

<h3><spring:message code="profile.header"></spring:message> ${user.login}</h3>
<hr>
<h4><spring:message code="user.registered"></spring:message>: 
	<fmt:formatDate value="${user.registered}" type="both" /></h4>
<h4><spring:message code="user.name"></spring:message>: ${user.name}</h4>
<h4><spring:message code="user.email"></spring:message>: ${user.email}</h4>

</jsp:body>
</t:template>