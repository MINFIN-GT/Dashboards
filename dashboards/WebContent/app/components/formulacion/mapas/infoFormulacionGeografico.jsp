<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal-header">
	<h3 class="modal-title">Detalle de Ejecución</h3>
</div>
<style type="text/css">
.odd {
	background-color: white;
	cursor: pointer;
}

.even {
	background-color: #F2F2F2;
	cursor: pointer;
}
</style>
<div class="modal-body" id="modal-body">
	Municipio:<br />
	<table>
		<thead>
			<tr>
				<th colspan="3"
					style="text-align: center; text-transform: capitalize;">
					{{infoCtrl.info.nombre}}
					<hr />
				</th>

			</tr>
			<tr>
				<th width="200" style="text-align: center;">Población</th>
				<th width="200" style="text-align: center;">Ejecución Per Cápita</th>
				<th width="200" style="text-align: center;">Ejecución</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="text-align: center;">{{infoCtrl.info.poblacion | number }}</td>
			<td style="text-align: center;">{{ infoCtrl.info.gastoPerCapita | currency: 'Q ' : 0 }}</td>
				<td style="text-align: center;">{{infoCtrl.info.gasto | currency: 'Q ' : 0}}</td>
			</tr>
		</tbody>
	</table>
	<br />

	<div
		style="overflow: scroll; width: 100%; border-style: inset;">
		<table>
			<thead style="display: block;">
				<tr style="background-color: #6E6E6E; color: white;">
					<th style="text-align: center; min-width: 100px; max-width: 100px;">Código</th>
					<th style="text-align: center; min-width: 300px; max-width: 300px;">Nombre</th>
					<th style="text-align: center; min-width: 150px; max-width: 150px;">Ejecutado</th>
					<th style="text-align: center; min-width: 150px; max-width: 150px;">Vigente</th>
					<th style="text-align: center; min-width: 100px; max-width: 100px;">% Anual</th>
				</tr>
			</thead>
			<tbody style="display: block; overflow-x: auto; height: 200px;">
				<tr ng-repeat="gasto in infoCtrl.gasto | orderBy:'codigo':false"
					ng-click="infoCtrl.nivel < 6 ? infoCtrl.getGasto(gasto.codigo) : return;"
					ng-class-odd="'odd'" ng-class-even="'even'">
					<td style="text-align: center; min-width: 100px; max-width: 100px;">{{gasto.codigo}}</td>
					<td style="text-align: left;  min-width: 300px; max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">{{gasto.nombre}}</td>
					<td style="text-align: right; min-width: 150px; max-width: 150px;">{{gasto.ejecutado | currency : 'Q ' : 0 }}</td>
					<td style="text-align: right; min-width: 150px; max-width: 150px;">{{gasto.vigente | currency : 'Q ' : 0 }}</td>
					<td style="text-align: right; min-width: 100px; max-width: 100px;">{{gasto.vigente == 0 ? 0.00 : gasto.ejecutado / gasto.vigente * 100 | number : 2 }}</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="modal-footer">
	<button style="float: left;" class="btn btn-primary" type="button" ng-show="infoCtrl.nivel > 1" ng-click="infoCtrl.nivel > 1 ? infoCtrl.back() : return;">Regresar</button>
	<button class="btn btn-primary" type="button" ng-click="infoCtrl.ok()">Cerrar</button>
</div>