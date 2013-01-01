package sp.phone.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;

import sp.phone.bean.ArticlePage;
import sp.phone.pojo.DelayMap;
import sp.phone.utils.ArticleUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.PollUtil;

@SuppressWarnings("serial")
public class TimerServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("gbk");
		PrintWriter out = response.getWriter();
		String uri = request.getParameter("uri");
		String out_string = "";

		System.out.println("timer uri:" + uri);

		if (uri != null) {
			if (uri.indexOf("@") != 0) {
				uri = uri.replace("@", "&");
			}
			ArticlePage articlePage = dmap.get(uri);

			if (articlePage == null) {
				System.out.println("new download");

				String html = HttpUtil.getHtml(uri);
				int length = html.length();
				if (length == 0) {
					out_string = "error_connect";// 连接异常
				} else if (length < 25000) {
					out_string = "error_ad";// 遇到了广告
				} else {
					try {
						articlePage = ArticleUtil.parserArticleList(html);
						
//						JSONObject jsonObject = JSONObject
//								.fromObject(articlePage);					
//						out_string = jsonObject.toString();
						out_string=	JSON.toJSONString(articlePage);
						dmap.put(uri, articlePage);
					} catch (ParserException e) {
						out_string = "error_parser";// 转换异常
					}
				}
			} else {
				System.out.println("old data");
				// 从服务器缓存中直接取出数据
//				JSONObject jsonObject = JSONObject.fromObject(articlePage);
//				out_string = jsonObject.toString();
				
				out_string=	JSON.toJSONString(articlePage);
			}
		}
		out.print(out_string);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	DelayMap<String, ArticlePage> dmap = new DelayMap<String, ArticlePage>(1,
			30, 4000, "timer");

	@Override
	public void init(ServletConfig config) throws ServletException {
		PollUtil pu = new PollUtil();
		String[] rss_uris = new String[] {
				"http://bbs.ngacn.cc/thread.php?fid=323&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=181&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=187&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=185&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=189&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=182&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=186&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=184&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=183&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=188&rss=1",
				"http://bbs.ngacn.cc/thread.php?fid=320&rss=1" };
		pu.poll(30, rss_uris, dmap);

	}

	public static void main(String[] args) {
	}
}
