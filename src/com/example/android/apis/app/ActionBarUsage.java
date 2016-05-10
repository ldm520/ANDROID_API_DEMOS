/*
 * Copyright (C) 2010 The Android Open Source Project
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * This demonstrates idiomatic usage of the Action Bar. The default Honeycomb theme
 * includes the action bar by default and a menu resource is used to populate the
 * menu data itself. If you'd like to see how these things work under the hood, see
 * ActionBarMechanics.
 */
/**
 * ActionBar习惯用法
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 上午10:01:03
 */
public class ActionBarUsage extends Activity implements OnQueryTextListener {
	TextView mSearchText;
	int mSortMode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 创建TextView
		mSearchText = new TextView(this);
		// 设置界面：就是上面新建的TextView
		setContentView(mSearchText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions, menu);
		// 获取SearchView控件
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		// 设置搜索监听
		searchView.setOnQueryTextListener(this);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mSortMode != -1) {
			Drawable icon = menu.findItem(mSortMode).getIcon();
			menu.findItem(R.id.action_sort).setIcon(icon);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * ActionBar菜单项点击
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this, "Selected Item: " + item.getTitle(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	// This method is specified as an onClick handler in the menu xml and will
	// take precedence over the Activity's onOptionsItemSelected method.
	// See res/menu/actions.xml for more info.
	public void onSort(MenuItem item) {
		mSortMode = item.getItemId();
		// Request a call to onPrepareOptionsMenu so we can change the sort icon
		invalidateOptionsMenu();
	}

	// The following callbacks are called for the
	// SearchView.OnQueryChangeListener
	// For more about using SearchView, see src/.../view/SearchView1.java and
	// SearchView2.java
	public boolean onQueryTextChange(String newText) {
		newText = newText.isEmpty() ? "" : "Query so far: " + newText;
		mSearchText.setText(newText);
		return true;
	}

	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this, "Searching for: " + query + "...",
				Toast.LENGTH_SHORT).show();
		return true;
	}
}
