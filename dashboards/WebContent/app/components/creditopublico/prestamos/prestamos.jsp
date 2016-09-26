<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div ng-controller="pepm_Controller as ctrlPEPM" class="maincontainer" id="title" class="all_page">
	<h3>Ejeución de Prestamos</h3>
	<br/>
	
	<h4 style="width: 300px;">Por Préstamo y Entidad</h4>
	<br/>
	
	<div class="row">
		<div class="col-sm-12 col-centered">
			<table class="table table-hover" style="width: 90%; margin: 0 auto;">
				<thead>
					<tr>
						<th>Préstamos</th>
						<th>Aprobado</th>
						<th>Modificaciones</th>
						<th>Vigente</th>
						<th>Ejecución</th>
						<th>% de Ejecución</th>
					</tr>
				</thead>
				<tbody data-ng-repeat="prestamos in ctrlPEPM.prestamos track by $index">
					<tr ng-style="(prestamos.nivel==0) ? { 'font-weight' : 'bold' } : {}">
						<td class="text-nowrap" ng-if="prestamos.nivel === 0" style="padding-left: 0px; font-weight: bold;">{{ prestamos.nombre + " [" + prestamos.sigla + "]" }}</td>
						<td class="text-nowrap" ng-if="prestamos.nivel === 1" style="padding-left: 20px; font-size: medium;">{{ prestamos.nombre }}</td>
						<td class="text-nowrap" ng-if="prestamos.nivel === 2" style="padding-left: 40px; font-size: small;">{{ prestamos.nombre }}</td>
						<td class="text-right text-nowrap">{{ prestamos.asignado | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right text-nowrap">{{ prestamos.modificaciones | currency:"Q&nbsp;":2  }}</td>
						<td class="text-right text-nowrap">{{ prestamos.vigente | currency:"Q&nbsp;":2  }}</td>
						<td class="text-right text-nowrap">{{ prestamos.ejecutado | currency:"Q&nbsp;":2  }}</td>
						<td class="text-center text-nowrap" text-nowrap>{{ prestamos.porcentaje | number:2}}&nbsp;%</td>
					</tr>
				</tbody>
				<tfoot>
					<tr style="font-weight: bold;">
						<td class="text-right text-nowrap"><strong>Totales</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.prestamos_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.prestamos_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center text-nowrap"><strong>{{ ctrlPEPM.prestamos_totales[2] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.prestamos_totales[3] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center text-nowrap"><strong>{{ ctrlPEPM.prestamos_totales[4]  | number:2}}&nbsp;%</strong></td>
					</tr>				</tfoot>
			</table>
		</div>
	</div>
	
	<br/><br/>
	
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 650px;">
				<h5 class="text-center">Ejecución por Préstamo</h5>
				<h6 class="text-center">-Millones de quetzales-</h6>
				<div style="width: 600px; height: 350px;" class="div-center">
					<canvas id="ejecucion_ejes" height="350" width="600" class="chart chart-bar" chart-data="ctrlPEPM.chart_prestamos['data']"
										  chart-labels="ctrlPEPM.chart_prestamos['labels']" chart-legend="false" chart-series="ctrlPEPM.chart_prestamos['series']" 
										  chart-options="ctrlPEPM.chart_prestamos['options']"
										  chart-click="ctrlPEPM.onClick_chart_prestamos">
					</canvas>
				</div>
				<div class="chart-legend">
						<ul class="line-legend">
							<li ng-repeat="year in ctrlPEPM.chart_prestamos['series'] track by $index">
							<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ctrlPEPM.chart_colors[$index] }};"></div>
								{{ ctrlPEPM.chart_prestamos['series'][$index] }} 
							</li>
						</ul>
				</div>
			</div>
		</div>
	</div>
	
	<br/><br/>
	
	<div class="row">
		<div class="col-sm-12">
			<h5 class="text-center">Ejecución de Préstamos por Entidad</h5>
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
					<tr ng-repeat="entidad in ctrlPEPM.tabla_entidades track by $index">
						<td class="text-nowrap">{{ ctrlPEPM.tabla_entidades[$index].entidad_nombre }}</td>
						<td class="text-right text-nowrap">{{ ctrlPEPM.tabla_entidades[$index].asignado | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right text-nowrap">{{ ctrlPEPM.tabla_entidades[$index].modificaciones | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right text-nowrap">{{ ctrlPEPM.tabla_entidades[$index].vigente | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right text-nowrap">{{ ctrlPEPM.tabla_entidades[$index].ejecutado | currency:"Q&nbsp;":2  }}</td>
						<td class="text-center text-nowrap">{{ ctrlPEPM.tabla_entidades[$index].porcentaje | number:2}}&nbsp;%</td>
					</tr>
					<tr>
						<td class="text-right text-nowrap"><strong>Totales</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.tabla_entidades_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.tabla_entidades_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.tabla_entidades_totales[2] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right text-nowrap"><strong>{{ ctrlPEPM.tabla_entidades_totales[3] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-center text-nowrap"><strong>{{ ctrlPEPM.tabla_entidades_totales[4] | number:2 }}&nbsp;%</strong></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<br/><br/>
	
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 600px; height: auto;">
				<h5 class="text-center">Préstamos por Organismo Cooperante</h5>
				<h6 class="text-center">-Monto en millones de Quetzales-</h6>
				<div style="width: 380px; height: 330px;" class="div-center">
				<canvas height="250" width="300" class="chart chart-pie" chart-data="ctrlPEPM.chart_organizaciones['data']"
					chart-labels="ctrlPEPM.chart_organizaciones['labels']" chart-legend="false" chart-series="ctrlPEPM.chart_organizaciones['series']" chart-options="ctrlPEPM.chart_organizaciones['options']">
				</canvas>
				</div>
				<div class="chart-legend">
					<ul class="line-legend" style="text-align: left; padding-left: 10%">
						<li ng-repeat="year in ctrlPEPM.chart_organizaciones['labels'] track by $index">
							<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ctrlPEPM.chart_colors[$index] }};"></div>
							{{ ctrlPEPM.chart_organizaciones['legends'][$index] }} 
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<br/>
	
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ ctrlPEPM.lastupdate }}</div>
		</div>
	</div>

</div>