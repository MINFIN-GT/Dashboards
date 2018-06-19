angular.module('calamidadAdminController',['dashboards','smart-table','ngFileUpload','ui.bootstrap']);
angular.module('calamidadAdminController').controller('adminCtrl', function($log,$scope,$rootScope,$routeParams,$http,$uibModal,$route){
	
    this.showloading = false;  
    this.actividades_data = [];
    this.actividades_data_original=[];
    
    $rootScope.subprograma = $routeParams.subprograma;
    this.titulo = $rootScope.titulo;
    
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
  
	this.loadList=function(){
		this.showloading=true;
		$http.post('/SSaveActividad', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.actividades_data_original = response.data.actividades;
		    	this.actividades_data = this.actividades_data_original.length> 0 ? this.actividades_data_original.slice(0) : [];
		    }
		    this.showloading=false;
	 	}.bind(this), function errorCallback(response){	
	 	}
		);
	}
	
	this.select=function(index){	
		$rootScope.id =this.actividades_data[index].id;
		$rootScope.nombre=this.actividades_data[index].nombre;
		$rootScope.descripcion=this.actividades_data[index].descripcion;
		$rootScope.fecha_inicio =  moment(this.actividades_data[index].fecha_inicio,"DD/MM/YYYY HH:mm a").toDate();
		$rootScope.fecha_fin =  moment(this.actividades_data[index].fecha_fin,"DD/MM/YYYY HH:mm a").toDate();
		$rootScope.entidades=this.actividades_data[index].entidades;
		$rootScope.porcentaje_ejecucion = this.actividades_data[index].porcentaje_ejecucion;
		$rootScope.coord_lat=this.actividades_data[index].latitude;
		$rootScope.coord_long=this.actividades_data[index].longitude;	
		$rootScope.coord = "(" + $rootScope.coord_lat +", "+ $rootScope.coord_long + ")";
		$rootScope.responsable_id=this.actividades_data[index].responsable_id;
		$rootScope.responsable_nombre=this.actividades_data[index].responsable_nombre;
		$rootScope.responsable_correo=this.actividades_data[index].responsable_correo;
		$rootScope.responsable_telefono=this.actividades_data[index].responsable_telefono;
		$rootScope.edit = false;
		 
		 var modalInstance = $uibModal.open({
		      animation: true,
		      keyboard:false,
		      backdrop:'static',
		      scope:$scope,
		      templateUrl: 'editActividad.html',
		      controller: 'editActividad',
		 });
		 
		 modalInstance.result.then(function() {
			 $route.reload();
		}, function() {

		})['finally'](function(){
			modalInstance = undefined;  
			$scope.render=false;
		});
 
	}
    
    this.addActivity = function () {
    	$rootScope.id = -1;
		 var modalInstance = $uibModal.open({
		      animation: true,
		      keyboard:false,
		      backdrop:'static',
		      scope:$scope,
		      templateUrl: 'editActividad.html',
		      controller: 'editActividad',
		 });
		 
		 modalInstance.result.then(function () {
			 $route.reload();
		    }, function () {
		    	
		    });
	 };	 
	 
	 this.addCompras = function () {
	    	$rootScope.id = -1;
			 var modalInstance = $uibModal.open({
			      animation: true,
			      keyboard:false,
			      backdrop:'static',
			      scope:$scope,
			      templateUrl: 'editCompra.html',
			      controller: 'editCompra',
			 });
			 
			 modalInstance.result.then(function () {
				 $route.reload();
			    }, function () {
			    	
			    });
		 };	 
	
		 this.addDonacion = function () {
		    	$rootScope.donacion_id = -1;
				 var modalInstance = $uibModal.open({
				      animation: true,
				      keyboard:false,
				      backdrop:'static',
				      scope:$scope,
				      templateUrl: 'editDonacion.html',
				      controller: 'editDonacion',
				 });
				 
				 modalInstance.result.then(function () {
					 $route.reload();
				    }, function () {
				    	
				    });
			 };
			 
			 this.addCUR = function () {
			    	$rootScope.cur_id = -1;
					 var modalInstance = $uibModal.open({
					      animation: true,
					      keyboard:false,
					      backdrop:'static',
					      scope:$scope,
					      templateUrl: 'editCUR.html',
					      controller: 'editCUR',
					 });
					 
					 modalInstance.result.then(function () {
						 $route.reload();
					    }, function () {
					    	
					    });
				 };
});


