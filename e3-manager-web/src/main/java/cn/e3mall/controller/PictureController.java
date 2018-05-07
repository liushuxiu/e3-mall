package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
    
    
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		
		try {
			
			FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
			String originalFilename = uploadFile.getOriginalFilename();
			String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			
			String url=fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url=IMAGE_SERVER_URL+url;
			
			Map result=new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			String json=JsonUtils.objectToJson(result);
			return json;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Map result=new HashMap<>();
			result.put("error", 1);
			result.put("url", "图片上传失败");
			String json=JsonUtils.objectToJson(result);
			return json;
			
		}
	}
}
