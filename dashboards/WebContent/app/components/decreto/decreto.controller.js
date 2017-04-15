/**
 * 
 */

angular.module('decretoController',['dashboards','ui.bootstrap.contextMenu']).controller('decretoController',['$scope','$routeParams','$http','$interval', 
       '$timeout',
	   function($scope,$routeParams,$http, $interval, $timeout){
			var me = this;
			
			$http.post('/SDecreto', { ejercicio: 2017, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	
					}
				    
			 	}, function errorCallback(response){
			 		
			 	}
			);
			
			
		}
	]);