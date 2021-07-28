package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LicensesModel extends Model{
	public boolean setLicense(int setByID,String License, String Datetime) {
		try{
		    PreparedStatement p = con.prepareStatement("insert into Licenses set setByID=?,License=?,Datetime=?,isUsed=0");
		    p.setInt(1, setByID);
		    p.setString(2, License);
		    p.setString(3, Datetime);
		    p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateLicenseUser(String License, int UserID) {
		try{
		    PreparedStatement p = con.prepareStatement("update Licenses set UserID=?, isUsed=1 where License=?");
		    p.setInt(1, UserID);
		    p.setString(2, License);
		    p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateLicense(int LicenseID, String License, String Datetime) {
		try{
		    PreparedStatement p = con.prepareStatement("update Licenses set License=?, Datetime=? where LicenseID=?");
		    p.setString(1, License);
		    p.setString(2, Datetime);
		    p.setInt(3, LicenseID);
		    p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public ResultSet getAllActiveLicenses() {
		try{
			this.rs=stmt.executeQuery("select * from Licenses inner join Users on Users.UserID=Licenses.UserID where Licenses.isUsed=1");  
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getAllActiveLicensesByUserID(int UserID) {
		try{
			PreparedStatement p = con.prepareStatement("select * from Licenses inner join Users on Users.UserID=Licenses.UserID where Licenses.isUsed=1 and setByID = ?");
		    p.setInt(1, UserID);
		    this.rs = p.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getActiveLicensesByProvince(int ProvinceID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Licenses inner join Users on Users.UserID = Licenses.UserID where Users.ProvinceID=? and Licenses.isUsed=1");
		    p.setInt(1, ProvinceID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getLicenseByLicense(String License) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Licenses where License = ?");
		    p.setString(1, License);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getLicenseByID(int LicenseID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Licenses where LicenseID = ?");
		    p.setInt(1, LicenseID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getLicenseByUserID(int UserID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Licenses where UserID = ? order by Datetime desc");
		    p.setInt(1, UserID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getInactiveLicenses() {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Licenses where isUsed is null or isUsed = 0");
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getInactiveLicensesByUserID(int setByID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Licenses where isUsed is null or isUsed = 0 and setByID = ?");
		    p.setInt(1, setByID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	
}
