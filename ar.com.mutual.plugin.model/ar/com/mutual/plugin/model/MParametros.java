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

public final class MParametros extends LP_G_Parametros {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -6215599179594921977L;
		
	public MParametros(Properties ctx, int G_Parametros_ID,
			String trxName) {
		super(ctx, G_Parametros_ID, trxName);
		// TODO Auto-generated constructor stub
	}	
		
	public MParametros(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	
	public static String getParameterValueByName(Properties ctx, String parameterName, String trxName ) {
		// Trae el valor de un parámetro por su clave de búsqueda
	   
		String retValue = null;
		String sql =	"SELECT name " +
    				"FROM libertya.g_parametros " +
    				"WHERE value = '" + parameterName + "'";
        
       PreparedStatement pstmt = null;

        try {
            pstmt = DB.prepareStatement( sql );
            ResultSet rs = pstmt.executeQuery();

            if( rs.next()) {
                retValue = rs.getString(1);
            } else {
                //s_log.log( Level.SEVERE,"Not found for parameterName=" + parameterName);                
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
		
}   // MParametros



/*
 *  @(#)MParametros.java   02.07.07
 * 
 *  Fin del fichero MParametros.java
 *  
 *  Versión 2.2
 *
 */
