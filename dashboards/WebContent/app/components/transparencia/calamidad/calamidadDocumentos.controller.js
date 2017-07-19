/**
 * 
 */


angular.module('calamidadDocumentosController',['dashboards','smart-table','ngFileUpload']).controller('calamidadDocumentosController',['$scope','$route','$routeParams','$http','$filter','Upload','$timeout','$rootScope',
	   function($scope,$route,$routeParams,$http,$filter,Upload,$timeout,$rootScope){
	
	this.lastupdate='';
	this.documentos=[];
	this.original_documentos=[];
	this.docFile = null;
	this.showloading=true;
	
	this.titulo=$rootScope.titulo;
	this.tipo = $rootScope.tipo;
	this.subprograma = $routeParams.subprograma;
	
	if($rootScope.titulo==null || $rootScope.titulo === undefined){
		$http.post('/STransparenciaEstadosCalamidad', { action: 'getEstado',subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.titulo = response.data.results.nombre;
		    	this.tipo = response.data.results.tipo;
		    	$rootScope.titulo = this.titulo;
		    	$rootScope.tipo = this.tipo;
			}
		}.bind(this)
		);
	}
	
	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.lastupdate = response.data.lastupdate;
			}
		}.bind(this)
	);
	
	$http.post('/STransparenciaDocumentos', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.original_documentos = response.data.documentos;
	    	this.documentos = this.original_documentos.length> 0 ? this.original_documentos.slice(0) : [];
	    	this.showloading=false;
	    }
 	}.bind(this), function errorCallback(response){
    	this.showloading=false;
 	}
	);
	
	this.uploadFile = function(file) {
		file.upload = Upload.upload({
		    url: '/SSaveFile',
		    data: {id_actividad: -1, place: this.subprograma, file: file},
		  });
		
		 file.upload.then(function (response) {
		    $timeout(function () {
		      file.result = response.data;
		      $route.reload();
		    });
		  }, function (response) {
		    if (response.status > 0)
		      this.errorMsg = response.status + ': ' + response.data;
		  }, function (evt) {
		    // Math.min is to fix IE which reports 200% sometimes
		    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		  });
	 }
	
}]);

