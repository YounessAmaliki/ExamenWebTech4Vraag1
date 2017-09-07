package edu.ap.rd.quotes;


import java.util.ArrayList;

import javax.ws.rs.*;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import redis.clients.*;
import redis.clients.jedis.Jedis;

@Path("/index")
public class movieResource {

	
	@GET
	@Produces({ "text/html" })
	public String getAllmovies() {
		// Uncomment voor opvullen
		this.fillDb(true);

		Jedis jedis = JedisConnection.getInstance().getConnection();
		StringBuilder builder = new StringBuilder();
		String htmlString = "<html><body>";
		
		htmlString +="<title>movie</title>";
		htmlString +="</head>";

		htmlString +="<body>";
		htmlString +="<ul>";
		ArrayList<String> movieList = new ArrayList<String>();
		
		for (String key : jedis.keys("movie:*")) {
				movieList.add(jedis.get(key));
		}
		for(String movie: movieList){
			//System.out.println(movie);
			String[] full_name = movie.split(" ");
			
			
			if(full_name.length == 1){
				htmlString +="<li><a href=/ExamenWebTech4Vraag1/index/"+ full_name[0]+"/actors>"+movie+"</a></li>";
				
			}else{
				htmlString +="<li><a href=/ExamenWebTech4Vraag1/index/"+ full_name[0]+"%20"+full_name[1]+"/actors>"+movie+"</a></li>";
				
			}
			
			
		}
		htmlString +="<br><a href=/ExamenWebTech4Vraag1/index/allactors>All actors</a>";
		htmlString +="</ul>";
		htmlString +="</body>";
		htmlString +="</html>";

		return htmlString;	
	}
	
	@GET
	@Path("{name}/actors")
	@Produces({ "text/html" })
	public String getmovieactors(@PathParam("name") String movie_name) {
		
		// Uncomment voor opvullen
		//this.fillDb(true);

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

/*
	@GET
	@Produces({ "text/html" })
	public String getAllactors() {
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
		for (String key : jedis.keys("actors:*")) {
			for (String quote : jedis.smembers(key)) {
				builder.append("<li>" + quote);
			}
		}
		builder.append("</ul>");
		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
	}

/*
	// Specifieke auteur
	// Data => de data die wordt gepost
	@POST
	public String getmovieactors(String data) {
		// Uncomment voor opvullen
		//this.fillDb(true);

		Jedis jedis = JedisConnection.getInstance().getConnection();
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<head>");
		builder.append("<title>actors</title>");
		builder.append("</head>");

		builder.append("<body>");
		// Selecteren van correcte movie
		for (String movie : jedis.keys("movie:*")) {
			String tmpmovie = jedis.get(movie);
			if (tmpmovie.equals(data)) {
				builder.append("testink");
				int movieId = Integer.parseInt(movie.split(":")[1]);

				builder.append("<h1>" + tmpmovie + "</h1>");
				builder.append("<ul>");
				for (String quote : jedis.smembers("actors:" + movieId)) {
					builder.append("<li>" + quote);
				}
				builder.append("</ul>");

				break; 
			}
		}

		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
	}



	*/
	private void fillDb(boolean flush) {
		Jedis jedis = JedisConnection.getInstance().getConnection();

		if (flush) {
			jedis.flushDB();
		}

		jedis.set("movie:1", "Deadpool");
		

		jedis.sadd("actors:1", "Wolverine");
		jedis.sadd("actors:1", "Batman");
		
	}

}
