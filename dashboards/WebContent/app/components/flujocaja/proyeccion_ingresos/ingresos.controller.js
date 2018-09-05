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
			me.viewQuetzales_r=true;
			
			me.sindatos=false;
			
			me.numero_pronosticos=12;
			me.total_ejercicio=[];
			me.total_pronosticos=0.0;
			me.totales=[];
			
			me.pronosticos_por_recurso = [];
			
			me.nivel = 1;
			me.recursos_selected=[];
			me.recursos_selected_a=[];
			me.auxiliares_selected=[];
			
			me.hide_tabla_recursos_blanks = true;
			
			me.stack_sum = [];
			
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
					me.recursos_selected_a=[];
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
				    	    	me.recursos_selected_a.push(n.recurso);
				    	    }
			    	    }
			    	    if((n.children===undefined || n.children==null || n.children.length==0) && n.auxiliar==-1)
			    	    	num_recursos++;
			    	});
					$http.post('/SFlujoCaja', { action: 'getPronosticosIngresos', ejercicio: me.anio, 
							recursosIds: (num_recursos==me.recursos_selected.length) ? null : JSON.stringify(me.recursos_selected),
							auxiliaresIds: (num_recursos==me.recursos_selected.length) ? null : JSON.stringify(me.auxiliares_selected), numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12,
							mes: me.mes  }).then(function(response){
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
					
					$http.post('/SFlujoCaja', { action: 'getPronosticosIngresosPorRecurso', ejercicio: me.anio, 
						recursosIds: JSON.stringify(me.recursos_selected_a),
						mes: me.mes,   
						numero: me.numero_pronosticos!=null && me.numero_pronosticos!='' && me.numero_pronosticos>0 ? me.numero_pronosticos : 12
					}).then(function(response){
							if(response.data.success){
								me.totales=[];
								for(var i=0; i<me.numero_pronosticos; i++)
									me.totales.push(0.0);
								me.pronosticos_por_recurso = response.data.pronosticos; 
								for(var i=0; i<me.pronosticos_por_recurso.length; i++){
									for(var j=0; j<me.pronosticos_por_recurso[i].pronosticos.length; j++)
										me.totales[j] += (me.pronosticos_por_recurso[i].pronosticos[j]!=null) ? me.pronosticos_por_recurso[i].pronosticos[j] : 0.0;
								}
								for(var i=0; i<me.pronosticos_por_recurso.length; i++){
									if(me.pronosticos_por_recurso[i].nivel==1){
										var sum=me.sumChildren(me.pronosticos_por_recurso,i, me.numero_pronosticos);
										me.pronosticos_por_recurso[i].pronosticos = sum;
									}
									var blank = true;
									for(var j=0; j<me.pronosticos_por_recurso[i].pronosticos.length; j++){
										blank = blank && (me.pronosticos_por_recurso[i].pronosticos[j]==null || me.pronosticos_por_recurso[i].pronosticos[j]==0);
									}
									me.pronosticos_por_recurso[i].blank = blank;
								}
								me.showloading = false;
							}
						});	
				
			}
			
			me.sumChildren=function(array, pos, length){
				var sum=[];
				for(var j=0; j<length; j++)
					sum.push(0.0);
				for(var i=pos+1; i<array.length && array[i].nivel!=array[pos].nivel; i++){
					if(i+1 < array.length){
						if(array[i].nivel<array[i+1].nivel){
							array[i].pronosticos=me.sumChildren(array,i, length);
							for(var j=0;j<length; j++)
								sum[j]+=array[i].pronosticos[j];
							var pos_act = i;
							while(array[pos_act].nivel<array[i+1].nivel && i<array.length-2)
								i++;
						}
						else if(array[i].nivel>array[i+1].nivel){
							for(var j=0;j<length; j++)
								sum[j]+=array[i].pronosticos[j];
							return sum;
						}
						else
							for(var j=0;j<length; j++)
								sum[j]+=array[i].pronosticos[j];
					}
					else
						return array[i].pronosticos;
				}
				return sum;
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
			
			me.filtroQuetzalesR=function(value){
				if(me.viewQuetzales_r){
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
			
			me.checkAll=function(selected){
				if(selected)
					ivhTreeviewMgr.selectAll(me.recursos);
				else
					ivhTreeviewMgr.deselectAll(me.recursos);
			}
			
			
			
		}
	]);