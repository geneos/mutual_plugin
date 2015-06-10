package ar.com.mutual.plugin.model;
 
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MInvoiceLine;
import org.openXpertya.model.MProduct;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;
import org.openXpertya.util.Env;
 
public class MInvoice extends MPluginDocAction {
 
 
	public MInvoice(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		
		org.openXpertya.model.MInvoice factura = (org.openXpertya.model.MInvoice)document;
		
		
		//GENEOS - AGREGAR CUOTAS SOCIALES
		
		int bPartnerID = factura.getC_BPartner_ID();
				
		// Buscar los items de cuotas sociales en la factura
		
		boolean crearCouta = false;
		
		MInvoiceLine[] lineas = factura.getLines();
		MProduct prod = null;
		
		BigDecimal montocuota=Env.ZERO;
		
		
		for ( int i = 0; i < lineas.length; i ++ ) {
			
			prod = new MProduct(this.m_ctx, lineas[i].getM_Product_ID(), null);
			
			// Es Cuota -> Ver Cantidad
			
			if (prod.getValue().equals("CS001") || prod.getValue().equals("CS002")) {
				crearCouta = true;
				montocuota = lineas[i].getPriceEntered();
		    }
			
		}
		
		if(crearCouta) {							
				// Alta de Cuota - controlar transacción !!	

				MCuotaSocial cuotanueva = new MCuotaSocial(m_ctx, 0 ,this.m_trx);
				Date dActual = new Date();
				Timestamp ts_now = new Timestamp(dActual.getTime());

				Calendar cal = Calendar.getInstance();
				cal.setTime(dActual);
				int yearcuota = cal.get(Calendar.YEAR);
				int monthcuota = cal.get(Calendar.MONTH) + 1;

				cuotanueva.setAnio(yearcuota);
				cuotanueva.setmes(monthcuota);		
				cuotanueva.setmonto(montocuota);				
				cuotanueva.setfechapago(ts_now);
				cuotanueva.setC_BPartner_ID(bPartnerID);				
				cuotanueva.save();
				
		}
		
		//FIN -AGREGAR CUOTAS SOCIALES		

			
		return status_docAction;
		
	}
	
}