package htmlparser;

import database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DisplayInfo{	
	
	Map<String, String> mapNameURL = new HashMap<String, String>();
	Map<String, String> mapNameURL2 = new HashMap<String, String>();
	Map<String, ArrayList<String>> mapNameComm = new HashMap<String, ArrayList<String>>();
	ArrayList<String> arrayComm = new ArrayList<String>();
	ArrayList<String> xPath1;
	ArrayList<String> xPath2;
	String mainURL;
	String numPages = new String();
	ConnectDB db = new ConnectDB();
	int n = 1;
	
	public DisplayInfo(ArrayList<String> xPath1, ArrayList<String> xPath2, String mainURL){
		this.xPath1 = xPath1;
		this.xPath2 = xPath2;
		this.mainURL = mainURL;
	}
	
	public void downloadNameURL(String URL){
		HTMLParser hP = new HTMLParser(URL, xPath1, xPath2);
        mapNameURL2 = hP.downloadAsMap();
        
        for(String key : mapNameURL2.keySet()){
        	if(mapNameURL.containsKey(key)){
        		//System.out.println("***** YA HAY UN RESTAURANTE CON EL NOMBRE "+key+" *****");
        	}
        	else{
        		mapNameURL.put(key, mapNameURL2.get(key));    		    
    		    //System.out.println(key);
        	}		    
        }
    }	
	
	public void downloadComm(String key, String URL, ArrayList<String> xPath3){
		HTMLParser hP = new HTMLParser(URL, xPath3);
        arrayComm = hP.downloadAsArray();
        
        // Seguramente, tras modificar HTMLParser para que nos devuelva una URL genérica,
        // debamos añadir a la URL de cada restaurante el inicio de la ruta como hicimos
        // al principio con TripAdvisor.
        
        mapNameComm.put(key, arrayComm);
    }
	
	public String downloadNumPages(ArrayList<String> xPath3, int n){
		if(n == 1){
			System.out.println("Counting pages of TripAdvisor...");
			
			HTMLParser hP = new HTMLParser(mainURL, xPath3);
			numPages = hP.downloadAsString();
		}
		
		return numPages;
	}
	
	public void displayMapNameURL(){
		if(mapNameURL.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{			
			for(String key : mapNameURL.keySet()){
			    System.out.println("\nRestaurante "+n+": "+key);
			    
			    n++;
			}
		}
	}
	
	public void displayMapComm(){
		if(mapNameComm.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{
			for(String key : mapNameComm.keySet()){
			    System.out.println("\nRestaurante: "+key+"\nComment: "+mapNameComm.get(key).get(1));
			}
        }
	}
	
	public void displayMapCommByIndex(String key){
		if(mapNameComm.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{
			if(mapNameComm.containsKey(key)){
				System.out.println("\nRestaurante: "+key+"\nComment: "+mapNameComm.get(key).get(1));
			}
			else{
				System.out.println("ERROR: There aren't restaurants with this name!");
			}
        }
	}
	
	public void startConnectionDB(){
		db.MySQLConnection("root", "", "lookneat_db");
	}
	
	public void closeConnectionDB(){
		db.closeConnection();
	}
	
	public void createTableNames(String nTable){
		db.createTableNames(nTable);
	}
	
	public void createTableComm(String nTable){
		db.createTableComm(nTable);
	}
	
	public void insertNameURL(String nTable){
		for(String key : mapNameURL.keySet()){
			db.insertDataTableNames(nTable, key, mapNameURL.get(key));
		}
	}
	
	public void insertComm(String nTable, String nTable2, ArrayList<String> xPath){
		ResultSet rSet = db.getNames(nTable);
		int i = 1;
		
		try{
			while(rSet.next()){
				if(i < 5){ //Sólo los 5 primeros restaurantes, de manera provisional
					System.out.println("Entrando en datos del restaurante "+i);
			    if(mapNameURL.containsKey(rSet.getString("nRest"))){
			    	String urlRest = mapNameURL.get(rSet.getString("nRest"));
			    	
			    	downloadComm(rSet.getString("nRest"), urlRest, xPath);
			    	ArrayList<String> arrayComm = getArrayComm(); //Cogemos solo el array de comentarios actual
			    	
			    	//String idStr = Integer.toString(i);
			    	
			    	for(String comm : arrayComm){
			    		//System.out.println("Entrando en comentarios del restaurante "+i);
			    		db.insertDataTableComm(nTable2, i, comm);
			    		//System.out.println("Comentario introducido");
					}
				}
			    
			    i++;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getMapNameURL(){
		return this.mapNameURL;
	}
	
	public ArrayList<String> getArrayComm(){
		return this.arrayComm;
	}
	
	public static void main(String[] args) throws Exception{
		ArrayList<String> xPath1 = new ArrayList<>();
		ArrayList<String> xPath2 = new ArrayList<>();
		xPath1.add("//a[@class='property_title']");
		xPath2.add("//div/div/h3/a/@href");
		String URL = "https://www.tripadvisor.es/Restaurants-g187432-[[oaxx]]-Cadiz_Costa_de_la_Luz_Andalucia.html";
		//String URL = "https://www.tripadvisor.es/Restaurants-g187433-[[oaxx]]-Chiclana_de_la_Frontera_Costa_de_la_Luz_Andalucia.html";
		String nTable = "test_table";
		String nTable2 = "comm_ta";
		int n;
		
		DisplayInfo dI = new DisplayInfo(xPath1, xPath2, URL);
		
		ArrayList<String> xPath3 = new ArrayList<>();
		xPath3.add("//div[3]/div/div/a[6]/@data-page-number");
		n = 1;
		
		String numPagesStr = dI.downloadNumPages(xPath3, n);
		int numPages = Integer.parseInt(numPagesStr);
		
		System.out.println("Number of pages on TripAdvisor: "+numPages);
		
		// *********************************** RESTAURANTS ***********************************
		
		//System.out.println("\nRESTAURANTS");
		//System.out.println("-----------");
		
		int URLNumPatt = 0;
		String URLPatt = "oa";
		String patt = "[[oaxx]]";
		
		for(int i = 0; i < numPages; i++){
			String newPatt = URLPatt+URLNumPatt;
			String URLgen = URL.replace(patt, newPatt);
			
			URLNumPatt = URLNumPatt + 30;
			URL = URLgen;
			patt = newPatt;
			
			dI.downloadNameURL(URL);
			//System.out.println("\n"+URL);
			//dI.displayMapNameURL();
		}
		
		System.out.println("Number of restaurants: "+dI.getMapNameURL().size());
		
		dI.displayMapNameURL();
		
		// *********************************** DATABASE ***********************************
		
		/*System.out.println("\nDATABASE");
		System.out.println("--------");
		
		ArrayList<String> xPath3 = new ArrayList<>();
		xPath3.add("//div[2]/div/div/div[3]/p");
		
		dI.startConnectionDB();
		
		//dI.createTableNames(nTable);
		//dI.insertNameURL(nTable);
		dI.insertComm(nTable, nTable2, xPath3);
		
		dI.closeConnectionDB();*/
		
		
		/*mapNameURL = dI.downloadNameURL();		
		dI.displayMapNameURL();*/
		
		// *********************************** COMMENTS ***********************************
		
		/*System.out.println("\nCOMMENTS");
		System.out.println("--------");
		
		ArrayList<String> xPath3 = new ArrayList<>();
		xPath3.add("//div[2]/div/div/div[3]/p");
		
		Map<String, String> mapNameURL = new HashMap<String, String>();
		mapNameURL = dI.getMapNameURL();
		
		/*for(String key : mapNameURL.keySet()){
			String urlRest = (String)mapNameURL.get(key);
			
		    dI.downloadComm(key, urlRest, xPath3);
		}*/
		
		/*String key = "BarraSie7e";
		
		if(mapNameURL.containsKey(key)){
			System.out.println("The restaurant "+key+" exists!");
			
			String urlRest = (String)mapNameURL.get(key);
			
		    dI.downloadComm(key, urlRest, xPath3);
		}
		
		dI.displayMapCommByIndex(key);*/
	}
}