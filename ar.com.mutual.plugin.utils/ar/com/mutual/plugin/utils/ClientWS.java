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
	
	public static boolean putItem(Properties m_ctx, String m_item, String v_item, String xml_item) {
		
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
	
	
	
}