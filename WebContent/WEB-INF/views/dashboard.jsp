<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/font-awesome.css" rel="stylesheet" type="text/css" media="screen">
<link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" type="text/css" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                <c:out value="<b class=\"number\">${ count }</b> Computers found" escapeXml="false">Error</c:out> 
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="#" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            Computer name
                        </th>
                        <th>
                            Introduced date
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
            	<c:forEach items="${ computers }" var="computer">
                    <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="${computer.id}">
                        </td>
                        <td>
                            <a href="editComputer?id=${computer.id}" onclick=""><c:out value="${computer.name}" escapeXml="true"></c:out></a>
                        </td>
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
            <ul class="pagination">
              <li id="First">
                  <a href="?page=1" aria-label="First">
                    <span aria-hidden="true">&laquo;</span>
                  </a>
              </li>
              <li id="Previous">
                  <a href="?page=${currentPage - 1 }" aria-label="Previous">
                    <span aria-hidden="true">&#8249;</span>
                  </a>
              </li>
              <li><a href="?page=${pagination-2}">${pagination-2}</a></li>
              <li><a href="?page=${pagination-1}">${pagination-1}</a></li>
              <li><a href="?page=${pagination}">${pagination}</a></li>
              <li><a href="?page=${pagination+1}">${pagination+1}</a></li>
              <li><a href="?page=${pagination+2}">${pagination+2}</a></li>
              <li id ="Next">
                  <a href="?page=${currentPage + 1 }" aria-label="Next">
                    <span aria-hidden="true">&#8250;</span>
                  </a>
              </li>
              <li id="Last">
                <a href="?page=${ lastPage }" aria-label="Last">
                    <span aria-hidden="true">&raquo;</span>
                </a>
              </li>
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