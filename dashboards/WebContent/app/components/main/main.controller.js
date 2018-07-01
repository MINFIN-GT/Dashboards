/**
 * 
 */
var app = angular.module('dashboards',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate', 'ngTouch', 
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps','ng.deviceDetector','ui.grid.grouping','ui.grid.autoResize','ngCkeditor','ngFlash','ngUtilidades','ivh.treeview']);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	   $locationProvider.hashPrefix('!');
	   //$locationProvider.html5Mode(true);
	   $routeProvider
	   		/*.when('/main',{
        		templateUrl : '',
        		resolve:{
        			main: function main(){
        				window.location.href = '/main.jsp';
        			}
        		}
        	})*/
		    .when('/dashboards/ejecucionpresupuestaria/:reset_grid?',{
            	template: '<div load-on-demand="\'ejecucionpresupuestariaController\'" class="all_page"></div>'
            })
            .when('/dashboards/ejecucionfisica/:reset_grid?',{
            	template: '<div load-on-demand="\'ejecucionfisicaController\'" class="all_page"></div>'
            })
            .when('/dashboards/ejecucionfisicaentidad/:reset_grid?/:ejercicio?/:entidad?/:nombre?',{
            	template: '<div load-on-demand="\'ejecucionfisicaentidadController\'" class="all_page"></div>'
            })
            .when('/dashboards/copep/:reset_grid?',{
            	template: '<div load-on-demand="\'copepController\'" class="all_page"></div>'
            })
            .when('/dashboards/copeprenglon/:reset_grid?',{
            	template: '<div load-on-demand="\'copeprenglonController\'" class="all_page"></div>'
            })
            .when('/paptn/ejecucionfinanciera',{
            	template: '<div load-on-demand="\'paptn_ejecucionfinancieraController\'" class="all_page"></div>'
            })
            .when('/maps/gastoGeneral',{
            	template: '<div load-on-demand="\'mapsGastoGeneralModule\'" class="all_page"></div>'
            })
            .when('/maps/gastoCodedesMunis',{
            	template: '<div load-on-demand="\'mapsGastoCodedesMunisModule\'" class="all_page"></div>'
            })
            .when('/transparencia/estados_de_calamidad',{
            	template: '<div load-on-demand="\'estadoscalamidadController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/:subprograma',{
            	template: '<div load-on-demand="\'calamidadController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/mapa/:subprograma',{
            	template: '<div load-on-demand="\'calamidadMapaController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/actividades/:subprograma',{
            	template: '<div load-on-demand="\'calamidadActividadesController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/ejecucion/:subprograma',{
            	template: '<div load-on-demand="\'calamidadEjecucionController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/admin/:subprograma',{
            	template: '<div load-on-demand="\'calamidadAdminController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/documentos/:subprograma',{
            	template: '<div load-on-demand="\'calamidadDocumentosController\'" class="all_page"></div>'
            })
            .when('/transparencia/calamidad/compras/:subprograma',{
            	template: '<div load-on-demand="\'calamidadComprasController\'" class="all_page"></div>'
            })
            .when('/presidenciales/metas',{
            	template: '<div load-on-demand="\'metasController\'" class="all_page"></div>'
            })
            .when('/prestamos',{
            	template: '<div load-on-demand="\'prestamosEjecucionPresupuestariaModule\'" class="all_page"></div>'
            })
            .when('/proyectopresupuesto',{
            	template: '<div load-on-demand="\'proyectoPresupuestoController\'" class="all_page"></div>'
            })
            .when('/dashboards/eventosgc',{
            	template: '<div load-on-demand="\'eventosGCController\'" class="all_page"></div>'
            })
            .when('/dashboards/proyeccion_ingresos',{
            	template: '<div load-on-demand="\'ingresosController\'" class="all_page"></div>'
            })
            .when('/dashboards/proyeccion_egresos',{
            	template: '<div load-on-demand="\'egresosController\'" class="all_page"></div>'
            })
            .when('/dashboards/flujocaja',{
            	template: '<div load-on-demand="\'flujoController\'" class="all_page"></div>'
            })
            .when('/logs/logs',{
            	template: '<div load-on-demand="\'logsController\'" class="all_page"></div>'
            })
            .when('/admin/users',{
            	template: '<div load-on-demand="\'usersController\'" class="all_page"></div>'
            })

            /*.when('/salir',{
            	templateUrl : '<div></div>',
            	resolve:{
            		logout: function logout($http){
            			$http.post('/SLogout', '').then(function(response){
	        				    if(response.data.success)
	        				    	window.location.href = '/login.jsp';
	        			 	}, function errorCallback(response){
	        			 		
	        			 	}
	        			 );
            			return true;
            		}
            	}
            });*/
    }]);

app.config(['$loadOnDemandProvider', function ($loadOnDemandProvider) {
	   var modules = [
	       {
	    	   name: 'ejecucionpresupuestariaController',
	    	   script: '/app/components/ejecucionpresupuestaria/ejecucionpresupuestaria.controller.js',
	    	   template: '/app/components/ejecucionpresupuestaria/ejecucionpresupuestaria.jsp'
	       },
	       {
	    	   name: 'ejecucionfisicaController',
	    	   script: '/app/components/ejecucionfisica/ejecucionfisica.controller.js',
	    	   template: '/app/components/ejecucionfisica/ejecucionfisica.jsp'
	       },
	       {
	    	   name: 'ejecucionfisicaentidadController',
	    	   script: '/app/components/ejecucionfisicaentidad/ejecucionfisicaentidad.controller.js',
	    	   template: '/app/components/ejecucionfisicaentidad/ejecucionfisicaentidad.jsp'
	       },
	       {
	    	   name: 'copepController',
	    	   script: '/app/components/copep/copep.controller.js',
	    	   template: '/app/components/copep/copep.jsp'
	       },
	       {
	    	   name: 'copeprenglonController',
	    	   script: '/app/components/copeprenglon/copeprenglon.controller.js',
	    	   template: '/app/components/copeprenglon/copeprenglon.jsp'
	       },
	       {
	           name: 'paptn_ejecucionfinancieraController',     
	           script: '/app/components/paptn/ejecucionfinanciera/ejecucionfinanciera.controller.js',
	           template: '/app/components/paptn/ejecucionfinanciera/ejecucionfinanciera.jsp'
	       },
	       {
	    	   name: 'mapsGastoGeneralModule',
	    	   script: '/app/components/maps/gastoGeneral/gastoGeneral.controller.js',
	    	   template: '/app/components/maps/gastoGeneral/gastoGeneral.jsp'
	       },
	       {
	    	   name: 'mapsGastoCodedesMunisModule',
	    	   script: '/app/components/maps/gastoCodedesMunis/gastoCodedesMunis.controller.js',
	    	   template: '/app/components/maps/gastoCodedesMunis/gastoCodedesMunis.jsp'
	       },
	       {
	    	   name: 'proyecciongastoController',
	    	   script: '/app/components/proyecciongasto/proyecciongasto.controller.js',
	    	   template: '/app/components/proyecciongasto/proyecciongasto.jsp'
	       },
	       {
	    	   name: 'estadoscalamidadController',     
	           script: '/app/components/transparencia/estadoscalamidad.controller.js',
	           template: '/app/components/transparencia/estadoscalamidad.jsp'
	       },
	       {
	    	   name: 'calamidadController',     
	           script: '/app/components/transparencia/calamidad/calamidad.controller.js',
	           template: '/app/components/transparencia/calamidad/calamidad.jsp'
	       },
	       {
	    	   name: 'calamidadMapaController',     
	           script: '/app/components/transparencia/calamidad/calamidadMapa.controller.js',
	           template: '/app/components/transparencia/calamidad/calamidadMapa.jsp'
	       },
	       {
	    	   name: 'calamidadActividadesController',     
	           script: '/app/components/transparencia/calamidad/calamidadActividades.controller.js',
	           template: '/app/components/transparencia/calamidad/calamidadActividades.jsp'
	       },
	       {
	    	   name: 'calamidadEjecucionController',     
	           script: '/app/components/transparencia/calamidad/calamidadEjecucion.controller.js',
	           template: '/app/components/transparencia/calamidad/calamidadEjecucion.jsp'
	       },
	       {
	    	   name: 'calamidadAdminController',     
	           script: '/app/components/transparencia/calamidad/admin/calamidadAdmin.controller.js',
	           template: '/app/components/transparencia/calamidad/admin/calamidadAdmin.jsp'
	       },
	       {
	    	   name: 'calamidadDocumentosController',     
	           script: '/app/components/transparencia/calamidad/calamidadDocumentos.controller.js',
	           template: '/app/components/transparencia/calamidad/calamidadDocumentos.jsp'
	       },
	       {
	    	   name: 'calamidadComprasController',     
	           script: '/app/components/transparencia/calamidad/calamidadCompras.controller.js',
	           template: '/app/components/transparencia/calamidad/calamidadCompras.jsp'
	       },
	       { 
	    	   name: 'metasController',     
	           script: '/app/components/presidenciales/metas/metas.controller.js',
	           template: '/app/components/presidenciales/metas/metas.jsp'
	       },
	       {
	    	   name: 'prestamosEjecucionPresupuestariaModule',     
	           script:   '/app/components/creditopublico/prestamos/prestamos.controller.js',
	           template: '/app/components/creditopublico/prestamos/prestamos.jsp'
	       },{
	    	   name: 'proyectoPresupuestoController',
	    	   script: '/app/components/proyectopresupuesto/proyectopresupuesto.controller.js',
	    	   template: '/app/components/proyectopresupuesto/proyectopresupuesto.jsp'
	       },
	       {
	    	   name: 'eventosGCController',
	    	   script: '/app/components/eventosgc/eventosgc_general.controller.js',
	    	   template: '/app/components/eventosgc/eventosgc_general.jsp'
	       },
	       {
	    	   name: 'ingresosController',
	    	   script: '/app/components/proyeccion_ingresos/ingresos.controller.js',
	    	   template: '/app/components/proyeccion_ingresos/ingresos.jsp'
	       },
	       {
	    	   name: 'egresosController',
	    	   script: '/app/components/proyeccion_egresos/egresos.controller.js',
	    	   template: '/app/components/proyeccion_egresos/egresos.jsp'
	       },
	       {
	    	   name: 'flujoController',
	    	   script: '/app/components/flujocaja/flujo.controller.js',
	    	   template: '/app/components/flujocaja/flujo.jsp'
	       },
	       {
	    	   name: 'logsController',
	    	   script: '/app/components/logs/logs.controller.js',
	    	   template: '/app/components/logs/logs.jsp'
	       },
	       {
	    	   name: 'usersController',
	    	   script: '/app/components/admin/users.controller.js',
	    	   template: '/app/components/admin/users.jsp'
	       }
	   ];
	   $loadOnDemandProvider.config(modules);
}]);

app.config(['uiGmapGoogleMapApiProvider',function(uiGmapGoogleMapApiProvider) {
    uiGmapGoogleMapApiProvider.configure({
        key: 'AIzaSyBPq-t4dJ1GV1kdtXoVZfG7PtfEAHrhr00',
        v: '3.', //defaults to latest 3.X anyhow
        libraries: 'weather,geometry,visualization'
    });
}]);

app.controller('MainController',['$scope','$document','deviceDetector','$rootScope','$location','$window',
   function($scope,$document,deviceDetector,$rootScope,$location,$window){
	$scope.lastscroll = 0;
	$scope.hidebar = false;
	
	numeral.language('es', numeral_language);
	
	$document.bind('scroll', function(){
		if($document[0].body.scrollTop > 15){
			if ($scope.lastscroll>$document[0].body.scrollTop) { //Scroll to Top
		        $scope.hidebar = false;
		    } else if($document[0].body.scrollTop>15) { //Scroll to Bottom
		        $scope.hidebar = true;
		    }
			$scope.$apply();
		}
		$scope.lastscroll = $document[0].body.scrollTop;
	});
	
	$scope.hideBarFromMenu=function(){
		$scope.hidebar = true;
	}
	
	$scope.showBarFromMenu=function(){
		$scope.hidebar = false;
	}
	
	$scope.device = deviceDetector;
	
	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		if (location.hostname !== "localhost" || location.hostname !== "127.0.0.1"){
			$window.ga('create', 'UA-74443600-1', 'auto');
    		$window.ga('send', 'pageview', $location.path());
		}
    });
}]);



