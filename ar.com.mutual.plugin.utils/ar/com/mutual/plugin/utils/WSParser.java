package ar.com.mutual.plugin.utils;
 
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import ar.com.mutual.plugin.model.MParametros;
 
public class WSParser {
	
	Properties ctx = null;
	// Item al que vamos a llamar
	String url_item = "";
	String item = "";
	// Valor del item a llamar
	String value_item = "";
	

	public WSParser(Properties m_ctx, String m_item, String v_item) {
		ctx = m_ctx;
		item =  m_item;
		value_item = v_item;
	}
	
	public ClientRequest parseResponse(byte[] resp) {
		
        // Creamos el builder basado en SAX  
        SAXBuilder builder = new SAXBuilder();  
        // Construimos el arbol DOM a partir del fichero xml  
        InputStream stream = new ByteArrayInputStream(resp);
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
        
		return null;
		
		
	}
	
	
	
}