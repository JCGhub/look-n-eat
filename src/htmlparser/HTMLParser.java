package htmlparser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class HTMLParser{

	public enum Method {GET, POST};
    
	String URL;
	ArrayList<String> xPaths;
	ArrayList<KeyValue> requestProperties = new ArrayList<KeyValue>();
	ArrayList<KeyValue> responseProperties = new ArrayList<KeyValue>();
	private iLogger logger = null;
	Method method = Method.GET;
	private Document doc = null;
	boolean https = false;
	int connectTimeout = 10000; // 10 seconds by default
	boolean encode = true;
    

	public HTMLParser(String URL) {
		this.URL = URL;
        
        this.https = URL.toLowerCase().startsWith("https");
	}
    
	public HTMLParser(String URL, ArrayList<String> xPaths) {
		this.URL = URL;
        this.xPaths = xPaths;
        
        this.https = URL.toLowerCase().startsWith("https");
	}
	
	public ArrayList<String> download() {
        if(xPaths.size() > 0) {
            ArrayList<String> results = new ArrayList<String>(xPaths.size());
            
            try {
                TagNode tagNode = new HtmlCleaner().clean(doRequest().toString());
                doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                
                XPath xpath = XPathFactory.newInstance().newXPath();
                
                for(String path: xPaths) {
                    NodeList nodes = (NodeList) xpath.evaluate(path, getDoc(),
                                    XPathConstants.NODESET);

                    for (int i = 0; i < nodes.getLength(); i++) {
                        results.add(nodes.item(i).getTextContent());
                    }
                }
                
                return results;
            } catch (XPathExpressionException e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(e.getMessage());
            }            
        }
        return null;     
    }
}