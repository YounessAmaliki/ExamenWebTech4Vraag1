package edu.ap.rd.quotes;

import redis.clients.jedis.Jedis;

public class JedisConnection {

	private static final String HOST = "localhost";
	private static JedisConnection instance;
	private Jedis connection;

	public static JedisConnection getInstance() {
		if (instance == null) {
			instance = new JedisConnection();
		}
		return instance;
	}

	private JedisConnection() {
		this.connection = new Jedis(HOST);
	}

	public Jedis getConnection() {
		return this.connection;
	}
}

	


