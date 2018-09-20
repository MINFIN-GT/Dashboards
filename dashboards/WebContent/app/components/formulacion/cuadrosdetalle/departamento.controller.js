
angular.module('departamentodetalleController',['dashboards','ui.bootstrap.contextMenu']).controller('departamentodetalleController',['$scope','$routeParams','$http','$interval',
'$location','$timeout','$filter',
function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	var me=this;
	
	me.viewMillones = true;
	me.anio = moment().year()+1;
	me.showloading=true;
	
	me.departamentos = [];
	me.data_chart = [];
	me.total_recomendado=0.0;
	
	me.chart=null;
	me.chartPath=[];
	me.chartTitle="Departamentos";
	
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
	
	
	$http.post('/SInstitucional',  { action: 'getDepartamento', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
	    if(response.data.success){
	    	me.departamentos=response.data.departamentos;
	    	for(var i=0; i<me.departamentos.length; i++){
	    		me.departamentos[i].showChildren=false;
	    		me.departamentos[i].show=true;
	    		me.departamentos[i].parent=null; 
	    		me.total_recomendado += me.departamentos[i].recomendado;
	    	}
	    	me.drawFirstChart();
	    }
	    me.showloading=false;
	});
	
	me.clickRow=function(row,index){
		if(row.showChildren==false && row.nivel<8){ //Mostrar hijos
			row.showChildren=true;
			var indexClick = index;
			if((index+1)<me.departamentos.length && me.departamentos[index+1].nivel>row.nivel){
				while((index+1)<me.departamentos.length && me.departamentos[index+1].nivel>row.nivel){
	    			if(me.departamentos[index+1].nivel==row.nivel+1)
	    				me.departamentos[index+1].show=true;
	    			index++;	    			
	    		}
	    		
				me.clickChart({ 
					id: row.nombre,
	    			departamento: (row.nivel==1) ? row.codigo : row.departamento , 
	    			municipio: (row.nivel==2) ? row.codigo : row.municipio , 
	    			entidad: (row.nivel==3) ? row.codigo : row.entidad, 
					unidad_ejecutora: (row.nivel==4) ? row.codigo : row.unidad_ejecutora,
	    			programa: (row.nivel==5) ? row.codigo : row.programa,
	    			grupo: (row.nivel==6) ? row.codigo : row.grupo,
	    			subgrupo: (row.nivel==7) ? row.codigo : row.subgrupo,
	    			nivel: me.departamentos[indexClick].nivel}, "forward");
	    	}
			else{
				row.showloading=true;
				$http.post('/SInstitucional',  { action: 'getDepartamento', ejercicio: me.anio, 
					departamento: (row.nivel==1) ? row.codigo : row.departamento,
					municipio: (row.nivel==2) ? row.codigo : row.municipio,
					entidad: (row.nivel==3) ? row.codigo : row.entidad,
					unidad_ejecutora: (row.nivel==4) ? row.codigo : row.unidad_ejecutora,
					programa: (row.nivel==5) ? row.codigo : row.programa,
					grupo: (row.nivel==6) ? row.codigo : row.grupo,
					subgrupo: (row.nivel==7) ? row.codigo : row.subgrupo,
					t: (new Date()).getTime()   }).then(function(response){
				    if(response.data.success){
				    	var datos = response.data;
				    	for(var i=0;i<datos.departamentos.length; i++){
				    		datos.departamentos[i].showChildren=false;
				    		datos.departamentos[i].show=true;
				    		if(row.nivel==1){
				    			datos.departamentos[i].departamento = row.codigo;
				    			datos.departamentos[i].parent = 'D-'+row.codigo;
				    		}
				    		if(row.nivel==2){
				    			datos.departamentos[i].departamento = row.departamento;
				    			datos.departamentos[i].municipio = row.codigo;
				    			datos.departamentos[i].parent = 'M-'+row.codigo;
				    		}
				    		if(row.nivel==3){
				    			datos.departamentos[i].departamento = row.departamento;
				    			datos.departamentos[i].municipio = row.municipio;
				    			datos.departamentos[i].entidad = row.codigo;
				    			datos.departamentos[i].parent = 'E-'+row.codigo;
				    		}
				    		if(row.nivel==4){
				    			datos.departamentos[i].departamento = row.departamento;
				    			datos.departamentos[i].municipio = row.municipio;
				    			datos.departamentos[i].entidad = row.entidad;
				    			datos.departamentos[i].unidad_ejecutora = row.codigo;
				    			datos.departamentos[i].parent = 'UE-'+row.codigo;
				    		}
				    		if(row.nivel==5){
				    			datos.departamentos[i].departamento = row.departamento;
				    			datos.departamentos[i].municipio = row.municipio;
				    			datos.departamentos[i].entidad = row.entidad;
				    			datos.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
				    			datos.departamentos[i].programa = row.codigo;
				    			datos.departamentos[i].parent = 'P-'+row.codigo;
				    		}
				    		if(row.nivel==6){
				    			datos.departamentos[i].departamento = row.departamento;
				    			datos.departamentos[i].municipio = row.municipio;
				    			datos.departamentos[i].entidad = row.entidad;
				    			datos.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
				    			datos.departamentos[i].programa = row.programa
				    			datos.departamentos[i].grupo = row.codigo;
				    			datos.departamentos[i].parent = 'GG-'+row.codigo;
				    		}
				    		if(row.nivel==7){
				    			datos.departamentos[i].departamento = row.departamento;
				    			datos.departamentos[i].municipio = row.municipio;
				    			datos.departamentos[i].entidad = row.entidad;
				    			datos.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
				    			datos.departamentos[i].programa = row.programa
				    			datos.departamentos[i].grupo = row.grupo
				    			datos.departamentos[i].subgrupo = row.codigo;
				    			datos.departamentos[i].parent = 'SG-'+row.codigo;
				    		}
				    	}

				    	me.departamentos.splice.apply(me.departamentos,[index+1,0].concat(datos.departamentos));
				    	me.clickChart({ 
				    		id: row.nombre,
			    			departamento: (row.nivel==1) ? row.codigo : row.departamento , 
			    			municipio: (row.nivel==2) ? row.codigo : row.municipio , 
			    			entidad: (row.nivel==3) ? row.codigo : row.entidad, 
	    					unidad_ejecutora: (row.nivel==4) ? row.codigo : row.unidad_ejecutora,
			    			programa: (row.nivel==5) ? row.codigo : row.programa,
			    			grupo: (row.nivel==6) ? row.codigo : row.grupo,
			    			subgrupo: (row.nivel==7) ? row.codigo : row.subgrupo,
			    			nivel: me.departamentos[index].nivel}, "forward");
				    }
				    row.showloading=false;
				});
			}
		}
		else{ //Borrar hijos
			row.showChildren=false;
			var pos=index+1;
			while(pos<me.departamentos.length && me.departamentos[pos].nivel>row.nivel){
				me.departamentos[pos].show=false;
				me.departamentos[pos].showChildren=false;
				pos++;
			}
		}
	}
	
	me.drawFirstChart=function(){
		me.data_chart=[];
		for(var i=0; i<me.departamentos.length; i++){
			me.data_chart.push({ 
				parent: 0,
				id: me.departamentos[i].nombre,
				nivel: 1,
				departamento: me.departamentos[i].codigo,
				recomendado: me.departamentos[i].recomendado
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
		if((direction=='forward' && row.nivel<8) || (direction=='backward' && row.nivel>1)){
			me.showChartLoading=true;
			if(direction=='backward'){
				me.chartPath.pop();
				row.nivel = row.nivel-2;				
			}
			else{
				while(row.nivel-1 < me.chartPath.length){
					me.chartPath.pop();
				}
				
				me.chartPath.push({nombre: row.id, departamento: row.departamento, municipio: row.municipio, entidad: row.entidad, unidad_ejecutora: row.unidad_ejecutora, programa: row.programa, 
					grupo: row.grupo, subgrupo: row.subgrupo, nivel: row.nivel+1});
			}
			$http.post('/SInstitucional',  { action: 'getDepartamento', ejercicio: me.anio, 
				departamento: (row.nivel>=1) ? row.departamento : -1,
				municipio: (row.nivel>=2) ? row.municipio : -1,
				entidad: (row.nivel>=3) ? row.entidad : -1,
				unidad_ejecutora: (row.nivel>=4) ? row.unidad_ejecutora : -1,
				programa: (row.nivel>=5) ? row.programa : -1,
				grupo: (row.nivel>=6) ? row.grupo : -1,
				subgrupo: (row.nivel>=7) ? row.subgrupo : -1,
				t: (new Date()).getTime()   }).then(function(response){
			    if(response.data.success){
			    	me.data_chart=[];
			    	for(var i=0;i<response.data.departamentos.length; i++){
			    		if(row.nivel===0){
			    			response.data.departamentos[i].parent = 0;
			    			response.data.departamentos[i].departamento = response.data.departamentos[i].codigo;
			    		}			    		
			    		else if(row.nivel==1){
			    			response.data.departamentos[i].municipio = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].parent = row.departamento;			    			
			    		}
			    		else if(row.nivel==2){
			    			response.data.departamentos[i].entidad = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].municipio = row.municipio;
			    			response.data.departamentos[i].parent = row.municipio;
			    		}
			    		else if(row.nivel==3){
			    			response.data.departamentos[i].unidad_ejecutora = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].municipio = row.municipio;
			    			response.data.departamentos[i].entidad = row.entidad;
			    			response.data.departamentos[i].parent = row.entidad;
			    			response.data.departamentos[i].nombre = response.data.departamentos[i].codigo + ' - ' + response.data.departamentos[i].nombre;
			    		}
			    		else if(row.nivel==4){
			    			response.data.departamentos[i].programa = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].municipio = row.municipio;
			    			response.data.departamentos[i].entidad = row.entidad;
			    			response.data.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.departamentos[i].parent = row.unidad_ejecutora;
			    			response.data.departamentos[i].nombre = (response.data.departamentos[i].codigo+'').padStart(2,'0') + ' - ' + response.data.departamentos[i].nombre;
			    		}
			    		else if(row.nivel==5){
			    			response.data.departamentos[i].grupo = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].municipio = row.municipio;
			    			response.data.departamentos[i].entidad = row.entidad;
			    			response.data.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.departamentos[i].programa = row.programa;
			    			response.data.departamentos[i].parent = row.programa;
			    			response.data.departamentos[i].nombre = (response.data.departamentos[i].codigo+'').padStart(2,'0') + ' - ' + response.data.departamentos[i].nombre;
			    		}
			    		else if(row.nivel==6){
			    			response.data.departamentos[i].subgrupo = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].municipio = row.municipio;
			    			response.data.departamentos[i].entidad = row.entidad;
			    			response.data.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.departamentos[i].programa = row.programa;
			    			response.data.departamentos[i].grupo = row.grupo;
			    			response.data.departamentos[i].parent = row.grupo;
			    			response.data.departamentos[i].nombre = (response.data.departamentos[i].codigo+'').padStart(2,'0') + ' - ' + response.data.departamentos[i].nombre;
			    		}
			    		else if(row.nivel==7){
			    			response.data.departamentos[i].renglon = response.data.departamentos[i].codigo;
			    			response.data.departamentos[i].departamento = row.departamento;
			    			response.data.departamentos[i].municipio = row.municipio;
			    			response.data.departamentos[i].entidad = row.entidad;
			    			response.data.departamentos[i].unidad_ejecutora = row.unidad_ejecutora;
			    			response.data.departamentos[i].programa = row.programa;
			    			response.data.departamentos[i].grupo = row.grupo;
			    			response.data.departamentos[i].subgrupo = row.subgrupo
			    			response.data.departamentos[i].parent = row.subgrupo;
			    			response.data.departamentos[i].nombre = (response.data.departamentos[i].codigo+'').padStart(2,'0') + ' - ' + response.data.departamentos[i].nombre;
			    		}
			    		response.data.departamentos[i].nivel = row.nivel+1;
			    		me.data_chart.push({ 
							parent: 0,
							id: response.data.departamentos[i].nombre,
							nivel: response.data.departamentos[i].nivel,
							departamento: response.data.departamentos[i].departamento,
							municipio: response.data.departamentos[i].municipio,
							entidad: response.data.departamentos[i].entidad,
							unidad_ejecutora: response.data.departamentos[i].unidad_ejecutora,
							programa: response.data.departamentos[i].programa,
							grupo: response.data.departamentos[i].grupo,
							subgrupo: response.data.departamentos[i].subgrupo,
							recomendado: response.data.departamentos[i].recomendado
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
		me.chartTitle= "Departamentos"+(title.length> 0  ?  title : "");
	}
}
]);