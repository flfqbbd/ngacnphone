package sp.phone.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.htmlparser.util.ParserException;

import sp.phone.bean.ArticlePage;

public class HttpUtil {
	public final static String PATH = "D:/sphone/avatarImage";

	public static final String NGA_HOST = "http://bbs.ngacn.cc";

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

	public static void downImage2(String uri, String fileName) {
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

	public static void resize(String filePath, int height, int width, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(filePath);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform
						.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void downImage(String uri, String fileName, int w) {
		try {
			BufferedImage bi = ImageIO.read(new URL(uri));
			int old_height = bi.getHeight();
			int old_width = bi.getWidth();
			int width = w;
			int height = (int) (1f * old_height / old_width * width);
			File f = new File(fileName);
			if (old_width > w) {
				System.out.println("开始缩放！" + fileName);
				double bo = 1d * w / old_width;

				BufferedImage changedImage = new BufferedImage(width, height,
						BufferedImage.TYPE_3BYTE_BGR);

				AffineTransform transform = new AffineTransform();
				transform.setToScale(bo, bo);

				// 根据原始图片生成处理后的图片。
				AffineTransformOp ato = new AffineTransformOp(transform, null);
				ato.filter(bi, changedImage);

				ImageIO.write(changedImage, FilenameUtils
						.getExtension(fileName), f);
			} else {

				System.out.println("保存" + fileName);
				ImageIO.write(bi, FilenameUtils.getExtension(fileName), f);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void change(String fileName, int w) {
		try {
			File f = new File(fileName);
			BufferedImage bi = ImageIO.read(f);
			if (bi != null) {

				int old_height = bi.getHeight();
				int old_width = bi.getWidth();
				int width = w;
				int height = (int) (1f * old_height / old_width * width);

				if (old_width > w) {
					System.out.println("开始缩放！" + fileName);
					double bo = 1d * w / old_width;

					BufferedImage changedImage = new BufferedImage(width,
							height, BufferedImage.TYPE_3BYTE_BGR);

					AffineTransform transform = new AffineTransform();
					transform.setToScale(bo, bo);

					// 根据原始图片生成处理后的图片。
					AffineTransformOp ato = new AffineTransformOp(transform,
							null);
					ato.filter(bi, changedImage);

					ImageIO.write(changedImage, FilenameUtils
							.getExtension(fileName), f);
				} else {
					System.out.println("不需要保存" + fileName);
				}
			}

		} catch (IOException e) {
			System.out.println(e);
			// e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		// File file = new File("D:\\sphone\\avatarImage2 - 副本\\avatarImage");
		// int i = 0;
		// for (final File f : file.listFiles()) {
		// try {
		// ZoomImage.zoomImage(f.getPath(), f.getPath(), 100, true);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println(i);
		// i++;
		// }

		// try {
		// ZoomImage
		// .zoomImage("D:\\sphone\\avatarImage2 - 副本\\avatarImage\\72841.jpg","D:\\sphone\\avatarImage2 - 副本\\avatarImage\\72841.jpg",
		// 100, true);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		ZipFile zf = new ZipFile(
				"D:\\sphone\\avatarImage2 - 副本\\avatarImage.zip");

		ZipEntry entry = zf.getEntry("avatarImage/554135.bmp");
		// Enumeration enu = zf.entries();
		InputStream is = zf.getInputStream(entry);
		ImageIO.write(ImageIO.read(is), "JPEG", new File(
				"D:\\sphone\\avatarImage2\\" + "554135.bmp"));
		// HttpUtil.aa();
	}

	public static void aa() throws IOException {

		ZipFile zf = new ZipFile(
				"D:\\sphone\\avatarImage2 - 副本\\avatarImage.zip");
		ZipEntry entry;
		zf.getEntry("avatarImage\\554135.bmp");
		Enumeration enu = zf.entries();
		while (enu.hasMoreElements()) {
			entry = (ZipEntry) enu.nextElement();
			if (entry.getName().indexOf("/") > 0) {
				continue;
			}
			if (!entry.isDirectory()) { // 找到文本文件，转换字字节流
				if (entry.getName().toLowerCase().equals("paramdefine.json")) {
					InputStream is = zf.getInputStream(entry);

					ImageIO.write(ImageIO.read(is), "JPEG", new File(
							"D:\\sphone\\avatarImage2" + entry.getName()));

					break;
				}
			}
		}
	}
}
