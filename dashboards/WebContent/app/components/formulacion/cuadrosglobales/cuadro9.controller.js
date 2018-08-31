
angular.module('cuadro9Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro9Controller',['$scope','$routeParams','$http','$interval',
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
	
	me.entidades_tipo_gasto = [];
	me.tipos_gasto=[];
	me.tipos_gasto_codigos=[11,12,13,21,22,23,31];
	me.total_tp = [];
	
	me.entidades_tp_grupos_gasto_funcionamiento=[];
	me.entidades_tp_grupos_gasto_inversion=[];
	me.entidades_tp_grupos_gasto_deuda=[];
	me.total_tp_gg_funcionamiento=[]
	me.total_tp_gg_inversion=[];
	me.total_tp_gg_deuda=[];
	me.total_tp_gg=[];
	me.grupos_gasto=[];
	
	me.entidades_finalidades = [];
	me.finalidades=[];
	me.finalidades_codigos=['01','02','03','04','05','06','07','08','09','10','11','12'];
	me.total_finalidad = [];
	
	
	me.entidades_tp_r_funcionamiento=[];
	me.entidades_tp_r_inversion=[];
	me.entidades_tp_r_deuda=[];
	me.total_tp_r_funcionamiento=[]
	me.total_tp_r_inversion=[];
	me.total_tp_r_deuda=[];
	me.total_tp_r=[];
	me.regiones=[];
	
	me.finalidades_region = [];
	me.total_finalidad_region = [];
	
	
	me.finalidades_economico = [];
	me.economicos=[];
	me.total_finalidad_economico = [];
	
	me.filtroMillones=function(value, transform){
		if(transform){
			var millones = value/1000000;
			return (value>0) ?  $filter('currency')(millones.toFixed(1), '', 1) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(1), '', 1).substring(1) + ')' : null)  ;
		}
		else 
			return value;
	}
	
	
		
	$http.post('/SInstitucional',  { action: 'getFinalidadEconomico', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
	    if(response.data.success){
	    	me.finalidades_economico=response.data.finalidades;
	    	for(var i=1; i<10; i++)
	    		me.economicos.push(me.finalidades_economico[0]['e'+i+'_nombre']);
	    	for(var i=0; i<10; i++)
	    		me.total_finalidad_economico.push(0.0);
	    	for(var i=0; i<me.finalidades_economico.length; i++){
	    		me.total_finalidad_economico[0] += me.finalidades_economico[i].recomendado_total;
	    		for(var j=1; j<10; j++){
	    			me.total_finalidad_economico[j]+=me.finalidades_economico[i]['e'+(j)+'_monto'];
	    		}
	    	}
	    }
	    me.showloading=false;
	});
		
		
}
]);