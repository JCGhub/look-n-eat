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
	public String mainURL, table_names, table_comm, namePortal;
	int nPortal, numPagesRest;
	
	public DisplayInfo(int nPortal){
		this.nPortal = nPortal;
	}
	
	public void initializePortalParameters(){
		switch(nPortal){
		case 1:
			namePortal = "TripAdvisor";
			xPathName.add("//a[@class='property_title']");
			xPathURL.add("//div/div/h3/a/@href");
			xPathPages.add("//div[3]/div/div/a[6]/@data-page-number");
			xPathComm.add("//div[2]/div/div/div[3]/p");
			mainURL = "https://www.tripadvisor.es/Restaurants-g187432-[[oaxx]]-Cadiz_Costa_de_la_Luz_Andalucia.html";
			table_names = "rest_ta";
			table_comm = "comm_ta";
			
			break;
		case 2:
			namePortal = "11870";
			xPathName.add("//h2[@class='card__title']/a");
			xPathURL.add("//h2[@class='card__title']/a/@href");
			xPathPages.add("");
			mainURL = "https://11870.com/k/restaurantes/es/es/cadiz?[[p=xx]]";
			table_names = "rest_11";
			table_comm = "comm_11";
			
			break;
		case 3:
			namePortal = "Yelp!";
			xPathName.add("//a[@class='biz-name js-analytics-click']/span");
			xPathURL.add("//a[@class='biz-name js-analytics-click']/@href");
			xPathPages.add("//div[@class='page-of-pages arrange_unit arrange_unit--fill']");
			xPathComm.add("//div[@class='review-content']/p");
			mainURL = "https://www.yelp.es/search?cflt=restaurants&l=p%3AES-CA%3AC%C3%A1diz%3A%3A&find_loc=C%C3%A1diz%2C+Spain&[[start=xx]]";		
			table_names = "rest_ye";
			table_comm = "comm_ye";
			
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
			downloadNumPagesRest(xPathPages);
			
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
			/*int pags = 7;
			
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
			}*/
			
			break;
		case 3:
			downloadNumPagesRest(xPathPages);
			
			URLNumPatt = 0;
			URLorigin = mainURL;
			URLPatt = "start=";
			patt = "[[start=xx]]";
			
			for(int i = 0; i < numPagesRest; i++){
				String newPatt = URLPatt+URLNumPatt;
				String URLgen = URLorigin.replace(patt, newPatt);
				
				URLNumPatt = URLNumPatt + 10;
				URLorigin = URLgen;
				patt = newPatt;
				
				downloadNameURL(URLorigin);
			}

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
        	}		    
        }
    }	
	
	public void downloadComm(String key, String URL){		
		HTMLParser hP = new HTMLParser(URL, xPathComm);
        arrayComm = hP.downloadAsArray();
           
        mapNameComm.put(key, arrayComm);
    }
	
	public void downloadNumPagesRest(ArrayList<String> xPathPages){
		String numPagesStr = "";		
		HTMLParser hP = new HTMLParser(mainURL, xPathPages);
		
		switch(nPortal){
		case 1:
			System.out.println("Counting pages of TripAdvisor...");
			
			numPagesStr = hP.downloadAsString();			
			numPagesRest = Integer.parseInt(numPagesStr);
			
			break;
		case 3:
			System.out.println("Counting pages of Yelp...");
			
			numPagesStr = hP.downloadAsString();
			String numPagesStrAux = numPagesStr.replace(" ", "");
			numPagesStr = numPagesStrAux.replace("P&aacute;gina1de", "");
			numPagesStrAux = numPagesStr.replace("\n", "");
			
			numPagesRest = Integer.parseInt(numPagesStrAux);
			
			break;
		default:
			break;
		}
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
	
	public void insertNameURL(ConnectDB db){
		for(String key : mapNameURL.keySet()){
			db.insertDataTableNames(table_names, key, mapNameURL.get(key));
		}
	}
	
	public void insertComm(ConnectDB db){
		ResultSet rSet = db.getNames(table_names);
		int i = 1;
		
		try{
			while(rSet.next()){
				if(i < 5){ //Sólo los 5 primeros restaurantes, de manera provisional
					//System.out.println("Entrando en datos del restaurante "+i);
					
				    if(mapNameURL.containsKey(rSet.getString("nRest"))){
				    	String urlRest = mapNameURL.get(rSet.getString("nRest"));
				    	
				    	if(nPortal == 1){
				    		urlRest = "https://www.tripadvisor.es" + urlRest;
				    		System.out.println(urlRest);
				    	}
				    	if(nPortal == 3){
				    		urlRest = "https://www.yelp.es" + urlRest;
				    		System.out.println(urlRest);
				    	}
				    	
				    	downloadComm(rSet.getString("nRest"), urlRest);
				    	ArrayList<String> arrayComm = getArrayComm(); //Cogemos solo el array de comentarios actual
				    	
				    	for(String comm : arrayComm){
				    		//System.out.println("Entrando en comentarios del restaurante "+i);
				    		db.insertDataTableComm(table_comm, i, comm);
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
}