/*
 * Copyright (C) 2007 The Android Open Source Project
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

import com.example.android.apis.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * 文字自动提示输入框AutoCompleteTextView
 * 
 * @description：
 * @author ldm
 * @date 2016-5-17 上午11:06:24
 */
public class AutoComplete4 extends Activity {
	private static final int COLUMN_DISPLAY_NAME = 1;
	// 查询联系人字段
	public static final String[] CONTACT_PROJECTION = new String[] {
			Contacts._ID, Contacts.DISPLAY_NAME };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autocomplete_4);
		// 通过上下文获取到ContentResolver
		ContentResolver content = getContentResolver();
		// 初始化数据库操作Cursor
		Cursor cursor = content.query(Contacts.CONTENT_URI, CONTACT_PROJECTION,
				null, null, null);
		// 联系人适配器
		ContactListAdapter adapter = new ContactListAdapter(this, cursor);

		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.edit);
		// 设置适配器
		textView.setAdapter(adapter);
	}

	/**
	 * 获取联系人的数据适配器
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-17 上午11:14:14
	 */
	public static class ContactListAdapter extends CursorAdapter implements
			Filterable {

		private ContentResolver mContent;

		@SuppressWarnings("deprecation")
		public ContactListAdapter(Context context, Cursor c) {
			super(context, c);
			mContent = context.getContentResolver();
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_dropdown_item_1line, parent, false);
			view.setText(cursor.getString(COLUMN_DISPLAY_NAME));
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((TextView) view).setText(cursor.getString(COLUMN_DISPLAY_NAME));
		}

		@Override
		public String convertToString(Cursor cursor) {
			return cursor.getString(COLUMN_DISPLAY_NAME);
		}

		// 后台查询，获取联系人数据
		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			FilterQueryProvider filter = getFilterQueryProvider();
			if (filter != null) {
				return filter.runQuery(constraint);
			}

			Uri uri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
					Uri.encode(constraint.toString()));
			return mContent.query(uri, CONTACT_PROJECTION, null, null, null);
		}

	}

}