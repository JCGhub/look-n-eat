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
		final int n;
		
		if(opt == "TripAdvisor"){
			n = 1;
		}
		else{
			if(opt == "11870"){
				n = 2;
			}
			else{
				n = 3;
			}
		}
		
		DisplayInfo dI = new DisplayInfo(n);
		
		JButton btnParse = new JButton("Parse");
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				dI.initializePortalParameters();
				dI.restPagination();
				
				JOptionPane.showMessageDialog(null, "In "+opt+" there are "+dI.getNumPagesRest()+" pages and "+dI.getNumRestaurants()+" restaurants!");	
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