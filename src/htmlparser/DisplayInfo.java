package htmlparser;

import java.util.*;

public class DisplayInfo{
	public static void main(String[] args) throws Exception {
		Map<String, ArrayList<String>> nombreMap = new HashMap<String, ArrayList<String>>();
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
		}
		
		//----------------------------------------
		
		ArrayList<String> xPaths = new ArrayList<>();
        xPaths.add("//a[@class='property_title']");
        HTMLParser request2 = new HTMLParser("https://www.tripadvisor.es/Restaurants-g2355755-Province_of_Cadiz_Andalucia.html", xPaths);
        ArrayList<String> results = request2.download();
        if (results != null) {
            for (String values : results) {
                System.out.println(values);
            }
        }
	}
}