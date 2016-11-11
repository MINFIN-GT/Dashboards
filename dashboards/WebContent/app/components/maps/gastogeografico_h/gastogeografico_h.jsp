<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="gastogeograficomap" ng-controller="mapsGastoGeograficoController as mapsGG" class="all_page">

	<script type="text/ng-template" id="infoGastoGeografico.jsp">
		<%@ include file="/app/components/maps/gastogeografico_h/infoGastoGeografico.jsp"%>
    </script>
	<div style="position: relative; height: 700px;" id="title">
		<ui-gmap-google-map center="mapsGG.map.center" options="mapsGG.map.options" zoom="mapsGG.map.zoom">
			<!-- polygon example --> 
			<ui-gmap-polygon static="true" ng-repeat="p in mapsGG.muniPolygon" path="p.path" stroke="p.stroke" visible="p.visible" geodesic="p.geodesic" fill="p.fill" fit="false" editable="p.editable" draggable="p.draggable" events="p.events">
			</ui-gmap-polygon> 
		</ui-gmap-google-map>

	</div>
</div>