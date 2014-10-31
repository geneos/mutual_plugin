package ar.com.mutual.plugin.utils;
 
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;

import ar.com.mutual.plugin.model.MParametros;
 
public class ClientRequestInstance extends ClientRequest {
	
	Properties ctx = null;
	// Item al que vamos a llamar
	String url_item = "";
	String item = "";
	// Valor del item a llamar
	String value_item = "";
	

	public ClientRequestInstance(Properties m_ctx, String m_item, String v_item) {
		super(m_item);
		ctx = m_ctx;
		item =  m_item;
		value_item = v_item;
	}
	
	public ClientRequest configure() {
		
        // Obtener los datos de conexión desde la tabla de parámetros
		
        String userId = MParametros.getParameterValueByName(ctx, "userPrestashop", null);
        
        // Deberíamos de aceptar valores nulos o como en esta prueba password no sacarlo de la tabla
        // String password = MParametros.getParameterValueByName(this.m_ctx, "passwordPrestashop", null);
                
        String password = "";
        
        url_item = MParametros.getParameterValueByName(ctx, "urlPrestashop", null) + item;

        Credentials credentials = new UsernamePasswordCredentials(userId, password);
        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        httpClient.getParams().setAuthenticationPreemptive(true);

        ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

        URI uri;
		try {
			uri = new URI(url_item);
	        ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri);        
	        ClientRequest requestGet = fac.createRequest(url_item + value_item);
	        requestGet.accept("application/xml");
	        return requestGet;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return null;
		
		
	}
	
	
	
}