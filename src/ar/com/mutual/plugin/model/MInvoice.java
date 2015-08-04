package ar.com.mutual.plugin.model;
 
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MDocType;
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
			int cant_cuotas = 0;
		
			BigDecimal montocuota = Env.ZERO;
	
			String indice_cuotas = "";
						
		
			for ( int i = 0; i < lineas.length; i ++ ) {
			
				prod = new MProduct(this.m_ctx, lineas[i].getM_Product_ID(), null);
			
				// Es Cuota -> Ver Cantidad
			
				System.out.println("Producto de factura: " + prod.getValue());
			
				if (prod.getValue().equals("CS001") || prod.getValue().equals("CS002")) {
					
					MDocType dt = new MDocType(this.m_ctx, factura.getC_DocType_ID(), this.m_trx);
					
					if(factura.getDocAction().equals(org.openXpertya.model.MInvoice.DOCACTION_Close) &&  
							dt.getDocBaseType().equals("ARI")) {
						// Agrego el indice de la cuota social.
						indice_cuotas += i + ",";
					}
					
			    }
			
			}
		
		
			
			/*
			
				if(ultimaCuota.getmes() == 12) {
					cuotanueva.setAnio(ultimaCuota.getAnio()+1);
					cuotanueva.setmes(1);	
				} else {
					cuotanueva.setAnio(ultimaCuota.getAnio());
					cuotanueva.setmes(ultimaCuota.getmes()+1);	
				}
					
				
				cuotanueva.setmonto(montocuota);				
				cuotanueva.setfechapago(ts_now);
				cuotanueva.setC_BPartner_ID(bPartnerID);	
				if ( !cuotanueva.save() ){
					error = true;
					msg = "Error al aplicar cuota social. No se pudo guardar la nueva cuota mes: "+montocuota;
					s_log.log(Level.SEVERE, msg);
				}			
				
				
			} else {
				
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
			*/
			
			// Si hay cuotas pagas en la factura
			
			if(indice_cuotas != "") {	
			
				MCuotaSocial cuotanueva;
				
				// Sacar Fecha actual y desglozar en mes y año
				
				Date fecha_actual = new Date();
				Timestamp ts_now = new Timestamp(fecha_actual.getTime());
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(fecha_actual);
				
				int fecha_actual_anio = cal.get(Calendar.YEAR);
				int fecha_actual_mes = cal.get(Calendar.MONTH) + 1;

				
				int mes = fecha_actual_mes;
				int anio = fecha_actual_anio;

				
				
				MCuotaSocial ultimaCuota = MCuotaSocial.ultimaCuotaPaga(m_ctx, bPartnerID, factura.get_TrxName());
				
				// Necesito verificar si la última cuota es anterior al mes actual
				
				boolean cuota_ant_now = true;
				
				if(ultimaCuota != null) {
					
					// Verifico si la última cuota es posterior a la fecha actual
					
					if(ultimaCuota.getAnio() > fecha_actual_anio)
						cuota_ant_now = false;
					else if(ultimaCuota.getAnio() == fecha_actual_anio)
						if(ultimaCuota.getmes() >= fecha_actual_mes)
							cuota_ant_now = false;
						
					
					// Si la última cuota es posterior a la fecha actual parto desde el mes que sigue a la última cuota
					// para registrar las siguientes.
					
					if(cuota_ant_now == false) {
						
						if(ultimaCuota.getmes() == 12) {
							anio = ultimaCuota.getAnio()+1;
							mes = 1;	
						} else {
							anio = ultimaCuota.getAnio();
							mes = ultimaCuota.getmes()+1;	
						}
	
					// Tengo que cobrar el mes en curso y de ahi en adelante
						
					} else {
						
						anio = fecha_actual_anio;
						mes = fecha_actual_mes;
							
					}
					
				}
				
				String[] ind = indice_cuotas.split(",");
				int ind_length = ind.length;

				for ( int i = 0; i <= ind_length-1; i ++ ) {
					
					prod = new MProduct(this.m_ctx, lineas[ Integer.parseInt(ind[i])].getM_Product_ID(), null);
					
					cant_cuotas = lineas[ Integer.parseInt(ind[i])].getQtyInvoiced().intValue();
					montocuota = lineas[ Integer.parseInt(ind[i])].getPriceEntered();
					
					for ( int ii = 0; ii <= cant_cuotas-1; ii ++ ) {
					
						cuotanueva = new MCuotaSocial(m_ctx, 0 ,factura.get_TrxName());
						cuotanueva.setAnio(anio);
						cuotanueva.setmes(mes);		
						cuotanueva.setmonto(montocuota);				
						cuotanueva.setfechapago(ts_now);
						cuotanueva.setC_BPartner_ID(bPartnerID);	
						if ( !cuotanueva.save() ){
							error = true;
							msg = "Error al aplicar cuota social. No se pudo guardar la nueva cuota mes: "+montocuota;
							s_log.log(Level.SEVERE, msg);
						}		
						
						// Incremento por si se siguen registrando cuotas
						
						if(mes == 12) {
							anio = anio+1;
							mes = 1;	
						} else {
							mes = mes+1;	
						}		
						
				    }
				
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