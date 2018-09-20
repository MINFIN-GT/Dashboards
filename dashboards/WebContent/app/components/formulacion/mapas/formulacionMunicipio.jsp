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

<div ng-controller="maparecomendadoMunicipioController as control" class="maincontainer" id="gastodepartamentomap" class="all_page">
    <h4>Recomendado por Municipio</h4>
	
	<br />
	
	<div style="position: relative; height: 100%;" id="title">
			<div class="col-sm-6" style="height: 100%">
				<ng-map style="height: 80%;" zoom="{{ control.map_options.zoom }}" styles="{{ control.map_options.styles }}" map-type-id="ROADMAP" style="height: 100%;"
					center="{{ control.map_options.center }}" map-initialized="control.mapLoaded(map)">
					<custom-control id="show_scale" position="BOTTOM_LEFT" index="1" style="z-index: 0; position: absolute; left: 75px; bottom: 0px;">
					      <div class="panel ng-scope" style="margin-top: 10px;">
					      	<table style="padding: 5px;">
					      		<tbody><tr>
					      			<td style="padding-left: 5px;">0.0 % - 1.0 %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #d6f5d6; font-family: FontAwesome"></i></td>
					      		</tr>
					      		<tr>
					      			<td style="padding-left: 5px;">1.0 % - 1.5 %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #adebad; font-family: FontAwesome"></i></td>
					      		</tr>
					      		<tr>
					      			<td style="padding-left: 5px;">1.5 % - 2.0 %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #85e085; font-family: FontAwesome"></i></td>
					      		</tr>
					      		<tr>
					      			<td style="padding-left: 5px;">2.0 % - 2.5 %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #5cd65c; font-family: FontAwesome"></i></td>
					      		</tr>
					      		<tr>
					      			<td style="padding-left: 5px;">Mayor a 2.5 %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #47d147; font-family: FontAwesome"></i></td>
					      		</tr>
					      	</tbody></table>
						  </div>
					  </custom-control>
				</ng-map>
			</div>
			<div class="col-sm-6">
			</div>
	</div>
	<br/>
	<div class="row">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>
</div>