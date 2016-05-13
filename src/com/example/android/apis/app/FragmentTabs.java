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
package com.example.android.apis.app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Fragment之Actionbar及Tab交互
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午11:13:40
 */
public class FragmentTabs extends Activity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 得到ActionBar
		final ActionBar bar = getActionBar();
		// 设置T模式
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		bar.addTab(bar
				.newTab()
				.setText("Simple")
				.setTabListener(
						new TabListener<FragmentStack.CountingFragment>(this,
								"simple", FragmentStack.CountingFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText("Contacts")
				.setTabListener(
						new TabListener<LoaderCursor.CursorLoaderListFragment>(
								this, "contacts",
								LoaderCursor.CursorLoaderListFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText("Apps")
				.setTabListener(
						new TabListener<LoaderCustom.AppListFragment>(this,
								"apps", LoaderCustom.AppListFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText("Throttle")
				.setTabListener(
						new TabListener<LoaderThrottle.ThrottledLoaderListFragment>(
								this,
								"throttle",
								LoaderThrottle.ThrottledLoaderListFragment.class)));

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
	 * 在ActionBar中实现标签页可以实现Android.app.ActionBar.TabListener
	 * ，重写onTabSelected、onTabUnselected和onTabReselected方法来关联Fragment。
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 上午11:15:55
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

			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getFragmentManager()
						.beginTransaction();
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
