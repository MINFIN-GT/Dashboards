/**
 * 
 */

angular.module('ingresosController',['dashboards','ui.bootstrap.contextMenu','angucomplete-alt','ngSanitize']).controller('ingresosController',['$scope','$routeParams','$http','$interval',
	'$location','$timeout','$filter','ivhTreeviewMgr','ivhTreeviewBfs','$sce','uiGridTreeViewConstants','uiGridConstants',
	   function($scope,$routeParams,$http, $interval, $location, $timeout, $filter,ivhTreeviewMgr,ivhTreeviewBfs,$sce,uiGridTreeViewConstants,
			   uiGridConstants){
	
			me = this;
			me.recursos=null;
			
			
			me.showloading = false;
			me.anio = moment().year();
			me.mes = moment().month()+1;
			
			me.chartLoaded=false;
			me.panel_recursos=false;
			
			me.recurso =null;
			me.tplFolderOpen = '<span class="twistie glyphicon glyphicon-menu-down"></span>';
			me.tplFolderClose = '<span class="twistie glyphicon glyphicon-menu-right"></span>';
			me.tplLeaf = '<span class="twistie" style="margin-left: 20px;"></span>';
			me.tplNode = this.tpl = [
			    ' <div title="{{trvw.label(node)}}">',
			      '<span ivh-treeview-toggle>',
			        '<span ivh-treeview-twistie></span>',
			      '</span>',
			      '<span ng-if="trvw.useCheckboxes()" ivh-treeview-checkbox>',
			      '</span>',
			      '<span class="ivh-treeview-node-label" ivh-treeview-toggle>',
			       '<span ng-bind-html="trvw.label(node)"></span>',
			      '</span>',
			      '<div ivh-treeview-children></div>',
			    '</div>'
			  ].join('\n');
			
			me.chartType = 'line';
			me.chartColors = [ '#4D4D4D', '#008000', '#5DA5DA'];
			me.meses = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			me.chartSeries = ["Histórico","Pronósticos"];
			me.numero = 12;
			me.chartLabels = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			
			me.viewQuetzales=true;
			me.viewQuetzales_p=true;
			
			me.sindatos=false;
			
			me.numero_pronosticos=12;
			me.total_ejercicio=[];
			me.total_pronosticos=0.0;
			
			me.nivel = 1;
			me.recursos_selected=[];
			me.auxiliares_selected=[];
			
			me.contextMenuOptions = function(row){
				var ret = [];
				if(this.level<2)
					ret.push(['Secciones', function(){ $scope.ingreso.clickGo(row,2);  }]);
				if(this.level<3)
					ret.push(['Grupos', function(){ $scope.ingreso.clickGo(row,3); }]);
				if(this.level<4)
					ret.push(['Recursos', function(){ $scope.ingreso.clickGo(row,4); }]);
				if(this.level<5)
					ret.push(['Auxiliares', function(){ $scope.ingreso.clickGo(row,5); } ]);
				return ret;
			}
			
			$scope.grid_columns=[
			      {  name: 'clase', width: 80, displayName: 'Clase', enableFiltering: false, cellClass: 'grid-align-right', type: 'number' },
			      { name: 'seccion', width: 80, displayName: 'Sección', enableFiltering: false, cellClass: 'grid-align-right', type: 'number',
			    	  cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP"><span >{{ row.entity.seccion>0 ? row.entity.seccion : "" }}</span></div>'   
			      },
			      { name: 'grupo', width: 80, displayName: 'Grupo', enableFiltering: false, cellClass: 'grid-align-right', type: 'number',
			    	  cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP"><span >{{ row.entity.grupo>0 ? row.entity.grupo : "" }}</span></div>'  
			      },
			      { name: 'recurso', width: 80, displayName: 'Recurso', enableFiltering: false,cellClass: 'grid-align-right', type: 'number', 
			    	  cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP"><span >{{ row.entity.recurso>0 ? row.entity.recurso : "" }}</span></div>'
			      },
			      { name: 'auxiliar', width: 80, displayName: 'Auxiliar', enableFiltering: false, cellClass: 'grid-align-right', type: 'number', 
			    	  cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP"><span >{{ row.entity.auxiliar>0 ? row.entity.auxiliar : "" }}</span></div>'
			      },
			      { name: 'nombre', width: 300, displayName: 'Nombre' }
			    ];
			
			me.chartOptions= {
					animation: {
			            duration: 0
			        },
					elements: {
				        line: {
				            tension: 0,
				            fill: false
				        },
				        point:{
				        	radius: 3
				        }
				    },
					datasetStrokeWidth : 6,
					legendTemplate: "<div class=\"chart-legend\"><ul class=\"line-legend\"><% for (var i=0; i<datasets.length; i++){%><li style=\"font-size: 12px;\"><div class=\"img-rounded\" style=\"float: left; margin-right: 5px; width: 15px; height: 15px; background-color:<%=datasets[i].strokeColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
					scales: {
		                xAxes: [{
		                	gridLines: {
			                    display: true,
			                    drawBorder: true,
			                    drawOnChartArea: false,
			                },
		                	ticks: {
		                        autoSkip: false,
		                        fontSize: 12,
		                        fontWeight: 'bold'
		                    },
		                    scaleLabel: {
                                display: true,
                                labelString: 'Mes'
                            }
		                }],
		                yAxes:[{
		                	gridLines: {
			                    display: true,
			                    drawBorder: true,
			                    drawOnChartArea: false,
			                },
			                scaleLabel: {
                                display: true,
                                labelString: 'Quetzales'
                            },
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
					},
					hover: { animationDuration: 0 }
			}
			
			me.chartData=[];
			
			me.chartDataset=[
				{
			        label: "Históricos",
			        type: 'line'
			      },
			      {
			        label: "Pronósticos",
			        type: 'line',
			        borderDash: [5,5]
			      }
			];
			
			$http.post('/SFlujoCaja', { action: 'getRecursosTree', ejercicio: me.anio, t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	me.recursos=response.data.recursos;
			    	ivhTreeviewMgr.selectAll(me.recursos);
			    	ivhTreeviewBfs(me.recursos, function(n) {
			    	    n.nombre_control = $sce.trustAsHtml(n.nombre_control);
			    	    for(var i=0; i<n.children.length; i++)
			    	    	n.children[i].parent=n;
			    	  });
			    	me.getPronosticos();
			    }
			});
			
			
			me.getPronosticos=function(){
				
					me.showloading = true;
					me.sindatos=true;
					me.recursos_selected=[];
					me.auxiliares_selected={};
					
					var num_recursos=0;
					
					ivhTreeviewBfs(me.recursos, function(n) {
			    	    if(n.selected){
				    	    if(n.auxiliar>-1){
				    	    	if(me.auxiliares_selected['r'+n.parent.recurso]==null || me.auxiliares_selected['r'+n.parent.recurso]===undefined)
				    	    		me.auxiliares_selected['r'+n.parent.recurso]=[];
				    	    	me.auxiliares_selected['r'+n.parent.recurso].push(n.auxiliar);
				    	    }
				    	    else{
				    	    	if(n.children===undefined || n.children==null || n.children.length==0){
				    	    		me.recursos_selected.push(n.recurso);
				    	    	}
				    	    }
			    	    }
			    	    if((n.children===undefined || n.children==null || n.children.length==0) && n.auxiliar==-1)
			    	    	num_recursos++;
			    	});
					console.log(num_recursos+' '+me.recursos_selected.length);
					$http.post('/SFlujoCaja', { action: 'getPronosticosIngresos', ejercicio: me.anio, 
							recursosIds: (num_recursos==me.recursos_selected.length) ? null : JSON.stringify(me.recursos_selected),
							auxiliaresIds: (num_recursos==me.recursos_selected.length) ? null : JSON.stringify(me.auxiliares_selected), numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12,
							mes: me.mes, ejercicio: me.anio  }).then(function(response){
					    if(response.data.success){
					    	var date = moment([me.anio, me.mes-1, 1]);
					    	date = date.subtract(12,'months');
					    	me.chartData=[];
					    	me.chartLabels=[];
					    	var historicos = [];
					    	var pronosticos=[];
					    	me.total_pronosticos=0.0;
					    	if(response.data.historicos.length>0){
					    		var historicos_año = response.data.historicos[0];
						    	for(var i=1; i<response.data.historicos.length; i++){
						    		historicos.push(response.data.historicos[i]);
						    		me.chartLabels.push(me.meses[date.month()]+" "+date.year());
						    		date=date.add(1,'months');
						    	}
						    }
					    	if(response.data.pronosticos.length>0){
					    		for(var i=0; i<historicos.length-1; i++)
					    			pronosticos.push(null);
					    		pronosticos.push(historicos[historicos.length-1]);
					    		for(var i=0; i<response.data.pronosticos.length; i++){
					    			pronosticos.push(response.data.pronosticos[i]);
					    			me.chartLabels.push(me.meses[date.month()]+" "+date.year());
					    			date=date.add(1,'months');
					    			me.total_pronosticos+=response.data.pronosticos[i];
					    		}
					    	}
					    	me.chartData.push(historicos);
					    	me.chartData.push(pronosticos);
					    	if(pronosticos.length>0){
					    		me.sindatos=false;
					    		me.chartLoaded=true;
					    	}
					    	else{
					    		me.sindatos=true;
					    		me.chartLoaded=false;
					    	}
					    	me.total_pronosticos=0.0;
					    	for(var i=0; i<pronosticos.length; i++)
					    		me.total_pronosticos+=pronosticos[i];
					    	me.historia = response.data.historia;
					    	me.total_ejercicio=[];
					    	for(var i=0; i<me.historia.length; i++){
					    		var total = 0.0;
					    		for(var j=0; j<me.historia[i].length; j++)
					    			total += me.historia[i][j];
					    		me.total_ejercicio.push(total);
					    	}
					    }
					    
					    me.panel_recursos=false;
					});
					
					$http.post('/SFlujoCaja', { action: 'getPronosticosIngresosDetalle', ejercicio: me.anio, 
						recursosIds: JSON.stringify(me.recursos_selected),
						auxiliaresIds: JSON.stringify(me.auxiliares_selected), numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12,
						mes: me.mes, ejercicio: me.anio  }).then(function(response){
							if(response.data.success){
								if(response.data.pronosticos[0]!=null){
									if($scope.grid_columns.length>5)
										$scope.grid_columns.splice(6, $scope.grid_columns.length-5);
									var date = moment([me.anio, me.mes-1, 1]);
									for(var i=0; i<response.data.pronosticos[0].pronosticos.length; i++){
										$scope.grid_columns.push({
											 name: 'p'+i, width: 150, field:'pronosticos['+i+']', cellFilter: 'currency:"Q " : 0', displayName: me.meses[date.month()]+" "+date.year(), enableFiltering: false,
												  enableSorting: false,
											 	  cellClass: 'grid-align-right', 
												  footerCellClass: 'grid-align-right', aggregationHideLabel: true, type: 'number',
												  treeAggregationType: uiGridTreeViewConstants.aggregation.SUM,
												  aggregationType: uiGridConstants.aggregationTypes.SUM, 
												  cellTemplate: '<div class="ui-grid-cell-contents" title="TOOLTIP"><span ng-hide="row.entity[\'$$\' + col.uid]">{{ row.entity.pronosticos['+i+'] | currency:"Q " : 0 }}</span><span ng-if="row.entity[\'$$\' + col.uid]"> {{row.entity["$$" + col.uid].value | currency:"Q " : 0 }}</span></div>',
												  footerCellTemplate: '<div class="ui-grid-cell-contents">{{ col.getAggregationValue().substring(6) | currency:"Q " : 0 }}</div>'
										});
										date=date.add(1,'months');
									}
									for(var i=0; i<response.data.pronosticos.length;i++){
										if(response.data.pronosticos[i].auxiliar==0)
											response.data.pronosticos[i].$$treeLevel = response.data.pronosticos[i].nivel-1;
									}
									me.recursos_gridOptions.data=response.data.pronosticos;
									me.showloading = false;
								}
							}
						});	
				
			}
			
			me.filtroQuetzales=function(value){
				if(me.viewQuetzales){
					return $filter('currency')(value, 'Q ', 2);
				}
				else 
					return value;
			}
			
			me.filtroQuetzalesP=function(value){
				if(me.viewQuetzales_p){
					return $filter('currency')(value, 'Q ', 2);
				}
				else 
					return value;
			}
			
			me.mesClick=function(mes, load){
				switch(mes){
					case 1: me.nmes="Enero"; break;
					case 2: me.nmes="Febrero"; break;
					case 3: me.nmes="Marzo"; break;
					case 4: me.nmes="Abril"; break;
					case 5: me.nmes="Mayo"; break;
					case 6: me.nmes="Junio"; break;
					case 7: me.nmes="Julio"; break;
					case 8: me.nmes="Agosto"; break;
					case 9: me.nmes="Septiembre"; break;
					case 10: me.nmes="Octubre"; break;
					case 11: me.nmes="Noviembre"; break;
					case 12: me.nmes="Diciembre"; break;
				}
				
				this.mes = mes;
			}
			
			me.mesClick(me.mes, false);
			
			me.anoClick=function(ano){
				me.anio=ano;
				
			}
			
			me.recursos_gridOptions = {
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
				    columnDefs: $scope.grid_columns,
				    onRegisterApi: function( gridApi ) {
				      $scope.gridApi = gridApi;
				      $scope.gridApi.grid.registerDataChangeCallback(function() {
				          //$scope.gridApi.treeBase.expandAllRows();
				    	 
				      });
				      $scope.gridApi.core.on.rowsRendered( $scope, function() {
				    	  
				      });
				      $scope.gridApi.selection.on.rowSelectionChanged($scope,function(row){ 
				       
				    	 
				      });
				      
				      /*if($routeParams.reset_grid=='gt1'){
				    	  this.saveState();
				      }
				      else{
				    	  var grid_data = { action: 'getstate', grid:'pronosticos_ingresos', t: (new Date()).getTime()};
				    	  $http.post('/SSaveGridState', grid_data).then(function(response){
					    	  if(response.data.success && response.data.state!='')
					    	  $scope.gridApi.saveState.restore( $scope, response.data.state);
					    	  $scope.gridApi.colMovable.on.columnPositionChanged($scope, this.saveState);
						      $scope.gridApi.colResizable.on.columnSizeChanged($scope, this.saveState);
						      $scope.gridApi.core.on.columnVisibilityChanged($scope, this.saveState);
						      $scope.gridApi.core.on.sortChanged($scope, this.saveState);
						  });
				      }*/
				    }
				  };
			
			me.saveState=function() {
				//var state = $scope.gridApi.saveState.save();
				//var grid_data = { action: 'savestate', grid:'pronosticos_ingresos', state: JSON.stringify(state), t: (new Date()).getTime() }; 
				//$http.post('/SSaveGridState', grid_data).then(function(response){
				//});
			}
			
			me.checkAll=function(selected){
				if(selected)
					ivhTreeviewMgr.selectAll(me.recursos);
				else
					ivhTreeviewMgr.deselectAll(me.recursos);
			}
			
			me.selectRecurso=function(ivhNode, ivhIsSelected){
				
			}
			
			
		}
	]);