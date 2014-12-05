package ar.com.mutual.plugin.utils;
 
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jdom2.Document;
import org.jdom2.Element;

import ar.com.mutual.plugin.model.MParametros;
 
public class ClientWS {

	public ClientWS() {
	}
	
	
	public static ClientRequest getItem(Properties m_ctx, String m_item, String v_item) {
		
        // Obtener los datos de conexión desde la tabla de parámetros
		
        String userId = MParametros.getParameterValueByName(m_ctx, "userPrestashop", null);
        
        // Deberíamos de aceptar valores nulos o como en esta prueba password no sacarlo de la tabla
        // String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
                
        String password = "";
        
        String url_item = MParametros.getParameterValueByName(m_ctx, "urlPrestashop", null) + m_item;
        
        Credentials credentials = new UsernamePasswordCredentials(userId, password);
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        httpClient.getParams().setAuthenticationPreemptive(true);

        ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

        URI uri;
		try {
			uri = new URI(url_item);
	        ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri);        
	        ClientRequest requestGet = fac.createRequest(url_item + v_item);
	        requestGet.accept("application/xml");
	        return requestGet;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
		
		
	}
	
	//Return ID of element
	public static String postItem(Properties m_ctx, String m_item, String xml_item) {
		
        // Obtener los datos de conexión desde la tabla de parámetros
		
        //String userId = MParametros.getParameterValueByName(m_ctx, "userPrestashop", null);
		String userId = "F9D8SLWW4CA5QPNSIK45FH1HPN2F8KCX";
		
        // Deberíamos de aceptar valores nulos o como en esta prueba password no sacarlo de la tabla
        // String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
                
        String password = "";
        
        //String url_item = MParametros.getParameterValueByName(m_ctx, "urlPrestashop", null) + m_item;
        String url_item = "http://localhost/www/prestashop/api"+ m_item;
        
        Credentials credentials = new UsernamePasswordCredentials(userId, password);
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        httpClient.getParams().setAuthenticationPreemptive(true);

        ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

        URI uri;
		try {
			uri = new URI(url_item);
	        ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri);    
            ClientRequest requestAdd = fac.createRequest(url_item);
            
            requestAdd.accept("application/xml").body( MediaType.APPLICATION_XML, xml_item);
            
            ClientResponse<String> responsePost = requestAdd.post(String.class);
            
            if (responsePost.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : " + responsePost.getStatus());
	        } else {	
	        	Document xmlDOM = WSParser.getDOMfromXML(responsePost);
	            String idResource = xmlDOM.getRootElement().getChildren().get(0).getChildText("id");
	        	System.out.println("Actualización exitosa");
	        	return idResource;
	        }
                
            
            
            
            //get response and automatically unmarshall to a string.
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return "0";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// Catch por requestAdd.put();
			
			e.printStackTrace();
			return "0";
		}
	}
	
	
	public static boolean putItem(Properties m_ctx, String m_item, String v_item, String xml_item) {
		
        // Obtener los datos de conexión desde la tabla de parámetros
		
        //String userId = MParametros.getParameterValueByName(m_ctx, "userPrestashop", null);
		String userId = "F9D8SLWW4CA5QPNSIK45FH1HPN2F8KCX";
		
        // Deberíamos de aceptar valores nulos o como en esta prueba password no sacarlo de la tabla
        // String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
                
        String password = "";
        
        //String url_item = MParametros.getParameterValueByName(m_ctx, "urlPrestashop", null) + m_item;
        String url_item = "http://localhost/www/prestashop/api"+ m_item;
        
        Credentials credentials = new UsernamePasswordCredentials(userId, password);
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        httpClient.getParams().setAuthenticationPreemptive(true);

        ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

        URI uri;
		try {
			uri = new URI(url_item);
	        ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri);    
            ClientRequest requestAdd = fac.createRequest(url_item + "{id}");
            
            System.out.println(xml_item);
            
            requestAdd.accept("application/xml").pathParameter("id", v_item).body( MediaType.APPLICATION_XML, xml_item);

            ClientResponse responsePut = requestAdd.put();
            
            if (responsePut.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + responsePut.getStatus());
	        } else {	
	        	System.out.println("Actualización exitosa");
	        	return true;
	        }
                
            
            
            
            //get response and automatically unmarshall to a string.
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// Catch por requestAdd.put();
			
			e.printStackTrace();
			return false;
		}
	
	}
	
public static boolean deleteItem(Properties m_ctx, String m_item, String v_item) {
		
        // Obtener los datos de conexión desde la tabla de parámetros
		
        //String userId = MParametros.getParameterValueByName(m_ctx, "userPrestashop", null);
		String userId = "F9D8SLWW4CA5QPNSIK45FH1HPN2F8KCX";
		
        // Deberíamos de aceptar valores nulos o como en esta prueba password no sacarlo de la tabla
        // String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
                
        String password = "";
        
        //String url_item = MParametros.getParameterValueByName(m_ctx, "urlPrestashop", null) + m_item;
        String url_item = "http://localhost/www/prestashop/api"+ m_item;
        
        Credentials credentials = new UsernamePasswordCredentials(userId, password);
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        httpClient.getParams().setAuthenticationPreemptive(true);

        ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

        URI uri;
		try {
			uri = new URI(url_item);
	        ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri);    
            ClientRequest requestAdd = fac.createRequest(url_item + "{id}");
            
            requestAdd.accept("application/xml").pathParameter("id", v_item);

            ClientResponse responsePut = requestAdd.delete();
            
            if (responsePut.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + responsePut.getStatus());
	        } else {	
	        	System.out.println("Actualización exitosa");
	        	return true;
	        }
                          
            //get response and automatically unmarshall to a string.
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// Catch por requestAdd.put();
			
			e.printStackTrace();
			return false;
		}		
	}	
	
	public static Document getDomBlank(Properties ctx, String resource) throws Exception{
		Document retValue = null;
		ClientRequest request = null;
		request = ClientWS.getItem(ctx, "/"+resource+"?schema=blank" , "");
		ClientResponse<String> response = request.get(String.class);
	 	if (response.getStatus() != 200) {
	         throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	 	} 
		retValue = WSParser.getDOMfromXML(response);	
		return retValue;
	};
	
	public static Document getDom(Properties ctx, String resource, String productValue) throws Exception{
		Document retValue = null;
		ClientRequest request = null;
		
		request = null;
		request = ClientWS.getItem(ctx,"/"+resource+"/",productValue);
		ClientResponse<String> response2 = request.get(String.class);
		
		//No Existe en Presta
		if (response2.getStatus() == 404) 
			return null;
		
		//Error
     	if (response2.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
     	} 
     	retValue = WSParser.getDOMfromXML(response2);	     
	        
		return retValue;
	};
	
	
}