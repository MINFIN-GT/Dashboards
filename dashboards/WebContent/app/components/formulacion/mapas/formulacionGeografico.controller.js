var modGastoGeneral = angular.module('maparecomendadoModule', [ 'dashboards',
		'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngMap']);

// Control principal
modGastoGeneral.controller('maparecomendadoController',
		maparecomendadoController);

// Control modal Info
modGastoGeneral.controller('modalInfoGastoGeneralController',
		modalInfoGastoGeneralController);

function maparecomendadoController($uibModal, $http,NgMap) {
	var me = this;

	me.showloading = false;
	me.ejercicio = moment().year()+1;

	me.map = null;

	me.lastupdate = "";

	me.geograficos = [];
	me.geograficos_loaded=false;
	
	me.grupos = [];
	me.grupos["v"] = [];
	me.grupos["va"] = [];
	me.grupos["a"] = [];
	me.grupos["ar"] = [];
	me.grupos["r"] = [];
	
	me.map_options={
			map: null,
			center: [15.605009229644448, -89.8793818359375],
			zoom: 7,
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

		if (porcentaje >= 0 && porcentaje < 0.1) {
			return "r";
		} else if (porcentaje >= 0.1 && porcentaje < 0.3) {
			return "ar";
		} else if (porcentaje >= 0.3 && porcentaje < 0.5) {
			return "a";
		} else if (porcentaje >= 0.5 && porcentaje < 1) {
			return "va";
		} else if (porcentaje >= 1) {
			return "v";
		} else {
			return "d";
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
			action : "getGeograficos",
			ejercicio : me.ejercicio,
			t: (new Date()).getTime() 
		};

		$http.post('/SGeografico', data).then(obtenerGasto, errorCallback);

		me.loadAttempted = true;

	};

	function lastUpdate(response) {
		if (response.data.success) {
			me.lastupdate = response.data.lastupdate;
		}
	}

	function obtenerGasto(response) {
		if (response.data.success) {
			me.geograficos = response.data.geograficos;
			dibujarMapa();
		}
	}

	function dibujarMapa() {
		me.geograficos_loaded=false;
		me.grupos["v"] = [];
		me.grupos["va"] = [];
		me.grupos["a"] = [];
		me.grupos["ar"] = [];
		me.grupos["r"] = [];
		var recomendado_total = me.geograficos[0];
		
		for (var j = 1; j < me.geograficos.length; j++) {

				var porcentaje = 0;
				porcentaje = me.geograficos[j].recomendado / recomendado_total.recomendado * 100;
				grupo = getGrupo(porcentaje);
				me.grupos[grupo].push(me.geograficos[j].geografico)
		}
		
		if(me.map_options.map==null){
			NgMap.getMap().then(function(map){
	    		me.map_options.map = map;
	    		dibujarLayerMunicipios();
	    	});
		}
		else{
			dibujarLayerMunicipios();
		}
		
		
	}
	
	function dibujarLayerMunicipios(){
		if(me.map_options.fusion_layer!=null){
			me.map_options.fusion_layer.setMap(null);
			me.map_options.fusion_layer=null;
		}
		
		var layer = new google.maps.FusionTablesLayer({
			query: {
		      select: 'geometry',
		      from: '1N_9GQ5_zJYwZGmzW2UMDfKAjGc6F4QVct1E7qgny'
		    },
		    styles:[{
	    		where: 'Codigo IN ('+me.grupos['v'].join(',')+')',
		        polygonOptions: {
		          fillColor: '#008000',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ me.grupos['va'].join(',') +')',
		        polygonOptions: {
		          fillColor: '#98fb98',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ me.grupos['a'].join(',') +')',
		        polygonOptions: {
		          fillColor: '#ffff00',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ me.grupos['ar'].join(',') +')',
		        polygonOptions: {
		          fillColor: '#ffdab9',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'Codigo IN ('+ me.grupos['r'].join(',') +')',
		        polygonOptions: {
		          fillColor: '#ff0000',
		          fillOpacity: 0.75
		        }
	        }
		    	
		    ],
		    suppressInfoWindows: true
		  });
		layer.setMap(me.map_options.map);
		/*google.maps.event.addListener(layer, 'click', function(e){
			var agasto = obtenerGeografico(e.row.Codigo.value);
			var data = {
					action : "gastomunicipio",
					mes : me.mes,
					ejercicio : me.ejercicio,
					grupos : me.getGrupos(),
					fuentes : me.getFuentes(),
					geografico : e.row.Codigo.value,
					nivel : 1,
					gasto : JSON.stringify(agasto)
				};
				$http.post('/SGastoGeneral',data).success(obtenerGastoMunicipio);
		 });*/
		me.map_options.fusion_layer = layer;
		
		me.showloading = false;
		
		me.geograficos_loaded=true;
	}
	
	function obtenerGeografico(codigo){
		for(var i=0; i<me.geograficos.length;i++){
			if(me.geograficos[i].geografico==codigo)
				return me.geograficos[i];
		}
	}

	function obtenerGastoMunicipio(data, status, headers, config) {
		$uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'infoGastoGeneral.jsp',
			controller : 'modalInfoGastoGeneralController',
			controllerAs : 'infoCtrl',
			backdrop : 'static',
			resolve : {
				param : config.data,
				ejercicio: me.ejercicio,
				info : JSON.parse(config.data.gasto),
				gasto : data
			}
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
	
	me.cargarGastos();
}

function modalInfoGastoGeneralController($uibModalInstance, $http, param,ejercicio,
		info, gasto) {
	var me = this;

	me.mes = param.mes;
	me.fuentes = param.fuentes;
	me.grupos = param.grupos;

	me.info = info;
	me.gasto = gasto.gasto;
	me.ejercicio = ejercicio;

	me.nivel = 1;

	// nivel por omision para cargar todas las entidades
	me.niveles = [ 0 ];

	me.getGasto = function(codigo) {

		me.niveles.push(codigo);

		me.nivel = me.niveles.length;

		loadGastos();
	};

	function loadGastos() {
		var newData = {
			action : "gastomunicipio",
			mes : me.mes,
			ejercicio : me.ejercicio,
			fuentes : me.fuentes,
			grupos : me.grupos,
			geografico : me.info.geografico,
			nivel : me.nivel,
			entidad : 0,
			unidad_ejecutora : 0,
			programa : 0,
			subprograma : 0,
			proyecto : 0
		};

		for (var i = 0; i < me.niveles.length; i++) {
			switch (i) {
			case 0:
				break;
			case 1:
				newData.entidad = me.niveles[i];
				break;
			case 2:
				newData.unidad_ejecutora = me.niveles[i];
				break;
			case 3:
				newData.programa = me.niveles[i];
				break;
			case 4:
				newData.subprograma = me.niveles[i];
				break;
			case 5:
				newData.proyecto = me.niveles[i];
				break;
			default:
				break;
			}
		}

		$http.post('/SGastoGeneral', newData).success(obtenerGastoMunicipio);
	}

	function obtenerGastoMunicipio(data, status, headers, config) {
		me.gasto = data.gasto;
	}

	me.back = function() {
		me.niveles.pop();
		me.nivel = me.niveles.length;

		loadGastos();
	}

	me.ok = function() {
		$uibModalInstance.close('ok');
	};

	me.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}