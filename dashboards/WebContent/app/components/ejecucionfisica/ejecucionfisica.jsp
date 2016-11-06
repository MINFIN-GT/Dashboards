<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="ejecucionfisicaController as ejecucion" class="maincontainer" id="title" class="all_page">
<h4>Ejecución Presupuestaria vs Ejecución Física</h4>
<div class="row" style="width: 90%; margin: auto">
<br/>
<br/>
		<div class="col-sm-12">
			<h4>ADMINISTRACIÓN CENTRAL</h4>
			<h5>Ejecución Presupuestaria vs Ejecución Física (Promedio Ponderado)</h5>
			<div class="row">
				<div class="col-sm-12" style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="ejecucion.chartType='line'" ng-disabled="ejecucion.showloading" role="button">Lineas</a>
						<a class="btn btn-default" href ng-click="ejecucion.chartType='bar'" ng-disabled="ejecucion.showloading" role="button">Barras</a>
						<a class="btn btn-default" href ng-click="ejecucion.chartType='radar'" ng-disabled="ejecucion.showloading" role="button">Radar</a>
					</div>
				</div>
			</div>
			<br/>
			<div class="chart-legend">
					<ul class="line-legend">
						<li ng-repeat="serie in ejecucion.chartSeries track by $index">
							<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ejecucion.chart_colours[$index] }};"></div>
							{{ ejecucion.chartSeries[$index] }} 
						</li>
					</ul>
				</div>
			<br/>
				<canvas height="180px" class="chart-base" chart-type="ejecucion.chartType" chart-data="ejecucion.chartData"
					chart-labels="ejecucion.chartLabels" chart-legend="true" chart-series="ejecucion.chartSeries" chart-options="ejecucion.chartOptions" chart-colors="ejecucion.chart_colours">
				</canvas>
	    </div>
</div>
<br/>
<br/>
<br/>
<div class="row">
	<div ui-i18n="es" class="col-sm-12">
		<div style="height: 35px;">
			<div style="float: left;"><a href class="btn btn-default no-border" ng-disabled="ejecucion.showloading">ADMINISTRACIÓN CENTRAL</a> </div>
			<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
												<a class="btn btn-default" href="#!/dashboards/ejecucionfisica/gt1" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
											</div>
			</div>
		</div>
		<br/>
		<div ui-grid="ejecucion.entidades_gridOptions" ui-grid-save-state class="grid" style="height: 660px;">
			  <div class="grid_loading" ng-hide="!ejecucion.showloading">
			  	<div class="msg">
			      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  <br /><br />
					  <b>Cargando, por favor espere...</b>
				  </span>
				</div>
			  </div>
		</div>
		<br/>
		<div style="text-align: center;"><p>Ejecución:  <span class="glyphicon glyphicon-certificate dot_4 "></span> Baja  |  <span class="glyphicon glyphicon-certificate dot_2 "></span> Media  |  <span class="glyphicon glyphicon-certificate dot_3"></span> Optima  |  <span class="glyphicon glyphicon-certificate dot_1"></span> Sobre Ejecución</p></div>
		<div class="text-center">Ejecución Esperada: {{ (100/12)*ejecucion.month | currency:"":2 }} %</div>
		<div class="row">
			<div class="col-sm-6">Última actualización: {{ ejecucion.lastupdate }}</div>
		</div>
	</div>
</div>
</div>
<br/>
<br/>
<br/>