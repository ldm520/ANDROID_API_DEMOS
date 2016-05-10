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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * This activity demonstrates how to implement an {@link android.view.ActionProvider}
 * for adding functionality to the Action Bar. In particular this demo creates an
 * ActionProvider for launching the system settings and adds a menu item with that
 * provider.
 */
/**
 * ActionBar操作栏，可以通过ActionBar监听打开系统设置
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 上午9:32:41
 */
public class ActionBarSettingsActionProviderActivity extends Activity {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// 获取ActionBar菜单对应布局
		getMenuInflater().inflate(R.menu.action_bar_settings_action_provider,
				menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// If this callback does not handle the item click,
		// onPerformDefaultAction
		// of the ActionProvider is invoked. Hence, the provider encapsulates
		// the
		// complete functionality of the menu item.
		Toast.makeText(this,
				R.string.action_bar_settings_action_provider_no_handling,
				Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 自定义ActionProvider
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-10 上午9:36:16
	 */
	public static class SettingsActionProvider extends ActionProvider {

		/** An intent for launching the system settings. */
		// 系统设置ACTION
		private static final Intent sSettingsIntent = new Intent(
				Settings.ACTION_SETTINGS);

		/** Context for accessing resources. */
		private final Context mContext;

		/**
		 * Creates a new instance.
		 * 
		 * @param context
		 *            Context for accessing resources.
		 */
		public SettingsActionProvider(Context context) {
			super(context);
			mContext = context;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public View onCreateActionView() {
			// Inflate the action view to be shown on the action bar.
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			View view = layoutInflater.inflate(
					R.layout.action_bar_settings_action_provider, null);
			ImageButton button = (ImageButton) view.findViewById(R.id.button);
			// Attach a click listener for launching the system settings.
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mContext.startActivity(sSettingsIntent);
				}
			});
			return view;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onPerformDefaultAction() {
			// This is called if the host menu item placed in the overflow menu
			// of the
			// action bar is clicked and the host activity did not handle the
			// click.
			mContext.startActivity(sSettingsIntent);
			return true;
		}
	}
}
