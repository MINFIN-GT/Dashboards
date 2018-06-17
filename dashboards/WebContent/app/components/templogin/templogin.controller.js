/**
 * 
 */
 
 var app=angular.module('dashboards',[]).config(function($locationProvider) {
	 $locationProvider.html5Mode({
		  enabled: true,
		  requireBase: false
		});
 });
 
 app.controller('TemploginController',['$scope','$http','$location',function($scope,$http,$location){
	 this.password = null;
	 this.password_check = null;
	 this.errorMessage=null;
	 
	 
	 this.user = $location.search().user;
	 this.hash = $location.search().access;
	 
	 if(this.user==null || this.user==''){
		 window.location.href = '/login.jsp';
	 }
	 else{
		 this.hash = decodeURIComponent(this.hash);
		 $scope.showerror = false;
	 }
	 
	 this.changePassword=function(){
		 if(this.password!=null && this.password!='' && this.password==this.password_check){
			 var data = { username: this.user, password: this.password, hash: this.hash};
			 $http.post('/STempLogin', data).then(function(response){
				    if(response.data.success)
				    	window.location.href = '/main.jsp';
				    else{
				    	$scope.showerror = true;
				    	this.errorMessage = "El link de acceso es inválido";
				    }
			 	}.bind(this), function errorCallback(response){
			 		$scope.showerror = true;
			 		this.errorMessage = "Se genero un error al validar el link de acceso";
			 	}.bind(this)
			 );
		 }
		 else{
			 $scope.showerror=true;
			 this.errorMessage = "Debe indicar ambos campos de contraseña y deben ser iguales"
		 }
	 }.bind(this);
 }]);