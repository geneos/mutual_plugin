/*
 *    El contenido de este fichero está sujeto a la  Licencia Pública openXpertya versión 1.1 (LPO)
 * en tanto en cuanto forme parte íntegra del total del producto denominado:  openXpertya, solución 
 * empresarial global , y siempre según los términos de dicha licencia LPO.
 *    Una copia  íntegra de dicha  licencia está incluida con todas  las fuentes del producto.
 *    Partes del código son CopyRight (c) 2002-2007 de Ingeniería Informática Integrada S.L., otras 
 * partes son  CopyRight (c) 2002-2007 de  Consultoría y  Soporte en  Redes y  Tecnologías  de  la
 * Información S.L.,  otras partes son  adaptadas, ampliadas,  traducidas, revisadas  y/o mejoradas
 * a partir de código original de  terceros, recogidos en el  ADDENDUM  A, sección 3 (A.3) de dicha
 * licencia  LPO,  y si dicho código es extraido como parte del total del producto, estará sujeto a
 * su respectiva licencia original.  
 *     Más información en http://www.openxpertya.org/ayuda/Licencia.html
 */



package ar.com.mutual.plugin.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.openXpertya.pos.exceptions.PosException;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.Env;
import org.openXpertya.util.DB;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 02.07.07
 * @author     Equipo de Desarrollo de openXpertya    
 */

public final class MCuotaSocial extends LP_G_Cuotasocial {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6215599179594921977L;
		
	public MCuotaSocial(Properties ctx, int G_Cuotasocial_ID,
			String trxName) {
		super(ctx, G_Cuotasocial_ID, trxName);
		// TODO Auto-generated constructor stub
	}	
		
	public MCuotaSocial(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Método que verifica si tiene la cuota del mes/año paga.
	 * @param mes
	 * @param año
	 * @return true si la cuota social del mes/año esta paga
	 * @return false si la cuota social del mes/año no esta paga
	 * 
	 * @autor Cooperativa Geneos 
	 * 
	 */
	
	public static boolean cuotaPaga(int mes, int anio, int socio) {
		
		String sql = "SELECT * FROM g_cuotasocial where mes = " + mes + " and anio = " + anio + " and C_BPartner_ID = " + socio;    
		System.out.println(sql);
		
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = DB.prepareStatement( sql );            
            rs = pstmt.executeQuery();

            if( rs.next()) {
            	rs.close();
                pstmt.close();
                pstmt = null;
                return true;
            } else {
            	rs.close();
                pstmt.close();
                pstmt = null;
                return false;
            }

        } catch( Exception e ) {
        	try {
				rs.close();
				pstmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}            
            pstmt = null;
        }

        return false;
    }	

		
}   // Mgcuotasocial



/*
 *  @(#)Mgcuotasocial.java   02.07.07
 * 
 *  Fin del fichero Mgcuotasocial.java
 *  
 *  Versión 2.2
 *
 */
