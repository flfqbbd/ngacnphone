package sp.phone.utils;

import java.util.Random;

import sp.phone.activity.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ActivityUtil {

	public static int bg;
	int[] bgs = { R.drawable.bg_black, R.drawable.bg_wood,
			R.drawable.bg_black_thread };

	public void setBG() {
		int r = new Random().nextInt(bgs.length);
		bg = bgs[r];
		System.out.println("set bg:" + r);
	}

	private ProgressDialog proDialog;

	private Context context;

	public ActivityUtil(Context context) {
		this.context = context;
	}

	public void notice(String title, String content) {
		Message message = new Message();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("content", content);
		message.setData(b);
		handler.sendMessage(message);
	}

	private Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			Bundle b = msg.getData();
			if (b != null) {
				String title = b.getString("title");
				String content = b.getString("content");
				if (proDialog != null) {

					if ("ERROR".equals(title)) {
						System.out.println("sleep 3 error");
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("sleep 2 info");
						// try {
						// Thread.sleep(2000);
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
					}
					proDialog.setTitle(title);
					proDialog.setMessage(content);
					proDialog.show();
				} else {
					proDialog = ProgressDialog.show(context, title, content);
					proDialog.setCanceledOnTouchOutside(true);
				}
			}
		};
	};

	public void dismiss() {
		if (proDialog != null) {
			proDialog.dismiss();
		}
	}

}
