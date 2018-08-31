package utilities;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoGrupoGasto;
import pojo.formulacion.CInstitucionalTotal;

public class CExcelFormulacion {	
	public CExcelFormulacion() {
		
	}
	
	public Workbook generateExcel(ArrayList<ArrayList<?>> lstDatos, Integer ejercicio, Integer numeroCuadro) {		
		DecimalFormat dosDecimales = new DecimalFormat("#.##");
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = null;
		try {

			String excelFilePath = "/TABLEROS/tpl_cuadros_globales.xls";
			FileInputStream inputStream = new FileInputStream(excelFilePath);
            workbook = WorkbookFactory.create(inputStream);
            workbook.setForceFormulaRecalculation(true);
            
			if(numeroCuadro == -1 || numeroCuadro == 1) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 2) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 3) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 4) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 5) {
				sheet = workbook.getSheetAt(4);
				Object objLista = lstDatos.get(0);
				ArrayList<CInstitucionalTotal> lstInstitucionalTotal = (ArrayList<CInstitucionalTotal>)objLista;
				
				//Encabezado		
				Cell cellDescripcion = sheet.getRow(36).getCell(1);
				cellDescripcion.setCellValue("Institución");
				
				Cell cellEjecutadoDosAnios = sheet.getRow(36).getCell(2);
				cellEjecutadoDosAnios.setCellValue("Ejecutado " + (ejercicio-2));
				Cell cellAprobado = sheet.getRow(36).getCell(3);
				cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
				Cell cellRecomendado = sheet.getRow(36).getCell(4);
				cellRecomendado.setCellValue("Recomendado " + ejercicio);
								
				//Tabla
				for(int i=39; i<= 57; i++) {
					Cell dataDescripcion = sheet.getRow(i).getCell(1);
					dataDescripcion.setCellValue(lstInstitucionalTotal.get(i-39).getEntidad_nombre());
					Cell dataDosAniosAntes = sheet.getRow(i).getCell(2);
					dataDosAniosAntes.setCellValue(Double.parseDouble(dosDecimales.format(lstInstitucionalTotal.get(i-39).getEjecutado_dos_antes()/1000000)));
					Cell dataAprobado = sheet.getRow(i).getCell(3);
					dataAprobado.setCellValue(Double.parseDouble(dosDecimales.format(lstInstitucionalTotal.get(i-39).getAproobado_anterior_mas_ampliaciones()/1000000)));
					Cell dataRecomendado = sheet.getRow(i).getCell(4);
					dataRecomendado.setCellValue(Double.parseDouble(dosDecimales.format(lstInstitucionalTotal.get(i-39).getRecomendado()/1000000)));
				}
			}else if(numeroCuadro == -1 || numeroCuadro == 6) {
				sheet = workbook.getSheetAt(5);
				Object objLista = lstDatos.get(1);
				ArrayList<CInstitucionalTipoGasto> lstInstitucionalTipoGasto = (ArrayList<CInstitucionalTipoGasto>)objLista;
				
				//Encabezado		
				Cell cellDescripcion = sheet.getRow(2).getCell(1);
				cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio + " por Tipo de Gasto");
				
				Cell cellEjecutadoDosAnios = sheet.getRow(41).getCell(1);
				cellEjecutadoDosAnios.setCellValue("Presupuesto Recomendado " + ejercicio);
				
				//Tabla
				for(int i=46; i<= 64; i++) {
					Cell dataDescripcion = sheet.getRow(i).getCell(1);
					dataDescripcion.setCellValue(lstInstitucionalTipoGasto.get(i-46).getEntidad_nombre());
					
					Double valor = lstInstitucionalTipoGasto.get(i-46).getTp11_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp11_monto()/1000000)) : null;
					Cell dataAdministracion = sheet.getRow(i).getCell(3);
					if(valor != null)
						dataAdministracion.setCellValue(valor);
					
					valor = lstInstitucionalTipoGasto.get(i-46).getTp12_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp12_monto()/1000000)) : null;
					Cell dataDesaHumano = sheet.getRow(i).getCell(4);
					if(valor != null)
						dataDesaHumano.setCellValue(valor);
					
					valor = lstInstitucionalTipoGasto.get(i-46).getTp13_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp13_monto()/1000000)) : null;
					Cell dataTransCorrientes = sheet.getRow(i).getCell(5);
					if(valor != null)
						dataTransCorrientes.setCellValue(valor);
					
					valor = lstInstitucionalTipoGasto.get(i-46).getTp21_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp21_monto()/1000000)) : null;
					Cell dataInvFisica = sheet.getRow(i).getCell(6);
					if(valor != null)
						dataInvFisica.setCellValue(valor);
					
					valor = lstInstitucionalTipoGasto.get(i-46).getTp22_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp22_monto()/1000000)) : null;
					Cell dataTransCapital = sheet.getRow(i).getCell(7);
					if(valor != null)
						dataTransCapital.setCellValue(valor);
					
					valor = lstInstitucionalTipoGasto.get(i-46).getTp23_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp23_monto()/1000000)) : null;
					Cell dataInvFinanciera = sheet.getRow(i).getCell(8);
					if(valor != null)
						dataInvFinanciera.setCellValue(valor);
					
					valor = lstInstitucionalTipoGasto.get(i-46).getTp31_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-46).getTp31_monto()/1000000)) : null;
					Cell dataDeudaPublica = sheet.getRow(i).getCell(9);
					if(valor != null)
						dataDeudaPublica.setCellValue(valor);
				}
			}else if(numeroCuadro == -1 || numeroCuadro == 7) {
				sheet = workbook.getSheetAt(6);
				Object objListaFinanciamiento = lstDatos.get(2);
				Object objListaInversion = lstDatos.get(3);
				Object objListaDeuda = lstDatos.get(4);
				
				ArrayList<CInstitucionalTipoGastoGrupoGasto> lstFinanciamientoGrupoGasto = (ArrayList<CInstitucionalTipoGastoGrupoGasto>)objListaFinanciamiento;
				ArrayList<CInstitucionalTipoGastoGrupoGasto> lstInversionGrupoGasto = (ArrayList<CInstitucionalTipoGastoGrupoGasto>)objListaInversion;
				ArrayList<CInstitucionalTipoGastoGrupoGasto> lstDeudaGrupoGasto = (ArrayList<CInstitucionalTipoGastoGrupoGasto>)objListaDeuda;
				
				//Encabezado		
				Cell cellDescripcion = sheet.getRow(2).getCell(1);
				cellDescripcion.setCellValue("Presupuesto Recomendado " + ejercicio + " por Tipo de Gasto");
				
				//financiamiento
				for(int i=10; i<=27;i++) {
					Cell dataDescripcion = sheet.getRow(i).getCell(1);
					dataDescripcion.setCellValue(lstFinanciamientoGrupoGasto.get(i-10).getEntidad_nombre());
					
					Double valor = lstFinanciamientoGrupoGasto.get(i-10).getG0_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG0_monto()/1000000)) : null;
					Cell dataServiciosPersonales = sheet.getRow(i).getCell(3);
					if(valor != null)
						dataServiciosPersonales.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG1_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG1_monto()/1000000)) : null;
					Cell dataServiciosNoPersonales = sheet.getRow(i).getCell(4);
					if(valor != null)
						dataServiciosNoPersonales.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG2_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG2_monto()/1000000)) : null;
					Cell dataMateySumi = sheet.getRow(i).getCell(5);
					if(valor != null)
						dataMateySumi.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG3_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG3_monto()/1000000)) : null;
					Cell dataPropPlanEquiInt = sheet.getRow(i).getCell(6);
					if(valor != null)
						dataPropPlanEquiInt.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG4_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG4_monto()/1000000)) : null;
					Cell dataTransCorrientes = sheet.getRow(i).getCell(7);
					if(valor != null)
						dataTransCorrientes.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG5_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG5_monto()/1000000)) : null;
					Cell dataTransCapital = sheet.getRow(i).getCell(8);
					if(valor != null)
						dataTransCapital.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG6_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG6_monto()/1000000)) : null;
					Cell dataActFinan = sheet.getRow(i).getCell(9);
					if(valor != null)
						dataActFinan.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG7_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG7_monto()/1000000)) : null;
					Cell dataDeudaPublica = sheet.getRow(i).getCell(10);
					if(valor != null)
						dataDeudaPublica.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG8_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG8_monto()/1000000)) : null;
					Cell dataOtrosGastos = sheet.getRow(i).getCell(11);
					if(valor != null)
						dataOtrosGastos.setCellValue(valor);
					
					valor = lstFinanciamientoGrupoGasto.get(i-10).getG9_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstFinanciamientoGrupoGasto.get(i-10).getG9_monto()/1000000)) : null;
					Cell dataAsigGlob = sheet.getRow(i).getCell(12);
					if(valor != null)
						dataAsigGlob.setCellValue(valor);
				}
				
				//inversion
				for(int i=30; i<=47;i++) {
					Cell dataDescripcion = sheet.getRow(i).getCell(1);
					dataDescripcion.setCellValue(lstInversionGrupoGasto.get(i-30).getEntidad_nombre());
					
					Double valor = lstInversionGrupoGasto.get(i-30).getG0_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG0_monto()/1000000)) : null;
					Cell dataServiciosPersonales = sheet.getRow(i).getCell(3);
					if(valor != null)
						dataServiciosPersonales.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG1_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG1_monto()/1000000)) : null;
					Cell dataServiciosNoPersonales = sheet.getRow(i).getCell(4);
					if(valor != null)
						dataServiciosNoPersonales.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG2_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG2_monto()/1000000)) : null;
					Cell dataMateySumi = sheet.getRow(i).getCell(5);
					if(valor != null)
						dataMateySumi.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG3_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG3_monto()/1000000)) : null;
					Cell dataPropPlanEquiInt = sheet.getRow(i).getCell(6);
					if(valor != null)
						dataPropPlanEquiInt.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG4_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG4_monto()/1000000)) : null;
					Cell dataTransCorrientes = sheet.getRow(i).getCell(7);
					if(valor != null)
						dataTransCorrientes.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG5_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG5_monto()/1000000)) : null;
					Cell dataTransCapital = sheet.getRow(i).getCell(8);
					if(valor != null)
						dataTransCapital.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG6_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG6_monto()/1000000)) : null;
					Cell dataActFinan = sheet.getRow(i).getCell(9);
					if(valor != null)
						dataActFinan.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG7_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG7_monto()/1000000)) : null;
					Cell dataDeudaPublica = sheet.getRow(i).getCell(10);
					if(valor != null)
						dataDeudaPublica.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG8_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG8_monto()/1000000)) : null;
					Cell dataOtrosGastos = sheet.getRow(i).getCell(11);
					if(valor != null)
						dataOtrosGastos.setCellValue(valor);
					
					valor = lstInversionGrupoGasto.get(i-30).getG9_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstInversionGrupoGasto.get(i-30).getG9_monto()/1000000)) : null;
					Cell dataAsigGlob = sheet.getRow(i).getCell(12);
					if(valor != null)
						dataAsigGlob.setCellValue(valor);
				}
				
				//deuda
				Double valor = lstDeudaGrupoGasto.get(0).getG0_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG0_monto()/1000000)) : null;
				Cell dataServiciosPersonales = sheet.getRow(49).getCell(3);
				if(valor != null)
					dataServiciosPersonales.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG1_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG1_monto()/1000000)) : null;
				Cell dataServiciosNoPersonales = sheet.getRow(49).getCell(4);
				if(valor != null)
					dataServiciosNoPersonales.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG2_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG2_monto()/1000000)) : null;
				Cell dataMateySumi = sheet.getRow(49).getCell(5);
				if(valor != null)
					dataMateySumi.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG3_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG3_monto()/1000000)) : null;
				Cell dataPropPlanEquiInt = sheet.getRow(49).getCell(6);
				if(valor != null)
					dataPropPlanEquiInt.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG4_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG4_monto()/1000000)) : null;
				Cell dataTransCorrientes = sheet.getRow(49).getCell(7);
				if(valor != null)
					dataTransCorrientes.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG5_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG5_monto()/1000000)) : null;
				Cell dataTransCapital = sheet.getRow(49).getCell(8);
				if(valor != null)
					dataTransCapital.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG6_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG6_monto()/1000000)) : null;
				Cell dataActFinan = sheet.getRow(49).getCell(9);
				if(valor != null)
					dataActFinan.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG7_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG7_monto()/1000000)) : null;
				Cell dataDeudaPublica = sheet.getRow(49).getCell(10);
				if(valor != null)
					dataDeudaPublica.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG8_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG8_monto()/1000000)) : null;
				Cell dataOtrosGastos = sheet.getRow(49).getCell(11);
				if(valor != null)
					dataOtrosGastos.setCellValue(valor);
				
				valor = lstDeudaGrupoGasto.get(0).getG9_monto() != 0 ? Double.parseDouble(dosDecimales.format(lstDeudaGrupoGasto.get(0).getG9_monto()/1000000)) : null;
				Cell dataAsigGlob = sheet.getRow(49).getCell(12);
				if(valor != null)
					dataAsigGlob.setCellValue(valor);
			}else if(numeroCuadro == -1 || numeroCuadro == 8) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 9) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 10) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 11) {
				
			}
			inputStream.close();
		}catch(Exception e) {
			CLogger.write("1", CExcelFormulacion.class, e);
		}
		return workbook;
	}
}
