package htmlparser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
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

import htmlparser.iLogger;
import htmlparser.KeyValue;
import htmlparser.HTMLParser.Method;
import htmlparser.DefaultLogger;


public class HTMLParser{

	public enum Method {GET, POST};
    
	String URL;
	ArrayList<KeyValue> parameters;
	ArrayList<String> xPaht1;
	ArrayList<String> xPaht2;
	ArrayList<KeyValue> requestProperties = new ArrayList<KeyValue>();
	ArrayList<KeyValue> responseProperties = new ArrayList<KeyValue>();
	private iLogger logger = null;
	Method method = Method.GET;
	private Document doc = null;
	boolean https = false;
	int connectTimeout = 10000; // 10 seconds by default
	boolean encode = true;
    

	public HTMLParser(String URL) {
        init(URL, Method.GET, new ArrayList<KeyValue>(), new ArrayList<String>(), new ArrayList<String>(), new DefaultLogger());
	}
    
	public HTMLParser(String URL, ArrayList<String> xPaht1) {
		init(URL, Method.GET, new ArrayList<KeyValue>(), xPaht1, new ArrayList<String>(), new DefaultLogger());
	}
	
	public HTMLParser(String URL, ArrayList<String> xPaht1, ArrayList<String> xPath2) {
		init(URL, Method.GET, new ArrayList<KeyValue>(), xPaht1, xPath2, new DefaultLogger());
	}
	
	private void init(String URL, Method method, ArrayList<KeyValue> parameters, ArrayList<String> xPaht1, ArrayList<String> xPaht2, iLogger logger) {
        this.URL = URL;
        this.parameters = parameters;
        this.xPaht1 = xPaht1;
        this.xPaht2 = xPaht2;
        this.setLogger(logger);
        this.method = method;
        
        this.https = URL.toLowerCase().startsWith("https");
    }
	
	public ArrayList<String> download() {
        if(xPaht1.size() > 0) {
            ArrayList<String> results = new ArrayList<String>(xPaht1.size());
            
            try {
                TagNode tagNode = new HtmlCleaner().clean(doRequest().toString());
                doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                
                XPath xpath = XPathFactory.newInstance().newXPath();
                
                for(String path: xPaht1) {
                    NodeList nodes = (NodeList) xpath.evaluate(path, getDoc(), XPathConstants.NODESET);

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
	
	public Map<String, String> downloadRestAndURL() {
        if(xPaht1.size() > 0) {
        	Map<String, String> results = new HashMap<String, String>();
            
            try {
                TagNode tagNode = new HtmlCleaner().clean(doRequest().toString());
                doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                
                XPath xpath1 = XPathFactory.newInstance().newXPath();
                XPath xpath2 = XPathFactory.newInstance().newXPath();
                int j = 0;
                
                for(String path: xPaht1) {
                    NodeList nodes1 = (NodeList) xpath1.evaluate(path, getDoc(), XPathConstants.NODESET);
                    NodeList nodes2 = (NodeList) xpath2.evaluate(xPaht2.get(j), getDoc(), XPathConstants.NODESET);
                    
                    j++;
                    
                    for (int i = 0; i < nodes1.getLength(); i++) {
                        results.put(nodes1.item(i).getTextContent(), "https://www.tripadvisor.es"+nodes2.item(i).getTextContent());
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
	
	private StringBuffer doRequest() throws Exception {
        return doRequest(-1);
    }
    
    private StringBuffer doRequest(int bytes) throws Exception {
        String urlParameters = getParameters(); 
        
        String url = URL;
        if(method == Method.GET && !urlParameters.isEmpty()) {
            if(url.endsWith("&") || url.endsWith("?")) {
                url = url + urlParameters;
            } else {
                url = url + "?" + urlParameters;
            }
        }
        
        URL obj = new URL(url);
        
        HttpURLConnection con;
        if(https) {
            con = (HttpsURLConnection) obj.openConnection();
        } else {
            con = (HttpURLConnection) obj.openConnection();
        }
        
        con.setConnectTimeout(connectTimeout);
        con.setReadTimeout(connectTimeout);

        //add request header
        String methodName = "GET";
        if(method == Method.POST) {
            methodName = "POST";
        }
        con.setRequestMethod(methodName);
        
        for(KeyValue par: requestProperties) {
            con.setRequestProperty(par.key, par.value);
        }    

        if(method == Method.GET) {
            // @todo necesita hacer algo diferente a cuando se trata de un POST?
        } else {
            // Send post request
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(urlParameters);
                wr.flush();
            }
        }

        logger.log("\nSending "+ methodName +" request to URL : " + url, iLogger.Level.INFO);
        if(method == Method.POST) {
            logger.log("POST parameters : " + urlParameters, iLogger.Level.INFO);
        }
        
        int responseCode = con.getResponseCode();
        StringBuffer response = new StringBuffer();
        
        logger.log("Response: " + responseCode, iLogger.Level.INFO);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        
        if(bytes < 0) {        
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
                //logger.log(inputLine);
            }
        } else {
            char[] array = new char[bytes];            
            int read = in.read(array);
            response.append(array);
            
            if(read < bytes) {
                logger.log("Waring: expecting " + bytes + " bytes at least but received " + read, iLogger.Level.INFO);
            }
        }
        
        in.close();
        
        logger.log("\nResponse header:", iLogger.Level.DEBUG);
        int i = 0;
        while(con.getHeaderField(i) != null) {
            responseProperties.add(new KeyValue(con.getHeaderFieldKey(i), con.getHeaderField(i)));
            logger.log("\n"+ con.getHeaderFieldKey(i) +": " + con.getHeaderField(i), iLogger.Level.DEBUG);
            ++i;
        }
        
        logger.log("\n" + response, iLogger.Level.DEBUG);
        
        return response;
    }
    
    public String getParameters() {
        return parametersToString(parameters, encode);
    }
    
    public static String parametersToString(ArrayList<KeyValue> parameters) {
        return parametersToString(parameters, true);
    }
    
    public static String parametersToString(ArrayList<KeyValue> parameters, boolean encode) {
        String urlParameters = "";
        if(encode) {
            for(KeyValue par: parameters) {
                urlParameters += "&" + par.key + "=" + par.value;
            }
        } else {
            for(KeyValue par: parameters) {
                urlParameters += "&" + URLEncoder.encode(par.key) + "=" + URLEncoder.encode(par.value);
            }
        }
        
        if(!urlParameters.isEmpty()) {
            urlParameters = urlParameters.substring(1, urlParameters.length());
        }
        
        return urlParameters;
    }
    
    public iLogger getLogger() {
        return logger;
    }
    
    public void setLogger(iLogger logger) {
        this.logger = logger;
    }
	
	public Document getDoc() {
        return doc;
    }
}