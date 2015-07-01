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



package ar.com.mutual.plugin.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.openXpertya.model.MProduct;
import org.openXpertya.model.MTab;
import org.openXpertya.process.ProcessInfoParameter;
import org.openXpertya.process.SvrProcess;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */

public class PriceGen extends SvrProcess {

    /**
     * Constructor de la clase ...
     *
     */

    public PriceGen() {
    	 super();
       
    }    // ProductPriceGen

    /** Descripción de Campos */

    private int AD_Client_ID = 103;

    /** Descripción de Campos */

    private int M_PriceList_Version_ID;

    /** Descripción de Campos */

    private int AD_Org_ID;

    /** Descripción de Campos */

    private int User;

    /** Descripción de Campos */

    private StringBuffer infoReturn;
    private MTab m_curTab;
    
    /** Incremento porcentual para el precio de los productos */

    private BigDecimal Incremento;    

    /**
     * Descripción de Método
     *
     */

    protected void prepare() {
    	log.info("GENEOS Estoy ProductGen.prepare");
        ProcessInfoParameter[] para = getParameter();
        
        AD_Client_ID = Env.getAD_Client_ID(this.getCtx());
        AD_Org_ID = Env.getAD_Org_ID(this.getCtx());
        User = Env.getAD_User_ID(this.getCtx());
        
        
        for( int i = 0;i < para.length;i++ ) {
            String name = para[ i ].getParameterName();

            if( para[ i ].getParameter() == null ) {
                ;
            } else if( name.equals( "M_PriceList_Version_ID" )) {
                M_PriceList_Version_ID = para[ i ].getParameterAsInt();
            } else if( name.equals( "Incremento" )) {
            	Incremento = new java.math.BigDecimal(para[ i ].getParameterAsInt());                
            } else {
                log.log( Level.SEVERE,"prepare - Unknown Parameter: " + name );
            }
        }
         
        infoReturn = new StringBuffer( "" );
    }    // prepare

    /**
     * Descripción de Método
     *
     *
     * @return
     */

    protected String doIt() {
        deleteMProductPrice();
        insertMProductPrice();

        return infoReturn.toString();
    }    // doIt

    /**
     * Descripción de Método
     *
     */

    public void deleteMProductPrice() {

    	log.info("GENEOS ProductGen.deleteMProductPrice con M_PriceList_Version_ID= "+ M_PriceList_Version_ID);
        try {
            StringBuffer sql = new StringBuffer( "DELETE FROM M_ProductPrice" );
            sql.append( " WHERE M_PriceList_Version_ID = " + M_PriceList_Version_ID );
            DB.executeUpdate( sql.toString());
        } catch( Exception e ) {
            log.log( Level.SEVERE,"ProductPriceGen - deleteMProductPrice; " + e );
        }
        
    }    // deleteMProductPrice

    /**
     * Descripción de Método
     *
     */

    public void insertMProductPrice() {
    	
    	log.info("GENEOS ProductGen.insertMProductPrice");
  
        PreparedStatement pstmt = null;
        ResultSet         rs;
        StringBuffer      sql;
        StringBuffer      i_sql;

        // Instanciar la Lista de Precios
        
        org.openXpertya.model.MPriceListVersion version = new org.openXpertya.model.MPriceListVersion(this.getCtx(),M_PriceList_Version_ID, null);
        
        
        
        
        try {
            sql = new StringBuffer( "SELECT M_Product_ID, PriceList, PriceStd, PriceLimit " );
            sql.append( " FROM M_ProductPrice WHERE M_PriceList_Version_ID=" + version.getM_Pricelist_Version_Base_ID());
            pstmt = DB.prepareStatement( sql.toString());
            rs    = pstmt.executeQuery();

            while( rs.next()) {
            	
            	//JOptionPane.showMessageDialog( null,"Insertado un producto en insertMProductPrice, para la version = "+ M_PriceList_Version_ID+"con M_Product_ID="+rs.getInt( 1 )+" SelectedPrice= "+rs.getInt( 2 )+"PriceStd= "+rs.getInt( 2 )+"PriceLimit= "+rs.getInt( 2 ),null, JOptionPane.INFORMATION_MESSAGE );
                MProduct prod = new MProduct( getCtx(),rs.getInt( 1 ),null );
                
                BigDecimal cien = new java.math.BigDecimal(100);
                
                BigDecimal PriceListMas =  rs.getBigDecimal(2).add((rs.getBigDecimal(2).multiply(Incremento).divide(cien)));
                BigDecimal PriceStdMas =  rs.getBigDecimal(2).add((rs.getBigDecimal(3).multiply(Incremento).divide(cien)));
                BigDecimal PriceLimitMas =  rs.getBigDecimal(2).add((rs.getBigDecimal(4).multiply(Incremento).divide(cien)));
                
                infoReturn.append( "<tr><td>" + rs.getInt( 1 ) + "</td><td>" + prod.getName() + "</td><td>" + rs.getBigDecimal( 2 ) + "</td></tr>" );
                i_sql = new StringBuffer( "INSERT INTO M_ProductPrice" );
                i_sql.append( " (M_PriceList_Version_ID, M_Product_ID, AD_Client_ID, AD_Org_ID, IsActive," );
                i_sql.append( " Created, CreatedBy, Updated , UpdatedBy, PriceList, PriceStd, PriceLimit) VALUES (" );
                i_sql.append( M_PriceList_Version_ID + ", " + rs.getInt( 1 ) + ", " + AD_Client_ID + ", " + AD_Org_ID + ", 'Y', SysDate, " );
                i_sql.append( User + ", SysDate, " + User + ", " + PriceListMas + ", " + PriceStdMas + ", " + PriceLimitMas + ")" );

                DB.executeUpdate( i_sql.toString());
                
            }
            

            rs.close();
            pstmt.close();
            pstmt = null;
        } catch( Exception e ) {
            log.log( Level.SEVERE,"ProductPriceGen - insertMProductPrice; " + e );
        }
    }    // insertMProductPrice
}    // ProductPriceGen



/*
 *  @(#)ProductPriceGen.java   02.07.07
 * 
 *  Fin del fichero ProductPriceGen.java
 *  
 *  Versión 2.2
 *
 */
