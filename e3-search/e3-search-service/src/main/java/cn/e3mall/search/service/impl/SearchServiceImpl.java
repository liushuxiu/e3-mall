package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	@Override
	public SearchResult search(String keyword, int page, int rows) {
		// TODO Auto-generated method stub
		SolrQuery query=new SolrQuery();
		query.setQuery(keyword);
		query.setStart((page-1)*rows);
		query.setRows(rows);
		query .set("df", "item_title");
		query .setHighlight(true);
		query.addHighlightField("item_title");
		query .setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		
		SearchResult searchResult = null;
		try {
			searchResult = searchDao.search(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long recordCount=searchResult .getRecordCount();
		int pages=(int) (recordCount /rows);
		if (recordCount % rows >0) {
			pages++;
		}
		searchResult.setTotalPages(pages);
		
		return searchResult;
		
	}

}
