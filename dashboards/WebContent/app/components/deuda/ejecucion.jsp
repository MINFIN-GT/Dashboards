<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
	    .tab1{
			padding-left: 10px !important;
			font-weight: bold;
		}
		
		.tab2{
			padding-left: 30px !important;
			font-weight: bold;
		}
		
		.tab3{
			padding-left: 50px !important;
		}
    </style>
<div ng-controller="ejecuciondeudaController as ctrl" class="maincontainer" id="title" class="all_page">
	<h4>Deuda</h4>
	<h5>Ejecución Mensualizada</h5>
	<br/>
	<br/>
	<div class="row" style="margin-bottom: 10px;">
		<div class="col-sm-12">
			<div class="btn-group" uib-dropdown>
		      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="ejecucion.showloading" style="width: 100px; text-align: left; font-size: 24px;">
		        {{ ctrl.ano }} <span class="caret"></span>
		      </button>
		      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
		        <li role="menuitem" ng-repeat="ano in ctrl.anos_historia">
		        	<a href ng-click="ctrl.anoClick(ano)">{{ ano }}</a></li>
		      </ul>
		    </div>
		    <span ng-show="ctrl.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<div class="btn-group">
		        <label class="btn btn-primary" ng-model="ctrl.viewVigentes" uib-btn-checkbox>Vigentes</label>
		        <label class="btn btn-primary" ng-model="ctrl.viewQuetzales" uib-btn-checkbox>Quetzales</label>
		    </div>
		</div>
	</div>
	<div class="row">
		<table st-table="ctrl.displayedCollection" st-safe-src="ctrl.rowCollection" class="table table-striped" style="font-size: 12px; white-space: nowrap;">
			<thead>
				<tr>
					<th></th>
					<th></th>
					<th></th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Enero</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Febrero</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Marzo</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Abril</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Mayo</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Junio</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Julio</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Agosto</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Septimebre</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Octubre</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Noviembre</th>
					<th colspan="{{ ctrl.viewVigentes ? 2 : 1}}" class="text-center">Diciembre</th>
				</tr>
				<tr>
					<th>Código</th>
					<th>Nombre</th>
					<th>Asignado</th>
					<th ng-repeat-start="n in [].constructor(12) track by $index" ng-show="ctrl.viewVigentes" class="text-center">Vigente</th>
					<th ng-repeat-end class="text-center">Ejecutado</th>
				</tr>
			</thead>
			<tbody style="white-space: nowrap; text-align: right;">
				<tr st-select-row="row" ng-repeat="row in ctrl.displayedCollection" ng-click="ctrl.clickRow(row)" ng-show="row.visible">
					<td>{{ (row.nivel==1) ? row.actividad : ((row.nivel==2) ? row.fuente : row.renglon) }}</td>
					<td style="text-align: left;" class="{{ 'tab'+row.nivel }}"><span style="font-size: 8px; margin-right: 15px;"class="{{ row.nivel<3 ? (row.showChildren==false ? 'glyphicon glyphicon-plus' : 'glyphicon glyphicon-minus') : '' }}"></span>{{ (row.nivel==1) ? row.actividad_nombre : ((row.nivel==2) ? row.fuente_nombre : row.renglon_nombre) }}</td>
					<td>{{ ctrl.filtroQuetzales(row.asignado.toFixed(2), ctrl.viewQuetzales) }}</td>
					<td ng-repeat-start="n in [].constructor(12) track by $index" ng-show="ctrl.viewVigentes">{{ ctrl.filtroQuetzales(row.vigente_meses[$index].toFixed(2), ctrl.viewQuetzales) }}</td>
					<td ng-repeat-end>{{ ctrl.filtroQuetzales(row.ejecucion_meses[$index].toFixed(2), ctrl.viewQuetzales) }}</td>
				</tr>
			</tbody>
			<tfoot>
				<tr style="font-weight: bold;">
					<td></td>
					<td class="text-right">Total:</td>
					<td>{{ ctrl.filtroQuetzales(ctrl.total_asignado, ctrl.viewQuetzales) }}</td>
					<th ng-repeat-start="n in [].constructor(12) track by $index" ng-show="ctrl.viewVigentes" class="text-right">{{ ctrl.filtroQuetzales(ctrl.total_vigentes[$index], ctrl.viewQuetzales)  }}</th>
					<th ng-repeat-end class="text-right">{{ ctrl.filtroQuetzales(ctrl.total_ejecuciones[$index], ctrl.viewQuetzales) }}</th>
				</tr>
			</tfoot>
		</table>
	</div>
	<br/>
	<br/>
	<br/>
</div>