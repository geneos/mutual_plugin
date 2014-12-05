package ar.com.mutual.plugin.model;
 
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.openXpertya.model.MInventoryLine;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;

import ar.com.mutual.plugin.utils.ClientWS;
import ar.com.mutual.plugin.utils.WSParser;

public class MInventory extends MPluginDocAction {
 
 
	public MInventory(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		

		String sinc = MParametros.getParameterValueByName(m_ctx, "sincMProductGamas", null);
		
		if (sinc != null && sinc.toLowerCase().equals("si")){
			return sincronizarConPresta(document);
		}
		
		return status_docAction;

	}
		
	public MPluginStatusDocAction sincronizarConPresta(DocAction document) {
		
		org.openXpertya.model.MInventory inventario = (org.openXpertya.model.MInventory)document;
		int[] movlines_id = MInventoryLine.getAllIDs("M_InventoryLine", "M_Inventory_ID = " + inventario.getM_Inventory_ID(), this.m_trx);
			
		try {
			
			/* 
			 * Para cada línea de la recepción de terceros tengo que actualizar el stock con la consulta
			 * al stock total disponible para ese producto (hacer función para obtener el dato).
			 * 
			*/
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				// Obtengo la información de cada uno de los productos que estoy moviendo y necesito actualizar stock
				
				org.openXpertya.model.MInventoryLine line = new org.openXpertya.model.MInventoryLine(this.m_ctx, movlines_id[ind], this.m_trx);
				String doctype = line.getInventoryType();
				
				org.openXpertya.model.MLocator loc = new org.openXpertya.model.MLocator(this.m_ctx, line.getM_Locator_ID(), this.m_trx);
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, line.getM_Product_ID(), this.m_trx);
				
				// Necesito determinar si el depósito de origen corresponde a restar o sumar stock en presta
				
				String sumarOrigen = MParametros.getParameterValueByName(this.m_ctx, "nombreOrigenSumarStock", this.m_trx);
				
				BigDecimal cant = line.getQtyCount();
				
				if(sumarOrigen.equals(loc.getValue())) {
					
					// Tipo de operación doctype = O es Sobreescribir Inventario
					
					if(doctype.equals("O")) {

						// Obtengo la información de la entidad que necesitamos modificar
						
						ClientRequest request =  ClientWS.getItem(this.m_ctx, "/stock_availables/", product.getValue());
						request.accept("application/xml");	            
			            ClientResponse<String> response = request.get(String.class);

			            if (response.getStatus() != 200) {
			                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			            } else {	
			                String xml_item = WSParser.parserUpdateStock(response.getEntity().getBytes("UTF-8"), cant.setScale(0), "cambiar");	                
			                ClientWS.putItem(this.m_ctx, "/stock_availables/", product.getValue(), xml_item);
			            }
					
					// Tipo de operación doctype = O es Sobreescribir Inventario
					} else if(doctype.equals("D")) {

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
            

		} catch (ClientProtocolException e) {
	
			e.printStackTrace();
	
		} catch (IOException e) {
	
			e.printStackTrace();
	
		} catch (Exception e) {
	
			e.printStackTrace();
	
		}

		return status_docAction;
		
		
	}
	
}