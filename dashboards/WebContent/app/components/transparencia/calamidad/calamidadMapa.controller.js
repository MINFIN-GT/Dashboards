/**
 * 
 */


angular.module('calamidadMapaController',['dashboards','ngMap']).controller('calamidadMapaController',['$scope','$routeParams','$http','NgMap','$uibModal',
	   function($scope,$routeParams,$http,NgMap,$uibModal){
	
	this.lastupdate='';
	this.actividades='';
	this.titulo ='';
	this.tipo = '';
	this.longitude = null;
	this.latitude = null;
	this.map_options={
			center: null,
			zoom: 7,
			options: {
				   streetViewControl: false,
				   scrollwheel: false,
				   mapTypeId: google.maps.MapTypeId.ROADMAP
			   },
			styles: [
				   {
					    "elementType": "labels",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.land_parcel",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.locality",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.neighborhood",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.province",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "road.arterial",
					    "elementType": "labels",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "road.highway",
					    "elementType": "labels",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "road.local",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  }
					]
	};
	
	this.show_marks = false;
	this.geograficos_94 = [];
	this.geograficos_otros = [];
	this.geograficos_94_list = "";
	this.geograficos_otros_list = "";
	this.geograficos_loaded = false;
	this.geograficos_list_all = "";
	
	this.geograficos_loaded=false;
	
	this.subprograma = $routeParams.subprograma;
	
	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.lastupdate = response.data.lastupdate;
			}
		}.bind(this)
	);
	
	$http.post('/STransparenciaEstadosCalamidad', { action: 'getEstado',subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
		this.geograficos_loaded = false;
		if(response.data.success){
	    	this.titulo = response.data.results.nombre;
	    	this.tipo = response.data.results.tipo;
	    	this.latitude = response.data.results.latitude;
	    	this.longitude = response.data.results.longitude;
	    	
	    	this.map_options.center= { lat: parseFloat(this.latitude), lng: parseFloat(this.longitude) }; 
			
	    	NgMap.getMap().then(function(map){
	    		map.setCenter(this.map_options.center);
	    		this.map_options.map = map;
	    	}.bind(this));
	    	
	    	$http.post('/SEjecucionFGeograficos', { programa: 94, subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.geograficos_94 = response.data.datos;
			    	var stemp = "";
			    	for(var i=0; i<this.geograficos_94.length; i++)
			    		stemp += ',' + this.geograficos_94[i].codigo;
			    	this.geograficos_94_list = stemp.length>1 ? stemp.substring(1) : "";
			    	$http.post('/SEjecucionFGeograficos', { programa: 94, subprograma: $routeParams.subprograma, otros: true, t: (new Date()).getTime() }).then(function(response){
					    if(response.data.success){
					    	this.geograficos_otros = response.data.datos;
					    	var stemp = "";
					    	for(var i=0; i<this.geograficos_otros.length; i++)
					    		stemp += ',' + this.geograficos_otros[i].codigo;
					    	this.geograficos_otros_list = stemp.length>1 ? stemp.substring(1) : "";
					    	this.geograficos_loaded = true;
					    	this.geograficos_list_all = this.geograficos_94_list + ( this.geograficos_94_list.length >0 ? ', ' : '') + this.geograficos_otros_list ; 
					    	setTimeout(function(){ 
					    		google.maps.event.addListener(this.map_options.map.fusionTablesLayers[0], 'click', function(e){
					    		     this.openGeograficoDetalle(e);
					    		 }.bind(this));
					    	}.bind(this), 1000);
					    }
					}.bind(this)
			    	);
				}
			}.bind(this)
	    	);
	    	
		}
	}.bind(this)
	);
	
	
	
	$http.post('/SSaveActividad', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.actividades = response.data.actividades;
	    	this.actividades_original = this.actividades.slice(0);
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
	this.showHideMarks=function(){
		if(this.show_marks){
			this.actividades = this.actividades_original.slice(0);
		}
		else{
			this.actividades = [];
		}
	}
	
	this.openGeograficosDetalle=function(){
			    var modalInstance = $uibModal.open({
			      animation: false,
			      ariaLabelledBy: 'modal-title',
			      ariaDescribedBy: 'modal-body',
			      templateUrl: 'geograficosDetalles.html',
			      controller: 'geograficosDetallesCtrl',
			      controllerAs: '$ctrl',
			      size: 'lg',
			      resolve: {
			        geograficos_94: function () {
			          return this.geograficos_94;
			        }.bind(this),
			        geograficos_otros: function () {
				          return this.geograficos_otros;
				        }.bind(this)
			      }
			    });

			    modalInstance.result.then(function () {
			      
			    }, function () {
			      
			    });
	}
	
	this.openGeograficoDetalle=function(geografico){
		console.log(geografico);
		var municipio = geografico.row.Codigo.value;
		this.datos_geografico_detalle = {};
		this.datos_geografico_detalle.municipio = geografico.row.Municipio.value;
		this.datos_geografico_detalle.departamento = geografico.row.Departamen.value;
		this.datos_geografico_detalle.ejecutado_94 = 0;
		this.datos_geografico_detalle.vigente_94 = 0;
		this.datos_geografico_detalle.comprometido_94 = 0;
		this.datos_geografico_detalle.ejecutado_otros = 0;
		this.datos_geografico_detalle.comprometido_otros = 0;
		for(var i=0; i<this.geograficos_94.length; i++){
			if(this.geograficos_94[i].codigo==municipio){
				this.datos_geografico_detalle.ejecutado_94 = this.geograficos_94[i].ejecutado;
				this.datos_geografico_detalle.vigente_94 = this.geograficos_94[i].vigente;
				this.datos_geografico_detalle.comprometido_94 = this.geograficos_94[i].compromiso;
				i=this.geograficos_94.length+1;
			}
		}
		for(var i=0; i<this.geograficos_otros.length; i++){
			if(this.geograficos_otros[i].codigo==municipio){
				this.datos_geografico_detalle.ejecutado_otros = this.geograficos_otros[i].ejecutado;
				this.datos_geografico_detalle.comprometido_otros = this.geograficos_otros[i].compromiso;
				i=this.geograficos_otros.length+1;
			}
		}
		var modalInstance = $uibModal.open({
	      animation: false,
	      ariaLabelledBy: 'modal-title',
	      ariaDescribedBy: 'modal-body',
	      templateUrl: 'geograficoDetalles.html',
	      controller: 'geograficoDetallesCtrl',
	      controllerAs: '$ctrl',
	      size: 'md',
	      resolve: {
	        datos: function () {
	          return this.datos_geografico_detalle;
	        }.bind(this)
	      }
	    });

	    modalInstance.result.then(function () {
	      
	    }, function () {
	      
	    });
	}
}]);

