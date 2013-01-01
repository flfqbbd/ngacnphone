package sp.phone.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;

import sp.phone.activity.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {

	public static Bitmap zoomImageByWidth(Bitmap bitmap, int bookWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width > bookWidth) {
			int newWidth = bookWidth;
			float newHeight = ((height * newWidth) / width);
			float scaleWidth = 1f * newWidth / width;
			float scaleHeight = newHeight / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, true);
			return resizedBitmap;
		} else {
			return bitmap;
		}
	}

	/**
	 * 
	 * @param drawable
	 *            原 Drawable
	 * @param bookWidth
	 *            预定 宽度
	 * @return
	 */
	public static Drawable zoomImageByWidth(Drawable drawable, int bookWidth) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();

		// System.out.println(width + ":width,height:" + height);

		int newWidth = width;
		int newHeight = height;

		if (width > bookWidth) {
			newWidth = bookWidth;
			newHeight = (height * newWidth) / width;
		}

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		// System.out.println(scaleWidth + ":scaleWidth,scaleHeight:"
		// + scaleHeight);

		Bitmap newbmp = Bitmap.createBitmap(drawableToBitmap(drawable), 0, 0,
				width, height, matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * Drawable转化为 Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 根据 source 提取 为 Drawable
	 * 
	 * @param activity
	 *            活动
	 * @param source
	 *            源
	 * @return
	 */
	public static Drawable reSetDrawable(Activity activity, String source) {
		// System.out.println("source:" + source);
		Drawable drawable = null;
		if (source.equals("[s:1]")) {
			drawable = activity.getResources().getDrawable(R.drawable.smile);
		} else if (source.equals("[s:2]")) {
			drawable = activity.getResources().getDrawable(R.drawable.mrgreen);
		} else if (source.equals("[s:3]")) {
			drawable = activity.getResources().getDrawable(R.drawable.question);
		} else if (source.equals("[s:4]")) {
			drawable = activity.getResources().getDrawable(R.drawable.wink);
		} else if (source.equals("[s:5]")) {
			drawable = activity.getResources().getDrawable(R.drawable.redface);
		} else if (source.equals("[s:6]")) {
			drawable = activity.getResources().getDrawable(R.drawable.sad);
		} else if (source.equals("[s:7]")) {
			drawable = activity.getResources().getDrawable(R.drawable.cool);
		} else if (source.equals("[s:8]")) {
			drawable = activity.getResources().getDrawable(R.drawable.crazy);
		} else if (source.equals("[s:34]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a14);
		} else if (source.equals("[s:32]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a12);
		} else if (source.equals("[s:30]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a10);
		} else if (source.equals("[s:29]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a09);
		} else if (source.equals("[s:28]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a08);
		} else if (source.equals("[s:27]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a07);
		} else if (source.equals("[s:26]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a06);
		} else if (source.equals("[s:24]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a04);
		} else if (source.equals("[s:35]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a15);
		} else if (source.equals("[s:36]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a16);
		} else if (source.equals("[s:37]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a17);
		} else if (source.equals("[s:38]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a18);
		} else if (source.equals("[s:39]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a19);
		} else if (source.equals("[s:40]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a20);
		} else if (source.equals("[s:41]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a21);
		} else if (source.equals("[s:42]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a22);
		} else if (source.equals("[s:43]")) {
			drawable = activity.getResources().getDrawable(R.drawable.a23);
		} else {
			// 默认图片
			drawable = activity.getResources().getDrawable(R.drawable.question);
		}
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
				.getIntrinsicHeight());
		return drawable;
	}

	public static String newImage2(String oldImage, String userId) {
		if (oldImage.indexOf(".") != -1) {
			String fileType = oldImage.substring(oldImage.lastIndexOf("."),
					oldImage.length());
			if (fileType.indexOf("?") != -1) {
				fileType = fileType.split("\\?")[0];
			}
			String lf = HttpUtil.PATH + "/" + userId + fileType;
			return lf;
		} else {
			return null;
		}
	}

	public static String newImage(String oldImage, String userId) {
		String extension = FilenameUtils.getExtension(oldImage);
		String path = FilenameUtils.getPath(oldImage);
		if (extension != null) {
			if (path == null || "".equals(path)) {
				return null;
			} else if (extension.length() == 3) {
				String newName = HttpUtil.PATH + "/" + userId + "." + extension;
				return newName;
			} else if (extension.length() >= 4
					&& "?".equals(extension.substring(3, 4))) {
				String newName = HttpUtil.PATH + "/" + userId + "."
						+ extension.substring(0, 3);
				return newName;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static ZipFile zf;

	public static InputStream getCacheStream(String userId, String extension) {

		InputStream is = null;
		try {
//			System.out.println(HttpUtil.PATH_ZIP);

			if (zf != null) {
				ZipEntry entry = zf.getEntry("avatarImage/" + userId + "."
						+ extension);
				if (entry != null) {
					System.out.println("ZipFile:" + entry.getName());
					is = zf.getInputStream(entry);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;

	}

	public static String getImageType(String uri) {

		String extension = FilenameUtils.getExtension(uri);
		if (extension.length() > 3 && extension.indexOf("?") == 3) {
			extension = extension.substring(0, 3);
		}
		if (extension.length() == 3) {
			return extension;
		} else {
			return null;
		}
	}
}
