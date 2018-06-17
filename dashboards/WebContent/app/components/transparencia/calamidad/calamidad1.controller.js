/**
 * 
 */


angular.module('calamidadController',['dashboards']).controller('calamidadController',['$scope','$routeParams','$http','$location','uiGmapGoogleMapApi','$rootScope',
	   function($scope,$routeParams,$http,$location,uiGmapGoogleMapApi, $rootScope){
	
	this.num_compras=0;
	this.num_donaciones=0;
	this.num_documentos=0;
	this.num_actividades=0;
	this.ejecucion_fisica = 0.0;
	this.ejecucion_financiera = 0.0;
	this.titulo = '';
	this.latitude = null;
	this.longitude = null;
	this.tipo = null;
	if($routeParams.subprograma===undefined)
		$routeParams.subprograma = 7;
	this.subprograma = $routeParams.subprograma;
	
	
	$http.post('/STransparenciaVentanas', { t: (new Date()).getTime(), subprograma: this.subprograma }).then(function(response){
	    if(response.data.success){
	    	this.num_documentos = response.data.results.documentos;
	    	this.num_actividades = response.data.results.actividades;
	    	this.num_compras = response.data.results.compras;
	    	this.num_donaciones = response.data.results.donaciones;
	    	this.ejecucion_financiera = response.data.results.ejecucion_financiera;
	    	this.ejecucion_fisica = response.data.results.ejecucion_fisica;
	    	this.titulo = response.data.results.titulo;
	    	this.latitude = response.data.results.latitude;
	    	this.longitude = response.data.results.longitude;
	    	this.tipo = response.data.results.tipo;
	    	this.subprograma = $routeParams.subprograma;
	    	
	    	$rootScope.titulo = this.titulo;
	    	$rootScope.tipo = this.tipo;
	    	
	    	uiGmapGoogleMapApi.then(function() {
	       		$scope.mapcalamidad = { center: { latitude: this.latitude, longitude: this.longitude }, 
	       					   zoom: 15,
	       					   options: {
	       						   scrollwheel: false,
	       						   mapTypeId: google.maps.MapTypeId.SATELLITE,
	       						   disableDefaultUI: true
	       					   },
	       					   refresh: true
	       					};
	           }.bind(this));
		}
 	}.bind(this), function errorCallback(response){
 		
 	});
	
	$scope.go = function ( path ) {
		  $location.path( path );
	};
	
}]);

