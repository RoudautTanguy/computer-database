<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
<link rel="icon" href="favicon.ico" />
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
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
			<h1 id="homeTitle">
				<c:out value="<b class=\"number\">${ count } </b>" escapeXml="false">Error</c:out>
				<spring:message code="dashboard.found" />
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" method="GET" class="form-inline">
						<spring:message code="dashboard.search_placeholder"
							var="search_placeholder_text" />
						<spring:message code="dashboard.filter_btn" var="filter_btn_text" />

						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="${search_placeholder_text }" />
						<input type="submit" id="searchsubmit" value="${filter_btn_text}"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addComputer"><spring:message
							code="all.add_computer" /></a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();"><spring:message code="all.edit" /></a>
				</div>
			</div>
		</div>

		<form id="deleteForm" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<div id="orderBy" class="hidden">
				<c:out value="${orderBy}">0</c:out>
			</div>
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;">
							<input type="checkbox" id="selectall" /> 
							<span style="vertical-align: top;"> - 
								<a id="deleteSelected" onclick="$.fn.deleteSelected();"> 
									<em class="fa fa-trash-o fa-lg"></em>
								</a>
							</span>
						</th>
						<th>Computer name <a><em class="fa fa-sort"></em></a>
						</th>
						<th>Introduced date <a><em class="fa fa-sort"></em></a>
						</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date <a><em class="fa fa-sort"></em></a>
						</th>
						<!-- Table header for Company -->
						<th>Company <a><em class="fa fa-sort"></em></a>
						</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach items="${ computers }" var="computer">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
							<td><a href="editComputer/${computer.id}" onclick=""><c:out
										value="${computer.name}" escapeXml="true"></c:out></a></td>
							<td>${computer.introduced}</td>
							<td>${computer.discontinued}</td>
							<td>${computer.company}</td>

						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul id="pagination" class="pagination">
				<li id="First"><a href="?page=1" aria-label="First"> <span
						aria-hidden="true">&laquo;</span>
				</a></li>
				<li id="Previous"><a href="?page=${currentPage - 1 }"
					aria-label="Previous"> <span aria-hidden="true">&#8249;</span>
				</a></li>
				<li><a href="?page=${pagination-2}">${pagination-2}</a></li>
				<li><a href="?page=${pagination-1}">${pagination-1}</a></li>
				<li><a href="?page=${pagination}">${pagination}</a></li>
				<li><a href="?page=${pagination+1}">${pagination+1}</a></li>
				<li><a href="?page=${pagination+2}">${pagination+2}</a></li>
				<li id="Next"><a href="?page=${currentPage + 1 }"
					aria-label="Next"> <span aria-hidden="true">&#8250;</span>
				</a></li>
				<li id="Last"><a href="?page=${ lastPage }" aria-label="Last">
						<span aria-hidden="true">&raquo;</span>
				</a></li>
			</ul>

			<ul id="size" class="pagination pull-right" current-size="${size}">
				<li><a href="?size=10">10</a></li>
				<li><a href="?size=50">50</a></li>
				<li><a href="?size=100">100</a></li>
			</ul>
		</div>

	</footer>
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>
</html>