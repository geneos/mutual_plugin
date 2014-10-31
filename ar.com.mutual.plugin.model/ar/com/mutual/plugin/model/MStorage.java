package ar.com.mutual.plugin.model;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusPO;
 
public class MStorage extends MPluginDocAction {
 
 
	public MStorage(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
	
	 
	public MPluginStatusPO afterBeforeSave(PO po, boolean newRecord) {
		
		org.openXpertya.model.MStorage storage = (org.openXpertya.model.MStorage)po;
		
		int product_ID = storage.getM_Product_ID();

		// Si el parámetro M_Warehouse_ID posición 1 es 0 no filtra por almacenes.
		
		BigDecimal stock = org.openXpertya.model.MStorage.getQtyAvailable(0, product_ID);
		
			
		try {
            
            // Obtener los datos de conexión desde la tabla de parámetros
			
            String userId = MParametros.getParameterValueByName(this.m_ctx, "userPrestashop", null);
            String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
            String URLParam = MParametros.getParameterValueByName(this.m_ctx, "urlPrestashop", null);
                	
            URLParam += "/stock_availables";
                
            //String userId = "44JBNUUQY34YG5R2BHNFJ6AC3XJ7Z8IF";
            //String password = "";

            Credentials credentials = new UsernamePasswordCredentials(userId, password);
            HttpClient httpClient = new HttpClient();
            httpClient.getState().setCredentials(AuthScope.ANY, credentials);
            httpClient.getParams().setAuthenticationPreemptive(true);

            ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

            URI uri = new URI(URLParam);
            
            ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri); 
            
            // Request necesita instanciar el producto a actualizar stock
            // URLParam/id

            String URLParamItem = URLParam + "/4";
            
            
            ClientRequest requestGet = fac.createRequest(URLParamItem);

            requestGet.accept("application/xml");
            
            ClientResponse<String> response = requestGet.get(String.class);
            System.out.println(response.getEntity());
            
            

            if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                                    + response.getStatus());
            } else {
                
                // Creamos el builder basado en SAX  
                SAXBuilder builder = new SAXBuilder();  
                // Construimos el arbol DOM a partir del fichero xml  
                InputStream stream = new ByteArrayInputStream(response.getEntity().getBytes("UTF-8"));
                Document documentJDOM = builder.build(stream);
                
                // Obtengo el valor de la etiqueta a modificar
                
                Element etiquetaPrestashop = documentJDOM.getRootElement();
                Element etiquetaStockDisp = etiquetaPrestashop.getChild("stock_available");
                Element etiquetaQty = etiquetaStockDisp.getChild("quantity");
                
                String texto = etiquetaQty.getText();
                
                System.out.println(texto);
                
                etiquetaQty.setText("1000");
                
                String texto_nuevo = etiquetaQty.getText();
                                        
                System.out.println(texto_nuevo);

                ClientRequest requestAdd = fac.createRequest(URLParam + "/{id}");


                // Vamos a serializar el XML  
                // Lo primero es obtener el formato de salida  
                // Partimos del "Formato bonito", aunque también existe el plano y el compacto  
                
                Format format = Format.getRawFormat();  
                
                // Creamos el serializador con el formato deseado  
                
                XMLOutputter xmloutputter = new XMLOutputter(format);  
                
                // Serializamos el document parseado  
                
                String xmltext = xmloutputter.outputString(documentJDOM.getDocument());                          

                System.out.println(xmltext);
                
                XMLOutputter serializer = new XMLOutputter();                       
                
                xmltext = serializer.outputString(documentJDOM);
                                  
                System.out.println(xmltext);
                
                requestAdd.accept("application/xml").pathParameter("id", 4).body( MediaType.APPLICATION_XML, xmltext);

                ClientResponse responsePut = requestAdd.put();
                //get response and automatically unmarshall to a string.

                System.out.println(responsePut.getStatus());
                //get response and automatically unmarshall to a string.

                System.out.println(responsePut);                        
                
                
                
                
            }
            

		} catch (ClientProtocolException e) {
	
			e.printStackTrace();
	
		} catch (IOException e) {
	
			e.printStackTrace();
	
		} catch (Exception e) {
	
			e.printStackTrace();
	
		}
			
			
		return status_po;
	}	
 
	
}