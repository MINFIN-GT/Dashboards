
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
	
	me.chart=null;
	me.chartPath=[];
	me.chartTitle="Administración Central";
	
	me.showChartLoading=false;
	
	document.oncontextmenu=function(){
		return false;
	};
	
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
	    	me.drawFirstChart();
	    }
	    me.showloading=false;
	});
	
	me.clickRow=function(row,index){
		if(row.showChildren==false && row.nivel<6){ //Mostrar hijos
			row.showChildren=true;
			if((index+1)<me.entidades.length && me.entidades[index+1].nivel>row.nivel){
	    		while((index+1)<me.entidades.length && me.entidades[index+1].nivel>row.nivel){
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
			while(pos<me.entidades.length && me.entidades[pos].nivel>row.nivel){
				me.entidades[pos].show=false;
				me.entidades[pos].showChildren=false;
				pos++;
			}
		}
	}
	
	me.drawFirstChart=function(){
		me.data_chart=[];
		for(var i=0; i<me.entidades.length; i++){
			me.data_chart.push({ 
				parent: 0,
				id: me.entidades[i].nombre,
				nivel: 1,
				entidad: me.entidades[i].codigo,
				ejecutado_dos_antes: me.entidades[i].ejecutado_dos_antes,
				aprobado_anterior_mas_ampliaciones: me.entidades[i].aprobado_anterior_mas_ampliaciones,
				recomendado: me.entidades[i].recomendado
			});
		}
		me.drawChart(me.data_chart);
	}
	
	me.drawChart = function (data_chart){
		me.chart=new d3plus.Treemap()
		  .data(data_chart)
		  .groupBy(["parent", "id"])
		  .select("#chart_div")
		  .sum("recomendado")
		  .color("recomendado")
		  .legend(false)
		  .tooltipConfig({
			    body: function(d) {
			      var table = "<table class='tooltip-table'>";
			      table += "<tr><td>Ejecutado "+(me.anio-2)+":</td><td class='data'>"+ (me.viewMillones ? 'Q ' : '') + me.filtroMillones(d.ejecutado_dos_antes, me.viewMillones) + "</td></tr>";
			      table += "<tr><td>Aprobado "+(me.anio-1)+":</td><td class='data'>"+ (me.viewMillones ? 'Q ' : '')  + me.filtroMillones(d.aprobado_anterior_mas_ampliaciones, me.viewMillones) + "</td></tr>";
			      table += "<tr><td>Recomendado "+me.anio+":</td><td class='data'>"+ (me.viewMillones ? 'Q ' : '')  + me.filtroMillones(d.recomendado, me.viewMillones) + "</td></tr>";
			      table += "</table>";
			      return table;
			    },
			    footer: function(d) {
			      return "<sub class='tooltip-footer'></sub>";
			    },
			    title: function(d) {
			      var txt = d.id;
			      return '<span class="tooltip-title">'+txt+'</span>';
			    }
			  })
		  .shapeConfig({
		    labelConfig: {
		      fontMax: 14
		    }
		  })
		  .on("mousedown", function(d){
			  switch(event.button){
			  	/*case 0:
			  		me.clickChart(d,'forward');
			  		break;*/
			  	case 2:
			  		me.clickChart(d,'backward');
			  		break;
			  }
		   })
		   .on("click",function(d){
			   me.clickChart(d,'forward');
		   })
		   .on("touchstart",function(d){
			   me.clickChart(d,'forward');
		   })
		  .render();
	}
	
	me.clickChart=function(row, direction){
		if((direction=='forward' && row.nivel<6) || (direction=='backward' && row.nivel>1)){
			me.showChartLoading=true;
			if(direction=='backward'){
				me.chartPath.pop();
				row.nivel = row.nivel-2;
			}
			else{
				me.chartPath.push({nombre: row.id, entidad: row.entidad, unidad_ejecutora: row.unidad_ejecutora, programa: row.programa, 
					grupo: row.grupo, subgrupo: row.subgrupo, nivel: row.nivel+1});
			}
			$http.post('/SInstitucional',  { action: 'getInstitucionalTotalDetalle', ejercicio: me.anio, 
				entidad: (row.nivel>=1) ? row.entidad : -1,
				unidad_ejecutora: (row.nivel>=2) ? row.unidad_ejecutora : -1,
				programa: (row.nivel>=3) ? row.programa : -1,
				grupo: (row.nivel>=4) ? row.grupo : -1,
				subgrupo: (row.nivel>=5) ? row.subgrupo : -1,
				t: (new Date()).getTime()   }).then(function(response){
			    if(response.data.success){
			    	if(row.nivel==1&&response.data.entidades.length>1){
			    		response.data.entidades.splice(0,1);
			    	}
			    	me.data_chart=[];
			    	for(var i=0;i<response.data.entidades.length; i++){
			    		if(row.nivel===0){
			    			response.data.entidades[i].parent = 0;
			    			response.data.entidades[i].entidad = response.data.entidades[i].codigo;
			    		}
			    		else if(row.nivel==1){
			    			response.data.entidades[i].entidad = row.entidad;
			    			response.data.entidades[i].parent = row.entidad;
			    			response.data.entidades[i].unidad_ejecutora = response.data.entidades[i].codigo;
			    		}
			    		else if(row.nivel==2){
			    			response.data.entidades[i].programa = response.data.entidades[i].codigo;
			    			response.data.entidades[i].entidad = row.entidad;
			    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.entidades[i].parent = row.unidad_ejecutora;
			    		}
			    		else if(row.nivel==3){
			    			response.data.entidades[i].grupo = response.data.entidades[i].codigo;
			    			response.data.entidades[i].programa = row.programa;
			    			response.data.entidades[i].entidad = row.entidad;
			    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.entidades[i].parent = row.programa;
			    			response.data.entidades[i].nombre = response.data.entidades[i].codigo + ' - ' + response.data.entidades[i].nombre;
			    		}
			    		else if(row.nivel==4){
			    			response.data.entidades[i].subgrupo = response.data.entidades[i].codigo;
			    			response.data.entidades[i].grupo = row.grupo;
			    			response.data.entidades[i].programa = row.programa;
			    			response.data.entidades[i].entidad = row.entidad;
			    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.entidades[i].parent = row.grupo;
			    			response.data.entidades[i].nombre = (response.data.entidades[i].codigo+'').padStart(2,'0') + ' - ' + response.data.entidades[i].nombre;
			    		}
			    		else if(row.nivel==5){
			    			response.data.entidades[i].subgrupo = row.subgrupo;
			    			response.data.entidades[i].grupo = row.grupo;
			    			response.data.entidades[i].programa = row.programa;
			    			response.data.entidades[i].entidad = row.entidad;
			    			response.data.entidades[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.entidades[i].parent = row.grupo;
			    			response.data.entidades[i].nombre = (response.data.entidades[i].codigo+'').padStart(2,'0') + ' - ' + response.data.entidades[i].nombre;
			    		}
			    		response.data.entidades[i].nivel = row.nivel+1;
			    		me.data_chart.push({ 
							parent: 0,
							id: response.data.entidades[i].nombre,
							nivel: response.data.entidades[i].nivel,
							entidad: response.data.entidades[i].entidad,
							unidad_ejecutora: response.data.entidades[i].unidad_ejecutora,
							programa: response.data.entidades[i].programa,
							grupo: response.data.entidades[i].grupo,
							subgrupo: response.data.entidades[i].subgrupo,
							ejecutado_dos_antes: response.data.entidades[i].ejecutado_dos_antes,
							aprobado_anterior_mas_ampliaciones: response.data.entidades[i].aprobado_anterior_mas_ampliaciones,
							recomendado: response.data.entidades[i].recomendado
						});
			    	}
			    	var div=document.getElementById("chart_div");
			    	div.removeChild(div.childNodes[0]);
			    	me.drawChart(me.data_chart);
			    }
			    me.showChartLoading=false;
			});
			me.writeChartTitle();
		}
	}
	
	me.returnByChartTitle=function(){
		if(me.chartPath.length>0){
			var last=me.chartPath[me.chartPath.length-1];
			me.clickChart(last, 'backward');
		}
	}
	
	me.writeChartTitle=function(){
		var title = "";
		for(var i=0; i<me.chartPath.length; i++){
			title += " / " + me.chartPath[i].nombre;
		}
		me.chartTitle= "Administración Central"+(title.length> 0  ?  title : "");
	}
}
]);