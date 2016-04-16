package htmlparser;

import java.util.*;
import java.io.*;

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
		
		ArrayList<String> a = new ArrayList();
		
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
	}
}