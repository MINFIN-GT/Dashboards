<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
	.event_title {
		font-size: 14px;
		font-weight: bold;
	}
	
	.cbox {
	    background-color: #fff;
	    border: 1px solid #d4d4d4;
	    border-radius: 2px;
	    padding: 12px;
	    -webkit-box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);
	    box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);
	}
	
	
	</style>
    <div ng-controller="estadoscalamidadController as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de Excepción</h3>
	<br/>
	<br/>
	<div ng-repeat="estado in control.estadoscalamidad" >
		<div ng-click="estado.link != '' ? control.redirect(estado.subprograma) : ''" class="cbox">
			<div class="success ng-scope ng-isolate-scope bounce-in" style="cursor: pointer;">
				<div class="timeline-panel" >
					<timeline-heading class="ng-scope">
						<div class="timeline-heading">
								<div class="row" style="margin: 0; display: flex; align-items: center;">	
									<div class="col-sm-6 ">
									    <div style="width: 100%; font-size: larger;" class="event_title ng-binding ng-scope">{{ estado.nombre }}</div>
											<p style="width: 100%" class="ng-scope">
									            <small class="text-muted ng-binding" style="font-size: larger;"><i class="glyphicon glyphicon-time"></i>{{ estado.fecha_declaracion}}</small>
									         </p>
									      <p style="width: 100%" class="ng-binding ng-scope"><b>Estado de {{ estado.tipo }}</b></p>   
								          <p style="width: 100%" class="ng-binding ng-scope">Decreto: {{ estado.decreto }}</p>
								          <p style="width: 100%" class="ng-binding ng-scope">Programa: {{ estado.programa }}</p>
								          <p style="width: 100%" class="ng-binding ng-scope">Subprograma: {{ estado.subprograma }}</p>
							         </div>
						         <div class="col-sm-6">
						         			<div style="white-space: nowrap; text-align: right; font-size: 32px; color: green; font-weight: bold;">
						         				Q {{ estado.ejecucion }}
						         			</div>
						         			<div style="text-align: right; color: green;">Ejecución</div>
						         </div>
					        	</div>
					    </div>
					 </timeline-heading>
			</div>
			</div>
		</div>
		<br/>
	</div>
</div>

