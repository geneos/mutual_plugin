package ar.com.mutual.plugin.utils;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import ar.com.mutual.plugin.model.MParametros;
 
public class WSParser {


	public WSParser(Properties m_ctx, String m_item, String v_item) {
	}
	
	public static String parserUpdateStock(byte[] resp, BigDecimal cant, String accion) {
		
        // Creamos el builder basado en SAX  
        SAXBuilder builder = new SAXBuilder();  
        // Construimos el arbol DOM a partir del fichero xml  
        InputStream stream = new ByteArrayInputStream(resp);
        Document documentJDOM;
		try {
			documentJDOM = builder.build(stream);
	        // Obtengo el valor de la etiqueta a modificar
	        
	        Element etiquetaPrestashop = documentJDOM.getRootElement();
	        Element etiquetaStockDisp = etiquetaPrestashop.getChild("stock_available");
	        Element etiquetaQty = etiquetaStockDisp.getChild("quantity");
	        
	        BigDecimal anterior = new BigDecimal(etiquetaQty.getText().replaceAll(",", ""));
	        
	        if(accion == "sumar") {
	        	anterior = anterior.add(cant);	
	        }
	        else if(accion == "restar") {
	        	anterior = anterior.subtract(cant);	
	        }
	        else if(accion == "cambiar") {
	        	anterior = cant;
	        }
	        
	        
	        	
	        	
	        	
	        
	        // Solo admite valores enteros
	        
	        etiquetaQty.setText(anterior.setScale(0).toString());

	        // Vamos a serializar el XML  
	        // Lo primero es obtener el formato de salida  
	        // Partimos del "Formato bonito", aunque también existe el plano y el compacto  
	        
	        Format format = Format.getRawFormat();  
	        
	        // Creamos el serializador con el formato deseado  
	        
	        XMLOutputter xmloutputter = new XMLOutputter(format);  
	        
	        // Serializamos el document parseado  
	        
	        String xmltext = xmloutputter.outputString(documentJDOM.getDocument());                          
	        
	        XMLOutputter serializer = new XMLOutputter();                       
	        xmltext = serializer.outputString(documentJDOM);
	                          
	        return xmltext;

		
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		return null;
		
		
	}

	public static String parserUpdatePrice(byte[] resp, String priceStd) {
		
        // Creamos el builder basado en SAX  
        SAXBuilder builder = new SAXBuilder();  
        // Construimos el arbol DOM a partir del fichero xml  
        InputStream stream = new ByteArrayInputStream(resp);
        Document documentJDOM;
		try {
			documentJDOM = builder.build(stream);
	        // Obtengo el valor de la etiqueta a modificar
	        
	        Element etiquetaPrestashop = documentJDOM.getRootElement();
	        Element etiquetaProduct = etiquetaPrestashop.getChild("product");
	        Element etiquetaPrice = etiquetaProduct.getChild("price");
	        
	        etiquetaPrice.setText(priceStd);
	        
	        //Element etiquetaLrw = etiquetaProduct.getChild("link_rewrite");
	        
	        //etiquetaLrw.setText("LR1");
       
	        
            // Prueba de eliminar posibles conflictos
            
            etiquetaProduct.removeChild("associations");
            etiquetaProduct.removeChild("manufacturer_name");
            etiquetaProduct.removeChild("quantity");
            etiquetaProduct.removeChild("id_shop_default");
            
            
            

	        // Vamos a serializar el XML  
	        // Lo primero es obtener el formato de salida  
	        // Partimos del "Formato bonito", aunque también existe el plano y el compacto  
	        
	        Format format = Format.getRawFormat();  
	        
	        // Creamos el serializador con el formato deseado  
	        
	        XMLOutputter xmloutputter = new XMLOutputter(format);  
	        
	        // Serializamos el document parseado  
	        
	        String xmltext = xmloutputter.outputString(documentJDOM.getDocument());                          
	        
	        XMLOutputter serializer = new XMLOutputter();                       
	        xmltext = serializer.outputString(documentJDOM);
	                          
	        return xmltext;

		
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		return null;

	}
	
	
	
}