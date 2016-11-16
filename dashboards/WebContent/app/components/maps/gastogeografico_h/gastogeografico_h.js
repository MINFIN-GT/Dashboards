var modGastoGeografico = angular.module('mapsGastoGeograficoController', [
		'dashboards', 'ngAnimate', 'ngSanitize', 'ui.bootstrap' ]);

// Control principal
modGastoGeografico.controller('mapsGastoGeograficoController',
		fnGastosGeograficoCtrl);

// Control modal Info
modGastoGeografico
		.controller('modalInfoGeograficoController', fnInfoGeografico);

function randomFrom(min, max) {
	return Math.random() * (max - min) + min;
}

function getColor(porcentaje, $log) {
	var color = {};
	color["V"] = "#008000";
	color["VA"] = "#98fb98";
	color["A"] = "#ffff00";
	color["AR"] = "#ffdab9";
	color["R"] = "#ff0000";

	if (porcentaje >= 0 && porcentaje < 0.1) {
		return color["R"];
	} else if (porcentaje >= 0.1 && porcentaje < 0.3) {
		return color["AR"];
	} else if (porcentaje >= 0.3 && porcentaje < 0.5) {
		return color["A"];
	} else if (porcentaje >= 0.5 && porcentaje < 1) {
		return color["VA"];
	} else if (porcentaje >= 1) {
		return color["V"];
	} else {
		$log.info(porcentaje);
	}
}

function fnGastosGeograficoCtrl($uibModal, $http, uiGmapGoogleMapApi, $log) {
	var me = this;

	me.showloading = false;
	me.mes = 1;
	me.nmonth = "Enero";

	me.muniMap = [];

	me.fuentes = "Fuentes de Financiamiento";
	me.fuentes_descripcion = "Todas";
	me.fuentes_array = [];
	me.panel_fuentes;
	me.fuentes_loaded = false;

	me.grupos = "Grupos de Gasto";
	me.grupos_array = [];
	me.grupos_descripcion = "Todos";
	me.grupos_loaded = false;
	me.panel_grupos;
	me.todosgrupos = 1;

	me.lastupdate = "";

	me.tributarias = [ 11, 12, 13, 14, 15, 16, 21, 22, 29 ];

	me.mesClick = function(mes) {
		me.mes = mes;

		me.cargarGastos();

		switch (mes) {
		case 1:
			me.nmonth = "Enero";
			break;
		case 2:
			me.nmonth = "Febrero";
			break;
		case 3:
			me.nmonth = "Marzo";
			break;
		case 4:
			me.nmonth = "Abril";
			break;
		case 5:
			me.nmonth = "Mayo";
			break;
		case 6:
			me.nmonth = "Junio";
			break;
		case 7:
			me.nmonth = "Julio";
			break;
		case 8:
			me.nmonth = "Agosto";
			break;
		case 9:
			me.nmonth = "Septiembre";
			break;
		case 10:
			me.nmonth = "Octubre";
			break;
		case 11:
			me.nmonth = "Noviembre";
			break;
		case 12:
			me.nmonth = "Diciembre";
			break;
		}

	};

	function errorCallback(response) {

	}

	$http.post('/SFuente', {
		ejercicio : 2016,
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
			ejercicio : 2016,
			grupos : me.getGrupos(),
			fuentes : me.getFuentes()
		};

		$log.info(data);

		$http.post('/SGastoGeografico', data).then(obtenerGasto, errorCallback);

		me.showloading = false;
		me.loadAttempted = true;

	};

	function loadFuentes(response) {

		if (response.data.success) {
			me.fuentes_array = response.data.fuentes;
		}

		if (!me.fuentes_loaded) {
			$http.post('/SGrupoGasto', {
				ejercicio : 2016,
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

			uiGmapGoogleMapApi
					.then(function() {
						me.map = {
							center : {
								latitude : 15.605009229644448,
								longitude : -89.8793818359375
							},
							zoom : 8,
							options : {
								streetViewControl : false,
								scrollwheel : false
							}
						};

						var general = response.data.geograficos[0];
						$log.info(general);

						for (var j = 1; j < response.data.geograficos.length; j++) {

							// municipios es una variable que se encuentra en
							// assets/data/municipios.js
							var muni = municipios[response.data.geograficos[j].geografico];

							if (muni != null) {
								var porcentaje = response.data.geograficos[j].gasto
										/ general.gasto * 100;
								// $log.info(response.data.geograficos[j].nombre,porcentaje);

								for (var i = 0; i < muni.length; i++) {
									me.muniMap
											.push({
												id : muni[i].propiedad,
												path : muni[i].coordenadas,
												stroke : {
													color : '#6060FB',
													weight : 1
												},
												editable : false,
												draggable : false,
												geodesic : false,
												visible : true,
												fill : {
													color : (muni.CODIGO != 2000 ? getColor(
															porcentaje, $log)
															: '#a7d0e1'),
													opacity : 0.7
												},
												events : {
													click : function() {
														$uibModal
																.open({
																	animation : 'true',
																	ariaLabelledBy : 'modal-title',
																	ariaDescribedBy : 'modal-body',
																	templateUrl : 'infoGastoGeografico.jsp',
																	controller : 'modalInfoGeograficoController',
																	controllerAs : 'infoCtrl',
																	backdrop : 'static',
																	size: 'sm',
																	resolve : {
																		data : this.events.data
																	}
																});
													},
													data : response.data.geograficos[j]
												},
											});
								}
							}
						}
					});
			me.muniMap = [];
		}
	}
}

function fnInfoGeografico($uibModalInstance, $log, data) {
	var me = this;
	me.data = data;

	me.ok = function() {
		$uibModalInstance.close('ok');
	};

	me.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}