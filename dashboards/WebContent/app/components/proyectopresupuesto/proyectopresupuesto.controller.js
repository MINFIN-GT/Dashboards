/**
 * 
 */

angular.module('proyectopresupuestoController',['dashboards','ui.bootstrap.contextMenu']).controller('proyectopresupuestoController',['$scope','$routeParams','$http','$interval',
	'$location','uiGridTreeViewConstants','uiGridConstants','i18nService','$timeout','uiGridGroupingConstants',
	   function($scope,$routeParams,$http, $interval, $location, uiGridTreeViewConstants, uiGridConstants, i18nService, $timeout, uiGridGroupingConstants){
			
			i18nService.setCurrentLang('es');
			
			this.texto = "Esta es una prueba";
			this.teto_size = context.measureText(this.texto).width;
		}
	]);