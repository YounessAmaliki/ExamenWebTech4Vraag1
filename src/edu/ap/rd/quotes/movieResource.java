package edu.ap.rd.quotes;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.ws.rs.*;



import redis.clients.*;
import redis.clients.jedis.Jedis;

@Path("/movies")
public class movieResource {
	
	public static String callURL(String myURL) {
		
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
				Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
	
	
	
	
	


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
			else{
				
				System.out.println("\nOutput: \n" + callURL("http://www.omdbapi.com/?t=" + movie_name + "&apikey=plzBanMe"));
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
