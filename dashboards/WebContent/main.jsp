<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/app/shared/includes.jsp"%>

<script src="/app/components/main/main.controller.js"></script>
<title>MINFIN - Tableros</title>
</head>
<body ng-app="dashboards" ng-controller="MainController as mainController">
<%@ include file="/app/components/menu/menu.jsp" %>
	<div id="mainview">
		<div ng-view></div>
    </div>
    <div class="footer">- Minfin 2017 -</div>
</body>
</html>

