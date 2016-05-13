package htmlparser;

import java.util.*;

public class DisplayInfo{	
	
	Map<String, ArrayList<String>> mapRest;
	Map<String, String> mapNameRest;
	ArrayList<String> arrayComm = new ArrayList<String>();
	ArrayList<String> xPath1;
	ArrayList<String> xPath2;
	String URL;
	
	public DisplayInfo(Map<String, ArrayList<String>> mapRest, Map<String, String> mapNameRest, ArrayList<String> xPath1, ArrayList<String> xPath2, String URL){
		this.mapRest = mapRest;
		this.mapNameRest = mapNameRest;
		this.xPath1 = xPath1;
		this.xPath2 = xPath2;
		this.URL = URL;
	}
	
	public Map<String, String> downloadNameRest(){
		HTMLParser request = new HTMLParser(URL, xPath1, xPath2);
        mapNameRest = request.downloadRestAndURL();
        
        return mapNameRest;
    }	
	
	public void downloadCommRest(String URL, ArrayList<String> xPath3){
		HTMLParser request = new HTMLParser(URL, xPath3);
        arrayComm = request.download();
    }
	
	public void displayMapNameRest(){
		if(mapNameRest.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			for(String key : mapNameRest.keySet()){
			    //System.out.println("\nRestaurante: "+key+"URL: "+mapNameRest.get(key));
				System.out.println(key);
			}
		}
	}
	
	public void displayArrayComm(){
		if(arrayComm.isEmpty()){
			System.out.println("Vacio");
		}
		else{
            for(String values : arrayComm){
                System.out.println(values);
            }
        }
	}
	
	public Map<String, String> getMapNameRest(){
		return this.mapNameRest;
	}
	
	public Map<String, ArrayList<String>> getMap(){
		return this.mapRest;
	}	
	
	public static void main(String[] args) throws Exception {
		Map<String, ArrayList<String>> mapRest = new HashMap<String, ArrayList<String>>();
		Map<String, String> mapNameRest = new HashMap<String, String>();
		ArrayList<String> xPath1 = new ArrayList<>();
		ArrayList<String> xPath2 = new ArrayList<>();
		xPath1.add("//a[@class='property_title']");
		xPath2.add("//div/div/h3/a/@href");
		String URL = "https://www.tripadvisor.es/Restaurants-g187432-Cadiz_Costa_de_la_Luz_Andalucia.html";
		
		DisplayInfo dI = new DisplayInfo(mapRest, mapNameRest, xPath1, xPath2, URL);
		
		mapNameRest = dI.downloadNameRest();		
		dI.displayMapNameRest();
		
		ArrayList<String> xPath3 = new ArrayList<>();
		xPath3.add("//div[2]/div/div/div[3]/p");
		
		System.out.println("------------------------------------------------------------");
		
		String URL2 = (String)mapNameRest.get("Sopranis");		
		System.out.println(URL2);
		
		dI.downloadCommRest(URL2, xPath3);
		dI.displayArrayComm();
		
		
		/*ArrayList<String> xPaths = new ArrayList<>();
        xPaths.add("//a[@class='property_title']");
        HTMLParser request2 = new HTMLParser("https://www.tripadvisor.es/Restaurants-g187432-Cadiz_Costa_de_la_Luz_Andalucia.html", xPaths);
        ArrayList<String> results = request2.download();
        if (results != null) {
            for (String values : results) {
                System.out.println(values);
            }
        }*/
		
		/*Map<String, ArrayList<String>> nombreMap = new HashMap<String, ArrayList<String>>();
		nombreMap.size(); // Devuelve el numero de elementos del Map
		
		if(nombreMap.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			System.out.println("Tiene elementos");
		}
		
		ArrayList<String> a = new ArrayList<String>();
		
		a.add("La Iberica");
		a.add("Calle Cangrejo");
		
		nombreMap.put("Res1", a);
		
		if(nombreMap.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			System.out.println("Tiene elementos");
		}
		
		System.out.println(nombreMap.get("Res1").get(1));

		if(nombreMap.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			System.out.println("Tiene elementos");
		}*/
	}
}