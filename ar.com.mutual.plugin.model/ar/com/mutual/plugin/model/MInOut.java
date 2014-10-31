package ar.com.mutual.plugin.model;
 
import java.io.IOException;
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
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;

import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;
 
public class MInOut extends MPluginDocAction {
 
 
	public MInOut(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		
		org.openXpertya.model.MInOut inout = (org.openXpertya.model.MInOut)document;
		
		// Verifico si es transacción de ventas [if] o de compras [else]
		
		if(inout.isSOTrx()) {

			try {
                
                String userId = "44JBNUUQY34YG5R2BHNFJ6AC3XJ7Z8IF";
                String password = "";

                Credentials credentials = new UsernamePasswordCredentials(userId, password);
                HttpClient httpClient = new HttpClient();
                httpClient.getState().setCredentials(AuthScope.ANY, credentials);
                httpClient.getParams().setAuthenticationPreemptive(true);
 
                ClientExecutor clientExecutor = new ApacheHttpClientExecutor(httpClient);

                URI uri = new URI("http://localhost/prestashop/api/zones");
                ClientRequestFactory fac = new ClientRequestFactory(clientExecutor,uri); 

                
                /*

                Para trabajar con parámetros
                
                ClientRequest requestAdd = fac.createRequest("http://testgeneos.com.ar/shop/api/zones/{id}");
                StringBuilder sb = new StringBuilder();
                sb.append("<prestashop><zone><id>33</id><name>Falucho2</name><active>1</active></zone></prestashop>");
                String xmltext = sb.toString();
                requestAdd.accept("application/xml").pathParameter("id", 33).body( MediaType.APPLICATION_XML, xmltext);

                 * 
                 */                    
                
                ClientRequest requestAdd = fac.createRequest("http://localhost/prestashop/api/zones/");

                StringBuilder sb = new StringBuilder();
                
                
                sb.append("<prestashop><zone><id>9</id><name>Falucho848</name><active>1</active></zone></prestashop>");


                String xmltext = sb.toString();

                requestAdd.accept("application/xml").body( MediaType.APPLICATION_XML, xmltext);

                String response = requestAdd.postTarget( String.class);
                //get response and automatically unmarshall to a string.

                System.out.println(response);

			} catch (ClientProtocolException e) {
		
				e.printStackTrace();
		
			} catch (IOException e) {
		
				e.printStackTrace();
		
			} catch (Exception e) {
		
				e.printStackTrace();
		
			}
					
		} else {
			
		}
			
		return status_docAction;
		
	}
	
}