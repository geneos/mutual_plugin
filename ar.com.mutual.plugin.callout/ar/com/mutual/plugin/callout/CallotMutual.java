package ar.com.mutual.plugin.callout;

import java.util.Properties;

import org.openXpertya.model.MField;
import org.openXpertya.model.MOrder;
import org.openXpertya.model.MTab;
import org.openXpertya.plugin.CalloutPluginEngine;
import org.openXpertya.plugin.MPluginStatus;
import org.openXpertya.plugin.MPluginStatusCallout;

public class CallotMutual extends CalloutPluginEngine {

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

    public MPluginStatusCallout order( Properties ctx,int WindowNo,MTab mTab,MField mField,Object value ) {
        System.out.println("Llamada al plugin por medio de un callout");
        state.setContinueStatus(MPluginStatusCallout.STATE_TRUE_AND_SKIP);
        return state;
    }    // order
	
	
	
}
