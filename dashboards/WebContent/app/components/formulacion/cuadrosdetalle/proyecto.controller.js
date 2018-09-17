
angular.module('proyectodetalleController',['dashboards','ui.bootstrap.contextMenu']).controller('proyectodetalleController',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = false;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.ejecutado_dos_antes=0.0;
	me.aprobado_anterior_mas_amp=0.0;
	me.recomendado=0.0;
	
	me.goTo=function(path){
		$location.path( path );
	}
	
	me.filtroMillones=function(value, transform){
		var millones = value;
		var fixed=2;
		if(transform){
			millones = value/1000000;
			fixed=1;
		}
		return (value>0) ?  $filter('currency')(millones.toFixed(fixed), '', fixed) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(fixed), '', fixed).substring(1) + ')' : null)  ;
	}
	
	$http.post('/SInstitucional',  { action: 'getTotales', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){ 
		if(response.data.success){
			me.ejecutado_dos_antes=response.data.ejecutado_dos_antes;
			me.aprobado_anterior_mas_amp=response.data.aprobado_anterior_mas_amp;
			me.recomendado=response.data.recomendado;
		}
		me.showloading=false;
	});
}
]);