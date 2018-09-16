
angular.module('cuadro5detalleController',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro5detalleController',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.entidades = [];
	me.data_chart = [];
	me.total_aprobado_anterior_mas_ampliaciones=0.0;
	me.total_ejecutado_dos_antes=0.0;
	me.total_recomendado=0.0;
	
	me.filtroMillones=function(value, transform){
		var millones = value;
		var fixed=2;
		if(transform){
			millones = value/1000000;
			fixed=1;
		}
		return (value>0) ?  $filter('currency')(millones.toFixed(fixed), '', fixed) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(fixed), '', fixed).substring(1) + ')' : null)  ;
	}
	
	
	$http.post('/SInstitucional',  { action: 'getInstitucionalTotalDetalle', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
	    if(response.data.success){
	    	me.entidades=response.data.entidades;
	    	for(var i=0; i<me.entidades.length; i++){
	    		me.entidades[i].showChildren=false;
	    		me.entidades[i].show=true;
	    		me.entidades[i].parent=null; 
	    		me.total_aprobado_anterior_mas_ampliaciones += me.entidades[i].aprobado_anterior_mas_ampliaciones;
	    		me.total_ejecutado_dos_antes += me.entidades[i].ejecutado_dos_antes;
	    		me.total_recomendado += me.entidades[i].recomendado;
	    	}
	    	google.charts.load('current', {'packages':['treemap']});
    	    google.charts.setOnLoadCallback(me.drawChart);
	    }
	    me.showloading=false;
	});
	
	me.clickRow=function(row,index){
		if(row.showChildren==false && row.nivel<6){ //Mostrar hijos
			row.showChildren=true;
			if(me.entidades[index+1].nivel>row.nivel){
	    		while(me.entidades[index+1].nivel>row.nivel && index<me.entidades.length){
	    			if(me.entidades[index+1].nivel==row.nivel+1)
	    				me.entidades[index+1].show=true;
	    			index++;
	    		}
	    	}
			else{
				row.showloading=true;
				$http.post('/SInstitucional',  { action: 'getInstitucionalTotalDetalle', ejercicio: me.anio, 
					entidad: (row.nivel==1) ? row.codigo : row.entidad,
					unidad_ejecutora: (row.nivel==2) ? row.codigo : row.unidad_ejecutora,
					programa: (row.nivel==3) ? row.codigo : row.programa,
					grupo: (row.nivel==4) ? row.codigo : row.grupo,
					subgrupo: (row.nivel==5) ? row.codigo : row.subgrupo,
					t: (new Date()).getTime()   }).then(function(response){
				    if(response.data.success){
				    	if(row.nivel==1&&response.data.entidades.length>1){
				    		response.data.entidades.splice(0,1);
				    	}
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
				    	me.entidades.splice.apply(me.entidades,[index+1,0].concat(response.data.entidades));
				    }
				    row.showloading=false;
				});
			}
		}
		else{ //Borrar hijos
			row.showChildren=false;
			var pos=index+1;
			while(me.entidades[pos].nivel>row.nivel && pos<me.entidades.length){
				me.entidades[pos].show=false;
				me.entidades[pos].showChildren=false;
				pos++;
			}
		}
	}
	
	me.drawChart=function(){
		me.data_chart=[['Entidad','Parent','Ejecutado_Dos_Antes']];
		me.data_chart.push(['Administración Central',null,me.total_ejecutado_dos_antes]);
		for(var i=0; i<me.entidades.length; i++){
			me.data_chart.push([{ v: me.entidades[i].nombre, f: me.entidades[i].nombre.substring(0,4)+'...' },'Administración Central',me.entidades[i].ejecutado_dos_antes]);
		}
		var data = google.visualization.arrayToDataTable(me.data_chart);
		tree = new google.visualization.TreeMap(document.getElementById('chart_div'));
		tree.draw(data, {
	          minColor: '#f00',
	          midColor: '#ddd',
	          maxColor: '#0d0',
	          headerHeight: 15,
	          fontColor: 'black',
	          showScale: false,
	          generateTooltip: function(row, size, value){
	        	  return '<div class="panel panel-default"><div class="panel-body">'+
	        	  		me.data_chart[row+1][0].v +
	        	  		'</div></div>';
	      		}
	        });
		google.visualization.events.addListener(tree, 'select', me.onClickChart);
	}
	
	me.onClickChart=function(){
	    var selection = tree.getSelection();
	    console.log(selection);
	}
	
}
]);