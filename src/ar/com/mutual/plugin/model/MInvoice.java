package ar.com.mutual.plugin.model;
 
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MInvoiceLine;
import org.openXpertya.model.MProduct;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.Env;
 
public class MInvoice extends MPluginDocAction {
 

	/** Logger */
	private static CLogger s_log = CLogger.getCLogger(MInvoice.class);
 
	public MInvoice(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {

		boolean error = false;
		String msg = "";
		
		try {
			org.openXpertya.model.MInvoice factura = (org.openXpertya.model.MInvoice)document;
		
		
			//GENEOS - AGREGAR CUOTAS SOCIALES
		
			int bPartnerID = factura.getC_BPartner_ID();
				
			// Buscar los items de cuotas sociales en la factura
		
			boolean crearCouta = false;
		
			MInvoiceLine[] lineas = factura.getLines(true);
			MProduct prod = null;
		
			BigDecimal montocuota=Env.ZERO;
		
		
			for ( int i = 0; i < lineas.length; i ++ ) {
			
				prod = new MProduct(this.m_ctx, lineas[i].getM_Product_ID(), null);
			
				// Es Cuota -> Ver Cantidad
			
				System.out.println("Producto de factura: " + prod.getValue());
			
				if (prod.getValue().equals("CS001") || prod.getValue().equals("CS002")) {
					crearCouta = true;
					montocuota = lineas[i].getPriceEntered();
			    }
			
			}
		
		
		
		
			if(crearCouta) {							
					// Alta de Cuota - controlar transacción !!	
				
					System.out.println("Ingresa a crear la cuota.");
				
					MCuotaSocial cuotanueva = new MCuotaSocial(m_ctx, 0 ,factura.get_TrxName());
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
					if ( !cuotanueva.save() ){
						error = true;
						msg = "Error al aplicar cuota social. No se pudo guardar la nueva cuota mes: "+montocuota;
						s_log.log(Level.SEVERE, msg);
					}			
				
			}
		
			//FIN -AGREGAR CUOTAS SOCIALES	
		} catch (Exception e){
			error = true;
			msg = "Error inesperado al generar cuota social";
			s_log.log(Level.SEVERE, msg, e);
		}	

		if (error){
		   status_docAction.setContinueStatus(0);
		   status_docAction.setProcessMsg(msg);
		}

			
		return status_docAction;
		
	}
	
}