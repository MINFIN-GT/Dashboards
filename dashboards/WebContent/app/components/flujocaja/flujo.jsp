<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	
	.table_datos {
		width:90%;
		overflow-x: auto; 
		margin: 0 auto;
		display: block;
	}
	
	.tr_egresos td {
		color: #FF0000;
		padding: 3px;
		white-space: nowrap; 
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
	}
	
	.tr_ingresos td {
		color: #008000;
		padding: 3px;
		white-space: nowrap; 
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
	}
	
	.tr_caja td {
		color: #000000;
		padding: 3px;
		white-space: nowrap; 
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
	}

</style>    
    
<div ng-controller="flujoController as flujo" class="maincontainer" id="title" class="all_page">
<h4>Flujo de Caja [Presupuesto de Caja]</h4>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="padding-left: 0px;">
		<div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="flujo.showloading" style="width: 100px; text-align: left; font-size: 24px;">
	        {{ flujo.anio }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="flujo.anoClick(2018)">2018</a></li>
	      </ul>
	    </div>
	    <span ng-show="flujo.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
    </div>
</div>
<br/>
<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="!flujo.sindatos">
	<div style="width: 800px; height: 400px; margin: 0 auto;">
		<canvas class="chart-base" chart-type="flujo.chartType" chart-data="flujo.chartData"
					chart-labels="flujo.chartLabels" chart-series="flujo.chartSeries" chart-options="flujo.chartOptions" chart-colors="flujo.chartColors"
					chart-dataset-override="flujo.chartDataset">
				</canvas>
	</div>
</div>
<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="flujo.chartLoaded && !flujo.sindatos">
	<div>Ingresos</div>
	<div style="text-align: center;">
		<div style="width: 90%; text-align: right;">
		<button type="button" class="btn btn-default" ng-model="flujo.viewQuetzales" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
        	Q
   		</button>
   		</div>
   		<br/>
		<table class="table_datos">
			<thead>
				<tr>
					<td></td>
					<td align="center" ng-repeat="label in flujo.chartLabels track by $index">{{ label }}</td>
					<td>Total</td>
				</tr>
			</thead>
			<tbody>
				<tr class="tr_ingresos">
					<td align="left">Ingresos</td>
					<td align="right" ng-repeat="dato in flujo.ingresos track by $index">{{ flujo.filtroQuetzales(dato.toFixed(2), flujo.viewQuetzales) }}</td>
					<td align="right">{{ flujo.filtroQuetzales(flujo.total_ingresos.toFixed(2), flujo.viewQuetzales) }}</td>
				</tr>
				<tr class="tr_egresos">
					<td align="left">Egresos</td>
					<td align="right" ng-repeat="dato in flujo.egresos track by $index">{{ flujo.filtroQuetzales(dato.toFixed(2), flujo.viewQuetzales) }}</td>
					<td align="right">{{ flujo.filtroQuetzales(flujo.total_egresos.toFixed(2), flujo.viewQuetzales) }}</td>
				</tr>
				<tr class="tr_caja">
					<td align="left">Caja</td>
					<td align="right" ng-repeat="dato in flujo.caja track by $index">{{ flujo.filtroQuetzales(dato.toFixed(2), flujo.viewQuetzales) }}</td>
					<td align="right">{{ flujo.filtroQuetzales(flujo.total_ingresos.toFixed(2)-flujo.total_egresos.toFixed(2), flujo.viewQuetzales) }}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br/>
</div>
<div style="text-align: center;" ng-show="flujo.sindatos">Sin datos históricos suficientes para generar los prónosticos</div>
<br/>
<br/>
<br/>
</div>