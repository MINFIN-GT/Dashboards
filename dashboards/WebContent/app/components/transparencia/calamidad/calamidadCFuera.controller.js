angular.module('calamidadCFueraController',['dashboards','smart-table']);
angular.module('calamidadCFueraController').controller('CFueraCtrl', function($scope,$http,$routeParams,$rootScope,$filter){
	
	this.compras=[];
	this.original_compras=[];
	this.showloading=true;
	this.titulo = $rootScope.titulo;
	this.tipo = $rootScope.tipo;
	
	this.total_eventos=0;
	this.total_adjudicados=0;
	this.total_monto_adjudicado=0.0;
	
	this.chart_colours = [ '#A9D18E', '#5B9BD5', '#F7464A', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'];
	this.chartType = "horizontalBar";
	this.chartTitle = "Eventos Guatecompras";
	this.chartLabels = [];
	this.chartSeries = ['Adjudicados', 'En proceso'];
	this.chartData = [];
	
	this.selected_entidad="";
	this.tab_active = 0;
	
	this.subprograma = $routeParams.subprograma;
	
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
	
	this.getCompras=function(){
		$http.post('/STransparenciaComprasFueraEstado', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
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
	};
	
	$http.post('/STransparenciaComprasFueraEstado', { action: 'getlist_entidades', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.compras_por_entidad = response.data.compras;
	    	this.total_eventos=0;
	    	this.total_adjudicados=0;
	    	this.total_monto_adjudicado=0.0;
	    	this.chartLabels = [];
	    	this.chartData = [[],[]];
	    	for(var i=0; i<this.compras_por_entidad.length; i++){
	    		this.total_eventos += this.compras_por_entidad[i].num_eventos;
	    		this.total_adjudicados += this.compras_por_entidad[i].num_adjudicados;
	    		this.total_monto_adjudicado += this.compras_por_entidad[i].total_adjudicado;
	    		this.chartLabels.push(this.compras_por_entidad[i].entidad);
	    		this.chartData[0].push(this.compras_por_entidad[i].num_adjudicados);
	    		this.chartData[1].push(this.compras_por_entidad[i].num_eventos - this.compras_por_entidad[i].num_adjudicados);
	    	}
	    	this.getCompras();
	    }
 	}.bind(this), function errorCallback(response){
    }
	);
	
	this.getComprasList=function(entidad){
		this.selected_entidad=entidad;
		this.tab_active = 1;
		
	};

	this.chartOptions= {
			tooltips:{
				enabled:true
			},
			responsive: true,
			maintainAspectRatio: false,
			scales: {
					xAxes: [{
						stacked: true,
						gridLines: {
							display:false
						},
						scaleLabel: {
							display: true,
							labelString: 'Eventos en Guatecompras'
						}
					}],
					yAxes: [{
						stacked: true,
						gridLines: {
							display:false
						},
						ticks: {
				            display: false
				        }
					}]
			},
            hover: { animationDuration: 0 }
	}
	
	this.resetFilters=function(){
		this.selected_entidad="";
	}
	
	
	
	
}).filter('myStrictFilter', function($filter){
    return function(input, predicate){
        return $filter('filter')(input, predicate, true);
    }
}).filter('unique', function() {
    return function (arr, field) {
        var o = {}, i, l = arr.length, r = [];
        for(i=0; i<l;i+=1) {
            o[arr[i][field]] = arr[i];
        }
        for(i in o) {
            r.push(o[i]);
        }
        return r;
    };
  }).directive("mhSearch", ['stConfig', '$timeout','$parse', function (stConfig, $timeout, $parse) {
	    return {
	        require: ['^stTable', 'ngModel'],
	        link: function(scope, element, attr, ctrls) {
	            var tableCtrl = ctrls[0],
	                modelCtrl = ctrls[1];
	            var promise = null;
	            var throttle = attr.stDelay || stConfig.search.delay;

	            function triggerSearch() {
	                var value = modelCtrl.$modelValue;

	                if (promise !== null) {
	                    $timeout.cancel(promise);
	                }

	                promise = $timeout(function () {
	                    tableCtrl.search(value, attr.mhSearch || '');
	                    promise = null;
	                }, throttle);
	            }

	            scope.$watch(function () { return modelCtrl.$modelValue; }, triggerSearch);
	        }
	    };
	}]);

