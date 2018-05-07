import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {
    
	@Test
	public void testUpload() throws FileNotFoundException, IOException, MyException {
		//创建一个配置文件，文件名任意，内容就是tracker服务器的地址
		
		//使用全局对象加载配置文件
		ClientGlobal.init("C:/e3-mall/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient=new TrackerClient();
		//通过TrackerClient获得一个TrackServer对象
		TrackerServer trackerServer=trackerClient.getConnection();
		//创建一个StorageServer的引用，可以使null
		StorageServer storageServer=null;
		//创建一个StorageClient对象，参
		StorageClient storageClient=new StorageClient(trackerServer, storageServer);
		
	     //使用StorageClient上传文件
		String[] files = storageClient.upload_file("C:/Users/ms/Desktop/psb.png", "png", null);
		for (String string : files) {
		//	System.out.println(string);
		}
	}
	
	
	@Test
	public  void testFastDfsClient() throws Exception{
		  FastDFSClient fastDFSClient=new FastDFSClient("C:/e3-mall/e3-manager-web/src/main/resources/conf/client.conf");
	      String string = fastDFSClient.uploadFile("C:/Users/ms/Desktop/psb.jpg");
	      System.out.println(string);
	
	
	}
	
	
	
	
}
