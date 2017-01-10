<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="ejecucionfisicaController as ejecucion" class="maincontainer" id="title" class="all_page">
<h4>Ejecución Presupuestaria vs Ejecución Física</h4>
<div class="row" style="width: 90%; margin: auto">
<br/>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">
		<div style="font-size: 20px; float: left; padding-top: 11px;">Ejercicio:</div> 
		<div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="ejecucion.showloading" style="width: 100px; text-align: left; font-size: 24px;">
	        {{ ejecucion.ano }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="ejecucion.anoClick(2017)">2017</a></li>
	        <li role="menuitem"><a href ng-click="ejecucion.anoClick(2016)">2016</a></li>
	      </ul>
	    </div>
	    <span ng-show="ejecucion.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
    </div>
</div>
<br/>
		<div class="col-sm-12">
			<h4>{{ ejecucion.chartTitle }}&nbsp;</h4>
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
						<li ng-repeat="serie in ejecucion.chartSeries track by $index" style="font-size: 16px;">
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
												<button type="button" class="btn btn-default" ng-click="ejecucion.resetView()" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></button>
											</div>
			</div>
		</div>
		<br/>
		<div ui-grid="ejecucion.entidades_gridOptions" ui-grid-save-state ui-grid-selection ui-grid-move-columns ui-grid-resize-columns class="grid" style="height: 700px;">
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
		<div style="text-align: center;"><p>Ejecución:  <span uib-tooltip="Menor al 50% del valor esperado" class="glyphicon glyphicon-certificate dot_4 "></span> Baja  |  <span uib-tooltip="Entre el 50% y el 75% del valor esperado" class="glyphicon glyphicon-certificate dot_2 "></span> Media  |  <span uib-tooltip="Entre el 75% y el 100% del valor esperado" class="glyphicon glyphicon-certificate dot_3"></span> Optima  |  <span uib-tooltip="Más del 100% del valor esperado" class="glyphicon glyphicon-certificate dot_1"></span> Sobre Ejecución</p></div>
			<div class="text-center">Ejecución Esperada: {{ (100/12)*ejecucion.month | currency:"":2 }} %</div>
		<br/>
		<br/>
		<div class="row">
			<div class="col-sm-12 text-center">Última actualización: {{ ejecucion.lastupdate }}</div>
		</div>
	</div>
</div>
</div>
<br/>
<br/>
<br/>