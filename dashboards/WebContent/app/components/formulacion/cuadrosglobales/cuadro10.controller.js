
angular.module('cuadro10Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro10Controller',['$scope','$routeParams','$http','$interval',
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
			return (value>0) ? $filter('currency')(millones.toFixed(1), '', 1) : null;
		}
		else 
			return value;
	}
	
	
		
	$http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoRegion', ejercicio: me.anio, t: (new Date()).getTime(), tipo_gasto: 10   }).then(function(response){
	    if(response.data.success){
	    	me.entidades_tp_r_funcionamiento=response.data.entidades;
	    	for(var i=1; i<12; i++)
	    		me.regiones.push(me.entidades_tp_r_funcionamiento[0]['r'+i+'_nombre']);
	    	for(var i=0; i<12; i++){
	    		me.total_tp_r_funcionamiento.push(0.0);
	    		me.total_tp_r.push(0.0);
	    	}
	    	for(var i=0; i<me.entidades_tp_r_funcionamiento.length; i++){
	    		me.total_tp_r[0] += me.entidades_tp_r_funcionamiento[i].recomendado_total;
	    		me.total_tp_r_funcionamiento[0] += me.entidades_tp_r_funcionamiento[i].recomendado_total;
	    		for(var j=1; j<12; j++){
	    			me.total_tp_r_funcionamiento[j]+=me.entidades_tp_r_funcionamiento[i]['r'+j+'_monto'];
	    			me.total_tp_r[j]+=me.entidades_tp_r_funcionamiento[i]['r'+j+'_monto'];
	    		}
	    	}
	    }
	    $http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoRegion', ejercicio: me.anio, t: (new Date()).getTime(), tipo_gasto: 20   }).then(function(response){
		    if(response.data.success){
		    	me.entidades_tp_r_inversion=response.data.entidades;
		    	for(var i=0; i<12; i++)
		    		me.total_tp_r_inversion.push(0.0);
		    	for(var i=0; i<me.entidades_tp_r_inversion.length; i++){
		    		me.total_tp_r[0] += me.entidades_tp_r_inversion[i].recomendado_total;
		    		me.total_tp_r_inversion[0] += me.entidades_tp_r_inversion[i].recomendado_total;
		    		for(var j=1; j<12; j++){
		    			me.total_tp_r_inversion[j]+=me.entidades_tp_r_inversion[i]['r'+j+'_monto'];
		    			me.total_tp_r[j]+=me.entidades_tp_r_inversion[i]['r'+j+'_monto'];
		    		}
		    	}
		    }
		    $http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoRegion', ejercicio: me.anio, t: (new Date()).getTime(), tipo_gasto: 30   }).then(function(response){
			    if(response.data.success){
			    	me.entidades_tp_r_deuda=response.data.entidades;
			    	for(var i=0; i<12; i++)
			    		me.total_tp_r_deuda.push(0.0);
			    	for(var i=0; i<me.entidades_tp_r_deuda.length; i++){
			    		me.total_tp_r[0] += me.entidades_tp_r_deuda[i].recomendado_total;
			    		me.total_tp_r_deuda[0] += me.entidades_tp_r_deuda[i].recomendado_total;
			    		for(var j=1; j<12; j++){
			    			me.total_tp_r_deuda[j]+=me.entidades_tp_r_deuda[i]['r'+j+'_monto'];
			    			me.total_tp_r[j]+=me.entidades_tp_r_deuda[i]['r'+j+'_monto'];
			    		}
			    	}
			    }
			    me.showloading=false;
			});
		});
	});
}
]);