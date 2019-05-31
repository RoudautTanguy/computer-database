<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
<link rel="icon" href="favicon.ico" />
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/font-awesome.css"
	rel="stylesheet" type="text/css" media="screen">
<link href="${pageContext.request.contextPath}/css/main.css"
	rel="stylesheet" type="text/css" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard"> Application - Computer
				Database </a>
			<div class="lang-selector">
				<a href="?lang=en">EN</a> <a href="?lang=fr">FR</a>
			</div>
		</div>
	</header>

	<section id="main">
		<div id="container" class="container">

			<div class="errorCode">${errorCode}</div>
			<div class="errorMsg">
				<spring:message code="${errorMsg}" />
			</div>
			<c:if test="${not empty info}">
				<div class="info">
					<spring:message code="${info}" />
				</div>
			</c:if>

			<a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard"> 
				<spring:message code="error.home_btn"/>
			</a>
		</div>
	</section>

	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/error.js"></script>
</body>
</html>