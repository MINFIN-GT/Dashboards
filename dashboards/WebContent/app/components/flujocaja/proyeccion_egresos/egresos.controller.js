/**
 * 
 */

angular.module('egresosController',['dashboards','ui.bootstrap.contextMenu','angucomplete-alt','treeGrid']).controller('egresosController',['$scope','$routeParams','$http','$interval',
	'$location','$timeout','$filter',
	   function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	
			me = this;
			
			me.con_regularizaciones="Con Regularizaciones";
			
			me.showloading = false;
			me.anio = moment().year();
			me.mes = moment().month()+1;
			
			me.anio_actual = me.anio;
			me.mes_actual = me.mes-1;
			
			me.chartLoaded=false;
			
			me.entidades = null;
			me.entidad =null;
			
			me.unidades_ejecutoras=null;
			me.unidad_ejecutora=null;
			
			me.chartType = 'line';
			me.chartColors = [ '#4D4D4D', '#FF0000', '#5DA5DA'];
			me.meses = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			me.chartSeries = ["Histórico","Pronósticos"];
			me.chartLabels = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			
			me.viewQuetzales=true;
			me.viewQuetzales_p=true;
			me.viewQuetzales_d=true;
			
			me.sindatos=false;
			
			me.numero_pronosticos=12;
			
			me.total_ejercicio=[];
			me.total_pronosticos=0.0;
			me.total_columnas=new Array(13);
			
			me.tree_data_original=[];
			me.tree_data=[];
			me.tree_cols=[{
				field: 'codigo',
				displayName: 'Código'
			},{
				field: 'nombre',
				displayName: 'Entidad'
			}];
			
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
			
			$http.post('/SFlujoCaja', { action: 'getEntidades', ejercicio: me.anio }).then(function(response){
			    if(response.data.success){
			    	me.entidades = response.data.entidades;
			    	me.entidades.splice(0,0,{
			    		id: 0,
			    		nombre_control: "Todas las Entidades",
			    	});
			    	$scope.$broadcast('angucomplete-alt:changeInput','entidad', 'Todas las Entidades');
			    	var selected={ originalObject: { entidad: 0 }};
			    	me.cambioEntidad(selected);
			    }
			});
			
			
			me.cambioEntidad=function(selected){
				if(selected!== undefined){
					me.sindatos=true;
					me.entidad = selected.originalObject.entidad;
					if(me.entidad>0){
						$http.post('/SFlujoCaja', { action: 'getUnidadesEjecutoras', ejercicio: me.anio, entidadId: me.entidad }).then(function(response){
						    if(response.data.success){
						    	me.unidades_ejecutoras = response.data.unidades_ejecutoras;
						    	if(me.unidades_ejecutoras==null)
						    		me.unidades_ejecutoras=[];
						    	me.unidades_ejecutoras.splice(0,0,{
						    		id: 0,
						    		nombre_control: "Todas las Unidades Ejecutoras",
						    	});
						    	$scope.$broadcast('angucomplete-alt:clearInput','unidad_ejecutora');
						    }
						});
					}
					else{
						me.showloading = true;
						$scope.$broadcast('angucomplete-alt:clearInput','unidad_ejecutora');
						$http.post('/SFlujoCaja', { action: 'getPronosticosEgresos', ejercicio: me.anio, entidadId: 0, unidad_ejecutoraId: 0, numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12,
								mes: me.mes, ejercicio: me.anio, con_regularizaciones: (me.con_regularizaciones=='Con Regularizaciones') ? 1 : 0 }).then(function(response){
						    if(response.data.success){
						    	me.chartData=[];
						    	me.chartLabels=[];
						    	var historicos = [];
						    	var pronosticos=[];
						    	me.total_pronosticos=0.0;
						    	historicos = response.data.historicos.slice(1);
						    	if(response.data.historicos.length>0){
						    		for(var i=0; i<12; i++)
							    		me.chartLabels.push(me.meses[i]+" "+me.anio);
							    }
						    	pronosticos = response.data.pronosticos;
						    	
						    	if(response.data.pronosticos.length>0){
						    		for(var i=0; i<response.data.pronosticos.length; i++){
						    			me.total_pronosticos+=response.data.pronosticos[i];
						    		}
						    	}
						    	me.tableData=[];
						    	if(me.anio==me.anio_actual){
						    		for(var i=0; i<12; i++){
						    			if(i>=me.mes_actual){
						    				historicos[i]=null;
						    				me.tableData[i] = pronosticos[i];
						    			}
						    			else{
						    				pronosticos[i]=null;
						    				me.tableData[i] = historicos[i];
						    			}
						    		}
						    		pronosticos[me.mes_actual-1]=historicos[me.mes_actual-1];
						    	}
						    	else
						    		me.tableData=pronosticos;
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
						    	me.historia = response.data.historia;
						    	me.total_ejercicio=[];
						    	for(var i=0; i<me.historia.length; i++){
						    		var total = 0.0;
						    		for(var j=1; j<me.historia[i].length; j++)
						    			total += me.historia[i][j];
						    		me.total_ejercicio.push(total);
						    	}
						    	$http.post('/SFlujoCaja', { action: 'getPronosticosEgresosTree', ejercicio: me.anio, entidadId: me.entidad, unidad_ejecutoraId: me.unidad_ejecutora, numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12,
										mes: me.mes, ejercicio: me.anio, con_regularizaciones: (me.con_regularizaciones=='Con Regularizaciones') ? 1 : 0 }).then(function(response){
											if(response.data.success){
												me.tree_data=response.data.arbol;
												if(me.tree_cols.length=2){
													for(var labels=0; labels<me.chartLabels.length; labels++){
														me.tree_cols.push({
															field: 'pronosticos',
															displayName: me.chartLabels[labels]
														});
													}
													me.tree_cols.push({
														field: 'pronosticos',
														displayName: 'Total'
													})
												}
												var total=0;
												for(var i=0; i<me.tree_data.length; i++){
													if(me.tree_data[i].children!=null){
														for(var j=0; j<me.tree_data[i].children.length; j++){
															for(var k=0; k<me.tree_data[i].children[j].pronosticos.length; k++){
																$filter('currency')(me.tree_data[i].children[j].pronosticos[k], 'Q ', 2);
																me.tree_data[i].pronosticos[k]+=me.tree_data[i].children[j].pronosticos[k];
															}
															me.tree_data[i].pronosticos.push(me.tree_data[i].pronosticos[k]);
														}
													}
												}
												me.tree_data_original = JSON.parse(JSON.stringify(me.tree_data));
												me.aplicarFormatoDetalle();
											}
									
								});
						    }
						    me.showloading = false;
						});
					}
				}
				else{
					me.entidad = null;
					me.unidad_ejecutora = null;
					me.unidades_ejecutoras=[];
					me.chartLoaded=false;
				}
			}
			
			me.aplicarFormatoDetalle=function(){
				var data_formateada = JSON.parse(JSON.stringify(me.tree_data_original));
				var total=0;
				for(var i=0; i<me.total_columnas.length; i++)
					me.total_columnas[i]=0;
				for(var i=0; i<data_formateada.length; i++){
					for(var j=0; j<data_formateada[i].children.length; j++){
						total=0;
						for(var k=0; k<data_formateada[i].children[j].pronosticos.length; k++){
							total+=(data_formateada[i].children[j].pronosticos[k] !=null ? data_formateada[i].children[j].pronosticos[k] : 0);
							data_formateada[i].children[j].pronosticos[k]= (me.viewQuetzales_d) ? 
									$filter('currency')(data_formateada[i].children[j].pronosticos[k], 'Q ', 2) :
										(data_formateada[i].children[j].pronosticos[k] !=null ? data_formateada[i].children[j].pronosticos[k].toFixed(2) : null);
						}
						data_formateada[i].children[j].pronosticos[data_formateada[i].children[j].pronosticos.length]=((me.viewQuetzales_d) ? 
								$filter('currency')(total.toFixed(2), 'Q ', 2) : total.toFixed(2));
					}
					total=0;
					for(var k=0; k<data_formateada[i].pronosticos.length; k++){
						total+=(data_formateada[i].pronosticos[k] !=null ? data_formateada[i].pronosticos[k] : 0);
						me.total_columnas[k]+=data_formateada[i].pronosticos[k];
						data_formateada[i].pronosticos[k]= (me.viewQuetzales_d) ? 
								$filter('currency')(data_formateada[i].pronosticos[k], 'Q ', 2) :
									(data_formateada[i].pronosticos[k]!=null ? data_formateada[i].pronosticos[k].toFixed(2) : null);
					}
					data_formateada[i].pronosticos = data_formateada[i].pronosticos.splice(0,12);
					data_formateada[i].pronosticos[data_formateada[i].pronosticos.length]=((me.viewQuetzales_d) ? 
							$filter('currency')(total.toFixed(2), 'Q ', 2) : total.toFixed(2));
				}
				total=0;
				var i=0;
				me.total_columnas = me.total_columnas.splice(0,data_formateada[0].pronosticos.length);
				for(i=0; i<me.total_columnas.length-1; i++){
					total+= me.total_columnas[i];
					me.total_columnas[i] = (me.viewQuetzales_d) ?  $filter('currency')(me.total_columnas[i].toFixed(2), 'Q ', 2) : 
						me.total_columnas[i].toFixed(2);
				}
				me.total_columnas[i]=(me.viewQuetzales_d) ?  $filter('currency')(total.toFixed(2), 'Q ', 2) : 
						total.toFixed(2);
				data_formateada.push({
					children:[],
					pronosticos: me.total_columnas,
					ejercicio: 2018,
					nombre: 'Total',
					codigo: ""
				});
				me.tree_data = data_formateada;
			}
			
			me.cambioUnidadEjecutora=function(selected){
				if(selected!== undefined){
					me.showloading = true;
					me.sindatos=true;
					me.unidad_ejecutora = selected.originalObject.unidad_ejecutora;
					$http.post('/SFlujoCaja', { action: 'getPronosticosEgresos', ejercicio: me.anio, entidadId: me.entidad, unidad_ejecutoraId: me.unidad_ejecutora, numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12,
							mes: me.mes, ejercicio: me.anio, con_regularizaciones: (me.con_regularizaciones=='Con Regularizaciones') ? 1 : 0  }).then(function(response){
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
					    	
					    	me.historia = response.data.historia;
					    	me.total_ejercicio=[];
					    	for(var i=0; i<me.historia.length; i++){
					    		var total = 0.0;
					    		for(var j=1; j<me.historia[i].length; j++)
					    			total += me.historia[i][j];
					    		me.total_ejercicio.push(total);
					    	}
					    	$http.post('/SFlujoCaja', { action: 'getPronosticosEgresosTree', ejercicio: me.anio, entidadId: me.entidad, unidad_ejecutoraId: me.unidad_ejecutora, 
									ejercicio: me.anio, con_regularizaciones: (me.con_regularizaciones=='Con Regularizaciones') ? 1 : 0  }).then(function(response){
										if(response.data.success){
											me.tree_data=response.data.arbol;
											if(me.tree_cols.length=2){
												for(var labels=0; labels<me.chartLabels.length; labels++){
													me.tree_cols.push({
														field: 'pronosticos',
														displayName: me.chartLabels[labels]
													});
												}
											}
											for(var i=0; i<me.tree_data.length; i++){
												if(me.tree_data[i].children!=null){
													for(var j=0; j<me.tree_data[i].children.length; j++){
														for(var k=0; k<me.tree_data[i].children[j].pronosticos.length; k++){
															$filter('currency')(me.tree_data[i].children[j].pronosticos[k], 'Q ', 2);
															me.tree_data[i].pronosticos[k]+=me.tree_data[i].children[j].pronosticos[k];
														}
													}
													for(var k=0; k<me.tree_data[i].pronosticos.length;k++)
														me.tree_data[i].pronosticos[k] = $filter('currency')(me.tree_data[i].pronosticos[k], 'Q ', 2);
												}
											}
											me.tree_data_original = JSON.parse(JSON.stringify(me.tree_data));
											me.aplicarFormatoDetalle();
										}
								
							});
					    }
						me.showloading = false;
					});
				}
				else
					me.unidad_ejecutora=null;
					me.chartLoaded=false;
			}
			
			me.blurEntidad=function(){
				if(me.entidad==null)
					$scope.$broadcast('angucomplete-alt:clearInput','entidad');
			}
			
			me.blurUnidadEjecutora=function(){
				if(me.unidad_ejecutora==null)
					$scope.$broadcast('angucomplete-alt:clearInput','unidad_ejecutora');
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
			
			me.mesClick=function(mes,load){
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
				if(load){
					if(me.unidad_ejecutora!=null){
						var selected={ originalObject: { unidad_ejecutora: me.unidad_ejecutora }};
				    	me.cambioUnidadEjecutora(selected);
					}
					else{
						var selected={ originalObject: { entidad: (me.entidad !=null ? me.entidad : 0 ) }};
				    	me.cambioEntidad(selected);
					}
				}
			}
			
			me.mesClick(me.mes,false);
			
			this.anoClick=function(ano){
				me.anio=ano;
				if(me.unidad_ejecutora!=null){
					var selected={ originalObject: { unidad_ejecutora: me.unidad_ejecutora }};
			    	me.cambioUnidadEjecutora(selected);
				}
				else{
					var selected={ originalObject: { entidad: (me.entidad !=null ? me.entidad : 0 ) }};
			    	me.cambioEntidad(selected);
				}
			}
			
			me.cambiarData=function(){
				me.mesClick(me.mes,true);
			}
			
			me.getEstiloDato=function(index){
				if(me.anio==me.anio_actual && index<me.mes_actual){
					return false;
				}
				return true;
			}
		}
	]);