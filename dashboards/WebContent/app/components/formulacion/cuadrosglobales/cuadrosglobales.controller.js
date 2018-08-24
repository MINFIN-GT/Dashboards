
angular.module('cuadrosglobalesController',['dashboards','ui.bootstrap.contextMenu']).controller('cuadrosglobalesController',['$scope','$routeParams','$http','$interval',
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
			return (value>0) ? $filter('currency')(millones.toFixed(1), '', 1) : null;
		}
		else 
			return value;
	}
	
	
	$http.post('/SInstitucional',  { action: 'getRecursosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
	   if(response.data.success){
		   var stack_recurso=[];
		   var stack_suma=[];
	    	me.recursos=response.data.recursos;
	    	for(var i=0; i<me.recursos.length-1; i++){
	    		if(me.recursos[i+1].nivel>me.recursos[i].nivel){
	    			stack_recurso.push(i);
	    			stack_suma.push(0.0);
	    		}
	    		else if(me.recursos[i+1].nivel==me.recursos[i].nivel){
	    			stack_suma[stack_suma.length-1] += me.recursos[i].recomendado;
	    		}
	    		else if(me.recursos[i+1].nivel<me.recursos[i].nivel){
	    			var stack_anterior = me.recursos[i].recomendado;
	    			for(var k=0;  k<(me.recursos[i].nivel-me.recursos[i+1].nivel);k++){
		    			if(k==0){
		    				stack_suma[stack_suma.length-1] +=stack_anterior;
		    			}
			    		stack_anterior = stack_suma[stack_suma.length-1];
		    			me.recursos[stack_recurso[stack_recurso.length-1]].recomendado = stack_suma[stack_suma.length-1];
		    			if(stack_suma.length>1){
		    				stack_suma[stack_suma.length-2] += stack_anterior;
		    			}
		    			stack_recurso.pop();
			    		stack_suma.pop();
		    		}
	    		}
	    	}
	    	stack_suma[stack_suma.length-1] += me.recursos[i].recomendado;
    		while(stack_recurso.length>0){
	    		stack_suma[stack_suma.length-2] += stack_suma[stack_suma.length-1];
	    		me.recursos[stack_recurso[stack_recurso.length-1]].recomendado = stack_suma[stack_suma.length-1];
	    		stack_recurso.pop();
    			stack_suma.pop();
	    	}
	    }
	    me.showloading[2]=false;
	});
	
		$http.post('/SInstitucional',  { action: 'getInstitucionalTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		    if(response.data.success){
		    	me.entidades=response.data.entidades;
		    	for(var i=0; i<me.entidades.length; i++){
		    		me.total_aprobado_anterior_mas_ampliaciones += me.entidades[i].aprobado_anterior_mas_ampliaciones;
		    		me.total_ejecutado_dos_antes += me.entidades[i].ejecutado_dos_antes;
		    		me.total_recomendado += me.entidades[i].recomendado;
		    	}
		    }
		    me.showloading[4]=false;
		});
		
		$http.post('/SInstitucional',  { action: 'getInstitucionalTipoGasto', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		    if(response.data.success){
		    	me.entidades_tipo_gasto=response.data.entidades;
		    	for(var i=0; i<me.tipos_gasto_codigos.length; i++)
		    		me.tipos_gasto.push(me.entidades_tipo_gasto[0]['tp'+me.tipos_gasto_codigos[i]+'_nombre']);
		    	for(var i=0; i<=me.tipos_gasto_codigos.length; i++)
		    		me.total_tp.push(0.0);
		    	for(var i=0; i<me.entidades_tipo_gasto.length; i++){
		    		me.total_tp[0] += me.entidades_tipo_gasto[i].recomendado;
		    		for(var j=0; j<me.tipos_gasto_codigos.length; j++){
		    			me.total_tp[j+1]+=me.entidades_tipo_gasto[i]['tp'+me.tipos_gasto_codigos[j]+'_monto'];
		    		}
		    	}
		    }
		    me.showloading[5]=false;
		});
		
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
				    me.showloading[6]=false;
				});
			});
		});
		
		$http.post('/SInstitucional',  { action: 'getInstitucionalFinalidad', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		    if(response.data.success){
		    	me.entidades_finalidades=response.data.entidades;
		    	for(var i=0; i<me.finalidades_codigos.length; i++)
		    		me.finalidades.push(me.entidades_finalidades[0]['f'+me.finalidades_codigos[i]+'_nombre']);
		    	for(var i=0; i<=me.finalidades_codigos.length; i++)
		    		me.total_finalidad.push(0.0);
		    	for(var i=0; i<me.entidades_finalidades.length; i++){
		    		me.total_finalidad[0] += me.entidades_finalidades[i].recomendado_total;
		    		for(var j=0; j<me.finalidades_codigos.length; j++){
		    			me.total_finalidad[j+1]+=me.entidades_finalidades[i]['f'+me.finalidades_codigos[j]+'_monto'];
		    		}
		    	}
		    }
		    me.showloading[7]=false;
		});
		
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
				    me.showloading[8]=false;
				});
			});
		});
		
		$http.post('/SInstitucional',  { action: 'getFinalidadRegion', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		    if(response.data.success){
		    	me.finalidades_region=response.data.finalidades;
		    	//for(var i=1; i<12; i++)
		    	//	me.regiones.push(me.finalidades_region[0]['r'+i+'_nombre']);
		    	for(var i=0; i<12; i++)
		    		me.total_finalidad_region.push(0.0);
		    	for(var i=0; i<me.finalidades_region.length; i++){
		    		me.total_finalidad_region[0] += me.finalidades_region[i].recomendado_total;
		    		for(var j=0; j<11; j++){
		    			me.total_finalidad_region[j+1]+=me.finalidades_region[i]['r'+(j+1)+'_monto'];
		    		}
		    	}
		    }
		    me.showloading[9]=false;
		});
		
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
		    me.showloading[10]=false;
		});
}
]);