

angular.module('calamidadDonacionesController',['dashboards','smart-table']);
angular.module('calamidadDonacionesController').controller('DonacionesCtrl', function($scope,$http,$routeParams,$rootScope){
	
	this.donaciones=[];
	this.original_donaciones=[];
	this.showloading=true;
	this.titulo = $rootScope.titulo;
	this.tipo = $rootScope.tipo;
	this.total_donaciones = 0;
	
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

	
	$http.post('/STransparenciaDonaciones', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.original_donaciones = response.data.donaciones;
	    	this.donaciones = this.original_donaciones.length> 0 ? this.original_donaciones.slice(0) : [];
	    	for(var i=0; i<this.donaciones.length; i++){
	    		this.donaciones[i].fecha_ingreso = Date.parse(this.donaciones[i].fecha_ingreso);
	    		this.total_donaciones += this.donaciones[i].monto_q;
	    	}
	    	this.showloading=false;
	    }
 	}.bind(this), function errorCallback(response){
    	this.showloading=false;
 	}
	);
	
});

