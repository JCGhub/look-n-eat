package htmlparser;

import database.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

public class DisplayInfo{	
	
	Map<String, String> mapNameURL = new HashMap<String, String>();
	Map<String, String> mapNameURL2 = new HashMap<String, String>();
	Map<String, ArrayList<String>> mapNameComm = new HashMap<String, ArrayList<String>>();
	ArrayList<String> arrayComm = new ArrayList<String>();
	ArrayList<String> arrayInfo = new ArrayList<String>();
	ArrayList<Integer> arrayVal = new ArrayList<Integer>();
	ArrayList<String> xPathName = new ArrayList<String>();
	ArrayList<String> xPathURL = new ArrayList<String>();
	ArrayList<String> xPathPages = new ArrayList<String>();
	ArrayList<String> xPathComm = new ArrayList<String>();
	String mainURL;
	ConnectDB db = new ConnectDB();
	int nPortal, numPagesRest;
	
	public DisplayInfo(int nPortal){
		this.nPortal = nPortal;
	}
	
	public void initializePortalParameters(){
		switch(nPortal){
		case 1:
			xPathName.add("//a[@class='property_title']");
			xPathURL.add("//div/div/h3/a/@href");
			xPathPages.add("//div[3]/div/div/a[6]/@data-page-number");			
			mainURL = "https://www.tripadvisor.es/Restaurants-g187432-[[oaxx]]-Cadiz_Costa_de_la_Luz_Andalucia.html";
			break;
		case 2:
			xPathName.add("//h2[@class='card__title']/a");
			xPathURL.add("//h2[@class='card__title']/a/@href");
			xPathPages.add("");			
			mainURL = "https://11870.com/k/restaurantes/es/es/cadiz?[[p=xx]]";		
			break;
		case 3:
			xPathName.add("//a[@class='biz-name js-analytics-click']/span");
			xPathURL.add("//a[@class='biz-name js-analytics-click']/@href");
			xPathPages.add("");			
			mainURL = "https://www.yelp.es/search?cflt=restaurants&find_loc=C%C3%A1diz%2C+Spain";		
			break;
		default:
			JOptionPane.showMessageDialog(null, "You has not chosen any restaurant!");			
			break;
		}
	}
	
	public void restPagination(){
		int URLNumPatt;
		String URLorigin, URLPatt, patt;
		
		switch(nPortal){
		case 1:
			downloadNumPagesRest(xPathPages, nPortal);
			
			URLNumPatt = 0;
			URLorigin = mainURL;
			URLPatt = "oa";
			patt = "[[oaxx]]";
			
			for(int i = 0; i < numPagesRest; i++){
				String newPatt = URLPatt+URLNumPatt;
				String URLgen = URLorigin.replace(patt, newPatt);
				
				URLNumPatt = URLNumPatt + 30;
				URLorigin = URLgen;
				patt = newPatt;
				
				downloadNameURL(URLorigin);
			}
			
			break;
		case 2:
			int pags = 6;
			
			URLNumPatt = 1;
			URLorigin = mainURL;
			URLPatt = "p=";
			patt = "[[p=xx]]";
			
			for(int i = 0; i < pags; i++){
				String newPatt = URLPatt+URLNumPatt;
				String URLgen = URLorigin.replace(patt, newPatt);
				
				URLNumPatt++;
				URLorigin = URLgen;
				patt = newPatt;
				
				//System.out.println("Evaluando URL: "+URLorigin);
				
				downloadNameURL(URLorigin);
			}
			
			break;
		case 3:

			break;
		default:
			JOptionPane.showMessageDialog(null, "You has not chosen any restaurant!");			
			break;
		}
	}
	
