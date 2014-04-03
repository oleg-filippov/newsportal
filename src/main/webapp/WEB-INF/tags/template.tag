<%@ tag language="java" description="page template" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="title"%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap.min.css"/>" />
		<link rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min.css"/>" />
		<link rel="stylesheet" href="<c:url value="/resources/css/summernote.css"/>" />
		<link rel="stylesheet" href="<c:url value="/resources/css/newsportal.css"/>" />
		<link rel="stylesheet" href="<c:url value="/resources/css/tagmanager.css"/>" />
		<link rel="stylesheet" href="<c:url value="/resources/css/typeahead.js-bootstrap.css"/>" />
		
 		<title>${title}</title>
 	</head>
	<body>
	<div id="wrap">
		<jsp:include page="../views/common/header.jsp"></jsp:include>
		<div class="container">
			<div class="row">
				<div class="col-xs-9">
					<jsp:doBody/>
				</div>
				<div class="col-xs-3">
					<jsp:include page="../views/common/sidebar.jsp"></jsp:include>
				</div>
			</div>
		</div>
    </div>
	<jsp:include page="../views/common/footer.jsp"></jsp:include>
	</body>
</html>