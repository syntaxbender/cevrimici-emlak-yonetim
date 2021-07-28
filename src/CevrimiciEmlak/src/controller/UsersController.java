package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.*;

import model.*;
import view.AltYoneticiEkrani;
import view.NormalKullaniciEkrani;
import view.YetkiliKullaniciEkrani;
import view.YoneticiEkrani;

public class UsersController {
	UsersModel UserModel = new UsersModel();
	public UsersModel Login(String Username, String Password) throws Exception{
	
		UserModel.LoginStatus = false;

		ResultSet UserData = UserModel.getUserByUsername(Username);
		if (UserData.next() == false || Password.equals(UserData.getString("Password")) == false || UserData.getInt("VerifyStatus") != 1) throw new Exception("Hatalı bilgiler giriş başarısız.");

		UserModel.LoginStatus = true;
		UserModel.RoleID = UserData.getInt("RoleID");
		UserModel.UserID = UserData.getInt("UserID");
		UserModel.VerifyStatus = UserData.getInt("VerifyStatus");
		UserModel.Username = UserData.getString("Username");
		UserModel.Password = UserData.getString("Password");
		UserModel.Phone = UserData.getString("Phone");
		UserModel.Email = UserData.getString("Email");
		UserModel.Name = UserData.getString("Name");
		UserModel.Surname = UserData.getString("Surname");
		UserModel.ProvinceID = UserData.getInt("ProvinceID");
		UserModel.DistrictID = UserData.getInt("DistrictID");
		UserModel.NeighborhoodID = UserData.getInt("NeighborhoodID");
		
		if(UserModel.RoleID == 1) { // yonetici
			new YoneticiEkrani(UserModel);
		}else if(UserModel.RoleID == 2) { // alt yonetici
			String Zone;
			int ZoneID;
			if(UserModel.NeighborhoodID != -1) {
				Zone = "NeighborhoodID";
				ZoneID = UserModel.NeighborhoodID;
			}else if(UserModel.DistrictID != -1) {
				Zone = "DistrictID";
				ZoneID = UserModel.DistrictID;
			}else{
				Zone = "ProvinceID";
				ZoneID = UserModel.ProvinceID;
			}
			new AltYoneticiEkrani(UserModel,Zone,ZoneID);

		}else if(UserModel.RoleID == 3) { // yetkili
			new YetkiliKullaniciEkrani(UserModel);
		}else if(UserModel.RoleID == 4) { // kullanici
			new NormalKullaniciEkrani(UserModel);
		}

		return UserModel;
	}
	public void CreateUser(String Username, String Password, String RePassword,  String Phone, String Email, String Name, String Surname, int Province, int District, int Neighborhood) throws Exception{
		ResultSet UserDataByMail = UserModel.getUserByMail(Email);
		ResultSet UserDataByUsername = UserModel.getUserByUsername(Username);
		try {
			if(UserDataByMail.next()) throw new Exception("Mail adresi ile daha önceden kayıt olunmuş. Lütfen giriş yapınız.");
			if(UserDataByUsername.next()) throw new Exception("Kullanıcı adı ile daha önceden kayıt olunmuş. Farklı bir kullanıcı ile tekrar deneyiniz.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean checkName = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Name);
		boolean checkSurname = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Surname);
		boolean checkMail = Pattern.matches("^.+@.+\\..+$", Email);
		boolean checkPhone = Pattern.matches("^05[0-9]{9}$", Phone);
		boolean checkUsername = Pattern.matches("^[a-z0-9.]{3,16}$", Username);
		boolean checkPassword = Pattern.matches("^.{6,16}$", Password);
		if(Password.equals(RePassword) == false) throw new Exception("Girdiğiniz şifreler birbiri ile uyuşmuyor.");
		if(checkName == false) throw new Exception("Kullanıcı adınız yalnızca 3 - 16 karakter aralığında, ingilizce alfabede küçük harflerden oluşabilir.");
		if(checkSurname == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");
		if(checkMail == false) throw new Exception("Lütfen geçerli bir mail adresi giriniz.");
		if(checkPhone == false) throw new Exception("Lütfen geçerli bir telefon numarası giriniz.");
		if(checkUsername == false) throw new Exception("Kullanıcı adınız yalnızca 3 - 16 karakter aralığında, ingilizce alfabede küçük harflerden oluşabilir.");
		if(checkPassword == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");
		if(Province == -1 || District == -1 || Neighborhood == -1) throw new Exception("İkamet bölgenizi il, ilçe, mahalle/belde/köy olarak eksik olarak tanımlayınız.");
		if(UserModel.setUser(4, 0, Username, Password, Phone, Email, Name, Surname, Province, District, Neighborhood) == false) throw new Exception("Kayıt olurken bilinmeyen bir hata oluştu.");
		
	}
	public void UpdatePassword(int UserID, String Password, String RePassword) throws Exception {
		boolean checkPassword = Pattern.matches("^.{6,16}$", Password);
		if(checkPassword == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");
		if(Password.equals(RePassword) == false) throw new Exception("Girdiğiniz şifreler birbiri ile uyuşmuyor.");
		if(UserModel.updatePassword(UserID, Password) == false) throw new Exception("Şifre güncellenirken bilinmeyen bir hata oluştu.");
	}
	public void UpdateMail(int UserID, String Email) throws Exception {
		boolean checkMail = Pattern.matches("^.+@.+\\..+$", Email);
		if(checkMail == false) throw new Exception("Lütfen geçerli bir mail adresi giriniz.");
		if(UserModel.updateMail(UserID, Email) == false) throw new Exception("Mail güncellenirken bilinmeyen bir hata oluştu.");
	}
	public void CreateSubAdmin(int RoleID,int VerifyStatus, String Username, String Password, String RePassword,  String Phone, String Email, String Name, String Surname, int Province, int District, int Neighborhood) throws Exception{
		ResultSet UserDataByMail = UserModel.getUserByMail(Email);
		ResultSet UserDataByUsername = UserModel.getUserByUsername(Username);
		try {
			if(UserDataByMail.next()) throw new Exception("Mail adresi ile daha önceden kayıt olunmuş. Lütfen giriş yapınız.");
			if(UserDataByUsername.next()) throw new Exception("Kullanıcı adı ile daha önceden kayıt olunmuş. Farklı bir kullanıcı ile tekrar deneyiniz.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean checkName = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Name);
		boolean checkSurname = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Surname);
		boolean checkMail = Pattern.matches("^.+@.+\\..+$", Email);
		boolean checkPhone = Pattern.matches("^05[0-9]{9}$", Phone);
		boolean checkUsername = Pattern.matches("^[a-z0-9.]{3,16}$", Username);
		boolean checkPassword = Pattern.matches("^.{6,16}$", Password);
		if(Password.equals(RePassword) == false) throw new Exception("Girdiğiniz şifreler birbiri ile uyuşmuyor.");
		if(checkName == false) throw new Exception("Kullanıcı adınız yalnızca 3 - 16 karakter aralığında, ingilizce alfabede küçük harflerden oluşabilir.");
		if(checkSurname == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");
		if(checkMail == false) throw new Exception("Lütfen geçerli bir mail adresi giriniz.");
		if(checkPhone == false) throw new Exception("Lütfen geçerli bir telefon numarası giriniz.");
		if(checkUsername == false) throw new Exception("Kullanıcı adınız yalnızca 3 - 16 karakter aralığında, ingilizce alfabede küçük harflerden oluşabilir.");
		if(checkPassword == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");

		//if(Province == -1 || Neighborhood == -1 || District == -1) return false;
		if(Province == -1) throw new Exception("Hizmet bölgesi ili seçilmesi zorunludur.");
		if(UserModel.setUser(RoleID, VerifyStatus, Username, Password, Phone, Email, Name, Surname, Province, District, Neighborhood) == false) throw new Exception("Kayıt olurken bilinmeyen bir hata oluştu.");
	}
	public void editSubAdmin(int UserID, String Name, String Surname, String Password, String RePassword, String Phone, String Email, int Province, int District, int Neighborhood) throws Exception {
			ResultSet UserDataByMail = UserModel.getUserByMail(Email);
			try {
				if(UserDataByMail.next()) {
					if(UserDataByMail.getInt("UserID") != UserID) throw new Exception("Mail adresi ile daha önceden kayıt olunmuş. Lütfen giriş yapınız.");
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}

		boolean checkName = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Name);
		boolean checkSurname = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Surname);
		boolean checkMail = Pattern.matches("^.+@.+\\..+$", Email);
		boolean checkPhone = Pattern.matches("^05[0-9]{9}$", Phone);
		boolean checkPassword = Pattern.matches("^.{6,16}$", Password);
		if(Password.equals(RePassword) == false) throw new Exception("Girdiğiniz şifreler birbiri ile uyuşmuyor.");
		if(checkMail == false) throw new Exception("Lütfen geçerli bir mail adresi giriniz.");
		if(checkPhone == false) throw new Exception("Lütfen geçerli bir telefon numarası giriniz.");
		if(checkPassword == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");

		//if(Province == -1 || Neighborhood == -1 || District == -1) return false;
		if(Province == -1) throw new Exception("Hizmet bölgesi ili seçilmesi zorunludur.");
		if(UserModel.updateSubAdmin(UserID, Name, Surname, Password, Phone, Email, Province, District, Neighborhood) == false) throw new Exception("İşlem başarısız. Bilinmeyen bir hata oluştu."); 
	}
	public void editUser(int UserID,  int VerifyStatus, String Name, String Surname, String Password, String RePassword, String Phone, String Email, int Province, int District, int Neighborhood) throws Exception {
			ResultSet UserDataByMail = UserModel.getUserByMail(Email);
			try {
				if(UserDataByMail.next()) {
					int Usermailid = UserDataByMail.getInt("UserID");
					if(UserDataByMail.getInt("UserID") != UserID) throw new Exception("Mail adresi ile daha önceden kayıt olunmuş. Lütfen giriş yapınız.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			boolean checkName = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Name);
			boolean checkSurname = Pattern.matches("^[a-zA-ZĞÜŞİÖÇğüşıöç]+$", Surname);
			boolean checkMail = Pattern.matches("^.+@.+\\..+$", Email);
			boolean checkPhone = Pattern.matches("^05[0-9]{9}$", Phone);
			boolean checkPassword = Pattern.matches("^.{6,16}$", Password);
			if(Password.equals(RePassword) == false) throw new Exception("Girdiğiniz şifreler birbiri ile uyuşmuyor.");
			if(checkMail == false) throw new Exception("Lütfen geçerli bir mail adresi giriniz.");
			if(checkPhone == false) throw new Exception("Lütfen geçerli bir telefon numarası giriniz.");
			if(checkPassword == false) throw new Exception("Şifreniz en az 6, en çok 16 karakterden oluşabilir.");

			//if(Province == -1 || Neighborhood == -1 || District == -1) return false;
			if(Province == -1) throw new Exception("Hizmet bölgesi ili seçilmesi zorunludur.");
			if(UserModel.updateUser(UserID, VerifyStatus, Name, Surname, Password, Phone, Email, Province, District, Neighborhood) == false) throw new Exception("İşlem başarısız. Bilinmeyen bir hata oluştu."); 
		
	}
}
