

angular.module('calamidadComprasController',['dashboards','smart-table']);
angular.module('calamidadComprasController').controller('ComprasCtrl', function($scope,$http,$routeParams,$rootScope){
	
	this.compras=[];
	this.original_compras=[];
	this.showloading=true;
	this.titulo = $rootScope.titulo;
	this.tipo = $rootScope.tipo;
	
	this.total_eventos=0;
	this.total_adjudicados=0;
	this.total_monto_adjudicado=0.0;
	
	this.chart_colours = [ '#A9D18E', '#5B9BD5', '#F7464A', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'];
	this.chartType = "horizontalBar";
	this.chartTitle = "Eventos Guatecompras";
	this.chartLabels = [];
	this.chartSeries = ['Adjudicados', 'En proceso'];
	this.chartData = [];
	
	if($rootScope.titulo==null || $rootScope.titulo === undefined){
		$http.post('/STransparenciaEstadosCalamidad', { action: 'getEstado',subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.titulo = response.data.results.nombre;
		    	this.tipo = response.data.results.tipo;
		    	$rootScope.titulo = this.titulo;
		    	$rootScope.tipo = this.tipo;
		    	$http.post('/STransparenciaCompras', { action: 'getlist_entidades', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
		    	    if(response.data.success){
		    	    	this.compras_por_entidad = response.data.compras;
		    	    	this.total_eventos=0;
		    	    	this.total_adjudicados=0;
		    	    	this.total_monto_adjudicado=0.0;
		    	    	this.chartLabels = [];
		    	    	this.chartData = [[],[]];
		    	    	for(var i=0; i<this.compras_por_entidad.length; i++){
		    	    		this.total_eventos += this.compras_por_entidad[i].num_eventos;
		    	    		this.total_adjudicados += this.compras_por_entidad[i].num_adjudicados;
		    	    		this.total_monto_adjudicado += this.compras_por_entidad[i].total_adjudicado;
		    	    		this.chartLabels.push(this.compras_por_entidad[i].entidad);
		    	    		this.chartData[0].push(this.compras_por_entidad[i].num_adjudicados);
		    	    		this.chartData[1].push(this.compras_por_entidad[i].num_eventos - this.compras_por_entidad[i].num_adjudicados);
		    	    	}
		    	    	$http.post('/STransparenciaCompras', { action: 'getlist', subprograma: $routeParams.subprograma, t: (new Date()).getTime() }).then(function(response){
		    	    	    if(response.data.success){
		    	    	    	this.original_compras = response.data.compras;
		    	    	    	this.compras = this.original_compras.length> 0 ? this.original_compras.slice(0) : [];
		    	    	    	for(var i=0; i<this.compras.length; i++)
		    	    	    		this.compras[i].fecha = Date.parse(this.compras[i].fecha);
		    	    	    	this.showloading=false;
		    	    	    }
		    	     	}.bind(this), function errorCallback(response){
		    	        	this.showloading=false;
		    	     	}
		    	    	);
		    	    }
		     	}.bind(this), function errorCallback(response){
		        }
		    	);
			}
		}.bind(this)
		);
	}

	this.chartOptions= {
			//scaleLabel: function(label){ return  label.value+' %'; },
			//tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
			//multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>",
			//legendTemplate: "<div class=\"chart-legend\"><ul class=\"line-legend\"><% for (var i=0; i<datasets.length; i++){%><li style=\"font-size: 12px;\"><div class=\"img-rounded\" style=\"float: left; margin-right: 5px; width: 15px; height: 15px; background-color:<%=datasets[i].strokeColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>",
			tooltips:{
				enabled:true
			},
			responsive: true,
			maintainAspectRatio: false,
			scales: {
					xAxes: [{
						stacked: true,
						gridLines: {
							display:false
						},
						scaleLabel: {
							display: true,
							labelString: 'Eventos en Guatecompras'
						}
					}],
					yAxes: [{
						stacked: true,
						gridLines: {
							display:false
						},
						ticks: {
				            display: false
				        }
					}]
			},
            hover: { animationDuration: 0 }
	}
	
	
	
	
});

