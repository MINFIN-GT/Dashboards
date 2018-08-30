
angular.module('cuadro5Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro5Controller',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.entidades = [];
	me.total_aprobado_anterior_mas_ampliaciones=0.0;
	me.total_ejecutado_dos_antes=0.0;
	me.total_recomendado=0.0;
	
	me.filtroMillones=function(value, transform){
		if(transform){
			var millones = value/1000000;
			return (value>0) ? $filter('currency')(millones.toFixed(1), '', 1) : null;
		}
		else 
			return value;
	}
	
	
	$http.post('/SInstitucional',  { action: 'getInstitucionalTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
	    if(response.data.success){
	    	me.entidades=response.data.entidades;
	    	for(var i=0; i<me.entidades.length; i++){
	    		me.total_aprobado_anterior_mas_ampliaciones += me.entidades[i].aprobado_anterior_mas_ampliaciones;
	    		me.total_ejecutado_dos_antes += me.entidades[i].ejecutado_dos_antes;
	    		me.total_recomendado += me.entidades[i].recomendado;
	    	}
	    }
	    me.showloading=false;
	});
}
]);