package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import model.EstatesModel;
import model.LicensesModel;
import model.UsersModel;
import view.YetkiliKullaniciEkrani;
import view.YoneticiEkrani;

public class EstatesController {
	UsersModel UserModel = new UsersModel();
	EstatesModel EstateModel = new EstatesModel();
	public void setEstate(String estateHeader,String estateDesc, int estateType, String estatePrice, String bathCount, String netArea, String grossArea, String roomCount, String floorCount, int heathingType, int provinceID, int districtID, int neighborhoodID, int setByUserID) throws Exception{
		Date Datetimex = null;
		String License = "";
		String Datestring = "";
		LicensesModel LicenseModel = new LicensesModel();
		ResultSet LicenseData = LicenseModel.getLicenseByUserID(setByUserID);
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
		
		if(Datetimex == (Date) null || Datetimex.before(new Date())) throw new Exception("Lisansınız sona erdiği için yeni ilan ekleyemezsiniz. Lütfen lisansınızı yenileyiniz.");
		
		boolean checkPrice = Pattern.matches("^[0-9]+$", estatePrice);
		boolean checkBath = Pattern.matches("^[0-9]+$", bathCount);
		boolean checkNetArea = Pattern.matches("^[0-9]+$", netArea);
		boolean checkGrossArea = Pattern.matches("^[0-9]+$", grossArea);
		boolean checkRoom = Pattern.matches("^[0-9]+$", roomCount);
		boolean checkFloor = Pattern.matches("^[0-9]+$", floorCount);

		if(checkPrice == false) throw new Exception("İlan fiyat alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkBath == false) throw new Exception("İlan banyo sayısı alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkNetArea == false) throw new Exception("İlan net alan alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkGrossArea == false) throw new Exception("İlan brüt alan alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkRoom == false) throw new Exception("İlan oda sayısı alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkFloor == false) throw new Exception("İlan kat bilgisi alanı yalnızca tamsayı rakamlardan oluşabilir.");
		
		if(provinceID == -1 || districtID == -1 || neighborhoodID == -1) throw new Exception("Emlak adresini il, ilçe, mahalle/belde/köy olarak eksik olarak tanımlayınız.");
		if(EstateModel.setEstate(estateHeader,estateDesc, estateType, Integer.parseInt(estatePrice), Integer.parseInt(bathCount), Integer.parseInt(netArea), Integer.parseInt(grossArea), Integer.parseInt(roomCount), Integer.parseInt(floorCount), heathingType, provinceID, districtID, neighborhoodID, setByUserID) == false) throw new Exception("İşlem başarısız. Bilinmeyen bir hata oluştu.");
	}
	public void editEstate(String estateHeader,String estateDesc, int estateType, String estatePrice, String bathCount, String netArea, String grossArea, String roomCount, String floorCount, int heathingType, int provinceID, int districtID, int neighborhoodID, int VerifyStatus, int EstateID) throws Exception{
		boolean checkPrice = Pattern.matches("^[0-9]+$", estatePrice);
		boolean checkBath = Pattern.matches("^[0-9]+$", bathCount);
		boolean checkNetArea = Pattern.matches("^[0-9]+$", netArea);
		boolean checkGrossArea = Pattern.matches("^[0-9]+$", grossArea);
		boolean checkRoom = Pattern.matches("^[0-9]+$", roomCount);
		boolean checkFloor = Pattern.matches("^[0-9]+$", floorCount);

		if(checkPrice == false) throw new Exception("İlan fiyat alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkBath == false) throw new Exception("İlan banyo sayısı alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkNetArea == false) throw new Exception("İlan net alan alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkGrossArea == false) throw new Exception("İlan brüt alan alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkRoom == false) throw new Exception("İlan oda sayısı alanı yalnızca tamsayı rakamlardan oluşabilir.");
		if(checkFloor == false) throw new Exception("İlan kat bilgisi alanı yalnızca tamsayı rakamlardan oluşabilir.");
		
		if(provinceID == -1 || districtID == -1 || neighborhoodID == -1) throw new Exception("Emlak adresini il, ilçe, mahalle/belde/köy olarak eksik olarak tanımlayınız.");
		if(EstateModel.updateEstate(estateHeader,estateDesc, estateType, Integer.parseInt(estatePrice), Integer.parseInt(bathCount), Integer.parseInt(netArea), Integer.parseInt(grossArea), Integer.parseInt(roomCount), Integer.parseInt(floorCount), heathingType, provinceID, districtID, neighborhoodID, VerifyStatus, EstateID) == false) throw new Exception("İşlem başarısız. Bilinmeyen bir hata oluştu.");
	}
	public ResultSet getFavorites(int UserID) {
		ResultSet FavPlain = UserModel.getFavorites(UserID);
		try {
			if(FavPlain.next()) {
				String FavPlainData = FavPlain.getString("Favorites");
				if(FavPlainData != null && FavPlainData.isEmpty() == false) {
					ArrayList<String> List = new ArrayList<String>(Arrays.asList(FavPlainData.split(",")));
					return EstateModel.getEstatesByIDs(List);
				}else{
					return null;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean setFavorite(int UserID, int EstateID) throws Exception {
		ResultSet FavPlain = UserModel.getFavorites(UserID);
		ResultSet EstateData = EstateModel.getEstateByID(EstateID);

		try {
			if(EstateData.next() && FavPlain.next()) {
				if(EstateData.getInt("UserID") == UserID) throw new Exception("Kendi ilanınızı favorilere ekleyemezsiniz."); 
				String Favorites = FavPlain.getString("Favorites");
				if(Favorites == null || Favorites.isEmpty())
					Favorites = Integer.toString(EstateID);
				else
					Favorites += ","+Integer.toString(EstateID);
				return UserModel.updateFavorites(UserID, Favorites);
			}else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean delFavorite(int UserID, int EstateID) {
		ResultSet FavPlain = UserModel.getFavorites(UserID);
		try {
			if(FavPlain.next()) {
				ArrayList<String> List = new ArrayList<String>(Arrays.asList(FavPlain.getString("Favorites").split(",")));
				if(List.indexOf(Integer.toString(EstateID)) == -1) return false;
				List.remove(Integer.toString(EstateID));
				String Favorites = "";
				for(int i = 0; i<List.size();i++) {
					Favorites += List.get(i);
					if(i!=(List.size()-1)) Favorites += ",";
				}
				return UserModel.updateFavorites(UserID, Favorites);
			}else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
