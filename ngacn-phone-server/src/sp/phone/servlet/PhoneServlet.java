package sp.phone.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;

import sp.phone.bean.ArticlePage;
import sp.phone.pojo.DelayMap;
import sp.phone.utils.ArticleUtil;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.PollUtil;

@SuppressWarnings("serial")
public class PhoneServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("gbk");
		PrintWriter out = response.getWriter();

		System.out.println(request.getRemoteAddr());
		String uri = request.getParameter("uri");
		System.out.println(dmap.size() + " phone uri:" + uri);
		String out_string = "";
		if (uri != null) {
			if (uri.indexOf("@") != 0) {
				uri = uri.replace("@", "&");
			}

			ArticlePage articlePage = dmap.get(uri);

			if (articlePage == null) {

				System.out.println("new download2");

				URL url = new URL(uri);
				InputStream is = url.openStream();
				String html = IOUtils.toString(is);

				int length = html.length();
				if (length == 0) {
					out_string = "error_connect";// 连接异常
				} else {
					try {
						articlePage = ArticleUtil.parserArticleList(html);

						out_string = JSON.toJSONString(articlePage);

						// JSONObject jsonObject = JSONObject
						// .fromObject(articlePage);
						// out_string = jsonObject.toString();
						dmap.put(uri, articlePage);
					} catch (ParserException e) {
						out_string = "error_parser";// 转换异常
					}
				}
			} else {
				System.out.println("old data2");
				// 从服务器缓存中直接取出数据
				// JSONObject jsonObject = JSONObject.fromObject(articlePage);

				out_string = JSON.toJSONString(articlePage);

				// out_string = jsonObject.toString();
			}
		}

		out.print(out_string);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("fuck 1");
		doGet(request, response);
		System.out.println("post2");
	}

	DelayMap<String, ArticlePage> dmap = new DelayMap<String, ArticlePage>(1,
			15, 800, "phone");

	@Override
	public void init() throws ServletException {

		File file = new File(HttpUtil.PATH);
		if (!file.exists()) {
			file.mkdir();
			System.out.println(file.getName());
		}

		PollUtil pu = new PollUtil();
		pu.poll(10,
				new String[] { "http://bbs.ngacn.cc/thread.php?fid=7&rss=1" },
				dmap);

	}

}
