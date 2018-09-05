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
	public static ArrayList<ArrayList<?>> generarDatos(Integer ejercicio, Integer numeroCuadro){
		ArrayList<ArrayList<?>> datos = new ArrayList<>();
		
		try {
			/*if(numeroCuadro == -1 || numeroCuadro == 1) {
				datos.add(null);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 2) {
				datos.add(null);
			}*/
			if(numeroCuadro ==-1 || numeroCuadro == 3) {
				ArrayList<CRecursoEconomico> eRecursoTotal = CRecursoDAO.getRecursosTotal(ejercicio);
				datos.add(eRecursoTotal);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 4) {
				ArrayList<CGastoEconomico> eGastoTotal = CGastoDAO.getGastosTotal(ejercicio);
				datos.add(eGastoTotal);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 5) {
				ArrayList<CInstitucionalTotal> eInstitucionalTotal = CInstitucionalDAO.getInstitucionalTotal(ejercicio);
				datos.add(eInstitucionalTotal);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 6) {
				ArrayList<CInstitucionalTipoGasto> eInstitucionalTipoGasto = CInstitucionalDAO.getInstitucionalTipoGasto(ejercicio);
				datos.add(eInstitucionalTipoGasto);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 7) {				
				ArrayList<CInstitucionalTipoGastoGrupoGasto> eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 10);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
				eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 20);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
				eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 30);
				datos.add(eInstitucionalTipoGastoGrupoGasto);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 8) {
				ArrayList<CInstitucionalFinalidad> eInstitucionalFinalidad = CInstitucionalDAO.getInstitucionalFinalidad(ejercicio);
				datos.add(eInstitucionalFinalidad);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 9) {
				ArrayList<CFinalidadEconomico> finalidades = CFinalidadDAO.getFinalidadRecurso(ejercicio);
				datos.add(finalidades);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 10) {
				ArrayList<CInstitucionalTipoGastoRegion> entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,10);
				datos.add(entidades);
				entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,20);
				datos.add(entidades);
				entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,30);
				datos.add(entidades);
			}
			if(numeroCuadro ==-1 || numeroCuadro == 11) {
				ArrayList<CFinalidadRegion> finalidades = CFinalidadDAO.getFinalidadRegion(ejercicio);
				datos.add(finalidades);
			}		
		}catch(Exception e) {
			CLogger.write("1", CuadroExportarDAO.class, e);
		}
		
		return datos;
	} 
}
