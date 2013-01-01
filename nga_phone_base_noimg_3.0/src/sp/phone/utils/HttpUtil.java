package sp.phone.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;

import sp.phone.bean.ArticlePage;

public class HttpUtil {
	public final static String PATH = android.os.Environment
			.getExternalStorageDirectory()
			+ "/nga_cache/nga_cache";
	public final static String PATH_SD = android.os.Environment
			.getExternalStorageDirectory()
			+ "/";
//	public static String PATH_ZIP = "";

	public final static String Server = "http://bbs.ngacn.cc";

	private static String[] host_arr = { "http://aa121077313.gicp.net:8099",
			"http://aa121077313.gicp.net:8098", "http://10.0.2.2:8099",
			"http://10.0.2.2:8098" };

	public static String HOST = "";
	public static String Servlet_phone = "/servlet/PhoneServlet";
	public static String Servlet_timer = "/servlet/TimerServlet";

	public static String HOST_PORT = "";

	public static void selectServer2() {
		for (String host : host_arr) {
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(host).openConnection();
				conn.setConnectTimeout(6000);
				int result = conn.getResponseCode();
				String re = conn.getResponseMessage();
				if (result == HttpURLConnection.HTTP_OK) {
					System.out.println(re);
					System.out.println(host + "ok !");
					HOST = host;// 设置服务器
					break;
				} else {
					System.out.println(host + "fail !");
				}
			} catch (MalformedURLException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}
			}

		}
	}

	public static boolean selectServer() {
		boolean status = false;
		for (String host : host_arr) {
			String str = host + Servlet_phone;
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(str).openConnection();
				conn.setConnectTimeout(4000);
				int result = conn.getResponseCode();
				if (result == HttpURLConnection.HTTP_OK) {
					System.out.println(host + "ok !");
					HOST = str;// 设置服务器
					HOST_PORT = host;
					status = true;
					break;
				} else {
					System.out.println(host + "fail !");
				}
			} catch (MalformedURLException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (conn != null) {
					conn.disconnect();
					conn = null;
				}
			}
		}
		return status;
	}

	public static void downImage(String uri, String fileName) {
		try {
			URL url = new URL(uri);
			File file = new File(fileName);
			FileUtils.copyURLToFile(url, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static InputStream downImage2(String uri, String fileName) {
		InputStream is = null;
		try {
			URL url = new URL(uri);
			is = url.openStream();
			File file = new File(fileName);
			FileUtils.copyInputStreamToFile(is, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
		return is;
	}

	public static InputStream imageInputStream2(String uri,
			final String newFileName) {

		try {
			URL url = new URL(uri);
			final InputStream is = url.openStream();
			// InputStream is2 = is;
			System.out.println("get image is");
			new Thread() {
				public void run() {
					System.out.println("write image");
					writeFile(is, newFileName);
				};
			}.start();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void writeFile(InputStream is, String fileName) {
		try {
			FileUtils.copyInputStreamToFile(is, new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeFile(URL url, String fileName) {
		try {
			FileUtils.copyURLToFile(url, new File(fileName), 4000, 3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getHtml(String uri) {
		InputStream is = null;
		try {
			URL url = new URL(uri);
			is = url.openStream();
			return IOUtils.toString(is, "gbk");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
		return null;
	}

	public static ArticlePage getArticlePage(String uri) {
		try {
			String html = getHtml(uri);
			return ArticleUtil.parserArticleList(html);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArticlePage getArticlePageByJson(String uri) {
		System.out.println("from this");
		String json = getHtml(uri);
		long t = System.currentTimeMillis();
		ArticlePage ap = JSON.parseObject(json, ArticlePage.class);
		System.out.println(System.currentTimeMillis() - t);
		return ap;
	}
}
