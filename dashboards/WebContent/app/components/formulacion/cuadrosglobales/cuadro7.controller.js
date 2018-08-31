
angular.module('cuadro7Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro7Controller',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=[true,true,true,true,true,true,true,true,true,true,true];
	
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
	
	
		
		$http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoGrupoGasto', ejercicio: me.anio, t: (new Date()).getTime(), tipo_gasto: 10   }).then(function(response){
		    if(response.data.success){
		    	me.entidades_tp_grupos_gasto_funcionamiento=response.data.entidades;
		    	for(var i=0; i<10; i++)
		    		me.grupos_gasto.push(me.entidades_tp_grupos_gasto_funcionamiento[0]['g'+i+'_nombre']);
		    	for(var i=0; i<=10; i++){
		    		me.total_tp_gg_funcionamiento.push(0.0);
		    		me.total_tp_gg.push(0.0);
		    	}
		    	for(var i=0; i<me.entidades_tp_grupos_gasto_funcionamiento.length; i++){
		    		me.total_tp_gg[0] += me.entidades_tp_grupos_gasto_funcionamiento[i].recomendado_total;
		    		me.total_tp_gg_funcionamiento[0] += me.entidades_tp_grupos_gasto_funcionamiento[i].recomendado_total;
		    		for(var j=0; j<10; j++){
		    			me.total_tp_gg_funcionamiento[j+1]+=me.entidades_tp_grupos_gasto_funcionamiento[i]['g'+j+'_monto'];
		    			me.total_tp_gg[j+1]+=me.entidades_tp_grupos_gasto_funcionamiento[i]['g'+j+'_monto'];
		    		}
		    	}
		    }
		    $http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoGrupoGasto', ejercicio: me.anio, t: (new Date()).getTime(), tipo_gasto: 20   }).then(function(response){
			    if(response.data.success){
			    	me.entidades_tp_grupos_gasto_inversion=response.data.entidades;
			    	for(var i=0; i<=10; i++)
			    		me.total_tp_gg_inversion.push(0.0);
			    	for(var i=0; i<me.entidades_tp_grupos_gasto_inversion.length; i++){
			    		me.total_tp_gg[0] += me.entidades_tp_grupos_gasto_inversion[i].recomendado_total;
			    		me.total_tp_gg_inversion[0] += me.entidades_tp_grupos_gasto_inversion[i].recomendado_total;
			    		for(var j=0; j<10; j++){
			    			me.total_tp_gg_inversion[j+1]+=me.entidades_tp_grupos_gasto_inversion[i]['g'+j+'_monto'];
			    			me.total_tp_gg[j+1]+=me.entidades_tp_grupos_gasto_inversion[i]['g'+j+'_monto'];
			    		}
			    	}
			    }
			    $http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoGrupoGasto', ejercicio: me.anio, t: (new Date()).getTime(), tipo_gasto: 30   }).then(function(response){
				    if(response.data.success){
				    	me.entidades_tp_grupos_gasto_deuda=response.data.entidades;
				    	for(var i=0; i<=10; i++)
				    		me.total_tp_gg_deuda.push(0.0);
				    	for(var i=0; i<me.entidades_tp_grupos_gasto_deuda.length; i++){
				    		me.total_tp_gg[0] += me.entidades_tp_grupos_gasto_deuda[i].recomendado_total;
				    		me.total_tp_gg_deuda[0] += me.entidades_tp_grupos_gasto_deuda[i].recomendado_total;
				    		for(var j=0; j<10; j++){
				    			me.total_tp_gg_deuda[j+1]+=me.entidades_tp_grupos_gasto_deuda[i]['g'+j+'_monto'];
				    			me.total_tp_gg[j+1]+=me.entidades_tp_grupos_gasto_deuda[i]['g'+j+'_monto'];
				    		}
				    	}
				    }
				    me.showloading=false;
				});
			});
		});
		
		
}
]);