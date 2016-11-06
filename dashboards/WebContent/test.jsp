<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.CUserDAO" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<% 
	CUserDAO.createUser("pcastro", "0208!pc", "Perla", null, "Castro", null, "Centro de Gobierno", "Directora");
%>
</body>
</html>