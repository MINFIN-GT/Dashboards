/**
 * 
 */


angular.module('estadoscalamidadController',['dashboards']).controller('estadoscalamidadController',['$scope','$routeParams','$http','$location',
	   function($scope,$routeParams,$http,$location){
	
	this.estadoscalamidad = [];
	
	
	$http.post('/STransparenciaEstadosCalamidad', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.estadoscalamidad = response.data.estados_calamidad;
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
	this.redirect=function(subprograma){
		 $location.path("/transparencia/calamidad/"+ subprograma );
	};
	
}]);

