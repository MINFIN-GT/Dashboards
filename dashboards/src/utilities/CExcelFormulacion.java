package utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import dao.formulacion.CuadroExportarDAO;
import pojo.formulacion.CFinalidadEconomico;
import pojo.formulacion.CFinalidadRegion;
import pojo.formulacion.CGastoEconomico;
import pojo.formulacion.CInstitucionalFinalidad;
import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoGrupoGasto;
import pojo.formulacion.CInstitucionalTipoGastoRegion;
import pojo.formulacion.CInstitucionalTotal;
import pojo.formulacion.CRecursoEconomico;

public class CExcelFormulacion {	
	public CExcelFormulacion() {
		
	}
	
	public Workbook generateExcel(Integer ejercicio) {		
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = null;
		try {

			String excelFilePath = "/files/templates/tpl_cuadros_globales.xls";
			FileInputStream inputStream = new FileInputStream(excelFilePath);
            workbook = WorkbookFactory.create(inputStream);
            workbook.setForceFormulaRecalculation(true);
            
            //CUADRO_3
			sheet = workbook.getSheetAt(2);
			ArrayList<CRecursoEconomico> lstRecursoEconomico = CuadroExportarDAO.getLstRecursosTotal(ejercicio);
				
			//Encabezado		
			Cell cellEjecutadoDosAnios = sheet.getRow(5).getCell(2);
			cellEjecutadoDosAnios.setCellValue("Ejecutado " + (ejercicio-2));
			Cell cellAprobado = sheet.getRow(5).getCell(3);
			cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
			Cell cellRecomendado = sheet.getRow(5).getCell(4);
			cellRecomendado.setCellValue("Recomendado " + ejercicio);
				
			for(int i=7;i<=24; i++) {
				Cell dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-7).getTexto());
				
				Double valor = lstRecursoEconomico.get(i-7).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-7).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-7).getejecutado_dos_antes()/1000000 : null;
				Cell dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-7).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-7).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-7).getaprobado_anterior_mas_amp()/1000000 : null;
				Cell dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-7).getRecomendado() != null && lstRecursoEconomico.get(i-7).getRecomendado() != 0 ? lstRecursoEconomico.get(i-7).getRecomendado()/1000000 : null;
				Cell dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);	
			}
				
			for(int i=26;i<=30; i++) {
				Cell dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-8).getTexto());
				
				Double valor = lstRecursoEconomico.get(i-8).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-8).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-8).getejecutado_dos_antes()/1000000 : null;
				Cell dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-8).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-8).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-8).getaprobado_anterior_mas_amp()/1000000 : null;
				Cell dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-8).getRecomendado() != null && lstRecursoEconomico.get(i-8).getRecomendado() != 0 ? lstRecursoEconomico.get(i-8).getRecomendado()/1000000 : null;
				Cell dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);	
			}
				
			//Contribuciones a la seguridad social
			Cell dataDescripcion = sheet.getRow(32).getCell(1);
			dataDescripcion.setCellValue(lstRecursoEconomico.get(23).getTexto());
			
			Double valor = lstRecursoEconomico.get(23).getejecutado_dos_antes() != null && lstRecursoEconomico.get(23).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(23).getejecutado_dos_antes()/1000000 : null;
			Cell dataEjecutadoDosAntes = sheet.getRow(32).getCell(2);
			if(valor != null)
				dataEjecutadoDosAntes.setCellValue(valor);
			
			valor = lstRecursoEconomico.get(23).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(23).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(23).getaprobado_anterior_mas_amp()/1000000 : null;
			Cell dataAprobado = sheet.getRow(32).getCell(3);
			if(valor != null)
				dataAprobado.setCellValue(valor);
			
			valor = lstRecursoEconomico.get(23).getRecomendado() != null && lstRecursoEconomico.get(23).getRecomendado() != 0 ? lstRecursoEconomico.get(23).getRecomendado()/1000000 : null;
			Cell dataRecomendado = sheet.getRow(32).getCell(4);
			if(valor != null)
				dataRecomendado.setCellValue(valor);	
			
			//Ventas de bienes y servicios de la administración pública
			dataDescripcion = sheet.getRow(34).getCell(1);
			dataDescripcion.setCellValue(lstRecursoEconomico.get(24).getTexto());
			
			valor = lstRecursoEconomico.get(24).getejecutado_dos_antes() != null && lstRecursoEconomico.get(24).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(24).getejecutado_dos_antes()/1000000 : null;
			dataEjecutadoDosAntes = sheet.getRow(34).getCell(2);
			if(valor != null)
				dataEjecutadoDosAntes.setCellValue(valor);
			
			valor = lstRecursoEconomico.get(24).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(24).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(24).getaprobado_anterior_mas_amp()/1000000 : null;
			dataAprobado = sheet.getRow(34).getCell(3);
			if(valor != null)
				dataAprobado.setCellValue(valor);
			
			valor = lstRecursoEconomico.get(24).getRecomendado() != null && lstRecursoEconomico.get(24).getRecomendado() != 0 ? lstRecursoEconomico.get(24).getRecomendado()/1000000 : null;
			dataRecomendado = sheet.getRow(34).getCell(4);
			if(valor != null)
				dataRecomendado.setCellValue(valor);
			
			for(int i=36;i<=39;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-11).getTexto());
				
				valor = lstRecursoEconomico.get(i-11).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-11).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-11).getejecutado_dos_antes()/1000000 : null;
				dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-11).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-11).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-11).getaprobado_anterior_mas_amp()/1000000 : null;
				dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-11).getRecomendado() != null && lstRecursoEconomico.get(i-11).getRecomendado() != 0 ? lstRecursoEconomico.get(i-11).getRecomendado()/1000000 : null;
				dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);
			}
				
			for(int i=41;i<=43;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-12).getTexto());
				
				valor = lstRecursoEconomico.get(i-12).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-12).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-12).getejecutado_dos_antes()/1000000 : null;
				dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-12).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-12).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-12).getaprobado_anterior_mas_amp()/1000000 : null;
				dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-12).getRecomendado() != null && lstRecursoEconomico.get(i-12).getRecomendado() != 0 ? lstRecursoEconomico.get(i-12).getRecomendado()/1000000 : null;
				dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);
			}
			
			for(int i=45;i<=47;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-13).getTexto());
				
				valor = lstRecursoEconomico.get(i-13).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-13).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-13).getejecutado_dos_antes()/1000000 : null;
				dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-13).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-13).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-13).getaprobado_anterior_mas_amp()/1000000 : null;
				dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-13).getRecomendado() != null && lstRecursoEconomico.get(i-13).getRecomendado() != 0 ? lstRecursoEconomico.get(i-13).getRecomendado()/1000000 : null;
				dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);
			}
				
			for(int i=49;i<=51;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-14).getTexto());
				
				valor = lstRecursoEconomico.get(i-14).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-14).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-14).getejecutado_dos_antes()/1000000 : null;
				dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-14).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-14).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-14).getaprobado_anterior_mas_amp()/1000000 : null;
				dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-14).getRecomendado() != null && lstRecursoEconomico.get(i-14).getRecomendado() != 0 ? lstRecursoEconomico.get(i-14).getRecomendado()/1000000 : null;
				dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);
			}
				
			for(int i=53;i<=56;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstRecursoEconomico.get(i-15).getTexto());
				
				valor = lstRecursoEconomico.get(i-15).getejecutado_dos_antes() != null && lstRecursoEconomico.get(i-15).getejecutado_dos_antes() != 0 ? lstRecursoEconomico.get(i-15).getejecutado_dos_antes()/1000000 : null;
				dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-15).getaprobado_anterior_mas_amp() != null && lstRecursoEconomico.get(i-15).getaprobado_anterior_mas_amp() != 0 ? lstRecursoEconomico.get(i-15).getaprobado_anterior_mas_amp()/1000000 : null;
				dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstRecursoEconomico.get(i-15).getRecomendado() != null && lstRecursoEconomico.get(i-15).getRecomendado() != 0 ? lstRecursoEconomico.get(i-15).getRecomendado()/1000000 : null;
				dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);
			}

			//CUADRO_4
			sheet = workbook.getSheetAt(3);
			ArrayList<CGastoEconomico> lstGastoEconomico = CuadroExportarDAO.getLstGastoTotal(ejercicio);
			
			//Encabezado		
			cellEjecutadoDosAnios = sheet.getRow(5).getCell(2);
			cellEjecutadoDosAnios.setCellValue("Ejecutado " + (ejercicio-2));
			cellAprobado = sheet.getRow(5).getCell(3);
			cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
			cellRecomendado = sheet.getRow(5).getCell(4);
			cellRecomendado.setCellValue("Recomendado " + ejercicio);
			
			for(int i=7; i<=66;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstGastoEconomico.get(i-7).getTexto());
				
				valor = lstGastoEconomico.get(i-7).getejecutado_dos_antes() != null ? lstGastoEconomico.get(i-7).getejecutado_dos_antes()/1000000 : null;
				dataEjecutadoDosAntes = sheet.getRow(i).getCell(2);
				if(valor != null)
					dataEjecutadoDosAntes.setCellValue(valor);
				
				valor = lstGastoEconomico.get(i-7).getaprobado_anterior_mas_amp() != null ? lstGastoEconomico.get(i-7).getaprobado_anterior_mas_amp()/1000000 : null;
				dataAprobado = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAprobado.setCellValue(valor);
				
				valor = lstGastoEconomico.get(i-7).getRecomendado() != null ? lstGastoEconomico.get(i-7).getRecomendado()/1000000 : null;
				dataRecomendado = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRecomendado.setCellValue(valor);									
			}
		
			//CUADRO_5
			sheet = workbook.getSheetAt(4);
			ArrayList<CInstitucionalTotal> lstInstitucionalTotal = CuadroExportarDAO.getLstInstitucionalTotal(ejercicio);
			
			//Encabezado		
			Cell cellDescripcion = sheet.getRow(36).getCell(1);
			cellDescripcion.setCellValue("Institución");
			
			cellEjecutadoDosAnios = sheet.getRow(36).getCell(2);
			cellEjecutadoDosAnios.setCellValue("Ejecutado " + (ejercicio-2));
			cellAprobado = sheet.getRow(36).getCell(3);
			cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
			cellRecomendado = sheet.getRow(36).getCell(4);
			cellRecomendado.setCellValue("Recomendado " + ejercicio);
							
			//Tabla
			for(int i=39; i<= 57; i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstInstitucionalTotal.get(i-39).getEntidad_nombre());
				Cell dataDosAniosAntes = sheet.getRow(i).getCell(2);
				dataDosAniosAntes.setCellValue(lstInstitucionalTotal.get(i-39).getEjecutado_dos_antes()/1000000);
				dataAprobado = sheet.getRow(i).getCell(3);
				dataAprobado.setCellValue(lstInstitucionalTotal.get(i-39).getAproobado_anterior_mas_ampliaciones()/1000000);
				dataRecomendado = sheet.getRow(i).getCell(4);
				dataRecomendado.setCellValue(lstInstitucionalTotal.get(i-39).getRecomendado()/1000000);
			}
		
			//CUADRO_5
			sheet = workbook.getSheetAt(5);
			ArrayList<CInstitucionalTipoGasto> lstInstitucionalTipoGasto = CuadroExportarDAO.getLstInstitucionalTipoGasto(ejercicio);
			
			//Encabezado		
			cellDescripcion = sheet.getRow(2).getCell(1);
			cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio + " por Tipo de Gasto");
			
			cellEjecutadoDosAnios = sheet.getRow(41).getCell(1);
			cellEjecutadoDosAnios.setCellValue("Presupuesto Recomendado " + ejercicio);
			
			//Tabla
			for(int i=47; i<= 65; i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstInstitucionalTipoGasto.get(i-47).getEntidad_nombre());
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp11_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp11_monto()/1000000 : null;
				Cell dataAdministracion = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataAdministracion.setCellValue(valor);
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp12_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp12_monto()/1000000 : null;
				Cell dataDesaHumano = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataDesaHumano.setCellValue(valor);
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp13_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp13_monto()/1000000 : null;
				Cell dataTransCorrientes = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataTransCorrientes.setCellValue(valor);
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp21_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp21_monto()/1000000 : null;
				Cell dataInvFisica = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataInvFisica.setCellValue(valor);
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp22_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp22_monto()/1000000 : null;
				Cell dataTransCapital = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataTransCapital.setCellValue(valor);
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp23_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp23_monto()/1000000 : null;
				Cell dataInvFinanciera = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataInvFinanciera.setCellValue(valor);
				
				valor = lstInstitucionalTipoGasto.get(i-47).getTp31_monto() != 0 ? lstInstitucionalTipoGasto.get(i-47).getTp31_monto()/1000000 : null;
				Cell dataDeudaPublica = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataDeudaPublica.setCellValue(valor);
			}
			
			//CUADRO_6
			sheet = workbook.getSheetAt(6);

			ArrayList<CInstitucionalTipoGastoGrupoGasto> lstFinanciamientoGrupoGasto = CuadroExportarDAO.getLstInstitucionalTipoGastoGrupoGasto(ejercicio, 10);
			ArrayList<CInstitucionalTipoGastoGrupoGasto> lstInversionGrupoGasto = CuadroExportarDAO.getLstInstitucionalTipoGastoGrupoGasto(ejercicio, 20);
			ArrayList<CInstitucionalTipoGastoGrupoGasto> lstDeudaGrupoGasto = CuadroExportarDAO.getLstInstitucionalTipoGastoGrupoGasto(ejercicio, 30);
			
			//Encabezado		
			cellDescripcion = sheet.getRow(2).getCell(1);
			cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio + " por Tipo de Gasto");
			
			//financiamiento
			for(int i=10; i<=27;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstFinanciamientoGrupoGasto.get(i-10).getEntidad_nombre());
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG0_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG0_monto()/1000000 : null;
				Cell dataServiciosPersonales = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataServiciosPersonales.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG1_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG1_monto()/1000000 : null;
				Cell dataServiciosNoPersonales = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataServiciosNoPersonales.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG2_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG2_monto()/1000000 : null;
				Cell dataMateySumi = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataMateySumi.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG3_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG3_monto()/1000000 : null;
				Cell dataPropPlanEquiInt = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataPropPlanEquiInt.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG4_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG4_monto()/1000000 : null;
				Cell dataTransCorrientes = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataTransCorrientes.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG5_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG5_monto()/1000000 : null;
				Cell dataTransCapital = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataTransCapital.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG6_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG6_monto()/1000000 : null;
				Cell dataActFinan = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataActFinan.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG7_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG7_monto()/1000000 : null;
				Cell dataDeudaPublica = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataDeudaPublica.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG8_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG8_monto()/1000000 : null;
				Cell dataOtrosGastos = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataOtrosGastos.setCellValue(valor);
				
				valor = lstFinanciamientoGrupoGasto.get(i-10).getG9_monto() != 0 ? lstFinanciamientoGrupoGasto.get(i-10).getG9_monto()/1000000 : null;
				Cell dataAsigGlob = sheet.getRow(i).getCell(12);
				if(valor != null)
					dataAsigGlob.setCellValue(valor);
			}
			
			//inversion
			for(int i=30; i<=47;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstInversionGrupoGasto.get(i-30).getEntidad_nombre());
				
				valor = lstInversionGrupoGasto.get(i-30).getG0_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG0_monto()/1000000 : null;
				Cell dataServiciosPersonales = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataServiciosPersonales.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG1_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG1_monto()/1000000 : null;
				Cell dataServiciosNoPersonales = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataServiciosNoPersonales.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG2_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG2_monto()/1000000 : null;
				Cell dataMateySumi = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataMateySumi.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG3_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG3_monto()/1000000 : null;
				Cell dataPropPlanEquiInt = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataPropPlanEquiInt.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG4_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG4_monto()/1000000 : null;
				Cell dataTransCorrientes = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataTransCorrientes.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG5_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG5_monto()/1000000 : null;
				Cell dataTransCapital = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataTransCapital.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG6_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG6_monto()/1000000 : null;
				Cell dataActFinan = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataActFinan.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG7_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG7_monto()/1000000 : null;
				Cell dataDeudaPublica = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataDeudaPublica.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG8_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG8_monto()/1000000 : null;
				Cell dataOtrosGastos = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataOtrosGastos.setCellValue(valor);
				
				valor = lstInversionGrupoGasto.get(i-30).getG9_monto() != 0 ? lstInversionGrupoGasto.get(i-30).getG9_monto()/1000000 : null;
				Cell dataAsigGlob = sheet.getRow(i).getCell(12);
				if(valor != null)
					dataAsigGlob.setCellValue(valor);
			}
			
			//deuda
			valor = lstDeudaGrupoGasto.get(0).getG0_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG0_monto()/1000000 : null;
			Cell dataServiciosPersonales = sheet.getRow(49).getCell(3);
			if(valor != null)
				dataServiciosPersonales.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG1_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG1_monto()/1000000 : null;
			Cell dataServiciosNoPersonales = sheet.getRow(49).getCell(4);
			if(valor != null)
				dataServiciosNoPersonales.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG2_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG2_monto()/1000000 : null;
			Cell dataMateySumi = sheet.getRow(49).getCell(5);
			if(valor != null)
				dataMateySumi.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG3_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG3_monto()/1000000 : null;
			Cell dataPropPlanEquiInt = sheet.getRow(49).getCell(6);
			if(valor != null)
				dataPropPlanEquiInt.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG4_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG4_monto()/1000000 : null;
			Cell dataTransCorrientes = sheet.getRow(49).getCell(7);
			if(valor != null)
				dataTransCorrientes.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG5_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG5_monto()/1000000 : null;
			Cell dataTransCapital = sheet.getRow(49).getCell(8);
			if(valor != null)
				dataTransCapital.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG6_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG6_monto()/1000000 : null;
			Cell dataActFinan = sheet.getRow(49).getCell(9);
			if(valor != null)
				dataActFinan.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG7_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG7_monto()/1000000 : null;
			Cell dataDeudaPublica = sheet.getRow(49).getCell(10);
			if(valor != null)
				dataDeudaPublica.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG8_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG8_monto()/1000000 : null;
			Cell dataOtrosGastos = sheet.getRow(49).getCell(11);
			if(valor != null)
				dataOtrosGastos.setCellValue(valor);
			
			valor = lstDeudaGrupoGasto.get(0).getG9_monto() != 0 ? lstDeudaGrupoGasto.get(0).getG9_monto()/1000000 : null;
			Cell dataAsigGlob = sheet.getRow(49).getCell(12);
			if(valor != null)
				dataAsigGlob.setCellValue(valor);
		
		
			//CUADRO_7
			sheet = workbook.getSheetAt(7);
			
			ArrayList<CInstitucionalFinalidad> lstInstitucionalFinalidad = CuadroExportarDAO.getLstInstitucionalFinalidad(ejercicio);

			//Encabezado		
			cellDescripcion = sheet.getRow(2).getCell(1);
			cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio);
			
			for(int i=8; i<=26;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstInstitucionalFinalidad.get(i-8).getEntidad_nombre());
				
				valor = lstInstitucionalFinalidad.get(i-8).getF01_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF01_monto()/1000000 : null;
				Cell dataServiciosPublicosGen = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataServiciosPublicosGen.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF02_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF02_monto()/1000000 : null;
				Cell dataDefensa = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataDefensa.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF03_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF03_monto()/1000000 : null;
				Cell dataOrdPubSegCiudadana = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataOrdPubSegCiudadana.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF04_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF04_monto()/1000000 : null;
				Cell dataAtnDesastres = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataAtnDesastres.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF05_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF05_monto()/1000000 : null;
				Cell dataAsuntosEconomicos = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataAsuntosEconomicos.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF06_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF06_monto()/1000000 : null;
				Cell dataProtAmbiental = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataProtAmbiental.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF07_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF07_monto()/1000000 : null;
				Cell dataUrbServComun = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataUrbServComun.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF08_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF08_monto()/1000000 : null;
				Cell dataSalud = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataSalud.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF09_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF09_monto()/1000000 : null;
				Cell dataActDepRecreCultRel = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataActDepRecreCultRel.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF10_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF10_monto()/1000000 : null;
				Cell dataEducacion = sheet.getRow(i).getCell(12);
				if(valor != null)
					dataEducacion.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF11_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF11_monto()/1000000 : null;
				Cell dataProtSocial = sheet.getRow(i).getCell(13);
				if(valor != null)
					dataProtSocial.setCellValue(valor);
				
				valor = lstInstitucionalFinalidad.get(i-8).getF12_monto() != 0 ? lstInstitucionalFinalidad.get(i-8).getF12_monto()/1000000 : null;
				Cell dataTransDeudaPublica = sheet.getRow(i).getCell(14);
				if(valor != null)
					dataTransDeudaPublica.setCellValue(valor);
			}

			//CUADRO_8
			sheet = workbook.getSheetAt(8);
			
			ArrayList<CFinalidadEconomico> lstFinalidadEconomico = CuadroExportarDAO.getLstFinalidadRecurso(ejercicio);
			
			//Encabezado		
			cellDescripcion = sheet.getRow(2).getCell(1);
			cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio);
			
			for(int i=8; i<=19;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstFinalidadEconomico.get(i-8).getFinalidad_nombre());
				
				valor = lstFinalidadEconomico.get(i-8).getE1_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE1_monto()/1000000 : null;
				Cell dataRemuneraciones = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataRemuneraciones.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE2_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE2_monto()/1000000 : null;
				Cell dataBienesServicios = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataBienesServicios.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE3_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE3_monto()/1000000 : null;
				Cell dataRentasPropiedad = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataRentasPropiedad.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE4_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE4_monto()/1000000 : null;
				Cell dataPresSeguridadSocial = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataPresSeguridadSocial.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE5_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE5_monto()/1000000 : null;
				dataTransCorrientes = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataTransCorrientes.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE6_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE6_monto()/1000000 : null;
				Cell dataInvRealDirecta = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataInvRealDirecta.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE7_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE7_monto()/1000000 : null;
				dataTransCapital = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataTransCapital.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE8_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE8_monto()/1000000 : null;
				Cell dataInvFinanciera = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataInvFinanciera.setCellValue(valor);
				
				valor = lstFinalidadEconomico.get(i-8).getE9_monto() != 0 ? lstFinalidadEconomico.get(i-8).getE9_monto()/1000000 : null;
				Cell dataOtros = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataOtros.setCellValue(valor);
			}

			//CUADRO_9
			sheet = workbook.getSheetAt(9);
			
			ArrayList<CInstitucionalTipoGastoRegion> lstFinanciamientoTipoGastoRegion = CuadroExportarDAO.getLstInstitucionalTipoGastoRegion(ejercicio, 10);
			ArrayList<CInstitucionalTipoGastoRegion> lstInversionTipoGastoRegion = CuadroExportarDAO.getLstInstitucionalTipoGastoRegion(ejercicio, 20);
			ArrayList<CInstitucionalTipoGastoRegion> lstDeudaTipoGastoRegion = CuadroExportarDAO.getLstInstitucionalTipoGastoRegion(ejercicio, 30);
			
			//Encabezado		
			cellDescripcion = sheet.getRow(2).getCell(1);
			cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio);
			
			//financiamiento
			for(int i=9; i<=26;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstFinanciamientoGrupoGasto.get(i-9).getEntidad_nombre());
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR1_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR1_monto()/1000000 : null;
				Cell dataRegion1 = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataRegion1.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR2_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR2_monto()/1000000 : null;
				Cell dataRegion2 = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRegion2.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR3_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR3_monto()/1000000 : null;
				Cell dataRegion3 = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataRegion3.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR4_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR4_monto()/1000000 : null;
				Cell dataRegion4 = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataRegion4.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR5_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR5_monto()/1000000 : null;
				Cell dataRegion5 = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataRegion5.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR6_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR6_monto()/1000000 : null;
				Cell dataRegion6 = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataRegion6.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR7_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR7_monto()/1000000 : null;
				Cell dataRegion7 = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataRegion7.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR8_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR8_monto()/1000000 : null;
				Cell dataRegion8 = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataRegion8.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR9_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR9_monto()/1000000 : null;
				Cell dataRegion9 = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataRegion9.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR10_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR10_monto()/1000000 : null;
				Cell dataRegion10 = sheet.getRow(i).getCell(12);
				if(valor != null)
					dataRegion10.setCellValue(valor);
				
				valor = lstFinanciamientoTipoGastoRegion.get(i-9).getR11_monto() != 0 ? lstFinanciamientoTipoGastoRegion.get(i-9).getR11_monto()/1000000 : null;
				Cell dataRegion11 = sheet.getRow(i).getCell(13);
				if(valor != null)
					dataRegion11.setCellValue(valor);
			}
			
			//inversion
			for(int i=29; i<=46;i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstInversionGrupoGasto.get(i-29).getEntidad_nombre());
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR1_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR1_monto()/1000000 : null;
				dataServiciosPersonales = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataServiciosPersonales.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR2_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR2_monto()/1000000 : null;
				dataServiciosNoPersonales = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataServiciosNoPersonales.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR3_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR3_monto()/1000000 : null;
				dataMateySumi = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataMateySumi.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR4_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR4_monto()/1000000 : null;
				dataPropPlanEquiInt = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataPropPlanEquiInt.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR5_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR5_monto()/1000000 : null;
				dataTransCorrientes = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataTransCorrientes.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR6_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR6_monto()/1000000 : null;
				dataTransCapital = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataTransCapital.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR7_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR7_monto()/1000000 : null;
				dataActFinan = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataActFinan.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR8_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR8_monto()/1000000 : null;
				dataDeudaPublica = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataDeudaPublica.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR9_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR9_monto()/1000000 : null;
				dataOtrosGastos = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataOtrosGastos.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR10_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR10_monto()/1000000 : null;
				dataAsigGlob = sheet.getRow(i).getCell(12);
				if(valor != null)
					dataAsigGlob.setCellValue(valor);
				
				valor = lstInversionTipoGastoRegion.get(i-29).getR11_monto() != 0 ? lstInversionTipoGastoRegion.get(i-29).getR11_monto()/1000000 : null;
				Cell dataRegion11 = sheet.getRow(i).getCell(13);
				if(valor != null)
					dataRegion11.setCellValue(valor);
			}
			
			//deuda
			valor = lstDeudaTipoGastoRegion.get(0).getR1_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR1_monto()/1000000 : null;
			dataServiciosPersonales = sheet.getRow(48).getCell(3);
			if(valor != null)
				dataServiciosPersonales.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR2_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR2_monto()/1000000 : null;
			dataServiciosNoPersonales = sheet.getRow(48).getCell(4);
			if(valor != null)
				dataServiciosNoPersonales.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR3_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR3_monto()/1000000 : null;
			dataMateySumi = sheet.getRow(48).getCell(5);
			if(valor != null)
				dataMateySumi.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR4_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR4_monto()/1000000 : null;
			dataPropPlanEquiInt = sheet.getRow(48).getCell(6);
			if(valor != null)
				dataPropPlanEquiInt.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR5_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR5_monto()/1000000 : null;
			dataTransCorrientes = sheet.getRow(48).getCell(7);
			if(valor != null)
				dataTransCorrientes.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR6_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR6_monto()/1000000 : null;
			dataTransCapital = sheet.getRow(48).getCell(8);
			if(valor != null)
				dataTransCapital.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR7_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR7_monto()/1000000 : null;
			dataActFinan = sheet.getRow(48).getCell(9);
			if(valor != null)
				dataActFinan.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR8_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR8_monto()/1000000 : null;
			dataDeudaPublica = sheet.getRow(48).getCell(10);
			if(valor != null)
				dataDeudaPublica.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR9_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR9_monto()/1000000 : null;
			dataOtrosGastos = sheet.getRow(48).getCell(11);
			if(valor != null)
				dataOtrosGastos.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR10_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR10_monto()/1000000 : null;
			dataAsigGlob = sheet.getRow(48).getCell(12);
			if(valor != null)
				dataAsigGlob.setCellValue(valor);
			
			valor = lstDeudaTipoGastoRegion.get(0).getR11_monto() != 0 ? lstDeudaTipoGastoRegion.get(0).getR11_monto()/1000000 : null;
			Cell dataRegion11 = sheet.getRow(48).getCell(13);
			if(valor != null)
				dataRegion11.setCellValue(valor);
		
			//CUADRO_11
			sheet = workbook.getSheetAt(10);
			
			ArrayList<CFinalidadRegion> lstFinalidadRegion = CuadroExportarDAO.getLstFinalidadRegion(ejercicio);
			
			//Encabezado		
			cellDescripcion = sheet.getRow(2).getCell(1);
			cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio);
			
			for(int i=8; i<=19; i++) {
				dataDescripcion = sheet.getRow(i).getCell(1);
				dataDescripcion.setCellValue(lstFinalidadRegion.get(i-8).getFinalidad_nombre());
				
				valor = lstFinalidadRegion.get(i-8).getR1_monto() != 0 ? lstFinalidadRegion.get(i-8).getR1_monto()/1000000 : null;
				Cell dataRegion1 = sheet.getRow(i).getCell(3);
				if(valor != null)
					dataRegion1.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR2_monto() != 0 ? lstFinalidadRegion.get(i-8).getR2_monto()/1000000 : null;
				Cell dataRegion2 = sheet.getRow(i).getCell(4);
				if(valor != null)
					dataRegion2.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR3_monto() != 0 ? lstFinalidadRegion.get(i-8).getR3_monto()/1000000 : null;
				Cell dataRegion3 = sheet.getRow(i).getCell(5);
				if(valor != null)
					dataRegion3.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR4_monto() != 0 ? lstFinalidadRegion.get(i-8).getR4_monto()/1000000 : null;
				Cell dataRegion4 = sheet.getRow(i).getCell(6);
				if(valor != null)
					dataRegion4.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR5_monto() != 0 ? lstFinalidadRegion.get(i-8).getR5_monto()/1000000 : null;
				Cell dataRegion5 = sheet.getRow(i).getCell(7);
				if(valor != null)
					dataRegion5.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR6_monto() != 0 ? lstFinalidadRegion.get(i-8).getR6_monto()/1000000 : null;
				Cell dataRegion6 = sheet.getRow(i).getCell(8);
				if(valor != null)
					dataRegion6.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR7_monto() != 0 ? lstFinalidadRegion.get(i-8).getR7_monto()/1000000 : null;
				Cell dataRegion7 = sheet.getRow(i).getCell(9);
				if(valor != null)
					dataRegion7.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR8_monto() != 0 ? lstFinalidadRegion.get(i-8).getR8_monto()/1000000 : null;
				Cell dataRegion8 = sheet.getRow(i).getCell(10);
				if(valor != null)
					dataRegion8.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR9_monto() != 0 ? lstFinalidadRegion.get(i-8).getR9_monto()/1000000 : null;
				Cell dataRegion9 = sheet.getRow(i).getCell(11);
				if(valor != null)
					dataRegion9.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR10_monto() != 0 ? lstFinalidadRegion.get(i-8).getR10_monto()/1000000 : null;
				Cell dataRegion10 = sheet.getRow(i).getCell(12);
				if(valor != null)
					dataRegion10.setCellValue(valor);
				
				valor = lstFinalidadRegion.get(i-8).getR11_monto() != 0 ? lstFinalidadRegion.get(i-8).getR11_monto()/1000000 : null;
				dataRegion11 = sheet.getRow(i).getCell(13);
				if(valor != null)
					dataRegion11.setCellValue(valor);
			}
			inputStream.close();
		}catch(Exception e) {
			CLogger.write("1", CExcelFormulacion.class, e);
		}
		return workbook;
	}
}
