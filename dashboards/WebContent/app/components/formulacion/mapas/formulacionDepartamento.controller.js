var modGastoGeneral = angular.module('maparecomendadoDepartamentoController', [ 'dashboards', 'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngMap']);

modGastoGeneral.controller('maparecomendadoDepartamentoController', ['$uibModal', '$http','NgMap','$location','$rootScope' , maparecomendadoDepartamentoController]);
 
function maparecomendadoDepartamentoController($uibModal, $http,NgMap,$location,$rootScope){
	var me = this;

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
			NgMap.getMap({id: 'departamentos'}).then(function(map){
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
		      from: '1gQ_6l10l2RV8Ps_u8G9JWU_K42-Y9al-9jqx1EVl'
		    },
		    styles:[{
	    		where: 'departamento IN ('+me.grupos['v'].join(',')+')',
		        polygonOptions: {
		          fillColor: '#47d147',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'departamento IN ('+ me.grupos['va'].join(',') +')',
		        polygonOptions: {
		          fillColor: '#5cd65c',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'departamento IN ('+ (me.grupos['a'].length>0 ? me.grupos['a'].join(',') : '0') +')',
		        polygonOptions: {
		          fillColor: '#85e085',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'departamento IN ('+ (me.grupos['ar'].length>0 ? me.grupos['ar'].join(',') : '0') +')',
		        polygonOptions: {
		          fillColor: '#adebad',
		          fillOpacity: 0.75
		        }
	        },
	        {
	    		where: 'departamento IN ('+ (me.grupos['r'].length>0 ? me.grupos['r'].join(',') : '0') +')',
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
	
	function obtenerMunicipio(codigo){
		$rootScope.$apply(function() {
			$location.path('/formulacion/mapamunicipio/' + Number(codigo) );
	    });
	}

	function obtenerGastoDepartamento(data, status, headers, config) {
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
};



//function maparecomendadoDepartamentoController($uibModal, $http,NgMap,$location) {
//	
//}

//Control modal Info
modGastoGeneral.controller('modalInfoGastoGeneralController',
		modalInfoGastoGeneralController);
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