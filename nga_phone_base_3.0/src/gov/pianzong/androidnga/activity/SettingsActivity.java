package gov.pianzong.androidnga.activity;

import gov.pianzong.androidnga.R;
import sp.phone.bean.PerferenceConstant;
import sp.phone.fragment.AlertDialogFragment;
import sp.phone.utils.ActivityUtil;
import sp.phone.utils.ImageUtil;
import sp.phone.utils.PhoneConfiguration;
import sp.phone.utils.ReflectionUtil;
import sp.phone.utils.ThemeManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends FragmentActivity implements
		PerferenceConstant {

	private View view;
	private CompoundButton checkBoxDownimgNowifi;
	private CompoundButton checkBoxDownAvatarNowifi;
	private CompoundButton nightMode;
	private CompoundButton showAnimation;
	private CompoundButton useViewCache;
	private CompoundButton showSignature;
	private CompoundButton notification;
	private CompoundButton notificationSound;
	private CompoundButton uploadLocation;

	private CompoundButton split = null;
	private CompoundButton ha = null;
	private SeekBar fontSizeBar;
	private float defaultFontSize;
	private TextView fontTextView;
	private int defaultWebSize;
	private SeekBar webSizebar;
	private WebView websizeView;
	private TextView avatarSizeTextView;
	private ImageView avatarImage;
	private SeekBar avatarSeekBar;

	// private MyGestureListener gestureListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// gestureListener = new MyGestureListener(this);

		initView();

	}

	void initView() {

		int orentation = ThemeManager.getInstance().screenOrentation;
		if (orentation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				|| orentation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(orentation);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
		ThemeManager.SetContextTheme(this);
		int layoutId = R.layout.settings;
		if (ActivityUtil.isMeizu())
			layoutId = R.layout.settings_meizu;
		view = getLayoutInflater().inflate(layoutId, null);

		this.setContentView(view);
		PhoneConfiguration config = PhoneConfiguration.getInstance();

		checkBoxDownimgNowifi = (CompoundButton) findViewById(R.id.checkBox_down_img_no_wifi);
		checkBoxDownimgNowifi.setChecked(config.downImgNoWifi);
		DownImgNoWifiChangedListener listener = new DownImgNoWifiChangedListener();
		checkBoxDownimgNowifi.setOnCheckedChangeListener(listener);

		checkBoxDownAvatarNowifi = (CompoundButton) findViewById(R.id.checkBox_download_avatar_no_wifi);
		checkBoxDownAvatarNowifi.setChecked(config.downAvatarNoWifi);
		DownAvatarNowifiChangedListener AvatarListener = new DownAvatarNowifiChangedListener();
		checkBoxDownAvatarNowifi.setOnCheckedChangeListener(AvatarListener);

		nightMode = (CompoundButton) findViewById(R.id.checkBox_night_mode);
		nightMode
				.setChecked(ThemeManager.getInstance().getMode() == ThemeManager.MODE_NIGHT);
		nightMode.setOnCheckedChangeListener(new NightModeListener());

		showAnimation = (CompoundButton) findViewById(R.id.checkBox_show_animation);
		showAnimation.setChecked(config.showAnimation);
		showAnimation.setOnCheckedChangeListener(new ShowAnimationListener());

		useViewCache = (CompoundButton) findViewById(R.id.checkBox_use_view_cache);
		useViewCache.setChecked(config.useViewCache);
		useViewCache.setOnCheckedChangeListener(new UseViewCacheListener());

		showSignature = (CompoundButton) findViewById(R.id.checkBox_show_signature);
		showSignature.setChecked(config.showSignature);
		showSignature.setOnCheckedChangeListener(new ShowSignatureListener());

		notificationSound = (CompoundButton) findViewById(R.id.checkBox_notification_sound);
		notificationSound
				.setOnCheckedChangeListener(new NotificationSoundChangedListener());
		notificationSound.setChecked(config.notificationSound);
		notificationSound.setEnabled(config.notification);

		notification = (CompoundButton) findViewById(R.id.checkBox_notification);
		notification
				.setOnCheckedChangeListener(new NotificationChangedListener(
						notificationSound));
		notification.setChecked(config.notification);

		uploadLocation = (CompoundButton) findViewById(R.id.checkBox_upload_location);
		uploadLocation.setChecked(config.uploadLocation);
		uploadLocation.setOnCheckedChangeListener(new UploadLocationListener());

		split = (CompoundButton) findViewById(R.id.checkBox_split);
		if (split != null) {
			boolean checked = true;
			if ((config.getUiFlag() & UI_FLAG_SPLIT) == 0) {
				checked = false;
			}
			split.setChecked(checked);
			split.setOnCheckedChangeListener(new SplitChangedListener());
		}

		ha = (CompoundButton) findViewById(R.id.checkBox_ha);
		if (ha != null) {
			boolean checked = true;
			if ((config.getUiFlag() & UI_FLAG_HA) == 0) {
				checked = false;
			}
			ha.setChecked(checked);
			ha.setOnCheckedChangeListener(new HaChangedListener());
		}

		fontTextView = (TextView) findViewById(R.id.textView_font_size);
		defaultFontSize = fontTextView.getTextSize();

		fontSizeBar = (SeekBar) findViewById(R.id.fontsize_seekBar);
		fontSizeBar.setMax(300);
		final float textSize = config.getTextSize();
		int progress = (int) (100.0f * textSize / defaultFontSize);
		fontSizeBar.setProgress(progress);
		fontSizeBar.setOnSeekBarChangeListener(new FontSizeListener());
		fontTextView.setTextSize(textSize);

		websizeView = (WebView) findViewById(R.id.websize_view);
		defaultWebSize = websizeView.getSettings().getDefaultFontSize();
		webSizebar = (SeekBar) findViewById(R.id.webszie_bar);
		webSizebar.setMax(300);
		final int webSize = config.getWebSize();
		progress = 100 * webSize / defaultWebSize;
		webSizebar.setProgress(progress);
		websizeView.getSettings().setDefaultFontSize(webSize);
		websizeView.loadDataWithBaseURL(null, "帖子内字体大小", "text/html", "utf-8",
				"");
		webSizebar.setOnSeekBarChangeListener(new WebSizeListener());

		progress = config.nikeWidth;
		avatarSizeTextView = (TextView) findViewById(R.id.textView_avatarsize);
		avatarImage = (ImageView) findViewById(R.id.avatarsize);
		Drawable defaultAvatar = getResources().getDrawable(
				R.drawable.default_avatar);
		Bitmap bitmap = ImageUtil.zoomImageByWidth(defaultAvatar, progress);
		avatarImage.setImageBitmap(bitmap);

		avatarSeekBar = (SeekBar) findViewById(R.id.avatarsize_seekBar);
		avatarSeekBar.setMax(200);
		avatarSeekBar.setProgress(progress);
		avatarSeekBar.setOnSeekBarChangeListener(new AvatarSizeListener());
		updateThemeUI();
	}

	private void updateThemeUI() {
		int fgColor = getResources().getColor(
				ThemeManager.getInstance().getForegroundColor());
		checkBoxDownimgNowifi.setTextColor(fgColor);
		checkBoxDownAvatarNowifi.setTextColor(fgColor);
		nightMode.setTextColor(fgColor);
		showAnimation.setTextColor(fgColor);
		useViewCache.setTextColor(fgColor);
		showSignature.setTextColor(fgColor);
		notification.setTextColor(fgColor);
		notificationSound.setTextColor(fgColor);
		uploadLocation.setTextColor(fgColor);
		if (split != null)
			split.setTextColor(fgColor);
		if (ha != null)
			ha.setTextColor(fgColor);

		fontTextView.setTextColor(fgColor);
		avatarSizeTextView.setTextColor(fgColor);

		view.setBackgroundResource(ThemeManager.getInstance()
				.getBackgroundColor());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int flags = 15;
		/*
		 * ActionBar.DISPLAY_SHOW_HOME; flags |= ActionBar.DISPLAY_USE_LOGO;
		 * flags |= ActionBar.DISPLAY_SHOW_TITLE; flags |=
		 * ActionBar.DISPLAY_HOME_AS_UP; flags |= ActionBar.DISPLAY_SHOW_CUSTOM;
		 */
		// final ActionBar bar = getActionBar();
		// bar.setDisplayOptions(flags);
		ReflectionUtil.actionBar_setDisplayOption(this, flags);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			// case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class NightModeListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(NIGHT_MODE, arg1);
			editor.commit();
			int mode = ThemeManager.MODE_NORMAL;
			if (arg1)
				mode = ThemeManager.MODE_NIGHT;
			ThemeManager.getInstance().setMode(mode);
			updateThemeUI();
		}

	}

	class ShowAnimationListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			PhoneConfiguration.getInstance().showAnimation = isChecked;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(SHOW_ANIMATION, isChecked);
			editor.commit();

		}

	}

	class UseViewCacheListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			PhoneConfiguration.getInstance().useViewCache = isChecked;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(USE_VIEW_CACHE, isChecked);
			editor.commit();

			if (isChecked) {
				new AlertDialog.Builder(SettingsActivity.this).setTitle("提示")
						.setMessage(R.string.view_cache_tips)
						.setPositiveButton("知道了", null).show();
			}

		}

	}

	class ShowSignatureListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			PhoneConfiguration.getInstance().showSignature = isChecked;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(SHOW_SIGNATURE, isChecked);
			editor.commit();

		}

	}

	class UploadLocationListener implements OnCheckedChangeListener,
			PerferenceConstant {
		private final String TAG = "UploadLocationAlert";

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!isChecked) {
				changeTo(isChecked);
			} else {

				final CompoundButton b = buttonView;
				String alertString = getString(R.string.set_upload_location_alert);
				AlertDialogFragment f = AlertDialogFragment.create(alertString);
				f.setOkListener(new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						changeTo(true);

					}

				});
				f.setCancleListener(new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						b.setChecked(false);

					}

				});

				FragmentActivity a = (FragmentActivity) buttonView.getContext();
				f.show(a.getSupportFragmentManager(), TAG);
			}

		}

		private void changeTo(boolean isChecked) {
			PhoneConfiguration.getInstance().uploadLocation = isChecked;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(UPLOAD_LOCATION, isChecked);
			editor.commit();
		}

	}

	class DownImgNoWifiChangedListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

			PhoneConfiguration.getInstance().downImgNoWifi = arg1;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(DOWNLOAD_IMG_NO_WIFI, arg1);
			editor.commit();

		}

	}

	class DownAvatarNowifiChangedListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

			PhoneConfiguration.getInstance().downAvatarNoWifi = arg1;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(DOWNLOAD_AVATAR_NO_WIFI, arg1);
			editor.commit();

		}

	}

	class NotificationChangedListener implements OnCheckedChangeListener,
			PerferenceConstant {
		final CompoundButton child;

		public NotificationChangedListener(CompoundButton child) {
			this.child = child;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			PhoneConfiguration.getInstance().notification = isChecked;
			child.setEnabled(isChecked);
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(ENABLE_NOTIFIACTION, isChecked);
			editor.commit();

		}

	}

	class NotificationSoundChangedListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			PhoneConfiguration.getInstance().notificationSound = isChecked;

			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putBoolean(NOTIFIACTION_SOUND, isChecked);
			editor.commit();

		}

	}

	class FontSizeListener implements SeekBar.OnSeekBarChangeListener,
			PerferenceConstant {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if (progress != 0)
				fontTextView.setTextSize(defaultFontSize * progress / 100.0f);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			float textSize = defaultFontSize * seekBar.getProgress() / 100.0f;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putFloat(TEXT_SIZE, textSize);
			editor.commit();
			PhoneConfiguration.getInstance().setTextSize(textSize);
		}

	}

	class WebSizeListener implements SeekBar.OnSeekBarChangeListener,
			PerferenceConstant {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress != 0)
				websizeView.getSettings().setDefaultFontSize(
						(int) (defaultWebSize * progress / 100.0f));

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// int textSize = (int)
			// (defaultWebSize*seekBar.getProgress()/100.0f);

			int webSize = (int) (defaultWebSize * seekBar.getProgress() / 100.0f);
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();

			editor.putInt(WEB_SIZE, webSize);
			editor.commit();

			PhoneConfiguration.getInstance().setWebSize(webSize);

		}

	}

	class AvatarSizeListener implements SeekBar.OnSeekBarChangeListener,
			PerferenceConstant {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			if (2 > progress)
				progress = 2;
			Drawable defaultAvatar = getResources().getDrawable(
					R.drawable.default_avatar);
			Bitmap bitmap = ImageUtil.zoomImageByWidth(defaultAvatar, progress);
			try {
				ImageUtil.recycleImageView(avatarImage);
				avatarImage.setImageBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			int progress = seekBar.getProgress();
			if (2 > progress)
				progress = 2;
			PhoneConfiguration.getInstance().nikeWidth = progress;
			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putInt(NICK_WIDTH, progress);
			editor.commit();

		}

	}

	class HaChangedListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			
			int flag = PhoneConfiguration.getInstance().getUiFlag();
			if(isChecked){
				flag |= UI_FLAG_HA;
			}else{
				flag = flag & ~UI_FLAG_HA;
			}
			
			PhoneConfiguration.getInstance().setUiFlag(flag);

			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putInt(UI_FLAG, flag);
			editor.commit();

		}

	}

	class SplitChangedListener implements OnCheckedChangeListener,
			PerferenceConstant {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			int flag = PhoneConfiguration.getInstance().getUiFlag();
			if(isChecked){
				flag |= UI_FLAG_SPLIT;
			}else{
				flag = flag & ~UI_FLAG_SPLIT;
			}
			
			PhoneConfiguration.getInstance().setUiFlag(flag);

			SharedPreferences share = getSharedPreferences(PERFERENCE,
					MODE_PRIVATE);

			Editor editor = share.edit();
			editor.putInt(UI_FLAG, flag);
			editor.commit();

		}

	}

}
