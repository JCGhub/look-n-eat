package htmlparser;

import java.util.*;

public class DisplayInfo{	
	
	Map<String, ArrayList<String>> mapRest;
	ArrayList<String> xPaths;
	String route;
	String URL;
	
	public DisplayInfo(Map<String, ArrayList<String>> mapRest, ArrayList<String> xPaths, String route, String URL){
		this.mapRest = mapRest;
		this.xPaths = xPaths;
		this.route = route;
		this.URL = URL;
	}
	
	public Map<String, ArrayList<String>> getMap(){
		return this.mapRest;
	}
	
	public Map<String, ArrayList<String>> downloadNameRest(){
		xPaths.add(route);

        HTMLParser request = new HTMLParser(URL, xPaths);
        ArrayList<String> results = request.download();
        if(results != null){
            for(String values:results){
                System.out.println(values);
            	
            	mapRest.put(values, null);
            }
        }
		
		return mapRest;
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
		ArrayList<String> xPaths = new ArrayList<>();
		String route = "//a[@class='property_title']";
		String URL = "https://www.tripadvisor.es/Restaurants-g187432-Cadiz_Costa_de_la_Luz_Andalucia.html";
		
		DisplayInfo dI = new DisplayInfo(mapRest, xPaths, route, URL);
		
		mapRest = dI.downloadNameRest();
		
		dI.displayMap();
		
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