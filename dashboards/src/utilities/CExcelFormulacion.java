package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import pojo.formulacion.CInstitucionalTotal;

public class CExcelFormulacion {	
	public CExcelFormulacion() {
		
	}
	
	Workbook workbook = null;
	
	public Workbook generateExcel(ArrayList<ArrayList<?>> lstDatos, Integer ejercicio, Integer numeroCuadro) {		
		DecimalFormat dosDecimales = new DecimalFormat("#.##");
		Sheet sheet = null;
		try {
			workbook = new HSSFWorkbook();
			String excelFilePath = "/TABLEROS/template_cuadros_globales.xls";
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            workbook = WorkbookFactory.create(inputStream);
            
			if(numeroCuadro == -1 || numeroCuadro == 0) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 1) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 2) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 3) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 4) {
				sheet = workbook.getSheetAt(4);
				int posicion = numeroCuadro != -1 ? 0 : 4;
				Object objLista = lstDatos.get(posicion);
				ArrayList<CInstitucionalTotal> lstInstitucionalTipoGasto = (ArrayList<CInstitucionalTotal>)objLista;
				
				//Encabezado		
				Cell cellDescripcion = sheet.getRow(36).getCell(1);
				cellDescripcion.setCellValue("Institución");
				Cell cellEjecutadoDosAnios = sheet.getRow(36).getCell(2);
				cellEjecutadoDosAnios.setCellValue("Ejecutado " + (ejercicio-2));
				Cell cellAprobado = sheet.getRow(36).getCell(3);
				cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
				Cell cellRecomendado = sheet.getRow(36).getCell(4);
				cellRecomendado.setCellValue("Recomendado " + ejercicio);
				
				for(int i=38; i< 60; i++) {
					if(i==38) {
						Cell desc = sheet.getRow(i).getCell(1);
						desc.setCellValue("total");
					}else {						
						
						Cell dataDescripcion = sheet.getRow(i).getCell(1);
						dataDescripcion.setCellValue(lstInstitucionalTipoGasto.get(i-39).getEntidad_nombre());
						Cell dataDosAniosAntes = sheet.getRow(i).getCell(2);
						dataDosAniosAntes.setCellValue(Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-39).getEjecutado_dos_antes()/1000000)));
						Cell dataAprobado = sheet.getRow(i).getCell(3);
						dataAprobado.setCellValue(Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-39).getAproobado_anterior()/1000000)));
						Cell dataRecomendado = sheet.getRow(i).getCell(4);
						dataRecomendado.setCellValue(Double.parseDouble(dosDecimales.format(lstInstitucionalTipoGasto.get(i-39).getRecomendado()/1000000)));
					}
				}
			}else if(numeroCuadro == -1 || numeroCuadro == 5) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 6) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 7) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 8) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 9) {
				
			}else if(numeroCuadro == -1 || numeroCuadro == 10) {
				
			}
			workbook.close();
		}catch(Exception e) {
			CLogger.write("1", CExcelFormulacion.class, e);
		}
		return workbook;
	}
}
