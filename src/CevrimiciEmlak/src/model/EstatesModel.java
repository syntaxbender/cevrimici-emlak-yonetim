package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EstatesModel extends Model {
	public int EstateType = -1;
	public int ProvinceID = -1;
	public int DistrictID = -1;
	public int PriceStart = -1;
	public int PriceEnd = -1;
	public int VerifyStatus = 1;
	public String Zone = "";
	public int ZoneID = -1;
	public ResultSet getEstatesByFilter() { // verify = 1
		
		ArrayList<String> Filters = new ArrayList<String>();
		ArrayList<Object> Parameters = new ArrayList<Object>(); // using string and integers at the same time in an array

		String SQLQuery = "select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID";
		if(this.Zone.isEmpty() == false && this.ZoneID != -1) {
			Filters.add("a."+Zone+" = ?");
			Parameters.add(this.ZoneID);
		}		
		if(this.VerifyStatus != -1) {
			Filters.add("a.VerifyStatus = ?");
			Parameters.add(VerifyStatus);
		}
		if(this.EstateType != -1) {
			Filters.add("a.EstateType = ?");
			Parameters.add(new Integer(this.EstateType));
		}
		if(this.DistrictID != -1) {
			Filters.add("a.DistrictID = ?");
			Parameters.add(new Integer(this.DistrictID));
		}else if(this.ProvinceID != -1) {
			Filters.add("a.ProvinceID = ?");
			Parameters.add(new Integer(this.ProvinceID));
		}
		if(this.PriceStart != -1 && this.PriceEnd != -1) {
			Filters.add("a.Price between ? and ?");
			Parameters.add(new Integer(this.PriceStart));
			Parameters.add(new Integer(this.PriceEnd));
		}else if(this.PriceStart != -1) {
			Filters.add("a.Price > ?");
			Parameters.add(new Integer(this.PriceStart));
		}else if(this.PriceEnd != -1) {
			Filters.add("a.Price < ?");
			Parameters.add(new Integer(this.PriceEnd));
		}
		if(Filters.size()>0) SQLQuery += " where ";
		for (int i = 0; i < Filters.size(); i++) {
			SQLQuery += Filters.get(i);
			if(i != (Filters.size()-1)) SQLQuery += " and ";
		}
		try{
		    PreparedStatement p = con.prepareStatement(SQLQuery);
			for (int i = 0; i < Parameters.size(); i++) { 
				Object Parameter = Parameters.get(i);
				if (Parameter.getClass() == Integer.class) {
				    p.setInt(i+1, (int) Parameters.get(i));
				}else if (Parameter.getClass() == String.class) {
				    p.setString(i+1, (String) Parameters.get(i));
				}
			}
		    this.rs = p.executeQuery(); 
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getEstatesByUserID(int UserID) {
		try {
			PreparedStatement p = con.prepareStatement("select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID where a.UserID=?");
		    p.setInt(1, UserID);
		    this.rs = p.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getEstateByID(int EstateID) {
		try {
			PreparedStatement p = con.prepareStatement("select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID inner join Neighborhoods as d on a.NeighborhoodID=d.NeighborhoodID where a.EstateID=?");
		    p.setInt(1, EstateID);
		    this.rs = p.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getEstatesByIDs(ArrayList<String> EstateIDs) { // verify = 1
		String QueryAddition = "(";
		for (int i = 0; i < EstateIDs.size(); i++) {
			QueryAddition += "?";
			if(i == (EstateIDs.size()-1)) {
				QueryAddition += ")";
			}else{
				QueryAddition += ", ";
			}
		}
		try {
			PreparedStatement p = con.prepareStatement("select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID where a.VerifyStatus=1 and a.EstateID IN "+QueryAddition);
			for(int i2 = 0; i2<EstateIDs.size(); i2++)
		    p.setInt(i2+1, Integer.parseInt(EstateIDs.get(i2)));
		    this.rs = p.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.rs;
	}
	public boolean setEstate(String estateHeader,String estateDesc, int estateType, int estatePrice, int bathCount, int netArea, int grossArea, int roomCount, int floorCount, int heathingType, int provinceID, int districtID, int neighborhoodID, int setByUserID) {
		try{
		    PreparedStatement p = con.prepareStatement("insert into Estates set Title=?, Description=?, EstateType=?, Price=?, BathCount=?, NetArea=?, GrossArea=?, RoomCount=?, FloorCount=?, HeatingType=?, ProvinceID=?, DistrictID=?, NeighborhoodID=?, UserID=? ,VerifyStatus=0");
		    p.setString(1, estateHeader);
		    p.setString(2, estateDesc);
		    p.setInt(3, estateType);
		    p.setInt(4, estatePrice);
		    p.setInt(5, bathCount);
		    p.setInt(6, netArea);
		    p.setInt(7, grossArea);
		    p.setInt(8, roomCount);
		    p.setInt(9, floorCount);
		    p.setInt(10, heathingType);
		    p.setInt(11, provinceID);
		    p.setInt(12, districtID);
		    p.setInt(13, neighborhoodID);
		    p.setInt(14, setByUserID);
		    
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public boolean updateEstate(String estateHeader,String estateDesc, int estateType, int estatePrice, int bathCount, int netArea, int grossArea, int roomCount, int floorCount, int heathingType, int provinceID, int districtID, int neighborhoodID, int VerifyStatus,  int EstateID) {
		try{
		    PreparedStatement p = con.prepareStatement("update Estates set Title=?, Description=?, EstateType=?, Price=?, BathCount=?, NetArea=?, GrossArea=?, RoomCount=?, FloorCount=?, HeatingType=?, ProvinceID=?, DistrictID=?, NeighborhoodID=?, VerifyStatus=? where EstateID=?");
		    p.setString(1, estateHeader);
		    p.setString(2, estateDesc);
		    p.setInt(3, estateType);
		    p.setInt(4, estatePrice);
		    p.setInt(5, bathCount);
		    p.setInt(6, netArea);
		    p.setInt(7, grossArea);
		    p.setInt(8, roomCount);
		    p.setInt(9, floorCount);
		    p.setInt(10, heathingType);
		    p.setInt(11, provinceID);
		    p.setInt(12, districtID);
		    p.setInt(13, neighborhoodID);
		    p.setInt(14, VerifyStatus);
		    p.setInt(15, EstateID);
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public boolean verifyEstate(int EstateID, int VerifyStatus) {
		try{
		    PreparedStatement p = con.prepareStatement("update Estates set VerifyStatus=? where EstateID=?");
		    
		    p.setInt(1, VerifyStatus);
		    p.setInt(2, EstateID);
		    
		    this.returnVal = p.executeUpdate();
		    return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		} 
	}
	public ResultSet getEstates() {
		try{
			this.rs=stmt.executeQuery("select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID");  
		}catch(SQLException e){
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getEstatesByVerifyStatus(int VerifyStatus) {
		try {
			PreparedStatement p = con.prepareStatement("select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID where a.VerifyStatus = ?");
		    p.setInt(1, VerifyStatus);
		    this.rs = p.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.rs;
	}
	public ResultSet getEstatesByVerifyStatusAndZone(int VerifyStatus, String Zone, int ZoneID) {
		try {
			PreparedStatement p = con.prepareStatement("select *, IF(EstateType=0, \"Kiralık\", \"Satılık\") as EstateTypeText, IF(VerifyStatus=1, \"Yayında\", \"Onay Bekliyor\") as VerifyStatusText from Estates as a inner join Provinces as b on b.ProvinceID=a.ProvinceID inner join Districts as c on c.DistrictID=a.DistrictID where a.VerifyStatus = ? and a."+Zone+" = ?");
		    p.setInt(1, VerifyStatus);
		    p.setInt(2, ZoneID);
		    this.rs = p.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.rs;
	}
	public void ResetFilter() {
		this.Zone = "";
		this.ZoneID = -1;
		this.EstateType = -1;
		this.ProvinceID = -1;
		this.DistrictID = -1;
		this.PriceStart = -1;
		this.PriceEnd = -1;
	}
}
