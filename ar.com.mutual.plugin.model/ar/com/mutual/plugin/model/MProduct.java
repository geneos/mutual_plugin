package ar.com.mutual.plugin.model;
 
import java.util.Properties;

import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginPO;
import org.openXpertya.plugin.MPluginStatusPO;
 
public class MProduct extends MPluginPO {
 
 
	public MProduct(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
	
	 
	public MPluginStatusPO afterBeforeSave(PO po, boolean newRecord) {
		
		org.openXpertya.model.MProduct product = (org.openXpertya.model.MProduct) po;
		System.out.println(product.getName());
		
		/*
		
		if(Integer.getInteger(priceListVersionParameter_ID).intValue() == priceListVersion_ID) {
                	
                try {
                	
                	
                	
                	// Obtener los datos de conexión desde la tabla de parámetros
    				
                    //String userId = MParametros.getParameterValueByName(this.m_ctx, "userPrestashop", null);
                    //String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
                    //String URLParam = MParametros.getParameterValueByName(this.m_ctx, "urlPrestashop", null);
                    
                    String userId = "44JBNUUQY34YG5R2BHNFJ6AC3XJ7Z8IF";
                    String password = "";

                    Credentials credentials = new UsernamePasswordCredentials(userId, password);
                    HttpClient httpClient = new HttpClient();
                    httpClient.getState().setCredentials(AuthScope.ANY, credentials);
                    httpClient.getParams().setAuthenticationPreemptive(true);
     
                    ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

                    URI uri = new URI("http://localhost/prestashop/api/stock_availables");
                    
                    ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri); 

                    ClientRequest requestGet = fac.createRequest("http://localhost/prestashop/api/stock_availables/4");

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
                        
                        
                        // Prueba de eliminar posibles conflictos
                        
                        //etiquetaProduct.removeChild("associations");
                        //etiquetaProduct.removeChild("position_in_category");
                        //etiquetaProduct.removeChild("meta_description");
                        //etiquetaProduct.removeChild("meta_keywords");
                        //etiquetaProduct.removeChild("meta_title");
                        //etiquetaProduct.removeChild("link_rewrite");
                        //etiquetaProduct.removeChild("name");
                        //etiquetaProduct.removeChild("description");
                        //etiquetaProduct.removeChild("description_short");
                        //etiquetaProduct.removeChild("available_now");
                        //etiquetaProduct.removeChild("available_later");
                        
                        
                        


                        //Para trabajar con parámetros


                        ClientRequest requestAdd = fac.createRequest("http://localhost/prestashop/api/stock_availables/{id}");
                        StringBuilder sb = new StringBuilder();


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
			
		}
		
		*/
		
		return status_po;
	}	
 
	
}