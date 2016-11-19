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
	
<div ng-controller="mapsGastoCodedesMunisController as mapsGG" class="maincontainer" id="gastogeograficomap" class="all_page">

	<script type="text/ng-template" id="infoGastoCodedesMunis.jsp">
		<%@ include file="/app/components/maps/gastoCodedesMunis/infoGastoCodedesMunis.jsp"%>
    </script>
    
    <h4>Gasto CODEDES y Municipalidades por Geográfico</h4>
    

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
		    <span ng-show="mapsGG.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
	    </div>
	</div>
	<div class="row" style="margin-bottom: 10px;">
		<div class="col-sm-3">	    
		</div>	    
		<div class="col-sm-6" style="text-align: center;">	    
			 <div class="btn-group">
	        	<label class="btn btn-primary activo" ng-model="mapsGG.mostrarCodedes" ng-click="mapsGG.cambiarRenglon()" uib-btn-checkbox>CODEDE</label>
    	    	<label class="btn btn-success activo" ng-model="mapsGG.mostrarMunis" ng-click="mapsGG.cambiarRenglon()" uib-btn-checkbox>Municipalidad</label>
    		</div>
		</div>
	</div>
										
	
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
			<span tooltip-placement="top" uib-tooltip=" 0 - 2 por millar" class="glyphicon glyphicon-certificate" style="color: #ff0000"></span> Bajo  |  
			<span tooltip-placement="top" uib-tooltip=" 2 - 4 por millar" class="glyphicon glyphicon-certificate" style="color: #ffdab9"></span> Medio Bajo  |  
			<span tooltip-placement="top" uib-tooltip=" 4 - 6 por millar" class="glyphicon glyphicon-certificate" style="color: #ffff00"></span> Medio  |  
			<span tooltip-placement="top" uib-tooltip=" 6 - 8 por millar" class="glyphicon glyphicon-certificate" style="color: #98fb98"></span> Medio Óptimo  |  
			<span tooltip-placement="top" uib-tooltip=" mayor al 8 por millar" class="glyphicon glyphicon-certificate" style="color: #008000"></span> Óptimo
		</p>
	</div>
	
	<div class="row">
			<div class="col-sm-6">Última actualización: {{ mapsGG.lastupdate }}</div>
		</div>
</div>