angular.module('calamidadAdminController')
.controller('editActividad', function ($log, $scope, $rootScope,  $http, $window,  $uibModalInstance,  $timeout,  uiGmapGoogleMapApi,uiGmapIsReady,Upload) {
	
	if ($rootScope.id<=0){
		$rootScope.id=-1; 
		$rootScope.nombre="";
		$rootScope.descripcion="";
		$rootScope.fecha_inicio=null;
		$rootScope.fecha_fin=null;
		$rootScope.entidades="";
		$rootScope.porcentaje_ejecucion = 0.0;
		$rootScope.coord_lat=null;
		$rootScope.coord_long=null;	
		$rootScope.coord="";
		$rootScope.entidad="";
		$rootScope.unidad_ejecutora="";
		$rootScope.programa="";
		//$rootScope.subprograma="";
		$rootScope.proyecto="";
		$rootScope.actividad="";
		$rootScope.obra="";
		$rootScope.responsable_nombre="";
		$rootScope.responsable_correo="";
		$rootScope.responsable_telefono="";
	}
	$rootScope.docFile = null
	$scope.activeTab=0;
	$scope.center="opt1";
	$scope.cancel = function () {
		$rootScope.render=false;
	    $uibModalInstance.dismiss('cancel');
	};
	
	$http.post('/STransparenciaDocumentos', { action: 'getlist', subprograma: $rootScope.subprograma, id:$rootScope.id, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	$scope.original_documentos = response.data.documentos;
	    	$scope.documentos = $scope.original_documentos.length> 0 ? $scope.original_documentos.slice(0) : [];
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
	 $scope.deleteDoc=function(idDoc){
		 var data = {action:"delete",iddoc:idDoc}
		 $http.post('/STransparenciaDocumentos', data).then(function(response){
			    if(response.data.success){
			    	$http.post('/STransparenciaDocumentos', { action: 'getlist', subprograma: $rootScope.subprograma, id:$rootScope.id, t: (new Date()).getTime() }).then(function(response){
			    	    if(response.data.success){
			    	    	$scope.original_documentos = response.data.documentos;
			    	    	$scope.documentos = $scope.original_documentos.length> 0 ? $scope.original_documentos.slice(0) : [];
			    	    }
			     	}.bind(this), function errorCallback(response){
			     		
			     	}
			    	);			    
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		) 
	 };
	
	$scope.save=function(){	  
		if ($rootScope.id>0)
			$scope.update()
		else{
	    	var tsFI= Math.floor($rootScope.fecha_inicio / 1 );
		 	var tsFF= Math.floor($rootScope.fecha_fin / 1 );
		 	var data = { action:"create", nombre: $rootScope.nombre, descripcion: $rootScope.descripcion, fecha_inicio:tsFI, fecha_fin:tsFF,
					 	entidades: $rootScope.entidades, porcentaje_ejecucion:$rootScope.porcentaje_ejecucion, coord_lat:$rootScope.coord_lat, coord_long:$rootScope.coord_long,
					 	responsable_nombre:$rootScope.responsable_nombre, responsable_correo:$rootScope.responsable_correo, responsable_telefono:$rootScope.responsable_telefono,
					 	programa:$rootScope.programa,subprograma:$rootScope.subprograma};
			$http.post('/SSaveActividad', data).then(function(response){
				    if(response.data.success){
						$rootScope.render=false;
				    	$uibModalInstance.close();
				    }
				    else
				    	window.alert("error");
			 	}.bind(this), function errorCallback(response){
			 		$scope.showerror = true;
			 	}
			 );		
		}
	 }.bind($scope);
		
	$scope.update=function(){
		 var tsFI= Math.floor($rootScope.fecha_inicio / 1 );
		 var tsFF= Math.floor($rootScope.fecha_fin / 1 );
		 var data = { action:"update", id:$rootScope.id, nombre:$rootScope.nombre, descripcion:$rootScope.descripcion, coord_lat:$rootScope.coord_lat, coord_long:$rootScope.coord_long,
				 		porcentaje_ejecucion:$rootScope.porcentaje_ejecucion, fecha_inicio:tsFI, fecha_fin:tsFF, entidades:$rootScope.entidades,
				 		responsable_nombre:$rootScope.responsable_nombre, responsable_correo:$rootScope.responsable_correo, responsable_telefono:$rootScope.responsable_telefono, responsable_id:$rootScope.responsable_id};
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success){
					$rootScope.render=false;
			    	$uibModalInstance.close();
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );
	 }.bind($scope);
	 
	 $scope.erase=function(){
		 var data = {action:"delete",id:$rootScope.id,responsable_id:$rootScope.responsable_id}
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success){
					$rootScope.render=false;
			    	$uibModalInstance.close();
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );
	 }.bind($scope);

 
	 $scope.uploadPic = function(file) {
			file.upload = Upload.upload({
			    url: '/SSaveFile',
			    data: {id_actividad: this.id, place: $rootScope.subprograma, file: file},
			  });
			
			 file.upload.then(function (response) {
			    $timeout(function () {
			      file.result = response.data;
			      $rootScope.docFile=null;
			      $http.post('/STransparenciaDocumentos', { action: 'getlist', subprograma: $rootScope.subprograma, id:$rootScope.id, t: (new Date()).getTime() }).then(function(response){
			    	    if(response.data.success){
			    	    	$scope.original_documentos = response.data.documentos;
			    	    	$scope.documentos = $scope.original_documentos.length> 0 ? $scope.original_documentos.slice(0) : [];
			    	    }
			     	}.bind(this), function errorCallback(response){
			     		
			     	}
			    	);	
			    });
			  }, function (response) {
			    if (response.status > 0)
			      this.errorMsg = response.status + ': ' + response.data;
			  }, function (evt) {
			    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
			  });
		 };	 
		 
	$scope.changeCenter=function(){
		if (this.center == "opt1")
			$scope.map.center = { latitude: '14.6287331', longitude:'-90.5111991' };
		else if (this.center == "opt2")
			$scope.map.center = { latitude: '14.5743078', longitude:'-90.5274319' };
		else if (this.center == "opt3")
			$scope.map.center = { latitude: '14.4376902', longitude:'-90.9427951' };
		$scope.map.zoom=15;
	}
	
	uiGmapGoogleMapApi.then(function() {
		$scope.map = { center: { latitude: '14.6287331', longitude:'-90.5111991' }, 
					   zoom: 15,					   
					   options: {
						   streetViewControl: false,
						   scrollwheel: true,
						   mapTypeId: google.maps.MapTypeId.SATELLITE
					   },
					   refresh: true,
					   clickedMarker: {
					        id: 0,
					        options:{
					        },
					        latitude:$scope.coord_lat,
					        longitude:$scope.coord_long,
					   },
					   events: {
						   click: function (mapModel, eventName, originalEventArgs) {
					          var e = originalEventArgs[0];
					          var lat = e.latLng.lat(),
					              lon = e.latLng.lng();
					          $scope.map.clickedMarker = {
					            id: 0,
					            options: {
					            },
					            latitude: lat,
					            longitude: lon
					          };
					          $rootScope.coord_lat=lat;
					          $rootScope.coord_long=lon;
					          $rootScope.coord = "(" + $rootScope.coord_lat +", "+ $rootScope.coord_long + ")";
					          $scope.activeTab=0;
					          $scope.$digest();
					        }					   
					   }
					   
					};
	  });
	
	uiGmapIsReady.promise().then(function(instances) {
		 maps = instances[0].map;
		 center = maps.getCenter();
		 maps.setCenter(center);
		 maps.setZoom(15);
		 google.maps.event.trigger(maps,'resize');
     })
     

	$scope.changeTab = function(val){
		$scope.activeTab = val;
	    $rootScope.render=true;

	};
	
	$scope.mapTabSelect = function(){
	    $rootScope.render=true;
	}
});

angular.module('calamidadAdminController')
.controller('editCompra', function ($log, $scope, $http, $window,  $uibModalInstance,  $timeout, $rootScope) {
	
	$scope.tipoCompra;
	$scope.idCompra;
	$scope.showloading=true;
	
	$scope.clearError=function(){
		$scope.error = false;
		$scope.errorMessage="";
	}
	
	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
	
	$http.post('/STransparenciaCompras', { action: 'getlist', subprograma: $rootScope.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	$scope.original_compras = response.data.compras;
	    	$scope.compras = $scope.original_compras.length> 0 ? $scope.original_compras.slice(0) : [];
	    	$scope.showloading=false;
	    }
 	}.bind($scope), function errorCallback(response){
    	$scope.showloading=false;
 	}
	);
	
	$scope.getCompra=function(){
    	$scope.showloading=true;
		$http.post('/STransparenciaCompras', { action: 'getlist', subprograma: $rootScope.subprograma, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	$scope.original_compras = response.data.compras;
		    	$scope.compras = $scope.original_compras.length> 0 ? $scope.original_compras.slice(0) : [];
		    	$scope.showloading=false;
		    }
	 	}.bind($scope), function errorCallback(response){
	    	$scope.showloading=false;
	 	}
		);
	}
	
	$scope.addCompra=function(){
		isValid = false;
		if (this.tipoCompra=="NOG"){
			if( !isNaN(parseFloat(this.idCompra)) && isFinite(this.idCompra) )
				isValid=true;
			else{
				isValid=false;
				$scope.error=true;
				$scope.errorMessage=" Formato invalido para NOG";
			}
		}
		
		for (i=0; i<$scope.compras.length; i++ ){
			isValid=true;
			if ($scope.compras[i].id == this.idCompra){
				isValid=false;
				$scope.error=true;
				$scope.errorMessage=" Proceso de compra ya existe";
				i=$scope.compras.length;
			}
		}
		
		if (isValid){
			$http.post('/STransparenciaCompras', { action: 'add', subprograma: $rootScope.subprograma, tipoCompra:this.tipoCompra, idCompra:this.idCompra, t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.getCompra();
			    	this.tipoCompra=null;
			    	this.idCompra=null;
			    }
			    else{
			    	$scope.error=true;
					$scope.errorMessage=response.data.result;
			    }
		 	}.bind($scope), function errorCallback(response){
		 	}
			);
		}
		
	};
	
	$scope.deleteCompra=function(tipo,id){
		$http.post('/STransparenciaCompras', { action: 'delete', subprograma: $rootScope.subprograma, tipoCompra:tipo, idCompra:id, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.getCompra();
		    }
	 	}.bind($scope), function errorCallback(response){
	 		
	 	}
		);
	};
	
});

