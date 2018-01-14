/**
 * 
 */

angular.module('flujoController',['dashboards','ui.bootstrap.contextMenu','angucomplete-alt']).controller('flujoController',['$scope','$routeParams','$http','$interval',
	'$location','$timeout','$filter',
	   function($scope,$routeParams,$http, $interval, $location, $timeout, $filter){
	
			me = this;
			
			me.showloading = false;
			me.anio = moment().year();
			
			me.ingresos = [];
			me.egresos = [];
			me.caja = [];
			
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
			
			$http.post('/SFlujoCaja',  { action: 'getPronosticosFlujo', ejercicio: me.anio, ejercicio: me.anio  }).then(function(response){
			    if(response.data.success){
			    	var pronosticos_egresos = response.data.pronosticos_egresos;
			    	if(pronosticos_egresos!=null && pronosticos_egresos.length<12){
			    		for(var i=pronosticos_egresos.length; i<12; i++)
			    			pronosticos_egresos.splice(0,0,null);
			    	}	
			    	var historicos_egresos = response.data.historicos_egresos;
			    	if(historicos_egresos!=null && historicos_egresos.length<12){
			    		for(var i=historicos_egresos.length; i<12; i++)
			    			historicos_egresos.push(null);
			    	}
			    	var pronosticos_ingresos = response.data.pronosticos_ingresos;
			    	if(pronosticos_ingresos!=null && pronosticos_ingresos.length<12){
			    		for(var i=pronosticos_ingresos.length; i<12; i++)
			    			pronosticos_ingresos.splice(0,0,null);
			    	}
			    	var historicos_ingresos = response.data.historicos_ingresos;
			    	if(historicos_ingresos!=null && historicos_ingresos.length<12){
			    		for(var i=historicos_ingresos.length; i<12; i++)
			    			historicos_ingresos.push(null);
			    	}
			    	me.caja=[];
			    	var saldo=0;
			    	me.total_ingresos=0.0;
			    	me.total_egresos=0.0;
			    	for(var i=0; i<12; i++){
			    		var egreso_mes = historicos_egresos[i]!=null ? historicos_egresos[i] : ( pronosticos_egresos[i]!=null ? pronosticos_egresos[i] : 0);
			    		var ingreso_mes = historicos_ingresos[i]!=null ? historicos_ingresos[i] : ( pronosticos_ingresos[i]!=null ? pronosticos_ingresos[i] : 0);
			    		saldo = ingreso_mes-egreso_mes+saldo;
			    		me.caja.push(saldo);
			    		me.ingresos.push(ingreso_mes);
			    		me.egresos.push(egreso_mes);
			    		me.total_ingresos+=ingreso_mes;
			    		me.total_egresos+=egreso_mes;
			    	}
			    	
			    	me.chartData=[];
			    	me.chartData.push(me.caja);
			    	me.chartData.push(historicos_ingresos);
			    	me.chartData.push(pronosticos_ingresos);
			    	me.chartData.push(historicos_egresos);
			    	me.chartData.push(pronosticos_egresos);
			    	me.chartLoaded=true;
			    }
			});
			
			me.filtroQuetzales=function(value, transform){
				if(transform){
					return $filter('currency')(value, 'Q ', 2);
				}
				else 
					return value;
			}
			
		}
	]);