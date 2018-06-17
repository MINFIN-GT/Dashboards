<%@page import="shiro.utilities.CShiro"%>
<%@page import="dao.CUserDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.bwindow {
		cursor:pointer; 
		margin: 0 auto;
	}
	
	.bwindow_title{
		text-align: center;
		font-weight: bold;
		font-size: 14px;
		margin-bottom: 15px;
	}
	
	table td{
		vertical-align: middle;
		padding: 15px;
	}
</style>
<div ng-controller="calamidadController as control" class="maincontainer all_page" id="title">
	<h3>Tablero de Seguimiento a Estado de {{ control.tipo }} - {{ control.titulo }}</h3>
	<br/>
	<div class="container container-fluid" style="height: 100%;">
		<div style="text-align: left; width: 100%; margin: auto;">
			<table style="width: 100%;">
				<tr>
					<td ng-click="go('/transparencia/calamidad/ejecucion/'+control.subprograma)">
						<div class="bwindow_title">Ejecución Física y Financiera</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp={{ control.subprograma }}&idevento=-1&pic=b1.png&pic_w=300" alt="Ejecución Física y Financiera" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 50%; height: 40px;">{{ control.ejecucion_financiera | number:2 }}% Financiera</button>
	  							<button type="button" class="btn btn-default" style="width: 50%; height: 40px;">{{ control.ejecucion_fisica | number:2 }}% Física</button>
							</div>
						</div>
					</td>
					<td ng-click="go('/transparencia/calamidad/actividades/'+control.subprograma)">
						<div class="bwindow_title">Actividades</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp={{ control.subprograma }}&idevento=-1&pic=b2.png&pic_w=300" alt="Actividades" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.num_actividades }} actividades</button>
	  						</div>
						</div>
					</td>
				</tr>
				<tr>
					<td ng-click="go('/transparencia/calamidad/mapa/'+control.subprograma)">
						<div class="bwindow_title">Mapa</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden; height: 190px;">
								<ui-gmap-google-map id="mapcalamidad" center="mapcalamidad.center" zoom="mapcalamidad.zoom" options="mapcalamidad.options">
								</ui-gmap-google-map>
							</div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.titulo }}</button>
							</div>
						</div>
					</td>
					<td ng-click="go('/transparencia/calamidad/documentos/'+control.subprograma)">
						<div class="bwindow_title">Documentos</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp={{ control.subprograma }}&idevento=-1&pic=b4.png&pic_w=300" alt="Documentos" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.num_documentos }} documentos</button>
	  						</div>
						</div>
					</td>
				</tr>
				<tr>
					<td ng-click="go('/transparencia/calamidad/compras/'+control.subprograma)">
						<div class="bwindow_title">Compras</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp={{ control.subprograma }}&idevento=-1&pic=b5.png&pic_w=300" alt="Compras" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.num_compras }} eventos en Guatecompras</button>
	  						</div>
						</div>						
					</td>
					<td ng-click="go('/transparencia/calamidad/donaciones/'+control.subprograma)">
						<div class="bwindow_title">Donaciones</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden; text-align: center;"><img src="/assets_public/icons/donaciones.png" alt="Donaciones" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.num_donaciones }} donaciones</button>
	  						</div>
						</div>						
					</td>
				</tr>
				<tr>
					<% 
					String url = request.getHeader("Referer");
					if(url.contains("main.jsp")){
						if(CUserDAO.hasUserPermission(CShiro.getIdUser(), "100")){ %>
						<td ng-click="go('/transparencia/calamidad/admin/'+control.subprograma)">
							<div class="bwindow_title">Administración</div>
							<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
								<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp={{ control.subprograma }}&idevento=-1&pic=b1.png&pic_w=300" alt="Administración" class="img-rounded"></div>
								<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
									<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">Administración</button>
		  						</div>
							</div>
						</td>
					<% }
					}
					%>
				</tr>
			</table>
		</div>
	</div>
</div>

