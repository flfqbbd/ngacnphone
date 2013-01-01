package sp.phone.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZoomImage {
	private static final Log log = LogFactory.getLog(ZoomImage.class);

	public static void zoomImage(String filePath, String outPutFile, int width,
			int height, boolean replace) throws Exception,
			FileNotFoundException {
		File inPutFile = new File(filePath);
		File outPut = new File(outPutFile);
		zoomImage(inPutFile, outPut, width, height, replace);
	}

	public static void zoomImage(File inPutFile, File outPutFile, int width,
			int height, boolean replace) throws Exception,
			FileNotFoundException {
		if (!inPutFile.isFile()) {
			log.error("文件不存在:" + inPutFile);
			throw new FileNotFoundException("文件不存在:" + inPutFile);
		}
		if (!outPutFile.exists() || replace) {
			zoomImage(inPutFile, outPutFile, height, width);
		}
	}

	/**
	 * 按指定大小缩放图片
	 * 
	 * @param inPutFile
	 * @param outPutFile
	 * @param height
	 * @param width
	 * @throws Exception
	 */
	public static void zoomImage(File inPutFile, File outPutFile, int width,
			int height) throws Exception {
		BufferedImage source = ImageIO.read(inPutFile);
		if (source == null) {
			return;
		}
		double hx = (double) height / source.getHeight();
		double wy = (double) width / source.getWidth();
		if (hx < wy) {
			wy = hx;
			width = (int) (source.getWidth() * wy);
		} else {
			hx = wy;
			height = (int) (source.getHeight() * hx);
		}

		int type = source.getType();
		BufferedImage target = null;
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(width, height, type);
		}
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g.drawRenderedImage(source, AffineTransform.getScaleInstance(wy, hx));
		g.dispose();

		try {
			ImageIO.write(target, "JPEG", outPutFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void zoomImage(File inPutFile, File outPutFile, int width)
			throws Exception {
		BufferedImage source = ImageIO.read(inPutFile);
		if (source == null || source.getWidth() <= width) {
			return;
		}
		double wy = (double) width / source.getWidth();
		int height = (int) (wy * source.getHeight());

		int type = source.getType();
		BufferedImage target = null;
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(width, height, type);
		}
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g.drawRenderedImage(source, AffineTransform.getScaleInstance(wy, wy));
		g.dispose();

		try {
			ImageIO.write(target, "JPEG", outPutFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void zoomImage(URL url, File outPutFile, int width)
			throws Exception {
		BufferedImage source = ImageIO.read(url);
		if (source == null || source.getWidth() <= width) {
			return;
		}
		double wy = (double) width / source.getWidth();
		int height = (int) (wy * source.getHeight());

		int type = source.getType();
		BufferedImage target = null;
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width,
					height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(width, height, type);
		}
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		g.drawRenderedImage(source, AffineTransform.getScaleInstance(wy, wy));
		g.dispose();

		try {
			ImageIO.write(target, "JPEG", outPutFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void zoomImage(File inPutFile, File outPutFile, int width,
			boolean replace) throws Exception, FileNotFoundException {
		if (!inPutFile.isFile()) {
			log.error("文件不存在:" + inPutFile);
			throw new FileNotFoundException("文件不存在:" + inPutFile);
		}
		if (!outPutFile.exists() || replace) {
			zoomImage(inPutFile, outPutFile, width);
		}
	}

	public static void zoomImage(String filePath, String outPutFile, int width,
			boolean replace) throws Exception, FileNotFoundException {
		File inPutFile = new File(filePath);
		File outPut = new File(outPutFile);
		zoomImage(inPutFile, outPut, width, replace);
	}

	public static void main(String arg[]) throws Exception {
		// zoomImage("D:/sphone/avatarImage/6124802.png", "D:/6124802.png", 100,
		// 60, false);
		// zoomImage("D:/291584.gif", "D:/2915842.gif", 100, true);
		File file = new File("D:\\sphone\\avatarImage");

		int i = 0;
		for (File f : file.listFiles()) {
			File f2 = new File("D:\\sphone\\avatarImage3\\" + f.getName());
//			if (!f2.exists()) {
				try {
					System.out.println(f.getName());
					zoomImage(f, f2, 100, true);
					System.out.println(i);
					i++;
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

//			}

		}
	}
}