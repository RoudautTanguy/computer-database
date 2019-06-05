<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/jquery-ui.css"
	rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/main.css"
	rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand"
				href="${pageContext.request.contextPath}/dashboard"> Application
				- Computer Database </a>
			<div class="lang-selector">
				<a href="?lang=en">EN</a> <a href="?lang=fr">FR</a>
			</div>
		</div>

	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Login</h1>
					<form id="login" action="login" method="POST">
						<div class="form-group">
							<label class="control-label" for="username">Username</label>
							<input type="text" class="form-control"
								id="username" name="username"
								placeholder="Username" />
						</div>
						<div class="form-group">
							<label class="control-label" for="password">Username</label>
							<input type="password" class="form-control"
								id="password" name="password"
								placeholder="Password" />
						</div>
						<div>
							<input type="submit" value="Sign In" class="btn btn-primary">
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/editComputer.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
</html>