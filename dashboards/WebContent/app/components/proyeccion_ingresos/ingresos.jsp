<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.table_historicos{
		width:90%;
		overflow-x: auto; 
		margin: 0 auto;
		display: block;
	}
	
	.table_historicos td {
		padding: 3px;
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
		white-space: nowrap; 
	}
	
	.table_pronosticos{
		width:90%;
		overflow-x: auto; 
		margin: 0 auto;
		display: block;
	}
	
	.table_pronosticos td {
		color: #008000;
		padding: 3px;
		white-space: nowrap; 
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
	}

</style>    
    
<div ng-controller="ingresosController as ingreso" class="maincontainer" id="title" class="all_page">
<h4>Ingresos</h4>
<h5>Pronósticos</h5>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="padding-left: 0px;">
		<div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" ng-disabled="ingreso.showloading"  uib-dropdown-toggle style="width: 150px; text-align: left; font-size: 24px;">
	        {{ ingreso.nmes }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(1)">Enero</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(2)">Febrero</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(3)">Marzo</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(4)">Abril</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(5)">Mayo</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(6)">Junio</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(7)">Julio</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(8)">Agosto</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(9)">Septiembre</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(10)">Octubre</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(11)">Noviembre</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(12)">Diciembre</a></li>
	      </ul>
	    </div>
	    <div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="ingreso.showloading" style="width: 100px; text-align: left; font-size: 24px;">
	        {{ ingreso.anio }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="ingreso.anoClick(2017)">2017</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.anoClick(2016)">2018</a></li>
	      </ul>
	    </div>
	    <span ng-show="ingreso.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
    </div>
</div>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">Número de meses a proyectar:
		<input type="number" ng-model="ingreso.numero_pronosticos" min="1" max="24" style="text-align: right;"/>
	</div>
</div>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">
		<div>Recurso</div>
		<div angucomplete-alt id="recurso"
              placeholder="Busqueda de recursos"
              pause="100"
              selected-object="ingreso.cambioRecurso"
              local-data="ingreso.recursos"
              search-fields="nombre_control"
              title-field="nombre_control"
              description-field=""
              minlength="1"
              input-class="input-angucomplete"
              match-class="highlight"
              focus-out="ingreso.blurRecurso()"
              inputname="recurso"></div>
    </div>
    <div class="col-sm-12" style="margin-top: -20px;">
    	<div>Auxiliar</div>
		<div angucomplete-alt id="auxiliar"
              placeholder="Busqueda de auxiliares"
              pause="100"
              selected-object="ingreso.cambioAuxiliar"
              local-data="ingreso.auxiliares"
              search-fields="nombre_control"
              title-field="nombre_control"
              description-field=""
              minlength="1"
              input-class="input-angucomplete"
              match-class="highlight"
              focus-out="ingreso.blurAuxiliar()"
              inputname="auxiliar" 
              disable-input="ingreso.recurso==null"></div>
    </div>
</div>
<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="!ingreso.sindatos">
	<div style="width: 800px; height: 400px; margin: 0 auto;">
		<canvas class="chart-base" chart-type="ingreso.chartType" chart-data="ingreso.chartData"
					chart-labels="ingreso.chartLabels" chart-series="ingreso.chartSeries" chart-options="ingreso.chartOptions" chart-colors="ingreso.chartColors"
					chart-dataset-override="ingreso.chartDataset">
				</canvas>
	</div>
</div>
<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="ingreso.chartLoaded && !ingreso.sindatos">
	<div>Pronósticos</div>
	<div style="text-align: center;">
		<div style="width: 90%; text-align: right;">
		<button type="button" class="btn btn-default" ng-model="ingreso.viewQuetzales_p" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
        	Q
   		</button>
   		</div>
   		<br/>
		<table class="table_pronosticos">
			<thead>
				<tr>
					<td align="center" ng-repeat="label in ingreso.chartLabels.slice(12) track by $index">{{ label }}</td>
					<td align="center">Total</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td align="right" ng-repeat="dato_pronostico in ingreso.chartData[1].slice(12) track by $index">{{ ingreso.filtroQuetzalesP(dato_pronostico.toFixed(2)) }}</td>
					<td align="right">{{ ingreso.filtroQuetzales(ingreso.total_pronosticos.toFixed(2)) }}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br/>
	<div>Historia</div>
	<div style="text-align: center;">
		<div style="width: 90%; text-align: right;">
		<button type="button" class="btn btn-default" ng-model="ingreso.viewQuetzales" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
        	Q
   		</button>
   		</div>
   		<br/>
		<table class="table_historicos">
			<thead>
				<tr>
					<td>Año</td>
					<td ng-repeat="mes in ingreso.meses">{{ mes }}</td>
					<td>Total</td>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="ejercicio in ingreso.historia track by $index">
					<td align="center">{{ ejercicio[0] }}</td>
					<td align="right" ng-repeat="dato in ejercicio.slice(1) track by $index">{{ ingreso.filtroQuetzales(dato.toFixed(2)) }}</td>
					<td align="right">{{ ingreso.filtroQuetzales(ingreso.total_ejercicio[$index].toFixed(2)) }}</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div style="text-align: center;" ng-show="ingreso.sindatos && ingreso.recurso==0">Sin datos históricos suficientes para generar los prónosticos</div>
<br/>
<br/>
<br/>
</div>