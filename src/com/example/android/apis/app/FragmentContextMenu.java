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
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * Fragment中实现上下文菜单功能
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午3:54:49
 */
public class FragmentContextMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 创建ContextMenuFragment对象，并将其作为activity中唯一的内容
		ContextMenuFragment content = new ContextMenuFragment();
		getFragmentManager().beginTransaction()
				.add(android.R.id.content, content).commit();
	}

	public static class ContextMenuFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// 关联界面
			View root = inflater.inflate(R.layout.fragment_context_menu,
					container, false);
			// 注册上下文菜单
			registerForContextMenu(root.findViewById(R.id.long_press));
			return root;
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			// 添加菜单
			menu.add(Menu.NONE, R.id.a_item, Menu.NONE, "Menu A");
			menu.add(Menu.NONE, R.id.b_item, Menu.NONE, "Menu B");
		}

		/**
		 * 菜单点击事件
		 */
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.a_item:
				Toast.makeText(getActivity(), "Item 1a was chosen",
						Toast.LENGTH_SHORT).show();
				return true;
			case R.id.b_item:
				Toast.makeText(getActivity(), "Item 1b was chosen",
						Toast.LENGTH_SHORT).show();
				return true;
			}
			return super.onContextItemSelected(item);
		}
	}
}
