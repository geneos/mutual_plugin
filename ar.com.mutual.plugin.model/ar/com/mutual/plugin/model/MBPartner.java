package ar.com.mutual.plugin.model;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jdom2.Document;
import org.jdom2.Element;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginPO;
import org.openXpertya.plugin.MPluginStatusPO;

import ar.com.mutual.plugin.utils.ClientWS;
import ar.com.mutual.plugin.utils.WSParser;
 
public class MBPartner extends MPluginPO {
 
	static final String resource = "customers";
 
	public MBPartner(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
	
	public void sincronizarConPresta(PO po, boolean newRecord, String searchKey) throws 
					ClientProtocolException, 
					IOException, 
					Exception 
	{
		boolean existInPresta = false;
		Document xml_dom = null;
		if (newRecord) 
			//Obtengo XML base del recurso 
			xml_dom = ClientWS.getDomBlank(this.m_ctx,resource);
		else {
			//Intento obtener XML del recurso a travez del ID de presta (Campo Value en Libertya)
			xml_dom = ClientWS.getDom(this.m_ctx,resource,searchKey);
			if( xml_dom != null )
				existInPresta = true;
			else
				//Si fallo obtengo el XML Base
				xml_dom = ClientWS.getDomBlank(this.m_ctx,resource);
		}

		//Sincronizo Datos
		if (existInPresta){
			editItem(xml_dom,po);
		}
		else
			addItem(xml_dom,po);
	}
	
	/**
	 * Ejecución previa al beforeSave
	 * @return estado del procesamiento
	 */
	public MPluginStatusPO preBeforeSave(PO po, boolean newRecord) {

		
		
		boolean error = false;
		
		
		try {
			String sinc = MParametros.getParameterValueByName(m_ctx, "sincMBPartner", null);
			if (sinc != null && sinc.toLowerCase().equals("si")){
				sincronizarConPresta(po,newRecord,po.get_ValueAsString("Value"));
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
		   status_po.setContinueStatus(0);
		   status_po.setErrorMessage("Error al intentar sincronizar cambios con Prestashop");
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
	
	private boolean addItem(Document xml_dom,PO po){
		
		org.openXpertya.model.MBPartner partner = (org.openXpertya.model.MBPartner) po;
		
		Element customerDom = xml_dom.getRootElement().getChild("customer");
		
		
		customerDom.getChild("email").setText( "cuentaejemplo1@yopmail.com");
		customerDom.getChild("id_default_group").setText( "0");
		customerDom.getChild("id_lang").setText( "1" );
		customerDom.getChild("deleted").setText( "0" );

		customerDom.getChild("passwd").setText( "a33789052b" );
				
		customerDom.getChild("lastname").setText( "Velazquez" );
		customerDom.getChild("firstname").setText( "Pablo" );
		customerDom.getChild("id_gender").setText( "1" );
		
		customerDom.getChild("birthday").setText( "1988-06-24" );
		customerDom.getChild("newsletter").setText( "0" );
		customerDom.getChild("optin").setText( "0" );
		
		customerDom.getChild("is_guest").setText( "0" );
		customerDom.getChild("id_shop_group").setText( "1" );
		customerDom.getChild("id_shop").setText( "1" );
		
        String active = partner.isActive() ? "1" : "0";
        customerDom.getChild("active").setText( active );
       
        customerDom.removeChild("secure_key");
        customerDom.removeChild("last_passwd_gen");       
        
        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        
        String value = ClientWS.postItem(this.m_ctx, "/"+resource, xml_item);
        
        partner.setValue(value);
        if (value.equals("0"))
        	return false;
        return true;
	}
	
	private boolean editItem(Document xml_dom,PO po){
		
		org.openXpertya.model.MBPartner partner = (org.openXpertya.model.MBPartner) po;
		
		Element customerDom = xml_dom.getRootElement().getChild("customer");
		
		
		customerDom.getChild("email").setText( "cuentaejemplo1@yopmail.com");
		
		customerDom.getChild("lastname").setText( "Velazquez" );
		customerDom.getChild("firstname").setText( "Pablo" );

		customerDom.getChild("birthday").setText( "1988-06-24" );
		
        String active = partner.isActive() ? "1" : "0";
        customerDom.getChild("active").setText( active );
        
        customerDom.removeChild("secure_key");
        customerDom.removeChild("last_passwd_gen");
        
        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        return ClientWS.putItem(this.m_ctx, "/"+resource+"/", partner.getValue(), xml_item);
	}
	
	/**
	 * Ejecución posterior al beforeDelete
	 * @return estado del procesamiento
	 */
	public MPluginStatusPO postBeforeDelete(PO po) {
		org.openXpertya.model.MProduct product = (org.openXpertya.model.MProduct) po;
		
		if ( !ClientWS.deleteItem(this.m_ctx, "/"+resource+"/", product.getValue()) ){
		   status_po.setContinueStatus(0);
		   status_po.setErrorMessage("Error al intentar sincronizar cambios con Prestashop");
		}
		return status_po;
	}
	
}