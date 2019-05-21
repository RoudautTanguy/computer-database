<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/jquery-ui.css" rel="stylesheet" media="screen">
<link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    <form:form id="addComputer" action="addComputer" method="POST" modelAttribute="dtoComputer">
                        <fieldset>
                        	<legend>Computer</legend>
                            <div id="name-group" class="form-group">
                                <form:label path="name" class="control-label" for="computerName">Computer name</form:label>
                                <form:input path="name" type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name"/>
                            </div>
                            <div id="introduced-group" class="form-group">
                                <form:label path="introduced" class="control-label" for="introduced">Introduced date</form:label>
                                <form:input path="introduced" type="text" class="form-control" id="introduced" name="introduced" placeholder="Introduced date"/>
                            </div>
                            <div id="discontinued-group" class="form-group">
                                <form:label path="discontinued" class="control-label" for="discontinued">Discontinued date</form:label>
                                <form:input path="discontinued" type="text" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date"/>
                            </div>
                            <div id="company-group" class="form-group">
                                <form:label path="company" class="control-label" for="companyId">Company</form:label>
                                <form:select path="company" class="form-control" id="companyId" name="companyId">
                                    <option value="0">--</option>
                                    <c:forEach items="${ companies }" var="company">
                                    	<option value="${ company.id }">${ company.name }</option>
                                    </c:forEach>
                                </form:select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <a href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </section>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/addComputer.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
</html>