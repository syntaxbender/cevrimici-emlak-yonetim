package view;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import model.*;
import view.core.CustomPasswordField;
import view.core.CustomTextField;
import view.core.MainFrame;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import controller.*;

public class GirisEkrani {

	public GirisEkrani() throws IOException{
		
		MainFrame mainFrame = new MainFrame(350, 600,true);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Çevrimiçi Emlak Yönetim Sistemi");
		JLabel usernameIcon = new JLabel();
		JLabel passwordIcon = new JLabel();
		CustomTextField usernameField = new CustomTextField("Kullanıcı adı",mainPanel);
		CustomPasswordField passwordField = new CustomPasswordField("Şifre", mainPanel);
		JButton registerButton = new JButton("Kayıt");
		JButton loginAsAnonymous = new JButton("Ziyaretçi Olarak Giriş Yap");
		JButton loginButton = new JButton("Giriş");
		JLabel leftHomeIcon = new JLabel();
		JLabel backgroundImage = new JLabel();
		mainFrame.addWindowListener(new WindowAdapter(){
	         public void windowOpened(WindowEvent windowEvent){
	        	 loginButton.requestFocus();
	         }        
	    });
		mainFrame.getContentPane().setBackground(Color.WHITE);
		mainFrame.getContentPane().setLayout(null);
		
		mainPanel.setBounds(0, 0, 600, 325);
		mainFrame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setBounds(80, 12, 440, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		mainPanel.add(headerLabel);
		
		usernameIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/username.png")));
		usernameIcon.setBounds(286, 113, 35, 35);
		mainPanel.add(usernameIcon);
		
		passwordIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon.setBounds(286, 161, 35, 35);
		mainPanel.add(passwordIcon);
		
		usernameField.setBounds(333, 116, 220, 30);

		mainPanel.add(usernameField);
		
		passwordField.setColumns(10);
		passwordField.setOpaque(false);
		passwordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		passwordField.setBounds(333, 164, 220, 30);
		mainPanel.add(passwordField);
		
		registerButton.setForeground(new Color(255, 255, 255));
		registerButton.setBackground(new Color(0, 0, 51));
		registerButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new KayitEkrani();
			}
		});
		registerButton.setBounds(286, 214, 123, 25);
		mainPanel.add(registerButton);

		loginAsAnonymous.setForeground(new Color(255, 255, 255));
		loginAsAnonymous.setBackground(new Color(0, 0, 51));
		loginAsAnonymous.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		loginAsAnonymous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		loginAsAnonymous.setBounds(286, 255, 267, 25); 
		mainPanel.add(loginAsAnonymous);
		
		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setBackground(new Color(0, 0, 51));
		loginButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Username = usernameField.getText();
				String Password = new String(passwordField.getPassword());
				try {
					new UsersController().Login(Username,Password);
					mainFrame.setVisible(false);
					mainFrame.dispose();
				}catch(Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e1.getMessage());
				}
			}
		});
		loginButton.setBounds(430, 214, 123, 25);
		mainPanel.add(loginButton);
		
		leftHomeIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/estate2.png")));
		leftHomeIcon.setBounds(12, 49, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		backgroundImage.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/bg_min.png")));
		backgroundImage.setBounds(0, 0, 600, 325);
		mainPanel.add(backgroundImage);

		mainFrame.setVisible(true);
		
	}
}
