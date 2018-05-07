package cn.e3mall.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import javassist.tools.framedump;

public class HtmlGenListener implements MessageListener{

	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			// 创建一个模板
			//从消息中拿到id
			TextMessage textMessage=(TextMessage) message;
			String text=textMessage.getText();
			Long itemId=new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//查询商品信息
			TbItem tbItem=itemService.getItemById(itemId);
			Item item=new Item(tbItem);
			TbItemDesc itemDesc=itemService.getItemDescById(itemId);
			//创建一个数据集，把商品数据封装
			Map data=new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//加载模板对象
			Configuration configuration =freeMarkerConfigurer.getConfiguration();
			Template template=configuration.getTemplate("item.ftl");
			//创建一个输出流，指定输出的目录以及文件名
			Writer out=new FileWriter(HTML_GEN_PATH+itemId+".html");
			//生成静态页面
			template.process(data, out);
			//关闭流
			out.close();
			//
			
			
			
			
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
