<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal-header">
	<h3 class="modal-title">Detalle de Gasto del Municipio </h3>
</div>
<div class="modal-body" id="modal-body">
	{{$ctrl.data.CODIGO}} - {{$ctrl.data.MUNICIPIO}}
</div>
<div class="modal-footer">
	<button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">OK</button>
	<button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Cancel</button>
</div>