/**
 * 
 */


angular.module('calamidadMapaController',['dashboards']).controller('calamidadMapaController',['$scope','$routeParams','$http','uiGmapGoogleMapApi',
	   function($scope,$routeParams,$http,uiGmapGoogleMapApi){
	
	this.lastupdate='';
	this.actividades='';
	this.titulo ='';
	this.tipo = '';
	this.longitude = null;
	this.latitude = null;
	
	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.lastupdate = response.data.lastupdate;
			}
		}.bind(this)
	);
	
	$http.post('/STransparenciaEstadosCalamidad', { action: 'getEstado',subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.titulo = response.data.results.nombre;
	    	this.tipo = response.data.results.tipo;
	    	this.latitude = response.data.results.latitude;
	    	this.longitude = response.data.results.longitude;
	    	
	    	uiGmapGoogleMapApi.then(function() {
	       		$scope.mapcalamidad = { center: { latitude: this.latitude, longitude: this.longitude }, 
	       					   zoom: 15,
	       					   options: {
	    						   streetViewControl: false,
	    						   scrollwheel: false,
	    						   mapTypeId: google.maps.MapTypeId.SATELLITE
	    					   },
	    					   refresh: true
	       					};
	           }.bind(this));
		}
	}.bind(this)
	);
	
	
	
	$http.post('/SSaveActividad', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.actividades = response.data.actividades;
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
}]);

