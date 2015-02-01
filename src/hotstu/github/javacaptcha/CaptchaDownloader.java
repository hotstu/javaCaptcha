package hotstu.github.javacaptcha;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CaptchaDownloader {
	private Runnable task;

	public CaptchaDownloader() {
		this.task = new Runnable() {
			

			@Override
			public void run() {
				CloseableHttpClient client = HttpClients.createDefault();
				HttpGet get = new HttpGet(
						"https://account.bilibili.com/captcha");
				CloseableHttpResponse response = null;
				OutputStream outStream = null;
				try {
					System.out.println("start...."+ Thread.currentThread().getId());
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						File f = new File("download2/"+System.currentTimeMillis()+".png");
						outStream = new BufferedOutputStream(new FileOutputStream(f));
						entity.writeTo(outStream);
						outStream.flush();
						System.out.println("success...."+Thread.currentThread().getId());
					}
					System.out.println("complete...."+Thread.currentThread().getId());
				}catch (IOException e) {
					e.printStackTrace();
				}finally {
					if (outStream != null) {
						try {
							outStream.close();
						} catch (IOException e) {
						}
					}
					if (response != null) {
						try {
							response.close();
						} catch (IOException e) {
							
						}
					}
				}

			}
		};
	}

	public void download() {
		new Thread(task).start();
		return;
	}
	
	public static void main(String[] args) {
		CaptchaDownloader c = new CaptchaDownloader();
		for (int i = 0; i < 100; i++) {
			c.download();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
