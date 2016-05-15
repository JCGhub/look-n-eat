package htmlparser;
import database.*;

import java.util.*;

public class DisplayInfo{	
	
	Map<String, ArrayList<String>> mapRest;
	Map<String, String> mapNameRest;
	Map<String, String> mapNameRest2 = new HashMap<String, String>();
	ArrayList<String> arrayComm = new ArrayList<String>();
	ArrayList<String> xPath1;
	ArrayList<String> xPath2;
	String URL;
	String numPages = new String();
	int n = 1;
	ConnectDB db = new ConnectDB();
	
	public DisplayInfo(Map<String, ArrayList<String>> mapRest, Map<String, String> mapNameRest, ArrayList<String> xPath1, ArrayList<String> xPath2, String URL){
		this.mapRest = mapRest;
		this.mapNameRest = mapNameRest;
		this.xPath1 = xPath1;
		this.xPath2 = xPath2;
		this.URL = URL;
	}
	
	public void downloadNameRest(String URL){
		HTMLParser request = new HTMLParser(URL, xPath1, xPath2);
        mapNameRest2 = request.downloadRestAndURL();
        
        for(String key : mapNameRest2.keySet()){
        	if(mapNameRest.containsKey(key)){
        		//System.out.println("***** YA HAY UN RESTAURANTE CON EL NOMBRE "+key+" *****");
        	}
        	else{
        		mapNameRest.put(key, mapNameRest2.get(key));    		    
    		    //System.out.println(key);
        	}		    
        }
    }	
	
	public void downloadCommRest(String key, String URL, ArrayList<String> xPath3, Map<String, ArrayList<String>> mapNameComm){
		HTMLParser request = new HTMLParser(URL, xPath3);
        arrayComm = request.download();
        
        mapNameComm.put(key, arrayComm);
    }
	
	public String downloadNumPages(ArrayList<String> xPath3){
		HTMLParser request = new HTMLParser(URL, xPath3);
		numPages = request.downloadString();
		
		return numPages;
	}
	
	public void displayMapNameRest(){
		if(mapNameRest.isEmpty()){
			System.out.println("Vacio");
		}
		else{			
			for(String key : mapNameRest.keySet()){
			    System.out.println("\nRestaurante "+n+": "+key);
			    
			    n++;
			}
		}
	}
	
	public void displayMapCommRest(Map<String, ArrayList<String>> mapNameComm){
		if(mapNameComm.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			for(String key : mapNameComm.keySet()){
			    System.out.println("\nRestaurante: "+key+"\nComment: "+mapNameComm.get(key).get(1));
			}
        }
	}
	
	public void startConnectionDB(){
		db.MySQLConnection("root", "", "lookneat_db");
	}
	
	public void closeConnectionDB(){
		db.closeConnection();
	}
	
	public void createTable(String nTable){
		db.createTable(nTable);
	}
	
	public void insertMapNameRestIntoDB(String nTable){
		for(String key : mapNameRest.keySet()){
			db.insertData(nTable, key, mapNameRest.get(key));
		}
	}
	
	public Map<String, String> getMapNameRest(){
		return this.mapNameRest;
	}
	
	public Map<String, ArrayList<String>> getMap(){
		return this.mapRest;
	}	
	
	public static void main(String[] args) throws Exception{
		Map<String, ArrayList<String>> mapRest = new HashMap<String, ArrayList<String>>();
		Map<String, String> mapNameRest = new HashMap<String, String>();
		ArrayList<String> xPath1 = new ArrayList<>();
		ArrayList<String> xPath2 = new ArrayList<>();
		xPath1.add("//a[@class='property_title']");
		xPath2.add("//div/div/h3/a/@href");
		String URL = "https://www.tripadvisor.es/Restaurants-g187432-[[oaxx]]-Cadiz_Costa_de_la_Luz_Andalucia.html";
		//String URL = "https://www.tripadvisor.es/Restaurants-g187433-[[oaxx]]-Chiclana_de_la_Frontera_Costa_de_la_Luz_Andalucia.html";
		String nTable = "test_table";
		
		DisplayInfo dI = new DisplayInfo(mapRest, mapNameRest, xPath1, xPath2, URL);
		
		ArrayList<String> xPath4 = new ArrayList<>();
		xPath4.add("//div[3]/div/div/a[6]/@data-page-number");
		String numPagesStr = dI.downloadNumPages(xPath4);
		int numPages = Integer.parseInt(numPagesStr);
		
		System.out.println("Numero de paginas: "+numPages);
		
		//System.out.println("-------------------- RESTAURANTS --------------------");
		
		int URLNumPatt = 0;
		String URLPatt = "oa";
		String patt = "[[oaxx]]";
		
		for(int i = 0; i < numPages; i++){
			String newPatt = URLPatt+URLNumPatt;
			String URLgen = URL.replace(patt, newPatt);
			
			URLNumPatt = URLNumPatt + 30;
			URL = URLgen;
			patt = newPatt;
			
			dI.downloadNameRest(URL);
			//System.out.println("\n"+URL);
			//dI.displayMapNameRest();
		}
		
		System.out.println("Numero de restaurantes: "+dI.getMapNameRest().size());
		
		//dI.displayMapNameRest();
		
		System.out.println("-------------------- DATABASE --------------------");
		
		dI.startConnectionDB();
		
		dI.createTable(nTable);
		dI.insertMapNameRestIntoDB(nTable);		
		
		dI.closeConnectionDB();
		
		
		/*mapNameRest = dI.downloadNameRest();		
		dI.displayMapNameRest();*/		
		
		/*System.out.println("-------------------- COMMENTS --------------------");
		
		ArrayList<String> xPath3 = new ArrayList<>();
		xPath3.add("//div[2]/div/div/div[3]/p");
		
		Map<String, ArrayList<String>> mapNameComm = new HashMap<String, ArrayList<String>>();
		
		for(String key : mapNameRest.keySet()){
			String URLRest = (String)mapNameRest.get(key);
			
		    dI.downloadCommRest(key, URLRest, xPath3, mapNameComm);
		}
		
		dI.displayMapCommRest(mapNameComm);*/
	}
}