package org.cl.main.crawler;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.cl.configuration.Config;
import org.cl.run.GetWeiboCon;
import org.cl.service.GetInfo;
import org.cl.service.MyRejectHandler;
import org.cl.service.RWUid;
import org.cl.service.SaveInfo;
/**
 * 获取用户微博中的微博内容
 * @author Administrator
 * params:文件名
 */
public class Main_GetWeiboCon {
	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Config.corePoolSize,Config.maximumPoolSize, Config.keepAliveTime, 
			Config.unit, new LinkedBlockingQueue<Runnable>(), new MyRejectHandler());//限制3个线程，防止服务器性能不够用

	private static void idFilter(RWUid y_ids){
		GetInfo.idfilter_dirId(y_ids, "\\WeibosCon\\");
		System.out.println("y_ids.getNum()="+y_ids.getNum());
	}
	public static void main(String args[]) throws IOException, InterruptedException
	{	
		SaveInfo.mkdir("\\WeibosCon\\");
		//读取用户ID放入ids[hashSet]
		RWUid y_ids=GetInfo.getUIDinDir("Weibos");
		idFilter(y_ids);
		
		String uid = null;
		while (null!=(uid = y_ids.getUid())) {
			GetWeiboCon getWeiboCon = new GetWeiboCon(uid);
			threadPool.execute(getWeiboCon);
		}
		
		threadPool.shutdown();
		while (threadPool.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
