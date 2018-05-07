package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {

	@Autowired 
	private SearchService service;
	@Value ("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	
	@RequestMapping ("/search")
	public String searchItemList(String keyword,
			@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		keyword =new String(keyword .getBytes("iso-8859-1"), "utf-8");
		SearchResult  result =service .search(keyword, page, SEARCH_RESULT_ROWS);
		model.addAttribute("query", keyword );
		model.addAttribute("totalPages", result .getTotalPages());
		model.addAttribute("page", page );
		model.addAttribute("recordCounr",result.getRecordCount());
		model.addAttribute("itemList", result .getItemList());
		return "search";
		
	}
}
