
angular.module('cuadro6detalleController',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro6Controller',['$scope','$routeParams','$http','$interval',
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
	
	
	
	me.filtroMillones=function(value, transform){
		if(transform){
			var millones = value/1000000;
			return (value>0) ?  $filter('currency')(millones.toFixed(1), '', 1) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(1), '', 1).substring(1) + ')' : null)  ;
		}
		else 
			return value;
	}
	
	
		$http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoDetalle', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
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
		    	for(var i=0; i<me.entidades_tipo_gasto.length;i++){
		    		me.entidades_tipo_gasto[i].showChildren=false;
		    		me.entidades_tipo_gasto[i].show=true;
		    		me.entidades_tipo_gasto[i].parent=null; 
		    	}
		    }
		    me.showloading=false;
		});
		
		me.clickRow=function(row,index){
			if(row.showChildren==false && row.nivel<6){ //Mostrar hijos
				row.showChildren=true;
				if((index+1 < me.entidades_tipo_gasto.length) && me.entidades_tipo_gasto[index+1].nivel>row.nivel){
		    		while(index<me.entidades_tipo_gasto.length && me.entidades_tipo_gasto[index+1].nivel>row.nivel){
		    			if(me.entidades_tipo_gasto[index+1].nivel==row.nivel+1)
		    				me.entidades_tipo_gasto[index+1].show=true;
		    			index++;
		    		}
		    	}
				else{
					row.showloading=true;
					$http.post('/SInstitucional',  { action: 'getInstitucionalTipoGastoDetalle', ejercicio: me.anio, 
						entidad: (row.nivel==1) ? row.codigo : row.entidad,
						unidad_ejecutora: (row.nivel==2) ? row.codigo : row.unidad_ejecutora,
						programa: (row.nivel==3) ? row.codigo : row.programa,
						grupo: (row.nivel==4) ? row.codigo : row.grupo,
						subgrupo: (row.nivel==5) ? row.codigo : row.subgrupo,
						t: (new Date()).getTime()   }).then(function(response){
					    if(response.data.success){
					    	for(var i=0;i<response.data.entidades.length; i++){
					    		response.data.entidades[i].showChildren=false;
					    		response.data.entidades[i].show=true;
					    		if(row.nivel==1){
					    			response.data.entidades[i].entidad = row.codigo;
					    			response.data.entidades[i].parent = 'E-'+row.codigo;
					    		}
					    		if(row.nivel==2){
					    			response.data.entidades[i].entidad = row.entidad;
					    			response.data.entidades[i].unidad_ejecutora = row.codigo;
					    			response.data.entidades[i].parent = 'UE-'+row.codigo;
					    		}
					    		if(row.nivel==3){
					    			response.data.entidades[i].programa = row.codigo;
					    			response.data.entidades[i].entidad = row.entidad;
					    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
					    			response.data.entidades[i].parent = 'P-'+row.codigo;
					    		}
					    		if(row.nivel==4){
					    			response.data.entidades[i].grupo = row.codigo;
					    			response.data.entidades[i].programa = row.programa;
					    			response.data.entidades[i].entidad = row.entidad;
					    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
					    			response.data.entidades[i].parent = 'GG-'+row.codigo;
					    		}
					    		if(row.nivel==5){
					    			response.data.entidades[i].subgrupo = row.codigo;
					    			response.data.entidades[i].grupo = row.grupo;
					    			response.data.entidades[i].programa = row.programa;
					    			response.data.entidades[i].entidad = row.entidad;
					    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
					    			response.data.entidades[i].parent = 'SG-'+row.codigo;
					    		}
					    	}
					    	me.entidades_tipo_gasto.splice.apply(me.entidades_tipo_gasto,[index+1,0].concat(response.data.entidades));
					    }
					    row.showloading=false;
					});
				}
			}
			else{ //Borrar hijos
				row.showChildren=false;
				var pos=index+1;
				while(pos<me.entidades_tipo_gasto.length && me.entidades_tipo_gasto[pos].nivel>row.nivel){
					me.entidades_tipo_gasto[pos].show=false;
					me.entidades_tipo_gasto[pos].showChildren=false;
					pos++;
				}
			}
		}
}
]);