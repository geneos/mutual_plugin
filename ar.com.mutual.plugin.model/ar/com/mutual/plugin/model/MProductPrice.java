package ar.com.mutual.plugin.model;
 
import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginPO;
import org.openXpertya.plugin.MPluginStatusPO;

import ar.com.mutual.plugin.utils.ClientWS;
import ar.com.mutual.plugin.utils.WSParser;
 
public class MProductPrice extends MPluginPO {
 
 
	public MProductPrice(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
	
	 
	public MPluginStatusPO preBeforeSave(PO po, boolean newRecord) {
		
		org.openXpertya.model.MProductPrice price = (org.openXpertya.model.MProductPrice) po;
		
		int product_ID = price.getM_Product_ID();
		org.openXpertya.model.MProduct product = new org.openXpertya.model.MProduct(this.m_ctx, product_ID, null);
		
		System.out.println("ID de producto " + product_ID);
		String priceListVersion_ID = Integer.toString(price.getM_PriceList_Version_ID());
		
		String priceListVersionParameter_ID = MParametros.getParameterValueByName(this.m_ctx, "priceListVersion_ID", null);

	
		if(priceListVersionParameter_ID.equals(priceListVersion_ID)) {
                	
                try {
                	
                	
					// Obtengo la información de la entidad que necesitamos modificar
					
					ClientRequest request =  ClientWS.getItem(this.m_ctx, "/products/", product.getValue());
					request.accept("application/xml");	            
		            ClientResponse<String> response = request.get(String.class);
	
	
		            if (response.getStatus() != 200) {
		                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		            } else {	
		                String xml_item = WSParser.parserUpdatePrice(response.getEntity().getBytes("UTF-8"), price.getPriceStd().setScale(2).toString());	                
		                System.out.println(xml_item);
		                ClientWS.putItem(this.m_ctx, "/products/", product.getValue(), xml_item);
		                //ClientWS.putItem(this.m_ctx, "/products/", product.getValue(), response.getEntity().getBytes("UTF-8").toString());
		            }
		        
                	

				} catch (ClientProtocolException e) {
		
					e.printStackTrace();
		
				} catch (IOException e) {
		
					e.printStackTrace();
		
				} catch (Exception e) {
		
					e.printStackTrace();
		
				}			
			
		}

		
		return status_po;
	}

	
	
	/**
	 * Ejecución posterior al beforeSave
	 * @return estado del procesamiento
	 */
	public MPluginStatusPO postBeforeSave(PO po, boolean newRecord) {
		return status_po;
	}
	

	/**
	 * Ejecución previa al afterSave
	 * @return estado del procesamiento
	 */
	public MPluginStatusPO preAfterSave(PO po, boolean newRecord, boolean success) {
		return status_po;
	}
	
	
	/**
	 * Ejecución posterior al afterSave
	 * @return estado del procesamiento
	 */
	public MPluginStatusPO postAfterSave(PO po, boolean newRecord, boolean success) {
		return status_po;
	}	
 
	
}