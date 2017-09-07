package edu.ap.rd.quotes;


import java.util.ArrayList;

import javax.ws.rs.*;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import redis.clients.*;
import redis.clients.jedis.Jedis;
import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@Path("/movies")
public class movieResource {

	
	
	

	@GET
	@Path("{name}")
	@Produces({ "text/html" })
	public String getmovieactors(@PathParam("name") String movie_name) {
		
		
	
		// Uncomment voor opvullen
		this.fillDb(true);

		Jedis jedis = JedisConnection.getInstance().getConnection();
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<head>");
		builder.append("<title>actors</title>");
		builder.append("</head>");

		builder.append("<body>");
		builder.append("<ul>");
		ArrayList<String> actorsList = new ArrayList<String>();
		for (String key : jedis.keys("movie:*")) {		
			String movie = jedis.get(key);
			if(movie.equals(movie_name)){
				
				String id = key.split(":")[1];
				for (String quote : jedis.smembers("actors:" + id)) {
					actorsList.add(quote);
				}
				
				for (String year : jedis.smembers("year:" + id)) {
					actorsList.add(year);
				}
				
			}
		}

		for(String quote: actorsList){
			
			builder.append("<li>"+quote+"</li>");
			
		}
		

		builder.append("<br><br><a href=/ExamenWebTech4Vraag1/index/>Home</a>");
				
		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
		

		



	
}
	
	@GET
	@Path("allactors")
	@Produces({ "text/html" })
	public String getAllactors() {
		// Uncomment voor opvullen

		Jedis jedis = JedisConnection.getInstance().getConnection();
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<head>");
		builder.append("<title>actors</title>");
		builder.append("</head>");

		builder.append("<body>");
		builder.append("<ul>");
		for (String key : jedis.keys("actors:*")) {
			for (String quote : jedis.smembers(key)) {
				builder.append("<li>" + quote);
			}
		}
		builder.append("</ul>");
		builder.append("<br><br><a href=/ExamenWebTech4Vraag1/index/>Home</a>");
		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
	}


	private void fillDb(boolean flush) {
		Jedis jedis = JedisConnection.getInstance().getConnection();

		if (flush) {
			jedis.flushDB();
		}

		jedis.set("movie:1", "Deadpool");
		

		jedis.sadd("actors:1", "Wolverine");
		jedis.sadd("actors:1", "Batman");
		jedis.sadd("year:1", "2016");
		
	}

}
