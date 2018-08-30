package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
	
	public Workbook generateExcel(ArrayList<ArrayList<?>> lstDatos, Integer ejercicio) {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = null;
		try {
			String excelFilePath = "/TABLEROS/template_cuadros_globales.xlsx";
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            workbook = WorkbookFactory.create(inputStream);
            
			for(int i=0; i<lstDatos.size(); i++) {
				switch(i) {
				case 0:
					sheet = workbook.getSheetAt(i);
					break;
				case 1:
					sheet = workbook.getSheetAt(i);
					break;
				case 2:
					ArrayList<CRecursoEconomico> lstRecursoEconomico = (ArrayList<CRecursoEconomico>)lstDatos.get(i);					
					sheet = workbook.getSheetAt(i);
					
					/*//Encabezado		
					Cell cellDescripcion = sheet.getRow(4).getCell(1);
					cellDescripcion.setCellValue("Descripci�n");
					Cell cellEjecutadoDosA�os = sheet.getRow(4).getCell(2);
					cellEjecutadoDosA�os.setCellValue("Ejecutado " + (ejercicio-2));
					Cell cellAprobado = sheet.getRow(4).getCell(3);
					cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
					Cell cellRecomendado = sheet.getRow(4).getCell(3);
					cellRecomendado.setCellValue("Recomendado " + ejercicio);
					
					//Data
					for(int j = 0; j<lstRecursoEconomico.size(); j++) {
						
					}*/
					break;
				/*case 3:
					ArrayList<CGastoEconomico> lstGastoEconomico = (ArrayList<CGastoEconomico>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;*/
				case 4:
					ArrayList<CInstitucionalTotal> lstInstitucionalTotal = (ArrayList<CInstitucionalTotal>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					
					//Encabezado		
					Cell cellDescripcion = sheet.getRow(36).getCell(1);
					cellDescripcion.setCellValue("Instituci�n");
					Cell cellEjecutadoDosAnios = sheet.getRow(36).getCell(2);
					cellEjecutadoDosAnios.setCellValue("Ejecutado " + (ejercicio-2));
					Cell cellAprobado = sheet.getRow(36).getCell(3);
					cellAprobado.setCellValue("Aprobado " + (ejercicio-1) + " (*)");
					Cell cellRecomendado = sheet.getRow(36).getCell(3);
					cellRecomendado.setCellValue("Recomendado " + ejercicio);
					break;
				/*case 5:
					ArrayList<CInstitucionalTipoGasto> lstInstitucionalTipoGasto = (ArrayList<CInstitucionalTipoGasto>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;
				case 6:
					ArrayList<CInstitucionalTipoGastoGrupoGasto> lstInstitucionalTipoGastoGrupoGasto = (ArrayList<CInstitucionalTipoGastoGrupoGasto>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;
				case 7:
					ArrayList<CInstitucionalFinalidad> lstInstitucionalFinalidad = (ArrayList<CInstitucionalFinalidad>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;
				case 8:
					ArrayList<CInstitucionalTipoGastoRegion> lstInstitucionalTipoGastoRegion = (ArrayList<CInstitucionalTipoGastoRegion>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;
				case 9:
					ArrayList<CFinalidadRegion> lstFinalidadRegion = (ArrayList<CFinalidadRegion>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;
				case 10:
					ArrayList<CFinalidadEconomico> lstFinalidadEconomico = (ArrayList<CFinalidadEconomico>)lstDatos.get(i);
					sheet = workbook.getSheetAt(i);
					break;*/
				}
			}
		}catch(Exception e) {
			CLogger.write("1", CExcelFormulacion.class, e);
		}
		
		return workbook;
	}
}
