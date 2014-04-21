<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="title">
	<spring:message code="signup.pageTitle" /> | <spring:message code="project.title" />
</c:set>

<c:set var="loginNotFree"><spring:message code="validation.user.unique.login" /></c:set>
<c:set var="emailNotFree"><spring:message code="validation.user.unique.email" /></c:set>

<t:template title="${title}">
	<jsp:body>
		<form:form name="form-s" class="form-signin" method="post" modelAttribute="user"
				data-toggle="validator">
			
			<!-- Signup header -->
			<h2 class="form-signin-heading">
				<spring:message code="signup.pageTitle" />
			</h2>
			
			<!-- Login -->
			<div class="form-group">
				<h4><form:errors path="login" class="label label-danger"></form:errors></h4>
				<div id="login-err" class="help-block with-errors"></div>
				<spring:bind path="login">
					<input id="login" type="text" name="login" class="form-control" autofocus
						pattern="^([_A-z0-9]){4,}$" maxlength="30" value="${user.login}"
						placeholder="<spring:message code="user.login" />" required
						data-error="<spring:message code="validation.user.loginSize" />"><span></span>
				</spring:bind>
			</div>
			
			<!-- Password -->
			<div class="form-group">
				<h4><form:errors path="password" class="label label-danger"></form:errors></h4>
				<div class="help-block with-errors"></div>
				<spring:bind path="password">
					<input type="password" name="password" id="password" class="form-control"
						data-minlength="7" maxlength="60"
						placeholder="<spring:message code="user.password" />" value="${user.password}"
						data-error="<spring:message code="validation.user.passwordSize" />">
				</spring:bind>
			</div>
			
			<!-- Confirm password -->
			<div class="form-group">
			<div class="help-block with-errors"></div>
				<input type="password" class="form-control" maxlength="60" value="${user.password}"
					placeholder="<spring:message code="user.passwordConfirm" />"
					required data-match="#password"
					data-match-error="<spring:message code="validation.user.passwordMatch" />">
			</div>
			
			<!-- Email -->
			<div class="form-group">
				<h4><form:errors path="email" class="label label-danger"></form:errors></h4>
				<div id="email-err" class="help-block with-errors"></div>
				<spring:bind path="email">
					<input id="email" type="email" name="email" class="form-control" maxlength="50"
						placeholder="<spring:message code="user.email" />" value="${user.email}"
						required data-error="<spring:message code="validation.user.emailValid" />">
				</spring:bind>
			</div>
			
			<!-- Name -->
			<div class="form-group">
				<h4><form:errors path="name" class="label label-danger"></form:errors></h4>
				<div class="help-block with-errors"></div>
				<spring:bind path="name">
					<input type="text" name="name" class="form-control" maxlength="50" required
						placeholder="<spring:message code="user.name" />" value="${user.name}"
						data-error="<spring:message code="validation.user.nameNotBlank" />">
				</spring:bind>
			</div>
			
			<!-- Submit button -->
			<button id="submit" class="btn btn-lg btn-primary btn-block" type="submit">
				<spring:message code="signup.submitButton" />
			</button>
		</form:form>
		
		<script type="text/javascript" src="<c:url value="/resources/js/app/inputs.js"/>"></script>
		<script>
			$("#login").blur(function() {
				login =  $("#login").val();
				var isLoginValid = /^([_A-z0-9]){4,}$/g.test(login);
				if (isLoginValid) {
					$.ajax({
						url:  "${pageContext.request.contextPath}/check-login",
						data:  {"login": login},
						cache:  false,
						success:  function(response) {
							if (response  == "no") {
								$("#login-err").text("${loginNotFree}").css("color", "#A94442").show();
								$("#submit").attr("disabled", "disabled");
							} else if (response  == "ok") {
								$("#login-err").hide();
							}
						}
					});
				}
			});
			
			$("#email").blur(function() {
				email =  $("#email").val();
				var isEmailValid = /^([_A-z0-9])+@.+$/g.test(email);
				if(isEmailValid) {
					$.ajax({
						url:  "${pageContext.request.contextPath}/check-email",
						data:  {"email": email},
						cache:  false,
						success:  function(response) {
							if (response  == "no") {
								$("#email-err").text("${emailNotFree}").css("color", "#A94442").show();
								$("#submit").attr("disabled", "disabled");
							} else if (response  == "ok") {
								$("#email-err").hide();
							}
						}
					});
				}
			});
		</script>
	</jsp:body>
</t:template>