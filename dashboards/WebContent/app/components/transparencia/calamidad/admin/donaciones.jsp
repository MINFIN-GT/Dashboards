<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal-header">
	<h3 class="modal-title">Edición de Donaciones</h3>
</div>
<div class="modal-body" style="margin-left: 15px; margin-right: 15px;">
	<uib-accordion>
	<div style="padding-top: 5px;">
		<div uib-accordion-group class="panel-info row"
			heading="Agregar donación">
			<form name="myForm" class="css-form" novalidate style="margin-top: 15px;">
				<div class="form-group" style="text-align: left">
					<label for="donante">Donante</label>
					<input type="text" class="form-control" id="donante" placeholder="Donante" ng-model="donante" ng-change="clearError()" required />
				</div>
				<div class="form-group" style="text-align: left">
					<label for="procedencia">Procedencia</label>
					<input type="text" class="form-control" id="procedencia" ng-model="procedencia" placeholder="Procedencia" ng-change="clearError()" required />
				</div>
				<div class="form-group" style="text-align: left">
					<label for="fecha_inbreso">Fecha de ingreso</label>
					<input type="date" class="form-control" id="fecha_ingreso" ng-model="fecha_ingreso" placeholder="Fecha de Ingreso" ng-change="clearError()" required />
				</div>
				<div class="form-group" style="text-align: left;">
					<label class="control-label ">Método de Acreditamiento</label>
					<label class="radio-inline"><input type="radio" ng-model="metodo_acredita" value="Depósito" ng-change="clearError()" />Depósito</label>
					<label class="radio-inline"><input type="radio" ng-model="metodo_acredita" value="LBTR" ng-change="clearError()" />LBTR</label>
					<label class="radio-inline"><input type="radio" ng-model="metodo_acredita" value="Cheque" ng-change="clearError()" />Cheque</label>
				</div>
				<div class="form-group" style="text-align: left;">
					<label for="monto_d">Monto $</label>
					<input type="number" class="form-control" ng-model="monto_d" ng-change="clearError()" placeholder="Monto $" id="monto_d" required />
				</div>
				<div class="form-group" style="text-align: left;">
					<label for="monto_q">Monto Q</label>
					<input type="number" class="form-control" ng-model="monto_q" id="monto_q" placeholder="Monto Q" ng-change="clearError()" required />
				</div>
				<div class="form-group" style="text-align: left;">
					<label for="estado">Estado</label>
					<input type="text" class="form-control" id="estado" placeholder="Estado" ng-model="estado" ng-change="clearError()" />
				</div>
				<div class="form-group" style="text-align: left;">
					<label for="destino">Destino</label>
					<input type="text" class="form-control" ng-model="destino" id="destino" placeholder="Destino" ng-change="clearError()" />
				</div>
				<div class="form-group" style="text-align: center;">
					<div class="btn-group" role="group">
						<input type="button" value="Agregar" ng-disabled="!myForm.$valid" class="btn btn-success" ng-click="addDonacion()" />
					</div>
				</div>
				<div class="row" style="font-size:16px; text-align: center; color: red;" ng-show="error">
					<label><span class="glyphicon glyphicon-exclamation-sign"></span> {{errorMessage}}</label>
				</div>
				<div class="row" style="font-size:16px; text-align: center; color: green;" ng-show="success">
					<label>{{successMessage}}</label>
				</div>
			</form>
		</div>
	</div>
	</uib-accordion>

	<div class="row panel panel-default"
		style="margin: 10px 0px 20px 0px; height: 250px; position: relative; overflow: auto;">
		<table st-table="donaciones" st-safe-src="original_donaciones"
			class="table table-striped">
			<thead>
				<tr>
					<th st-sort="donante">Donante</th>
					<th st-sort="fecha_ingreso">Fecha Ingreso</th>
					<th st-sort="monto_d">Monto $</th>
					<th st-sort="monto_q">Monto Q</th>
					<th>Borrar</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in donaciones">
					<td style="text-align: left">{{ row.donante }}</td>
					<td style="text-align: right">{{ row.fecha_ingreso | date:'d/M/yyyy' }}</td>
					<td style="min-width: 100px; white-space: nowrap;">$ {{ row.monto_d|number:2 }}</td>
					<td style="min-width: 100px; white-space: nowrap;">Q {{ row.monto_q|number:2 }}</td>
					<td style="text-align: center;"><a
						ng-click="deleteDonacion(row.tipo, row.id)"><span style="color: red;"
							class="glyphicon glyphicon-remove"></span></a></td>
				</tr>
			</tbody>
		</table>
		<div class="grid_loading" ng-hide="!showloading">
 			<div class="msg">
     			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
	  			<br /><br />
	  			<b>Cargando, por favor espere...</b>
  				</span>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button class="btn btn-danger" type="button" ng-click="cancel()">Cerrar</button>
</div>