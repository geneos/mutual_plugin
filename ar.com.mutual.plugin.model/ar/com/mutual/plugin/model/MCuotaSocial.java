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
import java.util.Properties;
import java.util.logging.Level;

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
	
	
	public static String getLastCuotaByBPartner(Properties ctx, int MBPartner_ID, String trxName ) {
		//Trae la última cuota social paga de un determinado cliente
    	String retValue = null;
        String    sql      = "SELECT mes, anio FROM libertya.g_cuotasocial WHERE c_bpartner_id = ? " +
        					 "ORDER BY anio, mes DESC LIMIT 1";         	    	
        PreparedStatement pstmt = null;

        try {
            pstmt = DB.prepareStatement( sql );
            pstmt.setInt( 1,MBPartner_ID );
            ResultSet rs = pstmt.executeQuery();

            if( rs.next()) {
                retValue = rs.getString("mes") + "/" + rs.getString("Anio");
            } else {
                //s_log.log( Level.SEVERE,"Not found for MBPartner_ID=" + MBPartner_ID);                
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch( Exception e ) {
            //s_log.log( Level.SEVERE,sql,e );
        } finally {
            try {
                if( pstmt != null ) {
                    pstmt.close();
                }
            } catch( Exception e ) {
            }

            pstmt = null;
        }

        return retValue;
    }
	
	public static int getLastCuotaSocialID(Properties ctx, String trxName ) {
		//Trae el ultimo ID de Cuota Social
    	int retValue = 0;
        String    sql  	= "SELECT g_cuotasocial_id FROM libertya.g_cuotasocial ORDER BY g_cuotasocial_id DESC LIMIT 1";         	    	
        PreparedStatement pstmt = null;

        try {
            pstmt = DB.prepareStatement( sql );            
            ResultSet rs = pstmt.executeQuery();

            if( rs.next()) {
                retValue = rs.getInt("g_cuotasocial_id");
            } else {
                //s_log.log( Level.SEVERE,"Not found for MBPartner_ID=" + MBPartner_ID);                
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch( Exception e ) {
            //s_log.log( Level.SEVERE,sql,e );
        } finally {
            try {
                if( pstmt != null ) {
                    pstmt.close();
                }
            } catch( Exception e ) {
            }

            pstmt = null;
        }

        return retValue;
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
