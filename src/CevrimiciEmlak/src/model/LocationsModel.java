package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationsModel extends Model{
	public ResultSet getProvinces() {
		try{
			this.rs=stmt.executeQuery("select * from Provinces");  
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getDistricts(int ProvinceID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Districts where ProvinceID=?");
		    p.setInt(1, ProvinceID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getNeighborhoods(int DistrictsID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Neighborhoods where DistrictID=?");
		    p.setInt(1, DistrictsID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getProvinceByID(int ProvinceID) {
		try{
		    PreparedStatement p = con.prepareStatement("select * from Provinces where ProvinceID = ?");
		    p.setInt(1, ProvinceID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getDistrictByID(int DistrictID) {	
		try{
		    PreparedStatement p = con.prepareStatement("select * from Districts where DistrictID = ?");
		    p.setInt(1, DistrictID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getNeighborhoodByID(int NeighborhoodID) {
		try{
			PreparedStatement p = con.prepareStatement("select * from Neighborhoods where NeighborhoodID = ?");
		    p.setInt(1, NeighborhoodID);
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
}
