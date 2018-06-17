<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="dashboards">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/app/shared/includes.jsp"%>
<link rel="stylesheet" type="text/css" href="/assets/css/loginform.css" />
<script src="/app/components/templogin/templogin.controller.js"></script>
<title>Tableros</title>
</head>
<body ng-controller="TemploginController as login">
<div class="container container-fluid">
		<div class="row-fluid">
		<div style="text-align:center;">Bienvenido usuario {{ login.user }} al sistema de tableros del MINFIN</div>
		<div style="text-align:center;">A continuación se le pedirá ingresar la contraseña que utilizará para el ingreso al sistema.</div>
		<div style="text-align:center;">Por favor ingrese la misma contraseña en ambos campos, luego de esto podra ingresar al sistema de forma normal.  Este paso se realizará una sola vez</div>
		<br/>
		<br/>
		<form class="login-form">
			
			    <label for="user" class="sr-only">Contraseña</label>
		        <input ng-model="login.password" ng-change="showerror=false" type="password" 
		        	id="inputUsername" class="form-control" placeholder="Contraseña" required autofocus>
		        <label for="pass" class="sr-only">Verificación Contraseña</label>
		        <input ng-model="login.password_check" ng-change="showerror=false" type="password" 
		        	id="inputPassword" class="form-control" placeholder="Verificación de Contraseña" required>
		        <br />
		        <div class="alert alert-danger text-center" role="alert" ng-show="showerror">{{ login.errorMessage }}</div>
		        <button class="btn btn-lg btn-primary btn-block" ng-click="login.changePassword()">Ingresar</button>
		      
		</form>
	    </div>
</div> 
</body>
</html>