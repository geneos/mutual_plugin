package ar.com.mutual.plugin.callout;




import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.openXpertya.model.MField;
import org.openXpertya.model.MTab;
import org.openXpertya.model.attribute.RecommendedAtributeInstance;
import org.openXpertya.plugin.CalloutPluginEngine;
import org.openXpertya.plugin.MPluginStatusCallout;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;
import org.openXpertya.util.Util;

import ar.com.mutual.plugin.model.MUOMConversion;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */

public class CalloutInOut extends CalloutPluginEngine {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     */

   

    
    

 

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param WindowNo
     * @param mTab
     * @param mField
     * @param value
     *
     * @return
     */

    public MPluginStatusCallout qty( Properties ctx,int WindowNo,MTab mTab,MField mField,Object value ) {
    	state.setContinueStatus(MPluginStatusCallout.STATE_TRUE_AND_SKIP);
    	if( isCalloutActive() || (value == null) ) {
            return state;
        }

        setCalloutActive( true );

        int M_Product_ID = Env.getContextAsInt( ctx,WindowNo,"M_Product_ID" );

        // log.log(Level.WARNING,"qty - init - M_Product_ID=" + M_Product_ID);

        BigDecimal MovementQty,QtyEntered;

        // No Product

        if( M_Product_ID == 0 ) {
            QtyEntered = ( BigDecimal )mTab.getValue( "QtyEntered" );
            mTab.setValue( "MovementQty",QtyEntered );
        }

        // UOM Changed - convert from Entered -> Product

        else if( mField.getColumnName().equals( "C_UOM_ID" )) {
            int C_UOM_To_ID = (( Integer )value ).intValue();

            QtyEntered  = ( BigDecimal )mTab.getValue( "QtyEntered" );
            MovementQty = MUOMConversion.convertProductFrom( ctx,M_Product_ID,C_UOM_To_ID,QtyEntered );

            if( MovementQty == null ) {
                MovementQty = QtyEntered;
            }

            boolean conversion = QtyEntered.compareTo( MovementQty ) != 0;

            log.fine( "UOM=" + C_UOM_To_ID + ", QtyEntered=" + QtyEntered + " -> " + conversion + " MovementQty=" + MovementQty );
            Env.setContext( ctx,WindowNo,"UOMConversion",conversion
                    ?"Y"
                    :"N" );
            mTab.setValue( "MovementQty",MovementQty );
        }

        // No UOM defined

        else if( Env.getContextAsInt( ctx,WindowNo,"C_UOM_ID" ) == 0 ) {
            QtyEntered = ( BigDecimal )mTab.getValue( "QtyEntered" );
            mTab.setValue( "MovementQty",QtyEntered );
        }

        // QtyEntered changed - calculate MovementQty

        else if( mField.getColumnName().equals( "QtyEntered" )) {
        	int C_UOM_To_ID = Env.getContextAsInt( ctx,WindowNo,"C_UOM_ID" );

            QtyEntered  = ( BigDecimal )value;
            boolean isSOTrx = "Y".equals(Env.getContext(ctx, WindowNo, "IsSOTrx"));

            //Se quito la siguiente validación ya que ahora es necesario pode tener cantidades menores a 0, para poder
            //manejar los descuentos a proveedores.      

            /*
            // En remitos de compra no se permiten cantidades menores o iguales que cero
            if (!isSOTrx && QtyEntered.compareTo(Env.ZERO) < 0) {
            	mTab.setValue("QtyEntered", null);
            	setCalloutActive(false);
            	return Msg.getMsg(ctx,"FieldUnderZeroError", new Object[] {Msg.translate(ctx,"QtyEntered")});
            }
			*/
            MovementQty = MUOMConversion.convertProductFrom( ctx,M_Product_ID,C_UOM_To_ID,QtyEntered );

            if( MovementQty == null ) {
                MovementQty = QtyEntered;
            }

            boolean conversion = QtyEntered.compareTo( MovementQty ) != 0;

            log.fine( "UOM=" + C_UOM_To_ID + ", QtyEntered=" + QtyEntered + " -> " + conversion + " MovementQty=" + MovementQty );
            Env.setContext( ctx,WindowNo,"UOMConversion",conversion
                    ?"Y"
                    :"N" );
            mTab.setValue( "MovementQty",MovementQty );
        }

        // MovementQty changed - calculate QtyEntered

        else if( mField.getColumnName().equals( "MovementQty" )) {
            int C_UOM_To_ID = Env.getContextAsInt( ctx,WindowNo,"C_UOM_ID" );

            MovementQty = ( BigDecimal )value;
            QtyEntered  = MUOMConversion.convertProductTo( ctx,M_Product_ID,C_UOM_To_ID,MovementQty );

            if( QtyEntered == null ) {
                QtyEntered = MovementQty;
            }

            boolean conversion = MovementQty.compareTo( QtyEntered ) != 0;

            log.fine( "UOM=" + C_UOM_To_ID + ", MovementQty=" + MovementQty + " -> " + conversion + " QtyEntered=" + QtyEntered );
            Env.setContext( ctx,WindowNo,"UOMConversion",conversion
                    ?"Y"
                    :"N" );
            mTab.setValue( "QtyEntered",QtyEntered );
        }

        //

        // updateMasi(ctx, WindowNo, mTab, (Integer)mTab.getValue( "M_Product_ID" ));
        
        //
        
        setCalloutActive( false );
        state.setContinueStatus(MPluginStatusCallout.STATE_TRUE_AND_SKIP);
        return state;
        //return "";
    }    // qty
    
    

    
}    // CalloutInOut

