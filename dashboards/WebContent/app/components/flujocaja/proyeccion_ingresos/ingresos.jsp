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
	
	.dato-pronostico{
		color: #008000;
	}
	
	.dato-historico{
		color: #000;
	}
	

</style>    
    
<div ng-controller="ingresosController as ingreso" class="maincontainer" id="title" class="all_page">
<h4>Ingresos</h4>
<h5>Pronósticos</h5>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="padding-left: 0px;">
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
	<div class="btn-group">
	        <label class="btn btn-default" ng-model="ingreso.fecha_referencia" uib-btn-radio="'Fecha Real'" uncheckable ng-change="ingreso.cambioFechaReal()">Fecha Real + 3</label>
	        <label class="btn btn-default" ng-model="ingreso.fecha_referencia" uib-btn-radio="'Fecha Aprobado'" uncheckable ng-change="ingreso.cambioFechaReal()">Fecha Aprobado</label>
	    </div>
</div>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">
		<button type="button" class="btn btn-default no-border" style="font-size: 18px;" ng-click="ingreso.panel_recursos = !ingreso.panel_recursos" ng-disabled="ingreso.showloading">Recursos</button>
		<div uib-collapse="!ingreso.panel_recursos" style="margin: 10px 0px 0px 12px;" class="panel panel-default">
			<div style="margin: 5px 15px 0px 15px;">
			<label>Buscar</label>
		    <input type="text" ng-model="ingreso.treeFilter" class="form-control" />
		    </div>
		    <br/>
		    <div style="text-align: right; margin: 5px;">
				<button type="button" class="btn btn-default btn-xs" ng-click="ingreso.checkAll(true)"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Todos</button> 
				<button type="button" class="btn btn-default btn-xs" ng-click="ingreso.checkAll(false)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>Ninguno</button>
				<button type="button" class="btn btn-default btn-xs" ng-click="ingreso.getPronosticos()"><span class="glyphicon glyphicon-play" aria-hidden="true"></span>Generar</button>
			</div>
		    <div ivh-treeview="ingreso.recursos"
			  ivh-treeview-label-attribute="'nombre_control'"
			  ivh-treeview-default-selected-state="true"
			  ivh-treeview-expand-to-depth="1"
			  ivh-treeview-on-cb-change="ingreso.selectRecurso(ivhNode, ivhIsSelected)"
			  ivh-treeview-twistie-expanded-tpl="ingreso.tplFolderOpen"
              ivh-treeview-twistie-collapsed-tpl="ingreso.tplFolderClose"
              ivh-treeview-twistie-leaf-tpl="ingreso.tplLeaf"
              ivh-treeview-node-tpl="ingreso.tplNode"
              ivh-treeview-filter="ingreso.treeFilter" style="margin-left: 15px;">
			</div>
		</div>
	 </div>
</div>
<uib-tabset active="1">
	<uib-tab index="1" heading="Gráfica">
		<div style="margin-bottom: 10px; margin-top: 30px; text-align: center;" ng-show="!ingreso.sindatos">
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
							<td align="center" ng-repeat="label in ingreso.chartLabels track by $index" style="{{ingreso.getEstiloDato($index) ? 'color: #008000;' : 'color: #000;' }}">{{ label }}</td>
							<td align="center">Total</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td align="right" ng-repeat="dato_pronostico in ingreso.tablePronosticos track by $index" style="{{ingreso.getEstiloDato($index) ? 'color: #008000;' : 'color: #000;' }}">{{ ingreso.filtroQuetzalesP(dato_pronostico.toFixed(2)) }}</td>
							<td align="right">{{ ingreso.filtroQuetzalesP(ingreso.total_pronosticos.toFixed(2)) }}</td>
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
	</uib-tab>
	<uib-tab index="2" heading="Por Recurso">
		<br />
		<div style="width: 90%;">
				<button type="button" class="btn btn-default" ng-model="ingreso.hide_tabla_recursos_blanks" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false"
				uib-tooltip="Ocultar filas en blanco" tooltip-placement="top">
		        	<span class="glyphicon glyphicon-list" aria-hidden="true"></span>
		   		</button>
				<button type="button" class="btn btn-default" ng-model="ingreso.viewQuetzales_r" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false"
				uib-tooltip="Mostrar datos sin formato" tooltip-placement="top">
		        	Q
		   		</button>
		</div>
		<br/>
		<div style="width: 100%; overflow-x: scroll;">
			<table style="white-space: nowrap; font-size: 12px;" class="table table-hover">
				<thead>
					<tr>
						<th>Recurso</th>
						<th style="text-align: center;" ng-repeat="label in ingreso.chartLabels track by $index">{{ label }}</th>
						<th style="text-align: center;">Total</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="row in ingreso.pronosticos_por_recurso track by $index" ng-hide="ingreso.hide_tabla_recursos_blanks && row.blank">
						<td style="padding-left: {{ row.nivel*10 }}px; font-weight: {{ row.nivel<4 ? 'bold' : 'normal'}};">{{ row.recurso + ' ' + row.nombre }}</td>
						<td style="text-align: right;" class="dato-pronostico" ng-repeat="dato in row.pronosticos track by $index">{{ dato>0 ? ingreso.filtroQuetzalesR(dato.toFixed(2)) : null }}</td>
						<td style="text-align: right; font-weight: bold;">{{ row.total >0 ? ingreso.filtroQuetzalesR(row.total.toFixed(2)) : null }}</td>
					</tr>
				</tbody>
				<tfoot>
					<tr style="text-align: right; font-weight: bold;">
						<td ng-init="ingreso.total_totales=0.0">Total</td>
						<td style="text-align: right;" class="dato-pronostico"ng-repeat="dato in ingreso.totales track by $index" ng-init="ingreso.total_totales=ingreso.total_totales+dato">{{ dato>0 ? ingreso.filtroQuetzalesR(dato.toFixed(2)) :  null}}</td>
						<td>{{ ingreso.total_totales>0 ? ingreso.filtroQuetzalesR(ingreso.total_totales.toFixed(2)) :  null}}</td>
					</tr>
				</tfoot>
			</table>
			<br/>
			<br/>
		</div>
	</uib-tab>
</uib-tabset>
<br/>
<div><span style="font-weight: bold;">Nota:</span> Los números en color verde son pronósticos, los datos en color negro son históricos</div>
<br/>
<br/>
<br/>
</div>