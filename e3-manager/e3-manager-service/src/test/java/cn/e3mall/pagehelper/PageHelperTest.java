/*package cn.e3mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {

	@Test
	public void testPageHelper() {
		
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得mapper的代理对象
		TbItemMapper itemMapper=applicationContext.getBean(TbItemMapper.class);
		
		
		//执行sql语句之前设置分页信息使用pagehelper的startPage方法
		
		PageHelper.startPage(1, 10);
		//执行查询
		TbItemExample example=new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取分页信息，pageInfo   1.总记录数  2。当前页码
		PageInfo<TbItem>pageInfo=new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(list.size());
		
		
	}
}
*/