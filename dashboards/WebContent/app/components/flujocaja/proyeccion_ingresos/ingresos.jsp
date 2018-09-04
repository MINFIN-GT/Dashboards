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
	
	.tab1{
		padding-left: 10px;
	}
	
	.tab2{
		padding-left: 20px;
	}
	
	.tab3{
		padding-left: 30px;
	}
	
	.tab4{
		padding-left: 40px;
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
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button" >
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(1, true)">Enero</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(2, true)">Febrero</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(3, true)">Marzo</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(4, true)">Abril</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(5, true)">Mayo</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(6, true)">Junio</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(7, true)">Julio</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(8, true)">Agosto</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(9, true)">Septiembre</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(10, true)">Octubre</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(11, true)">Noviembre</a></li>
	        <li role="menuitem"><a href ng-click="ingreso.mesClick(12, true)">Diciembre</a></li>
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
		<input type="number" ng-model="ingreso.numero_pronosticos" min="1" max="24" style="text-align: right;" ng-disabled="ingreso.showloading"/>
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
	</uib-tab>
	<uib-tab index="2" heading="Por Recurso">
		<br />
		<table style="white-space: nowrap; font-size: 12px;" class="table table-hover">
			<thead>
				<tr>
					<th>Recurso</th>
					<th style="text-align: center;"ng-repeat="label in ingreso.chartLabels.slice(12) track by $index">{{ label }}</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in ingreso.pronosticos_por_recurso track by $index" ng-hide="{{ ingreso.hide_tabla_recursos_blanks && row.blank }}">
					<td style="padding-left: {{ row.nivel*10 }}px; font-weight: {{ row.nivel<4 ? 'bold' : 'normal'}};">{{ row.recurso + ' ' + row.nombre }}</td>
					<td style="text-align: right;" ng-repeat="dato in row.pronosticos track by $index">{{ dato>0 ? ingreso.filtroRQuetzales(dato.toFixed(2)) : null }}</td>
				</tr>
			</tbody>
		</table>
	</uib-tab>
</uib-tabset>
<br/>
<br/>
<br/>
</div>