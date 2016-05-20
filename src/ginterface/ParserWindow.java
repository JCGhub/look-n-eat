package ginterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import htmlparser.DisplayInfo;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ParserWindow extends JFrame{

	private JPanel contentPane;

	public ParserWindow(){
		setTitle("Look'n'Eat Parser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String opt = MainWindow.cBPortal.getSelectedItem().toString();
		ArrayList<String> xPath1 = new ArrayList<>();
		ArrayList<String> xPath2 = new ArrayList<>();
		ArrayList<String> xPath3 = new ArrayList<>();
		final String URL;
		final int n;
		
		if(opt == "TripAdvisor"){
			xPath1.add("//a[@class='property_title']");
			xPath2.add("//div/div/h3/a/@href");
			xPath3.add("//div[3]/div/div/a[6]/@data-page-number");
			n = 1;
			
			URL = "https://www.tripadvisor.es/Restaurants-g187432-[[oaxx]]-Cadiz_Costa_de_la_Luz_Andalucia.html";
		}
		else if(opt == "11870"){
			xPath1.add("//h2[@class='card__title']/a");
			xPath2.add("//h2[@class='card__title']/a/@href");
			xPath3.add("");
			n = 2;
			
			URL = "https://11870.com/k/restaurantes/es/es/cadiz?[[p=xx]]";
		}
		else{
			xPath1.add("");
			xPath2.add("");
			xPath3.add("");			
			URL = "";
			n = 3;
		}
		
		DisplayInfo dI = new DisplayInfo(xPath1, xPath2, URL);
		
		JButton btnParse = new JButton("Parse");
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(opt == "TripAdvisor"){
					String numPagesStr = dI.downloadNumPages(xPath3, n);
					int numPages = Integer.parseInt(numPagesStr);
					
					int URLNumPatt = 0;
					String URLorigin = URL;
					String URLPatt = "oa";
					String patt = "[[oaxx]]";
					
					for(int i = 0; i < numPages; i++){
						String newPatt = URLPatt+URLNumPatt;
						String URLgen = URLorigin.replace(patt, newPatt);
						
						URLNumPatt = URLNumPatt + 30;
						URLorigin = URLgen;
						patt = newPatt;
						
						dI.downloadNameURL(URLorigin);
					}
					
					JOptionPane.showMessageDialog(null, "TripAdvisor has "+numPages+" pages and "+dI.getMapNameURL().size()+" restaurants.");
				}
				else{
					int numPages = 6;
					
					int URLNumPatt = 1;
					String URLorigin = URL;
					String URLPatt = "p=";
					String patt = "[[p=xx]]";
					
					for(int i = 0; i < numPages; i++){
						String newPatt = URLPatt+URLNumPatt;
						String URLgen = URLorigin.replace(patt, newPatt);
						
						URLNumPatt++;
						URLorigin = URLgen;
						patt = newPatt;
						
						//System.out.println("Evaluando URL: "+URLorigin);
						
						dI.downloadNameURL(URLorigin);
					}
					
					JOptionPane.showMessageDialog(null, "11870 has "+numPages+" pages and "+dI.getMapNameURL().size()+" restaurants.");
				}
			}
		});
		btnParse.setBounds(175, 58, 89, 23);
		contentPane.add(btnParse);
		
		JButton btnRestaurants = new JButton("Restaurants");
		btnRestaurants.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dI.displayMapNameURL();
			}
		});
		btnRestaurants.setBounds(148, 126, 134, 23);
		contentPane.add(btnRestaurants);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(175, 194, 89, 23);
		contentPane.add(btnExit);
	}
}