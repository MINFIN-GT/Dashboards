<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <div ng-controller="prestamosController as control" class="maincontainer" id="title" class="all_page">
	<h3>Tablero de Seguimiento a Prestamos</h3>
	<br/>
	<div><h4>Presupuesto de prestamos</h4></div>
	<div class="row">
		<div>
			<div style="padding: 15px;">
				<div class="panel panel-default">
					<div>
						<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(1, false)" ng-disabled="control.showloading"><b>Por Prestamo</b></a> 
						<span ng-hide="control.prestamo_level<2">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(2, false)" ng-disabled="control.showloading">{{ control.prestamo_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<3">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(3, false)" ng-disabled="control.showloading">{{ control.prestamo_entidad_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<4">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(4, false)" ng-disabled="control.showloading">{{ control.prestamo_unidad_ejecutora_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<5">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(5, false)" ng-disabled="control.showloading">{{ control.prestamo_programa_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<6">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(6, false)" ng-disabled="control.showloading">{{ control.prestamo_subprograma_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<7">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(7, false)" ng-disabled="control.showloading">{{ control.prestamo_proyecto_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<8">/ 
							<a href class="btn btn-default no-border" ng-click="control.prestamo_goLevel(8, false)" ng-disabled="control.showloading">{{ control.prestamo_actividad_nombre }}</a>
						</span>
						<span ng-hide="control.prestamo_level<9">/ &nbsp;{{ control.prestamo_renglon_nombre }}</span>
					</div>
			  		<div style="position: relative;">
						<table class="table table-striped" st-table="control.prestamo_ejecucion_data" 
							   st-safe-src="control.prestamo_ejecucion_data_original" >
							 <thead>
								<tr>
									<th></th>
									<th st-sort="codigo">Código</th>
									<th st-sort="nombre">Nombre</th>
									<th st-sort="vigente" style="text-align: right; ">Asignado</th>		
									<th st-sort="modificaciones" style="text-align: right; ">Modificaciones</th>									
									<th st-sort="vigente" style="text-align: right; ">Vigente</th>	
									<th st-sort="ejecutado" style="text-align: right;">Ejecutado</th>
									<th st-sort="eje_financiera" style="text-align: right;">% Ejecución Financiera</th>
								</tr>
							</thead>
							<tfoot>
							    <tr style="font-weight: bold;">
							    	<td/>
							    	<td/>
							    	<td class="text-right"><strong>Totales</strong></td>
									<td class="text-right"><strong>{{ control.prestamo_ejecucion_totales[0] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.prestamo_ejecucion_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.prestamo_ejecucion_totales[2] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.prestamo_ejecucion_totales[3] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.prestamo_ejecucion_totales[4] | number:2 }}&nbsp;%</strong></td>
								</tr>
							</tfoot>
							<tbody>
								<tr ng-repeat="data in control.prestamo_ejecucion_data">
									<td><button ng-show="control.prestamo_level<8" class="btn btn-primary" ng-click="control.prestamo_clickRow(data.codigo,data.nombre)"></button></td>
									<td class="text-nowrap"><strong>{{data.codigo}}</strong></td>
									<td title="{{data.nombre}}" style="max-width: 350px; overflow: hidden;" class="text-nowrap"><strong>{{data.nombre}}</strong></td>
									<td class="text-right">{{ data.asignado | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.modificaciones | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.vigente | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.ejecutado | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.ejecucion_financiera | number:2}}&nbsp;%</td>
								</tr>
							</tbody>
						</table>	
						<div class="grid_loading" ng-hide="!control.prestamo_showloading">
				  			<div class="msg">
				      			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  			<br /><br />
						  			<b>Cargando, por favor espere...</b>
					  			</span>
							</div>
			  			</div>					
					</div>
				</div >
				<div class="panel panel-default">
					<div>
						<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(1, false)" ng-disabled="control.showloading"><b>Por Entidad</b></a> 
						<span ng-hide="control.entidad_level<2">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(2, false)" ng-disabled="control.showloading">{{ control.entidad_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<3">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(3, false)" ng-disabled="control.showloading">{{ control.entidad_prestamo_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<4">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(4, false)" ng-disabled="control.showloading">{{ control.entidad_unidad_ejecutora_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<5">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(5, false)" ng-disabled="control.showloading">{{ control.entidad_programa_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<6">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(6, false)" ng-disabled="control.showloading">{{ control.entidad_subprograma_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<7">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(7, false)" ng-disabled="control.showloading">{{ control.entidad_proyecto_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<8">/ 
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(8, false)" ng-disabled="control.showloading">{{ control.entidad_actividad_nombre }}</a>
						</span>
						<span ng-hide="control.entidad_level<9">/ &nbsp;{{ control.entidad_renglon_nombre }}</span>
					</div>
			  		<div style="position: relative;">
						<table class="table table-striped" st-table="control.entidad_ejecucion_data" 
							   st-safe-src="control.entidad_ejecucion_data_original" >
							 <thead>
								<tr>
									<th></th>
									<th st-sort="codigo">Código</th>
									<th st-sort="nombre">Nombre</th>
									<th st-sort="vigente" style="text-align: right; ">Asignado</th>		
									<th st-sort="modificaciones" style="text-align: right; ">Modificaciones</th>									
									<th st-sort="vigente" style="text-align: right; ">Vigente</th>	
									<th st-sort="ejecutado" style="text-align: right;">Ejecutado</th>
									<th st-sort="eje_financiera" style="text-align: right;">% Ejecución Financiera</th>
								</tr>
							</thead>
							<tfoot>
							    <tr style="font-weight: bold;">
							    	<td/>
							    	<td/>
							    	<td class="text-right"><strong>Totales</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[0] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[2] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[3] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[4] | number:2 }}&nbsp;%</strong></td>
								</tr>
							</tfoot>
							<tbody>
								<tr ng-repeat="data in control.entidad_ejecucion_data">
									<td><button ng-show="control.entidad_level<8" class="btn btn-primary" ng-click="control.entidad_clickRow(data.codigo,data.nombre)"></button></td>
									<td class="text-nowrap"><strong>{{data.codigo}}</strong></td>
									<td title="{{data.nombre}}" style="max-width: 350px; overflow: hidden;" class="text-nowrap"><strong>{{data.nombre}}</strong></td>
									<td class="text-right">{{ data.asignado | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.modificaciones | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.vigente | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.ejecutado | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.ejecucion_financiera | number:2}}&nbsp;%</td>
								</tr>
							</tbody>
						</table>	
						<div class="grid_loading" ng-hide="!control.entidad_showloading">
				  			<div class="msg">
				      			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  			<br /><br />
						  			<b>Cargando, por favor espere...</b>
					  			</span>
							</div>
			  			</div>					
					</div>
				</div >
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>		
	</div>
</div>

