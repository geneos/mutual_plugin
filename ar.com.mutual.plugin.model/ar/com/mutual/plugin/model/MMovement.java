package ar.com.mutual.plugin.model;
 
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.openXpertya.model.MMovementLine;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;

import ar.com.mutual.plugin.utils.ClientWS;
import ar.com.mutual.plugin.utils.WSParser;
 
public class MMovement extends MPluginDocAction {
 
 
	public MMovement(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		
		org.openXpertya.model.MMovement mov = (org.openXpertya.model.MMovement)document;
		
		int[] movlines_id = MMovementLine.getAllIDs("M_MovementLine", "M_Movement_ID = " + mov.getM_Movement_ID(), null);

		try {
			
			/* 
			 * Para cada línea del movimiento tengo que actualizar el stock con la consulta
			 * al stock total disponible para ese producto (hacer función para obtener el dato).
			 * 
			*/
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				// Obtengo la información de cada uno de los productos que estoy moviendo y necesito actualizar stock
				
				org.openXpertya.model.MMovementLine line = new org.openXpertya.model.MMovementLine(this.m_ctx, movlines_id[ind],null);
				org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, line.getM_Product_ID(),null);
				BigDecimal cant = line.getMovementQty();
				
				// Obtengo la información de la entidad que necesitamos modificar
				
				ClientRequest request =  ClientWS.getItem(this.m_ctx, "/stock_availables", product.getValue());
				request.accept("application/xml");	            
	            ClientResponse<String> response = request.get(String.class);


	            if (response.getStatus() != 200) {
	                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	            } else {	
	                String xml_item = WSParser.parserUpdateStock(response.getEntity().getBytes("UTF-8"), cant.toString());	                
	                ClientWS.putItem(this.m_ctx, "/stock_availables", product.getValue(), xml_item);
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