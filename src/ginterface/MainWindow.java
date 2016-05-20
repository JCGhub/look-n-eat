package ginterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class MainWindow extends JFrame{

	private JPanel contentPane;
	public static JComboBox<String> cBPortal;

	public MainWindow(){
		setTitle("Look'n'Eat Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cBPortal = new JComboBox<String>();
		cBPortal.setModel(new DefaultComboBoxModel<String>(new String[] {"TripAdvisor", "11870"}));
		cBPortal.setSelectedIndex(0);
		cBPortal.setBounds(144, 50, 254, 20);
		contentPane.add(cBPortal);
		
		JButton btnParser = new JButton("Parser");
		btnParser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final ParserWindow pW = new ParserWindow();
				
				JOptionPane.showMessageDialog(null, "You've chosen "+cBPortal.getSelectedItem().toString()+" to parse!");
				
				pW.setVisible(true);
				dispose();
			}
		});
		btnParser.setBounds(110, 164, 89, 23);
		contentPane.add(btnParser);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(175, 208, 70, 23);
		contentPane.add(btnExit);
		
		JLabel lblPortal = new JLabel("Web portal:");
		lblPortal.setBounds(30, 53, 89, 14);
		contentPane.add(lblPortal);
		
		JButton btnChangeData = new JButton("Change Data");
		btnChangeData.setBounds(110, 130, 202, 23);
		contentPane.add(btnChangeData);
		
		JButton btnDatabase = new JButton("Database");
		btnDatabase.setBounds(223, 164, 89, 23);
		contentPane.add(btnDatabase);
	}
}