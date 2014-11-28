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
 
public class MProduct extends MPluginPO {
 
	static final String resource = "products";
 
	public MProduct(PO po, Properties ctx, String trxName, String aPackage) {
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
			String sinc = MParametros.getParameterValueByName(m_ctx, "sincMProducs", null);
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
		
		
		org.openXpertya.model.MProduct product = (org.openXpertya.model.MProduct) po;
		
		Element productDom = xml_dom.getRootElement().getChild("product");
		
		
		//Busco categoria del producto de libertya en Presta
		org.openXpertya.model.MProductCategory category = new org.openXpertya.model.MProductCategory (this.m_ctx,product.getM_Product_Category_ID(),this.m_trx);

		productDom.getChild("id_category_default").setText( category.getValue() );
		
		
        productDom.getChild("description").setText( product.getDescription());
        String active = product.isActive() ? "1" : "0";
        productDom.getChild("active").setText( active );
        productDom.getChild("reference").setText( product.getName().replace(" ","").toLowerCase() );
        List<Element> nameLang = productDom.getChild("name").getChildren("language"); 
        for (  Element name : nameLang ){
        	name.setText( product.getName());
        }
        List<Element> linkRewriteLang = productDom.getChild("link_rewrite").getChildren("language"); 
        for (  Element linkRewrite : linkRewriteLang ){
        	linkRewrite.setText( product.getName().replace(" ","-").toLowerCase() );
        }
        
        /* Default Values */
        Element listCategories = new Element("categories");
        Element categoryNode = new Element("categories");
        Element categoryId = new Element("id");
        categoryId.setText(category.getValue());
        categoryNode.addContent(categoryId);
        listCategories.addContent(categoryNode);
        
        productDom.getChild("associations").addContent(listCategories);
        
        productDom.getChild("id_shop_default").setText( "1" );
        productDom.getChild("id_manufacturer").setText( String.valueOf( 0 ) );
        productDom.getChild("id_supplier").setText( String.valueOf( 0 ) );
        productDom.getChild("id_tax_rules_group").setText( String.valueOf( 0 ) );
        productDom.getChild("id_shop_default").setText( String.valueOf( 1 ) );
        productDom.getChild("width").setText( "0" );
        productDom.getChild("height").setText( "0" );
        productDom.getChild("depth").setText( "0" );
        productDom.getChild("weight").setText( "0" );
        productDom.getChild("quantity_discount").setText( "0" );
        
        
        productDom.getChild("minimal_quantity").setText( "1" );
        productDom.getChild("price").setText( "0" );
        productDom.getChild("wholesale_price").setText( "0" );
        productDom.getChild("unit_price_ratio").setText( "0" );
        productDom.getChild("additional_shipping_cost").setText( "0" );
        productDom.getChild("customizable").setText( "0" );
        productDom.getChild("uploadable_files").setText( "0" );
        productDom.getChild("redirect_type").setText( "404" );
        productDom.getChild("id_product_redirected").setText( "0" );
        productDom.getChild("available_for_order").setText( "1" );
        productDom.getChild("condition").setText( "new" );
        productDom.getChild("show_price").setText( "1" );
        productDom.getChild("indexed").setText( "1" );
        productDom.getChild("visibility").setText( "both" );
        
        productDom.removeChild("quantity");
        productDom.removeChild("manufacturer_name");
        productDom.removeChild("position_in_category");
        
        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        
        String value = ClientWS.postItem(this.m_ctx, "/"+resource, xml_item);
        
        product.setValue(value);
        if (value.equals("0"))
        	return false;
        return true;
	}
	
	private boolean editItem(Document xml_dom,PO po){
		
		org.openXpertya.model.MProduct product = (org.openXpertya.model.MProduct) po;
		
		Element productDom = xml_dom.getRootElement().getChild("product");
		
		//Busco categoria del producto de libertya en Presta
		org.openXpertya.model.MProductCategory category = new org.openXpertya.model.MProductCategory (this.m_ctx,product.getM_Product_Category_ID(),this.m_trx);

		productDom.getChild("id_category_default").setText( category.getValue() );
		
        productDom.getChild("description").setText( product.getDescription()); 
        String active = product.isActive() ? "1" : "0";
        productDom.getChild("active").setText( active );
        productDom.getChild("reference").setText( product.getName().replace(" ","").toLowerCase() );
        List<Element> nameLang = productDom.getChild("name").getChildren("language"); 
        for (  Element name : nameLang ){
        	name.setText( product.getName());
        }
        List<Element> linkRewriteLang = productDom.getChild("link_rewrite").getChildren("language"); 
        for (  Element linkRewrite : linkRewriteLang ){
        	linkRewrite.setText( product.getName().replace(" ","-").toLowerCase() );
        }
        
        
        //Sets category
        if (productDom.getChild("associations").getChild("categories").getChild("categories") == null){
        	productDom.getChild("associations").getChild("categories").addContent( new Element("categories").addContent(new Element( "id" )) ); 
        }
        
        productDom.getChild("associations").getChild("categories").getChild("categories").getChild("id").setText(category.getValue());
        
        productDom.removeChild("quantity");
        productDom.removeChild("manufacturer_name");
        
        
        
        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        return ClientWS.putItem(this.m_ctx, "/"+resource+"/", product.getValue(), xml_item);
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