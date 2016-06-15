package htmlparser;

import java.util.*;

import javax.swing.JOptionPane;
import java.util.regex.*;

public class DataFixer{
	private String namePortal;
	private String comArt[] = {"el","la","los","las","lo","de","del","the"};
	
	public DataFixer(String namePortal){
		this.namePortal = namePortal;
	}
	
	public String fixName(String str){
		String str1 = "", str2, res = "";
		Pattern patt;
		Matcher matStr;
		boolean aux = false;
		
		switch(namePortal){
		case "TripAdvisor":
			res = str.replace(" ", "/");
			
			break;
		case "Yelp":
			str1 = str.toLowerCase();			
			str2 = str1.replace(" ", "/");
			
			String[] arrayStr = str2.split("/");
			
			for(int i = 0; i < arrayStr.length; i++){
				for(int j = 0; j < comArt.length; j++){
					patt = Pattern.compile("^"+comArt[j]+"$");
					
					matStr = patt.matcher(arrayStr[i]);
					
					if(matStr.find()){
						aux = true;
					}
				}
				
				if(!aux){
					res = res+"/"+arrayStr[i];
				}
				
				aux = false;
			}
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixNumPages(String str){
		String str1, str2, res = "";
		
		switch(namePortal){
		case "TripAdvisor":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		case "Yelp":
			str1 = str.replace(" ", "");
			str2 = str1.replace("Página1de", "");
			res = str2.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixVal(String str){
		String str1, str2, str3, res = "";
		
		switch(namePortal){
		case "TripAdvisor":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		case "Yelp":
			str1 = str.replace(" ", "");
			str2 = str1.replace("Puntuaciónde", "");
			str3 = str2.replace("estrellas", "");
			res = str3.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixAddress(String str){
		return str;
	}
	
	public String fixTel(String str){
		String str1, res = "";
		
		switch(namePortal){
		case "TripAdvisor":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		case "Yelp":
			str1 = str.replace(" ", "");
			res = str1.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public String fixCoord(String str){
		String str1 = "", str2, str3, res = "";
		Pattern patt;
		Matcher matStr;
		
		switch(namePortal){
		case "TripAdvisor":
			res = str.replace("\n", "");
			
			break;
		case "Yelp":
			patt = Pattern.compile("[\\W]+[0-9]{1,2}\\W[0-9]{6}%2C[\\W][0-9]{1,2}\\W[0-9]{6}");
			
			matStr = patt.matcher(str);

			while (matStr.find()){
				str1 = matStr.group();
			}
				
			str2 = str1.replace("=", "");
			str3 = str2.replace("%2C", ", ");
			res = str3.replace("\n", "");
			
			break;
		default:
			break;
		}
		
		return res;
	}
}