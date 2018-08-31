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

<div ng-controller="maparecomendadoController as control" class="maincontainer" id="gastogeograficomap" class="all_page">

	<script type="text/ng-template" id="infoGastoGeneral.jsp">
		<%@ include file="/app/components/formulacion/mapas/infoFormulacionGeografico.jsp"%>
    </script>
    
    <h4>Recomendado por Geográfico</h4>
	
	<br />
	
	<div style="position: relative; height: 100%;" id="title">
		<ng-map zoom="{{ control.map_options.zoom }}" styles="{{ control.map_options.styles }}" map-type-id="ROADMAP" style="height: 100%;"
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