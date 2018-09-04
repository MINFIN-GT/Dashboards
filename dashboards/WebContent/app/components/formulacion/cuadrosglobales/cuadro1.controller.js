
angular.module('cuadro1Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro1Controller',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.pibs = [ 522796.1, 559411.3, 642367.1 ];
	
	me.filtroPorcentaje=function(value){
			var decimales=1;
			value = value*100;
			if(value>=0.1)
				decimales=1;
			else if(value<0.1 && value>=0.01)
				decimales=2;
			else if(value<0.01 && value>-0.01)
				decimales=3;
			else if(value<=-0.01 && value>-0.1)
				decimales=2;
			else
				decimales=1;
			var ret = (value>0) ?  $filter('currency')(value.toFixed(decimales), '', decimales) : ( value<0 ? '(' + $filter('currency')(value.toFixed(decimales), '', decimales).substring(1) + ')' : null);
			return (ret!=null) ? (ret.substring(ret.length-1,1)=='0' ? ret.substring(0,ret.length-1) : ret) : ret;
	}
	
	me.filtroCurrency=function(value){
		return $filter('currency')(value.toFixed(1), '', 1);
	}
	
	
	$http.post('/SInstitucional',  { action: 'getRecursosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		var columnas=['ejecutado_dos_antes','aprobado_anterior_mas_amp', 'recomendado'];  
		if(response.data.success){
			   me.recursos=response.data.recursos;
		       $http.post('/SInstitucional',  { action: 'getGastosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
				   if(response.data.success){
					   me.gastos=response.data.gastos;
					   $http.post('/SIndicadoresFiscal',  { action: 'getReporte', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
						   if(response.data.success){
							   me.lineas=response.data.lineas;
							   me.lineas_cuadro_2 = [];
							   for(var i=0; i<43; i++){
								    me.lineas_cuadro_2.push({
								    	ejecutado_dos_antes: 0,
								    	aprobado_anterior_mas_amp: 0,
								    	recomendado: 0
								    });
							   }	   
							   for(var i=0; i<columnas.length; i++){
								   me.lineas_cuadro_2[3][columnas[i]] = me.recursos[4][columnas[i]] + me.recursos[5][columnas[i]] +
								   		me.recursos[6][columnas[i]] + me.recursos[7][columnas[i]] + me.recursos[8][columnas[i]];
								   
								   me.lineas_cuadro_2[4][columnas[i]] = me.recursos[10][columnas[i]] + me.recursos[12][columnas[i]] +
							   		me.recursos[13][columnas[i]] + me.recursos[14][columnas[i]] + me.recursos[15][columnas[i]] +
							   		me.recursos[16][columnas[i]] + me.recursos[17][columnas[i]];
								   
								   me.lineas_cuadro_2[5][columnas[i]] =  me.recursos[23][columnas[i]];
								   
								   me.lineas_cuadro_2[6][columnas[i]] =  me.recursos[19][columnas[i]] + me.recursos[20][columnas[i]] +
								   me.recursos[21][columnas[i]] + me.recursos[22][columnas[i]];
								   
								   me.lineas_cuadro_2[7][columnas[i]] =  me.recursos[24][columnas[i]];
								   
								   me.lineas_cuadro_2[8][columnas[i]] =  me.recursos[26][columnas[i]] + me.recursos[27][columnas[i]] + me.recursos[28][columnas[i]];
								   
								   me.lineas_cuadro_2[9][columnas[i]] =  me.recursos[30][columnas[i]] + me.recursos[31][columnas[i]];
								   
								   me.lineas_cuadro_2[10][columnas[i]] =  me.recursos[33][columnas[i]] + me.recursos[34][columnas[i]];
								   
								   me.lineas_cuadro_2[2][columnas[i]] = me.lineas_cuadro_2[3][columnas[i]] + me.lineas_cuadro_2[4][columnas[i]];
								   
								   me.lineas_cuadro_2[1][columnas[i]] = me.lineas_cuadro_2[2][columnas[i]] + me.lineas_cuadro_2[5][columnas[i]] +
								   		me.lineas_cuadro_2[6][columnas[i]] + me.lineas_cuadro_2[7][columnas[i]] + me.lineas_cuadro_2[8][columnas[i]] +
								   		me.lineas_cuadro_2[9][columnas[i]];
								   
								   me.lineas_cuadro_2[0][columnas[i]] = me.lineas_cuadro_2[1][columnas[i]] + me.lineas_cuadro_2[10][columnas[i]];
								   
								   me.lineas_cuadro_2[14][columnas[i]] = me.gastos[4][columnas[i]] + me.gastos[5][columnas[i]] + me.gastos[6][columnas[i]] + me.gastos[7][columnas[i]];
								   me.lineas_cuadro_2[15][columnas[i]] = me.gastos[9][columnas[i]] + me.gastos[10][columnas[i]] + me.gastos[11][columnas[i]];
								   me.lineas_cuadro_2[16][columnas[i]] = me.gastos[12][columnas[i]] ;
								   me.lineas_cuadro_2[17][columnas[i]] = me.gastos[13][columnas[i]];
								   me.lineas_cuadro_2[20][columnas[i]] = me.gastos[16][columnas[i]];
								   me.lineas_cuadro_2[21][columnas[i]] = me.gastos[17][columnas[i]];
								   me.lineas_cuadro_2[22][columnas[i]] = me.gastos[18][columnas[i]];
								   me.lineas_cuadro_2[23][columnas[i]] = me.gastos[19][columnas[i]];
								   me.lineas_cuadro_2[24][columnas[i]] = me.gastos[20][columnas[i]];
								   
								   me.lineas_cuadro_2[25][columnas[i]]= me.gastos[22][columnas[i]] +  me.gastos[25][columnas[i]] +  me.gastos[26][columnas[i]] +
							   		me.gastos[28][columnas[i]] +  me.gastos[30][columnas[i]] +  me.gastos[31][columnas[i]];
								   
								   me.lineas_cuadro_2[27][columnas[i]]=0;
								   for(var j=35; j<43; j++)
									   me.lineas_cuadro_2[27][columnas[i]] += me.gastos[j][columnas[i]];
								   
								   me.lineas_cuadro_2[28][columnas[i]]= me.gastos[44][columnas[i]] + me.gastos[47][columnas[i]] + me.gastos[48][columnas[i]] +
								   		me.gastos[49][columnas[i]] +  me.gastos[50][columnas[i]] +  me.gastos[51][columnas[i]] +  me.gastos[53][columnas[i]] +  me.gastos[54][columnas[i]];
								   
								   me.lineas_cuadro_2[29][columnas[i]] = me.gastos[55][columnas[i]];
								   
								   me.lineas_cuadro_2[19][columnas[i]] = me.lineas_cuadro_2[20][columnas[i]] + me.lineas_cuadro_2[21][columnas[i]]; 
								   me.lineas_cuadro_2[18][columnas[i]] = me.lineas_cuadro_2[19][columnas[i]] + me.lineas_cuadro_2[22][columnas[i]] + me.lineas_cuadro_2[23][columnas[i]];
								   
								   me.lineas_cuadro_2[13][columnas[i]] = me.lineas_cuadro_2[14][columnas[i]] + me.lineas_cuadro_2[15][columnas[i]] + me.lineas_cuadro_2[16][columnas[i]] + me.lineas_cuadro_2[17][columnas[i]];
								   me.lineas_cuadro_2[12][columnas[i]] = me.lineas_cuadro_2[13][columnas[i]] + me.lineas_cuadro_2[18][columnas[i]] + me.lineas_cuadro_2[24][columnas[i]] + me.lineas_cuadro_2[25][columnas[i]];
								   
								   me.lineas_cuadro_2[26][columnas[i]] = me.lineas_cuadro_2[27][columnas[i]] + me.lineas_cuadro_2[28][columnas[i]] + me.lineas_cuadro_2[29][columnas[i]];
								   
								   me.lineas_cuadro_2[11][columnas[i]] = me.lineas_cuadro_2[12][columnas[i]] + me.lineas_cuadro_2[26][columnas[i]]; 
								   
								   
								   me.lineas_cuadro_2[30][columnas[i]] = me.lineas_cuadro_2[0][columnas[i]] - me.lineas_cuadro_2[11][columnas[i]];
								   me.lineas_cuadro_2[31][columnas[i]] = me.lineas_cuadro_2[0][columnas[i]] - me.lineas_cuadro_2[11][columnas[i]] + me.lineas_cuadro_2[19][columnas[i]];
								   me.lineas_cuadro_2[32][columnas[i]] = me.lineas_cuadro_2[1][columnas[i]] - me.lineas_cuadro_2[12][columnas[i]];
								   
								   me.lineas_cuadro_2[35][columnas[i]] = me.recursos[41][columnas[i]];
								   me.lineas_cuadro_2[36][columnas[i]] = me.gastos[59][columnas[i]];
								   me.lineas_cuadro_2[34][columnas[i]] = me.lineas_cuadro_2[35][columnas[i]] - me.lineas_cuadro_2[36][columnas[i]];
								   
								   
								   me.lineas_cuadro_2[38][columnas[i]] = me.recursos[39][columnas[i]];
								   me.lineas_cuadro_2[39][columnas[i]] = me.recursos[40][columnas[i]];
								   me.lineas_cuadro_2[40][columnas[i]] = me.gastos[57][columnas[i]] + me.gastos[58][columnas[i]];
								   
								   me.lineas_cuadro_2[37][columnas[i]] = me.lineas_cuadro_2[38][columnas[i]] + me.lineas_cuadro_2[39][columnas[i]] - me.lineas_cuadro_2[40][columnas[i]];
								   
								   me.lineas_cuadro_2[42][columnas[i]] = me.recursos[37][columnas[i]];
								   me.lineas_cuadro_2[41][columnas[i]] = me.recursos[37][columnas[i]];
								   
								   me.lineas_cuadro_2[33][columnas[i]] = me.lineas_cuadro_2[34][columnas[i]] + me.lineas_cuadro_2[37][columnas[i]] + me.lineas_cuadro_2[41][columnas[i]];
								   
							   }
							   var total_ingresos=0;
							   for(var k=0; k<me.recursos.length; k++)
								   total_ingresos+=(me.recursos[k].ejecutado_dos_antes!=null) ? me.recursos[k].ejecutado_dos_antes : 0;
							   
							   var total_egresos={
									   ejecutado_dos_antes: 0,
									   aprobado_anterior_mas_amp: 0,
									   recomendado: 0
							   };
							   for(var k=0; k<me.gastos.length; k++){
								   total_egresos.ejecutado_dos_antes+=me.gastos[k].ejecutado_dos_antes!=null ? me.gastos[k].ejecutado_dos_antes : 0;
								   total_egresos.aprobado_anterior_mas_amp+=me.gastos[k].aprobado_anterior_mas_amp!=null ? me.gastos[k].aprobado_anterior_mas_amp : 0;
								   total_egresos.recomendado+=me.gastos[k].recomendado!=null ? me.gastos[k].recomendado : 0;
							   }
								   
							   me.lineas_cuadro_2[42].ejecutado_dos_antes = total_egresos.ejecutado_dos_antes - total_ingresos;
							   me.lineas_cuadro_2[41].ejecutado_dos_antes = total_egresos.ejecutado_dos_antes - total_ingresos;
							   me.showloading=false;
							   
							   for(var i=0; i<columnas.length; i++){
								   me.lineas[0][columnas[i]] = (me.lineas_cuadro_2[0][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[1][columnas[i]] = (me.lineas_cuadro_2[1][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[2][columnas[i]] = (me.lineas_cuadro_2[2][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[3][columnas[i]] = ((me.lineas_cuadro_2[5][columnas[i]]+me.lineas_cuadro_2[6][columnas[i]] + me.lineas_cuadro_2[7][columnas[i]] 
								   + me.lineas_cuadro_2[8][columnas[i]] + me.lineas_cuadro_2[9][columnas[i]])/(me.pibs[i]*1000000));
								   me.lineas[4][columnas[i]] = (me.lineas_cuadro_2[10][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[5][columnas[i]] = (me.lineas_cuadro_2[11][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[6][columnas[i]] = (me.lineas_cuadro_2[12][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[7][columnas[i]] = (me.lineas_cuadro_2[13][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[8][columnas[i]] = (me.lineas_cuadro_2[18][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[9][columnas[i]] = (me.lineas_cuadro_2[24][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[10][columnas[i]] = (me.lineas_cuadro_2[25][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[11][columnas[i]] = (me.lineas_cuadro_2[26][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[12][columnas[i]] = (me.lineas_cuadro_2[27][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[13][columnas[i]] = (me.lineas_cuadro_2[28][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[14][columnas[i]] = (me.lineas_cuadro_2[29][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[15][columnas[i]] = (me.lineas_cuadro_2[32][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[16][columnas[i]] = (me.lineas_cuadro_2[31][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[17][columnas[i]] = (me.lineas_cuadro_2[30][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[18][columnas[i]] = (me.lineas_cuadro_2[33][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[19][columnas[i]] = ((me.lineas_cuadro_2[34][columnas[i]]+me.lineas_cuadro_2[37][columnas[i]])/(me.pibs[i]*1000000));
								   me.lineas[20][columnas[i]] = (me.lineas_cuadro_2[42][columnas[i]]/(me.pibs[i]*1000000));
								   me.lineas[21][columnas[i]] = (total_egresos[columnas[i]]/(me.pibs[i]*1000000));
								   
								   me.lineas[22][columnas[i]] = ((me.lineas_cuadro_2[19][columnas[i]]+me.lineas_cuadro_2[36][columnas[i]]+me.lineas_cuadro_2[40][columnas[i]])/(me.pibs[i]*1000000));
								   
								   me.lineas[23][columnas[i]] =(me.lineas_cuadro_2[2][columnas[i]]/me.lineas_cuadro_2[0][columnas[i]]);
								   me.lineas[24][columnas[i]] =((me.lineas_cuadro_2[5][columnas[i]]+me.lineas_cuadro_2[6][columnas[i]] + me.lineas_cuadro_2[7][columnas[i]] 
								   + me.lineas_cuadro_2[8][columnas[i]] + me.lineas_cuadro_2[9][columnas[i]])/me.lineas_cuadro_2[0][columnas[i]]);
								   me.lineas[25][columnas[i]] =(me.lineas_cuadro_2[10][columnas[i]]/me.lineas_cuadro_2[0][columnas[i]]);
								   me.lineas[26][columnas[i]] =(me.lineas_cuadro_2[19][columnas[i]]/me.lineas_cuadro_2[0][columnas[i]]);
								   
								   me.lineas[27][columnas[i]] =(me.lineas_cuadro_2[12][columnas[i]]-me.lineas_cuadro_2[19][columnas[i]])/total_egresos[columnas[i]];
								   me.lineas[28][columnas[i]] =(me.lineas_cuadro_2[26][columnas[i]]/total_egresos[columnas[i]]);
								   me.lineas[29][columnas[i]] =((me.lineas_cuadro_2[19][columnas[i]]+me.lineas_cuadro_2[36][columnas[i]]+me.lineas_cuadro_2[40][columnas[i]])/total_egresos[columnas[i]]);
							   }
						   }
						});
				   }
				});
		   }
		    
		});
}
]);