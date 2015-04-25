package com.realhack.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/helloworld")
public class HelloWorld {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	
	public String sayPlainTextHello() {
		String response = "hi";
		  try{
			   Class.forName("com.mysql.jdbc.Driver").newInstance();
			   Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/realhack_schema","root","root123");   
			   con.setReadOnly(true);   
			   Statement stmt = con.createStatement();   
			   ResultSet rs = stmt.executeQuery("Select * from properties");   
			   while(rs.next()){
				   
			    response += rs.getInt(1)+ " "+rs.getString(2);
			   }
			   rs.close();
			   stmt.close();
			   con.close();
			   
			  }catch(Exception e){
			   System.out.println("***** ERROR ***** ->" + e);
			  }
		return response;
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello World RESTful Jersey"
				+ "</hello>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		String response = "hi";
		  try{
			   Class.forName("com.mysql.jdbc.Driver").newInstance();
			   Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/realhack_schema","root","root123");   
			   con.setReadOnly(true);   
			   Statement stmt = con.createStatement();   
			   ResultSet rs = stmt.executeQuery("Select * from properties");   
			   while(rs.next()){
				   
			    response += rs.getInt(1)+ " "+rs.getString(2);
			   }
			   rs.close();
			   stmt.close();
			   con.close();
			   
			  }catch(Exception e){
			   System.out.println("***** ERROR ***** ->" + e);
			  }
		return "<html> " + "<title>" + "</title>" + "<body><h1>" + response
				+ "</body></h1>" + "</html> ";
	}
}
