package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;
	public SearchResult search(SolrQuery query) throws SolrServerException {
		QueryResponse response=solrServer.query(query);
		SolrDocumentList list=response.getResults();
		long numFound=list.getNumFound();
		SearchResult result=new SearchResult();
		result.setRecordCount(numFound);
		Map<String, Map<String, List<String>>> hightlighting=response.getHighlighting();
		List<String> list2=null;
		String title="";
		List<SearchItem> itemList=new ArrayList<>();
		for (SolrDocument d : list) {
			SearchItem item=new SearchItem();
			item.setId((String) d.get("id"));
			item.setCategory_name((String) d.get("item_category_name"));
			item.setImage((String) d.get("item_image"));
			item.setPrice((long) d.get("item_price"));
			item.setSell_point((String) d.get("item_sell_point"));
			
			list2 = hightlighting.get(d.getFieldValue("id")).get("item_title");
			if (list2!=null &&list2.size()>0) {
				title=list2.get(0);
			}else {
				title=(String) d.get("item_title");
			}
			item.setTitle(title);
			itemList.add(item);
		}
		result.setItemList(itemList);
		return result;
	}
	
}
