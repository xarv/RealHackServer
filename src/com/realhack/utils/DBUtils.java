package com.realhack.utils;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DBUtils {

	public static Connection getConnection(){
		return null;
		
	}
	
	public static void closeConnection(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