angular.module('calamidadAdminController')
.controller('editDonacion', function ($log, $scope, $http, $window,  $uibModalInstance,  $timeout, $rootScope) {
	
	$scope.idDonacion;
	$scope.showloading=true;
	$scope.successMessage="";
	$scope.success=false;
	
	$scope.clearError=function(){
		$scope.error = false;
		$scope.errorMessage="";
		$scope.successMessage="";
		$scope.success=false;
	}
	
	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
	
	$http.post('/STransparenciaDonaciones', { action: 'getlist', subprograma: $rootScope.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	$scope.original_donaciones = response.data.donaciones;
	    	$scope.donaciones = $scope.original_donaciones.length> 0 ? $scope.original_donaciones.slice(0) : [];
	    	$scope.showloading=false;
	    }
 	}.bind($scope), function errorCallback(response){
    	$scope.showloading=false;
 	}
	);
	
	$scope.getDonacion=function(){
    	$scope.showloading=true;
		$http.post('/STransparenciaDonaciones', { action: 'getlist', subprograma: $rootScope.subprograma, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	$scope.original_donaciones= response.data.donaciones;
		    	$scope.donaciones = $scope.original_donaciones.length> 0 ? $scope.original_donaciones.slice(0) : [];
		    	$scope.showloading=false;
		    }
	 	}.bind($scope), function errorCallback(response){
	    	$scope.showloading=false;
	 	}
		);
	}
	
	$scope.addDonacion=function(){
		
			$http.post('/STransparenciaDonaciones', { action: 'add', subprograma: $rootScope.subprograma, 
				donante: this.donante,
				procedencia: this.procedencia,
				fecha_ingreso: this.fecha_ingreso,
				metodo_acreditamiento: this.metodo_acredita,
				monto_d: this.monto_d,
				monto_q: this.monto_q,
				estado: this.estado,
				destino: this.destino,
				t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.getDonacion();
			    	this.idDonacion=null;
			    	this.donante = null;
			    	this.procedencia = null;
			    	this.fecha_ingreso = null;
			    	this.metodo_acredita=null;
			    	this.monto_d = null;
			    	this.monto_q = null;
			    	this.estado = null;
			    	this.destino = null;
			    	$scope.success = true;
			    	$scope.successMessage="Donación agregada con éxito";
			    }
			    else{
			    	$scope.error=true;
					$scope.errorMessage=response.data.result;
			    }
		 	}.bind(this), function errorCallback(response){
		 	}
			);
		
	};
	
	$scope.deleteDonacion=function(tipo,id){
		$http.post('/STransparenciaDonaciones', { action: 'delete', subprograma: $rootScope.subprograma, idDonacion:id, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.getDonacion();
		    }
	 	}.bind($scope), function errorCallback(response){
	 		
	 	}
		);
	};
	
});

