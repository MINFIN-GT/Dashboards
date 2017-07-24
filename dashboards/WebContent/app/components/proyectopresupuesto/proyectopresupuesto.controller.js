/**
 * 
 */

angular.module('proyectoPresupuestoController',['dashboards','ui.bootstrap.contextMenu']).controller('proyectoPresupuestoController',['$scope','$routeParams','$http','$interval',
	'$location','uiGridTreeViewConstants','uiGridConstants','i18nService','$timeout','uiGridGroupingConstants',
	   function($scope,$routeParams,$http, $interval, $location, uiGridTreeViewConstants, uiGridConstants, i18nService, $timeout, uiGridGroupingConstants){
			
			i18nService.setCurrentLang('es');
			this.ano = '';
			
			this.anoClick = function(ano){
				this.ano=ano;
				alert('Aqui muestra el proyecto del año '+ano);
			}
		}
	]);