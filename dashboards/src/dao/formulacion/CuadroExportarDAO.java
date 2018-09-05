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
	public static ArrayList<ArrayList<?>> generarDatos(Integer ejercicio){
		ArrayList<ArrayList<?>> datos = new ArrayList<>();
		
		try {
			//Cuadro 3
			ArrayList<CRecursoEconomico> eRecursoTotal = CRecursoDAO.getRecursosTotal(ejercicio);
			datos.add(eRecursoTotal);

			//Cuadro 4
			ArrayList<CGastoEconomico> eGastoTotal = CGastoDAO.getGastosTotal(ejercicio);
			datos.add(eGastoTotal);

			//Cuadro 5
			ArrayList<CInstitucionalTotal> eInstitucionalTotal = CInstitucionalDAO.getInstitucionalTotal(ejercicio);
			datos.add(eInstitucionalTotal);

			//Cuadro 6
			ArrayList<CInstitucionalTipoGasto> eInstitucionalTipoGasto = CInstitucionalDAO.getInstitucionalTipoGasto(ejercicio);
			datos.add(eInstitucionalTipoGasto);
			
			//Cuadro 7
			ArrayList<CInstitucionalTipoGastoGrupoGasto> eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 10);
			datos.add(eInstitucionalTipoGastoGrupoGasto);
			eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 20);
			datos.add(eInstitucionalTipoGastoGrupoGasto);
			eInstitucionalTipoGastoGrupoGasto = CInstitucionalDAO.getInstitucionalTipoGastoGrupoGasto(ejercicio, 30);
			datos.add(eInstitucionalTipoGastoGrupoGasto);

			//Cuadro 8
			ArrayList<CInstitucionalFinalidad> eInstitucionalFinalidad = CInstitucionalDAO.getInstitucionalFinalidad(ejercicio);
			datos.add(eInstitucionalFinalidad);

			//Cuadro 9
			ArrayList<CFinalidadEconomico> finalidades = CFinalidadDAO.getFinalidadRecurso(ejercicio);
			datos.add(finalidades);

			//Cuadro 10
			ArrayList<CInstitucionalTipoGastoRegion> entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,10);
			datos.add(entidades);
			entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,20);
			datos.add(entidades);
			entidades = CInstitucionalDAO.getInstitucionalTipoGastoRegion(ejercicio,30);
			datos.add(entidades);

			//Cuadro 11
			ArrayList<CFinalidadRegion> finalidadesRegion = CFinalidadDAO.getFinalidadRegion(ejercicio);
			datos.add(finalidadesRegion);
		}catch(Exception e) {
			CLogger.write("1", CuadroExportarDAO.class, e);
		}
		
		return datos;
	} 
}
