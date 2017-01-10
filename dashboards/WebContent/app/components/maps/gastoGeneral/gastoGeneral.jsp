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

<div ng-controller="mapsGastoGeneralController as mapsGG" class="maincontainer" id="gastogeograficomap" class="all_page">

	<script type="text/ng-template" id="infoGastoGeneral.jsp">
		<%@ include file="/app/components/maps/gastoGeneral/infoGastoGeneral.jsp"%>
    </script>
    
    <h4>Ejecución por Geográfico</h4>

	<div class="row">
		<div class="col-sm-12">
			<button type="button" class="btn btn-default no-border" style="font-size: 18px;" ng-click="mapsGG.panel_fuentes = !mapsGG.panel_fuentes" ng-disabled="mapsGG.showloading">{{ mapsGG.fuentes}} [ {{ mapsGG.fuentes_descripcion }} ]</button>
			<div uib-collapse="!mapsGG.panel_fuentes" style="margin: 10px 0px 0px 12px;" class="panel panel-default">
				<div style="text-align: right; margin: 5px;">
				<button type="button" class="btn btn-default" ng-click="mapsGG.checkAll(true)"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Todas</button> 
				<button type="button" class="btn btn-default" ng-click="mapsGG.checkAll(false)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>Ninguna</button>
				<button type="button" class="btn btn-default" ng-click="mapsGG.checkTributarias()"><span class="glyphicon glyphicon-tag" aria-hidden="true"></span>Tributarias</button>
				<button type="button" class="btn btn-default" ng-click="mapsGG.cargarGastos(true)"><span class="glyphicon glyphicon-play" aria-hidden="true"></span>Generar</button>
				</div>
				<div class="checkbox" style="margin-left: 12px;" ng-repeat="f in mapsGG.fuentes_array">
			     <label>
			          <input type="checkbox" ng-model="f.checked" ng-change="mapsGG.changeFuentes()">
			          {{ f.fuente }} {{ f.nombre }}
			        </label>
		        </div>
		    </div>
		 </div>
		 <div class="col-sm-12">
		    <button type="button" class="btn btn-default no-border" style="font-size: 18px;" ng-click="mapsGG.panel_grupos = !mapsGG.panel_grupos" ng-disabled="mapsGG.showloading">{{ mapsGG.grupos}} [ {{ mapsGG.grupos_descripcion }} ]</button>
		    <div uib-collapse="!mapsGG.panel_grupos" style="margin: 10px 0px 0px 12px;" class="panel panel-default">
				<div style="text-align: right; margin: 5px;">
				<button type="button" class="btn btn-default" ng-click="mapsGG.checkGruposAll(true)"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Todos</button> 
				<button type="button" class="btn btn-default" ng-click="mapsGG.checkGruposAll(false)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>Ninguno</button>
				<button type="button" class="btn btn-default" ng-click="mapsGG.cargarGastos(true)"><span class="glyphicon glyphicon-play" aria-hidden="true"></span>Generar</button>
				</div>
				<div class="checkbox" style="margin-left: 12px;" ng-repeat="g in mapsGG.grupos_array">
			     <label>
			          <input type="checkbox" ng-model="g.checked" ng-change="mapsGG.changeGrupos()">
			          {{ g.grupo }} {{ g.nombre }}
			        </label>
		        </div>
		    </div>
		</div>
	</div>

	<div class="row" style="margin-bottom: 10px;">
		<div class="col-sm-12">
			<div class="btn-group" uib-dropdown>
		      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="mapsGG.showloading" style="width: 150px; text-align: left; font-size: 24px;">
		        {{ mapsGG.nmonth }} <span class="caret"></span>
		      </button>
		      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(1)">Enero</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(2)">Febrero</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(3)">Marzo</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(4)">Abril</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(5)">Mayo</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(6)">Junio</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(7)">Julio</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(8)">Agosto</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(9)">Septiembre</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(10)">Octubre</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(11)">Noviembre</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.mesClick(12)">Diciembre</a></li>
		      </ul>
		    </div>
		    <div class="btn-group" uib-dropdown>
		      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="mapsGG.showloading" style="width: 100px; text-align: left; font-size: 24px;">
		        {{ mapsGG.ejercicio }} <span class="caret"></span>
		      </button>
		      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
		        <li role="menuitem"><a href ng-click="mapsGG.anoClick(2017)">2017</a></li>
		        <li role="menuitem"><a href ng-click="mapsGG.anoClick(2016)">2016</a></li>
		      </ul>
		    </div>
		    <span ng-show="mapsGG.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
	    </div>
	</div>
	
	<div class="row">
		<div class="col-sm-12 text-center">
			<div class="btn-group">
				<label class="btn btn-success activo" ng-model="mapsGG.mostrarPerCapita" uib-btn-radio="false" ng-click="mapsGG.cambiarTipo()">General</label>
			    <label class="btn btn-success activo" ng-model="mapsGG.mostrarPerCapita" uib-btn-radio="true" ng-click="mapsGG.cambiarTipo()">Per Cápita</label>
			</div>
		</div>
	</div>
	
	<br />
	
	<div style="position: relative; height: 700px;" id="title">
	
		<ui-gmap-google-map center="mapsGG.map.center" options="mapsGG.map.options" zoom="mapsGG.map.zoom">

			<ui-gmap-polygon ng-repeat="p in mapsGG.map.polygons" static="true" 
				path="p.path" stroke="p.stroke" visible="p.visible" geodesic="p.geodesic" fill="p.fill" fit="false" editable="p.editable" draggable="p.draggable" events="p.events">
			</ui-gmap-polygon> 

		</ui-gmap-google-map>

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
			<div class="col-sm-6">Última actualización: {{ mapsGG.lastupdate }}</div>
		</div>
</div>