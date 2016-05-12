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

import com.example.android.apis.R;
import com.example.android.apis.Shakespeare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Demonstration of using fragments to implement different activity layouts.
 * This sample provides a different layout (and activity flow) when run in
 * landscape.
 */
public class FragmentLayout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 这个Layout中只包含了TitlesFragment。这就意味着，在竖屏模式下，只会显示剧目列表。所以当用户点击其中一项的时候，
		 * 应用程序会启动一个新的Activity来显示这个剧目的简介，而不是加载一个Fragment进来。
		 */
		setContentView(R.layout.fragment_layout);
	}

	/**
	 * Fragment显示了这个剧目的简介信息
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 下午5:31:53
	 */
	public static class DetailsActivity extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// 如果在横屏状态下这个Activity会自动Finish，这样main
			// Activity就能显示DetailsFragment和TitlesFragment了。这会发生在用户在最开始的时候显示DetailsActivity的时候是竖屏的，但是后来切换到了横屏（会重新创建当前的Activity）。
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 判断手机屏幕方向
				finish();
				return;
			}

			if (savedInstanceState == null) {
				DetailsFragment details = new DetailsFragment();
				details.setArguments(getIntent().getExtras());
				getFragmentManager().beginTransaction()
						.add(android.R.id.content, details).commit();
			}
		}
	}

	/**
	 * 这是“顶层”Fragment，该列表 用户可以挑选。 ListFragment派生，大部分列表的功能由ListFragment提供。
	 * 　　当用户选择一个Title时
	 * ，代码需要做出两种行为，一种是在同一个activity中显示创建并显示摘要fragment，另一种是启动一个新的activity。
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 下午5:27:07
	 */

	public static class TitlesFragment extends ListFragment {
		boolean mDualPane;
		int mCurCheckPosition = 0;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// 设置数据
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_activated_1,
					Shakespeare.TITLES));

			View detailsFrame = getActivity().findViewById(R.id.details);
			mDualPane = detailsFrame != null
					&& detailsFrame.getVisibility() == View.VISIBLE;

			if (savedInstanceState != null) {
				mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
			}

			if (mDualPane) {
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				showDetails(mCurCheckPosition);
			}
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putInt("curChoice", mCurCheckPosition);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			showDetails(position);
		}

		/*
		 * 显示详细信息
		 */
		void showDetails(int index) {
			mCurCheckPosition = index;

			if (mDualPane) {
				getListView().setItemChecked(index, true);

				DetailsFragment details = (DetailsFragment) getFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null || details.getShownIndex() != index) {
					details = DetailsFragment.newInstance(index);
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					if (index == 0) {
						ft.replace(R.id.details, details);
					} else {
						ft.replace(R.id.a_item, details);
					}
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				}

			} else {
				Intent intent = new Intent();
				intent.setClass(getActivity(), DetailsActivity.class);
				intent.putExtra("index", index);
				startActivity(intent);
			}
		}
	}

	public static class DetailsFragment extends Fragment {
		public static DetailsFragment newInstance(int index) {
			DetailsFragment f = new DetailsFragment();

			Bundle args = new Bundle();
			args.putInt("index", index);
			f.setArguments(args);

			return f;
		}

		public int getShownIndex() {
			return getArguments().getInt("index", 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (container == null) {
				return null;
			}

			ScrollView scroller = new ScrollView(getActivity());
			TextView text = new TextView(getActivity());
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getActivity()
							.getResources().getDisplayMetrics());
			text.setPadding(padding, padding, padding, padding);
			scroller.addView(text);
			text.setText(Shakespeare.DIALOGUE[getShownIndex()]);
			return scroller;
		}
	}

}
