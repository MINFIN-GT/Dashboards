var modGastoGeneral = angular.module('mapsGastoGeneralModule', [ 'dashboards',
		'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngMap']);

// Control principal
modGastoGeneral.controller('mapsGastoGeneralController',
		mapsGastoGeneralController);

// Control modal Info
modGastoGeneral.controller('modalInfoGastoGeneralController',
		modalInfoGastoGeneralController);

function mapsGastoGeneralController($uibModal, $http,NgMap) {
	var me = this;

	me.showloading = false;
	me.mes = 1;
	me.nmonth = "Enero";
	me.ejercicio = moment().year();

	me.map = null;

	me.mostrarPerCapita = false;

	me.fuentes = "Fuentes de Financiamiento";
	me.fuentes_descripcion = "Todas";
	me.fuentes_array = [];
	me.panel_fuentes;
	me.fuentes_loaded = false;

	me.grupos_gasto = "Grupos de Gasto";
	me.grupos_array = [];
	me.grupos_gasto_descripcion = "Todos";
	me.grupos_loaded = false;
	me.panel_grupos;
	me.todosgrupos = 1;

	me.lastupdate = "";

	me.tributarias = [ 11, 12, 13, 14, 15, 16, 21, 22, 29 ];

	me.geograficos = [];
	me.geograficos_loaded=false;
	
	me.color = {};
	me.color["v"] = "#008000";
	me.color["va"] = "#98fb98";
	me.color["a"] = "#ffff00";
	me.color["ar"] = "#ffdab9";
	me.color["r"] = "#ff0000";
	me.color["d"] = "#bdbdbd";
	
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

	me.mesClick = function(mes) {
		me.mes = mes;

		me.cargarGastos();

		switch (mes) {
			case 1: me.nmonth = "Enero"; break;
			case 2: me.nmonth = "Febrero"; break;
			case 3: me.nmonth = "Marzo"; break;
			case 4: me.nmonth = "Abril"; break;
			case 5: me.nmonth = "Mayo"; break;
			case 6: me.nmonth = "Junio"; break;
			case 7: me.nmonth = "Julio"; break;
			case 8: me.nmonth = "Agosto"; break;
			case 9: me.nmonth = "Septiembre"; break;
			case 10: me.nmonth = "Octubre"; break;
			case 11: me.nmonth = "Noviembre"; break;
			case 12: me.nmonth = "Diciembre"; break;
		}

	};
	
	me.anoClick =  function(n_ano){
		me.ejercicio = n_ano;
		me.cargarGastos();
	}

	function errorCallback(response) {

	}

	$http.post('/SFuente', {
		ejercicio : me.ejercicio,
		t : (new Date()).getTime()
	}).then(loadFuentes, errorCallback);

	me.checkAll = function(check) {
		for ( var fuente in me.fuentes_array)
			me.fuentes_array[fuente].checked = check;
		me.fuentes_descripcion = (check) ? "Todas" : "Ninguna";
	};

	me.checkTributarias = function() {
		for ( var fuente in me.fuentes_array) {
			me.fuentes_array[fuente].checked = me.tributarias
					.indexOf(me.fuentes_array[fuente].fuente) > -1;
		}
		me.fuentes_descripcion = "Tributarias";
	};

	me.changeFuentes = function() {
		var cont = 0;
		var ntributarias = 0;

		for ( var fuente in me.fuentes_array) {
			cont = me.fuentes_array[fuente].checked ? cont + 1 : cont;
			ntributarias = (me.fuentes_array[fuente].checked && me.tributarias
					.indexOf(me.fuentes_array[fuente].fuente) > -1) ? ntributarias + 1
					: ntributarias;
		}

		if (cont == me.fuentes_array.length)
			me.fuentes_descripcion = "Todas";
		else if (cont == 0)
			thmeis.fuentes_descripcion = "Ninguna";
		else if (cont == ntributarias && ntributarias == me.tributarias.length) {
			me.fuentes_descripcion = "Tributarias";
		} else {
			var tfuentes = '';
			for ( var fuente in me.fuentes_array) {
				if (me.fuentes_array[fuente].checked)
					tfuentes = tfuentes + ',' + me.fuentes_array[fuente].fuente;
			}
			me.fuentes_descripcion = tfuentes.substring(1);
		}
	};

	me.changeGrupos = function() {
		var cont = 0;
		for ( var grupo in me.grupos_array) {
			cont = me.grupos_array[grupo].checked ? cont + 1 : cont;
		}
		if (cont == me.grupos_array.length) {
			me.grupos_descripcion = "Todos";
			me.todosgrupos = 1;
		} else if (cont == 0) {
			me.grupos_descripcion = "Ninguno";
			me.todosgrupos = 0;
		} else {
			var tgrupos = '';
			for ( var grupo in me.grupos_array) {
				if (me.grupos_array[grupo].checked)
					tgrupos = tgrupos + ',' + me.grupos_array[grupo].grupo;
			}
			me.grupos_descripcion = tgrupos.substring(1);
			me.todosgrupos = 0;
		}
	};

	me.checkGruposAll = function(check) {
		for ( var fuente in me.grupos_array)
			me.grupos_array[fuente].checked = check;
		me.grupos_descripcion = (check) ? "Todos" : "Ninguno";
	};

	me.getFuentes = function() {
		var fuentes = '';
		for ( var fuente in me.fuentes_array)
			fuentes += (me.fuentes_array[fuente].checked) ? ','
					+ me.fuentes_array[fuente].fuente : '';
		fuentes = (fuentes.length > 0) ? fuentes.substr(1) : '';
		return fuentes;
	};

	me.getGrupos = function() {
		var grupos = '';
		for ( var grupo in me.grupos_array)
			grupos += (me.grupos_array[grupo].checked) ? ','
					+ me.grupos_array[grupo].grupo : '';
		grupos = (grupos.length > 0) ? grupos.substr(1) : '';
		return grupos;
	};

	me.cargarGastos = function() {
		me.panel_fuentes = false;
		me.panel_grupos = false;

		me.showloading = true;
		me.loadAttempted = false;

		var data = {
			action : "gastogeografico",
			mes : me.mes,
			ejercicio : me.ejercicio,
			grupos : me.getGrupos(),
			fuentes : me.getFuentes()
		};

		$http.post('/SGastoGeneral', data).then(obtenerGasto, errorCallback);

		me.loadAttempted = true;

	};

	me.cambiarTipo = function() {
		me.showloading = true;
		dibujarMapa();
	}

	function loadFuentes(response) {

		if (response.data.success) {
			me.fuentes_array = response.data.fuentes;
		}

		if (!me.fuentes_loaded) {
			$http.post('/SGrupoGasto', {
				ejercicio : me.ejercicio,
				t : (new Date()).getTime()
			}).then(loadGrupoGasto, errorCallback);
		}

		me.fuentes_loaded = true;
	}

	function loadGrupoGasto(response) {
		if (response.data.success) {
			me.grupos_array = response.data.Grupos;
			me.mesClick(moment().month() + 1);

			$http.post('/SLastupdate', {
				dashboard : 'ejecucionpresupuestaria',
				t : (new Date()).getTime()
			}).then(lastUpdate, errorCallback);
		}
	}

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
		var general = me.geograficos[0];
		
		for (var j = 1; j < me.geograficos.length; j++) {

				var porcentaje = 0;
				if (!me.mostrarPerCapita)
					porcentaje = me.geograficos[j].gasto / general.gasto * 100;
				else
					porcentaje = me.geograficos[j].gastoPerCapita / general.gastoPerCapita * 100;
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
		google.maps.event.addListener(layer, 'click', function(e){
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
		 });
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