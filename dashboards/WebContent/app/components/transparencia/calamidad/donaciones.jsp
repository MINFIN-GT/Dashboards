<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <div ng-controller="DonacionesCtrl as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de {{ control.tipo }} - {{ control.titulo }}</h3>
	<br/>
	<div class="row">
		<div class="col-sm-12">
			<h4>Donaciones</h4>
		</div>
	</div>
	
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px; height: 600px; position: relative; overflow: auto;">
		<table st-table="control.donaciones" st-safe-src="control.original_donaciones" class="table table-striped">
			<thead>
				<tr>
					<th st-sort="donante">Donante</th>
					<th st-sort="procedencia">Procedencia</th>
					<th st-sort="fecha_ingreso">Fecha de Ingreso</th>
					<th st-sort="monto_d">Monto en $</th>
					<th st-sort="monto_q">Monto en Q</th>
					<th st-sort="estado">Estado actual</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in control.donaciones">
					<td>{{ row.donante }}</td>
					<td>{{ row.procedencia }}</td>
					<td>{{ row.fecha_ingreso | date: 'd/M/yyyy' }}</td>
					<td style="min-width: 100px; text-align: right; white-space: nowrap;">$ {{ row.monto_d | number:2 }}</td>
					<td style="min-width: 100px; text-align: right; white-space: nowrap;">Q {{ row.monto_q | number:2 }}</td>
					<td>{{ row.estado }}</td>
				</tr>
			</tbody>
		</table>
		<div class="grid_loading" ng-hide="!control.showloading">
 			<div class="msg">
     			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
	  			<br /><br />
	  			<b>Cargando, por favor espere...</b>
  				</span>
			</div>
		</div>
	</div>
	<div style="text-align: right;">Total de {{ control.donaciones.length }} donaciones por un total de Q {{ control.total_donaciones | number:2 }}</div><br/><br/>
	</div>

