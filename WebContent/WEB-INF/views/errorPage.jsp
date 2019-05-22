<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
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
			<a class="navbar-brand" href="dashboard"> Application - Computer
				Database </a>
			<div class="lang-selector">
				<a href="?lang=en">EN</a> <a href="?lang=fr">FR</a>
			</div>
		</div>
	</header>

	<section id="main">
		<div id="container" class="container">
			<div class="errorCode">${errorCode}</div>
			<div class="errorMsg">${errorMsg}</div>
			<div class="info">Check the URL you entered for any mistakes
				and try again.</div>
			<a class="btn btn-primary" href="dashboard">Go Home</a>
		</div>
	</section>

	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>