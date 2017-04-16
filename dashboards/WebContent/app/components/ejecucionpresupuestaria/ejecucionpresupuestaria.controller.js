/**
 * 
 */

angular.module('ejecucionpresupuestariaController',['dashboards','ui.bootstrap.contextMenu']).controller('ejecucionpresupuestariaController',['$scope','$routeParams','$http','$interval',
	'$location','uiGridTreeViewConstants','uiGridConstants','i18nService','$timeout','uiGridGroupingConstants',
	   function($scope,$routeParams,$http, $interval, $location, uiGridTreeViewConstants, uiGridConstants, i18nService, $timeout, uiGridGroupingConstants){
			i18nService.setCurrentLang('es');
			
			this.tributarias = [11,12,13,14,15,16,21,22,29];
			this.titulo = "Entidades";
			
			this.showloading = false;
			this.nmonth = "Enero";
			this.chartType = "line";
			this.chartTitle = "Administración Central"
			this.selectedRow = null;
			this.row_selected = [];
			this.loadAttempted = false;
			this.level=1; //1 Entidades, 2 Unidad Ejecutora, 3 Renglon
			this.month=1;
			this.ano = moment().year();
			this.entidad = null;
			this.unidad_ejecutora = null;
			this.programa = null;
			this.subprograma = null;
			this.proyecto = null;
			this.actividad = null;
			this.obra = null;
			this.renglon = null;
			this.entidad_nombre = "";
			this.unidad_ejecutora_nombre = "";
			this.programa_nombre="";
			this.subprograma_nombre="";
			this.proyecto_nombre="";
			this.actividad_obra_nombre = "";
			this.renglon_nombre = "";
			
			this.fuentes = "Fuentes de Financiamiento";
			this.fuentes_descripcion ="Todas";
			this.fuentes_array=[];
			this.panel_fuentes;
			this.fuentes_loaded=false;
			
			this.grupos = "Grupos de Gasto";
			this.grupos_array=[];
			this.grupos_descripcion ="Todos";
			this.grupos_loaded=false;
			this.panel_grupos;
			this.todosgrupos = 1;
			
			this.lastupdate = "";
			
			var ano_actual = moment().year();
			this.chartLabels = [];
			this.chartSeries = [];
			this.chartData = [];
			this.chartData_start = [];
			
			this.months_labels = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			this.chartLabels_historico = this.months_labels.slice();
			this.chartSeries_historico = [];
			this.chartData_historico = [];
			this.chartData_historico_todos = [];
			this.chartData_historico_hasta_actual = [];
			
			this.total_ejecucion = 0;
			this.indicador_total_ejecucion = 0;
			
			this.ano1=0, this.ano2, this.ano3, this.ano4, this.ano5;
			this.aprobado=0, this.aprobado_acumulado=0;
	    	this.ejecutado=0, this.ejecutado_acumulado=0, this.vigente=0, this.asignado=0;
			
			
			$http.post('/SFuente', { ejercicio: 2016, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	this.fuentes_array = response.data.fuentes;
					}
				    if(!this.fuentes_loaded){
				    	$http.post('/SGrupoGasto', { ejercicio: 2016, t: (new Date()).getTime() }).then(function(response){
						    if(response.data.success){
						    	this.grupos_array = response.data.Grupos;
						    	this.mesClick(moment().month()+1);
						    	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
									    if(response.data.success){
									    	this.lastupdate = response.data.lastupdate;
										}
									}.bind(this)
						    	);
							}
					 	}.bind(this), function errorCallback(response){
					 		
					 	}
				    	);
				    }
				    this.fuentes_loaded=true;
			 	}.bind(this), function errorCallback(response){
			 		
			 	}
			);
			
			this.getFuentes=function(){
				var fuentes='';
				for(var fuente in this.fuentes_array)
					fuentes += (this.fuentes_array[fuente].checked) ? ',' + this.fuentes_array[fuente].fuente : '';
				fuentes = (fuentes.length>0) ? fuentes.substr(1) : '';
				return fuentes;
			}
			
			this.getGrupos=function(){
				var grupos='';
				for(var grupo in this.grupos_array)
					grupos += (this.grupos_array[grupo].checked) ? ',' + this.grupos_array[grupo].grupo : '';
				grupos = (grupos.length>0) ? grupos.substr(1) : '';
				return grupos;
			}
			
			this.goLevel=function(level, mantain_select){
				this.panel_fuentes=false;
				this.panel_grupos=false;
				this.level = level;
				this.showloading=true;
				this.loadAttempted=false;
				var data = { action: 'entidadesData', ejercicio: this.ano, nmes:this.nmonth, mes: this.month, level: this.level, 
						entidad: this.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
						proyecto: this.proyecto, actividad: this.actividad, obra: this.obra,
						fuentes: this.getFuentes(), 
						grupos: this.getGrupos(), todosgrupos: this.todosgrupos, t: (new Date()).getTime() };
				this.chartData=[];
	    		this.chartSeries=[];
	    		this.chartTitle = '';
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
	    		if(this.level<2){
	    			this.entidad = null;
	    			this.entidad_nombre = "";
	    		}
	    		$http.post('/SEjecucion', data).then(function(response){
					    if(response.data.success){
					    	//if(!mantain_select)
					    		this.row_selected=[];
					    	var ano1=0, ano2=0, ano3=0, ano4=0, ano5=0, ejecutado=0, vigente=0, ejecutado_acumulado=0;
					    	for(var i=0; i<response.data.entidades.length; i++){
					    		ano1+= response.data.entidades[i].ano1;
					    		ano2+= response.data.entidades[i].ano2;
					    		ano3+= response.data.entidades[i].ano3;
					    		ano4+= response.data.entidades[i].ano4;
					    		ano5+= response.data.entidades[i].ano5;
					    		ejecutado+= response.data.entidades[i].ejecutado;
					    		ejecutado_acumulado += response.data.entidades[i].ejecutado_acumulado;
					    		vigente+= response.data.entidades[i].vigente;
					    		if(response.data.entidades[i].parent!=null && this.level==7)
					    			response.data.entidades[i].$$treeLevel = response.data.entidades[i].parent;
					    	}
					    	this.entidades_gridOptions.data = response.data.entidades;
					    	this.total_ejecucion = (ejecutado_acumulado/vigente)*100;
					    	this.indicador_total_ejecucion = (this.total_ejecucion*100.00)/((100/12)*this.month);
					    	if(this.indicador_total_ejecucion<50)
					    		this.indicador_total_ejecucion = 4;
							else if(this.indicador_total_ejecucion<75)
								this.indicador_total_ejecucion = 2;
							else if(this.indicador_total_ejecucion<=100)
								this.indicador_total_ejecucion = 3;
							else
								this.indicador_total_ejecucion = 1;
					    	if(this.selectedRow==null || !mantain_select){
					    		switch(this.level){
						    		case 1:
								    	this.chartTitle = 'Administración Central';
								    	this.titulo = 'Entidades';
								    	break;
						    		case 2:
						    			this.chartTitle = this.entidad_nombre;
						    			this.titulo = 'Unidades Ejecutoras';
						    			break;
						    		case 3:
						    			this.chartTitle = this.unidad_ejecutora_nombre;
						    			this.titulo = 'Programas';
						    			break;
						    		case 4:
						    			this.chartTitle = this.programa_nombre;
						    			this.titulo = 'Subprogramas';
						    			break;
						    		case 5:
						    			this.chartTitle = this.subprograma_nombre;
						    			this.titulo = 'Proyectos';
						    			break;
						    		case 6:
						    			this.chartTitle = this.proyecto_nombre;
						    			this.titulo = 'Actividades / Obras';
						    			break;
						    		case 7:
						    			this.chartTitle = this.actividad_obra_nombre;
						    			this.titulo = 'Renglones';
						    			break;
					    		}
					    		this.selectedRow=null;
					    		this.chartData=[];
					    		this.chartSeries=[];
					    		this.chartData_start = [ ano1/1000000, ano2/1000000, ano3/1000000, ano4/1000000, ano5/1000000, ejecutado/1000000];
					    		this.chartData.push(this.chartData_start);
					    		this.chartSeries.push('Administración Central');
					    		
					    		$http.post('/SEjecucion', { action: 'historico_ejecucion', ejercicio: this.ano,
					    			entidad: this.entidad, ue: this.unidad_ejecutora, programa: this.programa, subprograma: this.subprograma,
									proyecto: this.proyecto, actividad: this.actividad, obra: this.obra,
									fuentes: this.getFuentes(), grupos: this.getGrupos(), todosgrupos: this.todosgrupos, 
									t: (new Date()).getTime() }).then(function(response){
										this.chartSeries_historico=[];
										this.chartData_historico=[];
										this.chartData_historico_hasta_actual=[];
										this.chartData.historico_todos=[];
										for(var i=0; i<response.data.historico.length; i++){
											this.chartSeries_historico.push(response.data.historico[i].ejercicio+"");
											if(i == response.data.historico.length-1){
												this.chartData_historico.push(response.data.historico[i].meses.slice(0,this.month));
												this.chartData_historico_todos.push(response.data.historico[i].meses.slice(0,this.month));
											}
											else{
												this.chartData_historico.push(response.data.historico[i].meses);
												this.chartData_historico_todos.push(response.data.historico[i].meses);
											}
											this.chartData_historico_hasta_actual.push(response.data.historico[i].meses.slice(0,this.month));
										}
								}.bind(this));
						    }
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
			
			this.entidades_gridOptions = {
				    enableSorting: true,
				    enableFiltering: true,
				    enableRowSelection: true,
				    selectionRowHeaderWidth: 35,
				    multiSelect: true,
				    modifierKeysToMultiSelect: false,
				    noUnselect: false,
				    showColumnFooter: true,
				    enableRowHeaderSelection: false,
				    showGridFooter:true,
				    headerRowHeight: 50,
				    columnDefs: [
				      { name: 'goToLevel', width: 57, displayName: '   ', cellClass: 'grid-align-center', type: 'number', cellTemplate:'<div class="btn-group" style="min-width: 60px; margin: 3px;"><label class="btn btn-primary" style="font-size: 5px; width: 30px; padding: 5px;" ng-click="grid.appScope.ejecucion.clickRow(row)">&nbsp;</label><label class="btn btn-success" style="font-size: 5px; width: 20px; padding: 5px;" context-menu="grid.appScope.ejecucion.contextMenuOptions(row)" context-menu-on="click">&nbsp;</label></div>', 
				    	  enableFiltering: false, enableSorting: false, pinnedLeft: true },    
				      { name: 'entidad', width: 100, displayName: 'Código', cellClass: 'grid-align-right', type: 'number', pinnedLeft:true },
				      { name: 'nombre', width: '30%', displayName: 'Nombre', pinnedLeft:true },
				      { name: 'ano1', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecución del mes / '+(this.ano-5), enableFiltering: false,
				    	  cellClass: 'grid-align-right',
				    	  headerCellClass: 'grid-align-center',
				    	  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
				    	  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ano1 | currency:"Q " : 0 }}</div>' },
				      { name: 'ano2', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecución del mes / '+(this.ano-4), enableFiltering: false,
				    	  cellClass: 'grid-align-right',
				    	  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
				    	  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ano2 | currency:"Q " : 0 }}</div>'},
				      { name: 'ano3', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecución del mes / '+(this.ano-3), enableFiltering: false,
					      cellClass: 'grid-align-right',
					      footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
					      footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ano3 | currency:"Q " : 0 }}</div>'},
					  { name: 'ano4', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecución del mes / '+(this.ano-2), enableFiltering: false,
						  cellClass: 'grid-align-right', 
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ano4 | currency:"Q " : 0 }}</div>'},
					  { name: 'ano5', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecución del mes / '+(this.ano-1), enableFiltering: false,
						  cellClass: 'grid-align-right', 
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ano5 | currency:"Q " : 0 }}</div>'},
				      { name: 'aprobado', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Cuota Aprobada del mes', enableFiltering: false,
						  cellClass: 'grid-align-right', 
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.aprobado | currency:"Q " : 0 }}</div>'},
					  { name: 'aprobado_acumulado', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Cuota Aprobada Acumulada', enableFiltering: false,
						  cellClass: 'grid-align-right', 
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.aprobado_acumulado | currency:"Q " : 0 }}</div>'},
					  { name: 'ejecutado', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecutado del mes', enableFiltering: false,
					      cellClass: 'grid-align-right', 
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ejecutado | currency:"Q " : 0 }}</div>'},
					  { name: 'ejecutado_acumulado', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Ejecutadado Acumulado', enableFiltering: false,
						  cellClass: 'grid-align-right', 
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.ejecutado_acumulado | currency:"Q " : 0 }}</div>'},
					  { name: 'asignado', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Asignado', enableFiltering: false,
						  cellClass: 'grid-align-right',  
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
						  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.asignado | currency:"Q " : 0 }}</div>' },
					  { name: 'vigente', width: 150, cellFilter: 'currency:"Q " : 0', displayName: 'Vigente hasta el mes', enableFiltering: false,
							  cellClass: 'grid-align-right',  
							  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
							  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.vigente | currency:"Q " : 0 }}</div>' },	  
					  { name: 'ejecucion_anual', width: 100,cellFilter: 'number: 2', displayName: '% Anual', enableFiltering: false,
						  cellClass: 'grid-align-right',  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ grid.appScope.ejecucion.total_ejecucion | number:2 }}&nbsp%</div>',
						  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number'},
					  { name: 'icono_ejecucion_anual', width: 100, displayName: 'Indicador', enableFiltering: false, enableSorting: false,
					    cellClass: 'grid-align-center', type: 'number', cellTemplate: '<span class="glyphicon glyphicon-certificate dot_{{ row.entity.icono_ejecucion_anual }}"></span>',
					    footerCellClass: 'grid-align-center',
					    footerCellTemplate: '<div class="ui-grid-cell-contents"><span class="glyphicon glyphicon-certificate dot_{{ grid.appScope.ejecucion.indicador_total_ejecucion }}"></span></div>'
					  }
				    ],
				    onRegisterApi: function( gridApi ) {
				      $scope.gridApi = gridApi;
				      $scope.gridApi.grid.registerDataChangeCallback(function() {
				          $scope.gridApi.treeBase.expandAllRows();
				      });
				      $scope.gridApi.core.on.rowsRendered( $scope, function() {
				    	  var rows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
				    	  if(rows.length>0){
				    		  for(var i=0; i<$scope.gridApi.grid.columns.length; i++){
				    			  switch($scope.gridApi.grid.columns[i].field){
				    			  	case 'ano1': $scope.gridApi.grid.columns[i].displayName= 'Ejecución del mes / '+(this.grid.appScope.ejecucion.ano-5); break;
				    			  	case 'ano2': $scope.gridApi.grid.columns[i].displayName= 'Ejecución del mes / '+(this.grid.appScope.ejecucion.ano-4); break;
				    			  	case 'ano3': $scope.gridApi.grid.columns[i].displayName= 'Ejecución del mes / '+(this.grid.appScope.ejecucion.ano-3); break;
				    			  	case 'ano4': $scope.gridApi.grid.columns[i].displayName= 'Ejecución del mes / '+(this.grid.appScope.ejecucion.ano-2); break;
				    			  	case 'ano5': $scope.gridApi.grid.columns[i].displayName= 'Ejecución del mes / '+(this.grid.appScope.ejecucion.ano-1); break;
				    			  }
				    		  }
				    		  this.grid.appScope.ejecucion.ano1=0, this.grid.appScope.ejecucion.ano2=0, this.grid.appScope.ejecucion.ano3=0, this.grid.appScope.ejecucion.ano4=0, this.grid.appScope.ejecucion.ano5=0;
					    	  this.grid.appScope.ejecucion.aprobado=0, this.grid.appScope.ejecucion.aprobado_acumulado=0;
					    	  this.grid.appScope.ejecucion.ejecutado=0, this.grid.appScope.ejecucion.ejecutado_acumulado=0, this.grid.appScope.ejecucion.vigente=0, this.grid.appScope.ejecucion.asignado=0;
					    	  for(var i=0; i<rows.length; i++){
					    		  if(rows[i].entity.parent==null || this.grid.appScope.ejecucion.level<7){
					    			  this.grid.appScope.ejecucion.ano1 += rows[i].entity.ano1;
					    			  this.grid.appScope.ejecucion.ano2 += rows[i].entity.ano2;
					    			  this.grid.appScope.ejecucion.ano3 += rows[i].entity.ano3;
					    			  this.grid.appScope.ejecucion.ano4 += rows[i].entity.ano4;
					    			  this.grid.appScope.ejecucion.ano5 += rows[i].entity.ano5;
					    			  this.grid.appScope.ejecucion.aprobado += rows[i].entity.aprobado;
					    			  this.grid.appScope.ejecucion.aprobado_acumulado += rows[i].entity.aprobado_acumulado;
					    			  this.grid.appScope.ejecucion.ejecutado += rows[i].entity.ejecutado;
					    			  this.grid.appScope.ejecucion.ejecutado_acumulado += rows[i].entity.ejecutado_acumulado;
					    			  this.grid.appScope.ejecucion.asignado += rows[i].entity.asignado;
					    			  this.grid.appScope.ejecucion.vigente += rows[i].entity.vigente;
					    		  }
					    	  }
					    	  this.grid.appScope.ejecucion.total_ejecucion = (this.grid.appScope.ejecucion.ejecutado_acumulado/this.grid.appScope.ejecucion.vigente)*100;
					    	  this.grid.appScope.ejecucion.indicador_total_ejecucion = (this.grid.appScope.ejecucion.total_ejecucion*100.00)/((100/12)*this.grid.appScope.ejecucion.month);
						    	if(this.grid.appScope.ejecucion.indicador_total_ejecucion<50)
						    		this.grid.appScope.ejecucion.indicador_total_ejecucion = 4;
								else if(this.grid.appScope.ejecucion.indicador_total_ejecucion<75)
									this.grid.appScope.ejecucion.indicador_total_ejecucion = 2;
								else if(this.grid.appScope.ejecucion.indicador_total_ejecucion<=100)
									this.grid.appScope.ejecucion.indicador_total_ejecucion = 3;
								else
									this.grid.appScope.ejecucion.indicador_total_ejecucion = 1;
					      }
				    	});
				      $scope.gridApi.selection.on.rowSelectionChanged($scope,function(row){ 
				    	  var index_selected = this.entidades_gridOptions.data.indexOf(row.entity);
				    	  if(row.isSelected){
				    		 this.row_selected.push(index_selected);
					    	 
					    		  this.selectedRow = this.entidades_gridOptions.data.indexOf(row.entity);
						    	  if(this.row_selected.length==1){
						    		  this.chartData=[];
						    		  this.chartSeries=[];
						    		  this.chartTitle = row.entity.nombre;
						    	  }
						    	  else{
						    		  switch(this.level){
						    		  	case 1: this.chartTitle = 'Entidades'; this.titulo= 'Entidades'; break;
						    		  	case 2: this.chartTitle = 'Unidades Ejecutoras'; this.titulo='Unidades Ejecutoras'; break;
						    		  	case 3: this.chartTitle = 'Programas'; this.titulo='Programas'; break;
						    		  	case 4: this.chartTitle = 'Subprogramas'; this.titulo = 'Subprogramas'; break;
						    		  	case 5: this.chartTitle = 'Proyectos'; this.titulo='Proyectos'; break;
						    		  	case 6: this.chartTitle = 'Actividades / Obras'; this.titulo='Actividades / Obras'; break;
						    		  	case 7: this.chartTitle = 'Renglones'; this.titulo='Renglones'; break;
						    		  }
						    	  }
						    	  this.chartData.push([row.entity.ano1/1000000, row.entity.ano2/1000000, row.entity.ano3/1000000, row.entity.ano4/1000000, row.entity.ano5/1000000, row.entity.ejecutado/1000000]);
						    	  this.chartSeries.push(row.entity.nombre);
					     }
				    	 else{
				    		 var pos = this.row_selected.indexOf(index_selected);
				    		 this.row_selected.splice(pos,1);
				    		 this.chartData.splice(pos,1);
				    		 this.chartSeries.splice(pos,1);
				    		 switch(this.row_selected.length){
				    		 	case 0:
				    		 		this.chartData.push(this.chartData_start);
				    		 		switch(this.level){
						    		  	case 1: this.chartTitle = 'Entidades'; this.titulo= 'Entidades'; break;
						    		  	case 2: this.chartTitle = 'Unidades Ejecutoras'; this.titulo='Unidades Ejecutoras'; break;
						    		  	case 3: this.chartTitle = 'Programas'; this.titulo='Programas'; break;
						    		  	case 4: this.chartTitle = 'Subprogramas'; this.titulo = 'Subprogramas'; break;
						    		  	case 5: this.chartTitle = 'Proyectos'; this.titulo='Proyectos'; break;
						    		  	case 6: this.chartTitle = 'Actividades / Obras'; this.titulo='Actividades / Obras'; break;
						    		  	case 7: this.chartTitle = 'Renglones'; this.titulo='Renglones'; break;
				    		 		}
				    		 		this.chartSeries[0] = 'Administración Central';
				    		 		this.chartTitle = this.chartSeries[0];
				    		 		break;
				    		 	case 1:
				    		 		this.chartTitle = this.chartSeries[0];
					    			break;
					    		default:
					    			switch(this.level){
						    		  	case 1: this.chartTitle = 'Entidades'; this.titulo= 'Entidades'; break;
						    		  	case 2: this.chartTitle = 'Unidades Ejecutoras'; this.titulo='Unidades Ejecutoras'; break;
						    		  	case 3: this.chartTitle = 'Programas'; this.titulo='Programas'; break;
						    		  	case 4: this.chartTitle = 'Subprogramas'; this.titulo = 'Subprogramas'; break;
						    		  	case 5: this.chartTitle = 'Proyectos'; this.titulo='Proyectos'; break;
						    		  	case 6: this.chartTitle = 'Actividades / Obras'; this.titulo='Actividades / Obras'; break;
						    		  	case 7: this.chartTitle = 'Renglones'; this.titulo='Renglones'; break;
					    			}
					    			break;
				    		 }
				    		 
				    	 }
				    	 
				      }.bind(this));
				      
				      if($routeParams.reset_grid=='gt1'){
				    	  this.saveState();
				      }
				      else{
				    	  var grid_data = { action: 'getstate', grid:'ejecucionpresupuestaria_1', t: (new Date()).getTime()};
				    	  $http.post('/SSaveGridState', grid_data).then(function(response){
					    	  if(response.data.success && response.data.state!='')
					    	  $scope.gridApi.saveState.restore( $scope, response.data.state);
					    	  $scope.gridApi.colMovable.on.columnPositionChanged($scope, this.saveState);
						      $scope.gridApi.colResizable.on.columnSizeChanged($scope, this.saveState);
						      $scope.gridApi.core.on.columnVisibilityChanged($scope, this.saveState);
						      //$scope.gridApi.core.on.filterChanged($scope, this.saveState);
						      $scope.gridApi.core.on.sortChanged($scope, this.saveState);
						  }.bind(this));
				      }
				    }.bind(this)
				  };
			
			this.saveState=function() {
				var state = $scope.gridApi.saveState.save();
				var grid_data = { action: 'savestate', grid:'ejecucionpresupuestaria_1', state: JSON.stringify(state), t: (new Date()).getTime() }; 
				$http.post('/SSaveGridState', grid_data).then(function(response){
					
				});
			}
			
			this.mesClick=function(mes){
				switch(mes){
					case 1: this.nmonth="Enero"; break;
					case 2: this.nmonth="Febrero"; break;
					case 3: this.nmonth="Marzo"; break;
					case 4: this.nmonth="Abril"; break;
					case 5: this.nmonth="Mayo"; break;
					case 6: this.nmonth="Junio"; break;
					case 7: this.nmonth="Julio"; break;
					case 8: this.nmonth="Agosto"; break;
					case 9: this.nmonth="Septiembre"; break;
					case 10: this.nmonth="Octubre"; break;
					case 11: this.nmonth="Noviembre"; break;
					case 12: this.nmonth="Diciembre"; break;
				}
				
				this.month = mes;
				this.goLevel(this.level, true);
			}
			
			this.anoClick=function(ano){
				this.ano=ano;
				this.goLevel(this.level, true);
			}
			
			this.chartOptions= {
					elements: {
				        line: {
				            tension: 0
				        },
				        point:{
				        	radius: 5
				        }
				    },
					scales: {
						yAxes:[{
							ticks:{
								callback: function(value){
									return numeral(value).format('$ 0,0');
								}
							}	
						}]
					},
					tooltips:{
							callbacks:{
								label:function(tooltipItem, data){
									return numeral(tooltipItem.yLabel).format('$ 0,0.00');
								}
							}
					},
					legend:{
						display: true,
						position: 'bottom'
					}
			};
			
			this.chartOptions_historico= {
					elements: {
				        line: {
				            tension: 0,
				            fill: false
				        },
				        point:{
				        	radius: 5
				        }
				    },
					scales: {
						yAxes:[{
							ticks:{
								callback: function(value){
									return numeral(value).format('$ 0,0');
								}
							}	
						}]
					},
					tooltips:{
							callbacks:{
								label:function(tooltipItem, data){
									return numeral(tooltipItem.yLabel).format('$ 0,0.00');
								}
							}
					},
					legend:{
						display: true,
						position: 'bottom'
					}
			};
			
			for(var i=0; i<6; i++)
	    		this.chartLabels.push((ano_actual-5+i)+'');
			
			this.checkAll=function(check){
				for(var fuente in this.fuentes_array)
					this.fuentes_array[fuente].checked=check;
				this.fuentes_descripcion = (check) ?  "Todas" : "Ninguna";
			}.bind(this);
			
			this.checkTributarias=function(){
				for(var fuente in this.fuentes_array){
					this.fuentes_array[fuente].checked=this.tributarias.indexOf(this.fuentes_array[fuente].fuente)>-1;
				}
				this.fuentes_descripcion = "Tributarias";
			}
			
			this.changeFuentes=function(){
				var cont=0;
				var ntributarias=0;
				for(var fuente in this.fuentes_array){
					cont = this.fuentes_array[fuente].checked ? cont+1 : cont;
					ntributarias = (this.fuentes_array[fuente].checked && this.tributarias.indexOf(this.fuentes_array[fuente].fuente)>-1) ? ntributarias+1 : ntributarias;
				}
				if(cont==this.fuentes_array.length)
					this.fuentes_descripcion = "Todas";
				else if(cont==0)
					this.fuentes_descripcion = "Ninguna";
				else if(cont==ntributarias && ntributarias == this.tributarias.length){
					this.fuentes_descripcion = "Tributarias";
				}
				else{
					var tfuentes = '';
					for(var fuente in this.fuentes_array){
						if(this.fuentes_array[fuente].checked)
							tfuentes = tfuentes + ',' + this.fuentes_array[fuente].fuente;
					}	
					this.fuentes_descripcion = tfuentes.substring(1);
				}
			}
			
			this.changeGrupos=function(){
				var cont=0;
				for(var grupo in this.grupos_array){
					cont = this.grupos_array[grupo].checked ? cont+1 : cont;
				}
				if(cont==this.grupos_array.length){
					this.grupos_descripcion = "Todos";
					this.todosgrupos = 1;
				}
				else if(cont==0){
					this.grupos_descripcion = "Ninguno";
					this.todosgrupos = 0;
				}
				else{
					var tgrupos = '';
					for(var grupo in this.grupos_array){
						if(this.grupos_array[grupo].checked)
							tgrupos = tgrupos + ',' + this.grupos_array[grupo].grupo;
					}	
					this.grupos_descripcion = tgrupos.substring(1);
					this.todosgrupos = 0;
				}
			}
			
			this.checkGruposAll=function(check){
				for(var fuente in this.grupos_array)
					this.grupos_array[fuente].checked=check;
				this.grupos_descripcion = (check) ?  "Todos" : "Ninguno";
			}.bind(this);
			
			this.exportXLS=function(){
				var data = { action: 'entidadesData', ejercicio: this.ano,nmes:this.nmonth, mes: this.month, level: this.level, 
						entidad: this.entidad, ue: this.unidad_ejecutora, fuentes: this.getFuentes(), 
						grupos: this.getGrupos(), todosgrupos: this.todosgrupos, excel: 1, t: (new Date()).getTime() };
				$http.post('/SEjecucion', data).then(function successCallback(response) {
					var anchor = angular.element('<a/>');
				    anchor.attr({
				         href: 'data:application/ms-excel;base64,' + response.data,
				         target: '_blank',
				         download: 'Ejecución_Presupuestaria.xls'
				     })[0].click();
				  }.bind(this), function errorCallback(response){
				 		
				 	}
				 );
			}
			
			this.clickRow = function(row){
				this.showloading=true;
				this.loadAttempted=false;
				row.setSelected(false);
				switch(this.level){
					case 1:	this.entidad = row.entity.entidad;
							this.entidad_nombre = row.entity.nombre; break;
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
				}
				this.level = this.level<7 ? this.level+1 : this.level;
				this.goLevel(this.level, false);
			}
			
			this.clickGo = function(row, toLevel){
				this.showloading=true;
				this.loadAttempted=false;
				row.setSelected(false);
				switch(this.level){
					case 1:	this.entidad = row.entity.entidad;
							this.entidad_nombre = row.entity.nombre; break;
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
				}
				this.level = toLevel;
				this.goLevel(this.level, false);
			}
			
			this.contextMenuOptions = function(row){
				var ret = [];
				if(this.level<2)
					ret.push(['Unidades Ejecutoras', function(){ $scope.ejecucion.clickGo(row,2);  }]);
				if(this.level<3)
					ret.push(['Programas', function(){ $scope.ejecucion.clickGo(row,3); }]);
				if(this.level<4)
					ret.push(['Subprogramas', function(){ $scope.ejecucion.clickGo(row,4); }]);
				if(this.level<5)
					ret.push(['Proyectos', function(){ $scope.ejecucion.clickGo(row,5); } ]);
				if(this.level<6)
					ret.push(['Actividades / Obras', function(){ $scope.ejecucion.clickGo(row,6); } ]);
				if(this.level<7)
					ret.push(['Renglones', function(){ $scope.ejecucion.clickGo(row,7); }]);
				return ret;
			}
			
			this.resetView = function(){
				if($location.path()=='/dashboards/ejecucionpresupuestaria/gt1')
					$route.reload();
				else
					$location.path('/dashboards/ejecucionpresupuestaria/gt1');
			}
			
			this.chartHistoricoHastaMesActual=function(view_all){
				if(view_all){
					this.chartData_historico = this.chartData_historico_todos.slice();
					this.chartLabels_historico = this.months_labels.slice();
				}
				else{
					this.chartData_historico = this.chartData_historico_hasta_actual.slice();
					this.chartLabels_historico = [];
					for(var i=0; i<this.month; i++){
						this.chartLabels_historico.push(this.months_labels[i]);
					}
				}
			}
		}
	]);