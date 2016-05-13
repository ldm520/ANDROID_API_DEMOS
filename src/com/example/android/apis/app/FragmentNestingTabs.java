/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.example.android.apis.app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Fragment实现ActionBar功能
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午10:24:56
 */
public class FragmentNestingTabs extends Activity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 使用getFragmentManager().enableDebugLogging(true);来提供相关的debug功能。
		FragmentManager.enableDebugLogging(true);
		super.onCreate(savedInstanceState);
		// 获取ActionBar对象
		final ActionBar bar = getActionBar();
		// 设置ActionBar模式
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		// 添加Tab
		bar.addTab(bar
				.newTab()
				.setText("Menus")
				.setTabListener(
						new TabListener<FragmentMenuFragment>(this, "menus",
								FragmentMenuFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText("Args")
				.setTabListener(
						new TabListener<FragmentArgumentsFragment>(this,
								"args", FragmentArgumentsFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText("Stack")
				.setTabListener(
						new TabListener<FragmentStackFragment>(this, "stack",
								FragmentStackFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText("Tabs")
				.setTabListener(
						new TabListener<FragmentTabsFragment>(this, "tabs",
								FragmentTabsFragment.class)));

		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	/**
	 * Tab监听事件
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 上午10:28:49
	 */
	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(Activity activity, String tag, Class<T> clz,
				Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;
			// 检查是否已经有一个Fragment，通过mTag这个标签，可能从先前保存的状态查找
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager()
						.beginTransaction();
				// 使用detach()会将view删除,和remove()不同,此时fragment的状态依然保持着,在使用attach()时会再次调用onCreateView()来重绘视图
				ft.detach(mFragment);
				ft.commit();
			}
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(),
						mArgs);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
		}
	}
}
