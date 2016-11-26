<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal-header">
	<h3 class="modal-title">Detalle de la Ejecución</h3>
</div>
<div class="modal-body" id="modal-body">
	Municipio:<br />
	<table>
		<thead>
			<tr>
				<th colspan="3"
					style="text-align: center; text-transform: capitalize;">
					{{infoCtrl.data.nombre}} <hr /></th>

			</tr>
			<tr>
				<th width="200" style="text-align: center;">Población</th>
				<th width="200" style="text-align: center;">Ejecución Per Cápita</th>
				<th width="200" style="text-align: center;">Ejecución</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="text-align: center;">{{infoCtrl.data.poblacion | number }}</td>
				<td style="text-align: center;">{{ infoCtrl.data.gastoPerCapita | currency: 'Q. '}}</td>
				<td style="text-align: center;">{{infoCtrl.data.gasto | currency: 'Q. '}}</td>
			</tr>
		</tbody>
	</table>
		<button class="btn btn-primary" type="button" ng-click="infoCtrl.getGasto(1)">ENTIDADES</button>
		<button class="btn btn-primary" type="button" ng-click="infoCtrl.getGasto(2)">UNIDADES_EJECUTORAS</button>
		<button class="btn btn-primary" type="button" ng-click="infoCtrl.getGasto(3)">PROGRAMAS</button>
		<button class="btn btn-primary" type="button" ng-click="infoCtrl.getGasto(4)">SUBPROGRAMS</button>
		<button class="btn btn-primary" type="button" ng-click="infoCtrl.getGasto(5)">PROYECTOS</button>
		<button class="btn btn-primary" type="button" ng-click="infoCtrl.getGasto(6)">ACTIVIDADES_OBRAS</button>
</div>
<div class="modal-footer">
	<button class="btn btn-primary" type="button" ng-click="infoCtrl.ok()">OK</button>
</div>