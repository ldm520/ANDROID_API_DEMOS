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
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SimpleCursorAdapter;
import android.widget.SearchView.OnQueryTextListener;

/**
 * 通过CursorLoader加载手机中联系人信息
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 上午10:44:56
 */
public class LoaderCursor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getFragmentManager();
		// 添加展示列表的Fragment
		if (fm.findFragmentById(android.R.id.content) == null) {
			CursorLoaderListFragment list = new CursorLoaderListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	/**
	 * 自定义列表ListFragment
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-16 上午10:45:43
	 */
	public static class CursorLoaderListFragment extends ListFragment implements
			OnQueryTextListener, OnCloseListener,
			LoaderManager.LoaderCallbacks<Cursor> {

		// 与数据库打交道的Adapter,数据来源指定是数据库
		SimpleCursorAdapter mAdapter;

		// 搜索
		SearchView mSearchView;

		// 当前过滤的关键字
		String mCurFilter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// 设置无数据时的提示语
			setEmptyText("No phone numbers");
			// 添加菜单功能
			setHasOptionsMenu(true);
			// 初始化Adapter
			mAdapter = new SimpleCursorAdapter(getActivity(),
					android.R.layout.simple_list_item_2, null, new String[] {
							Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS },
					new int[] { android.R.id.text1, android.R.id.text2 }, 0);
			setListAdapter(mAdapter);
			// 设置不显示ListView,等待加载完成以后显示
			setListShown(false);
			// 初始化加载器
			getLoaderManager().initLoader(0, null, this);
		}

		public static class MySearchView extends SearchView {
			public MySearchView(Context context) {
				super(context);
			}

			@Override
			public void onActionViewCollapsed() {
				setQuery("", false);
				super.onActionViewCollapsed();
			}
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuItem item = menu.add("Search");
			item.setIcon(android.R.drawable.ic_menu_search);
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
			mSearchView = new MySearchView(getActivity());
			mSearchView.setOnQueryTextListener(this);
			mSearchView.setOnCloseListener(this);
			mSearchView.setIconifiedByDefault(true);
			item.setActionView(mSearchView);
		}

		public boolean onQueryTextChange(String newText) {

			String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
			if (mCurFilter == null && newFilter == null) {
				return true;
			}
			if (mCurFilter != null && mCurFilter.equals(newFilter)) {
				return true;
			}
			mCurFilter = newFilter;
			getLoaderManager().restartLoader(0, null, this);
			return true;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			return true;
		}

		@Override
		public boolean onClose() {
			if (!TextUtils.isEmpty(mSearchView.getQuery())) {
				mSearchView.setQuery(null, true);
			}
			return true;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentComplexList", "Item clicked: " + id);
		}

		static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
				Contacts._ID, Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS,
				Contacts.CONTACT_PRESENCE, Contacts.PHOTO_ID,
				Contacts.LOOKUP_KEY, };

		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri baseUri;
			if (mCurFilter != null) {
				baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
						Uri.encode(mCurFilter));
			} else {
				baseUri = Contacts.CONTENT_URI;
			}

			// 创建一个新的装载器
			String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
					+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
					+ Contacts.DISPLAY_NAME + " != '' ))";
			return new CursorLoader(getActivity(), baseUri,
					CONTACTS_SUMMARY_PROJECTION, select, null,
					Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		}

		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// 这里将新的Cursor交给mAdapter，以便ListView能够显示，在创建mAdapter的时候传入的Cursor为空，其实是没有数据
			// 的，在这里加载好了，就把这个新的Curosr对象传进去，让ListView
			// 显示，这个地方很多网友搞不明白，这里多解释一下
			mAdapter.swapCursor(data);

			// 现在显示ListView
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		public void onLoaderReset(Loader<Cursor> loader) {
			// 当这个Loader被重置时，也就是调用了restartLoader方法是，使以前的数据无效
			mAdapter.swapCursor(null);
		}
	}

}
