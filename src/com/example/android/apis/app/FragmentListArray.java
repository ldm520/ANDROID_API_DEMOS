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

import com.example.android.apis.Shakespeare;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * ListFragment使用
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午5:39:25
 */
public class FragmentListArray extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 判断当前Fragment是否存在
		if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
			// 初始化ArrayListFragment
			ArrayListFragment list = new ArrayListFragment();
			// 添加Fragment到Activity中
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, list).commit();
		}
	}

	/**
	 * 定义ListFragment的子类ArrayListFragment
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 上午10:12:56
	 */
	public static class ArrayListFragment extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// 设置数据
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, Shakespeare.TITLES));
		}

		// ListView的item点击事件
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}
}
