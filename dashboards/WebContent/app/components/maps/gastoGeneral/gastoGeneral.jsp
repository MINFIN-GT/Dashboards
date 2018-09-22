<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>    
    .activo{
    	border: none; 
    	width: 200px; 
    	height: 40px; 
    	color: #000000; 
    	font-size: 24px; 
    	display: block; 
    	font-weight: 200; 
    	padding-top: 3px; 
    	background-color: #BDBDBD;
    }
</style>

<div ng-controller="mapsGastoGeneralController as control" class="maincontainer" id="gastogeograficomap" class="all_page">

	<script type="text/ng-template" id="infoGastoGeneral.jsp">
		<%@ include file="/app/components/maps/gastoGeneral/infoGastoGeneral.jsp"%>
    </script>
    
    <h4>Ejecución por Geográfico</h4>

	<div class="row">
		<div class="col-sm-12">
			<button type="button" class="btn btn-default no-border" style="font-size: 18px;" ng-click="control.panel_fuentes = !control.panel_fuentes" ng-disabled="control.showloading">{{ control.fuentes}} [ {{ control.fuentes_descripcion }} ]</button>
			<div uib-collapse="!control.panel_fuentes" style="margin: 10px 0px 0px 12px;" class="panel panel-default">
				<div style="text-align: right; margin: 5px;">
				<button type="button" class="btn btn-default" ng-click="control.checkAll(true)"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Todas</button> 
				<button type="button" class="btn btn-default" ng-click="control.checkAll(false)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>Ninguna</button>
				<button type="button" class="btn btn-default" ng-click="control.checkTributarias()"><span class="glyphicon glyphicon-tag" aria-hidden="true"></span>Tributarias</button>
				<button type="button" class="btn btn-default" ng-click="control.cargarGastos(true)"><span class="glyphicon glyphicon-play" aria-hidden="true"></span>Generar</button>
				</div>
				<div class="checkbox" style="margin-left: 12px;" ng-repeat="f in control.fuentes_array">
			     <label>
			          <input type="checkbox" ng-model="f.checked" ng-change="control.changeFuentes()">
			          {{ f.fuente }} {{ f.nombre }}
			        </label>
		        </div>
		    </div>
		 </div>
		 <div class="col-sm-12">
		    <button type="button" class="btn btn-default no-border" style="font-size: 18px;" ng-click="control.panel_grupos = !control.panel_grupos" ng-disabled="control.showloading">{{ control.grupos_gasto}} [ {{ control.grupos_gasto_descripcion }} ]</button>
		    <div uib-collapse="!control.panel_grupos" style="margin: 10px 0px 0px 12px;" class="panel panel-default">
				<div style="text-align: right; margin: 5px;">
				<button type="button" class="btn btn-default" ng-click="control.checkGruposAll(true)"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Todos</button> 
				<button type="button" class="btn btn-default" ng-click="control.checkGruposAll(false)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>Ninguno</button>
				<button type="button" class="btn btn-default" ng-click="control.cargarGastos(true)"><span class="glyphicon glyphicon-play" aria-hidden="true"></span>Generar</button>
				</div>
				<div class="checkbox" style="margin-left: 12px;" ng-repeat="g in control.grupos_array">
			     <label>
			          <input type="checkbox" ng-model="g.checked" ng-change="control.changeGrupos()">
			          {{ g.grupo }} {{ g.nombre }}
			        </label>
		        </div>
		    </div>
		</div>
	</div>

	<div class="row" style="margin-bottom: 10px;">
		<div class="col-sm-12">
			<div class="btn-group" uib-dropdown>
		      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="control.showloading" style="width: 150px; text-align: left; font-size: 24px;">
		        {{ control.nmonth }} <span class="caret"></span>
		      </button>
		      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
		        <li role="menuitem"><a href ng-click="control.mesClick(1)">Enero</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(2)">Febrero</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(3)">Marzo</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(4)">Abril</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(5)">Mayo</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(6)">Junio</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(7)">Julio</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(8)">Agosto</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(9)">Septiembre</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(10)">Octubre</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(11)">Noviembre</a></li>
		        <li role="menuitem"><a href ng-click="control.mesClick(12)">Diciembre</a></li>
		      </ul>
		    </div>
		    <div class="btn-group" uib-dropdown>
		      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="control.showloading" style="width: 100px; text-align: left; font-size: 24px;">
		        {{ control.ejercicio }} <span class="caret"></span>
		      </button>
		      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
		      	<li role="menuitem" ng-repeat="anio in control.anios">
		      		<a href ng-click="control.anoClick(anio)">{{ anio }}</a>
		      	</li>
		        </ul>
		    </div>
		    <span ng-show="control.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
	    </div>
	</div>
	
	<div class="row">
		<div class="col-sm-12 text-center">
			<div class="btn-group">
				<label class="btn btn-success btn-sm" ng-model="control.mostrarPerCapita" uib-btn-radio="false" ng-click="control.cambiarTipo()">General</label>
			    <label class="btn btn-success btn-sm" ng-model="control.mostrarPerCapita" uib-btn-radio="true" ng-click="control.cambiarTipo()">Per Cápita</label>
			</div>
		</div>
	</div>
	
	<br />
	
	<div style="position: relative; height: 100%;" id="title">
		<ng-map zoom="{{ control.map_options.zoom }}" styles="{{ control.map_options.styles }}" map-type-id="ROADMAP" style="height: 100%;" id="gastoGeneral"
			center="{{ control.map_options.center }}" map-initialized="control.mapLoaded(map)">
		</ng-map>
	</div>
	<br/>
	<div style="text-align: center;">
		<p>
			<span tooltip-placement="top" uib-tooltip=" 0 - 1 por millar" class="glyphicon glyphicon-certificate" style="color: #ff0000"></span> Bajo  |  
			<span tooltip-placement="top" uib-tooltip=" 1 - 3 por millar" class="glyphicon glyphicon-certificate" style="color: #ffdab9"></span> Medio Bajo  |  
			<span tooltip-placement="top" uib-tooltip=" 3 - 5 por millar" class="glyphicon glyphicon-certificate" style="color: #ffff00"></span> Medio  |  
			<span tooltip-placement="top" uib-tooltip=" 5 - 10 por millar" class="glyphicon glyphicon-certificate" style="color: #98fb98"></span> Medio Óptimo  |  
			<span tooltip-placement="top" uib-tooltip=" mayor a 1 por ciento" class="glyphicon glyphicon-certificate" style="color: #008000"></span> Óptimo
		</p>
	</div>
	
	<div class="row">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>
</div>