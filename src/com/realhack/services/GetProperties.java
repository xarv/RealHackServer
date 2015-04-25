package com.realhack.services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.realhack.models.Locality;
import com.realhack.models.Property;

@Path("/getproperties/{placeId}")
public class GetProperties {
	private String PLACES_ID = "PLACES_ID"; //column name
	private String response;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties(@PathParam("placeId") String placeId){
		
		
		if(placeId != null){
			 try{
				   Class.forName("com.mysql.jdbc.Driver").newInstance();
				   Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/realhack_schema","root","root123");   
				   con.setReadOnly(true);   
				   Statement stmt = con.createStatement();   
				   ResultSet rs = stmt.executeQuery("Select * from locality where PLACES_ID = " +"'"+placeId+"'"); 
				   
				   while(rs.next()){
					 if(rs.getInt("PARENT_LOC_ID") == 0){
						   Statement stmtLocality = con.createStatement();  
						   ResultSet rsLocalities = stmtLocality.executeQuery("Select * from locality where PARENT_LOC_ID = " +rs.getInt("LOCATION_ID")); 
						   JsonObject response = new JsonObject();
						   response.addProperty("type", "localities");
						   
						   ArrayList<Locality> localities = new ArrayList<Locality>();
						   while(rsLocalities.next()){
							   Locality locality = new Locality();
							   locality.setLocId(rsLocalities.getInt("LOCATION_ID"));
							   locality.setLocLat(rsLocalities.getDouble("LOCATION_LAT"));
							   locality.setLocLong(rsLocalities.getDouble("LOCATION_LONG"));
							   locality.setLocName(rsLocalities.getString("LOCATION_NAME"));
							   locality.setParentLocId(rsLocalities.getInt("PARENT_LOC_ID"));
							   locality.setPlaceId(rsLocalities.getString("PLACES_ID"));
							   locality.setLocROI(rsLocalities.getDouble("LOC_ROI"));
							   locality.setLocLFSTL(rsLocalities.getDouble("LOC_LFST"));
							   
							   localities.add(locality);
						   }
						  
						   Gson gson = new Gson();
						   JsonElement element = gson.toJsonTree(localities, new TypeToken<List<Locality>>() {}.getType());

						   if (! element.isJsonArray()) {
						   // fail appropriately
						       throw new JsonIOException("error");
						   }

						   JsonArray jsonArray = element.getAsJsonArray();
						   rsLocalities.close();
						   stmtLocality.close();
						   con.close();
						   response.addProperty("locality", jsonArray.toString());
						   return Response.ok(response.toString()).build();
					 }
					 else{
						   Statement stmtProperties = con.createStatement();  

						   ResultSet rsProperties = stmtProperties.executeQuery("Select * from properties where LOCATION_ID = " +rs.getInt("LOCATION_ID")); 
						   JsonObject response = new JsonObject();
						   response.addProperty("type", "properties");
						   
						   ArrayList<Property> properties = new ArrayList<Property>();
						   while(rsProperties.next()){
							   Property property = new Property();
							   property.setPropID(rsProperties.getInt("PROP_ID"));
							   property.setLocId(rsProperties.getInt("LOCATION_ID"));
							   property.setPropName(rsProperties.getString("PROP_NAME"));
							   properties.add(property);
						   }
						   Gson gson = new Gson();
						   JsonElement element = gson.toJsonTree(properties, new TypeToken<List<Property>>() {}.getType());

						   if (! element.isJsonArray()) {
						   // fail appropriately
						       throw new JsonIOException("error");
						   }

						   JsonArray jsonArray = element.getAsJsonArray();
						   rsProperties.close();
						   stmtProperties.close();
						   con.close();
						   response.addProperty("properties", jsonArray.toString());
						   return Response.ok(response.toString()).build();
					 }
				     
				   }
				   rs.close();
				   stmt.close();
				   con.close();
				   
				  }catch(Exception e){
				   System.out.println("***** ERROR ***** ->" + e);
				  }
			 return Response.ok(response).build();
		}
		else{
			
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		
	}
	
}
