/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.view;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * Activity演示了使用系统用户界面UI来实现内容 浏览器风格的用户界面（如阅读器界面）。
 * 
 * @description：
 * @author ldm
 * @date 2016-5-24 下午4:03:51
 */
@SuppressWarnings("deprecation")
public class ContentBrowserActivity extends Activity implements
		OnQueryTextListener, ActionBar.TabListener {

	private Content mContent;

	/**
	 * 自定义ScrollView,管理系统UI
	 * 
	 * @description：View.OnSystemUiVisibilityChangeListener注册监听器来监听系统UI可见性的变化
	 * @author ldm
	 * @date 2016-5-24 下午3:43:21
	 */
	public static class Content extends ScrollView implements
			View.OnSystemUiVisibilityChangeListener, View.OnClickListener {
		private TextView mText;
		private TextView mTitleView;
		private SeekBar mSeekView;
		// SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN:Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
		int mBaseSystemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| SYSTEM_UI_FLAG_LAYOUT_STABLE;// SYSTEM_UI_FLAG_LAYOUT_STABLE:防止系统栏隐藏时内容区域大小发生变化
		int mLastSystemUiVis;

		Runnable mNavHider = new Runnable() {
			@Override
			public void run() {
				setNavVisibility(false);
			}
		};

		public Content(Context context, AttributeSet attrs) {
			super(context, attrs);

			mText = new TextView(context);
			mText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			mText.setText(context
					.getString(R.string.alert_dialog_two_buttons2ultra_msg));
			mText.setClickable(false);
			mText.setOnClickListener(this);
			mText.setTextIsSelectable(true);
			// 给ScrollView中添加子控件
			addView(mText, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			// 设置系统 UI可见性变化监听
			setOnSystemUiVisibilityChangeListener(this);
		}

		/**
		 * 初始化标题及SeekBar拖动条
		 * 
		 * @description：
		 * @author ldm
		 * @date 2016-5-24 下午4:00:37
		 */
		public void init(TextView title, SeekBar seek) {
			mTitleView = title;
			mSeekView = seek;
			// 设置状态栏可见
			setNavVisibility(true);
		}

		@Override
		public void onSystemUiVisibilityChange(int visibility) {
			int diff = mLastSystemUiVis ^ visibility;
			mLastSystemUiVis = visibility;
			if ((diff & SYSTEM_UI_FLAG_LOW_PROFILE) != 0
					&& (visibility & SYSTEM_UI_FLAG_LOW_PROFILE) == 0) {
				// 设置导状态栏可见
				setNavVisibility(true);
			}
		}

		// 当窗口中包含的可见的view发生变化时触发。
		@Override
		protected void onWindowVisibilityChanged(int visibility) {
			super.onWindowVisibilityChanged(visibility);
			setNavVisibility(true);
			getHandler().postDelayed(mNavHider, 2000);
		}

		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			super.onScrollChanged(l, t, oldl, oldt);
			// 滚动过程中，状态栏隐藏
			setNavVisibility(false);
		}

		@Override
		public void onClick(View v) {
			// 当用户点击 操作时，进行状态栏设置
			int curVis = getSystemUiVisibility();
			setNavVisibility((curVis & SYSTEM_UI_FLAG_LOW_PROFILE) != 0);
		}

		void setBaseSystemUiVisibility(int visibility) {
			mBaseSystemUiVisibility = visibility;
		}

		void setNavVisibility(boolean visible) {
			int newVis = mBaseSystemUiVisibility;
			if (!visible) {
				// SYSTEM_UI_FLAG_LOW_PROFILE:状态栏显示处于低能显示状态(low
				// profile模式)，状态栏上一些图标显示会被隐藏。
				// SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
				newVis |= SYSTEM_UI_FLAG_LOW_PROFILE
						| SYSTEM_UI_FLAG_FULLSCREEN;
			}
			// 获取状态栏当前可见性
			final boolean changed = newVis == getSystemUiVisibility();

			if (changed || visible) {
				Handler h = getHandler();
				if (h != null) {
					h.removeCallbacks(mNavHider);
				}
			}

			// 设置可见性
			setSystemUiVisibility(newVis);
			mTitleView.setVisibility(visible ? VISIBLE : INVISIBLE);
			mSeekView.setVisibility(visible ? VISIBLE : INVISIBLE);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 标题栏显示在内容上面
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.content_browser);
		mContent = (Content) findViewById(R.id.content);
		mContent.init((TextView) findViewById(R.id.title),
				(SeekBar) findViewById(R.id.seekbar));
		// 添加Tab
		ActionBar bar = getActionBar();
		bar.addTab(bar.newTab().setText("Tab 1").setTabListener(this));
		bar.addTab(bar.newTab().setText("Tab 2").setTabListener(this));
		bar.addTab(bar.newTab().setText("Tab 3").setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.content_actions, menu);
		//搜索功能
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setOnQueryTextListener(this);

		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// 分享操作
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		actionProvider.setShareIntent(shareIntent);
		return true;
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 对菜单项目进行操作
	 */
	@SuppressLint("InlinedApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.show_tabs://显示ActionBar
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			item.setChecked(true);
			return true;
		case R.id.hide_tabs://隐藏ActionBar
			getActionBar()
					.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			item.setChecked(true);
			return true;
		case R.id.stable_layout://设置状态栏是否保持可见状态
			item.setChecked(!item.isChecked());
			mContent.setBaseSystemUiVisibility(item.isChecked() ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					: View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			return true;
		}
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this, "Searching for: " + query + "...",
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
