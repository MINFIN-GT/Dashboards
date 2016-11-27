<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal-header">
	<h3 class="modal-title">Detalle de Gasto</h3>
</div>
<div class="modal-body" id="modal-body">
	Municipio:<br />
	<div style="text-align: center; width: 100%;">
		<h4>{{infoCtrl.data.nombre}}</h4>
	</div>
	Ejecución:<br />
	<div style="text-align: center; width: 100%;">
		<b>{{infoCtrl.data.gasto | currency: 'Q. '}}</b>
	</div>
</div>
<div class="modal-footer">
	<button class="btn btn-primary" type="button" ng-click="infoCtrl.ok()">OK</button>
</div>