package view;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class KayitEkrani {

	public boolean locker = false;
	
	public KayitEkrani() {

		MainFrame mainFrame = new MainFrame(600, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Çevrimiçi Emlak Yönetim Sistemi");
		JComboBox<ComboDBItem> provincesCombobox = new JComboBox<ComboDBItem>();
		JComboBox<ComboDBItem> districtsCombobox = new JComboBox<ComboDBItem>();
		JComboBox<ComboDBItem> neighborsCombobox = new JComboBox<ComboDBItem>();
		CustomTextField telephoneField = new CustomTextField("Telefon",mainPanel);
		CustomTextField emailField = new CustomTextField("E-mail",mainPanel);
		CustomTextField surnameField = new CustomTextField("Soyadı",mainPanel);
		CustomTextField nameField = new CustomTextField("Adı",mainPanel);
		CustomTextField usernameField = new CustomTextField("Kullanıcı Adı",mainPanel);
		CustomPasswordField passwordField = new CustomPasswordField("Şifre",mainPanel);
	
		CustomPasswordField repasswordField = new CustomPasswordField("Şifre Tekrar",mainPanel);
		JLabel provinceIcon = new JLabel("New label");
		provinceIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/map.png")));
		JButton registerButton = new JButton("Kayıt");
		JButton cancelButton = new JButton("İptal");
		JLabel neighborhoodIcon = new JLabel("New label");
		neighborhoodIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/map.png")));
		JLabel districtIcon = new JLabel("New label");
		districtIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/map.png")));
		JLabel emailIcon = new JLabel("New label");
		emailIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/email.png")));
		JLabel telephoneIcon = new JLabel("New label");
		telephoneIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/phone.png")));
		JLabel surnameIcon = new JLabel("New label");
		surnameIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/username.png")));
		JLabel nameIcon = new JLabel("New label");
		nameIcon.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/username.png")));
		JLabel usernameIcon = new JLabel("New label");
		JLabel passwordIcon = new JLabel("New label");
		JLabel leftHomeIcon = new JLabel();
		JLabel backgroundImage = new JLabel();
		mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowOpened(WindowEvent windowEvent){
	        	 registerButton.requestFocus();
	         }        
	    });
		mainFrame.getContentPane().setBackground(Color.WHITE);
		mainFrame.getContentPane().setLayout(null);
		
		mainPanel.setBounds(0, 0, 600, 591);

		mainPanel.setLayout(null);

		headerLabel.setForeground(Color.WHITE);
		headerLabel.setBounds(80, 18, 440, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		mainPanel.add(headerLabel);

		provincesCombobox.setBounds(333, 397, 220, 24);
		districtsCombobox.setBounds(333, 443, 220, 24);
		neighborsCombobox.setBounds(333, 489, 220, 24);
		
		provincesCombobox.addItem(new ComboDBItem(-1,"Lütfen ikamet ilinizi seçiniz."));
		districtsCombobox.addItem(new ComboDBItem(-1,"Lütfen ikamet ilçenizi seçiniz."));
		neighborsCombobox.addItem(new ComboDBItem(-1,"Lütfen ikamet mahallenizi/belde/köyünüzü seçiniz."));
		LocationsModel locationmodel = new LocationsModel();
		ResultSet Provinces = locationmodel.getProvinces();
		try {
			while(Provinces.next()) {
				provincesCombobox.addItem(new ComboDBItem(Provinces.getInt("ProvinceID") ,Provinces.getString("ProvinceName")));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		provincesCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				locker = true;
				ComboDBItem SelectedItem = (ComboDBItem) provincesCombobox.getSelectedItem();
				if(SelectedItem.ElementID == -1) return;
				try {
					ResultSet Districts = locationmodel.getDistricts(SelectedItem.ElementID);
					districtsCombobox.removeAllItems();
					neighborsCombobox.removeAllItems();
					districtsCombobox.addItem(new ComboDBItem(-1,"Lütfen ikamet ilçenizi seçiniz."));
					neighborsCombobox.addItem(new ComboDBItem(-1,"Lütfen ikamet mahallenizi/belde/köyünüzü seçiniz."));
					while(Districts.next()) {
						districtsCombobox.addItem(new ComboDBItem(Districts.getInt("DistrictID") ,Districts.getString("DistrictName")));
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				locker = false;
			}
		});
		districtsCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(locker == true) return;
				ComboDBItem SelectedItem = (ComboDBItem) districtsCombobox.getSelectedItem();
				if(SelectedItem.ElementID == -1) return;
				try {
					ResultSet Neighborhoods = locationmodel.getNeighborhoods(SelectedItem.ElementID);
					neighborsCombobox.removeAllItems();
					neighborsCombobox.addItem(new ComboDBItem(-1,"Lütfen ikamet mahallenizi/belde/köyünüzü seçiniz."));					
					while(Neighborhoods.next()) {
						neighborsCombobox.addItem(new ComboDBItem(Neighborhoods.getInt("NeighborhoodID") ,Neighborhoods.getString("NeighborhoodName")));
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				
			}
		});
		
		repasswordField.setOpaque(false);
		repasswordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		repasswordField.setBounds(333, 169, 220, 30);
		mainPanel.add(repasswordField);
		
		JLabel passwordIcon_1 = new JLabel("New label");
		passwordIcon_1.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon_1.setBounds(286, 166, 35, 35);
		mainPanel.add(passwordIcon_1);
		mainPanel.add(provincesCombobox);
		mainPanel.add(districtsCombobox);
		mainPanel.add(neighborsCombobox);
		
		provinceIcon.setBounds(286, 392, 35, 35);
		mainPanel.add(provinceIcon);
		
		registerButton.setForeground(Color.WHITE);
		registerButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		registerButton.setBackground(new Color(0, 0, 51));
		registerButton.setBounds(286, 537, 123, 25);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ComboDBItem provincesSelected = (ComboDBItem) provincesCombobox.getSelectedItem();
				ComboDBItem districtsSelected = (ComboDBItem) districtsCombobox.getSelectedItem();
				ComboDBItem neighborsSelected = (ComboDBItem) neighborsCombobox.getSelectedItem();
				UsersController UsersController = new UsersController();
				try {
					UsersController.CreateUser(usernameField.getText(),new String(passwordField.getPassword()),new String(repasswordField.getPassword()), telephoneField.getText(), emailField.getText(), nameField.getText(), surnameField.getText(), provincesSelected.ElementID, districtsSelected.ElementID, neighborsSelected.ElementID);
					JOptionPane.showMessageDialog(mainFrame, "Başarıyla kayıt oldunuz. Üyeliğiniz en kısa zamanda yetkili tarafından onaylanacaktır.");
					mainFrame.setVisible(false);
					mainFrame.dispose();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
			}
		});
		mainPanel.add(registerButton);
		
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		cancelButton.setBackground(new Color(0, 0, 51));
		cancelButton.setBounds(430, 537, 123, 25);
		mainPanel.add(cancelButton);
		
		neighborhoodIcon.setBounds(286, 484, 35, 35);
		mainPanel.add(neighborhoodIcon);
		
		districtIcon.setBounds(286, 438, 35, 35);
		mainPanel.add(districtIcon);
		
		telephoneField.setOpaque(false);
		telephoneField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		telephoneField.setBounds(333, 303, 220, 30);
		mainPanel.add(telephoneField);
		
		emailField.setOpaque(false);
		emailField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		emailField.setBounds(333, 348, 220, 30);
		mainPanel.add(emailField);
		
		emailIcon.setBounds(286, 345, 35, 35);
		mainPanel.add(emailIcon);
		
		telephoneIcon.setBounds(286, 300, 35, 35);
		mainPanel.add(telephoneIcon);
		
		surnameField.setOpaque(false);
		surnameField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		surnameField.setBounds(333, 258, 220, 30);
		mainPanel.add(surnameField);
		
		surnameIcon.setBounds(286, 255, 35, 35);
		mainPanel.add(surnameIcon);
		

		nameField.setOpaque(false);
		nameField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		nameField.setBounds(333, 213, 220, 30);
		mainPanel.add(nameField);

		nameIcon.setBounds(286, 210, 35, 35);
		mainPanel.add(nameIcon);
		
		usernameIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/username.png")));
		usernameIcon.setBounds(286, 78, 35, 35);
		mainPanel.add(usernameIcon);
		
		passwordIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon.setBounds(286, 122, 35, 35);
		mainPanel.add(passwordIcon);
		
		usernameField.setBounds(333, 81, 220, 30);
		usernameField.setOpaque(false);
		usernameField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		mainPanel.add(usernameField);
		
		passwordField.setOpaque(false);
		passwordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		passwordField.setBounds(333, 125, 220, 30);
		mainPanel.add(passwordField);
		
		leftHomeIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/estate2.png")));
		leftHomeIcon.setBounds(12, 125, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		backgroundImage.setIcon(new ImageIcon(KayitEkrani.class.getResource("/assets/images/bg_register.png")));
		backgroundImage.setBounds(0, 0, 600, 591);
		mainPanel.add(backgroundImage);
		
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.revalidate();
		mainFrame.repaint();
		mainFrame.setVisible(true);
	}
}
