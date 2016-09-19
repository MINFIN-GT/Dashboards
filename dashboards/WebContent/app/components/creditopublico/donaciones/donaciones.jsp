<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div ng-controller="depm_Controller as ctrlDEPM" class="maincontainer" id="title" class="all_page">
	<h3>Donaciones para Ejecución Presupuestaria</h3>
	<br/>
	
	<h4 style="width: 300px;">Ejecución Presupuestaria</h4>
	<br/>
	
	<div class="row">
		<div class="col-sm-12 col-centered">
			<h5 class="text-center">Ejecución de donaciones según entidad y unidad ejecutora</h5>
			<br/>
			<table class="table table-hover" style="width: 90%; margin: 0 auto;">
				<thead>
					<tr>
						<th>Donaciones</th>
						<th>Aprobado</th>
						<th>Modificaciones</th>
						<th>Vigente</th>
						<th>Ejecución</th>
						<th>% de Ejecución</th>
					</tr>
				</thead>
				<tbody data-ng-repeat="donaciones in ctrlDEPM.donaciones track by $index">
					<tr>
						<td class="text-nowrap" ng-if="donaciones.nivel === 0" style="padding-left: 0px; font-weight: bold;">{{ donaciones.nombre }}</td>
						<td class="text-nowrap" ng-if="donaciones.nivel === 1" style="padding-left: 20px; font-size: medium;">{{ donaciones.nombre }}</td>
						<td class="text-nowrap" ng-if="donaciones.nivel === 2" style="padding-left: 40px; font-size: small;">{{ donaciones.nombre }}</td>
						<td class="text-right">{{ donaciones.asignado | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right">{{ donaciones.modificaciones | currency:"Q&nbsp;":2  }}</td>
						<td class="text-right">{{ donaciones.vigente | currency:"Q&nbsp;":2  }}</td>
						<td class="text-right">{{ donaciones.ejecutado | currency:"Q&nbsp;":2  }}</td>
						<td class="text-center">{{ donaciones.porcentaje | number:2}}&nbsp;%</td>
					</tr>
				</tbody>
				<tfoot>
					<tr style="font-weight: bold;">
						<td class="text-right"><strong>Totales</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.donaciones_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.donaciones_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center"><strong>{{ ctrlDEPM.donaciones_totales[2] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.donaciones_totales[3] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center"><strong>{{ ctrlDEPM.donaciones_totales[4]  | number:2}}&nbsp;%</strong></td>
					</tr>				</tfoot>
			</table>
		</div>
	</div>
	
	<br/><br/>
	
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 650px;">
				<h5 class="text-center">Ejecución por Eje Estratégico</h5>
				<h6 class="text-center">-Millones de quetzales-</h6>
				<div style="width: 600px; height: 350px;" class="div-center">
					<canvas id="ejecucion_ejes" height="350" width="600" class="chart chart-bar" chart-data="ctrlDEPM.chart_donaciones['data']"
										  chart-labels="ctrlDEPM.chart_donaciones['labels']" chart-legend="false" chart-series="ctrlDEPM.chart_donaciones['series']" 
										  chart-options="ctrlDEPM.chart_donaciones['options']"
										  chart-click="ctrlDEPM.onClick_chart_donaciones">
					</canvas>
				</div>
				<div class="chart-legend">
						<ul class="line-legend">
							<li ng-repeat="year in ctrlDEPM.chart_donaciones['series'] track by $index">
							<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ctrlDEPM.chart_colors[$index] }};"></div>
								{{ ctrlDEPM.chart_donaciones['series'][$index] }} 
							</li>
						</ul>
				</div>
			</div>
		</div>
	</div>
	
	<br/><br/>
	
	<div class="row">
		<div class="col-sm-12">
			<h5 class="text-center">Ejecución de Donaciones por entidad</h5>
			<br/>
			<table class="table table-hover" style="width: 90%; margin: 0 auto;">
				<thead>
					<tr>
						<th class="text-center">Entidad</th>
						<th class="text-center">Aprobado</th>
						<th class="text-center">Modificaciones</th>
						<th class="text-center">Vigente</th>
						<th class="text-center">Ejecución</th>
						<th class="text-center">% de Ejecución</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="entidad in ctrlDEPM.tabla_entidades track by $index">
						<td class="text-nowrap">{{ ctrlDEPM.tabla_entidades[$index].entidad_nombre }}</td>
						<td class="text-right">{{ ctrlDEPM.tabla_entidades[$index].asignado | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right">{{ ctrlDEPM.tabla_entidades[$index].modificaciones | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right">{{ ctrlDEPM.tabla_entidades[$index].vigente | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right">{{ ctrlDEPM.tabla_entidades[$index].ejecutado | currency:"Q&nbsp;":2  }}</td>
						<td class="text-center">{{ ctrlDEPM.tabla_entidades[$index].porcentaje | number:2}}&nbsp;%</td>
					</tr>
					<tr>
						<td class="text-right"><strong>Totales</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.tabla_entidades_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.tabla_entidades_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.tabla_entidades_totales[2] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right"><strong>{{ ctrlDEPM.tabla_entidades_totales[3] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-center"><strong>{{ ctrlDEPM.tabla_entidades_totales[4] | number:2 }}&nbsp;%</strong></td>
					</tr>
				</tbody>
			</table>
			<div style="width: 90%; margin: 0 auto;">Ejecución de Donaciones a nivel de entidad</div>
		</div>
	</div>
	
	<br/><br/>
	
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 600px; height: auto;">
				<h5 class="text-center">Donaciones Ejecutado por Organismo</h5>
				<h6 class="text-center">-Donación Ejecutado-</h6>
				<div style="width: 380px; height: 330px;" class="div-center">
				<canvas height="250" width="300" class="chart chart-pie" chart-data="ctrlDEPM.chart_organizaciones['data']"
					chart-labels="ctrlDEPM.chart_organizaciones['labels']" chart-legend="false" chart-series="ctrlDEPM.chart_organizaciones['series']" chart-options="ctrlDEPM.chart_organizaciones['options']">
				</canvas>
				</div>
				<div class="chart-legend">
					<ul class="line-legend" style="text-align: left; padding-left: 10%">
						<li ng-repeat="year in ctrlDEPM.chart_organizaciones['labels'] track by $index">
							<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ctrlDEPM.chart_colors[$index] }};"></div>
							{{ ctrlDEPM.chart_organizaciones['labels'][$index] }} - {{ ctrlDEPM.chart_organizaciones['legends'][$index] }} 
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<br/>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ ctrlDEPM.lastupdate }}</div>
		</div>
	</div>

</div>