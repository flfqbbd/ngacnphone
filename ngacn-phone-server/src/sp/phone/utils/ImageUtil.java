package sp.phone.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class ImageUtil {

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

	public static void main(String[] args) {
		getCacheStream(
				"http://img.ngacn.cc/avatars/00/10/f8/nga_1112286.jpg?2",
				"34534");
		// newImage("http://img.ngacn.cc/avatars/00/10/f8/nga_1112286.jpg2","34534");
	}

	public static InputStream getCacheStream(String uri, String userId) {
		String extension = FilenameUtils.getExtension(uri);
		System.out.println(extension.indexOf("?"));
		if (extension.length() > 3 && extension.indexOf("?") == 3) {
			extension = extension.substring(0, 3);
		}
		InputStream is = null;
		try {
			ZipFile zf = new ZipFile("");
			ZipEntry entry = zf.getEntry("avatarImage/" + userId + "."
					+ extension);
			is = zf.getInputStream(entry);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
		return is;
	}

	public static void main2(String[] args) {
		File file_sd = new File("d:\\");
		// File[] f = file_sd.listFiles();

		// String[] nga_cache = file_sd.list();
		// System.out.println(nga_cache);
	}

}
