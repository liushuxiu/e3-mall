package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.itemMapper;
import cn.e3mall.search.service.SearchItemService;

//索引库维护Service 

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private itemMapper mapper;
	@Autowired
	private SolrServer server;
	@Override
	public E3Result importAllItems() {
		// TODO Auto-generated method stub
		try {
			//查询商品列表
			List<SearchItem> list=mapper.getItemList();
			//遍历商品列表
			for (SearchItem searchItem : list) {
				SolrInputDocument document=new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
			    server.add(document);
			}
			server.commit();
			return E3Result.ok();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return E3Result.build(500, "数据导入失败");
		}
		
	}

}
