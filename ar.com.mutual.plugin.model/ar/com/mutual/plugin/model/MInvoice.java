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
		
		int cuotaspedido = 0;
		BigDecimal montocuota = BigDecimal.ZERO;
		
		MInvoiceLine[] lineas = factura.getLines();
		MProduct prod = null;
		
		for ( int i = 0; i < lineas.length; i ++ ) {
			
			prod = new MProduct(this.m_ctx, lineas[i].getM_Product_ID(), null);
			
			// Es Cuota -> Ver Cantidad
			
			if (prod.getValue().equals("CS001") || prod.getValue().equals("CS002")) {
				// Acumulo la cantidad de cuotas que va pagando
		    	cuotaspedido += lineas[i].getQtyInvoiced().intValue();		    
		    	montocuota = lineas[i].getPriceEntered();
		    }
			
		}
		
		if (cuotaspedido > 0) {
		
			//Si tiene cuotas en el pedido -> GET Ultima Cuota Social Paga
			//GET: Ultima Cuota Social Paga			
			
			// Si cuota == null significa que no ha pagado aún por lo que debe tomar diferente
			// la forma de computal la primera cuota a partir de getStartHolidays.
			
			String cuota = MCuotaSocial.getLastCuotaByBPartner(m_ctx, bPartnerID, null);
			
			int yearcuota = 0;
			int monthcuota = 0;							
			boolean flag = false;
			
			if( cuota != null ) {
				String[] parts = cuota.split("/");
				String mes = parts[0]; 
				String anio = parts[1]; 								
				yearcuota = Integer.parseInt(anio);						
				monthcuota = Integer.parseInt(mes);
			} else {
				// En este caso tomo la fecha de inicio de pagos y pongo un flag para el momento
				// de crear la cuota para ver el mes que toma al inicio.				
				flag = true;
				MBPartner bp = new MBPartner(m_ctx, bPartnerID, null);
				Date dfechainicio = bp.getStartHolidays();
				Calendar calinicio = Calendar.getInstance();
				calinicio.setTime(dfechainicio);
				yearcuota = calinicio.get(Calendar.YEAR);
				monthcuota = calinicio.get(Calendar.MONTH) + 1;							
			}
			
			for ( int x = 1; x <= cuotaspedido; x ++ ) {
				
				if (flag) {
					
					// La primera cuota es del mes y del año del incio de pagos	
					// Solo hay que validar en la primera entrada
					flag = false;
					
				} else {
					
					if (monthcuota == 12) {
						monthcuota = 1;
						yearcuota = yearcuota + 1;
					} else {
						monthcuota = monthcuota + 1;
					}
					
				}
				
				// Alta de Cuota - controlar transacción !!	

				MCuotaSocial cuotanueva = new MCuotaSocial(m_ctx, 0 ,this.m_trx);
				Date dActual = new Date();
				Timestamp ts_now = new Timestamp(dActual.getTime());
				cuotanueva.setAnio(yearcuota);
				cuotanueva.setmes(monthcuota);		
				cuotanueva.setmonto(montocuota);				
				cuotanueva.setfechapago(ts_now);
				cuotanueva.setC_BPartner_ID(bPartnerID);				
				cuotanueva.save();
			}					
		}
		
		//FIN -AGREGAR CUOTAS SOCIALES		

			
		return status_docAction;
		
	}
	
}