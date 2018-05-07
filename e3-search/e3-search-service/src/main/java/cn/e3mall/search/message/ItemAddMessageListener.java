package cn.e3mall.search.message;


import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.print.attribute.standard.Severity;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.itemMapper;


public class ItemAddMessageListener implements MessageListener{

	@Autowired 
	private itemMapper mapper;
	@Autowired
	private SolrServer server;
	@Override
	public void onMessage(Message message){
		// TODO Auto-generated method stub
		try {
			TextMessage textMessage=(TextMessage) message;
			
			String	text = textMessage.getText();
			Long itemId=new Long(text);
			System.err.println("itemId jieshou --------"+itemId);
			Thread.sleep(1000);
			SearchItem searchItem =mapper.getItemById(itemId);
			SolrInputDocument document=new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			server.add(document);
			server.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}

}
