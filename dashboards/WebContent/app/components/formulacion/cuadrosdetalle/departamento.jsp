<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/main_cuadros_detalle.css" />
<div ng-controller="departamentodetalleController as ctrl" class="maincontainer" id="title" class="all_page">
<h4>Cuadros Globales</h4>
<div class="row" style="margin-bottom: 10px;">
</div>
<div class="row">
	
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto por Departamento</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		    	<div class="row">
					<div class="col-sm-12" style="text-align: right;">
						<div class="btn-group">
					        <label class="btn btn-default" ng-model="ctrl.viewMillones" uib-btn-radio="true" uncheckable tooltip-placement="bottom" uib-tooltip="Ver en millones">MQ</label>
					        <label class="btn btn-default" ng-model="ctrl.viewMillones" uib-btn-radio="false" uncheckable tooltip-placement="bottom" uib-tooltip="Ver en quetzales">Q</label>
					    </div>
					</div>
				</div>
		    	<table st-table="ctrl.departamentos" class="table">
					<thead>
					<tr>
						<th style="text-align: center;">Departamento</th>
						<th style="text-align: center; width: 160px;">Recomendado <br/>{{ ctrl.anio }}</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td style="font-weight: bold; text-align: center;">Total</td>						
						<td style="font-weight: bold;">{{ ctrl.filtroMillones(ctrl.total_recomendado, ctrl.viewMillones) }}</td>
					</tr>
					<tr ng-repeat="row in ctrl.departamentos track by $index" ng-click="ctrl.clickRow(row,$index)" ng-show="row.show">
						<td style="white-space: nowrap; text-transform: capitalize">
							<span class="{{ 'tab'+row.nivel }}"></span>
								<span style="font-size: 8px; margin-right: 15px;"class="{{ row.nivel<8 ? (row.showChildren==false ? 'glyphicon glyphicon-chevron-down' : 'glyphicon glyphicon-chevron-up') : '' }}"></span>
									{{ row.nivel>3 ? (row.nivel==8 ? (row.codigo+'').padStart(2,'0') : row.codigo) +' - '+row.nombre : row.nombre }}
								<span ng-show="row.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span>
						</td>
						<td>{{ ctrl.filtroMillones(row.recomendado, ctrl.viewMillones) }}</td>
					</tr>
					</tbody>
					<tfoot>
						<tr>
							<td style="font-weight: bold; text-align: center;">Total</td>							
							<td style="font-weight: bold;">{{ ctrl.filtroMillones(ctrl.total_recomendado, ctrl.viewMillones) }}</td>
						</tr>
					</tfoot>
				</table>
				<div class="nota" ng-show="ctrl.viewMillones"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
				<div>
					<div class="grid_loading_chart" ng-hide="!ctrl.showChartLoading">
					  	<div class="msg">
					      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
							  <br /><br />
							  <b>Cargando, por favor espere...</b>
						  </span>
						</div>
					</div>
					<div class="chart-title" ng-click="ctrl.returnByChartTitle()">{{ ctrl.chartTitle }}</div>
					<div id="chart_div" style="margin-top: 10px;"></div>
					<div class="chart-return">Para regresar a un nivel anterior presione el titulo de la gráfica</div>
				</div>
			</div>
	   
</div>
<br/>
<br/>
<br/>
</div>