/**
 * 
 */

angular.module('flujoController',['dashboards','ui.bootstrap.contextMenu','angucomplete-alt']).controller('flujoController',['$scope','$routeParams','$http','$interval',
	'$location','$timeout','$filter',
	   function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	
			me = this;
			
			me.showloading = false;
			me.anio = moment().year();
			me.mes = moment().month();
			
			me.cuentas_saldo=[];
			me.ingresos = [];
			me.egresos = [];
			me.egresos_contables = [];
			me.egresos_totales = [];
			me.caja = [];
			
			me.fecha_referencia='Fecha Real';
			
			me.chartLoaded=false;
			
			me.chartType = 'line';
			me.chartColors = [ '#4D4D4D', '#008000', '#008000', '#FF0000', '#FF0000'];
			me.meses = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			me.chartSeries = ["Caja","Ingresos","Pronósticos de Ingresos","Egresos","Pronósticos de Egresos"];
			me.numero = 12;
			me.chartLabels = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			
			me.viewQuetzales=true;
			
			me.sindatos=false;
			
			me.numero_pronosticos=12;
			me.total_ingresos=0.0;
			me.total_egresos=0.0;
			me.cuentas_saldo_total=0.0;
			
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
                                labelString: 'Millones de Quetzales'
                            },
                            ticks:{
								callback: function(value){
									return numeral(value/1000000).format('$ 0,0');
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
			        label: "Caja",
			        type: 'line'
			      },
			      {
			        label: "Ingresos",
			        type: 'line'
			      },
			      {
				        label: "Pronósticos de ingresos",
				        type: 'line',
				        borderDash: [5,5]
				  },
			      {
				    label: "Egresos",
				    type: 'line'
				  },
				  {
				        label: "Pronósticos de egresos",
				        type: 'line',
				        borderDash: [5,5]
				      }
			];
			
			me.loadFlujo=function(){
				$http.post('/SFlujoCaja',  { action: 'getPronosticosFlujo', ejercicio: me.anio,
					fecha_real: me.fecha_referencia=='Fecha Real' ? 1 : 0 }).then(function(response){
				    if(response.data.success){
				    	var mes=moment().month;
				    	pronosticos_egresos_totales = [];
				    	historicos_egresos_totales = [];
				    	me.cuentas_saldo = response.data.cuentas_saldo;
				    	var pronosticos_egresos = response.data.pronosticos_egresos;
				    	var historicos_egresos = response.data.historicos_egresos;
				    	var pronosticos_egresos_contables = response.data.pronosticos_egresos_contables;
				    	var historicos_egresos_contables = response.data.historicos_egresos_contables;
				    	var pronosticos_ingresos = response.data.pronosticos_ingresos;
				    	var historicos_ingresos = response.data.historicos_ingresos;
				    	
				    	
				    	me.caja=[];
				    	me.ingresos=[];
				    	me.egresos=[];
				    	me.egresos_contables=[];
				    	var saldo=0;
				    	for(var i=0; i<me.cuentas_saldo.length;i++)
				    		saldo+=me.cuentas_saldo[i].saldo_inicial;
				    	me.cuentas_saldo_total=saldo.toFixed(2);
				    	me.total_ingresos=0.0;
				    	me.total_egresos=0.0;
				    	me.total_egresos_contables=0.0;
				    	var anio_actual = moment().year();
				    	var mes_actual = moment().month();
				    	var historicos_ingresos_grafica=[];
				    	var pronosticos_ingresos_grafica=[];
				    	var historicos_egresos_grafica=[];
				    	var pronosticos_egresos_grafica=[];
				    	for(var i=0; i<12; i++){
				    		var egreso_mes = me.anio == anio_actual ? (i<mes_actual ? historicos_egresos[i+1] : ( pronosticos_egresos[i]!=null ? pronosticos_egresos[i] : 0)) :  ( pronosticos_egresos[i]!=null ? pronosticos_egresos[i] : 0);
				    		var egreso_contable_mes = me.anio == anio_actual ? (i<mes_actual ? historicos_egresos_contables[i+1] : ( pronosticos_egresos_contables[i]!=null ? pronosticos_egresos_contables[i] : 0)) : ( pronosticos_egresos_contables[i]!=null ? pronosticos_egresos_contables[i] : 0);
				    		var ingreso_mes =  me.anio == anio_actual ? (i<mes_actual ?  historicos_ingresos[i+1] : ( pronosticos_ingresos[i]!=null ? pronosticos_ingresos[i] : 0)) : ( pronosticos_ingresos[i]!=null ? pronosticos_ingresos[i] : 0);
				    		saldo = (ingreso_mes - egreso_mes - egreso_contable_mes) + saldo;
				    		me.caja.push(saldo);
				    		me.ingresos.push(ingreso_mes);
				    		me.egresos.push(egreso_mes);
				    		me.egresos_contables.push(egreso_contable_mes);
				    		me.total_ingresos+=ingreso_mes;
				    		me.total_egresos+=egreso_mes;
				    		me.total_egresos_contables+=egreso_contable_mes;
				    		me.egresos_totales.push(egreso_mes+egreso_contable_mes);
				    		historicos_egresos_totales.push(egreso_mes+egreso_contable_mes);
				    		if(me.anio==anio_actual){
				    			historicos_ingresos_grafica.push(i<mes_actual? historicos_ingresos[i+1] : null);
				    			pronosticos_ingresos_grafica.push(i>=mes_actual ? pronosticos_ingresos[i] : null);
				    			historicos_egresos_grafica.push(i<mes_actual ? (historicos_egresos[i+1]+historicos_egresos_contables[i+1]) : null);
				    			pronosticos_egresos_grafica.push(i>=mes_actual ? pronosticos_egresos[i]+pronosticos_egresos_contables[i] : null);
				    		}
				    		else{
				    			historicos_ingresos_grafica.push(null);
				    			pronosticos_ingresos_grafica.push(pronosticos_ingresos[i]);
				    			historicos_egresos_grafica.push(null);
				    			pronosticos_egresos_grafica.push(pronosticos_egresos[i]);
				    		}
				    	}
				    	
				    	me.chartData=[];
				    	me.chartData.push(me.caja);
				    	pronosticos_ingresos_grafica[mes_actual-1] = historicos_ingresos_grafica[mes_actual-1];
				    	me.chartData.push(historicos_ingresos_grafica);
				    	me.chartData.push(pronosticos_ingresos_grafica);
				    	
				    	pronosticos_egresos_grafica[mes_actual-1] = historicos_egresos_grafica[mes_actual-1];
				    	me.chartData.push(historicos_egresos_grafica);
				    	me.chartData.push(pronosticos_egresos_grafica);
				    	me.chartLoaded=true;
				    }
				});
			};
			
			me.loadFlujo();
			
			me.filtroQuetzales=function(value, transform){
				if(transform){
					return $filter('currency')(value, 'Q ', 2);
				}
				else 
					return value;
			}
			
			this.anoClick=function(ano){
				me.anio=ano;
				me.loadFlujo();
			}
			
			me.cambioFechaReal=function(){
				me.loadFlujo();
			}
			
		}
	]);