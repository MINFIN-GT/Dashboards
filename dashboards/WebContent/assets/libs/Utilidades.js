'use strict';

var app = angular.module('ngUtilidades', [ 'ngFlash' ]);

app.provider('Utilidades', function() {

	this.$get = [
			'$rootScope',
			'Flash',
			function($rootScope, $alertas) {
				var dataFactory = {};

				dataFactory.elementosPorPagina = 20;
				dataFactory.numeroMaximoPaginas = 5;
				dataFactory.sistema_nombre = "SIPRO";
				
				dataFactory.mensaje = function(tipo, texto) {
					return $alertas.create(tipo, texto, 5000, {
						container : 'alertas'
					});
				};

				dataFactory.setFocus = function(elemento){
					if(elemento !== undefined && elemento !== null){
						setTimeout(function(){elemento.focus();}, 100);
					}
				}

				return dataFactory;
			} ];
})