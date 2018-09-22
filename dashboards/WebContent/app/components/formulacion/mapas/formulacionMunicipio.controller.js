var modGastoGeneral = angular.module('maparecomendadoMunicipioController', [ 'dashboards',
		'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngMap']);

// Control principal
modGastoGeneral.controller('maparecomendadoMunicipioController',
		maparecomendadoController);

function maparecomendadoController($uibModal, $http,NgMap,$routeParams, $filter,$location,$rootScope) {
	var me = this;
	
	me.departamentoid = Number($routeParams.codigo);
	me.centros=[
		'14.571215, -90.488758, 8, 0, 0.2, 0.4, 0.6, 0.8', '14.873280, -90.067862, 8, 0, 3.0, 6.0, 9.0, 12.0', '14.549064, -90.740827, 8, 0, 1.5, 3.0, 4.5, 6.0', '14.677114, -90.922238, 8, 0, 2.0, 4.0, 6.0, 8.0', '14.151063, -91.049860, 8, 0, 2.0, 4.0, 6.0, 8.0',
		'14.161141, -90.310771, 8, 0, 2.5, 5.0, 7.5, 10.0', '14.728824, -91.258666, 8, 0, 2.5, 5.0, 7.5, 10.0', '15.022623, -91.385171, 8, 0, 3.0, 6.0, 9.0, 12.0', '14.781806, -91.703437, 8, 0, 1.0, 2.0, 3.0, 4.0', '14.374640, -91.439802, 8, 0, 1.6, 3.2, 4.8, 6.4', 
		'14.477956, -91.845054, 8, 0, 2.5, 5.0, 7.5, 10.0', '14.978071, -91.934749, 8, 0, 1.5, 3.0, 4.5, 6.0', '15.687077, -91.548037, 8, 0, 1.5, 3.0, 4.5, 6.0', '15.424314, -90.968691, 8, 0, 1.8, 3.6, 5.4, 7.2', '15.084385, -90.330797, 8, 0, 3.5, 7.0, 10.5, 14.0',
		'15.619779, -90.096447, 8, 0, 1.5, 3.0, 4.5, 6.0', '16.904906, -90.049813, 8, 0, 1.8, 3.6, 5.4, 7.2', '15.589733, -88.919914, 8, 9.0, 9.4, 10.2, 10.6, 11.0', '14.998361, -89.519731, 8, 0, 2.2, 4.4, 6.6, 8.8', '14.691688, -89.412665, 8, 0, 2.5, 5.0, 7.5, 10.0',
		'14.615048, -89.960796, 8, 0, 2.5, 5.0, 7.5, 10.0', '14.213508, -89.852187, 8, 0, 1.5, 3.0, 4.5, 6.0'];
	
	me.showloading = false;
	me.ejercicio = moment().year()+1;
	me.viewMillones = true;
	me.map = null;

	me.lastupdate = "";

	me.municipios = [];
	me.municipios_loaded=false;
	me.data_chart = [];
	
	me.chart=null;
	me.chartPath=[];
	me.chartTitle="Municipios";
	
	me.showChartLoading=false;
	
	me.grupos = [];
	me.grupos["v"] = [];
	me.grupos["va"] = [];
	me.grupos["a"] = [];
	me.grupos["ar"] = [];
	me.grupos["r"] = [];
	
	me.total_recomendado=0.0;
	
	me.map_options={
			map: null,
			center: [Number(me.centros[me.departamentoid-1].split(',')[0]), Number(me.centros[me.departamentoid-1].split(',')[1])],
			zoom: Number(me.centros[me.departamentoid-1].split(',')[2]),
			options: {
				   streetViewControl: false,
				   scrollwheel: false,
				   mapTypeId: google.maps.MapTypeId.ROADMAP
			   },
			styles: [
				   {
					    "elementType": "labels",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.land_parcel",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.locality",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.neighborhood",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "administrative.province",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "road.arterial",
					    "elementType": "labels",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "road.highway",
					    "elementType": "labels",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  },
					  {
					    "featureType": "road.local",
					    "stylers": [
					      {
					        "visibility": "off"
					      }
					    ]
					  }
					]
	};
	
	me.anios = [];
	
	function getGrupo(porcentaje) {

		if (porcentaje >= Number(me.centros[me.departamentoid-1].split(',')[3]) && porcentaje < Number(me.centros[me.departamentoid-1].split(',')[4])) {
			return "r";
		} else if (porcentaje >= Number(me.centros[me.departamentoid-1].split(',')[4]) && porcentaje < Number(me.centros[me.departamentoid-1].split(',')[5])) {
			return "ar";
		} else if (porcentaje >= Number(me.centros[me.departamentoid-1].split(',')[5]) && porcentaje < Number(me.centros[me.departamentoid-1].split(',')[6])) {
			return "a";
		} else if (porcentaje >= Number(me.centros[me.departamentoid-1].split(',')[6]) && porcentaje < Number(me.centros[me.departamentoid-1].split(',')[7])) {
			return "va";
		} else if (porcentaje >= Number(me.centros[me.departamentoid-1].split(',')[7])) {
			return "v";
		} else {
			return "r";
		}
	}

	function errorCallback(response) {

	}
	
	me.cargarGastos = function() {
		me.panel_fuentes = false;
		me.panel_grupos = false;

		me.showloading = true;
		me.loadAttempted = false;

		var data = {
			action : "getDepartamento",
			ejercicio : me.ejercicio,
			departamento: me.departamentoid,
			t: (new Date()).getTime() 
		};

		$http.post('/SInstitucional', data).then(obtenerGasto, errorCallback);

		me.loadAttempted = true;

	};

	function lastUpdate(response) {
		if (response.data.success) {
			me.lastupdate = response.data.lastupdate;
		}
	}

	function obtenerGasto(response) {
		if (response.data.success) {
			me.municipios = response.data.departamentos;
			for(var i=0; i<me.municipios.length; i++){
	    		me.municipios[i].showChildren=false;
	    		me.municipios[i].show=true;
	    		me.municipios[i].parent=null; 
	    	}
			dibujarMapa();
	    	me.drawFirstChart();
		}
	}

	function dibujarMapa() {
		me.municipios_loaded=false;
		me.grupos["v"] = [];
		me.grupos["va"] = [];
		me.grupos["a"] = [];
		me.grupos["ar"] = [];
		me.grupos["r"] = [];
		var recomendado_total = 0.0;
		
		for(var i=0; i< me.municipios.length; i++){
			me.total_recomendado += me.municipios[i].recomendado;
		}
		
		for (var j = 0; j < me.municipios.length; j++) {
			if(me.municipios[j].codigo != 0){
				var porcentaje = 0;
				porcentaje = me.municipios[j].recomendado / me.total_recomendado * 100;
				grupo = getGrupo(porcentaje);
				me.grupos[grupo].push(me.municipios[j].codigo)
			}
		}
		
		NgMap.getMap({id: 'Municipios'}).then(function(map){
    		me.map_options.map = map;
    		dibujarLayerDepartamentos();
    	});
	}
	
	function dibujarLayerDepartamentos(){
		if(me.map_options.fusion_layer!=null){
			me.map_options.fusion_layer.setMap(null);
			me.map_options.fusion_layer=null;
		}
		
		var layer = new google.maps.FusionTablesLayer({
			query: {
		      select: 'geometry',
		      from: '1N_9GQ5_zJYwZGmzW2UMDfKAjGc6F4QVct1E7qgny',
		      where: 'Cod_Dep IN ('+me.departamentoid+')',		      
		    },
		    styles:[{
	    		where: 'Codigo IN ('+me.grupos['v'].join(',')+')',
		        polygonOptions: {
		          fillColor: '#47d147',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ me.grupos['va'].join(',') +')',
		        polygonOptions: {
		          fillColor: '#5cd65c',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ (me.grupos['a'].length>0 ? me.grupos['a'].join(',') : '0') +')',
		        polygonOptions: {
		          fillColor: '#85e085',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ (me.grupos['ar'].length>0 ? me.grupos['ar'].join(',') : '0') +')',
		        polygonOptions: {
		          fillColor: '#adebad',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ (me.grupos['r'].length>0 ? me.grupos['r'].join(',') : '0') +')',
		        polygonOptions: {
		          fillColor: '#d6f5d6',
		          fillOpacity: 0.75
		        }
	        }
		    	
		    ],
		    suppressInfoWindows: true
		  });
		layer.setMap(me.map_options.map);
		google.maps.event.addListener(layer, 'click', function(e){
			obtenerGeografico(e.row.Codigo.value);
	 	});
		me.map_options.fusion_layer = layer;
		
		me.showloading = false;
		
		me.municipios_loaded=true;
	}
	
	function obtenerGeografico(codigo){
		$rootScope.$apply(function() {
			$location.path('/formulacion/mapageografico/' + Number(codigo) );
	    });
	}
	
	me.mapLoaded=function(map){
		//Belice
		var belice = new google.maps.FusionTablesLayer({
			map: map,
		    heatmap: { enabled: false },
		    query: {
		      select: 'geometry',
		      from: '1soQUQhG0lzrro_eRnAnWx9s3LjhIxUJyR7-cnebE'
		    },
		      options: {
		          styleId: 2,
		          templateId: 2
		        },
		        suppressInfoWindows: true
		  });
	}
	
	me.filtroMillones=function(value, transform){
		var millones = value;
		var fixed=2;
		if(transform){
			millones = value/1000000;
			fixed=1;
		}
		return (value>0) ?  $filter('currency')(millones.toFixed(fixed), '', fixed) : ( value<0 ? '(' + $filter('currency')(millones.toFixed(fixed), '', fixed).substring(1) + ')' : null)  ;
	}
	
	
	me.cargarTabla = function(){
		for(var i=0; i<me.municipios.length; i++){
    		me.municipios[i].showChildren=false;
    		me.municipios[i].show=true;
    		me.municipios[i].parent=null; 
    		me.total_recomendado += me.municipios[i].recomendado;
    	}
    	me.drawFirstChart();
    	me.showloading=false;
	}
	
//	$http.post('/SInstitucional',  { action: 'getDepartamento', departamento: me.departamentoid, ejercicio: me.ejercicio, t: (new Date()).getTime()   }).then(function(response){
//	    if(response.data.success){
//	    	me.municipios=response.data.departamentos;
//	    	for(var i=0; i<me.municipios.length; i++){
//	    		me.municipios[i].showChildren=false;
//	    		me.municipios[i].show=true;
//	    		me.municipios[i].parent=null; 
//	    		me.total_recomendado += me.municipios[i].recomendado;
//	    	}
//	    	me.drawFirstChart();
//	    }
//	    me.showloading=false;
//	});
	
	$http.post('/SInstitucional',  { action: 'getNombreDepartamento', departamento: me.departamentoid, t: (new Date()).getTime()   }).then(function(response){
	    if(response.data.success){
	    	me.nombreDepartamento = response.data.nombreDepartamento;
	    }
	});
	
	me.clickRow=function(row,index){
		if(row.showChildren==false && row.nivel<8){ //Mostrar hijos
			row.showChildren=true;
			var indexClick = index;
			if((index+1)<me.municipios.length && me.municipios[index+1].nivel>row.nivel){
				while((index+1)<me.municipios.length && me.municipios[index+1].nivel>row.nivel){
	    			if(me.municipios[index+1].nivel==row.nivel+1)
	    				me.municipios[index+1].show=true;
	    			index++;	    			
	    		}
				me.data_chart.pop();
				me.clickChart({ 
					id: row.nombre,
	    			departamento: me.departamentoid, 
	    			municipio: (row.nivel==2) ? row.codigo : row.municipio , 
	    			entidad: (row.nivel==3) ? row.codigo : row.entidad, 
					unidad_ejecutora: (row.nivel==4) ? row.codigo : row.unidad_ejecutora,
	    			programa: (row.nivel==5) ? row.codigo : row.programa,
	    			grupo: (row.nivel==6) ? row.codigo : row.grupo,
	    			subgrupo: (row.nivel==7) ? row.codigo : row.subgrupo,
	    			nivel: me.municipios[indexClick].nivel}, "forward");
	    	}
			else{
				row.showloading=true;
				$http.post('/SInstitucional',  { action: 'getDepartamento', ejercicio: me.ejercicio, 
					departamento: me.departamentoid,
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
				    	
				    	var div=document.getElementById("chart_div");
				    	if(div.childNodes[0] != null)
				    		div.removeChild(div.childNodes[0]);
				    	me.municipios.splice.apply(me.municipios,[index+1,0].concat(datos.departamentos));
				    	me.clickChart({ 
				    		id: row.nombre,
				    		departamento: me.departamentoid,
			    			municipio: (row.nivel==2) ? row.codigo : row.municipio , 
			    			entidad: (row.nivel==3) ? row.codigo : row.entidad, 
	    					unidad_ejecutora: (row.nivel==4) ? row.codigo : row.unidad_ejecutora,
			    			programa: (row.nivel==5) ? row.codigo : row.programa,
			    			grupo: (row.nivel==6) ? row.codigo : row.grupo,
			    			subgrupo: (row.nivel==7) ? row.codigo : row.subgrupo,
			    			nivel: me.municipios[index].nivel}, "forward");
				    }
				    row.showloading=false;
				});
			}
		}
		else{ //Borrar hijos
			row.showChildren=false;
			var pos=index+1;
			while(pos<me.municipios.length && me.municipios[pos].nivel>row.nivel){
				me.municipios[pos].show=false;
				me.municipios[pos].showChildren=false;
				pos++;
			}
		}
	}
	
	me.drawFirstChart=function(){
		me.data_chart=[];
		for(var i=0; i<me.municipios.length; i++){
			me.data_chart.push({ 
				parent: 0,
				id: me.municipios[i].nombre,
				nivel: 2,
				municipio: me.municipios[i].codigo,
				recomendado: me.municipios[i].recomendado
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
			      table += "<tr><td>Recomendado "+me.ejercicio+":</td><td class='data'>"+ (me.viewMillones ? 'Q ' : '')  + me.filtroMillones(d.recomendado, me.viewMillones) + "</td></tr>";
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
		if((direction=='forward' && row.nivel<8) || (direction=='backward' && row.nivel>2)){
			me.showChartLoading=true;
			if(direction=='backward'){
				me.chartPath.pop();
				row.nivel = row.nivel-2;				
			}
			else{
				while(row.nivel-2 < me.chartPath.length){
					me.chartPath.pop();
				}
				
				me.chartPath.push({nombre: row.id, departamento: me.departamentoid, municipio: row.municipio, entidad: row.entidad, unidad_ejecutora: row.unidad_ejecutora, programa: row.programa, 
					grupo: row.grupo, subgrupo: row.subgrupo, nivel: row.nivel+1});
			}
			$http.post('/SInstitucional',  { action: 'getDepartamento', ejercicio: me.ejercicio, 
				departamento: me.departamentoid,
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
			    	if(div.childNodes[0] != null)
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
		me.chartTitle= "Municipios"+(title.length> 0  ?  title : "");
	}
	
	me.cargarGastos();
	me.cargarTabla();
}