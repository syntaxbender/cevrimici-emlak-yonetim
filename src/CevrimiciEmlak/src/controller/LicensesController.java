package controller;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import model.*;
public class LicensesController {
	LicensesModel LicenseModel = new LicensesModel();
	public String generateLicense() {
		String key = "";
		for (int i = 0; i < 3; i++) {
			for (int i2 = 0; i2 < 5; i2++) {
				int charOrInt = (int)Math.round(Math.random());
				if(charOrInt == 0) { // char
					key += Character.toString((char) (((int)Math.round(Math.random()*25))+65));
				}else { // int
					key += Integer.toString((int)Math.round(Math.random()*9));
				}
			}
			if(i == 2) break;
			key += "-";
		}
		return key;
	}
	public void setLicense(int setByID,String License, String Datetime) throws Exception {
		ResultSet LicenseData = LicenseModel.getLicenseByLicense(License);
		if(LicenseData.next()) throw new Exception("Türetilen lisans anahtarı sistemde tanımlıdır. Lütfen yeni bir anahtar oluşturunuz.");
		boolean checkDate = Pattern.matches("^(19([0-9]{2})|20([0-9]{2}))-(0([1-9])|1([012]))-((0[1-9])|([12][0-9])|(3[01])) (((1|0)([0-9]))|2([0-3])):([0-5][0-9]):([0-5][0-9])$", Datetime); // 2021-05-30 23:59:59
		if(checkDate == false) throw new Exception("Lütfen takvimden geçerli bir tarih seçiniz.");
		if(LicenseModel.setLicense(setByID,License,Datetime) == false) throw new Exception("Lisans anahtarı sisteme tanımlanırken bilinmeyen bir hata oluştu.");
	}
	public void updateLicense(int LicenseID,String License, String Datetime) throws Exception {
		ResultSet LicenseData = LicenseModel.getLicenseByLicense(License);
		if(LicenseData.next() && LicenseData.getInt("LicenseID") != LicenseID) throw new Exception("Türetilen lisans anahtarı sistemde tanımlıdır. Lütfen yeni bir anahtar oluşturunuz.");
		boolean checkDate = Pattern.matches("^(19([0-9]{2})|20([0-9]{2}))-(0([1-9])|1([012]))-((0[1-9])|([12][0-9])|(3[01])) (((1|0)([0-9]))|2([0-3])):([0-5][0-9]):([0-5][0-9])$", Datetime); // 2021-05-30 23:59:59
		if(checkDate == false) throw new Exception("Lütfen takvimden geçerli bir tarih seçiniz.");
		if(LicenseModel.updateLicense(LicenseID,License,Datetime) == false) throw new Exception("Lisans anahtarı sisteme tanımlanırken bilinmeyen bir hata oluştu.");
	}
	public void updateLicenseUser(String License, int UserID) throws Exception {
		
		ResultSet LicenseData = LicenseModel.getLicenseByLicense(License);
		if(LicenseData.next()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date Datetime = dateFormat.parse( LicenseData.getString("Datetime"));
			if(Datetime.before(new Date())) throw new Exception("Lisans anahtarınızın süresi geçmiştir. Varsa başka bir lisans anahtarı deneyiniz.");
		}else{
			throw new Exception("Lütfen lisans anahtarınızı kontrol ediniz.");
		}
		UsersModel UserModel = new UsersModel();
		if(UserModel.updateUserRole(UserID,3) == false) throw new Exception("Lisans anahtarı tanımlanırken bilinmeyen bir hata oluştu.");
		if(LicenseModel.updateLicenseUser(License, UserID) == false) throw new Exception("Lisans anahtarı tanımlanırken bilinmeyen bir hata oluştu.");

	}
}
