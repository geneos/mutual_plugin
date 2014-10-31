package ar.com.mutual.plugin.model;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.openXpertya.model.MMovementLine;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginDocAction;
import org.openXpertya.plugin.MPluginStatusDocAction;
import org.openXpertya.process.DocAction;

import ar.com.mutual.plugin.utils.ClientRequestInstance;
 
public class MMovement extends MPluginDocAction {
 
 
	public MMovement(PO po, Properties ctx, String trxName, String aPackage) {
		super(po, ctx, trxName, aPackage);
		// TODO Auto-generated constructor stub
	}
 
	public MPluginStatusDocAction postCompleteIt(DocAction document) {
		
		org.openXpertya.model.MMovement mov = (org.openXpertya.model.MMovement)document;
		
		int[] movlines_id = MMovementLine.getAllIDs("M_MovementLine", "M_Movement_ID = " + mov.getM_Movement_ID(), null);

		try {
			
			for(int ind=0;ind<movlines_id.length;ind++) {
				
				
				
				
			}
			
			String URLItem = "/stock_availables";
			
			ClientRequestInstance requestGet = new ClientRequestInstance(this.m_ctx, URLItem, "");
            ClientResponse<String> response = requestGet.get(String.class);
            System.out.println(response.getEntity());
            
            

            if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                                    + response.getStatus());
            } else {
                
                
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

			
		return status_docAction;
		
	}
	
}