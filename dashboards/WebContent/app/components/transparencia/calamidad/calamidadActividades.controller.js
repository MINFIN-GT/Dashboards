/**
 * 
 */


angular.module('calamidadActividadesController',['dashboards','angular-timeline','angular-scroll-animate','ngSanitize']).controller('calamidadActividadesController',['$scope','$routeParams','$http','uiGridConstants','$document','$timeout','$sce','$uibModal','uiGmapGoogleMapApi', '$rootScope',
	   function($scope,$routeParams,$http,uiGridConstants, $document, $timeout,$sce,$uibModal,uiGmapGoogleMapApi, $rootScope){
			
			this.lastupdate = '';
			this.actividad_seleccionada=-1;
			this.titulo = $rootScope.titulo;
			this.tipo = $rootScope.tipo;
			
			$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	this.lastupdate = response.data.lastupdate;
					}
			}.bind(this)
			);
			
			if($rootScope.titulo==null || $rootScope.titulo === undefined){
				$http.post('/STransparenciaEstadosCalamidad', { action: 'getEstado',subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	this.titulo = response.data.results.nombre;
				    	this.tipo = response.data.results.tipo;
				    	$rootScope.titulo = this.titulo;
				    	$rootScope.tipo = this.tipo;
					}
				}.bind(this)
				);
			}
			
			this.trustAsHtml = function(string) {
		        return $sce.trustAsHtml(string);
		    };
		    
		    
		
			this.side='';
			this.actividad = '';
			this.activiades = [];
			
			$http.post('/SSaveActividad', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.actividades = response.data.actividades;
			    }
		 	}.bind(this), function errorCallback(response){
		 		
		 	}
			);
			
			// optional: not mandatory (uses angular-scroll-animate)
			this.animateElementIn = function($el) {
				$el.removeClass('timeline-hidden');
				$el.addClass('bounce-in');
			};

			// optional: not mandatory (uses angular-scroll-animate)
			this.animateElementOut = function($el) {
				$el.addClass('timeline-hidden');
				$el.removeClass('bounce-in');
			};
			
			this.selectActividad=function(index){
				this.side = this.side='left';
				this.actividad = this.actividades[index];
				this.actividad_seleccionada = index;
			}
			
			this.open = function (posicionlat, posicionlong) {
				$scope.geoposicionlat = posicionlat;
				$scope.geoposicionlong = posicionlong;
				
			    var modalInstance = $uibModal.open({
			      animation: true,
			      templateUrl: 'map.html',
			      controller: 'mapCtrl',
			      resolve: {
			        glat: function(){
			        	return $scope.geoposicionlat;
			        },
			        glong: function(){
			        	return $scope.geoposicionlong;
			        }
			      }
			    
			    });
			  };
			  
			  this.cerrarActividad=function(){
				  this.side='';
				  this.actividad_seleccionada=-1;
			  }
			
		}
	]);

angular.module('calamidadActividadesController').controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
                                                         function ($scope, $uibModalInstance,$timeout, uiGmapGoogleMapApi, glat, glong) {
                                                     	$scope.geoposicionlat = glat;
                                                     	$scope.geoposicionlong = glong;
                                                     	
                                                     	$scope.refreshMap = true;
                                                     	
                                                     	uiGmapGoogleMapApi.then(function() {
                                                     		$scope.map = { center: { latitude: $scope.geoposicionlat, longitude: $scope.geoposicionlong }, 
                                                     					   zoom: 15,
                                                     					   height: 400,
                                                     					   options: {
                                                     						   streetViewControl: false,
                                                     						   scrollwheel: true,
                                                     						  mapTypeId: google.maps.MapTypeId.SATELLITE
                                                     					   },
                                                     					   refresh: true
                                                     					};
                                                         });
                                                     	
                                                     	
                                                     	
                                                     	  $scope.ok = function () {
                                                     	    $uibModalInstance.dismiss('cancel');
                                                     	    
                                                     	  };

                                                     	}]);