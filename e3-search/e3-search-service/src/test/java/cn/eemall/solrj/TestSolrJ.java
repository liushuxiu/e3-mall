//package cn.eemall.solrj;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.apache.solr.client.solrj.response.QueryResponse;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.common.SolrInputDocument;
//import org.junit.Test;
//
//public class TestSolrJ {
//	@Test
//	public void addDocument() throws SolrServerException, IOException {
//		//创建SolrServer对象，创建一个连接，参数solr服务的url
//		SolrServer server=new HttpSolrServer("http://192.168.25.24:8080/solr/collection1");
//		//创建一个文档对象SolrInputDocument
//		SolrInputDocument document=new SolrInputDocument();
//		//想文档对象中添加域，文档中必须包含一个id值，所有的域名必须在schema.xml中定义
//		document.addField("id", "doc01");
//		document.addField("item_title", "测试商品01");
//		document.addField("item_price",1000);
//		//把文档写入索引库
//		server.add(document);
//		//提交
//		server.commit();
//	}
//
//	@Test
//	public void deleteDocument() throws SolrServerException, IOException {
//		SolrServer server=new HttpSolrServer("http://192.168.25.24:8080/solr/collection1");
//		//server.deleteById("doc01");
//		server.deleteByQuery("id:doc01");
//		server.commit();
//
//	}
//	@Test
//	public void queryIndex() throws SolrServerException {
//		SolrServer server=new HttpSolrServer("http://192.168.25.24:8080/solr/collection1");
//		SolrQuery query=new SolrQuery();
//		query.set("q", "*:*");
//		QueryResponse response=server.query(query);
//		SolrDocumentList list=response.getResults();
//		System.out.println("查询结果总记录数"+list.getNumFound());
//		for (SolrDocument d : list) {
//			System.out.println(d.get("id"));
//			System.out.println(d.get("item_title"));
//			System.out.println(d.get("item_sell_point"));
//			System.out.println(d.get("item_price"));
//			System.out.println(d.get("item_image"));
//			System.out.println(d.get("item_category_name"));
//		}
//	}
//	
//	@Test
//	public void fuzhichaxun() throws SolrServerException {
//		SolrServer server=new HttpSolrServer("http://192.168.25.24:8080/solr/collection1");
//		SolrQuery query=new SolrQuery();
//		query.setQuery("手机");
//		query.setStart(0);
//		query.setRows(20);
//		query.set("df", "item_title");
//		query.setHighlight(true);
//		query.addHighlightField("item_title");
//		query.setHighlightSimplePre("<em>");
//		query.setHighlightSimplePost("</em>");
//
//		QueryResponse response=server.query(query);
//		SolrDocumentList list=response.getResults();
//		System.out.println("查询结果总记录数"+list.getNumFound());
//		Map<String, Map<String, List<String>>> hightlighting=response.getHighlighting();
//        String title="";
//		for (SolrDocument d : list) {
//			System.out.println(d.get("id"));
//			//取高亮
//			List<String> list2 = hightlighting.get(d.getFieldValue("id")).get("item_title");
//			if (list2!=null &&list2.size()>0) {
//
//				title=list2.get(0);
//			}else {
//				title=(String) d.get("item_title");
//			}
//			System.out.println(title);
//			System.out.println(d.get("item_sell_point"));
//			System.out.println(d.get("item_price"));
//			System.out.println(d.get("item_image"));
//			System.out.println(d.get("item_category_name"));
//		}
//
//
//
//
//
//
//
//
//	}
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
//}
