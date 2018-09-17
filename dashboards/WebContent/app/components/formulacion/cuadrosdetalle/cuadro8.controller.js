
angular.module('cuadro8detalleController',['dashboards','ui.bootstrap.contextMenu']).controller('cuadro8detalleController',['$scope','$routeParams','$http','$interval',
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
		var millones = value;
		var fixed=2;
		if(transform){
			millones = value/1000000;
			fixed=1;
		}
		return (value>0) ?  $filter('currency')(millones.toFixed(fixed), '', fixed) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(fixed), '', fixed).substring(1) + ')' : null)  ;
	}
	
	
		$http.post('/SInstitucional',  { action: 'getInstitucionalFinalidadDetalle', ejercicio: me.anio, t: (new Date()).getTime()   }).then(function(response){
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
		    	for(var i=0; i<me.entidades_finalidades.length;i++){
		    		me.entidades_finalidades[i].showChildren=false;
		    		me.entidades_finalidades[i].show=true;
		    		me.entidades_finalidades[i].parent=null; 
		    	}
		    	me.drawFirstChart(me.entidades_finalidades);
		    }
		    me.showloading=false;
		});
		
		me.clickRow=function(row,index){
			if(row.showChildren==false && row.nivel<6){ //Mostrar hijos
				row.showChildren=true;
				if(((index+1) < me.entidades_finalidades.length) && me.entidades_finalidades[index+1].nivel>row.nivel){
		    		while((index+1) < me.entidades_finalidades.length && me.entidades_finalidades[index+1].nivel>row.nivel){
		    			if(me.entidades_finalidades[index+1].nivel==row.nivel+1)
		    				me.entidades_finalidades[index+1].show=true;
		    			index++;
		    		}
		    	}
				else{
					row.showloading=true;
					$http.post('/SInstitucional',  { action: 'getInstitucionalFinalidadDetalle', ejercicio: me.anio, 
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
					    	me.entidades_tipo_gasto.splice.apply(me.entidades_finalidades,[index+1,0].concat(response.data.entidades));
					    }
					    row.showloading=false;
					});
				}
			}
			else{ //Borrar hijos
				row.showChildren=false;
				var pos=index+1;
				while((pos<me.entidades_finalidades.length) && me.entidades_finalidades[pos].nivel>row.nivel){
					me.entidades_finalidades[pos].show=false;
					me.entidades_finalidades[pos].showChildren=false;
					pos++;
				}
			}
		}
		
		me.drawFirstChart=function(data){
			me.chartData=[];
			for(var i=0; i<me.finalidades_codigos.length; i++){
				for(var j=data.length-1; j>=0; j--){
					me.chartData.push({
						id: me.finalidades[i],
						x:data[j].nombre,
						y: (data[j]['f'+me.finalidades_codigos[i]+'_monto']!=null) ? (data[j]['f'+me.finalidades_codigos[i]+'_monto']/1000000) : 0
					});
				}
			}
			me.chart=new d3plus.BarChart()
			  .data(me.chartData)
			  .stacked(true)
			  .select("#chart_div")
			  .x("y")
			  .y("x")
			  .discrete("y")
			  .xConfig({
			    title: "Miles de Millones de Quetzales"
			  })
			  .yConfig({
			    gridConfig:  {stroke: "#fff"}
			  }) 
			  .tooltipConfig({
				    body: function(d) {
				    	if(!Array.isArray(d.x)){
					      var table = "<table class='tooltip-table'>";
					      table += "<tr><td>Recomendado "+(me.anio)+":</td><td class='data'>"+ (me.viewMillones ? 'Q ' : '') + me.filtroMillones(d.y*1000000, true) + "</td></tr>";
					      table += "</table>";
					       return table;
				    	}
				    	else
				    		return d.id;
				    },
				    footer: function(d) {
				      return "<sub class='tooltip-footer'></sub>";
				    },
				    title: function(d) {
				    	if(!Array.isArray(d.x))
				    		return '<div><span class="tooltip-title">'+d.x+'</span></div><div class="tooltip-subtitle">'+d.id+'</div>';
				    }
				  })
			  .shapeConfig({
			    labelConfig: {
			      fontMax: 14
			    }
			  })
			  .render();
		}
}
]);