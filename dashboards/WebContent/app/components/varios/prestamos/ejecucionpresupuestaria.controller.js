var modulePEPM = angular.module('prestamosEjecucionPresupuestariaModule',
		[ 'dashboards' ]);
var controlPEPM = modulePEPM.controller('pepm_Controller', funcPEPM);

function funcPEPM($http, $log) {
	ctrlPEPM = this;

	ctrlPEPM.chart_colors = [ '#4682B4', '#F7464A', '#dcdcdc', '#97bbcd',
			'#ebb45c', '#949fb1', '#4d5360' ];

	Chart.defaults.global.colours = [ '#4682B4', '#F7464A', '#dcdcdc',
			'#97bbcd', '#ebb45c', '#949fb1', '#4d5360' ];

	ctrlPEPM.lastupdate;

	/**
	 * CHART_PRESTAMOS (BARRAS)
	 */
	ctrlPEPM.chart_prestamos = [];
	ctrlPEPM.chart_prestamos['labels'] = [];
	ctrlPEPM.chart_prestamos['legends'] = [];
	ctrlPEPM.chart_prestamos['data'] = [];
	ctrlPEPM.chart_prestamos['series'] = [];
	ctrlPEPM.chart_prestamos['options'] = {
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
	ctrlPEPM.chart_organizaciones = [];
	ctrlPEPM.chart_organizaciones['labels'] = [];
	ctrlPEPM.chart_organizaciones['legends'] = [];
	ctrlPEPM.chart_organizaciones['data'] = [];
	ctrlPEPM.chart_organizaciones['series'] = [];
	ctrlPEPM.chart_organizaciones['options'] = {
		bezierCurve : false,
		datasetStrokeWidth : 6,
		pointDotRadius : 6,
		scaleLabel : function(label) {
			return numeral(label.value).format('$ 0,0')
		},
		tooltipTemplate : "<%= numeral(value).format('$ 0,0.00') +' (' + numeral((circumference / 6.283)*100).format('0.00')+' %)' %>",
		multiTooltipTemplate : "<%= numeral(value).format('$ 0,0.00') %>"
	};

	$http.post('/SPrestamos', {
		tipo : 'PRESTAMOS'
	}).then(successPrestamos, errorCallback);

	$http.post('/SPrestamos', {
		tipo : 'ENTIDADES'
	}).then(successEntidades, errorCallback);

	$http.post('/SPrestamos', {
		tipo : 'ORGANISMOS'
	}).then(successOrganizaciones, errorCallback);

	$http.post('/SLastupdate', {
		dashboard : 'ejecucionpresupuestaria'
	}).then(successLastUpdate, errorCallback);

	function errorCallback(response) {
		$log.error(response);
	}

	function successPrestamos(response) {
		if (response.data.success) {

			var allPrestamos = response.data.prestamos;

			var count = [];
			var data_vigente = [];
			var data_ejecucion = [];
			var chartPrestamo = [ data_vigente, data_ejecucion ];

			ctrlPEPM.prestamos = [];
			ctrlPEPM.prestamos_totales = [ 0.0, 0.0, 0.0, 0.0, 0.0 ];

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

			for (var index = 0; index < allPrestamos.length; index++) {

				var unidad_ejecutora = {
					"nombre" : allPrestamos[index].unidad_ejecutora_nombre,
					"asignado" : allPrestamos[index].asignado,
					"modificaciones" : allPrestamos[index].modificaciones,
					"vigente" : allPrestamos[index].vigente,
					"ejecutado" : allPrestamos[index].ejecutado,
					"porcentaje" : allPrestamos[index].porcentaje,
					"nivel" : 2
				};

				ctrlPEPM.prestamos.push(unidad_ejecutora);

				e_asignado += unidad_ejecutora.asignado;
				e_vigente += unidad_ejecutora.vigente;
				e_ejecutado += unidad_ejecutora.ejecutado;
				e_modificaciones += unidad_ejecutora.modificaciones;

				p_asignado += unidad_ejecutora.asignado;
				p_vigente += unidad_ejecutora.vigente;
				p_ejecutado += unidad_ejecutora.ejecutado;
				p_modificaciones += unidad_ejecutora.modificaciones;

				if (index + 1 >= allPrestamos.length
						|| (allPrestamos[index + 1] !== null && (allPrestamos[index].entidad_nombre !== allPrestamos[index + 1].entidad_nombre || allPrestamos[index].prestamo_nombre !== allPrestamos[index + 1].prestamo_nombre))) {
					entidad_anterior = allPrestamos[index].entidad_nombre;
					var entidad = {
						"nombre" : allPrestamos[index].entidad_nombre,
						"asignado" : e_asignado,
						"modificaciones" : e_modificaciones,
						"vigente" : e_vigente,
						"ejecutado" : e_ejecutado,
						"porcentaje" : e_vigente > 0 ? e_ejecutado / e_vigente
								* 100 : 0.0,
						"nivel" : 1
					};

					ctrlPEPM.prestamos.splice(indexEntidad, 0, entidad);
					indexEntidad = ctrlPEPM.prestamos.length;

					e_asignado = 0.0;
					e_modificaciones = 0.0;
					e_vigente = 0.0;
					e_ejecutado = 0.0;
					e_porcentaje = 0.0;
				}

				if (index + 1 >= allPrestamos.length
						|| (allPrestamos[index + 1] !== null && allPrestamos[index].prestamo_nombre !== allPrestamos[index + 1].prestamo_nombre)) {
					prestamo_anterior = allPrestamos[index].prestamo_nombre;

					var prestamo = {
						"nombre" : allPrestamos[index].prestamo_nombre,
						"sigla": allPrestamos[index].sigla,
						"asignado" : p_asignado,
						"modificaciones" : p_modificaciones,
						"vigente" : p_vigente,
						"ejecutado" : p_ejecutado,
						"porcentaje" : p_vigente > 0 ? p_ejecutado / p_vigente
								* 100 : 0.0,
						"nivel" : 0
					};

					ctrlPEPM.prestamos_totales[0] += p_asignado;
					ctrlPEPM.prestamos_totales[1] += p_modificaciones;
					ctrlPEPM.prestamos_totales[2] += p_vigente;
					ctrlPEPM.prestamos_totales[3] += p_ejecutado;
					ctrlPEPM.prestamos_totales[4] = ctrlPEPM.prestamos_totales[2] > 0 ? ctrlPEPM.prestamos_totales[3]
							/ ctrlPEPM.prestamos_totales[2] * 100
							: 0.0;

					data_vigente.push(p_vigente / 1000000);
					data_ejecucion.push(p_ejecutado / 1000000);

					ctrlPEPM.prestamos.splice(indexPrestamo, 0, prestamo);
					indexPrestamo = ctrlPEPM.prestamos.length;
					indexEntidad++;

					ctrlPEPM.chart_prestamos['labels']
							.push(allPrestamos[index].sigla);
					ctrlPEPM.chart_prestamos['legends']
							.push(allPrestamos[index].prestamo_nombre);
					
					// reiniciamos valores
					p_asignado = 0.0;
					p_modificaciones = 0.0;
					p_vigente = 0.0;
					p_ejecutado = 0.0;
					p_porcentaje = 0.0;
				}

			}

			ctrlPEPM.chart_prestamos['series'] = [ 'vigente', 'ejecutado' ];
			ctrlPEPM.chart_prestamos['data'].push(data_vigente);
			ctrlPEPM.chart_prestamos['data'].push(data_ejecucion);
		}
	}

	function successEntidades(response) {
		if (response.data.success) {

			ctrlPEPM.tabla_entidades = response.data.prestamos;

			ctrlPEPM.tabla_entidades_totales = [ 0.0, 0.0, 0.0, 0.0, 0.0 ];

			for ( var index in ctrlPEPM.tabla_entidades) {
				ctrlPEPM.tabla_entidades_totales[0] += ctrlPEPM.tabla_entidades[index].asignado;
				ctrlPEPM.tabla_entidades_totales[1] += ctrlPEPM.tabla_entidades[index].modificaciones;
				ctrlPEPM.tabla_entidades_totales[2] += ctrlPEPM.tabla_entidades[index].vigente;
				ctrlPEPM.tabla_entidades_totales[3] += ctrlPEPM.tabla_entidades[index].ejecutado;
				ctrlPEPM.tabla_entidades_totales[4] += ctrlPEPM.tabla_entidades[index].porcentaje;
			}

			ctrlPEPM.tabla_entidades_totales[4] = (ctrlPEPM.tabla_entidades_totales[3]
					/ ctrlPEPM.tabla_entidades_totales[2] * 100);

		}

	}

	function successOrganizaciones(response) {
		if (response.data.success) {

			var prestamos = response.data.prestamos;

			var total = 0.0;
			var financiamiento = [];
			for (var i = 0; i < prestamos.length; i++) {
				ctrlPEPM.chart_organizaciones['labels']
						.push(prestamos[i].organismo);
				ctrlPEPM.chart_organizaciones['legends']
						.push(prestamos[i].organismo_nombre);
				financiamiento[i] = prestamos[i].ejecutado / 1000000;
				total += financiamiento[i];
			}

			ctrlPEPM.chart_organizaciones['data'] = financiamiento;

			ctrlPEPM.chart_organizaciones['total'] = (total);
		}
	}

	function successLastUpdate(response) {
		if (response.data.success) {
			ctrlPEPM.lastupdate = response.data.lastupdate;
		}
	}

}