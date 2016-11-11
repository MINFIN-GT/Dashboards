var modGastoGeografico = angular.module('mapsGastoGeograficoController',
		[ 'dashboards', 'ngAnimate', 'ngSanitize', 'ui.bootstrap' ]);

modGastoGeografico.controller('mapsGastoGeograficoController',
		fnGastosGeografico);

function fnGastosGeografico($uibModal, $http, uiGmapGoogleMapApi, $log) {
	var me = this;

	me.muniPolygon = [];

	var data = {
		action : 'gastogeografico'
	};

	uiGmapGoogleMapApi.then(function() {
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

		for ( var muniID in municipios) {
			var muni = municipios[muniID];

			for (var i = 0; i < muni.length; i++) {
				me.muniPolygon.push({
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
						color : '#ff0000',
						opacity : 0.8
					},
					events : {
						click : function() {							
							$uibModal.open({
							      animation: 'true',
							      ariaLabelledBy: 'modal-title',
							      ariaDescribedBy: 'modal-body',
							      templateUrl: 'infoGastoGeografico.jsp',
							      controller: 'modalCtrlInfoGeografico',
							      controllerAs: '$ctrl',
							      backdrop:'static',
							      size: 'lg',
							      resolve: {
							        data: this.events.data
							      }
							});
						},
						data : muni[i].propiedad
					},
				});

			}
		}
	});
}

modGastoGeografico.controller('modalCtrlInfoGeografico', fnInfoGeografico);
		
function fnInfoGeografico($uibModalInstance, $log, data) {
	  var me = this;
	  me.data = data;	  
	  
	  me.ok = function () {
	    $uibModalInstance.close('ok');
	  };

	  me.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
}