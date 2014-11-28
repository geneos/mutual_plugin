package ar.com.mutual.plugin.model;
 
import java.io.IOException;
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
 
public class MProductLines extends MPluginPO {
 
	static final String resource = "categories";
 
	public MProductLines(PO po, Properties ctx, String trxName, String aPackage) {
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
			String sinc = MParametros.getParameterValueByName(m_ctx, "sincMProductLines", null);
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
		
		
		org.openXpertya.model.MProductLines lines = (org.openXpertya.model.MProductLines) po;
		
		Element categoryDom = xml_dom.getRootElement().getChild("category");
		
		categoryDom.getChild("id_parent").setText( String.valueOf( 1 ) );

		categoryDom.getChild("description").setText( lines.getDescription());
        String active = lines.isActive() ? "1" : "0";
        categoryDom.getChild("active").setText( active );
        
        List<Element> nameLang = categoryDom.getChild("name").getChildren("language"); 
        for (  Element name : nameLang ){
        	name.setText( lines.getName());
        }
        
        List<Element> linkRewriteLang = categoryDom.getChild("link_rewrite").getChildren("language"); 
        for (  Element linkRewrite : linkRewriteLang ){
        	linkRewrite.setText( lines.getName().replace(" ","-").toLowerCase() );
        }
        
        /* Default Values */
        categoryDom.getChild("id_shop_default").setText( "1" );
        categoryDom.getChild("is_root_category").setText( "0" );
        //productDom.getChild("position").setText( String.valueOf( 0 ) );
        
        //Remuevo readOnlys
        categoryDom.removeChild("nb_products_recursive");
        categoryDom.removeChild("level_depth");
        
        
        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        
        String value = ClientWS.postItem(this.m_ctx, "/"+resource, xml_item);
        
        lines.setValue(value);
        if (value.equals("0"))
        	return false;
        return true;
	}
	
	private boolean editItem(Document xml_dom,PO po){
		
org.openXpertya.model.MProductLines lines = (org.openXpertya.model.MProductLines) po;
		
		Element categoryDom = xml_dom.getRootElement().getChild("category");
		
		categoryDom.getChild("id_parent").setText( String.valueOf( 1 ) );

		categoryDom.getChild("description").setText( lines.getDescription());
        String active = lines.isActive() ? "1" : "0";
        categoryDom.getChild("active").setText( active );
        
        List<Element> nameLang = categoryDom.getChild("name").getChildren("language"); 
        for (  Element name : nameLang ){
        	name.setText( lines.getName());
        }
        
        List<Element> linkRewriteLang = categoryDom.getChild("link_rewrite").getChildren("language"); 
        for (  Element linkRewrite : linkRewriteLang ){
        	linkRewrite.setText( lines.getName().replace(" ","-").toLowerCase() );
        }
        
        
        //Remuevo readOnlys
        categoryDom.removeChild("nb_products_recursive");
        categoryDom.removeChild("level_depth");
        
        
        
        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        return ClientWS.putItem(this.m_ctx, "/"+resource+"/", lines.getValue(), xml_item);
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