package model;

import java.sql.*;

public class Model {
	
	protected Connection con=null;
	protected Statement stmt=null;
    protected ResultSet rs=null;
    protected int returnVal=0;
	public Model() {
		try{
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EstateMS","root","12345678"); 
			this.stmt=this.con.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}  
	}
	public void closecon() throws SQLException  
	{  
		this.con.close();
	}  
}
