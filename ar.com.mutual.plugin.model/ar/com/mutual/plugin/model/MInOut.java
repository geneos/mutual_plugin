package ar.com.mutual.plugin.model;
 
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.openXpertya.model.MInOutLine;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;

import ar.com.mutual.plugin.utils.ClientWS;
import ar.com.mutual.plugin.utils.WSParser;
 
public class MInOut extends MPluginDocAction {
 
 
	public MInOut(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		
		boolean error = false;
		
		try {
			String sinc = MParametros.getParameterValueByName(m_ctx, "sincMInOut", null);
			if (sinc != null && sinc.toLowerCase().equals("si")){
				sincronizarConPresta(document);
			}
		} catch (ClientProtocolException e) {
			error = true;
			e.printStackTrace();

		} catch (IOException e) {
			error = true;
			e.printStackTrace();

		} catch (Exception e) {
			error = true;
			e.printStackTrace();

		}		
	   
	   if (error){
		   status_docAction.setContinueStatus(0);
		   status_docAction.setProcessMsg("Error al intentar sincronizar cambios con Prestashop");
	   }
	   
		return status_docAction;
		
	}
	
	public void sincronizarConPresta(DocAction document)throws 
	ClientProtocolException, 
	IOException, 
	Exception {
		org.openXpertya.model.MInOut inout = (org.openXpertya.model.MInOut)document;
		int[] movlines_id = MInOutLine.getAllIDs("M_InOutLine", "M_InOut_ID = " + inout.getM_InOut_ID(), this.m_trx);
		
		org.openXpertya.model.MDocType doctype = new org.openXpertya.model.MDocType(this.m_ctx, inout.getC_DocType_ID(), this.m_trx);
		
		// getDocTypeKey() = MMS => Remito de Salida por envío al cliente
		if(doctype.getDocTypeKey().equals("MMS")) {
				
			/* 
			 * Para cada línea de la recepción de terceros tengo que actualizar el stock con la consulta
			 * al stock total disponible para ese producto (hacer función para obtener el dato).
			 * 
			*/
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				// Obtengo la información de cada uno de los productos que estoy moviendo y necesito actualizar stock
				
				org.openXpertya.model.MInOutLine line = new org.openXpertya.model.MInOutLine(this.m_ctx, movlines_id[ind], this.m_trx);
				org.openXpertya.model.MLocator loc = new org.openXpertya.model.MLocator(this.m_ctx, line.getM_Locator_ID(), this.m_trx);
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, line.getM_Product_ID(), this.m_trx);
				
				// Necesito determinar si el depósito de origen corresponde a restar o sumar stock en presta
				
				String sumarOrigen = MParametros.getParameterValueByName(this.m_ctx, "nombreOrigenSumarStock", this.m_trx);
				
				BigDecimal cant = line.getMovementQty();
				
				if(sumarOrigen.equals(loc.getValue())) {
					
					// Obtengo la información de la entidad que necesitamos modificar
					
					ClientRequest request =  ClientWS.getItem(this.m_ctx, "/stock_availables/", product.getValue());
					request.accept("application/xml");	            
		            ClientResponse<String> response = request.get(String.class);


		            if (response.getStatus() != 200) {
		                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		            } else {	
		                String xml_item = WSParser.parserUpdateStock(response.getEntity().getBytes("UTF-8"), cant.setScale(0), "restar");	                
		                ClientWS.putItem(this.m_ctx, "/stock_availables/", product.getValue(), xml_item);
		            }
		            
				}
				
	            	
			}

			
		// getDocTypeKey() = MMR => Remito de Entrada por recibo del proveedor
		} else if(doctype.getDocTypeKey().equals("MMR")) {
			
				
			/* 
			 * Para cada línea de la recepción de terceros tengo que actualizar el stock con la consulta
			 * al stock total disponible para ese producto (hacer función para obtener el dato).
			 * 
			*/
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				// Obtengo la información de cada uno de los productos que estoy moviendo y necesito actualizar stock
				
				org.openXpertya.model.MInOutLine line = new org.openXpertya.model.MInOutLine(this.m_ctx, movlines_id[ind], this.m_trx);
				org.openXpertya.model.MLocator loc = new org.openXpertya.model.MLocator(this.m_ctx, line.getM_Locator_ID(), this.m_trx);
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, line.getM_Product_ID(), this.m_trx);
				
				// Necesito determinar si el depósito de origen corresponde a restar o sumar stock en presta
				
				String sumarOrigen = MParametros.getParameterValueByName(this.m_ctx, "nombreOrigenSumarStock", this.m_trx);
				
				BigDecimal cant = line.getMovementQty();
				
				if(sumarOrigen.equals(loc.getValue())) {
					
					// Obtengo la información de la entidad que necesitamos modificar
					
					ClientRequest request =  ClientWS.getItem(this.m_ctx, "/stock_availables/", product.getValue());
					request.accept("application/xml");	            
		            ClientResponse<String> response = request.get(String.class);


		            if (response.getStatus() != 200) {
		                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		            } else {	
		                String xml_item = WSParser.parserUpdateStock(response.getEntity().getBytes("UTF-8"), cant.setScale(0), "sumar");	                
		                ClientWS.putItem(this.m_ctx, "/stock_availables/", product.getValue(), xml_item);
		            }
		            
				}
				
	            	
			}
			
		// getDocTypeKey() = VR => Remito de Salida por devolución al proveedor 
		} else if(doctype.getDocTypeKey().equals("VR")) {
			
			/* 
			 * Para cada línea de la recepción de terceros tengo que actualizar el stock con la consulta
			 * al stock total disponible para ese producto (hacer función para obtener el dato).
			 * 
			*/
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				// Obtengo la información de cada uno de los productos que estoy moviendo y necesito actualizar stock
				
				org.openXpertya.model.MInOutLine line = new org.openXpertya.model.MInOutLine(this.m_ctx, movlines_id[ind], this.m_trx);
				org.openXpertya.model.MLocator loc = new org.openXpertya.model.MLocator(this.m_ctx, line.getM_Locator_ID(), this.m_trx);
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, line.getM_Product_ID(), this.m_trx);
				
				// Necesito determinar si el depósito de origen corresponde a restar o sumar stock en presta
				
				String sumarOrigen = MParametros.getParameterValueByName(this.m_ctx, "nombreOrigenSumarStock", this.m_trx);
				
				BigDecimal cant = line.getMovementQty();
				
				if(sumarOrigen.equals(loc.getValue())) {
					
					// Obtengo la información de la entidad que necesitamos modificar
					
					ClientRequest request =  ClientWS.getItem(this.m_ctx, "/stock_availables/", product.getValue());
					request.accept("application/xml");	            
		            ClientResponse<String> response = request.get(String.class);


		            if (response.getStatus() != 200) {
		                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		            } else {	
		                String xml_item = WSParser.parserUpdateStock(response.getEntity().getBytes("UTF-8"), cant.setScale(0), "restar");	                
		                ClientWS.putItem(this.m_ctx, "/stock_availables/", product.getValue(), xml_item);
		            }
		            
				}
				
	            	
			}
			
		// getDocTypeKey() = DC => Remito de Salida por devolución del cliente 
		} else if(doctype.getDocTypeKey().equals("DC")) {
			
				
			/* 
			 * Para cada línea de la recepción de terceros tengo que actualizar el stock con la consulta
			 * al stock total disponible para ese producto (hacer función para obtener el dato).
			 * 
			*/
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				// Obtengo la información de cada uno de los productos que estoy moviendo y necesito actualizar stock
				
				org.openXpertya.model.MInOutLine line = new org.openXpertya.model.MInOutLine(this.m_ctx, movlines_id[ind], this.m_trx);
				org.openXpertya.model.MLocator loc = new org.openXpertya.model.MLocator(this.m_ctx, line.getM_Locator_ID(), this.m_trx);
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, line.getM_Product_ID(), this.m_trx);
				
				// Necesito determinar si el depósito de origen corresponde a restar o sumar stock en presta
				
				String sumarOrigen = MParametros.getParameterValueByName(this.m_ctx, "nombreOrigenSumarStock", this.m_trx);
				
				BigDecimal cant = line.getMovementQty();
				
				if(sumarOrigen.equals(loc.getValue())) {
					
					// Obtengo la información de la entidad que necesitamos modificar
					
					ClientRequest request =  ClientWS.getItem(this.m_ctx, "/stock_availables/", product.getValue());
					request.accept("application/xml");	            
		            ClientResponse<String> response = request.get(String.class);


		            if (response.getStatus() != 200) {
		                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		            } else {	
		                String xml_item = WSParser.parserUpdateStock(response.getEntity().getBytes("UTF-8"), cant.setScale(0), "sumar");	                
		                ClientWS.putItem(this.m_ctx, "/stock_availables/", product.getValue(), xml_item);
		            }
		            
				}

			}
			
		}
	}
	
}