package sp.phone.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SertchUtil {
	public static void search(String keywords) {
		URL url = null;
		try {
			url = new URL(
					"http://ajax.googleapis.com/ajax/services/search/web?v=1.0&hl=zh-CN&rsz=large&q="
							+ keywords + "&start=" + 0);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		URLConnection connection = null;
		StringBuilder builder = new StringBuilder();
		String builderStr = "";
		String line;
		BufferedReader reader = null;
		try {
			// 发送请求，读取查询结果
			connection = url.openConnection();
			// connection.addRequestProperty("Referer",
			// "http://www.mysite.com/index.html");
			reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream(), "utf-8"));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			builderStr = builder.toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
