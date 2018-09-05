package dao.formulacion;

import java.util.ArrayList;

import pojo.formulacion.CFinalidadEconomico;
import pojo.formulacion.CFinalidadRegion;
import pojo.formulacion.CGastoEconomico;
import pojo.formulacion.CInstitucionalFinalidad;
import pojo.formulacion.CInstitucionalTipoGasto;
import pojo.formulacion.CInstitucionalTipoGastoGrupoGasto;
import pojo.formulacion.CInstitucionalTipoGastoRegion;
import pojo.formulacion.CInstitucionalTotal;
import pojo.formulacion.CRecursoEconomico;
import utilities.CLogger;

public class CuadroExportarDAO {
	//CUADRO_3
	public static ArrayList<CRecursoEconomico> getLstRecursosTotal(Integer ejercicio){
		ArrayList<CRecursoEconomico> recursoTotal = null;
		try {
			recursoTotal = CRecursoDAO.getRecursosTotal(ejercicio);
		}catch(Exception e) {
			CLogger.write("1", CuadroExportarDAO.class, e);
		}
		
		return recursoTotal;
	}
	
	//CUADRO_4
	public static ArrayList<CGastoEconomico> getLstGastoTotal(Integer ejercicio){
		ArrayList<CGastoEconomico> gastoTotal = null;
		try {
			gastoTotal = CGastoDAO.getGastosTotal(ejercicio);
		}catch(Exception e) {
			CLogger.write("2", CuadroExportarDAO.class, e);
		}
		
		return gastoTotal;
	}
	
	//CUADRO_5
	public static ArrayList<CInstitucionalTotal> getLstInstitucionalTotal(Integer ejercicio){
		ArrayList<CInstitucionalTotal> institucionalTotal = null;
		try {
			institucionalTotal = CInstitucionalDAO.getInstitucionalTotal(ejercicio);
		}catch(Exception e) {
			CLogger.write("3", CuadroExportarDAO.class, e);
		}
		
		return institucionalTotal;
	}
	
	//CUADRO_6
	public static ArrayList<CInstitucionalTipoGasto> getLstInstitucionalTipoGasto(Integer ejercicio){
		ArrayList<CInstitucionalTipoGasto> eInstitucionalTipoGasto = null;
		try {
			eInstitucionalTipoGasto = CInstitucionalDAO.getInstitucionalTipoGasto(ejercicio);
		}catch(Exception e) {
			CLogger.write("4", CuadroExportarDAO.class, e);
		}
		
		return eInstitucionalTipoGasto;
	}
	
	//CUADRO_7
	public static ArrayList<CInstitucionalTipoGastoGrupoGasto> getLstInstitucionalTipoGastoGrupoGasto(Integer ejercicio, Integer tipo_gasto){
		ArrayList<CInstitucionalTipoGastoGrupoGasto> institucionalTipoGastoGrupoGasto = null;
		try {
			institucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, tipo_gasto);
		}catch(Exception e) {
			CLogger.write("5", CuadroExportarDAO.class, e);
		}
		
		return institucionalTipoGastoGrupoGasto;
	}
	
	//CUADRO_8
	public static ArrayList<CInstitucionalFinalidad> getLstInstitucionalFinalidad(Integer ejercicio){
		ArrayList<CInstitucionalFinalidad> institucionalFinalidad = null;
		try {
			institucionalFinalidad = CInstitucionalDAO.getInstitucionalFinalidad(ejercicio);
		}catch(Exception e) {
			CLogger.write("6", CuadroExportarDAO.class, e);
		}
		
		return institucionalFinalidad;
	}
	
	//CUADRO_9
	public static ArrayList<CFinalidadEconomico> getLstFinalidadRecurso(Integer ejercicio){
		ArrayList<CFinalidadEconomico> finalidadesRecurso = null;
		try {
			finalidadesRecurso = CFinalidadDAO.getFinalidadRecurso(ejercicio);
		}catch(Exception e) {
			CLogger.write("7", CuadroExportarDAO.class, e);
		}
		
		return finalidadesRecurso;
	}
	
	//CUADRO_10
	public static ArrayList<CInstitucionalTipoGastoRegion> getLstInstitucionalTipoGastoRegion(Integer ejercicio, Integer tipo_gasto){
		ArrayList<CInstitucionalTipoGastoRegion> entidades = null;
		try {
			entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio, tipo_gasto);
		}catch(Exception e) {
			CLogger.write("8", CuadroExportarDAO.class, e);
		}
		
		return entidades;
	}
	
	//CUADRO_11
	public static ArrayList<CFinalidadRegion> getLstFinalidadRegion(Integer ejercicio){
		ArrayList<CFinalidadRegion> finalidadesRegion = null;
		try {
			finalidadesRegion = CFinalidadDAO.getFinalidadRegion(ejercicio);
		}catch(Exception e) {
			CLogger.write("9", CuadroExportarDAO.class, e);
		}
		
		return finalidadesRegion;
	}
}
