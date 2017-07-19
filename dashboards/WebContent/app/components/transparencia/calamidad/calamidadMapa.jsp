<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <div ng-controller="calamidadMapaController as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de {{ control.tipo }} - {{ control.titulo}}</h3>
	<br/>
	<div class="row">
		<div class="col-sm-6">
			<h4>Mapa de Actividades</h4>
		</div>
	</div>
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px; height: 500px; position: relative;">
		<ui-gmap-google-map id="mapcalamidad" center="mapcalamidad.center" zoom="mapcalamidad.zoom" options="mapcalamidad.options">
		  	<ui-gmap-marker ng-repeat="actividad in control.actividades" idkey="actividad.id" coords="actividad">
				                <ui-gmap-window show="actividad.showinfowindow" coords="actividad" isIconVisibleOnClick="false" options=""  ng-cloak>
						            <div>{{ actividad.nombre }}</div>
						        </ui-gmap-window> 
				            </ui-gmap-marker>
		</ui-gmap-google-map>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>		
	</div>
</div>

