/**
 * 
 */

angular.module('ejecucionfisicaentidadController',['dashboards','ui.bootstrap']).controller('ejecucionfisicaentidadController',['$scope','$routeParams','$http','$interval', 
       'uiGridTreeViewConstants','uiGridConstants','i18nService','$timeout','uiGridGroupingConstants','$uibModal',
	   function($scope,$routeParams,$http, $interval, uiGridTreeViewConstants, uiGridConstants, i18nService, $timeout, uiGridGroupingConstants, $uibModal){
			i18nService.setCurrentLang('es');
			var current_year = moment().year();
			
			this.showloading = false;
			
			this.chartType = "bar";
			
			this.selectedRow = null;
			this.row_selected = [];
			this.loadAttempted = false;
			
			this.month= moment().month()+1;
			this.ano = $routeParams.ejercicio;
			
			this.entidad = {};
			this.entidad.nombre = $routeParams.nombre;
			this.entidad.id = $routeParams.entidad;
			this.unidad_ejecutora = null;
			this.programa = null;
			this.subprograma = null;
			this.proyecto = null;
			this.actividad = null;
			this.obra = null;
			this.titulo = "";
			
			this.lastupdate = "";
			
			this.chartLabels=['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
	    	this.chartSeries = ['Ejecución Presupuestaria', 'Ejecución Física'];
			this.chartData = [];
			this.chartData_start = [];
			
			this.total_ejecucion_fisica = 0;
			this.total_ejecucion_financiera = 0;
			this.indicador_total_ejecucion = 0;
			this.total_spi=0;
			this.level = 2;
			
			this.ejecucion_financiera=0.0, this.ejecucion_fisica=0.0, this.vigente_financiero=0.0;
			
			this.chart_colours = [ '#A9D18E', '#5B9BD5', '#F7464A', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'];
			
			
	    	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	this.lastupdate = response.data.lastupdate;
					}
				}.bind(this)
	    	);
			
			
			this.goLevel=function(level, mantain_select){
			
				this.level = level;
				
				switch(this.level){
		    		case 2:
		    			this.titulo = 'Unidades Ejecutoras';
		    			break;
		    		case 3:
		    			this.titulo = 'Programas';
		    			break;
		    		case 4:
		    			this.titulo = 'Subprogramas';
		    			break;
		    		case 5:
		    			this.titulo = 'Proyectos';
		    			break;
		    		case 6:
		    			this.titulo = 'Actividades / Obras';
		    			break;
		    	}
				
				if(this.level<7){
	    			this.actividad = null;
	    			this.obra = null;
	    			this.actividad_obra_nombre = "";
	    		}
	    		if(this.level<6){
	    			this.proyecto = null;
	    			this.proyecto_nombre = "";
	    		}
	    		if(this.level<5){
	    			this.subprograma = null;
	    			this.subprograma_nombre = "";
	    		}
	    		if(this.level<4){
	    			this.programa = null;
	    			this.programa_nombre = "";
	    		}
	    		if(this.level<3){
	    			this.unidad_ejecutora = null;
	    			this.unidad_ejecutora_nombre = "";
	    		}
	    		
	    		if(this.level<7){
	    			this.showloading=true;
					this.loadAttempted=false;
					
		    		var data_e = { action: 'entidadesData', level: this.level, ejercicio: this.ano,
							entidad: $routeParams.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
							proyecto: this.proyecto, actividad: this.actividad, obra: this.obra,
							t: (new Date()).getTime() };
		    		$http.post('/SEjecucionFisica', data_e).
		    			then(function(response){
						    if(response.data.success){
						    	if(!mantain_select)
						    		this.row_selected=[];
						    	var serie_presupuestaria=[];
						    	var serie_fisica=[];
						    	this.ejecucion_financiera = 0;
						    	this.vigente_financiero = 0;
						    	this.ejecucion_fisica = 0;
						    	for(var i=0; i<response.data.entidades.length; i++){
						    		this.ejecucion_financiera += response.data.entidades[i].ejecutado_financiero;
						    		this.vigente_financiero += response.data.entidades[i].vigente_financiero;
						    		this.ejecucion_fisica += response.data.entidades[i].porcentaje_ejecucion_fisica;
						    	}
						    	
						    	response.data.entidades.sort(this.compareEntidades);
						    	
						    	this.entidades_gridOptions.data = response.data.entidades;
						    	
						    	this.total_ejecucion_financiera = (this.ejecucion_financiera / this.vigente_financiero )*100;
						    	this.total_ejecucion_fisica = (this.ejecucion_fisica / response.data.entidades.length);
						    	this.indicador_total_ejecucion = ((this.total_ejecucion_fisica)/((100/12)*( this.ano<moment().year() ? 12 : this.month)))*100;
						    	
						    	if(this.indicador_total_ejecucion<50)
						    		this.indicador_total_ejecucion = 4;
								else if(this.indicador_total_ejecucion<75)
									this.indicador_total_ejecucion = 2;
								else if(this.indicador_total_ejecucion<=100)
									this.indicador_total_ejecucion = 3;
								else
									this.indicador_total_ejecucion = 1;
						    	if(mantain_select){
							    	$scope.gridApi.grid.modifyRows(this.entidades_gridOptions.data);
							    	$scope.gridApi.selection.selectRow(this.entidades_gridOptions.data[this.selectedRow]);
							    }
						    	
						    }
						    this.showloading=false;
						    this.loadAttempted=true;
					 	}.bind(this), function errorCallback(response){
					 		
					 	}
					);
	    		}
	    		else{
	    			var modalInstance = $uibModal.open({
	    			      animation: $scope.animationsEnabled,
	    			      ariaLabelledBy: 'modal-title',
	    			      ariaDescribedBy: 'modal-body',
	    			      templateUrl: 'tablaMetas.html',
	    			      controller: 'tablametasController',
	    			      size: 'lg',
	    			      resolve: {
	    			        estructura: function () {
	    			          return {
	    			        	  ejercicio: this.ano,
	    			        	  entidad: this.entidad,
	    			        	  unidad_ejecutora: this.unidad_ejecutora,
	    			        	  programa: this.programa,
	    			        	  subprograma: this.subprograma,
	    			        	  proyecto: this.proyecto,
	    			        	  actividad: this.actividad,
	    			        	  obra: this.obra
	    			          };
	    			        }.bind(this)
	    			      }
	    			    });
	    		}
	    	};
			
			this.entidades_gridOptions = {
				    enableSorting: true,
				    enableFiltering: true,
				    enableRowSelection: true,
				    selectionRowHeaderWidth: 35,
				    multiSelect: false,
				    modifierKeysToMultiSelect: false,
				    noUnselect: false,
				    showColumnFooter: false,
				    enableRowHeaderSelection: false,
				    showGridFooter:true,
				    headerRowHeight: 50,
				    columnDefs: [
				      { name: 'goToLevel', width: 57, displayName: '   ', cellClass: 'grid-align-center', type: 'number', cellTemplate:'<button type="button" class="btn btn-primary" style="width: 30px; height: 19px; margin-top: 6px;" ng-click="grid.appScope.ejecucion.clickRow(row,$event)">&nbsp;</button>', 
					    	  enableFiltering: false, enableSorting: false, pinnedLeft: true },
				      { name: 'entidad', minWidth: 125, width: 125, displayName: 'Código', cellClass: 'grid-align-right', type: 'number' },
				      { name: 'nombre', displayName: 'Nombre' },
				      { name: 'porcentaje_ejecucion_financiera', maxWidth: 200, width: 200, cellFilter: 'number: 2', displayName: 'Ejecución Presupuestaria', enableFiltering: false,
						  cellClass: 'grid-align-right',  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.total_ejecucion_financiera | number:2 }}&nbsp;%</div>',
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number'},
				      { name: 'porcentaje_ejecucion_fisica', maxWidth: 200, width: 200, cellFilter: 'number: 2', displayName: 'Ejecución Física', enableFiltering: false,
						  cellClass: 'grid-align-right',  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.total_ejecucion_fisica | number:2 }}&nbsp;%</div>',
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number'},
					  { name: 'icono_ejecucion_anual', minWidth: 200, width: 200, displayName: 'Sémaforo de Ejecución Física', enableFiltering: false, enableSorting: false,
							    cellClass: 'grid-align-center', type: 'number', cellTemplate: '<div class="glyphicon glyphicon-certificate dot_{{ row.entity.icono_ejecucion_anual }}"></div>',
							    footerCellClass: 'grid-align-center',
							    footerCellTemplate: '<div class="ui-grid-cell-contents"><span class="glyphicon glyphicon-certificate dot_{{ grid.appScope.ejecucion.indicador_total_ejecucion }}"></span></div>'
							  }
						  
					  
				    ],
				    onRegisterApi: function( gridApi ) {
				      $scope.gridApi = gridApi;
				      if($routeParams.reset_grid=='gt1'){
				    	  this.saveState();
				      }
				      else{
				    	  var grid_data = { action: 'getstate', grid:'ejecucionfisica_1', t: (new Date()).getTime()};
				    	  $http.post('/SSaveGridState', grid_data).then(function(response){
					    	  if(response.data.success && response.data.state!='')
					    		  $scope.gridApi.saveState.restore( $scope, response.data.state);
					    	    
					    	  //$scope.gridApi.colResizable.on.columnSizeChanged($scope, this.saveState);
						      $scope.gridApi.core.on.columnVisibilityChanged($scope, this.saveState);
						      //$scope.gridApi.core.on.filterChanged($scope, this.saveState);
						      $scope.gridApi.core.on.sortChanged($scope, this.saveState);
						  }.bind(this));
				      };
					  $scope.gridApi.selection.on.rowSelectionChanged($scope,function(row){ 
					    	  var index_selected = this.entidades_gridOptions.data.indexOf(row.entity);
					    	  if(row.isSelected){
					    		  this.chartTitle = row.entity.nombre;
					    		  switch(this.level){
					    		  	case 2: this.unidad_ejecutora = row.entity.entidad; break;
					    		  	case 3: this.programa = row.entity.entidad; break;
					    		  	case 4: this.subprograma = row.entity.entidad; break;
					    		  	case 5: this.proyecto = row.entity.entidad; break;
					    		  	case 6: this.actividad = row.entity.entidad; break;
					    		  };
					    		  $http.post('/SEjecucionFisica', { action: 'ejecucionMensualizada', level: this.level, ejercicio: this.ano,
					    			  entidad: $routeParams.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
					    			  proyecto: this.proyecto, actividad: this.actividad, obra: this.obra, t: (new Date()).getTime() }).
					    			then(function(response){
									    if(response.data.success){
									    	var serie_presupuestaria=[];
									    	var serie_fisica=[];
									    	
									    	this.chartLabels=['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
									    	for(var i=1; i<13; i++){
									    		serie_presupuestaria.push(Math.round(response.data.ejecucion[0]['ejecucion_presupuestaria_'+i]*100)/100);
									    		serie_fisica.push(Math.round(response.data.ejecucion[0]['ejecucion_porcentaje_'+i]*100)/100);
									    	}
									    	this.chartData=[];
									    	this.chartData.push(serie_presupuestaria);
									    	this.chartData.push(serie_fisica);
									    }
									    this.showloading=false;
									    this.loadAttempted=true;
								 	}.bind(this), function errorCallback(response){
								 		
								 	}
					    			);
					    	  }
					    	  else{
					    		  switch(this.level){
					    		  	case 2: this.unidad_ejecutora = null; break;
					    		  	case 3: this.programa = null; break;
					    		  	case 4: this.subprograma = null; break;
					    		  	case 5: this.proyecto = null; break;
					    		  	case 6: this.actividad = null; break;
					    		  };
					    		  var data = { action: 'ejecucionMensualizada', level: this.level-1, ejercicio: this.ano,
											entidad: $routeParams.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
											proyecto: this.proyecto, actividad: this.actividad, obra: this.obra,
											t: (new Date()).getTime() };
					    		  $http.post('/SEjecucionFisica', data).
					    			then(function(response){
									    if(response.data.success){
									    	var serie_presupuestaria=[];
									    	var serie_fisica=[];
									    	
									    	for(var i=1; i<13; i++){
									    		serie_presupuestaria.push(Math.round(response.data.ejecucion[0]['ejecucion_presupuestaria_'+i]*100)/100);
									    		serie_fisica.push(Math.round(response.data.ejecucion[0]['ejecucion_porcentaje_'+i]*100)/100);
									    	}
									    	this.chartData=[];
									    	this.chartData.push(serie_presupuestaria);
									    	this.chartData.push(serie_fisica);
									    }
									    this.showloading=false;
									    this.loadAttempted=true;
								 	}.bind(this), function errorCallback(response){
								 		
								 	}
					    			);
					    	  }
					    }.bind(this));
				    }.bind(this)
				  };
			
			this.saveState=function() {
				var state = $scope.gridApi.saveState.save();
				var grid_data = { action: 'savestate', grid:'ejecucionfisica_1', state: JSON.stringify(state), t: (new Date()).getTime() }; 
				$http.post('/SSaveGridState', grid_data).then(function(response){
					
				});
			}
			
			this.chartOptions= {
					bezierCurve : false,
					datasetStrokeWidth : 6,
					pointDotRadius : 6,
					scaleLabel: function(label){ return  label.value+' %'; },
					//tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
					//multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>",
					legendTemplate: "<div class=\"chart-legend\"><ul class=\"line-legend\"><% for (var i=0; i<datasets.length; i++){%><li style=\"font-size: 12px;\"><div class=\"img-rounded\" style=\"float: left; margin-right: 5px; width: 15px; height: 15px; background-color:<%=datasets[i].strokeColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
					tooltips:{
						enabled:false
					},
					scales: {
		                yAxes: [{
		                    ticks: {
		                        min: 0,
		                        max: 100,
		                        beginAtZero: true,
		                        callback: function(label, index, labels) {
		                            return label+' %';
		                         }
		                    }
		                }],
		                xAxes: [{
		                    ticks: {
		                        autoSkip: false,
		                        maxRotation: 90,
		                        minRotation: 90,
		                        fontSize: 16,
		                        fontWeight: 'bold'
		                    }
		                }]
		            },
		            animation: {
		                onComplete: function() {
		                	if(this.config.type=='bar' || this.config.type=='line'){
			                	var ctx = this.chart.ctx;
			                	  ctx.font = Chart.helpers.fontString(16, 'bold', Chart.defaults.global.defaultFontFamily);
			                	  ctx.fillStyle = this.chart.config.options.defaultFontColor;
			                	  ctx.textAlign = 'center';
			                	  ctx.textBaseline = 'bottom';
			                	  this.data.datasets.forEach(function (dataset) {
			                	    for (var i = 0; i < dataset.data.length; i++) {
			                	      if(dataset.hidden === true && dataset._meta[Object.keys(dataset._meta)[0]].hidden !== false){ continue; }
			                	      var model = dataset._meta[Object.keys(dataset._meta)[0]].data[i]._model;
			                	      if(dataset.data[i] !== null){
			                	    	  ctx.save();
			                	    	  ctx.beginPath();
			                	    	  ctx.translate( model.x + 7, model.y + 35)
			                	    	  ctx.rotate(270*Math.PI/180);
			                	    	  ctx.fillText(dataset.data[i]+' %', 0, 0);
			                	    	  ctx.restore();
			                	      }
			                	    }
			                	  });
		                	}
		               }
		            },
		            hover: { animationDuration: 0 }
			}
			
			this.goLevel(2, false);
			
			this.compareEntidades=function(a,b){
				if(a.porcentaje_ejecucion_financiera < b.porcentaje_ejecucion_financiera)
					return 1;
				if(a.porcentaje_ejecucion_financiera > b.porcentaje_ejecucion_financiera)
					return -1;
				return 0;
			}
			
			this.clickRow = function(row, $event){
				switch(this.level){
					case 2:
						this.unidad_ejecutora = row.entity.entidad;
						this.unidad_ejecutora_nombre = row.entity.nombre; break;
					case 3:
						this.programa = row.entity.entidad;
						this.programa_nombre = row.entity.nombre; break;
					case 4:
						this.subprograma = row.entity.entidad;
						this.subprograma_nombre = row.entity.nombre; break;
					case 5:
						this.proyecto = row.entity.entidad;
						this.proyecto_nombre = row.entity.nombre; break;
					case 6:
						this.actividad = row.entity.entidad;
						this.obra = row.entity.obra;
						this.actividad_obra_nombre = row.entity.nombre; break;
					case 7:
						this.actividad = row.entity.entidad;
						this.obra = row.entity.obra;
						this.actividad_obra_nombre = row.entity.nombre; break;
				}
				if(this.level<6){
					this.showloading=true;
					this.loadAttempted=false;
					row.setSelected(false);
					$http.post('/SEjecucionFisica', { action: 'ejecucionMensualizada', level: this.level, ejercicio: this.ano, 
						entidad: $routeParams.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
						proyecto: this.proyecto, actividad: this.actividad, obra: this.obra, t: (new Date()).getTime() }).
	    			then(function(response){
					    if(response.data.success){
					    	var serie_presupuestaria=[];
					    	var serie_fisica=[];
					    	
					    	for(var i=1; i<13; i++){
					    		serie_presupuestaria.push(Math.round(response.data.ejecucion[0]['ejecucion_presupuestaria_'+i]*100)/100);
					    		serie_fisica.push(Math.round(response.data.ejecucion[0]['ejecucion_porcentaje_'+i]*100)/100);
					    	}
					    	this.chartData=[];
					    	this.chartData.push(serie_presupuestaria);
					    	this.chartData.push(serie_fisica);
					    }
					    this.showloading=false;
					    this.loadAttempted=true;
				 	}.bind(this), function errorCallback(response){
				 		
				 	}
	    			);
				}
				this.level = this.level<7 ? this.level+1 : this.level;
				this.goLevel(this.level, false);
				$event.stopPropagation();
			}
			
			$http.post('/SEjecucionFisica', { action: 'ejecucionMensualizada', level: this.level-1, ejercicio: this.ano, 
				entidad: $routeParams.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
				proyecto: this.proyecto, actividad: this.actividad, obra: this.obra, t: (new Date()).getTime() }).
			then(function(response){
			    if(response.data.success){
			    	var serie_presupuestaria=[];
			    	var serie_fisica=[];
			    	
			    	for(var i=1; i<13; i++){
			    		serie_presupuestaria.push(Math.round(response.data.ejecucion[0]['ejecucion_presupuestaria_'+i]*100)/100);
			    		serie_fisica.push(Math.round(response.data.ejecucion[0]['ejecucion_porcentaje_'+i]*100)/100);
			    	}
			    	this.chartData=[];
			    	this.chartData.push(serie_presupuestaria);
			    	this.chartData.push(serie_fisica);
			    }
			    this.showloading=false;
			    this.loadAttempted=true;
		 	}.bind(this), function errorCallback(response){
		 		
		 	}
			);
			
			this.anoClick=function(n_ano){
				this.ano = n_ano;
				this.goLevel(this.level<7 ? this.level : 6, false);
				
				$http.post('/SEjecucionFisica', { action: 'ejecucionMensualizada', level: this.level-1, ejercicio: this.ano, 
					entidad: $routeParams.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
					proyecto: this.proyecto, actividad: this.actividad, obra: this.obra, t: (new Date()).getTime() }).
				then(function(response){
				    if(response.data.success){
				    	var serie_presupuestaria=[];
				    	var serie_fisica=[];
				    	
				    	for(var i=1; i<13; i++){
				    		serie_presupuestaria.push(Math.round(response.data.ejecucion[0]['ejecucion_presupuestaria_'+i]*100)/100);
				    		serie_fisica.push(Math.round(response.data.ejecucion[0]['ejecucion_porcentaje_'+i]*100)/100);
				    	}
				    	this.chartData=[];
				    	this.chartData.push(serie_presupuestaria);
				    	this.chartData.push(serie_fisica);
				    }
				    this.showloading=false;
				    this.loadAttempted=true;
			 	}.bind(this), function errorCallback(response){
			 		
			 	}
				);
			}
			
		}
	]);

	angular.module('ejecucionfisicaentidadController').controller('tablametasController', function ($scope,$http, $uibModalInstance, estructura) {
		
		$scope.loading_metas=true;
		$scope.metas = [];

		$http.post('/SEjecucionFisica', { action: 'metas', ejercicio: estructura.ejercicio, entidad: estructura.entidad.id, ue: estructura.unidad_ejecutora,  
			programa: estructura.programa, subprograma: estructura.subprograma,
			proyecto: estructura.proyecto, actividad: estructura.actividad, obra: estructura.obra, t: (new Date()).getTime() }).
		then(function(response){
		    if(response.data.success){
		    	$scope.metas = response.data.metas;
		    }
		    $scope.loading_metas = false
	 	});
		
		
	  $scope.cerrar = function () {
	    $uibModalInstance.close();
	  };
	});