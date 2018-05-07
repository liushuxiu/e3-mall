//package cn.eemall.solrj;
//
//import org.apache.solr.client.solrj.impl.CloudSolrServer;
//import org.apache.solr.common.SolrInputDocument;
//import org.junit.Test;
//
//public class TestSolrCloud {
//
//	@Test
//	public void testAddDocument() throws Exception{
//		//创建一个集群的连接，应该使用CloudSolrServer创建
//		String zkHost="192.168.25.24:2182,192.168.25.24:2183,192.168.25.24:2184";
//		CloudSolrServer server=new CloudSolrServer(zkHost);
//		server.setDefaultCollection("collection1");
//		SolrInputDocument document=new SolrInputDocument();
//		document.setField("id", "solrcloud02");
//		document.setField("item_title", "测试商品02");
//		document.setField("item_price", 123);
//		server.add(document);
//		server.commit();
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//	}
//}
