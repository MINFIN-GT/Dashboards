

angular.module('calamidadComprasController',['dashboards','smart-table']);
angular.module('calamidadComprasController').controller('ComprasCtrl', function($scope,$http,$routeParams,$rootScope){
	
	this.compras=[];
	this.original_compras=[];
	this.showloading=true;
	this.titulo = $rootScope.titulo;
	this.tipo = $rootScope.tipo;
	
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

	
	$http.post('/STransparenciaCompras', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.original_compras = response.data.compras;
	    	this.compras = this.original_compras.length> 0 ? this.original_compras.slice(0) : [];
	    	for(var i=0; i<this.compras.length; i++)
	    		this.compras[i].fecha = Date.parse(this.compras[i].fecha);
	    	this.showloading=false;
	    }
 	}.bind(this), function errorCallback(response){
    	this.showloading=false;
 	}
	);
	
});

