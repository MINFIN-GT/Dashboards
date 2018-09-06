
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
	
	me.showloading_excel = false;
	
	me.recursos_calculados=false;
	me.gastos_calculados=false;
	
	me.filtroMillones=function(value, transform){
		if(transform){
			var millones = value/1000000;
			return (value>0) ?  $filter('currency')(millones.toFixed(1), '', 1) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(1), '', 1).substring(1) + ')' : null)  ;
		}
		else 
			return value;
	}
	
	me.lineas_c1=[];
	me.lineas_c2=[];
	me.total_ingresos = 0.0;
	me.total_egresos = {};
	
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
		return ret.substring(ret.length-1,1)=='0' ? ret.substring(0,ret.length-1) : ret;
	}
	
	me.filtroCurrency=function(value){
		return $filter('currency')(value.toFixed(1), '', 1);
	}
	
	me.calcularCuadro1=function(){
		if(me.recursos_calculados && me.gastos_calculados){
			var columnas=['ejecutado_dos_antes','aprobado_anterior_mas_amp', 'recomendado'];  
			$http.post('/SIndicadoresFiscal',  { action: 'getReporte', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
			   if(response.data.success){
				   me.lineas_c1=response.data.lineas;
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
				   
				   me.lineas_cuadro_2[42].ejecutado_dos_antes = me.total_egresos.ejecutado_dos_antes - me.total_ingresos;
				   me.lineas_cuadro_2[41].ejecutado_dos_antes = me.total_egresos.ejecutado_dos_antes - me.total_ingresos;
				   me.lineas_cuadro_2[33].ejecutado_dos_antes = me.lineas_cuadro_2[34].ejecutado_dos_antes + me.lineas_cuadro_2[37].ejecutado_dos_antes + me.lineas_cuadro_2[41].ejecutado_dos_antes;
				   
				   me.showloading=false;
				   
				   for(var i=0; i<columnas.length; i++){
					   me.lineas_c1[0][columnas[i]] = (me.lineas_cuadro_2[0][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[1][columnas[i]] = (me.lineas_cuadro_2[1][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[2][columnas[i]] = (me.lineas_cuadro_2[2][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[3][columnas[i]] = ((me.lineas_cuadro_2[5][columnas[i]]+me.lineas_cuadro_2[6][columnas[i]] + me.lineas_cuadro_2[7][columnas[i]] 
					   + me.lineas_cuadro_2[8][columnas[i]] + me.lineas_cuadro_2[9][columnas[i]])/(me.pibs[i]*1000000));
					   me.lineas_c1[4][columnas[i]] = (me.lineas_cuadro_2[10][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[5][columnas[i]] = (me.lineas_cuadro_2[11][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[6][columnas[i]] = (me.lineas_cuadro_2[12][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[7][columnas[i]] = (me.lineas_cuadro_2[13][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[8][columnas[i]] = (me.lineas_cuadro_2[18][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[9][columnas[i]] = (me.lineas_cuadro_2[24][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[10][columnas[i]] = (me.lineas_cuadro_2[25][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[11][columnas[i]] = (me.lineas_cuadro_2[26][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[12][columnas[i]] = (me.lineas_cuadro_2[27][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[13][columnas[i]] = (me.lineas_cuadro_2[28][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[14][columnas[i]] = (me.lineas_cuadro_2[29][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[15][columnas[i]] = (me.lineas_cuadro_2[32][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[16][columnas[i]] = (me.lineas_cuadro_2[31][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[17][columnas[i]] = (me.lineas_cuadro_2[30][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[18][columnas[i]] = (me.lineas_cuadro_2[33][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[19][columnas[i]] = ((me.lineas_cuadro_2[34][columnas[i]]+me.lineas_cuadro_2[37][columnas[i]])/(me.pibs[i]*1000000));
					   me.lineas_c1[20][columnas[i]] = (me.lineas_cuadro_2[42][columnas[i]]/(me.pibs[i]*1000000));
					   me.lineas_c1[21][columnas[i]] = (me.total_egresos[columnas[i]]/(me.pibs[i]*1000000));
					   
					   me.lineas_c1[22][columnas[i]] = ((me.lineas_cuadro_2[19][columnas[i]]+me.lineas_cuadro_2[36][columnas[i]]+me.lineas_cuadro_2[40][columnas[i]])/(me.pibs[i]*1000000));
					   
					   me.lineas_c1[23][columnas[i]] =(me.lineas_cuadro_2[2][columnas[i]]/me.lineas_cuadro_2[0][columnas[i]]);
					   me.lineas_c1[24][columnas[i]] =((me.lineas_cuadro_2[5][columnas[i]]+me.lineas_cuadro_2[6][columnas[i]] + me.lineas_cuadro_2[7][columnas[i]] 
					   + me.lineas_cuadro_2[8][columnas[i]] + me.lineas_cuadro_2[9][columnas[i]])/me.lineas_cuadro_2[0][columnas[i]]);
					   me.lineas_c1[25][columnas[i]] =(me.lineas_cuadro_2[10][columnas[i]]/me.lineas_cuadro_2[0][columnas[i]]);
					   me.lineas_c1[26][columnas[i]] =(me.lineas_cuadro_2[19][columnas[i]]/me.lineas_cuadro_2[0][columnas[i]]);
					   
					   me.lineas_c1[27][columnas[i]] =(me.lineas_cuadro_2[12][columnas[i]]-me.lineas_cuadro_2[19][columnas[i]])/me.total_egresos[columnas[i]];
					   me.lineas_c1[28][columnas[i]] =(me.lineas_cuadro_2[26][columnas[i]]/me.total_egresos[columnas[i]]);
					   me.lineas_c1[29][columnas[i]] =((me.lineas_cuadro_2[19][columnas[i]]+me.lineas_cuadro_2[36][columnas[i]]+me.lineas_cuadro_2[40][columnas[i]])/me.total_egresos[columnas[i]]);
				   }
			   }
			});
		}
	}
	
	me.calcularCuadro2=function(){
		  if(me.recursos_calculados && me.gastos_calculados){
			  var columnas=['ejecutado_dos_antes','aprobado_anterior_mas_amp', 'recomendado'];  
			  $http.post('/SSituacion',  { action: 'getReporte', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
				   if(response.data.success){
					   me.lineas_c2=response.data.lineas;
					   for(var i=0; i<columnas.length; i++){
						   me.lineas_c2[3][columnas[i]] = me.recursos[4][columnas[i]] + me.recursos[5][columnas[i]] +
						   		me.recursos[6][columnas[i]] + me.recursos[7][columnas[i]] + me.recursos[8][columnas[i]];
						   
						   me.lineas_c2[4][columnas[i]] = me.recursos[10][columnas[i]] + me.recursos[12][columnas[i]] +
					   		me.recursos[13][columnas[i]] + me.recursos[14][columnas[i]] + me.recursos[15][columnas[i]] +
					   		me.recursos[16][columnas[i]] + me.recursos[17][columnas[i]];
						   
						   me.lineas_c2[5][columnas[i]] =  me.recursos[23][columnas[i]];
						   
						   me.lineas_c2[6][columnas[i]] =  me.recursos[19][columnas[i]] + me.recursos[20][columnas[i]] +
						   me.recursos[21][columnas[i]] + me.recursos[22][columnas[i]];
						   
						   me.lineas_c2[7][columnas[i]] =  me.recursos[24][columnas[i]];
						   
						   me.lineas_c2[8][columnas[i]] =  me.recursos[26][columnas[i]] + me.recursos[27][columnas[i]] + me.recursos[28][columnas[i]];
						   
						   me.lineas_c2[9][columnas[i]] =  me.recursos[30][columnas[i]] + me.recursos[31][columnas[i]];
						   
						   me.lineas_c2[10][columnas[i]] =  me.recursos[33][columnas[i]] + me.recursos[34][columnas[i]];
						   
						   me.lineas_c2[2][columnas[i]] = me.lineas_c2[3][columnas[i]] + me.lineas_c2[4][columnas[i]];
						   
						   me.lineas_c2[1][columnas[i]] = me.lineas_c2[2][columnas[i]] + me.lineas_c2[5][columnas[i]] +
						   		me.lineas_c2[6][columnas[i]] + me.lineas_c2[7][columnas[i]] + me.lineas_c2[8][columnas[i]] +
						   		me.lineas_c2[9][columnas[i]];
						   
						   me.lineas_c2[0][columnas[i]] = me.lineas_c2[1][columnas[i]] + me.lineas_c2[10][columnas[i]];
						   
						   me.lineas_c2[14][columnas[i]] = me.gastos[4][columnas[i]] + me.gastos[5][columnas[i]] + me.gastos[6][columnas[i]] + me.gastos[7][columnas[i]];
						   me.lineas_c2[15][columnas[i]] = me.gastos[9][columnas[i]] + me.gastos[10][columnas[i]] + me.gastos[11][columnas[i]];
						   me.lineas_c2[16][columnas[i]] = me.gastos[12][columnas[i]] ;
						   me.lineas_c2[17][columnas[i]] = me.gastos[13][columnas[i]];
						   me.lineas_c2[20][columnas[i]] = me.gastos[16][columnas[i]];
						   me.lineas_c2[21][columnas[i]] = me.gastos[17][columnas[i]];
						   me.lineas_c2[22][columnas[i]] = me.gastos[18][columnas[i]];
						   me.lineas_c2[23][columnas[i]] = me.gastos[19][columnas[i]];
						   me.lineas_c2[24][columnas[i]] = me.gastos[20][columnas[i]];
						   
						   me.lineas_c2[25][columnas[i]]= me.gastos[22][columnas[i]] +  me.gastos[25][columnas[i]] +  me.gastos[26][columnas[i]] +
					   		me.gastos[28][columnas[i]] +  me.gastos[30][columnas[i]] +  me.gastos[31][columnas[i]];
						   
						   me.lineas_c2[27][columnas[i]]=0;
						   for(var j=35; j<43; j++)
							   me.lineas_c2[27][columnas[i]] += me.gastos[j][columnas[i]];
						   
						   me.lineas_c2[28][columnas[i]]= me.gastos[44][columnas[i]] + me.gastos[47][columnas[i]] + me.gastos[48][columnas[i]] +
						   		me.gastos[49][columnas[i]] +  me.gastos[50][columnas[i]] +  me.gastos[51][columnas[i]] +  me.gastos[53][columnas[i]] +  me.gastos[54][columnas[i]];
						   
						   me.lineas_c2[29][columnas[i]] = me.gastos[55][columnas[i]];
						   
						   me.lineas_c2[19][columnas[i]] = me.lineas_c2[20][columnas[i]] + me.lineas_c2[21][columnas[i]]; 
						   me.lineas_c2[18][columnas[i]] = me.lineas_c2[19][columnas[i]] + me.lineas_c2[22][columnas[i]] + me.lineas_c2[23][columnas[i]];
						   
						   me.lineas_c2[13][columnas[i]] = me.lineas_c2[14][columnas[i]] + me.lineas_c2[15][columnas[i]] + me.lineas_c2[16][columnas[i]] + me.lineas_c2[17][columnas[i]];
						   me.lineas_c2[12][columnas[i]] = me.lineas_c2[13][columnas[i]] + me.lineas_c2[18][columnas[i]] + me.lineas_c2[24][columnas[i]] + me.lineas_c2[25][columnas[i]];
						   
						   me.lineas_c2[26][columnas[i]] = me.lineas_c2[27][columnas[i]] + me.lineas_c2[28][columnas[i]] + me.lineas_c2[29][columnas[i]];
						   
						   me.lineas_c2[11][columnas[i]] = me.lineas_c2[12][columnas[i]] + me.lineas_c2[26][columnas[i]]; 
						   
						   
						   me.lineas_c2[30][columnas[i]] = me.lineas_c2[0][columnas[i]] - me.lineas_c2[11][columnas[i]];
						   me.lineas_c2[31][columnas[i]] = me.lineas_c2[0][columnas[i]] - me.lineas_c2[11][columnas[i]] + me.lineas_c2[19][columnas[i]];
						   me.lineas_c2[32][columnas[i]] = me.lineas_c2[1][columnas[i]] - me.lineas_c2[12][columnas[i]];
						   
						   me.lineas_c2[35][columnas[i]] = me.recursos[41][columnas[i]];
						   me.lineas_c2[36][columnas[i]] = me.gastos[59][columnas[i]];
						   me.lineas_c2[34][columnas[i]] = me.lineas_c2[35][columnas[i]] - me.lineas_c2[36][columnas[i]];
						   
						   me.lineas_c2[38][columnas[i]] = me.recursos[39][columnas[i]];
						   me.lineas_c2[39][columnas[i]] = me.recursos[40][columnas[i]];
						   me.lineas_c2[40][columnas[i]] = me.gastos[57][columnas[i]] + me.gastos[58][columnas[i]];
						   
						   me.lineas_c2[37][columnas[i]] = me.lineas_c2[38][columnas[i]] + me.lineas_c2[39][columnas[i]] + me.lineas_c2[40][columnas[i]];
						   
						   
						   
						   me.lineas_c2[42][columnas[i]] = me.recursos[37][columnas[i]];
						   me.lineas_c2[41][columnas[i]] = me.recursos[37][columnas[i]];
						   
						   me.lineas_c2[33][columnas[i]] = me.lineas_c2[34][columnas[i]] + me.lineas_c2[37][columnas[i]] - me.lineas_c2[41][columnas[i]];
					   }
					      
					   me.lineas_c2[42].ejecutado_dos_antes = me.total_egresos.ejecutado_dos_antes - me.total_ingresos;
					   me.lineas_c2[41].ejecutado_dos_antes = me.total_egresos.ejecutado_dos_antes - me.total_ingresos;
					   me.showloading[1]=false;
					   me.calcularCuadro1();
				   }
			  });
		  }
	}
	
	$http.post('/SInstitucional',  { action: 'getRecursosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
	   if(response.data.success){
		   var stack_recurso=[];
		   var stack_suma=[];
		   var stack_suma_aprobado_anterior_mas_amp=[];
		   var stack_suma_ejecutado_dos_antes=[];
		   me.recursos=response.data.recursos;
		   me.total_ingresos=0;
		   
		   for(var k=0; k<me.recursos.length; k++)
			   me.total_ingresos+=(me.recursos[k].ejecutado_dos_antes!=null) ? me.recursos[k].ejecutado_dos_antes : 0;
		   
		   me.recursos_calculados=true;
		   me.calcularCuadro2();
	    	for(var i=0; i<me.recursos.length-1; i++){
	    		if(me.recursos[i+1].nivel>me.recursos[i].nivel){
	    			stack_recurso.push(i);
	    			stack_suma.push(0.0);
	    			stack_suma_aprobado_anterior_mas_amp.push(0.0);
	    			stack_suma_ejecutado_dos_antes.push(0.0);
	    		}
	    		else if(me.recursos[i+1].nivel==me.recursos[i].nivel){
	    			stack_suma[stack_suma.length-1] += me.recursos[i].recomendado;
	    			stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1] += me.recursos[i].aprobado_anterior_mas_amp;
	    			stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1] += me.recursos[i].ejecutado_dos_antes;
	    		}
	    		else if(me.recursos[i+1].nivel<me.recursos[i].nivel){
	    			var stack_anterior = me.recursos[i].recomendado;
	    			var stack_anterior_aprobado_anterior_mas_amp = me.recursos[i].aprobado_anterior_mas_amp;
	    			var stack_anterior_ejecutado_dos_antes = me.recursos[i].ejecutado_dos_antes;
	    			for(var k=0;  k<(me.recursos[i].nivel-me.recursos[i+1].nivel);k++){
		    			if(k==0){
		    				stack_suma[stack_suma.length-1] +=stack_anterior;
		    				stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1] +=stack_anterior_aprobado_anterior_mas_amp;
		    				stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1] +=stack_anterior_ejecutado_dos_antes;
		    			}
			    		stack_anterior = stack_suma[stack_suma.length-1];
			    		stack_anterior_aprobado_anterior_mas_amp = stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
			    		stack_anterior_ejecutado_dos_antes = stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
			    		me.recursos[stack_recurso[stack_recurso.length-1]].recomendado = stack_suma[stack_suma.length-1];
			    		me.recursos[stack_recurso[stack_recurso.length-1]].aprobado_anterior_mas_amp = stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
			    		me.recursos[stack_recurso[stack_recurso.length-1]].ejecutado_dos_antes = stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
			    		if(stack_suma.length>1){
		    				stack_suma[stack_suma.length-2] += stack_anterior;
		    				stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-2] += stack_anterior_aprobado_anterior_mas_amp;
		    				stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-2] += stack_anterior_ejecutado_dos_antes;
		    			}
		    			stack_recurso.pop();
			    		stack_suma.pop();
			    		stack_suma_aprobado_anterior_mas_amp.pop();
			    		stack_suma_ejecutado_dos_antes.pop();
		    		}
	    		}
	    	}
	    	stack_suma[stack_suma.length-1] += me.recursos[i].recomendado;
	    	stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1] += me.recursos[i].aprobado_anterior_mas_amp;
	    	stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1] += me.recursos[i].ejecutado_dos_antes;
    		while(stack_recurso.length>0){
	    		stack_suma[stack_suma.length-2] += stack_suma[stack_suma.length-1];
	    		stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-2] += stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
	    		stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-2] += stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
	    		me.recursos[stack_recurso[stack_recurso.length-1]].recomendado = stack_suma[stack_suma.length-1];
	    		me.recursos[stack_recurso[stack_recurso.length-1]].aprobado_anterior_mas_amp = stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
	    		me.recursos[stack_recurso[stack_recurso.length-1]].ejecutado_dos_antes = stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
	    		stack_recurso.pop();
    			stack_suma.pop();
    			stack_suma_aprobado_anterior_mas_amp.pop();
    			stack_suma_ejecutado_dos_antes.pop();
	    	}
	    }
	    me.showloading[2]=false;
	});
	
	$http.post('/SInstitucional',  { action: 'getGastosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		   if(response.data.success){
			   var stack_gasto=[];
			   var stack_suma=[];
			   var stack_suma_aprobado_anterior_mas_amp=[];
			   var stack_suma_ejecutado_dos_antes=[];
			   me.gastos=response.data.gastos;
			   
			   me.total_egresos={
					   ejecutado_dos_antes: 0,
					   aprobado_anterior_mas_amp: 0,
					   recomendado: 0
			   };
			   for(var k=0; k<me.gastos.length; k++){
				   me.total_egresos.ejecutado_dos_antes+=me.gastos[k].ejecutado_dos_antes!=null ? me.gastos[k].ejecutado_dos_antes : 0;
				   me.total_egresos.aprobado_anterior_mas_amp+=me.gastos[k].aprobado_anterior_mas_amp!=null ? me.gastos[k].aprobado_anterior_mas_amp : 0;
				   me.total_egresos.recomendado+=me.gastos[k].recomendado!=null ? me.gastos[k].recomendado : 0;
			   }
			   
			   me.gastos_calculados=true;
			   me.calcularCuadro2();
		    	for(var i=0; i<me.gastos.length-1; i++){
		    		if(me.gastos[i+1].nivel>me.gastos[i].nivel){
		    			stack_gasto.push(i);
		    			stack_suma.push(0.0);
		    			stack_suma_aprobado_anterior_mas_amp.push(0.0);
		    			stack_suma_ejecutado_dos_antes.push(0.0);
		    		}
		    		else if(me.gastos[i+1].nivel==me.gastos[i].nivel){
		    			stack_suma[stack_suma.length-1] += me.gastos[i].recomendado;
		    			stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1] += me.gastos[i].aprobado_anterior_mas_amp;
		    			stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1] += me.gastos[i].ejecutado_dos_antes;
		    		}
		    		else if(me.gastos[i+1].nivel<me.gastos[i].nivel){
		    			var stack_anterior = me.gastos[i].recomendado;
		    			var stack_anterior_aprobado_anterior_mas_amp = me.gastos[i].aprobado_anterior_mas_amp;
		    			var stack_anterior_ejecutado_dos_antes = me.gastos[i].ejecutado_dos_antes;
		    			for(var k=0;  k<(me.gastos[i].nivel-me.gastos[i+1].nivel);k++){
			    			if(k==0){
			    				stack_suma[stack_suma.length-1] +=stack_anterior;
			    				stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1] +=stack_anterior_aprobado_anterior_mas_amp;
			    				stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1] +=stack_anterior_ejecutado_dos_antes;
			    			}
				    		stack_anterior = stack_suma[stack_suma.length-1];
				    		stack_anterior_aprobado_anterior_mas_amp = stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
				    		stack_anterior_ejecutado_dos_antes = stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
				    		me.gastos[stack_gasto[stack_gasto.length-1]].recomendado = stack_suma[stack_suma.length-1];
				    		me.gastos[stack_gasto[stack_gasto.length-1]].aprobado_anterior_mas_amp = stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
				    		me.gastos[stack_gasto[stack_gasto.length-1]].ejecutado_dos_antes = stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
				    		if(stack_suma.length>1){
			    				stack_suma[stack_suma.length-2] += stack_anterior;
			    				stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-2] += stack_anterior_aprobado_anterior_mas_amp;
			    				stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-2] += stack_anterior_ejecutado_dos_antes;
			    			}
			    			stack_gasto.pop();
				    		stack_suma.pop();
				    		stack_suma_aprobado_anterior_mas_amp.pop();
				    		stack_suma_ejecutado_dos_antes.pop();
			    		}
		    		}
		    	}
		    	stack_suma[stack_suma.length-1] += me.gastos[i].recomendado;
		    	stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1] += me.gastos[i].aprobado_anterior_mas_amp;
		    	stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1] += me.gastos[i].ejecutado_dos_antes;
	    		while(stack_gasto.length>0){
		    		stack_suma[stack_suma.length-2] += stack_suma[stack_suma.length-1];
		    		stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-2] += stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
		    		stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-2] += stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
		    		me.gastos[stack_gasto[stack_gasto.length-1]].recomendado = stack_suma[stack_suma.length-1];
		    		me.gastos[stack_gasto[stack_gasto.length-1]].aprobado_anterior_mas_amp = stack_suma_aprobado_anterior_mas_amp[stack_suma_aprobado_anterior_mas_amp.length-1];
		    		me.gastos[stack_gasto[stack_gasto.length-1]].ejecutado_dos_antes = stack_suma_ejecutado_dos_antes[stack_suma_ejecutado_dos_antes.length-1];
		    		stack_gasto.pop();
	    			stack_suma.pop();
	    			stack_suma_aprobado_anterior_mas_amp.pop();
	    			stack_suma_ejecutado_dos_antes.pop();
		    	}
		    }
		    me.showloading[3]=false;
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
		
		me.exportarExcel=function(){
			me.showloading_excel=true;
			$http.post('/SCuadrosExportar',  { action: 'exportarExcel', ejercicio: me.anio, numeroCuadro: -1, t: (new Date()).getTime()   }).then(
					function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
							 href: 'data:application/ms-excel;base64,' + response.data,
							 target: '_blank',
							 download: 'Cuadros_Globales_Recomendado_' + me.anio + '.xls'
						  })[0].click();
						  me.showloading_excel=false;
					  }.bind(this), function errorCallback(response){				
			});
		} 
}
]);