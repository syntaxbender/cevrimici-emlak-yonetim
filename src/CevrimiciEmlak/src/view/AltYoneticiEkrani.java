package view;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;

import controller.EstatesController;
import controller.LicensesController;
import controller.UsersController;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.awt.event.ActionEvent;
import model.*;
import view.core.ButtonColumn;
import view.core.CustomPasswordField;
import view.core.CustomTextArea;
import view.core.CustomTextField;
import view.core.MainFrame;

public class AltYoneticiEkrani {
	public boolean locker;
	private MainFrame mainFrame;
	public JTabbedPane kullaniciIslemleri;
	public JTabbedPane emlakIslemleri;
	public JTabbedPane lisansIslemleri;
	public UsersModel UserSessionData;
	public UsersModel UserModel = new UsersModel();
	public LocationsModel LocationModel = new LocationsModel();
	public LicensesModel LicensesModel = new LicensesModel();
	public EstatesModel EstateModel = new EstatesModel();
	public String Zone;
	public int ZoneID;
	public AltYoneticiEkrani(UsersModel UserSessionData, String Zone, int ZoneID){
		this.Zone = Zone;
		this.ZoneID = ZoneID;
		this.UserSessionData = UserSessionData;
		mainFrame = new MainFrame(600, 900,true);
		mainFrame.getContentPane().setBackground(Color.WHITE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(false);
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu mnIlemler = new JMenu("İşlemler");
		menuBar.add(mnIlemler);
		
		JMenuItem mntmTest1 = new JMenuItem("Kullanıcı İşlemleri");
		JMenuItem mntmTest2 = new JMenuItem("Emlak İşlemleri");
		JMenuItem mntmTest3 = new JMenuItem("Lisans işlemleri");
		
		mnIlemler.add(mntmTest1);
		mnIlemler.add(mntmTest2);
		mnIlemler.add(mntmTest3);
		
		mntmTest1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HideAll();
				kullaniciIslemleri.setVisible(true);
			}
		});
		mntmTest2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HideAll();
				emlakIslemleri.setVisible(true);
			}
		});

		mntmTest3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HideAll();
				lisansIslemleri.setVisible(true);
			}
		});
		LisansIslemleriConstructor();
		KullaniciIslemleriConstructor();
		EmlakIslemleriConstructor();
		mainFrame.getContentPane().add(kullaniciIslemleri);
		mainFrame.getContentPane().add(emlakIslemleri);
		mainFrame.getContentPane().add(lisansIslemleri);
		HideAll();
		mainFrame.setVisible(true);		
		kullaniciIslemleri.setVisible(true);
	}
	public void HideAll() {
		kullaniciIslemleri.setVisible(false);
		emlakIslemleri.setVisible(false);
		lisansIslemleri.setVisible(false);
	}
	public void refreshKullaniciIslemleri() {
		int kullaniciTabsIndex = kullaniciIslemleri.getSelectedIndex();
		KullaniciIslemleriConstructor();
		kullaniciIslemleri.setSelectedIndex(kullaniciTabsIndex);
	}
	public void refreshEmlakIslemleri() {
		int emlakIslemleriTabsIndex = emlakIslemleri.getSelectedIndex();
		EmlakIslemleriConstructor();
		emlakIslemleri.setSelectedIndex(emlakIslemleriTabsIndex);
	}
	public void refreshLisansIslemleri() {
		int lisansTabsIndex = lisansIslemleri.getSelectedIndex();
		LisansIslemleriConstructor();
		lisansIslemleri.setSelectedIndex(lisansTabsIndex);
	}
	public void KullaniciIslemleriConstructor(){
		if(kullaniciIslemleri != null) {
			kullaniciIslemleri.removeAll(); 
		}else {
			kullaniciIslemleri = new JTabbedPane(JTabbedPane.TOP);
			kullaniciIslemleri.setBounds(0, 0, 900, 555);
		}
		
		JPanel normalKullanicilar=NormalKullanicilar();

		JPanel yetkiliKullanicilar=YetkiliKullanicilar();
		JPanel onaysizKullanicilar = OnaysizKullanicilar();
		
		kullaniciIslemleri.addTab("Tüm Kullanıcılar", null, normalKullanicilar, null);
		kullaniciIslemleri.addTab("Yetkili Kullanıcılar", null, yetkiliKullanicilar, null);
		kullaniciIslemleri.addTab("Kullanıcı Onaylama", null, onaysizKullanicilar, null);
	}
	public void EmlakIslemleriConstructor(){
		if(emlakIslemleri != null) {
			emlakIslemleri.removeAll(); 
		}else {
			emlakIslemleri = new JTabbedPane(JTabbedPane.TOP);
			emlakIslemleri.setBounds(0, 0, 900, 555);
		}
		
		JPanel panel1 = tumEmlaklar();
		emlakIslemleri.addTab("Tüm Emlaklar", null, panel1, null);
		JPanel panel2 = onaysizEmlaklar();
		emlakIslemleri.addTab("Onaysız Emlaklar", null, panel2, null);


	}
	public void LisansIslemleriConstructor(){
		if(lisansIslemleri != null) {
			lisansIslemleri.removeAll(); 
		}else {
			lisansIslemleri = new JTabbedPane(JTabbedPane.TOP);
			lisansIslemleri.setBounds(0, 0, 900, 555);
		}
		
		JPanel sistemdeTanimliLisanslar = TumLisanslar();
		lisansIslemleri.addTab("Sistemde Tanımlı Lisanslar", null, sistemdeTanimliLisanslar, null);
		JPanel yeniLisansEkleme = LisansEkleme();
		lisansIslemleri.addTab("Yeni Lisans Ekleme", null, yeniLisansEkleme, null);
		
	}
	public JPanel YetkiliKullanicilar() {
		JPanel yetkiliKullanicilar= new JPanel();
		int buttonColumnID = 6;
		JScrollPane yetkiliKullanicilarScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == buttonColumnID;
			}
		};
		JTable yetkiliKullanicilarTable = new JTable( model );
		String[] columnNames = {"Kullanıcı Adı", "İsim", "Soyisim","Mail","Telefon", "Onay Durumu", "Düzenle"};
		yetkiliKullanicilar.add(yetkiliKullanicilarScrollPane);
		yetkiliKullanicilar.setLayout(null);
		yetkiliKullanicilar.setBounds(0, 0, 900, 555);
		yetkiliKullanicilarScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet Users = UserModel.getUsersByRoleAndZone(3,Zone,ZoneID);
		try {
			while(Users.next()){
				Object[] UserData = {Users.getString("Username"), Users.getString("Name"), Users.getString("Surname"), Users.getString("Email"), Users.getString("Phone"), Users.getString("VerifyStatusText"), new ComboDBItem(Users.getInt("UserID"),"Düzenle")};
			    model.addRow(UserData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Action tumKullanicilarButtonAction = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable yetkiliKullanicilarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)yetkiliKullanicilarTable.getModel()).getValueAt(modelRow, buttonColumnID);
		        ResultSet selectedUserData = UserModel.getUserByID(ElementID.ElementID);
		        try {
		        	if(selectedUserData.next()){
		        		KullaniciDuzenleme(selectedUserData.getInt("UserID"),selectedUserData.getString("Username"), selectedUserData.getString("Password") , selectedUserData.getString("Name"), selectedUserData.getString("Surname"), selectedUserData.getString("Phone"), selectedUserData.getString("Email"), selectedUserData.getInt("ProvinceID"), selectedUserData.getInt("DistrictID"), selectedUserData.getInt("NeighborhoodID"), selectedUserData.getInt("VerifyStatus"));
		        	}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		    }
		};
		yetkiliKullanicilarTable.setAutoCreateRowSorter(true);
		ButtonColumn buttonColumn = new ButtonColumn(yetkiliKullanicilarTable, tumKullanicilarButtonAction, buttonColumnID);
		buttonColumn.setMnemonic(KeyEvent.VK_D);

		yetkiliKullanicilarScrollPane.setViewportView(yetkiliKullanicilarTable);
		return yetkiliKullanicilar;
	}
	public JPanel NormalKullanicilar() {
		JPanel tumKullanicilar= new JPanel();
		int buttonColumnID = 6;
		JScrollPane tumKullanicilarScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == buttonColumnID;
			}
		};
		JTable tumKullanicilarTable = new JTable( model );
		String[] columnNames = {"Kullanıcı Adı", "İsim", "Soyisim","Mail","Telefon", "Onay Durumu", "Düzenle"};
		tumKullanicilar.add(tumKullanicilarScrollPane);
		tumKullanicilar.setLayout(null);
		tumKullanicilar.setBounds(0, 0, 900, 555);
		tumKullanicilarScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet Users = UserModel.getUsersByRoleAndZone(4,Zone,ZoneID);
		try {
			while(Users.next()){
				Object[] UserData = {Users.getString("Username"), Users.getString("Name"), Users.getString("Surname"), Users.getString("Email"), Users.getString("Phone"), Users.getString("VerifyStatusText"), new ComboDBItem(Users.getInt("UserID"),"Düzenle")};
			    model.addRow(UserData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Action tumKullanicilarButtonAction = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable tumKullanicilarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)tumKullanicilarTable.getModel()).getValueAt(modelRow, buttonColumnID);
		        ResultSet selectedUserData = UserModel.getUserByID(ElementID.ElementID);
		        try {
		        	if(selectedUserData.next()){
		        		KullaniciDuzenleme(selectedUserData.getInt("UserID"),selectedUserData.getString("Username"), selectedUserData.getString("Password") , selectedUserData.getString("Name"), selectedUserData.getString("Surname"), selectedUserData.getString("Phone"), selectedUserData.getString("Email"), selectedUserData.getInt("ProvinceID"), selectedUserData.getInt("DistrictID"), selectedUserData.getInt("NeighborhoodID"), selectedUserData.getInt("VerifyStatus"));
		        	}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		    }
		};
		tumKullanicilarTable.setAutoCreateRowSorter(true);
		ButtonColumn buttonColumn = new ButtonColumn(tumKullanicilarTable, tumKullanicilarButtonAction, buttonColumnID);
		buttonColumn.setMnemonic(KeyEvent.VK_D);

		tumKullanicilarScrollPane.setViewportView(tumKullanicilarTable);
		return tumKullanicilar;
	}
	public JPanel OnaysizKullanicilar() {
		JPanel onaysizKullanicilar= new JPanel();
		int buttonColumnID = 6;
		JScrollPane onaysizKullanicilarScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == buttonColumnID;
			}
		};
		JTable onaysizKullanicilarTable = new JTable( model );
		String[] columnNames = {"Kullanıcı Adı", "İsim", "Soyisim","Mail","Telefon", "Onay Durumu", "Onayla"};
		onaysizKullanicilar.add(onaysizKullanicilarScrollPane);
		onaysizKullanicilar.setLayout(null);
		onaysizKullanicilar.setBounds(0, 0, 900, 555);
		onaysizKullanicilarScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet Users = UserModel.getUsersByStatusAndZone(0,4,Zone,ZoneID); // 0 verifystatus, 4 roleid
		try {
			while(Users.next()) {
				Object[] UserData = {Users.getString("Username"), Users.getString("Name"), Users.getString("Surname"), Users.getString("Email"), Users.getString("Phone"), Users.getString("VerifyStatusText"), new ComboDBItem(Users.getInt("UserID"),"Onayla")};
			    model.addRow(UserData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Action onaysizKullanicilarButtonAction = new AbstractAction(){
		    public void actionPerformed(ActionEvent e){
		        JTable onaysizKullanicilarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)onaysizKullanicilarTable.getModel()).getValueAt(modelRow, buttonColumnID);
		        if(UserModel.updateVerifyStatus(ElementID.ElementID, 1)) JOptionPane.showMessageDialog(mainFrame, "Hesap başarıyla onaylandı.");
		        else JOptionPane.showMessageDialog(mainFrame, "Hesap onaylanırken bir hata oluştu.");
		        refreshKullaniciIslemleri();
		    }
		};
		ButtonColumn buttonColumn = new ButtonColumn(onaysizKullanicilarTable, onaysizKullanicilarButtonAction, buttonColumnID);
		buttonColumn.setMnemonic(KeyEvent.VK_D);

		onaysizKullanicilarScrollPane.setViewportView(onaysizKullanicilarTable);
		return onaysizKullanicilar;
	}
	public void KullaniciDuzenleme(int UserID, String Username, String Password, String Name, String Surname, String Phone, String Email, int ProvinceID, int DistrictID, int NeighborhoodID, int VerifyStatus) {
		MainFrame editUserFrame = new MainFrame(650, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Kullanıcı Düzenleme");
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		headerLabel.setBounds(148, 12, 272, 29);

		mainPanel.add(headerLabel);
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 600, 625);
		editUserFrame.getContentPane().add(mainPanel);
	
		mainPanel.setLayout(null);
		editUserFrame.getContentPane().add(mainPanel);
		
		CustomPasswordField repasswordField = new CustomPasswordField("Şifre Tekrar", mainPanel);
		repasswordField.setBounds(333, 156, 220, 30);
		repasswordField.setText(Password);
		mainPanel.add(repasswordField);
		
		JLabel passwordIcon_1 = new JLabel("New label");
		passwordIcon_1.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon_1.setBounds(286, 153, 35, 35);
		mainPanel.add(passwordIcon_1);
		
		JComboBox<ComboDBItem> provincesCombobox = new JComboBox<ComboDBItem>();
		provincesCombobox.setBounds(333, 384, 220, 24);
		ResultSet Provinces = LocationModel.getProvinces();
		try {
			while(Provinces.next()) {
				ComboDBItem provinceItem = new ComboDBItem(Provinces.getInt("ProvinceID") ,Provinces.getString("ProvinceName"));
				provincesCombobox.addItem(provinceItem);
				if(Provinces.getInt("ProvinceID") == ProvinceID) {
					provincesCombobox.setSelectedItem(provinceItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(provincesCombobox);
		
		
		JComboBox<ComboDBItem> districtsCombobox = new JComboBox<ComboDBItem>();
		districtsCombobox.setBounds(333, 430, 220, 24);
		ResultSet Districts = LocationModel.getDistricts(ProvinceID);
		try {
			while(Districts.next()) {
				ComboDBItem districtItem = new ComboDBItem(Districts.getInt("DistrictID") ,Districts.getString("DistrictName"));
				districtsCombobox.addItem(districtItem);
				if(Districts.getInt("DistrictID") == DistrictID) {
					districtsCombobox.setSelectedItem(districtItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(districtsCombobox);
		
		JComboBox<ComboDBItem> neighborsCombobox = new JComboBox<ComboDBItem>();
		neighborsCombobox.setBounds(333, 476, 220, 24);
		ResultSet Neighborhoods = LocationModel.getNeighborhoods(DistrictID);
		try {
			while(Neighborhoods.next()) {
				ComboDBItem neighborhoodItem = new ComboDBItem(Neighborhoods.getInt("NeighborhoodID") ,Neighborhoods.getString("NeighborhoodName"));
				neighborsCombobox.addItem(neighborhoodItem);
				if(Neighborhoods.getInt("NeighborhoodID") == NeighborhoodID) {
					neighborsCombobox.setSelectedItem(neighborhoodItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(neighborsCombobox);
		
		
		provincesCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				locker = true;
				ComboDBItem SelectedItem = (ComboDBItem) provincesCombobox.getSelectedItem();
				if(SelectedItem.ElementID == -1) return;
				try {
					ResultSet Districts = LocationModel.getDistricts(SelectedItem.ElementID);
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
					ResultSet Neighborhoods = LocationModel.getNeighborhoods(SelectedItem.ElementID);
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
		
		JLabel emailIcon_1 = new JLabel("New label");
		emailIcon_1.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/confirmation.png")));
		emailIcon_1.setBounds(286, 516, 35, 35);
		mainPanel.add(emailIcon_1);
		
		JCheckBox userStatusCheckBox = new JCheckBox("New check box");
		userStatusCheckBox.setBounds(333, 522, 220, 23);
		userStatusCheckBox.setOpaque(false);
		userStatusCheckBox.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
		        if(e.getStateChange() == ItemEvent.SELECTED) {
		    		userStatusCheckBox.setText("Onaylı");
		        }else{
		    		userStatusCheckBox.setText("Onaysız");
		        }
		    }
		});
		if(VerifyStatus == 1) {
			userStatusCheckBox.setSelected(true);
    		userStatusCheckBox.setText("Onaylı");

		}else {
			userStatusCheckBox.setSelected(false);
    		userStatusCheckBox.setText("Onaysız");
		}
		
		mainPanel.add(userStatusCheckBox);
		
		JLabel provinceIcon = new JLabel("New label");
		provinceIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/map.png")));
		provinceIcon.setBounds(286, 379, 35, 35);
		mainPanel.add(provinceIcon);
		
		JButton registerButton = new JButton("Kaydet");
		registerButton.setForeground(Color.WHITE);
		registerButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		registerButton.setBackground(new Color(0, 0, 51));
		registerButton.setBounds(286, 563, 123, 25);
		mainPanel.add(registerButton);
		
		JButton cancelButton = new JButton("İptal");
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		cancelButton.setBackground(new Color(0, 0, 51));
		cancelButton.setBounds(430, 563, 123, 25);
		mainPanel.add(cancelButton);
		
		JLabel neighborhoodIcon = new JLabel("New label");
		neighborhoodIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/map.png")));
		neighborhoodIcon.setBounds(286, 471, 35, 35);
		mainPanel.add(neighborhoodIcon);
		
		JLabel districtIcon = new JLabel("New label");
		districtIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/map.png")));
		districtIcon.setBounds(286, 425, 35, 35);
		mainPanel.add(districtIcon);
		
		CustomTextField telephoneField = new CustomTextField("Telefon", mainPanel);
		telephoneField.setBounds(333, 290, 220, 30);
		mainPanel.add(telephoneField);
		telephoneField.setText(Phone);

		CustomTextField emailField = new CustomTextField("E-mail", mainPanel);
		emailField.setBounds(333, 335, 220, 30);
		mainPanel.add(emailField);
		emailField.setText(Email);

		JLabel emailIcon = new JLabel("New label");
		emailIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/email.png")));
		emailIcon.setBounds(286, 332, 35, 35);
		mainPanel.add(emailIcon);
		
		JLabel telephoneIcon = new JLabel("New label");
		telephoneIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/phone.png")));
		telephoneIcon.setBounds(286, 287, 35, 35);
		mainPanel.add(telephoneIcon);
		
		CustomTextField surnameField = new CustomTextField("Soyadı", mainPanel);
		surnameField.setBounds(333, 245, 220, 30);
		mainPanel.add(surnameField);
		surnameField.setText(Surname);
		JLabel surnameIcon = new JLabel("New label");
		surnameIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/username.png")));
		surnameIcon.setBounds(286, 242, 35, 35);
		mainPanel.add(surnameIcon);
		
		CustomTextField nameField = new CustomTextField("Adı", mainPanel);
		nameField.setBounds(333, 200, 220, 30);
		mainPanel.add(nameField);
		nameField.setText(Name);
		
		JLabel nameIcon = new JLabel("New label");
		nameIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/username.png")));
		nameIcon.setBounds(286, 197, 35, 35);
		mainPanel.add(nameIcon);
		
		JLabel usernameIcon = new JLabel("New label");
		usernameIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/username.png")));
		usernameIcon.setBounds(286, 65, 35, 35);
		mainPanel.add(usernameIcon);
		
		JLabel passwordIcon = new JLabel("New label");
		passwordIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon.setBounds(286, 109, 35, 35);
		mainPanel.add(passwordIcon);
		
		CustomTextField usernameField = new CustomTextField("Kullanıcı Adı", mainPanel);
		usernameField.setBounds(333, 68, 220, 30);
		mainPanel.add(usernameField);
		usernameField.setText(Username);
		usernameField.setEditable(false);
		CustomPasswordField passwordField = new CustomPasswordField("Şifre", mainPanel);
		passwordField.setBounds(333, 112, 220, 30);
		passwordField.setText(Password);

		mainPanel.add(passwordField);
		
		JLabel leftHomeIcon = new JLabel();
		leftHomeIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/edituser.png")));
		leftHomeIcon.setBounds(12, 169, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		JLabel backgroundImage = new JLabel();
		backgroundImage.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/bg_middle.png")));
		backgroundImage.setBounds(0, 0, 600, 625);
		mainPanel.add(backgroundImage);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LicensesController setLicense = new LicensesController();
				int checkboxStatus;
				if(userStatusCheckBox.isSelected()) {
					checkboxStatus = 1; 
				}else {
					checkboxStatus = 0;
				}
				ComboDBItem provinceSelected = (ComboDBItem) provincesCombobox.getSelectedItem();
				ComboDBItem districtsSelected = (ComboDBItem) districtsCombobox.getSelectedItem();
				ComboDBItem neighborhoodSelected = (ComboDBItem) neighborsCombobox.getSelectedItem();
				UsersController UserController = new UsersController();
				try {
					UserController.editUser(UserID, checkboxStatus,nameField.getText(),surnameField.getText(),new String(passwordField.getPassword()),new String(repasswordField.getPassword()), telephoneField.getText(), emailField.getText(),provinceSelected.ElementID,districtsSelected.ElementID,neighborhoodSelected.ElementID);
					// UserID, Name, Surname, Password, Phone, Email, Province, District, Neighborhood
					JOptionPane.showMessageDialog(mainFrame, "Kullanıcı başarıyla düzenlendi.");
					refreshKullaniciIslemleri();
				} catch (Exception e) {
					
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
			}
		});
		editUserFrame.setVisible(true);
	}
	public JPanel LisansEkleme() {
		JPanel lisansEkleme= new JPanel();
		JScrollPane lisansEklemeScrollPane = new JScrollPane();

		lisansEkleme.add(lisansEklemeScrollPane);
		lisansEkleme.setLayout(null);
		lisansEkleme.setBounds(0, 0, 900, 555);
		lisansEklemeScrollPane.setLayout(null);
		lisansEklemeScrollPane.setBounds(0, 0, 895, 528);
		
		JLabel anahtarText = new JLabel("Lisans Anahtarı");
		anahtarText.setForeground(Color.BLACK);
		anahtarText.setFont(new Font("Dialog", Font.PLAIN, 14));
		anahtarText.setBounds(325, 80, 325, 15);
		lisansEklemeScrollPane.add(anahtarText);

		JTextField textField = new JTextField();
		textField.setBounds(325, 100, 325, 25);
		textField.setEditable(false);
		textField.setText(new LicensesController().generateLicense());
		
		JLabel gecerlilikSuresi = new JLabel("Geçerlilik Süresi");
		gecerlilikSuresi.setForeground(Color.BLACK);
		gecerlilikSuresi.setFont(new Font("Dialog", Font.PLAIN, 14));
		gecerlilikSuresi.setBounds(325, 140, 325, 15);
		lisansEklemeScrollPane.add(gecerlilikSuresi);
		
		DatePickerSettings datePickerSettings = new DatePickerSettings();
		datePickerSettings.setFormatForDatesBeforeCommonEra("dd.MM.yyyy");
		datePickerSettings.setFormatForDatesCommonEra("dd.MM.yyyy");
		TimePickerSettings timePickerSettings = new TimePickerSettings();
		timePickerSettings.use24HourClockFormat();
		DatePicker datePicker = new DatePicker(datePickerSettings);
		datePicker.setBounds(325, 160, 325, 25);
		datePicker.setDateToToday();
		
		lisansEklemeScrollPane.add(textField);
		lisansEklemeScrollPane.add(datePicker);
		
		JButton btnNewButton_1 = new JButton("Lisansı Kaydet");
		btnNewButton_1.setBounds(325, 210, 150, 25);
		lisansEklemeScrollPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LicensesController setLicense = new LicensesController();
				try {
					setLicense.setLicense(UserSessionData.UserID, textField.getText(), datePicker.getDateStringOrEmptyString()+" 23:59:59");
					JOptionPane.showMessageDialog(mainFrame, "Lisans başarıyla tanımlandı");
					refreshLisansIslemleri();
				} catch (Exception e) {
					
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
			}
		});
		JButton btnNewButton = new JButton("Lisans Türet");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setText(new LicensesController().generateLicense());
			}
		});
		btnNewButton.setBounds(500, 210, 150, 25);
		lisansEklemeScrollPane.add(btnNewButton);

		return lisansEkleme;
	}
	public void LisansDuzenleme(int LicenseID, String License, String Date){

		MainFrame editLicenseFrame = new MainFrame(350, 600);
		JPanel editLicensePanel = new JPanel();
		JLabel headerLabel = new JLabel("Lisans Düzenleme");
		JLabel Icon1 = new JLabel();
		JLabel Icon2 = new JLabel();
		CustomTextField licenseField = new CustomTextField("",editLicensePanel);
		JButton saveButton = new JButton("Kaydet");
		JButton generateButton = new JButton("Lisans Türet");
		JLabel backgroundImage = new JLabel();
		editLicenseFrame.addWindowListener(new WindowAdapter(){
	         public void windowOpened(WindowEvent windowEvent){
	        	 saveButton.requestFocus();
	         }
	    });
		
		editLicenseFrame.getContentPane().setLayout(null);
		
		editLicensePanel.setBounds(0, 0, 600, 325);
		editLicenseFrame.getContentPane().add(editLicensePanel);
		editLicensePanel.setLayout(null);
		
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setBounds(183, 22, 241, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		editLicensePanel.add(headerLabel);
		
		DatePickerSettings datePickerSettings = new DatePickerSettings();
		datePickerSettings.setFormatForDatesBeforeCommonEra("dd.MM.yyyy");
		datePickerSettings.setFormatForDatesCommonEra("dd.MM.yyyy");
		TimePickerSettings timePickerSettings = new TimePickerSettings();
		timePickerSettings.use24HourClockFormat();
		DatePicker datePicker = new DatePicker(datePickerSettings);
		datePicker.setBounds(201, 153, 250, 30);

		datePicker.getComponentDateTextField().setOpaque(false);
		datePicker.getComponentDateTextField().setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		datePicker.getComponentToggleCalendarButton().setForeground(new Color(255, 255, 255));
		datePicker.getComponentToggleCalendarButton().setBackground(new Color(0, 0, 51));
		datePicker.getComponentToggleCalendarButton().setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		datePicker.setOpaque(false);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d = dateFormat.parse(Date);
			dateFormat.applyPattern("dd.MM.yyyy");
			datePicker.setDate(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		} catch (ParseException e1) {
			
			e1.printStackTrace();
		}

		editLicensePanel.add(datePicker);

		Icon1.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/date.png")));
		Icon1.setBounds(153, 148, 35, 35);
		editLicensePanel.add(Icon1);
		
		Icon2.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/key.png")));
		Icon2.setBounds(153, 99, 35, 35);
		editLicensePanel.add(Icon2);
		
		licenseField.setBounds(201, 99, 250, 30);
		licenseField.setText(License);
		licenseField.setEditable(false);
		editLicensePanel.add(licenseField);		
		
		saveButton.setForeground(new Color(255, 255, 255));
		saveButton.setBackground(new Color(0, 0, 51));
		saveButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LicensesController LicenseController = new LicensesController();	
				try {	
					LicenseController.updateLicense(LicenseID,licenseField.getText(),datePicker.getDateStringOrEmptyString()+" 23:59:59");	
					JOptionPane.showMessageDialog(mainFrame,"Lisans anahtarı başarıyla düzenlendi.");	
					refreshLisansIslemleri();
				} catch (Exception e) {	
						
					JOptionPane.showMessageDialog(mainFrame,e.getMessage());	
				}
			}
		});
		saveButton.setBounds(153, 213, 140, 25);
		editLicensePanel.add(saveButton);
		
		generateButton.setForeground(new Color(255, 255, 255));
		generateButton.setBackground(new Color(0, 0, 51));
		generateButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				licenseField.setText(new LicensesController().generateLicense());
			}
		});
		generateButton.setBounds(311, 213, 140, 25);
		editLicensePanel.add(generateButton);
		
		backgroundImage.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/bg_min.png")));
		backgroundImage.setBounds(0, 0, 600, 325);
		editLicensePanel.add(backgroundImage);

		editLicenseFrame.setVisible(true);
	
	}
	public JPanel TumLisanslar() {
		JPanel tumLisanslar= new JPanel();
		int buttonColumn1ID = 3;
		int buttonColumn2ID = 4;
		JScrollPane tumLisanslarScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == buttonColumn1ID || column == buttonColumn2ID);
			}
		};
		JTable tumLisanslarTable = new JTable( model );
		String[] columnNames = {"İlişkili Kullanıcı", "Lisans Anahtar", "Geçerlilik Süresi","Kopyala","Düzenle"};
		tumLisanslar.add(tumLisanslarScrollPane);
		tumLisanslar.setLayout(null);
		tumLisanslar.setBounds(0, 0, 900, 555);
		tumLisanslarScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet ActiveLicenses = LicensesModel.getAllActiveLicensesByUserID(UserSessionData.UserID);
		ResultSet InactiveLicenses = LicensesModel.getInactiveLicensesByUserID(UserSessionData.UserID);
		
		try {
			while(ActiveLicenses.next()){
				Object[] ActiveLicenseData = {ActiveLicenses.getString("Username"), ActiveLicenses.getString("License"),  ActiveLicenses.getString("Datetime"), new ComboDBItem(ActiveLicenses.getInt("LicenseID"),"Kopyala"), new ComboDBItem(ActiveLicenses.getInt("LicenseID"),"Düzenle")};
			    model.addRow(ActiveLicenseData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			while(InactiveLicenses.next()){
				Object[] InactiveLicenseData = {"-", InactiveLicenses.getString("License"), InactiveLicenses.getString("Datetime"), new ComboDBItem(InactiveLicenses.getInt("LicenseID"),"Kopyala"), new ComboDBItem(InactiveLicenses.getInt("LicenseID"),"Düzenle")};
			    model.addRow(InactiveLicenseData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Action LisanslarKopyalaAction = new AbstractAction(){
		    public void actionPerformed(ActionEvent e)
		    {
		       JTable tumLisanslarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)tumLisanslarTable.getModel()).getValueAt(modelRow, buttonColumn1ID);
		        ResultSet selectedLicenseData = LicensesModel.getLicenseByID(ElementID.ElementID);
		        try {
		        	if(selectedLicenseData.next()){
		        	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		        	    Transferable transferable = new StringSelection(selectedLicenseData.getString("License"));
		        	    clipboard.setContents(transferable, null);
						JOptionPane.showMessageDialog(mainFrame, "Lisans anahtarı kopyalandı.");
		        	}
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
		    }
		};
		Action LisanslarDuzenleAction = new AbstractAction(){
		    public void actionPerformed(ActionEvent e)
		    {
		       JTable tumLisanslarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)tumLisanslarTable.getModel()).getValueAt(modelRow, buttonColumn2ID);
		        ResultSet selectedLicenseData = LicensesModel.getLicenseByID(ElementID.ElementID);
		        try {
		        	if(selectedLicenseData.next()){
		        		LisansDuzenleme(selectedLicenseData.getInt("LicenseID"), selectedLicenseData.getString("License"),selectedLicenseData.getString("Datetime"));
		        	}
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
		    }
		};
		tumLisanslarTable.setAutoCreateRowSorter(true);
		ButtonColumn buttonColumn1 = new ButtonColumn(tumLisanslarTable, LisanslarKopyalaAction, buttonColumn1ID);
		ButtonColumn buttonColumn2 = new ButtonColumn(tumLisanslarTable, LisanslarDuzenleAction, buttonColumn2ID);
		buttonColumn1.setMnemonic(KeyEvent.VK_D);
		buttonColumn2.setMnemonic(KeyEvent.VK_D);

		tumLisanslarScrollPane.setViewportView(tumLisanslarTable);
		return tumLisanslar;
	}
	public JPanel tumEmlaklar() {
		JPanel tumEmlaklar= new JPanel();
		int buttonColumnID = 9;
		JScrollPane tumEmlaklarScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == buttonColumnID);
			}
		};
		JTable tumEmlaklarTable = new JTable( model );
		String[] columnNames = {"İlan Başlığı", "İlan Türü", "Oda Sayısı", "Net Alan", "Brüt Alan", "İl", "İlçe", "Fiyat", "Onay Durumu", "Düzenle"};
		tumEmlaklar.add(tumEmlaklarScrollPane);
		tumEmlaklar.setLayout(null);
		tumEmlaklar.setBounds(0, 0, 900, 555);
		tumEmlaklarScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		EstateModel.VerifyStatus = -1;
		EstateModel.Zone = this.Zone;
		EstateModel.ZoneID = this.ZoneID;
		ResultSet Estates = EstateModel.getEstatesByFilter();
		try {
			while(Estates.next()){
				Object[] EstateData = {Estates.getString("Title"), Estates.getString("EstateTypeText"), Estates.getInt("RoomCount"), Estates.getInt("NetArea"), Estates.getInt("GrossArea"), Estates.getString("ProvinceName"), Estates.getString("DistrictName"), Estates.getInt("Price"), Estates.getString("VerifyStatusText"), new ComboDBItem(Estates.getInt("EstateID"),"Düzenle")};
			    model.addRow(EstateData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tumEmlaklarTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		tumEmlaklarTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		tumEmlaklarTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		tumEmlaklarTable.getColumnModel().getColumn(4).setPreferredWidth(40);

		Action tumEmlaklarButtonAction = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
		       JTable tumEmlaklarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)tumEmlaklarTable.getModel()).getValueAt(modelRow, buttonColumnID);
		        ResultSet selectedEstateData = EstateModel.getEstateByID(ElementID.ElementID);
		        try {
		        	if(selectedEstateData.next())
					EmlakDuzenleme(selectedEstateData.getString("Title"),selectedEstateData.getString("Description"), selectedEstateData.getInt("EstateType"),  Integer.toString(selectedEstateData.getInt("Price")),  Integer.toString(selectedEstateData.getInt("BathCount")),  Integer.toString(selectedEstateData.getInt("NetArea")),  Integer.toString(selectedEstateData.getInt("GrossArea")),  Integer.toString(selectedEstateData.getInt("RoomCount")),  Integer.toString(selectedEstateData.getInt("FloorCount")),  selectedEstateData.getInt("HeatingType"), selectedEstateData.getInt("ProvinceID"), selectedEstateData.getInt("DistrictID"), selectedEstateData.getInt("NeighborhoodID"), selectedEstateData.getInt("VerifyStatus"), selectedEstateData.getInt("EstateID"));
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
		    }
		};
		tumEmlaklarTable.setAutoCreateRowSorter(true);
		ButtonColumn buttonColumn = new ButtonColumn(tumEmlaklarTable, tumEmlaklarButtonAction, buttonColumnID);

		buttonColumn.setMnemonic(KeyEvent.VK_D);

		tumEmlaklarScrollPane.setViewportView(tumEmlaklarTable);
		return tumEmlaklar;
	}
	public JPanel onaysizEmlaklar() {
		JPanel tumEmlaklar= new JPanel();
		int buttonColumnID = 9;
		JScrollPane tumEmlaklarScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == buttonColumnID);
			}
		};
		JTable tumEmlaklarTable = new JTable( model );
		String[] columnNames = {"İlan Başlığı", "İlan Türü", "Oda Sayısı", "Net Alan", "Brüt Alan", "İl", "İlçe", "Fiyat", "Onay Durumu", "Onayla"};
		tumEmlaklar.add(tumEmlaklarScrollPane);
		tumEmlaklar.setLayout(null);
		tumEmlaklar.setBounds(0, 0, 900, 555);
		tumEmlaklarScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet Estates = EstateModel.getEstatesByVerifyStatusAndZone(0,Zone,ZoneID);
		try {
			while(Estates.next()){
				Object[] EstateData = {Estates.getString("Title"), Estates.getString("EstateTypeText"), Estates.getInt("RoomCount"), Estates.getInt("NetArea"), Estates.getInt("GrossArea"), Estates.getString("ProvinceName"), Estates.getString("DistrictName"), Estates.getInt("Price"), Estates.getString("VerifyStatusText"), new ComboDBItem(Estates.getInt("EstateID"),"Onayla")};
			    model.addRow(EstateData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tumEmlaklarTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		tumEmlaklarTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		tumEmlaklarTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		tumEmlaklarTable.getColumnModel().getColumn(4).setPreferredWidth(40);

		Action tumEmlaklarButtonAction = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
		       JTable tumEmlaklarTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)tumEmlaklarTable.getModel()).getValueAt(modelRow, buttonColumnID);
	        	if(EstateModel.verifyEstate(ElementID.ElementID, 1))
					JOptionPane.showMessageDialog(mainFrame, "İlan başarıyla onaylandı.");
	        	else
					JOptionPane.showMessageDialog(mainFrame, "İşlem başarısız. Bilinmeyen bir hata oluştu.");
	        	refreshEmlakIslemleri();
		    }
		};
		tumEmlaklarTable.setAutoCreateRowSorter(true);
		ButtonColumn buttonColumn = new ButtonColumn(tumEmlaklarTable, tumEmlaklarButtonAction, buttonColumnID);

		buttonColumn.setMnemonic(KeyEvent.VK_D);

		tumEmlaklarScrollPane.setViewportView(tumEmlaklarTable);
		return tumEmlaklar;
	}
	public void EmlakDuzenleme(String estateHeader,String estateDesc, int estateType, String estatePrice, String bathCount, String netArea, String grossArea, String roomCount, String floorCount, int heathingType, int provinceID, int districtID, int neighborhoodID, int VerifyStatus, int EstateID) {
		MainFrame editUserFrame = new MainFrame(850, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Emlak Düzenleme");
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		headerLabel.setBounds(180, 12, 240, 29);

		mainPanel.add(headerLabel);
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 600, 825);
		editUserFrame.getContentPane().add(mainPanel);
	
		mainPanel.setLayout(null);
		editUserFrame.getContentPane().add(mainPanel);

		JLabel headerFieldIcon = new JLabel("New label");
		headerFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		headerFieldIcon.setBounds(286, 65, 35, 35);
		mainPanel.add(headerFieldIcon);
		
		CustomTextField headerField = new CustomTextField("İlan Başlığı", mainPanel);
		headerField.setBounds(333, 69, 220, 30);
		headerField.setText(estateHeader);
		mainPanel.add(headerField);
		
		JLabel descriptionFieldIcon = new JLabel("New label");
		descriptionFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		descriptionFieldIcon.setBounds(286, 117, 35, 35);
		mainPanel.add(descriptionFieldIcon);
		
		CustomTextArea descriptionField = new CustomTextArea("İlan Açıklaması", mainPanel);
		descriptionField.setBounds(333, 117, 220, 100);
		descriptionField.setText(estateDesc);
		mainPanel.add(descriptionField);
		
		JLabel typeComboboxIcon = new JLabel("New label");
		typeComboboxIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		typeComboboxIcon.setBounds(286, 224, 35, 35);
		mainPanel.add(typeComboboxIcon);
		
		JComboBox<ComboDBItem> typeCombobox = new JComboBox<ComboDBItem>();
		typeCombobox.setBackground(Color.WHITE);
		typeCombobox.addItem(new ComboDBItem(0,"Kiralık"));
		typeCombobox.addItem(new ComboDBItem(1,"Satılık"));		
		typeCombobox.setBounds(333, 229, 220, 24);
		typeCombobox.setSelectedIndex(estateType);
		mainPanel.add(typeCombobox);
		
		
		JLabel priceFieldIcon = new JLabel("New label");
		priceFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/price_icon.png")));
		priceFieldIcon.setBounds(286, 265, 35, 35);
		mainPanel.add(priceFieldIcon);
		
		CustomTextField priceField = new CustomTextField("Fiyat", mainPanel);
		priceField.setBounds(333, 268, 220, 30);
		priceField.setText(estatePrice);

		mainPanel.add(priceField);
		
		JLabel bathFieldIcon = new JLabel("New label");
		bathFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		bathFieldIcon.setBounds(286, 309, 35, 35);
		mainPanel.add(bathFieldIcon);
		
		CustomTextField bathField = new CustomTextField("Banyo Sayısı", mainPanel);
		bathField.setBounds(333, 312, 220, 30);
		bathField.setText(bathCount);
		mainPanel.add(bathField);
		
		JLabel netAreaFieldIcon = new JLabel("New label");
		netAreaFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		netAreaFieldIcon.setBounds(286, 354, 35, 35);
		mainPanel.add(netAreaFieldIcon);
		
		CustomTextField netAreaField = new CustomTextField("Net Alan (m²)", mainPanel);
		netAreaField.setBounds(333, 357, 220, 30);
		netAreaField.setText(netArea);
		mainPanel.add(netAreaField);
		
		JLabel grossAreaFieldIcon = new JLabel("New label");
		grossAreaFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		grossAreaFieldIcon.setBounds(286, 399, 35, 35);
		mainPanel.add(grossAreaFieldIcon);
		
		CustomTextField grossAreaField = new CustomTextField("Brüt Alan (m²)", mainPanel);
		grossAreaField.setBounds(333, 402, 220, 30);
		grossAreaField.setText(grossArea);
		mainPanel.add(grossAreaField);
		
		JLabel roomCountFieldIcon = new JLabel("New label");
		roomCountFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		roomCountFieldIcon.setBounds(286, 444, 35, 35);
		mainPanel.add(roomCountFieldIcon);
		
		CustomTextField roomCountField = new CustomTextField("Oda Sayısı", mainPanel);
		roomCountField.setBounds(333, 447, 220, 30);
		roomCountField.setText(roomCount);
		mainPanel.add(roomCountField);
		
		JLabel floorFieldIcon = new JLabel("New label");
		floorFieldIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		floorFieldIcon.setBounds(286, 489, 35, 35);
		mainPanel.add(floorFieldIcon);
		
		CustomTextField floorField = new CustomTextField("Kat Bilgisi", mainPanel);
		floorField.setBounds(333, 492, 220, 30);
		floorField.setText(floorCount);
		mainPanel.add(floorField);
		
		JLabel heathingComboboxIcon= new JLabel("New label");
		heathingComboboxIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/estate_icon.png")));
		heathingComboboxIcon.setBounds(286, 534, 35, 35);
		mainPanel.add(heathingComboboxIcon);
		
		JComboBox<ComboDBItem> heathingCombobox = new JComboBox<ComboDBItem>();
		heathingCombobox.setBounds(333, 539, 220, 24);
		heathingCombobox.addItem(new ComboDBItem(0,"Soba"));
		heathingCombobox.addItem(new ComboDBItem(1,"Doğalgaz"));
		heathingCombobox.addItem(new ComboDBItem(2,"Merkezi"));
		heathingCombobox.addItem(new ComboDBItem(3,"Klima"));
		heathingCombobox.addItem(new ComboDBItem(4,"Şömine"));
		heathingCombobox.setSelectedIndex(heathingType);
		mainPanel.add(heathingCombobox);
		
		JLabel provinceIcon = new JLabel("New label");
		provinceIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/map.png")));
		provinceIcon.setBounds(286, 575, 35, 35);
		mainPanel.add(provinceIcon);
		
		JComboBox<ComboDBItem> provincesCombobox = new JComboBox<ComboDBItem>();
		provincesCombobox.setBounds(333, 580, 220, 24);
		ResultSet Provinces = LocationModel.getProvinces();
		try {
			while(Provinces.next()) {
				ComboDBItem provinceItem = new ComboDBItem(Provinces.getInt("ProvinceID") ,Provinces.getString("ProvinceName"));
				provincesCombobox.addItem(provinceItem);
				if(Provinces.getInt("ProvinceID") == provinceID) {
					provincesCombobox.setSelectedItem(provinceItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(provincesCombobox);
		
		
		JLabel districtIcon = new JLabel("New label");
		districtIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/map.png")));
		districtIcon.setBounds(286, 621, 35, 35);
		mainPanel.add(districtIcon);
		
		JComboBox<ComboDBItem> districtsCombobox = new JComboBox<ComboDBItem>();
		districtsCombobox.setBounds(333, 626, 220, 24);
		ResultSet Districts = LocationModel.getDistricts(provinceID);
		try {
			while(Districts.next()) {
				ComboDBItem districtItem = new ComboDBItem(Districts.getInt("DistrictID") ,Districts.getString("DistrictName"));
				districtsCombobox.addItem(districtItem);
				if(Districts.getInt("DistrictID") == districtID) {
					districtsCombobox.setSelectedItem(districtItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(districtsCombobox);
		
		
		JLabel neighborhoodIcon = new JLabel("New label");
		neighborhoodIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/map.png")));
		neighborhoodIcon.setBounds(286, 667, 35, 35);
		mainPanel.add(neighborhoodIcon);
		
		JComboBox<ComboDBItem> neighborsCombobox = new JComboBox<ComboDBItem>();
		neighborsCombobox.setBounds(333, 672, 220, 24);
		ResultSet Neighborhoods = LocationModel.getNeighborhoods(districtID);
		try {
			while(Neighborhoods.next()) {
				ComboDBItem neighborhoodItem = new ComboDBItem(Neighborhoods.getInt("NeighborhoodID") ,Neighborhoods.getString("NeighborhoodName"));
				neighborsCombobox.addItem(neighborhoodItem);
				if(Neighborhoods.getInt("NeighborhoodID") == neighborhoodID) {
					neighborsCombobox.setSelectedItem(neighborhoodItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(neighborsCombobox);
		
		
		provincesCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				locker = true;
				ComboDBItem SelectedItem = (ComboDBItem) provincesCombobox.getSelectedItem();
				if(SelectedItem.ElementID == -1) return;
				try {
					ResultSet Districts = LocationModel.getDistricts(SelectedItem.ElementID);
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
					ResultSet Neighborhoods = LocationModel.getNeighborhoods(SelectedItem.ElementID);
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
		JLabel emailIcon_1 = new JLabel("New label");
		emailIcon_1.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/confirmation.png")));
		emailIcon_1.setBounds(286, 714, 35, 35);
		mainPanel.add(emailIcon_1);
		JCheckBox userStatusCheckBox = new JCheckBox("New check box");
		userStatusCheckBox.setBounds(333, 720, 220, 23);
		userStatusCheckBox.setOpaque(false);
		userStatusCheckBox.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
		        if(e.getStateChange() == ItemEvent.SELECTED) {
		    		userStatusCheckBox.setText("Onaylı");
		        }else{
		    		userStatusCheckBox.setText("Onaysız");
		        }
		    }
		});
		if(VerifyStatus == 1) {
			userStatusCheckBox.setSelected(true);
    		userStatusCheckBox.setText("Onaylı");

		}else {
			userStatusCheckBox.setSelected(false);
    		userStatusCheckBox.setText("Onaysız");
		}
		mainPanel.add(userStatusCheckBox);

		JButton saveButton = new JButton("Kaydet");
		saveButton.setForeground(Color.WHITE);
		saveButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		saveButton.setBackground(new Color(0, 0, 51));
		saveButton.setBounds(286, 773, 123, 25);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ComboDBItem provincesSelected = (ComboDBItem) provincesCombobox.getSelectedItem();
				ComboDBItem districtsSelected = (ComboDBItem) districtsCombobox.getSelectedItem();
				ComboDBItem neighborsSelected = (ComboDBItem) neighborsCombobox.getSelectedItem();
				ComboDBItem heathingSelected = (ComboDBItem) heathingCombobox.getSelectedItem();
				ComboDBItem typeSelected = (ComboDBItem) typeCombobox.getSelectedItem();
				
				EstatesController EstatesController = new EstatesController();
				try {
					int checkboxStatus;
					if(userStatusCheckBox.isSelected()) {
						checkboxStatus = 1; 
					}else {
						checkboxStatus = 0;
					}
					EstatesController.editEstate(headerField.getText(),descriptionField.getText(), typeSelected.ElementID , priceField.getText(), bathField.getText(), netAreaField.getText(), grossAreaField.getText(), roomCountField.getText(), floorField.getText(), heathingSelected.ElementID, provincesSelected.ElementID, districtsSelected.ElementID, neighborsSelected.ElementID, checkboxStatus, EstateID);
					JOptionPane.showMessageDialog(editUserFrame, "İlan başarıyla düzenlendi.");
					refreshEmlakIslemleri();
					editUserFrame.setVisible(false);
					editUserFrame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(editUserFrame, e.getMessage());
				}
			}
		});
		mainPanel.add(saveButton);
		JButton cancelButton = new JButton("İptal");
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		cancelButton.setBackground(new Color(0, 0, 51));
		cancelButton.setBounds(430, 773, 123, 25);
		mainPanel.add(cancelButton);
		
		JLabel leftHomeIcon = new JLabel();
		leftHomeIcon.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/apartment.png")));
		leftHomeIcon.setBounds(12, 194, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		JLabel backgroundImage = new JLabel();
		backgroundImage.setIcon(new ImageIcon(AltYoneticiEkrani.class.getResource("/assets/images/bg_max.png")));
		backgroundImage.setBounds(0, 0, 600, 825);
		mainPanel.add(backgroundImage);	


		editUserFrame.setVisible(true);
	}
}