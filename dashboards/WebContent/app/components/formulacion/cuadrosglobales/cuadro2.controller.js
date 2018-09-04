
angular.module('cuadro2Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro2Controller',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.filtroMillones=function(value, transform){
		if(transform){
			var millones = value/1000000;
			return (value>0) ?  $filter('currency')(millones, '', 1) : ( value<0 ? '(' + $filter('currency')(millones, '', 1).substring(1) + ')' : null)  ;
		}
		else 
			return value;
	}
	
	function roundWithPrecision(num, precision) {
			if(num!=null){
			  var multiplier = Math.pow(10, precision);
			  return Math.round( num * multiplier ) / multiplier;
			}
			else
				return null;
	}
	
	
	$http.post('/SInstitucional',  { action: 'getRecursosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		var columnas=['ejecutado_dos_antes','aprobado_anterior_mas_amp', 'recomendado'];  
		if(response.data.success){
			   me.recursos=response.data.recursos;
			   /*for(var i=0; i<me.recursos.length;i++){
				   me.recursos[i].ejecutado_dos_antes = me.recursos[i].ejecutado_dos_antes/1000000.00;
				   me.recursos[i].aprobado_anterior_mas_amp = me.recursos[i].aprobado_anterior_mas_amp/1000000.00;
				   me.recursos[i].recomendado = me.recursos[i].recomendado/1000000.00;
			   }*/
		       $http.post('/SInstitucional',  { action: 'getGastosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
				   if(response.data.success){
					   me.gastos=response.data.gastos;
					   /*for(var i=0; i<me.gastos.length;i++){
						   me.gastos[i].ejecutado_dos_antes = me.gastos[i].ejecutado_dos_antes/1000000.00;
						   me.gastos[i].aprobado_anterior_mas_amp = me.gastos[i].aprobado_anterior_mas_amp/1000000.00;
						   me.gastos[i].recomendado = me.gastos[i].recomendado/1000000.00;
					   }*/
					   $http.post('/SSituacion',  { action: 'getReporte', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
						   if(response.data.success){
							   me.lineas=response.data.lineas;
							   
							   for(var i=0; i<columnas.length; i++){
								   me.lineas[3][columnas[i]] = me.recursos[4][columnas[i]] + me.recursos[5][columnas[i]] +
								   		me.recursos[6][columnas[i]] + me.recursos[7][columnas[i]] + me.recursos[8][columnas[i]];
								   
								   me.lineas[4][columnas[i]] = me.recursos[10][columnas[i]] + me.recursos[12][columnas[i]] +
							   		me.recursos[13][columnas[i]] + me.recursos[14][columnas[i]] + me.recursos[15][columnas[i]] +
							   		me.recursos[16][columnas[i]] + me.recursos[17][columnas[i]];
								   
								   me.lineas[5][columnas[i]] =  me.recursos[23][columnas[i]];
								   
								   me.lineas[6][columnas[i]] =  me.recursos[19][columnas[i]] + me.recursos[20][columnas[i]] +
								   me.recursos[21][columnas[i]] + me.recursos[22][columnas[i]];
								   
								   me.lineas[7][columnas[i]] =  me.recursos[24][columnas[i]];
								   
								   me.lineas[8][columnas[i]] =  me.recursos[26][columnas[i]] + me.recursos[27][columnas[i]] + me.recursos[28][columnas[i]];
								   
								   me.lineas[9][columnas[i]] =  me.recursos[30][columnas[i]] + me.recursos[31][columnas[i]];
								   
								   me.lineas[10][columnas[i]] =  me.recursos[33][columnas[i]] + me.recursos[34][columnas[i]];
								   
								   me.lineas[2][columnas[i]] = me.lineas[3][columnas[i]] + me.lineas[4][columnas[i]];
								   
								   me.lineas[1][columnas[i]] = me.lineas[2][columnas[i]] + me.lineas[5][columnas[i]] +
								   		me.lineas[6][columnas[i]] + me.lineas[7][columnas[i]] + me.lineas[8][columnas[i]] +
								   		me.lineas[9][columnas[i]];
								   
								   me.lineas[0][columnas[i]] = me.lineas[1][columnas[i]] + me.lineas[10][columnas[i]];
								   
								   me.lineas[14][columnas[i]] = me.gastos[4][columnas[i]] + me.gastos[5][columnas[i]] + me.gastos[6][columnas[i]] + me.gastos[7][columnas[i]];
								   me.lineas[15][columnas[i]] = me.gastos[9][columnas[i]] + me.gastos[10][columnas[i]] + me.gastos[11][columnas[i]];
								   me.lineas[16][columnas[i]] = me.gastos[12][columnas[i]] ;
								   me.lineas[17][columnas[i]] = me.gastos[13][columnas[i]];
								   me.lineas[20][columnas[i]] = me.gastos[16][columnas[i]];
								   me.lineas[21][columnas[i]] = me.gastos[17][columnas[i]];
								   me.lineas[22][columnas[i]] = me.gastos[18][columnas[i]];
								   me.lineas[23][columnas[i]] = me.gastos[19][columnas[i]];
								   me.lineas[24][columnas[i]] = me.gastos[20][columnas[i]];
								   
								   me.lineas[25][columnas[i]]= me.gastos[22][columnas[i]] +  me.gastos[25][columnas[i]] +  me.gastos[26][columnas[i]] +
							   		me.gastos[28][columnas[i]] +  me.gastos[30][columnas[i]] +  me.gastos[31][columnas[i]];
								   
								   me.lineas[27][columnas[i]]=0;
								   for(var j=35; j<43; j++)
									   me.lineas[27][columnas[i]] += me.gastos[j][columnas[i]];
								   
								   me.lineas[28][columnas[i]]= me.gastos[44][columnas[i]] + me.gastos[47][columnas[i]] + me.gastos[48][columnas[i]] +
								   		me.gastos[49][columnas[i]] +  me.gastos[50][columnas[i]] +  me.gastos[51][columnas[i]] +  me.gastos[53][columnas[i]] +  me.gastos[54][columnas[i]];
								   
								   me.lineas[29][columnas[i]] = me.gastos[55][columnas[i]];
								   
								   me.lineas[19][columnas[i]] = me.lineas[20][columnas[i]] + me.lineas[21][columnas[i]]; 
								   me.lineas[18][columnas[i]] = me.lineas[19][columnas[i]] + me.lineas[22][columnas[i]] + me.lineas[23][columnas[i]];
								   
								   me.lineas[13][columnas[i]] = me.lineas[14][columnas[i]] + me.lineas[15][columnas[i]] + me.lineas[16][columnas[i]] + me.lineas[17][columnas[i]];
								   me.lineas[12][columnas[i]] = me.lineas[13][columnas[i]] + me.lineas[18][columnas[i]] + me.lineas[24][columnas[i]] + me.lineas[25][columnas[i]];
								   
								   me.lineas[26][columnas[i]] = me.lineas[27][columnas[i]] + me.lineas[28][columnas[i]] + me.lineas[29][columnas[i]];
								   
								   me.lineas[11][columnas[i]] = me.lineas[12][columnas[i]] + me.lineas[26][columnas[i]]; 
								   
								   
								   me.lineas[30][columnas[i]] = me.lineas[0][columnas[i]] - me.lineas[11][columnas[i]];
								   me.lineas[31][columnas[i]] = me.lineas[0][columnas[i]] - me.lineas[11][columnas[i]] + me.lineas[19][columnas[i]];
								   me.lineas[32][columnas[i]] = me.lineas[1][columnas[i]] - me.lineas[12][columnas[i]];
								   
								   me.lineas[35][columnas[i]] = me.recursos[41][columnas[i]];
								   me.lineas[36][columnas[i]] = me.gastos[59][columnas[i]];
								   me.lineas[34][columnas[i]] = me.lineas[35][columnas[i]] - me.lineas[36][columnas[i]];
								   
								   me.lineas[38][columnas[i]] = me.recursos[39][columnas[i]];
								   me.lineas[39][columnas[i]] = me.recursos[40][columnas[i]];
								   me.lineas[40][columnas[i]] = me.gastos[57][columnas[i]] + me.gastos[58][columnas[i]];
								   
								   me.lineas[37][columnas[i]] = me.lineas[38][columnas[i]] + me.lineas[39][columnas[i]] - me.lineas[40][columnas[i]];
								   
								   
								   
								   me.lineas[42][columnas[i]] = me.recursos[37][columnas[i]];
								   me.lineas[41][columnas[i]] = me.recursos[37][columnas[i]];
								   
								   me.lineas[33][columnas[i]] = me.lineas[34][columnas[i]] + me.lineas[37][columnas[i]] + me.lineas[41][columnas[i]];
							   }
							   var total_ingresos=0;
							   for(var k=0; k<me.recursos.length; k++)
								   total_ingresos+=(me.recursos[k].ejecutado_dos_antes!=null) ? me.recursos[k].ejecutado_dos_antes : 0;
							   
							   var total_egresos=0;
							   for(var k=0; k<me.gastos.length; k++)
								   total_egresos+=me.gastos[k].ejecutado_dos_antes!=null ? me.gastos[k].ejecutado_dos_antes : 0;
								   
							   me.lineas[42].ejecutado_dos_antes = total_egresos - total_ingresos;
							   me.lineas[41].ejecutado_dos_antes = total_egresos - total_ingresos;
							   me.lineas[33].ejecutado_dos_antes = me.lineas[34].ejecutado_dos_antes + me.lineas[37].ejecutado_dos_antes + me.lineas[41].ejecutado_dos_antes;
							   me.showloading=false;
						   }
						});
				   }
				});
		   }
		    
		});
}
]);