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

<div ng-controller="maparecomendadoDepartamentoController as control" class="maincontainer" id="gastodepartamentomap" class="all_page">
    <h4>Recomendado por Departamento</h4>
	
	<br />
	
	<div style="position: relative; height: 100%;" id="title">
		<ng-map zoom="{{ control.map_options.zoom }}" styles="{{ control.map_options.styles }}" map-type-id="ROADMAP" style="height: 100%;"
			center="{{ control.map_options.center }}" map-initialized="control.mapLoaded(map)">
		</ng-map>
	</div>
	<br/>
	<div style="text-align: center;">
		<p>
			<span tooltip-placement="top" uib-tooltip=" 0 - 1 por millar" class="glyphicon glyphicon-certificate" style="color: #d6f5d6"></span> Bajo  |  
			<span tooltip-placement="top" uib-tooltip=" 1 - 3 por millar" class="glyphicon glyphicon-certificate" style="color: #adebad"></span> Medio Bajo  |  
			<span tooltip-placement="top" uib-tooltip=" 3 - 5 por millar" class="glyphicon glyphicon-certificate" style="color: #85e085"></span> Medio  |  
			<span tooltip-placement="top" uib-tooltip=" 5 - 10 por millar" class="glyphicon glyphicon-certificate" style="color: #5cd65c"></span> Medio Alto  |  
			<span tooltip-placement="top" uib-tooltip=" mayor a 1 por ciento" class="glyphicon glyphicon-certificate" style="color: #47d147"></span> Alto
		</p>
	</div>
	
	<div class="row">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>
</div>