	public void downloadNameURL(String URL){
		HTMLParser hP = new HTMLParser(URL, xPathName, xPathURL);
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
	
	public void downloadComm(String key, String URL, ArrayList<String> xPathComm){
		HTMLParser hP = new HTMLParser(URL, xPathComm);
        arrayComm = hP.downloadAsArray();
        
        // Seguramente, tras modificar HTMLParser para que nos devuelva una URL genérica,
        // debamos añadir a la URL de cada restaurante el inicio de la ruta como hicimos
        // al principio con TripAdvisor.
        
        mapNameComm.put(key, arrayComm);
    }
	
	public void downloadNumPagesRest(ArrayList<String> xPathPages, int n){
		String numPagesStr = "";
		
		if(n == 1){
			System.out.println("Counting pages of TripAdvisor...");
			
			HTMLParser hP = new HTMLParser(mainURL, xPathPages);
			numPagesStr = hP.downloadAsString();
		}
		
		numPagesRest = Integer.parseInt(numPagesStr);
	}
	
	public void displayMapNameURL(){
		if(mapNameURL.isEmpty()){
			System.out.println("WARNING: The map is empty!");
		}
		else{
			int n = 1;
			
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
	
	public void insertComm(String nTable, String nTable2, ArrayList<String> xPathComm){
		ResultSet rSet = db.getNames(nTable);
		int i = 1;
		
		try{
			while(rSet.next()){
				if(i < 5){ //Sólo los 5 primeros restaurantes, de manera provisional
					System.out.println("Entrando en datos del restaurante "+i);
			    if(mapNameURL.containsKey(rSet.getString("nRest"))){
			    	String urlRest = mapNameURL.get(rSet.getString("nRest"));
			    	
			    	downloadComm(rSet.getString("nRest"), urlRest, xPathComm);
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
		return mapNameURL;
	}
	
	public ArrayList<String> getArrayComm(){
		return arrayComm;
	}
	
	public int getNumPagesRest(){
		return numPagesRest;
	}
	
	public int getNumRestaurants(){
		return getMapNameURL().size();
	}
	
	public static void main(String[] args) throws Exception{
		String opt = "TripAdvisor";
		int nPortal;
		
		if(opt == "TripAdvisor"){
			nPortal = 1;
		}
		else{
			if(opt == "11870"){
				nPortal = 2;
			}
			else{
				nPortal = 3;
			}
		}
		
		DisplayInfo dI = new DisplayInfo(nPortal);
		
		// *********************************** RESTAURANTS ***********************************
		
		System.out.println("\nRESTAURANTS");
		System.out.println("-----------");
		
		dI.initializePortalParameters();
		dI.restPagination();
		
		JOptionPane.showMessageDialog(null, "In "+opt+" there are "+dI.getNumPagesRest()+" pages and "+dI.getNumRestaurants()+" restaurants!");
		
		dI.displayMapNameURL();
		
		// *********************************** DATABASE ***********************************
		
		/*System.out.println("\nDATABASE");
		System.out.println("--------");
		
		String nTable = "test_table";
		String nTable2 = "comm_ta";
		
		ArrayList<String> xPathComm = new ArrayList<>();
		xPathComm.add("//div[2]/div/div/div[3]/p");
		
		dI.startConnectionDB();
		
		//dI.createTableNames(nTable);
		//dI.insertNameURL(nTable);
		dI.insertComm(nTable, nTable2, xPathComm);
		
		dI.closeConnectionDB();*/
		
		
		/*mapNameURL = dI.downloadNameURL();		
		dI.displayMapNameURL();*/
		
		// *********************************** COMMENTS ***********************************
		
		/*System.out.println("\nCOMMENTS");
		System.out.println("--------");
		
		ArrayList<String> xPathComm = new ArrayList<>();
		xPathComm.add("//div[2]/div/div/div[3]/p");
		
		Map<String, String> mapNameURL = new HashMap<String, String>();
		mapNameURL = dI.getMapNameURL();
		
		for(String key : mapNameURL.keySet()){
			String urlRest = (String)mapNameURL.get(key);
			
		    dI.downloadComm(key, urlRest, xPathComm);
		}*/
	}
}