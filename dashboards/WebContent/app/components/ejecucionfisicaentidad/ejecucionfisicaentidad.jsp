<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/ng-template" id="tablaMetas.html">
		<div class="modal-header">
            <h3 class="modal-title" id="modal-title">Metas</h3>
        </div>
        <div class="modal-body" id="modal-body">
			<div class="grid_loading" ng-hide="!loading_metas" style="width: 95%">
			  	<div class="msg">
			      <span><i class="fa fa-spinner fa-spin fa-2x"></i>
					  <br /><br />
					  <b>Cargando, por favor espere...</b>
				  </span>
				</div>
			  </div>
            <table class="table table-striped" style="display: block; overflow: auto; max-height: 400px;">
				<thead>
					<tr>
						<th>Código</th>
						<th>Meta</th>
						<th style="white-space: nowrap;">Unidad de Med.</th>
						<th></th>
						<th>Ene</th>
						<th>Feb</th>
						<th>Mar</th>
						<th>Abr</th>
						<th>May</th>
						<th>Jun</th>
						<th>Jul</th>
						<th>Ago</th>
						<th>Sep</th>
						<th>Oct</th>
						<th>Nov</th>
						<th>Dic</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat-start="meta in metas">
						<td rowspan="3" style="vertical-align: middle;">{{ meta.codigo_meta }}</td>
						<td rowspan="3" style="min-width: 200px; vertical-align: middle;">{{ meta.descripcion }}</td>
						<td rowspan="3" style="vertical-align: middle;">{{ meta.unidad_medida }}</td>
						<td>Ejecutado</td>
						<td style="text-align: right;">{{ meta.ejecucion_1 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_2 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_3 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_4 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_5 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_6 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_7 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_8 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_9 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_10 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_11 | number}}</td>
						<td style="text-align: right;">{{ meta.ejecucion_12 | number}}</td>						
					</tr>
					<tr>
						<td>Vigente</td>
						<td style="text-align: right;">{{ meta.vigente_1  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_2  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_3  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_4  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_5  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_6  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_7  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_8  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_9  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_10  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_11  | number}}</td>
						<td style="text-align: right;">{{ meta.vigente_12  | number}}</td>						
					</tr>
					<tr ng-repeat-end>
						<td>Modificación</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_1 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_1  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_2 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_2  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_3 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_3  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_4 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_4  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_5 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_5  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_6 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_6  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_7 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_7  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_8 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_8  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_9 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_9  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_10 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_10  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_11 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_11  | number}}</td>
						<td style="text-align: right;" ng-style="{ color: meta.modificacion_12 >= 0 ? 'green' : 'red' }">{{ meta.modificacion_12  | number}}</td>						
					</tr>
				</tbody>
			</table>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="cerrar()">OK</button>
        </div>
		</div>
</script>

<div ng-controller="ejecucionfisicaentidadController as ejecucion" class="maincontainer" id="title" class="all_page">
<h4>Ejecución Presupuestaria vs Ejecución Física</h4>
<div class="row" style="width: 90%; margin: auto">
<br/>
<br/>
		<div class="col-sm-12">
			<h4>{{ ejecucion.entidad.nombre  }}</h4>
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
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="text-align: center; font-size: 16px; font-weight: bold;">{{ ejecucion.titulo }}</div>
</div>
<br/>
<div class="row">
	<div ui-i18n="es" class="col-sm-12">
		<div class="row" style="margin-bottom: 10px;">
			<div class="col-sm-11" style="float: left;">
				<a href="#!/dashboards/ejecucionfisica" class="btn btn-default no-border" ng-disabled="ejecucion.showloading">ADMINISTRACIÓN CENTRAL</a>
				<span ng-hide="ejecucion.entidad.id==null"> / <a href class="btn btn-default no-border" ng-click="ejecucion.goLevel(2, false)" ng-disabled="ejecucion.showloading">[ {{ ejecucion.entidad.id }} ] {{ ejecucion.entidad.nombre }}</a></span>
				<span ng-hide="ejecucion.unidad_ejecutora==null"> / <a href class="btn btn-default no-border" ng-click="ejecucion.goLevel(3, false)" ng-disabled="ejecucion.showloading">[ {{ ejecucion.unidad_ejecutora}} ] {{ ejecucion.unidad_ejecutora_nombre }}</a></span>
				<span ng-hide="ejecucion.programa==null"> / <a href class="btn btn-default no-border" ng-click="ejecucion.goLevel(4, false)" ng-disabled="ejecucion.showloading">[ {{ ejecucion.programa}} ] {{ ejecucion.programa_nombre }}</a></span>
				<span ng-hide="ejecucion.subprograma==null"> / <a href class="btn btn-default no-border" ng-click="ejecucion.goLevel(5, false)" ng-disabled="ejecucion.showloading">[ {{ ejecucion.subprograma }} ] {{ ejecucion.subprograma_nombre }}</a></span>
				<span ng-hide="ejecucion.proyecto==null"> / <a href class="btn btn-default no-border" ng-click="ejecucion.goLevel(6, false)" ng-disabled="ejecucion.showloading">[ {{ ejecucion.proyecto }} ] {{ ejecucion.proyecto_nombre }}</a></span>
			</div>
			<div class="col-sm-1 text-right"><div class="btn-group" role="group" aria-label="">
													<a class="btn btn-default" href="#!/dashboards/ejecucionfisica/gt1" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
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