<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal-header">
	<h3 class="modal-title">Edición de CURs</h3>
</div>
<div class="modal-body" style="margin-left: 15px; margin-right: 15px;">
	<uib-accordion>
	<div style="padding-top: 5px;">
		<div uib-accordion-group class="panel-info row"
			heading="Agregar CUR de gasto">
			<form name="myForm" class="css-form" novalidate style="margin-top: 15px;">
				<div class="form-group row" style="text-align: left;">
					<label for="ejercicio">Ejercicio</label>
					<input type="number" ng-model="ejercicio" ng-change="clearError()" required class="form-control" id="ejercicio"/>
				</div>
				<div class="form-group row" style="text-align: left;">
					<label for="entidad">Entidad</label>
					<input type="number" ng-model="entidad" ng-change="clearError()" required class="form-control" id="entidad"/>
				</div>
				<div class="form-group row" style="text-align: left;">
					<label for="cur">Unidad Ejecutora</label>
					<input type="number" ng-model="unidad_ejecutora" ng-change="clearError()" required class="form-control" id="unidad_ejecutora"/>
				</div>
				<div class="form-group row" style="text-align: left;">
					<label for="cur">CUR</label>
					<input type="number" ng-model="idCur" ng-change="clearError()" required class="form-control" id="cur"/>
				</div>
				<div class="form-group row" style="text-align: center;">
					<div class="btn-group" role="group">
						<input type="button" value="Agregar" ng-disabled="!myForm.$valid" class="btn btn-success" ng-click="addCur()" />
					</div>
				</div>
				<div class="row" style="font-size:16px; text-align: center; color: red;" ng-show="error">
					<label><span class="glyphicon glyphicon-exclamation-sign">{{errorMessage}}</span> </label>
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
		<table st-table="curs" st-safe-src="original_curs"
			class="table table-striped">
			<thead>
				<tr>
					<th st-sort="ejercicio">Ejercicio</th>
					<th st-sort="entidad">Entidad</th>
					<th st-sort="unidad_ejecutora">Unidad Ejecutora</th>
					<th st-sort="cur">CUR</th>
					<th>Borrar</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in curs">
					<td>{{ row.ejercicio }}</td>
					<td>{{ row.entidad }}</td>
					<td>{{ row.unidad_ejecutora }}</td>
					<td>{{ row.cur }}</td>
					<td style="text-align: center;"><a
						ng-click="deleteCur(row.ejercicio, row.entidad, row.unidad_ejecutora, row.cur)"><span style="color: red;"
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