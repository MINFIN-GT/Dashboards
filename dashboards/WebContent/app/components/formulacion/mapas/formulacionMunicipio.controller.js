var modGastoGeneral = angular.module('maparecomendadoMunicipioController', [ 'dashboards',
		'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngMap']);

// Control principal
modGastoGeneral.controller('maparecomendadoMunicipioController',
		maparecomendadoController);

function maparecomendadoController($uibModal, $http,NgMap,$routeParams) {
	var me = this;
	
	me.departamentoid = $routeParams.codigo;
	me.centros=['14.629663, -90.443145', '14.908733, -90.034269', '14.571474, -90.722444', '14.677114, -90.922238', '14.150978, -90.987703',
		'14.166351, -90.295015', '14.724621, -91.241382', '15.022623, -91.385171', '14.781806, -91.703437', '14.439285, -91.380534', 
		'14.458797, -91.778005', '14.978071, -91.934749', '15.687077, -91.548037', '15.488247, -90.967115', '15.068791, -90.445816',
		'15.619779, -90.096447', '16.904906, -90.049813', '15.565821, -89.000093', '15.037612, -89.451070', '14.691688, -89.412665',
		'14.617443, -89.911675', '14.213508, -89.852187'];
	me.showloading = false;
	me.ejercicio = moment().year()+1;

	me.map = null;

	me.lastupdate = "";

	me.departamentos = [];
	me.departamentos_loaded=false;
	
	me.grupos = [];
	me.grupos["v"] = [];
	me.grupos["va"] = [];
	me.grupos["a"] = [];
	me.grupos["ar"] = [];
	me.grupos["r"] = [];
	
	me.map_options={
			map: null,
			center: [Number(me.centros[me.departamentoid-1].split(',')[0]), Number(me.centros[me.departamentoid-1].split(',')[1])],
			zoom: 9,
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

		if (porcentaje >= 0 && porcentaje < 1) {
			return "r";
		} else if (porcentaje >= 1 && porcentaje < 1.5) {
			return "ar";
		} else if (porcentaje >= 1.5 && porcentaje < 2) {
			return "a";
		} else if (porcentaje >= 2 && porcentaje < 2.5) {
			return "va";
		} else if (porcentaje >= 2.5) {
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
			me.departamentos = response.data.departamentos;
			dibujarMapa();
		}
	}

	function dibujarMapa() {
		me.departamentos_loaded=false;
		me.grupos["v"] = [];
		me.grupos["va"] = [];
		me.grupos["a"] = [];
		me.grupos["ar"] = [];
		me.grupos["r"] = [];
		var recomendado_total = 0.0;
		
		for(var i=0; i<me.departamentos.length;i++){
			recomendado_total += me.departamentos[i].recomendado;
		}
		
		for (var j = 0; j < me.departamentos.length; j++) {
			if(me.departamentos[j].codigo != 0){
				var porcentaje = 0;
				porcentaje = me.departamentos[j].recomendado / recomendado_total * 100;
				grupo = getGrupo(porcentaje);
				me.grupos[grupo].push(me.departamentos[j].codigo)
			}
		}
		
		if(me.map_options.map==null){
			NgMap.getMap({ id: 'municipios' }).then(function(map){
	    		me.map_options.map = map;
	    		dibujarLayerDepartamentos();
	    	});
		}
		else{
			dibujarLayerDepartamentos();
		}
		
		
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
			obtenerMunicipio(e.row.departamento.value);
	 	});
		me.map_options.fusion_layer = layer;
		
		me.showloading = false;
		
		me.departamentos_loaded=true;
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
	
	me.cargarGastos();
}