package htmlparser;

import java.util.*;

public class DisplayInfo{	
	
	Map<String, ArrayList<String>> mapRest;
	Map<String, String> mapNameRest;
	ArrayList<String> xPaths1;
	ArrayList<String> xPaths2;
	String route;
	String route2;
	String URL;
	
	public DisplayInfo(Map<String, ArrayList<String>> mapRest, Map<String, String> mapNameRest, ArrayList<String> xPaths1, ArrayList<String> xPaths2, String route, String route2, String URL){
		this.mapRest = mapRest;
		this.mapNameRest = mapNameRest;
		this.xPaths1 = xPaths1;
		this.xPaths2 = xPaths2;
		this.route = route;
		this.route2 = route2;
		this.URL = URL;
	}
	
	public Map<String, ArrayList<String>> getMap(){
		return this.mapRest;
	}
	
	public void downloadNameRest(){
		xPaths1.add(route);
		xPaths2.add(route2);

        HTMLParser request = new HTMLParser(URL, xPaths1, xPaths2);
        mapNameRest = request.downloadRestAndURL();
    }
	
	public void displayMapNameRest(){
		if(mapNameRest.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			for(String key : mapNameRest.keySet()){
			    System.out.println("\nRestaurante: "+key+"URL: "+mapNameRest.get(key));
			}
		}
	}
	
	/*public Map<String, ArrayList<String>> downloadInfoRest(){
		
		
		return mapRest;
    }*/
	
	public void displayMap(){
		if(mapRest.isEmpty()){
			System.out.println("Vacio");
		}
		else{
			for(String key : mapRest.keySet()){
			    System.out.println("Restaurante: "+key);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Map<String, ArrayList<String>> mapRest = new HashMap<String, ArrayList<String>>();
		Map<String, String> mapNameRest = new HashMap<String, String>();
		ArrayList<String> xPaths1 = new ArrayList<>();
		ArrayList<String> xPaths2 = new ArrayList<>();
		String route = "//a[@class='property_title']";
		String route2 = "//div/div/h3/a/@href";
		String URL = "https://www.tripadvisor.es/Restaurants-g187432-Cadiz_Costa_de_la_Luz_Andalucia.html";
		
		DisplayInfo dI = new DisplayInfo(mapRest, mapNameRest, xPaths1, xPaths2, route, route2, URL);
		
		dI.downloadNameRest();		
		dI.displayMapNameRest();
		
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