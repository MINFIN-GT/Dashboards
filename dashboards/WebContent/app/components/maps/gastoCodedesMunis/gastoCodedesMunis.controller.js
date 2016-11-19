var modGastoCodedesMunis = angular.module('mapsGastoCodedesMunisModule', [
		'dashboards', 'ngAnimate', 'ngSanitize', 'ui.bootstrap' ]);

// Control principal
modGastoCodedesMunis.controller('mapsGastoCodedesMunisController',
		fnGastosGeograficoCtrl);

// Control modal Info
modGastoCodedesMunis.controller('modalInfoGastoCodedesMunisController',
		fnInfoGastoCodedesMunis);

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

	if (porcentaje >= 0 && porcentaje < 0.2) {
		return color["R"];
	} else if (porcentaje >= 0.2 && porcentaje < 0.4) {
		return color["AR"];
	} else if (porcentaje >= 0.4 && porcentaje < 0.6) {
		return color["A"];
	} else if (porcentaje >= 0.6 && porcentaje < 0.8) {
		return color["VA"];
	} else if (porcentaje >= 0.8) {
		return color["V"];
	} else {
		$log.info(porcentaje);
	}
}

function fnGastosGeograficoCtrl($uibModal, $http, uiGmapGoogleMapApi, $log) {
	var me = this;

	me.mostrarCodedes = true;
	me.mostrarMunis = true;

	me.showloading = false;
	me.mes = 1;
	me.nmonth = "Enero";

	me.renglones = [ 444, 448, 523, 532 ]

	me.map = null;

	me.lastupdate = "";

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
			mes : me.mes,
			ejercicio : 2016,
			renglon : me.getRenglones()
		};

		$http.post('/SGastoCodedesMunis', data).then(obtenerGasto,
				errorCallback);

		me.showloading = false;
		me.loadAttempted = true;

	};

	function obtenerGasto(response) {

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
						},
						draw : null,
						polygons : []
					};

					if (response.data.success) {

						if (response.data.geograficos.length > 0) {
							var general = response.data.geograficos[0];

							// Se agrega Bélice sin ninguna funcion
							me.map.polygons.push({
								id : municipios["2000"][0].propiedad.CODIGO,
								path : municipios["2000"][0].coordenadas,
								stroke : {
									color : '#6060FB',
									weight : 1
								},
								editable : false,
								draggable : false,
								geodesic : false,
								visible : true,
								fill : {
									color : '#BDBDBD',
									opacity : 0.8
								}
							});
						}

						for (var j = 1; j < response.data.geograficos.length; j++) {

							// assets/data/municipios.js
							var muni = municipios[response.data.geograficos[j].geografico];

							if (muni != null) {
								var porcentaje = response.data.geograficos[j].gasto
										/ general.gasto * 100;

								for (var i = 0; i < muni.length; i++) {
									me.map.polygons
											.push({
												id : muni[i].propiedad,
												path : muni[i].coordenadas,
												stroke : {
													color : '#6060FB',
													weight : 1
												},
												editable : true,
												draggable : false,
												geodesic : false,
												visible : true,
												fill : {
													color : (muni.CODIGO != 2000 ? getColor(
															porcentaje, $log)
															: '#a7d0e1'),
													opacity : 0.8
												},
												events : {
													click : function() {
														$uibModal
																.open({
																	animation : 'true',
																	ariaLabelledBy : 'modal-title',
																	ariaDescribedBy : 'modal-body',
																	templateUrl : 'infoGastoCodedesMunis.jsp',
																	controller : 'modalInfoGastoCodedesMunisController',
																	controllerAs : 'infoCtrl',
																	backdrop : 'static',
																	size : 'sm',
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
					}
				});
	}

}

function fnInfoGastoCodedesMunis($uibModalInstance, $log, data) {
	var me = this;
	me.data = data;

	me.ok = function() {
		$uibModalInstance.close('ok');
	};

	me.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}