angular.module('calamidadAdminController')
.controller('editCUR', function ($log, $scope, $http, $window,  $uibModalInstance,  $timeout, $rootScope) {
	
	$scope.idCur;
	$scope.showloading=true;
	$scope.successMessage="";
	$scope.success=false;
	
	$scope.clearError=function(){
		$scope.error = false;
		$scope.errorMessage="";
		$scope.successMessage="";
		$scope.success=false;
	}
	
	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
	
	$http.post('/STransparenciaCurs', { action: 'getlist', subprograma: $rootScope.subprograma, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	$scope.original_curs = response.data.curs;
	    	$scope.curs = $scope.original_curs.length> 0 ? $scope.original_curs.slice(0) : [];
	    	$scope.showloading=false;
	    }
 	}.bind($scope), function errorCallback(response){
    	$scope.showloading=false;
 	}
	);
	
	$scope.getCur=function(){
    	$scope.showloading=true;
		$http.post('/STransparenciaCurs', { action: 'getlist', subprograma: $rootScope.subprograma, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	$scope.original_curs = response.data.curs;
		    	$scope.curs = $scope.original_curs.length> 0 ? $scope.original_curs.slice(0) : [];
		    	$scope.showloading=false;
		    }
	 	}.bind($scope), function errorCallback(response){
	    	$scope.showloading=false;
	 	}
		);
	}
	
	$scope.addCur=function(){
		isValid = false;
			$http.post('/STransparenciaCurs', { action: 'add', 
				subprograma: $rootScope.subprograma, 
				ejercicio:this.ejercicio, 
				entidad:this.entidad, 
				unidad_ejecutora: this.unidad_ejecutora,
				cur: this.idCur,
				t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.getCur();
			    	this.idCur=null;
			    	this.ejercicio = null;
			    	this.entidad = null;
			    	this.unidad_ejecutora = null;
			    	$scope.success = true;
			    	$scope.successMessage="CUR agregado con éxito";
			    }
			    else{
			    	$scope.error=true;
					$scope.errorMessage=response.data.result;
			    }
		 	}.bind(this), function errorCallback(response){
		 	}
			);
		
		
	};
	
	$scope.deleteCur=function(ejercicio, entidad, unidad_ejecutora, cur){
		$http.post('/STransparenciaCurs', { action: 'delete', subprograma: $rootScope.subprograma, 
			ejercicio: ejercicio, entidad:entidad, 
			unidad_ejecutora: unidad_ejecutora,
			cur: cur,
			t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.getCur();
		    }
	 	}.bind($scope), function errorCallback(response){
	 		
	 	}
		);
	};
	
});


