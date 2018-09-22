var modGastoCodedesMunis = angular.module('mapsGastoCodedesMunisModule', [
		'dashboards', 'ngAnimate', 'ngSanitize', 'ui.bootstrap','ngMap' ]);

// Control principal
modGastoCodedesMunis.controller('mapsGastoCodedesMunisController',
		mapsGastoCodedesMunisController);

// Control modal Info
modGastoCodedesMunis.controller('modalInfoGastoCodedesMunisController',
		modalInfoGastoCodedesMunisController);

function mapsGastoCodedesMunisController($uibModal, $http,
		$log,NgMap) {
	var me = this;

	me.mostrarCodedes = true;
	me.mostrarMunis = true;

	me.showloading = false;
	me.mes = 1;
	me.nmonth = "Enero";
	me.ejercicio = moment().year();

	me.renglones = [ 444, 448, 523, 532 ]

	me.map = null;

	me.lastupdate = "";
	
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
	
	me.grupos = [];
	me.grupos["v"] = [];
	me.grupos["va"] = [];
	me.grupos["a"] = [];
	me.grupos["ar"] = [];
	me.grupos["r"] = [];
	
	me.anios = [];
	
	for(var i=me.ejercicio; i>=2016; i--)
		me.anios.push(i);

	function getGrupo(porcentaje) {
		
		if (porcentaje >= 0 && porcentaje < 0.2) {
			return "r";
		} else if (porcentaje >= 0.2 && porcentaje < 0.4) {
			return "ar";
		} else if (porcentaje >= 0.4 && porcentaje < 0.6) {
			return "a";
		} else if (porcentaje >= 0.6 && porcentaje < 0.8) {
			return "va";
		} else if (porcentaje >= 0.8) {
			return "v";
		} else {
			return "d";
		}
	}

	$http.post('/SLastupdate', {
		dashboard : 'ejecucionpresupuestaria',
		t : (new Date()).getTime()
	}).then(lastUpdate, errorCallback);

	function lastUpdate(response) {
		if (response.data.success) {
			me.lastupdate = response.data.lastupdate;
		}

		me.mesClick(moment().month() + 1);

	}

	function errorCallback(response) {

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

	me.cambiarRenglon = function() {
		me.cargarGastos();
	}

	me.getRenglones = function() {
		var renglones = "";

		if (me.mostrarMunis) {
			renglones = "444, 448, 523";
		}

		if (me.mostrarCodedes) {
			if (renglones != "") {
				renglones += ", ";
			}

			renglones += "532"
		}

		return renglones;
	};

	me.cargarGastos = function() {
		me.panel_fuentes = false;
		me.panel_grupos = false;

		me.showloading = true;
		me.loadAttempted = false;

		var data = {
			action : "gastogeografico",
			ejercicio: me.ejercicio,
			mes : me.mes,
			renglon : me.getRenglones()
		};

		$http.post('/SGastoCodedesMunis', data).then(obtenerGasto,errorCallback);

		me.loadAttempted = true;

	};

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
		
		NgMap.getMap({id: 'Codedes'}).then(function(map){
    		me.map_options.map = map;
    		dibujarLayerMunicipios();
    	});
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
			me.agasto = obtenerGeografico(e.row.Codigo.value);
			$uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'infoGastoCodedesMunis.jsp',
				controller : 'modalInfoGastoCodedesMunisController',
				controllerAs : 'infoCtrl',
				backdrop : 'static',
				size : 'sm',
				resolve : {
					data : me.agasto
				}
			});
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

function modalInfoGastoCodedesMunisController($uibModalInstance, $log, data) {
	var me = this;
	me.data = data;

	me.ok = function() {
		$uibModalInstance.close('ok');
	};

	me.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}