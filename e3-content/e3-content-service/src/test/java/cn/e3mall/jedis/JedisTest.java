/*package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testJedis() {
	
		Jedis jedis=new Jedis("192.168.25.24",6379);
		jedis.set("test", "my first jedis test");
		String xString=jedis.get("test");
		System.out.println(xString);
		jedis.close();
	}
	
	@Test
	public void testJedisPool() {
		JedisPool jedisPool=new JedisPool("192.168.25.24", 6379);
		Jedis jedis=jedisPool.getResource();
		String string=jedis.get("test");
		System.out.println(string);
		jedis.close();
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() {
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.24", 7001));
		nodes.add(new HostAndPort("192.168.25.24", 7002));
		nodes.add(new HostAndPort("192.168.25.24", 7003));
		nodes.add(new HostAndPort("192.168.25.24", 7004));
		nodes.add(new HostAndPort("192.168.25.24", 7005));
		nodes.add(new HostAndPort("192.168.25.24", 7006));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		jedisCluster.set("x", "haha");
		System.out.println(jedisCluster.get("x"));
		jedisCluster.close();
		
		
		
		
		
	}
	
}
*/