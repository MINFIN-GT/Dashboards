<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="eventosGCController as eventos" class="maincontainer" id="title" class="all_page">
<h4>Eventos de Guatecompras</h4>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">
		<div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="eventos.showloading" style="width: 150px; text-align: left; font-size: 24px;">
	        {{ eventos.nmes }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="eventos.mesClick(1)">Enero</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(2)">Febrero</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(3)">Marzo</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(4)">Abril</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(5)">Mayo</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(6)">Junio</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(7)">Julio</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(8)">Agosto</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(9)">Septiembre</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(10)">Octubre</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(11)">Noviembre</a></li>
	        <li role="menuitem"><a href ng-click="eventos.mesClick(12)">Diciembre</a></li>
	      </ul>
	    </div>
	    <div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="eventos.showloading" style="width: 100px; text-align: left; font-size: 24px;">
	        {{ eventos.ano }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="eventos.anoClick(2017)">2017</a></li>
	        <li role="menuitem"><a href ng-click="eventos.anoClick(2016)">2016</a></li>
	      </ul>
	    </div>
	    <span ng-show="eventos.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
    </div>
</div>
<div class="row" style="margin-bottom: 10px; margin-top: 20px;">
	<div class="col-sm-12" style="text-align: center; font-size: 14px; font-weight: bold;">Presidencia, Ministerios de Estado, Secretarías, Otras Dependencias del Ejecutivo y Procuraduría General de la Nación</div>
</div>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="text-align: center; font-size: 12px; font-weight: bold;">{{ eventos.chart_title }}</div>
</div>
<div class="row">
	<div style="width: 80%; margin: 0 auto; text-align: right;">
		<div class="btn-group" role="group" aria-label="">
			<label class="btn btn-default btn-sm" ng-model="eventos.chart_acumulado" ng-change="eventos.changeAcumulado()" uib-btn-radio="true" uncheckable uib-tooltip="Eventos acumulados hasta el mes"><i class="glyphicon glyphicon-resize-full"></i></label>
        	<label class="btn btn-default btn-sm" ng-model="eventos.chart_acumulado" ng-change="eventos.changeAcumulado()" uib-btn-radio="false" uncheckable uib-tooltip="Eventos del mes"><i class="glyphicon glyphicon-resize-small"></i></label>
		</div>
	</div>
</div>
<div class="row">
	<div style="width: 80%; margin: 0 auto;">
		<canvas class="chart-base" chart-type="eventos.chartType" chart-data="eventos.chartData"
					chart-labels="eventos.chartLabels" chart-series="eventos.chartSeries" chart-options="eventos.chartOptions" chart-colors="eventos.chart_colors"
					chart-dataset-override="eventos.chartDataset">
				</canvas>
	</div>
</div>
<hr style="width: 80%; color: black; height: 1px; background-color:#c3c3c3; border-top: none; margin-top: 40px; margin-bottom:40px;" />
<div class="row" style="margin-bottom: 10px; margin-top: 30px;">
	<div class="col-sm-12" style="text-align: center; font-size: 12px; font-weight: bold;">Eventos de Guatecompras del año actual</div>
</div>
<div class="row" style="margin-bottom: 20px;">
	<div style="width: 80%; margin: 0 auto; text-align: right;">
		<div class="btn-group" role="group" aria-label="">
			<label class="btn btn-default btn-sm" ng-model="eventos.chart_acumulado_anual" ng-change="eventos.changeAcumulado_Anual()" uib-btn-radio="true" uncheckable uib-tooltip="Eventos acumulados hasta el mes"><i class="glyphicon glyphicon-resize-full"></i></label>
        	<label class="btn btn-default btn-sm" ng-model="eventos.chart_acumulado_anual" ng-change="eventos.changeAcumulado_Anual()" uib-btn-radio="false" uncheckable uib-tooltip="Eventos del mes"><i class="glyphicon glyphicon-resize-small"></i></label>
		</div>
	</div>
</div>
<div class="row">
	<div style="width: 80%; margin: 0 auto;">
		<canvas class="chart-base" chart-type="eventos.chartType_anual" chart-data="eventos.chartData_anual"
					chart-labels="eventos.chartLabels_anual" chart-series="eventos.chartSeries_anual" chart-options="eventos.chartOptions_anual" chart_colors="eventos.chart_colors_anual"
		chart-dataset-override="eventos.chartDataset_anual">
				</canvas>
	</div>
</div>
<hr style="width: 80%; color: black; height: 1px; background-color:#c3c3c3; border-top: none; margin-top: 40px; margin-bottom:40px;" />
<div class="row">
	<div class="col-sm-6">
		<div style="width: 90%; margin: 0 auto;">
			<div style="text-align: center; font-size: 12px; font-weight: bold;">Eventos de Guatecompras del año actual, por modalidad</div>
			<br/>
			<canvas class="chart-base" chart-type="eventos.chartType_modalidad" chart-data="eventos.chartData_modalidad"
						chart-labels="eventos.chartLabels_modalidad" chart-series="eventos.chartSeries_modalidad" chart-options="eventos.chartOptions_modalidad" chart_colors="eventos.chart_colors_modalidad">
					</canvas>
		</div>
	</div>
	<div class="col-sm-6">
		<div style="width: 90%; margin: 0 auto;">
			<div style="text-align: center; font-size: 12px; font-weight: bold;">Eventos de Guatecompras del año actual, por estado</div>
			<br/>
			<canvas class="chart-base" chart-type="eventos.chartType_estado" chart-data="eventos.chartData_estado"
						chart-labels="eventos.chartLabels_estado" chart-series="eventos.chartSeries_estado" chart-options="eventos.chartOptions_estado" chart_colors="eventos.chart_colors_estado">
					</canvas>
		</div>
	</div>
</div>
<br/>
<br/>
<br/>
</div>