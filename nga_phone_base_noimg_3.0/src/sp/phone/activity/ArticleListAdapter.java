package sp.phone.activity;

import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipFile;

import sp.phone.utils.ImageUtil;
import sp.phone.utils.StringUtil;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleListAdapter extends ArrayAdapter<HashMap<String, String>> {

	ZipFile zf;

	HashMap<Integer, View> m = new HashMap<Integer, View>();
	ListView listView;
	private LayoutInflater inflater;
	private Activity activity;

	public ArticleListAdapter(Activity activity,
			List<HashMap<String, String>> lMap, ListView listView, ZipFile zf) {
		super(activity, 0, lMap);
		this.activity = activity;
		this.listView = listView;
		this.zf = zf;
		inflater = LayoutInflater.from(activity);

	}

	public View getView(int position, View view, ViewGroup parent) {

		View rowView = m.get(position);
		if (rowView != null) {
			return rowView;
		} else {
			rowView = inflater.inflate(R.layout.article_list_2, null);
			HashMap<String, String> map = getItem(position);
			final String floor = map.get("floor");// 楼层

			// 其他处理

			TextView nickNameTV = (TextView) rowView
					.findViewById(R.id.nickName);
			nickNameTV.setText(map.get("nickName"));

			TextView contentTV = (TextView) rowView.findViewById(R.id.content);
			String ngaHtml = StringUtil.parseHTML3(map.get("content"));
			ImageGetter imgGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					return ImageUtil.reSetDrawable(activity, source);
				}
			};
			Spanned html = Html.fromHtml(ngaHtml, imgGetter, null);
			contentTV.setText(html);

			TextView floorTV = (TextView) rowView.findViewById(R.id.floor);

			floorTV.setText("" + floor + " 楼");

			TextView postTimeTV = (TextView) rowView
					.findViewById(R.id.postTime);
			postTimeTV.setText(map.get("postTime"));

			m.put(position, rowView);
		}

		return rowView;
	}

}