angular.module('calamidadMapaController').controller('geograficosDetallesCtrl', function ($uibModalInstance,geograficos_94, geograficos_otros) {
	  var $ctrl = this;
	  $ctrl.geograficos_94 = geograficos_94;
	  $ctrl.total_94_vigente = 0.0;
	  $ctrl.total_94_compromiso = 0.0;
	  $ctrl.total_94_ejecutado = 0.0;
	  $ctrl.geograficos_otros = geograficos_otros;
	  $ctrl.total_otros_compromiso = 0.0;
	  $ctrl.total_otros_ejecutado = 0.0;
	  
	  for(var i=0; i<$ctrl.geograficos_94.length; i++){
		  $ctrl.total_94_vigente += $ctrl.geograficos_94[i].vigente;
		  $ctrl.total_94_compromiso += $ctrl.geograficos_94[i].compromiso;
		  $ctrl.total_94_ejecutado += $ctrl.geograficos_94[i].ejecutado;
	  }
	  
	  for(var i=0; i<$ctrl.geograficos_otros.length; i++){
		  $ctrl.total_otros_compromiso += $ctrl.geograficos_otros[i].compromiso;
		  $ctrl.total_otros_ejecutado += $ctrl.geograficos_otros[i].ejecutado;
	  }
	  
	  $ctrl.ok = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
	});

angular.module('calamidadMapaController').controller('geograficoDetallesCtrl', function ($uibModalInstance,datos) {
	  var $ctrl = this;
	  $ctrl.datos=datos;
	  
	  $ctrl.ok = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
});


