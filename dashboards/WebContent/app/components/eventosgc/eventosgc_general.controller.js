/**
 * 
 */

angular.module('eventosGCController',['dashboards','ui.bootstrap.contextMenu']).controller('eventosGCController',['$scope','$routeParams','$http','$interval',
	'$location','uiGridTreeViewConstants','uiGridConstants','i18nService','$timeout','uiGridGroupingConstants','$filter',
	   function($scope,$routeParams,$http, $interval, $location, uiGridTreeViewConstants, uiGridConstants, i18nService, $timeout, uiGridGroupingConstants,$filter){
			i18nService.setCurrentLang('es');
			
			this.showloading = false;
			this.ano = moment().year();
			this.mes = moment().month();
			this.nmes = "Enero";
			
			this.chartType = "bar";
			this.chartLabels = [];
			this.chartSeries = [];
			this.chartData = [];
			this.chartData_start = [];
			this.chart_colors = [ '#4D4D4D', '#F15854', '#5DA5DA'];
			this.chart_title = 'Eventos de Guatecompras acumulados hasta el mes';
			this.chartDataset=[{
				borderWidth: 1	
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			}];
			this.chart_acumulado=true;
			
			this.chartType_anual = "line";
			this.chartLabels_anual = [];
			this.chartSeries_anual = [];
			this.chartData_anual = [];
			//this.chart_colors_anual = [ '#97bbcd', '#dcdcdc', '#f7464a','#46bfbd','#fdb45c','#949fb1','#4d5360','#97bbcd', '#dcdcdc', '#f7464a','#46bfbd','#fdb45c','#949fb1','#4d5360','#97bbcd', '#dcdcdc', '#f7464a','#46bfbd','#fdb45c','#949fb1','#4d5360'];
			this.chart_colors_anual = [ '#4D4D4D', '#5DA5DA', '#FAA43A','#60BD68','#F17CB0','#B2912F','#B276B2','#DECF3F', '#F15854', '#4D4D4D', '#5DA5DA', '#FAA43A','#60BD68','#F17CB0','#B2912F','#B276B2','#DECF3F', '#F15854'];
			this.chartDataset_anual=[{
				borderWidth: 1	
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			},{
				borderWidth: 1
			}];
			this.chart_acumulado_anual=true;
			
			this.chartType_modalidad = "pie";
			this.chartLabels_modalidad = [];
			this.chartData_modalidad = [];
			this.chart_colors_modalidad = [ '#5DA5DA', '#FAA43A','#60BD68','#F17CB0','#B2912F','#B276B2','#DECF3F', '#F15854', '#4D4D4D', '#5DA5DA', '#FAA43A','#60BD68','#F17CB0','#B2912F','#B276B2','#DECF3F', '#F15854'];
			this.chart_acumulado_modalidad=true;
			
			this.chartType_estado = "pie";
			this.chartLabels_estado = [];
			this.chartData_estado = [];
			this.chart_colors_estado = ['#5DA5DA', '#FAA43A','#60BD68','#F17CB0','#B2912F','#B276B2','#DECF3F', '#F15854', '#4D4D4D', '#5DA5DA', '#FAA43A','#60BD68','#F17CB0','#B2912F','#B276B2','#DECF3F', '#F15854'];
			this.chart_acumulado_estado=true;
			
			this.meses = ["Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"];
			
			this.lastupdate = "";
			
			this.chartOptions= {
					bezierCurve : false,
					legendTemplate: "<div class=\"chart-legend\"><ul class=\"line-legend\"><% for (var i=0; i<datasets.length; i++){%><li style=\"font-size: 12px;\"><div class=\"img-rounded\" style=\"float: left; margin-right: 5px; width: 15px; height: 15px; background-color:<%=datasets[i].strokeColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
					borderWidth: 1,
					scales: {
		                xAxes: [{
		                	gridLines: {
			                    display: true,
			                    drawBorder: true,
			                    drawOnChartArea: false,
			                },
		                	ticks: {
		                        autoSkip: false,
		                        maxRotation: 90,
		                        minRotation: 90,
		                        fontSize: 12,
		                        fontWeight: 'bold'
		                    },
		                    scaleLabel: {
                                display: true,
                                labelString: 'Entidades'
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
                                labelString: 'Eventos'
                            }
		                }]
		            },
		            legend:{
						display: true,
						position: 'top'
					},
					tooltips:{
						enabled:false
					},
					animation: {
		                onComplete: function() {
		                	if(this.config.type=='bar' || this.config.type=='line'){
			                	var ctx = this.chart.ctx;
			                	  ctx.font = Chart.helpers.fontString(12, 'normal', Chart.defaults.global.defaultFontFamily);
			                	  ctx.fillStyle = this.chart.config.options.defaultFontColor;
			                	  ctx.textAlign = 'center';
			                	  ctx.textBaseline = 'bottom';
			                	  this.data.datasets.forEach(function (dataset) {
			                	    for (var i = 0; i < dataset.data.length; i++) {
			                	      if(dataset._meta[Object.keys(dataset._meta)[0]].hidden){ 
			                	    	  continue; 
			                	      }
			                	      var model = dataset._meta[Object.keys(dataset._meta)[0]].data[i]._model;
			                	      if(dataset.data[i] !== null){
			                	    	  ctx.save();
			                	    	  ctx.beginPath();
			                	    	  ctx.translate( model.x + 7, model.y - 20)
			                	    	  ctx.rotate(270*Math.PI/180);
			                	    	  ctx.fillText($filter('number')(dataset.data[i], 0), 0, 0);
			                	    	  ctx.restore();
			                	      }
			                	    }
			                	  });
		                	}
		               }
		            },
		            hover: { animationDuration: 0 }
			}
			
			this.chartOptions_anual= {
					elements: {
				        line: {
				            tension: 0,
				            fill: false
				        },
				        point:{
				        	radius: 5
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
                                labelString: 'Eventos'
                            }
		                }]
		            },
		            legend:{
						display: true,
						position: 'right'
					},
					hover: { animationDuration: 0 }
			}
			
			this.chartOptions_modalidad= {
					datasetStrokeWidth : 6,
					legendTemplate: "<div class=\"chart-legend\"><ul class=\"line-legend\"><% for (var i=0; i<datasets.length; i++){%><li style=\"font-size: 12px;\"><div class=\"img-rounded\" style=\"float: left; margin-right: 5px; width: 15px; height: 15px; background-color:<%=datasets[i].strokeColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
					legend:{
						display: true,
						position: 'right'
					},
					pieceLabel: {
					    render: function (args) {
					        return $filter('number')(args.value, 0);
					      },
					    fontSize: 10,
					    fontStyle: 'bold',
					    fontColor: '#000',
					    position: 'outside'
					}
			}
			
			this.chartOptions_estado= {
					datasetStrokeWidth : 6,
					legendTemplate: "<div class=\"chart-legend\"><ul class=\"line-legend\"><% for (var i=0; i<datasets.length; i++){%><li style=\"font-size: 12px;\"><div class=\"img-rounded\" style=\"float: left; margin-right: 5px; width: 15px; height: 15px; background-color:<%=datasets[i].strokeColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
					legend:{
						display: true,
						position: 'right'
					},
					pieceLabel: {
						render: function (args) {
					        return $filter('number')(args.value, 0);
					      },
					    fontSize: 10,
					    fontStyle: 'bold',
					    fontColor: '#000',
					    position: 'outside'
					}
			}
			
			this.loadEventos=function(){
				$http.post('/SEventoGC', { action: 'entidadesData', ejercicio: this.ano, mes: this.mes }).then(function(response){
				    if(response.data.success){
				    	this.chartLabels_anual = [];
				    	for(i=0; i<this.mes; i++)
							this.chartLabels_anual.push(this.meses[i]);
				    	this.chartSeries = [''+(this.ano-2), ''+(this.ano-1), ''+this.ano];
				    	this.chartData_start = response.data.entidades;
				    	this.serie1 = [];
				    	this.serie2 = [];
				    	this.serie3 = [];
				    	this.chartLabels=[];
				    	this.chartSeries_anual=[];
				    	this.chartData=[];
				    	this.chartData_anual=[];
				    	for(var i=0; i<response.data.entidades.length; i++){
				    		this.chartLabels.push(response.data.entidades[i].nombre_corto);
				    		this.chartSeries_anual.push(response.data.entidades[i].nombre_corto);
				    		this.total1=0;
						    this.total2=0;
						    this.total3=0;
						    this.serie=[];
						    for(var j=0; j<this.mes; j++){
						    	this.total1+=response.data.entidades[i].ano1[j];
						    	this.total2+=response.data.entidades[i].ano2[j];
						    	this.total3+=response.data.entidades[i].ano_actual[j];
						    	this.serie.push(this.total3);
						    }
						    this.chartData_anual.push(this.serie);
						    this.serie1.push(this.total1);
				    		this.serie2.push(this.total2);
				    		this.serie3.push(this.total3);
				    	}
				    	this.chartData.push(this.serie1);
				    	this.chartData.push(this.serie2);
				    	this.chartData.push(this.serie3);
					    }
					    this.showloading=false;
				}.bind(this), function errorCallback(response){
				 		
				}
				);
				
				$http.post('/SEventoGC', { action: 'modalidadesData', ejercicio: this.ano, mes: this.mes }).then(function(response){
				    if(response.data.success){
				    	this.chartLabels_modalidad=[];
				    	this.chartData_modalidad=[];
				    	for(var i=0; i<response.data.modalidades.length; i++){
				    		this.chartLabels_modalidad.push(response.data.modalidades[i].nombre);
				    		this.chartData_modalidad.push(response.data.modalidades[i].eventos);
				    	}
					    this.showloading=false;
				    }
				}.bind(this), function errorCallback(response){
				 		
				}
				);
				
				$http.post('/SEventoGC', { action: 'estadosData', ejercicio: this.ano, mes: this.mes }).then(function(response){
				    if(response.data.success){
				    	this.chartLabels_estado=[];
				    	this.chartData_estado=[];
				    	for(var i=0; i<response.data.estados.length; i++){
				    		this.chartLabels_estado.push(response.data.estados[i].nombre);
				    		this.chartData_estado.push(response.data.estados[i].eventos);
				    	}
					    this.showloading=false;
				    }
				}.bind(this), function errorCallback(response){
				 		
				}
				);
			}
			
			this.mesClick=function(mes){
				switch(mes){
					case 1: this.nmes="Enero"; break;
					case 2: this.nmes="Febrero"; break;
					case 3: this.nmes="Marzo"; break;
					case 4: this.nmes="Abril"; break;
					case 5: this.nmes="Mayo"; break;
					case 6: this.nmes="Junio"; break;
					case 7: this.nmes="Julio"; break;
					case 8: this.nmes="Agosto"; break;
					case 9: this.nmes="Septiembre"; break;
					case 10: this.nmes="Octubre"; break;
					case 11: this.nmes="Noviembre"; break;
					case 12: this.nmes="Diciembre"; break;
				}
				this.mes = mes;
				this.loadEventos();
			}
			
			this.anoClick=function(ano){
				this.ano = ano;
				this.loadEventos();
			}
			
			this.mesClick(this.mes+1);
			
			this.changeAcumulado = function(){
				this.serie1 = [];
		    	this.serie2 = [];
		    	this.serie3 = [];
		    	this.chart_title = (this.chart_acumulado) ? 'Eventos de Guatecompras acumulados hasta el mes' : 'Eventos de Guatecompras del mes';
		    	for(var i=0; i<this.chartData_start.length; i++){
			    	if(!this.chart_acumulado){
		    			this.serie1.push(this.chartData_start[i].ano1[this.mes-1]);
		    			this.serie2.push(this.chartData_start[i].ano2[this.mes-1]);
		    			this.serie3.push(this.chartData_start[i].ano_actual[this.mes-1]);
		    		}
		    		else{
		    			this.total1=0;
				    	this.total2=0;
				    	this.total3=0;
				    	for(var j=0; j<this.mes; j++){
				    		this.total1+=this.chartData_start[i].ano1[j];
				    		this.total2+=this.chartData_start[i].ano2[j];
				    		this.total3+=this.chartData_start[i].ano_actual[j];
				    	}
				    	this.serie1.push(this.total1);
		    			this.serie2.push(this.total2);
		    			this.serie3.push(this.total3);
		    		}
		    	}
		    	this.chartData = [];
		    	this.chartData.push(this.serie1);
		    	this.chartData.push(this.serie2);
		    	this.chartData.push(this.serie3);
			}
			
			this.changeAcumulado_Anual = function(){
				this.chartData_anual = [];
		    	for(var i=0; i<this.chartData_start.length; i++){
		    		this.serie3 = [];
		    		this.total3=0;
			    	for(var j=0; j<this.mes; j++){
				    	this.total3+=this.chartData_start[i].ano_actual[j];
				    	this.serie3.push(this.chart_acumulado_anual ? this.total3 : this.chartData_start[i].ano_actual[j]);
				    }
			    	this.chartData_anual.push(this.serie3);
		    	}
			}
			
		}
	]);