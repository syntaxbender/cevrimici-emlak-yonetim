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
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import model.*;
import view.core.ButtonColumn;
import view.core.CustomPasswordField;
import view.core.CustomTextArea;
import view.core.CustomTextField;
import view.core.MainFrame;

public class YetkiliKullaniciEkrani {
	public boolean locker;
	private MainFrame mainFrame;
	public JTabbedPane emlakIslemleri;
	public UsersModel UserSessionData;
	public UsersModel UserModel = new UsersModel();
	public LocationsModel LocationModel = new LocationsModel();
	public EstatesModel EstateModel = new EstatesModel();
	public ArrayList<Integer> Favorites = new ArrayList<Integer>();
	public YetkiliKullaniciEkrani(UsersModel UserSessionData){
		this.UserSessionData = UserSessionData;
		mainFrame = new MainFrame(600, 900,true);
		mainFrame.getContentPane().setBackground(Color.WHITE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(false);
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		JMenu mnIlemler = new JMenu("Emlaklar");
		menuBar.add(mnIlemler);
		
		JMenuItem mntmTest1 = new JMenuItem("Emlak İlanları Filtreleme");
		
		mnIlemler.add(mntmTest1);
		
		JMenu menuHesabim = new JMenu("Hesabım");
		menuBar.add(menuHesabim);
		
		JMenuItem mntmTest5 = new JMenuItem("Şifre Güncelleme");
		JMenuItem mntmTest6 = new JMenuItem("Mail Güncelleme");
		JMenuItem mntmTest7 = new JMenuItem("Lisans İşlemleri");
		
		menuHesabim.add(mntmTest5);
		menuHesabim.add(mntmTest6);
		menuHesabim.add(mntmTest7);

		mntmTest1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmlakFiltreleme();
			}
		});

		mntmTest5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SifreGuncelleme();

			}
		});
		mntmTest6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmailGuncelleme();

			}
		});
		mntmTest7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LisansIslemleri();
			}
		});
		EmlakIslemleriConstructor();
		mainFrame.getContentPane().add(emlakIslemleri);
		mainFrame.setVisible(true);		
	}
	public void refreshEmlakIslemleri() {
		int emlakIslemleriTabsIndex = emlakIslemleri.getSelectedIndex();
		EmlakIslemleriConstructor();
		emlakIslemleri.setSelectedIndex(emlakIslemleriTabsIndex);
	}
	public void EmlakIslemleriConstructor(){
		if(emlakIslemleri != null) {
			emlakIslemleri.removeAll(); 
		}else {
			emlakIslemleri = new JTabbedPane(JTabbedPane.TOP);
			emlakIslemleri.setBounds(0, 0, 900, 555);
		}
		
		JPanel emlakIlanlari = EmlakIlanlari();
		JScrollPane emlakEkleme = EmlakEkleme();
		JPanel emlakIlanlarim = EmlakIlanlarim();
		JPanel favoriEmlaklarim = FavoriEmlaklarim();

		
		emlakIslemleri.addTab("Emlak İlanları", null, emlakIlanlari, null);
		emlakIslemleri.addTab("Emlak İlanı Yayınlama", null, emlakEkleme, null);
		emlakIslemleri.addTab("Emlak İlanlarım", null, emlakIlanlarim, null);
		emlakIslemleri.addTab("Favori Emlaklarım", null, favoriEmlaklarim, null);

	}

	public JPanel EmlakIlanlari() {

		JPanel EmlakIlanlari= new JPanel();
		int buttonColumn1ID = 8;
		int buttonColumn2ID = 9;
		JScrollPane EmlakIlanlariScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == buttonColumn1ID || column == buttonColumn2ID);
			}
		};
		JTable EmlakIlanlariTable = new JTable(model);
		String[] columnNames = {"İlan Başlığı", "İlan Türü", "Oda Sayısı", "Net Alan", "Brüt Alan", "İl", "İlçe", "Fiyat", "Görüntüle", "Favorilere Ekle"};
		EmlakIlanlari.add(EmlakIlanlariScrollPane);
		EmlakIlanlari.setLayout(null);
		EmlakIlanlari.setBounds(0, 0, 900, 555);
		EmlakIlanlariScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet Estates = EstateModel.getEstatesByFilter();
		try {
			while(Estates.next()){
				Object[] EstateData = {Estates.getString("Title"), Estates.getString("EstateTypeText"), Estates.getInt("RoomCount"), Estates.getInt("NetArea"), Estates.getInt("GrossArea"), Estates.getString("ProvinceName"), Estates.getString("DistrictName"), Estates.getInt("Price"), new ComboDBItem(Estates.getInt("EstateID"),"Görüntüle"), new ComboDBItem(Estates.getInt("EstateID"),"Favorilere Ekle")};
			    model.addRow(EstateData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EmlakIlanlariTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		EmlakIlanlariTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		EmlakIlanlariTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		EmlakIlanlariTable.getColumnModel().getColumn(4).setPreferredWidth(40);

		Action EmlakIlanlariButton1Action = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
				JTable EmlakIlanlariTable = (JTable)e.getSource();
				int modelRow = Integer.valueOf( e.getActionCommand() );
				ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)EmlakIlanlariTable.getModel()).getValueAt(modelRow, buttonColumn2ID);
		    	ResultSet EstateData = EstateModel.getEstateByID(ElementID.ElementID);
		    	String heathingType = "";
		    	String estateType = "";
		    	try {
		    		EstateData.next();
			    	estateType = EstateData.getInt("EstateType") == 0 
			    			  ? "Kiralık"
			    			  : "Satılık";
			    	int HeatingTypeID = EstateData.getInt("HeatingType");
			    	if(HeatingTypeID == 0) {
				    	heathingType = "Soba";
			    	}else if(HeatingTypeID == 1) {
				    	heathingType = "Doğalgaz";
			    	}else if(HeatingTypeID == 2) {
				    	heathingType = "Merkezi Isıtma";
			    	}else if(HeatingTypeID == 3) {
				    	heathingType = "Klima";
			    	}else if(HeatingTypeID == 4) {
				    	heathingType = "Şömine";
			    	}
			    	EmlakGoruntuleme(EstateData.getString("Title"),EstateData.getString("Description"), estateType, EstateData.getString("Price"), EstateData.getString("BathCount"), EstateData.getString("NetArea"), EstateData.getString("GrossArea"), EstateData.getString("RoomCount"), EstateData.getString("FloorCount"), heathingType, EstateData.getString("ProvinceName"), EstateData.getString("DistrictName"), EstateData.getString("NeighborhoodName"));
		    	}catch(Exception e1) {
		    		e1.printStackTrace();
		    	}
		    }
		};
		Action EmlakIlanlariButton2Action = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
		       JTable EmlakIlanlariTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)EmlakIlanlariTable.getModel()).getValueAt(modelRow, buttonColumn2ID);
		        EstatesController EstateController = new EstatesController();
		        try {
					if(EstateController.setFavorite(UserSessionData.UserID, ElementID.ElementID))
						JOptionPane.showMessageDialog(mainFrame, "İlan favorilere eklendi.");
					else
						JOptionPane.showMessageDialog(mainFrame, "İşlem başarısız, bilinmeyen bir hata oluştu.");
					refreshEmlakIslemleri();
				} catch (Exception e1) {
					
					JOptionPane.showMessageDialog(mainFrame, e1.getMessage());
				}
		    }
		};
		EmlakIlanlariTable.setAutoCreateRowSorter(true);
		ButtonColumn button1Column = new ButtonColumn(EmlakIlanlariTable, EmlakIlanlariButton1Action, buttonColumn1ID);
		ButtonColumn button2Column = new ButtonColumn(EmlakIlanlariTable, EmlakIlanlariButton2Action, buttonColumn2ID);

		button1Column.setMnemonic(KeyEvent.VK_D);
		button2Column.setMnemonic(KeyEvent.VK_D);

		EmlakIlanlariScrollPane.setViewportView(EmlakIlanlariTable);
		return EmlakIlanlari;
	}
	public JPanel EmlakIlanlarim() {

		JPanel EmlakIlanlari= new JPanel();
		int buttonColumnID = 9;
		JScrollPane EmlakIlanlariScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == buttonColumnID;
			}
		};
		JTable EmlakIlanlariTable = new JTable( model );
		String[] columnNames = {"İlan Başlığı", "İlan Türü", "Oda Sayısı", "Net Alan", "Brüt Alan", "İl", "İlçe", "Fiyat", "Durum", "Düzenle"};
		EmlakIlanlari.add(EmlakIlanlariScrollPane);
		EmlakIlanlari.setLayout(null);
		EmlakIlanlari.setBounds(0, 0, 900, 555);
		EmlakIlanlariScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		ResultSet Estates = EstateModel.getEstatesByUserID(UserSessionData.UserID);
		try {
			while(Estates.next()){
				Object[] EstateData = {Estates.getString("Title"), Estates.getString("EstateTypeText"), Estates.getInt("RoomCount"), Estates.getInt("NetArea"), Estates.getInt("GrossArea"), Estates.getString("ProvinceName"), Estates.getString("DistrictName"), Estates.getInt("Price"), Estates.getString("VerifyStatusText"), new ComboDBItem(Estates.getInt("EstateID"),"Düzenle")};
			    model.addRow(EstateData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EmlakIlanlariTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		EmlakIlanlariTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		EmlakIlanlariTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		EmlakIlanlariTable.getColumnModel().getColumn(4).setPreferredWidth(40);

		Action EmlakIlanlariButtonAction = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
		        JTable EmlakIlanlariTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)EmlakIlanlariTable.getModel()).getValueAt(modelRow, buttonColumnID);
		        ResultSet selectedEstateData = EstateModel.getEstateByID(ElementID.ElementID);
		        try {
		        	if(selectedEstateData.next()){
		        		EmlakDuzenleme(selectedEstateData.getString("Title"),selectedEstateData.getString("Description"), selectedEstateData.getInt("EstateType"),  Integer.toString(selectedEstateData.getInt("Price")),  Integer.toString(selectedEstateData.getInt("BathCount")),  Integer.toString(selectedEstateData.getInt("NetArea")),  Integer.toString(selectedEstateData.getInt("GrossArea")),  Integer.toString(selectedEstateData.getInt("RoomCount")),  Integer.toString(selectedEstateData.getInt("FloorCount")),  selectedEstateData.getInt("HeatingType"), selectedEstateData.getInt("ProvinceID"), selectedEstateData.getInt("DistrictID"), selectedEstateData.getInt("NeighborhoodID"), selectedEstateData.getInt("VerifyStatus"), selectedEstateData.getInt("EstateID"));
		        	}
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
		    }
		};
		EmlakIlanlariTable.setAutoCreateRowSorter(true);
		ButtonColumn buttonColumn = new ButtonColumn(EmlakIlanlariTable, EmlakIlanlariButtonAction, buttonColumnID);

		buttonColumn.setMnemonic(KeyEvent.VK_D);

		EmlakIlanlariScrollPane.setViewportView(EmlakIlanlariTable);
		return EmlakIlanlari;
	}
	public JPanel FavoriEmlaklarim() {

		JPanel EmlakIlanlari= new JPanel();
		int buttonColumn1ID = 8;
		int buttonColumn2ID = 9;
		JScrollPane EmlakIlanlariScrollPane = new JScrollPane();
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == buttonColumn1ID || column == buttonColumn2ID);
			}
		};
		JTable EmlakIlanlariTable = new JTable( model );
		String[] columnNames = {"İlan Başlığı", "İlan Türü", "Oda Sayısı", "Net Alan", "Brüt Alan", "İl", "İlçe", "Fiyat", "Görüntüle", "Sil"};
		EmlakIlanlari.add(EmlakIlanlariScrollPane);
		EmlakIlanlari.setLayout(null);
		EmlakIlanlari.setBounds(0, 0, 900, 555);
		EmlakIlanlariScrollPane.setBounds(0, 0, 895, 528);
		
		model.setColumnIdentifiers(columnNames);
		EstatesController EstateController = new EstatesController();
		ResultSet Estates = EstateController.getFavorites(UserSessionData.UserID);
		try {
			while(Estates.next()){
				Object[] EstateData = {Estates.getString("Title"), Estates.getString("EstateTypeText"), Estates.getInt("RoomCount"), Estates.getInt("NetArea"), Estates.getInt("GrossArea"), Estates.getString("ProvinceName"), Estates.getString("DistrictName"), Estates.getInt("Price"), new ComboDBItem(Estates.getInt("EstateID"),"Görüntüle"), new ComboDBItem(Estates.getInt("EstateID"),"Sil")};
			    model.addRow(EstateData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		EmlakIlanlariTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		EmlakIlanlariTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		EmlakIlanlariTable.getColumnModel().getColumn(3).setPreferredWidth(40);
		EmlakIlanlariTable.getColumnModel().getColumn(4).setPreferredWidth(40);
		Action EmlakIlanlariButton1Action = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
				JTable EmlakIlanlariTable = (JTable)e.getSource();
				int modelRow = Integer.valueOf( e.getActionCommand() );
				ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)EmlakIlanlariTable.getModel()).getValueAt(modelRow, buttonColumn2ID);
		    	ResultSet EstateData = EstateModel.getEstateByID(ElementID.ElementID);
		    	String heathingType = "";
		    	String estateType = "";
		    	try {
		    		EstateData.next();
			    	estateType = EstateData.getInt("EstateType") == 0 
			    			  ? "Kiralık"
			    			  : "Satılık";
			    	int HeatingTypeID = EstateData.getInt("HeatingType");
			    	if(HeatingTypeID == 0) {
				    	heathingType = "Soba";
			    	}else if(HeatingTypeID == 1) {
				    	heathingType = "Doğalgaz";
			    	}else if(HeatingTypeID == 2) {
				    	heathingType = "Merkezi Isıtma";
			    	}else if(HeatingTypeID == 3) {
				    	heathingType = "Klima";
			    	}else if(HeatingTypeID == 4) {
				    	heathingType = "Şömine";
			    	}
			    	EmlakGoruntuleme(EstateData.getString("Title"),EstateData.getString("Description"), estateType, EstateData.getString("Price"), EstateData.getString("BathCount"), EstateData.getString("NetArea"), EstateData.getString("GrossArea"), EstateData.getString("RoomCount"), EstateData.getString("FloorCount"), heathingType, EstateData.getString("ProvinceName"), EstateData.getString("DistrictName"), EstateData.getString("NeighborhoodName"));
		    	}catch(Exception e1) {
		    		e1.printStackTrace();
		    	}
		    }
		};
		Action EmlakIlanlariButton2Action = new AbstractAction() {
		    public void actionPerformed(ActionEvent e){
		        JTable EmlakIlanlariTable = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ComboDBItem ElementID = (ComboDBItem) ((DefaultTableModel)EmlakIlanlariTable.getModel()).getValueAt(modelRow, buttonColumn2ID);
		        if(EstateController.delFavorite(UserSessionData.UserID,ElementID.ElementID))
					JOptionPane.showMessageDialog(mainFrame, "İlan başarıyla favorilerinizden kaldırıldı.");
		        else
					JOptionPane.showMessageDialog(mainFrame, "İşlem başarısız. Bilinmeyen bir hata oluştu.");
		        refreshEmlakIslemleri();

		    }
		};
		EmlakIlanlariTable.setAutoCreateRowSorter(true);
		ButtonColumn button1Column = new ButtonColumn(EmlakIlanlariTable, EmlakIlanlariButton1Action, buttonColumn1ID);
		ButtonColumn button2Column = new ButtonColumn(EmlakIlanlariTable, EmlakIlanlariButton2Action, buttonColumn2ID);

		button1Column.setMnemonic(KeyEvent.VK_D);
		button2Column.setMnemonic(KeyEvent.VK_D);

		EmlakIlanlariScrollPane.setViewportView(EmlakIlanlariTable);
		return EmlakIlanlari;
	
	}
	
	public void EmlakDuzenleme(String estateHeader,String estateDesc, int estateType, String estatePrice, String bathCount, String netArea, String grossArea, String roomCount, String floorCount, int heathingType, int provinceID, int districtID, int neighborhoodID, int VerifyStatus, int EstateID) {
		MainFrame editUserFrame = new MainFrame(795, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Emlak Düzenleme");
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		headerLabel.setBounds(179, 24, 240, 29);

		mainPanel.add(headerLabel);
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 600, 770);
		editUserFrame.getContentPane().add(mainPanel);
	
		mainPanel.setLayout(null);
		editUserFrame.getContentPane().add(mainPanel);

		
		JLabel headerFieldIcon = new JLabel("New label");
		headerFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		headerFieldIcon.setBounds(286, 65, 35, 35);
		mainPanel.add(headerFieldIcon);
		
		CustomTextField headerField = new CustomTextField("İlan Başlığı", mainPanel);
		headerField.setBounds(333, 69, 220, 30);
		headerField.setText(estateHeader);
		mainPanel.add(headerField);
		
		JLabel descriptionFieldIcon = new JLabel("New label");
		descriptionFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		descriptionFieldIcon.setBounds(286, 117, 35, 35);
		mainPanel.add(descriptionFieldIcon);
		
		CustomTextArea descriptionField = new CustomTextArea("İlan Açıklaması", mainPanel);
		descriptionField.setBounds(333, 117, 220, 100);
		descriptionField.setText(estateDesc);
		mainPanel.add(descriptionField);
		
		JLabel typeComboboxIcon = new JLabel("New label");
		typeComboboxIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
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
		priceFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/price_icon.png")));
		priceFieldIcon.setBounds(286, 265, 35, 35);
		mainPanel.add(priceFieldIcon);
		
		CustomTextField priceField = new CustomTextField("Fiyat", mainPanel);
		priceField.setBounds(333, 268, 220, 30);
		priceField.setText(estatePrice);

		mainPanel.add(priceField);
		
		JLabel bathFieldIcon = new JLabel("New label");
		bathFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		bathFieldIcon.setBounds(286, 309, 35, 35);
		mainPanel.add(bathFieldIcon);
		
		CustomTextField bathField = new CustomTextField("Banyo Sayısı", mainPanel);
		bathField.setBounds(333, 312, 220, 30);
		bathField.setText(bathCount);
		mainPanel.add(bathField);
		
		JLabel netAreaFieldIcon = new JLabel("New label");
		netAreaFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		netAreaFieldIcon.setBounds(286, 354, 35, 35);
		mainPanel.add(netAreaFieldIcon);
		
		CustomTextField netAreaField = new CustomTextField("Net Alan (m²)", mainPanel);
		netAreaField.setBounds(333, 357, 220, 30);
		netAreaField.setText(netArea);
		mainPanel.add(netAreaField);
		
		JLabel grossAreaFieldIcon = new JLabel("New label");
		grossAreaFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		grossAreaFieldIcon.setBounds(286, 399, 35, 35);
		mainPanel.add(grossAreaFieldIcon);
		
		CustomTextField grossAreaField = new CustomTextField("Brüt Alan (m²)", mainPanel);
		grossAreaField.setBounds(333, 402, 220, 30);
		grossAreaField.setText(grossArea);
		mainPanel.add(grossAreaField);
		
		JLabel roomCountFieldIcon = new JLabel("New label");
		roomCountFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		roomCountFieldIcon.setBounds(286, 444, 35, 35);
		mainPanel.add(roomCountFieldIcon);
		
		CustomTextField roomCountField = new CustomTextField("Oda Sayısı", mainPanel);
		roomCountField.setBounds(333, 447, 220, 30);
		roomCountField.setText(roomCount);
		mainPanel.add(roomCountField);
		
		JLabel floorFieldIcon = new JLabel("New label");
		floorFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		floorFieldIcon.setBounds(286, 489, 35, 35);
		mainPanel.add(floorFieldIcon);
		
		CustomTextField floorField = new CustomTextField("Kat Bilgisi", mainPanel);
		floorField.setBounds(333, 492, 220, 30);
		floorField.setText(floorCount);
		mainPanel.add(floorField);
		
		JLabel heathingComboboxIcon= new JLabel("New label");
		heathingComboboxIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		heathingComboboxIcon.setBounds(286, 534, 35, 35);
		mainPanel.add(heathingComboboxIcon);
		
		JComboBox<ComboDBItem> heathingCombobox = new JComboBox<ComboDBItem>();
		heathingCombobox.setBounds(333, 539, 220, 24);
		heathingCombobox.addItem(new ComboDBItem(0,"Soba"));
		heathingCombobox.addItem(new ComboDBItem(1,"Doğalgaz"));
		heathingCombobox.addItem(new ComboDBItem(2,"Merkezi Isıtma"));
		heathingCombobox.addItem(new ComboDBItem(3,"Klima"));
		heathingCombobox.addItem(new ComboDBItem(4,"Şömine"));
		heathingCombobox.setSelectedIndex(heathingType);
		mainPanel.add(heathingCombobox);
		
		JLabel provinceIcon = new JLabel("New label");
		provinceIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
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
		districtIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
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
		neighborhoodIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
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

		JButton saveButton = new JButton("Kaydet");
		saveButton.setForeground(Color.WHITE);
		saveButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		saveButton.setBackground(new Color(0, 0, 51));
		saveButton.setBounds(286, 719, 123, 25);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ComboDBItem provincesSelected = (ComboDBItem) provincesCombobox.getSelectedItem();
				ComboDBItem districtsSelected = (ComboDBItem) districtsCombobox.getSelectedItem();
				ComboDBItem neighborsSelected = (ComboDBItem) neighborsCombobox.getSelectedItem();
				ComboDBItem heathingSelected = (ComboDBItem) heathingCombobox.getSelectedItem();
				ComboDBItem typeSelected = (ComboDBItem) typeCombobox.getSelectedItem();
				
				EstatesController EstatesController = new EstatesController();
				try {
					EstatesController.editEstate(headerField.getText(),descriptionField.getText(), typeSelected.ElementID , priceField.getText(), bathField.getText(), netAreaField.getText(), grossAreaField.getText(), roomCountField.getText(), floorField.getText(), heathingSelected.ElementID, provincesSelected.ElementID, districtsSelected.ElementID, neighborsSelected.ElementID, VerifyStatus , EstateID);
					JOptionPane.showMessageDialog(mainFrame, "İlan başarıyla düzenlendi.");
					refreshEmlakIslemleri();
					mainFrame.setVisible(false);
					mainFrame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
			}
		});
		mainPanel.add(saveButton);
		JButton cancelButton = new JButton("İptal");
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		cancelButton.setBackground(new Color(0, 0, 51));
		cancelButton.setBounds(430, 719, 123, 25);
		mainPanel.add(cancelButton);
		
		JLabel leftHomeIcon = new JLabel();
		leftHomeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/apartment.png")));
		leftHomeIcon.setBounds(12, 194, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		JLabel backgroundImage = new JLabel();
		backgroundImage.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/bg_max.png")));
		backgroundImage.setBounds(0, 0, 600, 770);
		mainPanel.add(backgroundImage);	

		editUserFrame.setVisible(true);
	}
	public JScrollPane EmlakEkleme() {

		JScrollPane emlakEklemeScrollPane = new JScrollPane();
		JPanel emlakEkleme= new JPanel();
		
		emlakEklemeScrollPane.setBounds(0,0,895, 528);
		
		emlakEkleme.setPreferredSize(new Dimension(850,930));
		emlakEkleme.setLayout(null);
		emlakEkleme.setBounds(0, 0, 850, 528);
		
		JLabel headerFieldLabel = new JLabel("İlan Başlığı");
		headerFieldLabel.setForeground(Color.BLACK);
		headerFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		headerFieldLabel.setBounds(325, 30, 325, 15);
		emlakEkleme.add(headerFieldLabel);

		JTextField headerField = new JTextField();
		headerField.setBounds(325, 50, 325, 25);
		emlakEkleme.add(headerField);

		JLabel descriptionFieldLabel = new JLabel("İlan Açıklaması");
		descriptionFieldLabel.setForeground(Color.BLACK);
		descriptionFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		descriptionFieldLabel.setBounds(325, 90, 325, 15);
		emlakEkleme.add(descriptionFieldLabel);	
		
		JTextArea descriptionField = new JTextArea();
		descriptionField.setBounds(325, 110, 325, 100);
		emlakEkleme.add(descriptionField);
		descriptionField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		JLabel typeComboboxLabel = new JLabel("İlan Türü");
		typeComboboxLabel.setForeground(Color.BLACK);
		typeComboboxLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		typeComboboxLabel.setBounds(325, 225, 325, 15);
		emlakEkleme.add(typeComboboxLabel);	
		
		JComboBox<ComboDBItem> typeCombobox = new JComboBox<ComboDBItem>();
		typeCombobox.setBackground(Color.WHITE);
		typeCombobox.setBounds(325, 245, 325, 25);
		typeCombobox.addItem(new ComboDBItem(0,"Kiralık"));
		typeCombobox.addItem(new ComboDBItem(1,"Satılık"));
		emlakEkleme.add(typeCombobox);	

		
		JLabel priceFieldLabel = new JLabel("Fiyat");
		priceFieldLabel.setForeground(Color.BLACK);
		priceFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		priceFieldLabel.setBounds(325, 285, 325, 15);
		emlakEkleme.add(priceFieldLabel);	
		
		JTextField priceField = new JTextField();
		priceField.setBounds(325, 305, 325, 25);
		emlakEkleme.add(priceField);

		
		JLabel bathFieldLabel = new JLabel("Banyo Sayısı");
		bathFieldLabel.setForeground(Color.BLACK);
		bathFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		bathFieldLabel.setBounds(325, 345, 325, 15);
		emlakEkleme.add(bathFieldLabel);	
		
		JTextField bathField = new JTextField();
		bathField.setBounds(325, 365, 325, 25);
		emlakEkleme.add(bathField);

		JLabel netAraeFieldLabel = new JLabel("Net Alan (m²)");
		netAraeFieldLabel.setForeground(Color.BLACK);
		netAraeFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		netAraeFieldLabel.setBounds(325, 405, 325, 15);
		emlakEkleme.add(netAraeFieldLabel);	
		
		JTextField netAreaField = new JTextField();
		netAreaField.setBounds(325, 425, 325, 25);
		emlakEkleme.add(netAreaField);
		
		

		JLabel grossAreaFieldLabel = new JLabel("Brüt Alan (m²)");
		grossAreaFieldLabel.setForeground(Color.BLACK);
		grossAreaFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		grossAreaFieldLabel.setBounds(325, 465, 325, 15);
		emlakEkleme.add(grossAreaFieldLabel);	
		
		JTextField grossAreaField = new JTextField();
		grossAreaField.setBounds(325, 485, 325, 25);
		emlakEkleme.add(grossAreaField);
		
		JLabel roomCountFieldLabel = new JLabel("Oda Sayısı");
		roomCountFieldLabel.setForeground(Color.BLACK);
		roomCountFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		roomCountFieldLabel.setBounds(325, 525, 325, 15);
		emlakEkleme.add(roomCountFieldLabel);	
		
		JTextField roomCountField = new JTextField();
		roomCountField.setBounds(325, 545, 325, 25);
		emlakEkleme.add(roomCountField);


		JLabel floorFieldLabel = new JLabel("Kat Bilgisi");
		floorFieldLabel.setForeground(Color.BLACK);
		floorFieldLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		floorFieldLabel.setBounds(325, 585, 325, 15);
		emlakEkleme.add(floorFieldLabel);	
		
		JTextField floorField = new JTextField();
		floorField.setBounds(325, 605, 325, 25);
		emlakEkleme.add(floorField);
		
		JLabel heathingComboboxLabel = new JLabel("Isıtma Türü");
		heathingComboboxLabel.setForeground(Color.BLACK);
		heathingComboboxLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		heathingComboboxLabel.setBounds(325, 645, 325, 15);
		emlakEkleme.add(heathingComboboxLabel);	
		
		JComboBox<ComboDBItem> heathingCombobox = new JComboBox<ComboDBItem>();
		
		heathingCombobox.setBackground(Color.WHITE);
		heathingCombobox.setBounds(325, 665, 325, 25);
		heathingCombobox.addItem(new ComboDBItem(0,"Soba"));
		heathingCombobox.addItem(new ComboDBItem(1,"Doğalgaz"));
		heathingCombobox.addItem(new ComboDBItem(2,"Merkezi"));
		heathingCombobox.addItem(new ComboDBItem(3,"Klima"));
		heathingCombobox.addItem(new ComboDBItem(4,"Şömine"));
		emlakEkleme.add(heathingCombobox);

		JLabel provincesComboboxLabel = new JLabel("Adres Bilgisi (İl)");
		provincesComboboxLabel.setForeground(Color.BLACK);
		provincesComboboxLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		provincesComboboxLabel.setBounds(325, 705, 325, 15);
		emlakEkleme.add(provincesComboboxLabel);	
		
		JLabel districtsComboboxLabel = new JLabel("Adres Bilgisi (İlçe)");
		districtsComboboxLabel.setForeground(Color.BLACK);
		districtsComboboxLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		districtsComboboxLabel.setBounds(325, 765, 325, 15);
		emlakEkleme.add(districtsComboboxLabel);	
		
		JLabel neighborsComboboxLabel = new JLabel("Adres Bilgisi (Mahalle/belde/köy)");
		neighborsComboboxLabel.setForeground(Color.BLACK);
		neighborsComboboxLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		neighborsComboboxLabel.setBounds(325, 825, 325, 15);
		emlakEkleme.add(neighborsComboboxLabel);
		
		JComboBox<ComboDBItem> provincesCombobox = new JComboBox<ComboDBItem>();
		JComboBox<ComboDBItem> districtsCombobox = new JComboBox<ComboDBItem>();
		JComboBox<ComboDBItem> neighborsCombobox = new JComboBox<ComboDBItem>();
		
		emlakEkleme.add(provincesCombobox);	
		emlakEkleme.add(districtsCombobox);	
		emlakEkleme.add(neighborsCombobox);
		
		provincesCombobox.setBackground(Color.WHITE);
		districtsCombobox.setBackground(Color.WHITE);
		neighborsCombobox.setBackground(Color.WHITE);
		
		provincesCombobox.setBounds(325, 725, 325, 25);
		districtsCombobox.setBounds(325, 785, 325, 25);
		neighborsCombobox.setBounds(325, 845, 325, 25);

		provincesCombobox.addItem(new ComboDBItem(-1,"Adres Bilgisi (İl)"));
		districtsCombobox.addItem(new ComboDBItem(-1,"Adres Bilgisi (İlçe)"));
		neighborsCombobox.addItem(new ComboDBItem(-1,"Adres Bilgisi (Mahalle/belde/köy)"));
		
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
					districtsCombobox.addItem(new ComboDBItem(-1,"Adres Bilgisi (İlçe)"));
					neighborsCombobox.addItem(new ComboDBItem(-1,"Adres Bilgisi (Mahalle/belde/köy)"));
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
					neighborsCombobox.addItem(new ComboDBItem(-1,"Adres Bilgisi (Mahalle/belde/köy)"));
					while(Neighborhoods.next()) {
						neighborsCombobox.addItem(new ComboDBItem(Neighborhoods.getInt("NeighborhoodID") ,Neighborhoods.getString("NeighborhoodName")));
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		});
		
		JButton btnNewButton_1 = new JButton("Kaydet");
		btnNewButton_1.setBounds(325, 885, 150, 25);
		emlakEkleme.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ComboDBItem provincesSelected = (ComboDBItem) provincesCombobox.getSelectedItem();
				ComboDBItem districtsSelected = (ComboDBItem) districtsCombobox.getSelectedItem();
				ComboDBItem neighborsSelected = (ComboDBItem) neighborsCombobox.getSelectedItem();
				ComboDBItem heathingSelected = (ComboDBItem) heathingCombobox.getSelectedItem();
				ComboDBItem typeSelected = (ComboDBItem) typeCombobox.getSelectedItem();
				
				EstatesController EstatesController = new EstatesController();
				try {
					EstatesController.setEstate(headerField.getText(), descriptionField.getText(), typeSelected.ElementID, priceField.getText(), bathField.getText(), netAreaField.getText(), grossAreaField.getText(), roomCountField.getText(), floorField.getText(), heathingSelected.ElementID, provincesSelected.ElementID, districtsSelected.ElementID, neighborsSelected.ElementID, UserSessionData.UserID);
					JOptionPane.showMessageDialog(mainFrame, "Kayıt başarıyla oluşturuldu.");
					refreshEmlakIslemleri();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
			}
		});
		JButton btnNewButton = new JButton("İptal");

		btnNewButton.setBounds(500, 885, 150, 25);
		emlakEkleme.add(btnNewButton);

		emlakEklemeScrollPane.setViewportView(emlakEkleme);
		return emlakEklemeScrollPane;
	}
	public void EmlakFiltreleme(){
		MainFrame mainFrame = new MainFrame(500, 600);
		JPanel mainPanel = new JPanel();

		mainFrame.getContentPane().setBackground(Color.WHITE);
		mainFrame.getContentPane().setLayout(null);
		
		mainPanel.setBounds(0, 0, 600, 575);
		mainFrame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JLabel headerLabel = new JLabel("Emlak Filtreleme");
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setBounds(187, 29, 225, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		mainPanel.add(headerLabel);
		
		JLabel minPriceIcon = new JLabel();
		minPriceIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/price_icon.png")));
		minPriceIcon.setBounds(301, 113, 35, 35);
		mainPanel.add(minPriceIcon);
		
		CustomTextField minPriceField = new CustomTextField("Fiyat (Minimum)",mainPanel);
		minPriceField.setBounds(348, 116, 220, 30);
		if(EstateModel.PriceStart != -1) minPriceField.setText(new Integer(EstateModel.PriceStart).toString());
		mainPanel.add(minPriceField);
		
		JLabel maxPriceIcon = new JLabel();
		maxPriceIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/price_icon.png")));
		maxPriceIcon.setBounds(301, 161, 35, 35);
		mainPanel.add(maxPriceIcon);
		
		CustomTextField maxPriceField = new CustomTextField("Fiyat (Maksimum)", mainPanel);
		maxPriceField.setBounds(348, 161, 220, 30);
		if(EstateModel.PriceEnd != -1) maxPriceField.setText(new Integer(EstateModel.PriceEnd).toString());
		mainPanel.add(maxPriceField);
		
		JLabel typeIcon = new JLabel();
		typeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		typeIcon.setBounds(301, 208, 35, 35);
		mainPanel.add(typeIcon);
		
		JComboBox<ComboDBItem> typeCombobox = new JComboBox<ComboDBItem>();
		typeCombobox.setBackground(Color.WHITE);
		typeCombobox.addItem(new ComboDBItem(-1,"Lütfen bir ilan türü seçiniz."));
		typeCombobox.addItem(new ComboDBItem(0,"Kiralık"));
		typeCombobox.addItem(new ComboDBItem(1,"Satılık"));		
		typeCombobox.setBounds(348, 215, 220, 24);
		if(EstateModel.EstateType != -1) typeCombobox.setSelectedIndex(EstateModel.EstateType);
		mainPanel.add(typeCombobox);
		
		JLabel provinceIcon = new JLabel();
		provinceIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
		provinceIcon.setBounds(301, 256, 35, 35);
		mainPanel.add(provinceIcon);
		
		JComboBox<ComboDBItem> provincesCombobox = new JComboBox<ComboDBItem>();
		provincesCombobox.setBounds(348, 260, 220, 24);
		provincesCombobox.addItem(new ComboDBItem(-1,"Filtrelemek için bir il seçiniz."));
		ResultSet Provinces = LocationModel.getProvinces();
		try {
			while(Provinces.next()) {
				ComboDBItem provinceItem = new ComboDBItem(Provinces.getInt("ProvinceID") ,Provinces.getString("ProvinceName"));
				provincesCombobox.addItem(provinceItem);
				if(EstateModel.ProvinceID != -1 && Provinces.getInt("ProvinceID") == EstateModel.ProvinceID) {
					provincesCombobox.setSelectedItem(provinceItem);
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		mainPanel.add(provincesCombobox);
		
		JLabel districtIcon = new JLabel();
		districtIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
		districtIcon.setBounds(301, 303, 35, 35);
		mainPanel.add(districtIcon);
		
		JComboBox<ComboDBItem> districtsCombobox = new JComboBox<ComboDBItem>();
		districtsCombobox.setBounds(348, 306, 220, 24);
		districtsCombobox.addItem(new ComboDBItem(-1,"Filtrelemek için bir ilçe seçiniz."));
		if(EstateModel.ProvinceID != -1) {
			ResultSet Districts = LocationModel.getDistricts(EstateModel.ProvinceID);
			try {
				while(Districts.next()) {
					ComboDBItem districtItem = new ComboDBItem(Districts.getInt("DistrictID") ,Districts.getString("DistrictName"));
					districtsCombobox.addItem(districtItem);
					if(EstateModel.DistrictID != -1 && Districts.getInt("DistrictID") == EstateModel.DistrictID) {
						districtsCombobox.setSelectedItem(districtItem);
					}
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		provincesCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				locker = true;
				ComboDBItem SelectedItem = (ComboDBItem) provincesCombobox.getSelectedItem();
				if(SelectedItem.ElementID == -1) return;
				try {
					ResultSet Districts = LocationModel.getDistricts(SelectedItem.ElementID);
					districtsCombobox.removeAllItems();
					districtsCombobox.addItem(new ComboDBItem(-1,"Filtrelemek için bir ilçe seçiniz."));
					while(Districts.next()) {
						districtsCombobox.addItem(new ComboDBItem(Districts.getInt("DistrictID") ,Districts.getString("DistrictName")));
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				locker = false;
			}
		});
		mainPanel.add(districtsCombobox);

		JButton filterButton = new JButton("Filtrele");
		filterButton.setForeground(new Color(255, 255, 255));
		filterButton.setBackground(new Color(0, 0, 51));
		filterButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		filterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EstateModel.ResetFilter();
				ComboDBItem selectedProvince = (ComboDBItem) provincesCombobox .getSelectedItem();
				ComboDBItem selectedDistrict = (ComboDBItem) districtsCombobox.getSelectedItem();
				ComboDBItem selectedType = (ComboDBItem) typeCombobox.getSelectedItem();
				if(maxPriceField.getText().isEmpty()) {
					EstateModel.PriceEnd = -1;
				}else if(Pattern.matches("^[0-9]+$", maxPriceField.getText())){
					EstateModel.PriceEnd = Integer.parseInt(maxPriceField.getText());
				}else{
					JOptionPane.showMessageDialog(mainFrame, "Lütfen geçerli bir maksimum fiyat değeri giriniz.");
				}
				if(minPriceField.getText().isEmpty()) {
					EstateModel.PriceStart = -1;
				}else if(Pattern.matches("^[0-9]+$", minPriceField.getText())){
					EstateModel.PriceStart = Integer.parseInt(minPriceField.getText());
				}else{
					JOptionPane.showMessageDialog(mainFrame, "Lütfen geçerli bir minumum fiyat değeri giriniz.");
				}
				EstateModel.EstateType = selectedType.ElementID;
				EstateModel.ProvinceID = selectedProvince.ElementID;
				EstateModel.DistrictID = selectedDistrict.ElementID;
				refreshEmlakIslemleri();
			}
		});
		filterButton.setBounds(301, 368, 123, 25);
		mainPanel.add(filterButton);
		
		JButton cancelButton = new JButton("Filtreleri Temizle");
		cancelButton.setForeground(new Color(255, 255, 255));
		cancelButton.setBackground(new Color(0, 0, 51));
		cancelButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				minPriceField.setText("");
				typeCombobox.setSelectedIndex(0);
				provincesCombobox.setSelectedIndex(0);
				districtsCombobox.setSelectedIndex(0);
				maxPriceField.setText("");
			}
		});
		cancelButton.setBounds(445, 368, 123, 25);
		mainPanel.add(cancelButton);
		
		JLabel leftHomeIcon = new JLabel();
		leftHomeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/filter.png")));
		leftHomeIcon.setBounds(22, 116, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		JLabel backgroundImage = new JLabel();
		backgroundImage.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/bg_middle.png")));
		backgroundImage.setBounds(0, 0, 600, 575);
		mainPanel.add(backgroundImage);
		
		mainFrame.addWindowListener(new WindowAdapter(){
	         public void windowOpened(WindowEvent windowEvent){
	        	 cancelButton.requestFocus();
	         }        
	    });
		mainFrame.setVisible(true);
	}
	public void SifreGuncelleme() {
		MainFrame mainFrame = new MainFrame(350, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Şifre Güncelleme");
		JLabel usernameIcon = new JLabel();
		JLabel passwordIcon = new JLabel();
		CustomPasswordField passwordField = new CustomPasswordField("Yeni Şifre", mainPanel);
		CustomPasswordField repasswordField = new CustomPasswordField("Şifre Tekrar", mainPanel);
		JButton registerButton = new JButton("Güncelle");
		JButton loginButton = new JButton("İptal");
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
		headerLabel.setBounds(181, 8, 228, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		mainPanel.add(headerLabel);
		
		usernameIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/password.png")));
		usernameIcon.setBounds(304, 114, 35, 35);
		mainPanel.add(usernameIcon);
		
		passwordIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon.setBounds(304, 162, 35, 35);
		mainPanel.add(passwordIcon);
		
		repasswordField.setBounds(351, 117, 220, 30);
		mainPanel.add(repasswordField);
		
		passwordField.setColumns(10);
		passwordField.setOpaque(false);
		passwordField.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode("#034E5E")));
		passwordField.setBounds(351, 165, 220, 30);
		mainPanel.add(passwordField);
		
		registerButton.setForeground(new Color(255, 255, 255));
		registerButton.setBackground(new Color(0, 0, 51));
		registerButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					UsersController UserController = new UsersController();
					UserController.UpdatePassword(UserSessionData.UserID, new String(passwordField.getPassword()),new String(repasswordField.getPassword()));
					JOptionPane.showMessageDialog(mainFrame, "Şifre başarıyla düzenlendi.");
				} catch (Exception e) {
					
					JOptionPane.showMessageDialog(mainFrame, e.getMessage());
				}
			}
		});
		registerButton.setBounds(304, 215, 123, 25);
		mainPanel.add(registerButton);

		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setBackground(new Color(0, 0, 51));
		loginButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		loginButton.setBounds(448, 215, 123, 25);
		mainPanel.add(loginButton);
		
		leftHomeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/synchronize.png")));
		leftHomeIcon.setBounds(12, 49, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		backgroundImage.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/bg_min.png")));
		backgroundImage.setBounds(0, 0, 600, 325);
		mainPanel.add(backgroundImage);

		mainFrame.setVisible(true);
	}
	public void EmailGuncelleme(){
		MainFrame mainFrame = new MainFrame(350, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("E-Mail Güncelleme");
		JLabel usernameIcon = new JLabel();
		CustomTextField mailField = new CustomTextField("Mail Adresiniz", mainPanel);
		JButton registerButton = new JButton("Tanımla");
		JButton loginButton = new JButton("İptal");
		JLabel leftHomeIcon = new JLabel();
		JLabel backgroundImage = new JLabel();
		mailField.setText(UserSessionData.Email);
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
		headerLabel.setBounds(180, 12, 247, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		mainPanel.add(headerLabel);
		
		usernameIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/username.png")));
		usernameIcon.setBounds(304, 107, 35, 35);
		mainPanel.add(usernameIcon);
	
		mailField.setBounds(351, 110, 220, 30);
		mainPanel.add(mailField);
		
		registerButton.setForeground(new Color(255, 255, 255));
		registerButton.setBackground(new Color(0, 0, 51));
		registerButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					UsersController UserController = new UsersController();

					UserController.UpdateMail(UserSessionData.UserID, mailField.getText());

					// UserID, Name, Surname, Password, Phone, Email, Province, District, Neighborhood
					JOptionPane.showMessageDialog(mainFrame, "Mail adresi başarıyla düzenlendi.");
				} catch (Exception e1) {
					
					JOptionPane.showMessageDialog(mainFrame, e1.getMessage());
				}			}
		});
		registerButton.setBounds(304, 178, 123, 25);
		mainPanel.add(registerButton);

		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setBackground(new Color(0, 0, 51));
		loginButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		loginButton.setBounds(448, 178, 123, 25);
		mainPanel.add(loginButton);
		
		leftHomeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/sync.png")));
		leftHomeIcon.setBounds(12, 49, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		backgroundImage.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/bg_min.png")));
		backgroundImage.setBounds(0, 0, 600, 325);
		mainPanel.add(backgroundImage);

		mainFrame.setVisible(true);
	}
	public void LisansIslemleri() {
		Date Datetimex = null;
		String License = "";
		String Datestring = "";
		LicensesModel LicenseModel = new LicensesModel();
		ResultSet LicenseData = LicenseModel.getLicenseByUserID(UserSessionData.UserID);
		try {
			if(LicenseData.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Datetimex = dateFormat.parse( LicenseData.getString("Datetime"));
				dateFormat.applyPattern("dd.MM.yyyy");
				Datestring = dateFormat.format(Datetimex);
				License = LicenseData.getString("License");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		MainFrame mainFrame = new MainFrame(350, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Lisans İşlemleri");
		JLabel passwordIcon = new JLabel();
		CustomTextField licenseField = new CustomTextField("Lisans Anahtarı", mainPanel);
		JButton registerButton = new JButton("Güncelle");
		JButton loginButton = new JButton("İptal");
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
		headerLabel.setBounds(198, 8, 208, 29);
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		mainPanel.add(headerLabel);
		
		passwordIcon.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/password.png")));
		passwordIcon.setBounds(309, 153, 35, 35);
		mainPanel.add(passwordIcon);
		
		licenseField.setColumns(10);
		licenseField.setBounds(352, 153, 220, 30);
		licenseField.setOpaque(false);

		mainPanel.add(licenseField);
		
		registerButton.setForeground(new Color(255, 255, 255));
		registerButton.setBackground(new Color(0, 0, 51));
		registerButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));

		registerButton.setBounds(305, 218, 123, 25);
		mainPanel.add(registerButton);

		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setBackground(new Color(0, 0, 51));
		loginButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		loginButton.setBounds(449, 218, 123, 25);
		mainPanel.add(loginButton);
		
		JLabel lblNewLabel = new JLabel("Onay");
		lblNewLabel.setBounds(200, 125, 500, 15);
		mainPanel.add(lblNewLabel);
		
		leftHomeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/synchronize.png")));
		leftHomeIcon.setBounds(12, 49, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		backgroundImage.setIcon(new ImageIcon(GirisEkrani.class.getResource("/assets/images/bg_min.png")));
		backgroundImage.setBounds(0, 0, 600, 325);
		mainPanel.add(backgroundImage);
		if(License.isEmpty() || Datetimex == (Date) null) {
			lblNewLabel.setVisible(false);
			registerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					LicensesController LicenseController = new LicensesController();
					try {
						LicenseController.updateLicenseUser(licenseField.getText(),UserSessionData.UserID);
						JOptionPane.showMessageDialog(mainFrame, "Lisansınız başarıyla aktive edildi. Lütfen uygulamayı kapatıp tekrar açınız.");
					} catch (Exception e) {
						
						JOptionPane.showMessageDialog(mainFrame, e.getMessage());
					}
				}
			});
		}else if(Datetimex.before(new Date())){
			lblNewLabel.setBounds(220, 125, 500, 15);
			lblNewLabel.setText(Datestring+" tarihinde lisansınız sona ermiştir.");
			lblNewLabel.setForeground(Color.red);

			registerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					LicensesController LicenseController = new LicensesController();
					try {
						LicenseController.updateLicenseUser(licenseField.getText(),UserSessionData.UserID);
						JOptionPane.showMessageDialog(mainFrame, "Lisansınız başarıyla aktive edildi. Lütfen uygulamayı kapatıp tekrar açınız.");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, e.getMessage());
					}
				}
			});
		}else{
			lblNewLabel.setText(Datestring+" tarihine kadar lisansınız geçerlidir.");
			lblNewLabel.setForeground(Color.green);
			registerButton.setVisible(false);
			licenseField.setVisible(false);
			passwordIcon.setVisible(false);
		}
		mainFrame.setVisible(true);
	}
	public void EmlakGoruntuleme(String estateHeader,String estateDesc, String estateType, String estatePrice, String bathCount, String netArea, String grossArea, String roomCount, String floorCount, String heathingType, String Province, String District, String Neighborhood){
		MainFrame editUserFrame = new MainFrame(820, 600);
		JPanel mainPanel = new JPanel();
		JLabel headerLabel = new JLabel("Emlak Görüntüleme");
		headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		headerLabel.setBounds(158, 12, 264, 29);

		mainPanel.add(headerLabel);
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 600, 825);
		editUserFrame.getContentPane().add(mainPanel);
	
		mainPanel.setLayout(null);
		editUserFrame.getContentPane().add(mainPanel);

		JLabel headerFieldIcon = new JLabel("New label");
		headerFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		headerFieldIcon.setBounds(286, 65, 35, 35);
		mainPanel.add(headerFieldIcon);
		
		CustomTextField headerField = new CustomTextField("İlan Başlığı", mainPanel);
		headerField.setBounds(333, 69, 220, 30);
		headerField.setText(estateHeader);
		headerField.setEditable(false);
		mainPanel.add(headerField);
		
		JLabel descriptionFieldIcon = new JLabel("New label");
		descriptionFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		descriptionFieldIcon.setBounds(286, 117, 35, 35);
		mainPanel.add(descriptionFieldIcon);
		
		CustomTextArea descriptionField = new CustomTextArea("İlan Açıklaması", mainPanel);
		descriptionField.setBounds(333, 117, 220, 100);
		descriptionField.setText(estateDesc);
		descriptionField.setEditable(false);
		mainPanel.add(descriptionField);
		
		JLabel typeComboboxIcon = new JLabel("New label");
		typeComboboxIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		typeComboboxIcon.setBounds(286, 224, 35, 35);
		mainPanel.add(typeComboboxIcon);
		
		CustomTextField typeField = new CustomTextField("Type", (JPanel) mainPanel);
		typeField.setBounds(333, 224, 220, 30);
		typeField.setText(estateType);
		typeField.setEditable(false);
		mainPanel.add(typeField);
		
		JLabel priceFieldIcon = new JLabel("New label");
		priceFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/price_icon.png")));
		priceFieldIcon.setBounds(286, 265, 35, 35);
		mainPanel.add(priceFieldIcon);
		
		CustomTextField priceField = new CustomTextField("Fiyat", mainPanel);
		priceField.setBounds(333, 268, 220, 30);
		priceField.setText(estatePrice);
		priceField.setEditable(false);

		mainPanel.add(priceField);
		
		JLabel bathFieldIcon = new JLabel("New label");
		bathFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		bathFieldIcon.setBounds(286, 309, 35, 35);
		mainPanel.add(bathFieldIcon);
		
		CustomTextField bathField = new CustomTextField("Banyo Sayısı", mainPanel);
		bathField.setBounds(333, 312, 220, 30);
		bathField.setText(bathCount);
		bathField.setEditable(false);
		mainPanel.add(bathField);
		
		JLabel netAreaFieldIcon = new JLabel("New label");
		netAreaFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		netAreaFieldIcon.setBounds(286, 354, 35, 35);
		mainPanel.add(netAreaFieldIcon);
		
		CustomTextField netAreaField = new CustomTextField("Net Alan (m²)", mainPanel);
		netAreaField.setBounds(333, 357, 220, 30);
		netAreaField.setText(netArea);
		netAreaField.setEditable(false);
		mainPanel.add(netAreaField);
		
		JLabel grossAreaFieldIcon = new JLabel("New label");
		grossAreaFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		grossAreaFieldIcon.setBounds(286, 399, 35, 35);
		mainPanel.add(grossAreaFieldIcon);
		
		CustomTextField grossAreaField = new CustomTextField("Brüt Alan (m²)", mainPanel);
		grossAreaField.setBounds(333, 402, 220, 30);
		grossAreaField.setText(grossArea);
		grossAreaField.setEditable(false);
		mainPanel.add(grossAreaField);
		
		JLabel roomCountFieldIcon = new JLabel("New label");
		roomCountFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		roomCountFieldIcon.setBounds(286, 444, 35, 35);
		mainPanel.add(roomCountFieldIcon);
		
		CustomTextField roomCountField = new CustomTextField("Oda Sayısı", mainPanel);
		roomCountField.setBounds(333, 447, 220, 30);
		roomCountField.setText(roomCount);
		roomCountField.setEditable(false);
		mainPanel.add(roomCountField);
		
		JLabel floorFieldIcon = new JLabel("New label");
		floorFieldIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		floorFieldIcon.setBounds(286, 489, 35, 35);
		mainPanel.add(floorFieldIcon);
		
		CustomTextField floorField = new CustomTextField("Kat Bilgisi", mainPanel);
		floorField.setBounds(333, 492, 220, 30);
		floorField.setText(floorCount);
		floorField.setEditable(false);
		mainPanel.add(floorField);
		
		JLabel heathingComboboxIcon= new JLabel("New label");
		heathingComboboxIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/estate_icon.png")));
		heathingComboboxIcon.setBounds(286, 534, 35, 35);
		mainPanel.add(heathingComboboxIcon);
		
		JLabel provinceIcon = new JLabel("New label");
		provinceIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
		provinceIcon.setBounds(286, 581, 35, 35);
		mainPanel.add(provinceIcon);

		
		JLabel districtIcon = new JLabel("New label");
		districtIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
		districtIcon.setBounds(286, 628, 35, 35);
		mainPanel.add(districtIcon);

		
		JLabel neighborhoodIcon = new JLabel("New label");
		neighborhoodIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/map.png")));
		neighborhoodIcon.setBounds(286, 675, 35, 35);
		mainPanel.add(neighborhoodIcon);

		CustomTextField heathingField = new CustomTextField("Kat Bilgisi", (JPanel) mainPanel);
		heathingField.setText(heathingType);
		heathingField.setBounds(333, 534, 220, 30);
		heathingField.setEditable(false);

		mainPanel.add(heathingField);
		
		CustomTextField provinceField = new CustomTextField("Kat Bilgisi", (JPanel) mainPanel);
		provinceField.setText(Province);
		provinceField.setBounds(333, 581, 220, 30);
		provinceField.setEditable(false);

		mainPanel.add(provinceField);
		
		CustomTextField districtField = new CustomTextField("Kat Bilgisi", (JPanel) mainPanel);
		districtField.setText(District);
		districtField.setBounds(333, 628, 220, 30);
		districtField.setEditable(false);

		mainPanel.add(districtField);
		
		CustomTextField neighborhoodField = new CustomTextField("Kat Bilgisi", (JPanel) mainPanel);
		neighborhoodField.setText(Neighborhood);
		neighborhoodField.setBounds(333, 675, 220, 30);
		neighborhoodField.setEditable(false);

		mainPanel.add(neighborhoodField);
		
		JButton cancelButton = new JButton("Kapat");
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBorder(new LineBorder(Color.decode("#FFFFFF"),1));
		cancelButton.setBackground(new Color(0, 0, 51));
		cancelButton.setBounds(430, 737, 123, 25);
		mainPanel.add(cancelButton);
		
		JLabel leftHomeIcon = new JLabel();
		leftHomeIcon.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/apartment.png")));
		leftHomeIcon.setBounds(12, 194, 256, 256);
		mainPanel.add(leftHomeIcon);
		
		JLabel backgroundImage = new JLabel();
		backgroundImage.setIcon(new ImageIcon(YetkiliKullaniciEkrani.class.getResource("/assets/images/bg_max.png")));
		backgroundImage.setBounds(0, 0, 600, 825);
		mainPanel.add(backgroundImage);	
		editUserFrame.setVisible(true);
	}
}