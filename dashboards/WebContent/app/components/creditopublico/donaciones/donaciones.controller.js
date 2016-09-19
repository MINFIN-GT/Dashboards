var moduleDEPM = angular.module('donacionesEjecucionPresupuestariaModule',
		[ 'dashboards' ]);
var contolDEPM = moduleDEPM.controller('depm_Controller', funcDEPM);

function funcDEPM($http, $log) {
	me = this;

	me.chart_colors = [ '#4682B4', '#F7464A', '#dcdcdc', '#97bbcd',
			'#ebb45c', '#949fb1', '#4d5360' ];

	Chart.defaults.global.colours = [ '#4682B4', '#F7464A', '#dcdcdc',
			'#97bbcd', '#ebb45c', '#949fb1', '#4d5360' ];

	me.lastupdate;

	/**
	 * CHART_DONACIONES (BARRAS)
	 */
	me.chart_donaciones = [];
	me.chart_donaciones['labels'] = [];
	me.chart_donaciones['legends'] = [];
	me.chart_donaciones['data'] = [];
	me.chart_donaciones['series'] = [];
	me.chart_donaciones['options'] = {
		bezierCurve : false,
		datasetStrokeWidth : 6,
		pointDotRadius : 6,
		scaleLabel : function(label) {
			return numeral(label.value).format('$ 0,0')
		},
		tooltipTemplate : "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
		multiTooltipTemplate : "<%= numeral(value).format('$ 0,0.00') %>"
	};

	/**
	 * CHART_ORGANIZACIONES (PIE)
	 */
	me.chart_organizaciones = [];
	me.chart_organizaciones['labels'] = [];
	me.chart_organizaciones['legends'] = [];
	me.chart_organizaciones['data'] = [];
	me.chart_organizaciones['series'] = [];
	me.chart_organizaciones['options'] = {
		bezierCurve : false,
		datasetStrokeWidth : 6,
		pointDotRadius : 6,
		scaleLabel : function(label) {
			return numeral(label.value).format('$ 0,0')
		},
		tooltipTemplate : "<%= numeral(value).format('$ 0,0.00') +' (' + numeral((circumference / 6.283)*100).format('0.00')+' %)' %>",
		multiTooltipTemplate : "<%= numeral(value).format('$ 0,0.00') %>"
	};

	$http.post('/SDonaciones', {
		tipo : 'DONACIONES'
	}).then(successDonaciones, errorCallback);

	$http.post('/SDonaciones', {
		tipo : 'ENTIDADES'
	}).then(successEntidades, errorCallback);

	$http.post('/SDonaciones', {
		tipo : 'ORGANISMOS'
	}).then(successOrganizaciones, errorCallback);

	$http.post('/SLastupdate', {
		dashboard : 'ejecucionpresupuestaria'
	}).then(successLastUpdate, errorCallback);

	function errorCallback(response) {
		$log.error(response);
	}

	function successDonaciones(response) {
		if (response.data.success) {

			var allDonaciones = response.data.donaciones;

			var count = [];
			var data_vigente = [];
			var data_ejecucion = [];
			var chartPrestamo = [ data_vigente, data_ejecucion ];

			me.donaciones = [];
			me.donaciones_totales = [ 0.0, 0.0, 0.0, 0.0, 0.0 ];

			var data_vigente = [];
			var data_ejecucion = [];

			var p_asignado = 0.0;
			var p_modificaciones = 0.0;
			var p_vigente = 0.0;
			var p_ejecutado = 0.0;
			var p_porcentaje = 0.0;

			var e_asignado = 0.0;
			var e_modificaciones = 0.0;
			var e_vigente = 0.0;
			var e_ejecutado = 0.0;
			var e_porcentaje = 0.0;

			var indexPrestamo = 0;
			var indexEntidad = 0;

			for (var index = 0; index < allDonaciones.length; index++) {

				var unidad_ejecutora = {
					"nombre" : allDonaciones[index].unidad_ejecutora_nombre,
					"asignado" : allDonaciones[index].asignado,
					"modificaciones" : allDonaciones[index].modificaciones,
					"vigente" : allDonaciones[index].vigente,
					"ejecutado" : allDonaciones[index].ejecutado,
					"porcentaje" : allDonaciones[index].porcentaje,
					"nivel" : 2
				};

				me.donaciones.push(unidad_ejecutora);

				e_asignado += unidad_ejecutora.asignado;
				e_vigente += unidad_ejecutora.vigente;
				e_ejecutado += unidad_ejecutora.ejecutado;
				e_modificaciones += unidad_ejecutora.modificaciones;

				p_asignado += unidad_ejecutora.asignado;
				p_vigente += unidad_ejecutora.vigente;
				p_ejecutado += unidad_ejecutora.ejecutado;
				p_modificaciones += unidad_ejecutora.modificaciones;

				if (index + 1 >= allDonaciones.length
						|| (allDonaciones[index + 1] !== null && (allDonaciones[index].entidad_nombre !== allDonaciones[index + 1].entidad_nombre || allDonaciones[index].prestamo_nombre !== allDonaciones[index + 1].prestamo_nombre))) {
					entidad_anterior = allDonaciones[index].entidad_nombre;
					var entidad = {
						"nombre" : allDonaciones[index].entidad_nombre,
						"asignado" : e_asignado,
						"modificaciones" : e_modificaciones,
						"vigente" : e_vigente,
						"ejecutado" : e_ejecutado,
						"porcentaje" : e_vigente > 0 ? e_ejecutado / e_vigente
								* 100 : 0.0,
						"nivel" : 1
					};

					me.donaciones.splice(indexEntidad, 0, entidad);
					indexEntidad = me.donaciones.length;

					e_asignado = 0.0;
					e_modificaciones = 0.0;
					e_vigente = 0.0;
					e_ejecutado = 0.0;
					e_porcentaje = 0.0;
				}

				if (index + 1 >= allDonaciones.length
						|| (allDonaciones[index + 1] !== null && allDonaciones[index].prestamo_nombre !== allDonaciones[index + 1].prestamo_nombre)) {
					prestamo_anterior = allDonaciones[index].prestamo_nombre;

					var prestamo = {
						"nombre" : allDonaciones[index].prestamo_nombre + " ("
								+ allDonaciones[index].correlativo + ")",
						"asignado" : p_asignado,
						"modificaciones" : p_modificaciones,
						"vigente" : p_vigente,
						"ejecutado" : p_ejecutado,
						"porcentaje" : p_vigente > 0 ? p_ejecutado / p_vigente
								* 100 : 0.0,
						"nivel" : 0
					};

					me.donaciones_totales[0] += p_asignado;
					me.donaciones_totales[1] += p_modificaciones;
					me.donaciones_totales[2] += p_vigente;
					me.donaciones_totales[3] += p_ejecutado;
					me.donaciones_totales[4] = me.donaciones_totales[2] > 0 ? me.donaciones_totales[3]
							/ me.donaciones_totales[2] * 100
							: 0.0;

					data_vigente.push(p_vigente / 1000000);
					data_ejecucion.push(p_ejecutado / 1000000);

					me.donaciones.splice(indexPrestamo, 0, prestamo);
					indexPrestamo = me.donaciones.length;
					indexEntidad++;

					me.chart_donaciones['labels']
							.push(allDonaciones[index].correlativo);
					me.chart_donaciones['legends']
							.push(allDonaciones[index].prestamo_nombre);

					// reiniciamos valores
					p_asignado = 0.0;
					p_modificaciones = 0.0;
					p_vigente = 0.0;
					p_ejecutado = 0.0;
					p_porcentaje = 0.0;
				}

			}

			me.chart_donaciones['series'] = [ 'vigente', 'ejecutado' ];
			me.chart_donaciones['data'].push(data_vigente);
			me.chart_donaciones['data'].push(data_ejecucion);
		}
	}

	function successEntidades(response) {
		if (response.data.success) {

			me.tabla_entidades = response.data.donaciones;

			me.tabla_entidades_totales = [ 0.0, 0.0, 0.0, 0.0, 0.0 ];

			for ( var index in me.tabla_entidades) {
				me.tabla_entidades_totales[0] += me.tabla_entidades[index].asignado;
				me.tabla_entidades_totales[1] += me.tabla_entidades[index].modificaciones;
				me.tabla_entidades_totales[2] += me.tabla_entidades[index].vigente;
				me.tabla_entidades_totales[3] += me.tabla_entidades[index].ejecutado;
				me.tabla_entidades_totales[4] += me.tabla_entidades[index].porcentaje;
			}

			me.tabla_entidades_totales[4] = (me.tabla_entidades_totales[3]
					/ me.tabla_entidades_totales[2] * 100);

		}

	}

	function successOrganizaciones(response) {
		if (response.data.success) {

			var donaciones = response.data.donaciones;

			var total = 0.0;
			var financiamiento = [];
			for (var i = 0; i < donaciones.length; i++) {
				me.chart_organizaciones['labels']
						.push(donaciones[i].organismo);
				me.chart_organizaciones['legends']
						.push(donaciones[i].organismo_nombre);
				financiamiento[i] = donaciones[i].ejecutado / 1000000;
				total += financiamiento[i];
			}

			me.chart_organizaciones['data'] = financiamiento;

			me.chart_organizaciones['total'] = (total);
		}
	}

	function successLastUpdate(response) {
		if (response.data.success) {
			me.lastupdate = response.data.lastupdate;
		}
	}

}