
angular.module('cuadro4Controller',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro4Controller',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.filtroMillones=function(value, transform){
		if(transform){
			var millones = value/1000000;
			return (value>0) ?  $filter('currency')(millones.toFixed(1), '', 1) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(1), '', 1).substring(1) + ')' : null)  ;
		}
		else 
			return value;
	}
	
	
	$http.post('/SInstitucional',  { action: 'getGastosTotal', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
		   if(response.data.success){
			   var stack_gasto=[];
			   var stack_suma=[];
			   var stack_suma_aprobado_anterior_mas_amp=[];
			   var stack_suma_ejecutado_dos_antes=[];
			   me.gastos=response.data.gastos;
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
		    me.showloading=false;
		});
	
		
}
]);