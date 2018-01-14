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
		color: #FF0000;
		padding: 3px;
		white-space: nowrap; 
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
	}

</style>    
    
<div ng-controller="egresosController as egreso" class="maincontainer" id="title" class="all_page">
<h4>Egresos</h4>
<h5>Pronósticos</h5>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="padding-left: 0px;">
		<div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" ng-disabled="egreso.showloading"  uib-dropdown-toggle style="width: 150px; text-align: left; font-size: 24px;">
	        {{ egreso.nmes }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="egreso.mesClick(1)">Enero</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(2)">Febrero</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(3)">Marzo</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(4)">Abril</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(5)">Mayo</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(6)">Junio</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(7)">Julio</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(8)">Agosto</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(9)">Septiembre</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(10)">Octubre</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(11)">Noviembre</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(12)">Diciembre</a></li>
	      </ul>
	    </div>
	    <div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="egreso.showloading" style="width: 100px; text-align: left; font-size: 24px;">
	        {{ egreso.anio }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="egreso.anoClick(2017)">2017</a></li>
	        <li role="menuitem"><a href ng-click="egreso.anoClick(2016)">2018</a></li>
	      </ul>
	    </div>
	    <span ng-show="egreso.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
    </div>
</div>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">Número de meses a proyectar:
		<input type="number" ng-model="egreso.numero_pronosticos" min="1" max="24" style="text-align: right;"/>
	</div>
</div>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">
		<div>Entidad</div>
		<div angucomplete-alt id="entidad"
              placeholder="Busqueda de entidades"
              pause="100"
              selected-object="egreso.cambioEntidad"
              local-data="egreso.entidades"
              search-fields="nombre_control"
              title-field="nombre_control"
              description-field=""
              minlength="1"
              input-class="input-angucomplete"
              match-class="highlight"
              focus-out="egreso.blurEntidad()"
              inputname="entidad"></div>
    </div>
    <div class="col-sm-12" style="margin-top: -20px;">
    	<div>Unidad Ejecutora</div>
		<div angucomplete-alt id="unidad_ejecutora"
              placeholder="Busqueda de unidades ejecutoras"
              pause="100"
              selected-object="egreso.cambioUnidadEjecutora"
              local-data="egreso.unidades_ejecutoras"
              search-fields="nombre_control"
              title-field="nombre_control"
              description-field=""
              minlength="1"
              input-class="input-angucomplete"
              match-class="highlight"
              focus-out="egreso.blurUnidadEjecutora()"
              inputname="auxiliar" 
              disable-input="egreso.entidad==null"></div>
    </div>
</div>
<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="!egreso.sindatos">
	<div style="width: 800px; height: 400px; margin: 0 auto;">
		<canvas class="chart-base" chart-type="egreso.chartType" chart-data="egreso.chartData"
					chart-labels="egreso.chartLabels" chart-series="egreso.chartSeries" chart-options="egreso.chartOptions" chart-colors="egreso.chartColors"
					chart-dataset-override="egreso.chartDataset">
				</canvas>
	</div>
</div>
<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="egreso.chartLoaded && !egreso.sindatos">
	<div>Pronósticos</div>
	<div style="text-align: center;">
		<div style="width: 90%; text-align: right;">
		<button type="button" class="btn btn-default" ng-model="egreso.viewQuetzales_p" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
        	Q
   		</button>
   		</div>
   		<br/>
		<table class="table_pronosticos">
			<thead>
				<tr>
					<td align="center" ng-repeat="label in egreso.chartLabels.slice(12) track by $index">{{ label }}</td>
					<td align="center">Total</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td align="right" ng-repeat="dato_pronostico in egreso.chartData[1].slice(12) track by $index">{{ egreso.filtroQuetzalesP(dato_pronostico.toFixed(2)) }}</td>
					<td align="right">{{ egreso.filtroQuetzales(egreso.total_pronosticos.toFixed(2)) }}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br/>
	<div>Historia</div>
	<div style="text-align: center;">
		<div style="width: 90%; text-align: right;">
		<button type="button" class="btn btn-default" ng-model="egreso.viewQuetzales" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
        	Q
   		</button>
   		</div>
   		<br/>
		<table class="table_historicos">
			<thead>
				<tr>
					<td>Año</td>
					<td ng-repeat="mes in egreso.meses">{{ mes }}</td>
					<td>Total</td>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="ejercicio in egreso.historia track by $index">
					<td align="center">{{ ejercicio[0] }}</td>
					<td align="right" ng-repeat="dato in ejercicio.slice(1) track by $index">{{ egreso.filtroQuetzales(dato.toFixed(2)) }}</td>
					<td align="right">{{ egreso.filtroQuetzales(egreso.total_ejercicio[$index].toFixed(2)) }}</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div style="text-align: center;" ng-show="egreso.sindatos && egreso.entidad==0">Sin datos históricos suficientes para generar los prónosticos</div>
<br/>
<br/>
<br/>
</div>