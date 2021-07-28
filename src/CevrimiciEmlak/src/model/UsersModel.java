package model;

import java.sql.*;
import java.util.ArrayList;

public class UsersModel extends Model {
	public boolean LoginStatus;
	public int UserID;
	public int RoleID;
	public int VerifyStatus;
	public String Username;
	public String Password;
	public String Phone;
	public String Email;
	public String Name;
	public String Surname;
	public int ProvinceID;
	public int DistrictID;
	public int NeighborhoodID;
	public String Favorites;
	public ResultSet getUserByUsername(String Username) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Users where Username = ?");
		    p.setString(1, Username);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getUserByID(int UserID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Users where UserID = ?");
		    p.setInt(1, UserID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getUsersByStatus(int VerifyStatus, int RoleID) {
		try{
		    PreparedStatement p = con.prepareStatement("select *,(IF(Users.VerifyStatus=1, \"Onaylı\", \"Onaysız\")) as VerifyStatusText from Users where VerifyStatus = ? and RoleID = ?");
		    p.setInt(1, VerifyStatus);
		    p.setInt(2, RoleID);

		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getUsersByStatusAndZone(int VerifyStatus, int RoleID, String Zone, int ZoneID) {
		try{
		    PreparedStatement p = con.prepareStatement("select *,(IF(Users.VerifyStatus=1, \"Onaylı\", \"Onaysız\")) as VerifyStatusText from Users where VerifyStatus = ? and RoleID = ? and "+Zone+" = ?");
		    p.setInt(1, VerifyStatus);
		    p.setInt(2, RoleID);
		    p.setInt(3, ZoneID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getUserByMail(String Email) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Users where Email = ?");
		    p.setString(1, Email);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public boolean setUser(int RoleID, int VerifyStatus, String Username, String Password, String Phone, String Email, String Name, String Surname, int Province, int District, int Neighborhood) {
		try{
		    PreparedStatement p = con.prepareStatement("insert into Users set RoleID=?, VerifyStatus=?, Username=?, Password=?, Phone=?, Email=?, Name=?, Surname=?, ProvinceID=?, DistrictID=?, NeighborhoodID=?");
		    p.setInt(1, RoleID);
		    p.setInt(2, VerifyStatus);
		    p.setString(3, Username);
		    p.setString(4, Password);
		    p.setString(5, Phone);
		    p.setString(6, Email);
		    p.setString(7, Name);
		    p.setString(8, Surname);
		    p.setInt(9, Province);
		    p.setInt(10, District);
		    p.setInt(11, Neighborhood);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateVerifyStatus(int UserID, int VerifyStatus) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set VerifyStatus = ? where UserID = ?");
		    p.setInt(1, VerifyStatus);
		    p.setInt(2, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public boolean updatePassword(int UserID, String Password) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set Password = ? where UserID = ?");
		    p.setString(1, Password);
		    p.setInt(2, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public boolean updateUserRole(int UserID, int RoleID) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set RoleID = ? where UserID = ?");
		    p.setInt(1, RoleID);
		    p.setInt(2, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public boolean updateMail(int UserID, String Mail) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set Email = ? where UserID = ?");
		    p.setString(1, Mail);
		    p.setInt(2, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public boolean updateSubAdmin(int UserID, String Name, String Surname, String Password, String Phone, String Email, int Province, int District, int Neighborhood) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set Password=?, Phone=?, Email=?, Name=?, Surname=?, ProvinceID=?, DistrictID=?, NeighborhoodID=? where UserID=?");
		    p.setString(1, Password);
		    p.setString(2, Phone);
		    p.setString(3, Email);
		    p.setString(4, Name);
		    p.setString(5, Surname);
		    p.setInt(6, Province);
		    p.setInt(7, District);
		    p.setInt(8, Neighborhood);
		    p.setInt(9, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateUser(int UserID, int VerifyStatus, String Name, String Surname, String Password, String Phone, String Email, int Province, int District, int Neighborhood) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set VerifyStatus=?, Password=?, Phone=?, Email=?, Name=?, Surname=?, ProvinceID=?, DistrictID=?, NeighborhoodID=? where UserID=?");
		    p.setInt(1, VerifyStatus);
		    p.setString(2, Password);
		    p.setString(3, Phone);
		    p.setString(4, Email);
		    p.setString(5, Name);
		    p.setString(6, Surname);
		    p.setInt(7, Province);
		    p.setInt(8, District);
		    p.setInt(9, Neighborhood);
		    p.setInt(10, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public ResultSet getUsers() {
		try{
			this.rs=stmt.executeQuery("select *,(IF(Users.VerifyStatus=1, \"Onaylı\", \"Onaysız\")) as VerifyStatusText from Users");  
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getFavorites(int UserID) {
		try{
		    PreparedStatement p = con.prepareStatement("select Favorites from Users where UserID=?");
		    p.setInt(1, UserID);
		    this.rs = p.executeQuery();
		    return this.rs;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public boolean updateFavorites(int UserID,String Favorites) {
		try{
		    PreparedStatement p = con.prepareStatement("update Users set Favorites=? where UserID=?");
		    p.setString(1, Favorites);
		    p.setInt(2, UserID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	public ResultSet getUsersByRole(int RoleID) {
		try{
		    PreparedStatement p = con.prepareStatement("select *,(IF(Users.VerifyStatus=1, \"Onaylı\", \"Onaysız\")) as VerifyStatusText from Users where RoleID = ?");
		    p.setInt(1, RoleID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getUsersByRoleAndZone(int RoleID, String Zone, int ZoneID) {
		try{
		    PreparedStatement p = con.prepareStatement("select *,(IF(Users.VerifyStatus=1, \"Onaylı\", \"Onaysız\")) as VerifyStatusText from Users where RoleID = ? and "+Zone+" = ?");
		    p.setInt(1, RoleID);
		    p.setInt(2, ZoneID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
}

