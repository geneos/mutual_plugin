package ar.com.mutual.plugin.model;
 
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jdom2.Document;
import org.jdom2.Element;
import org.openXpertya.model.MMovementLine;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;

import ar.com.mutual.plugin.utils.ClientWS;
import ar.com.mutual.plugin.utils.WSParser;
 
public class MOrder extends MPluginDocAction {
 
	static final String resource = "order_histories";
	private int idOrdenLista = 13;
 
	public MOrder(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public void sincronizarConPresta(org.openXpertya.model.MOrder order) throws 
	ClientProtocolException, 
	IOException, 
	Exception 
	{
		Document xml_dom = null;

		//Creo un nuevo historial para la orden pasandola de estado
		
		//Obtengo XML del recurso en blanco
		xml_dom = ClientWS.getDomBlank(this.m_ctx,resource );
		//Sincronizo Datos
		if (!addItem(xml_dom,order));
			throw new Exception("MOrder.postCompleteit: No se pudo agregar historial en Presta");

	}
	
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		boolean error = false;
		org.openXpertya.model.MOrder order = (org.openXpertya.model.MOrder)document;
		
		try {
			String sinc = MParametros.getParameterValueByName(m_ctx, "sincMOrder", null);
			if (sinc != null && sinc.toLowerCase().equals("si")){
				sincronizarConPresta(order);
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
	
private boolean addItem(Document xml_dom,org.openXpertya.model.MOrder order){
		
	
		Element orderHistoryDom = xml_dom.getRootElement().getChild("order_history");
		
		orderHistoryDom.getChild("id_order_state").setText( String.valueOf( idOrdenLista ) );
		orderHistoryDom.getChild("id_order").setText( order.getDocumentNo() );

        String xml_item = WSParser.getXMLfromDOM(xml_dom);
        
        String value = ClientWS.postItem(this.m_ctx, "/"+resource, xml_item);

        if (value.equals("0"))
        	return false;
        return true;
	}
	
	private boolean editItem(Document xml_dom,PO po){		
        return true;
	}
	
	
	
}