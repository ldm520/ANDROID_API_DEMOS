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
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * 自定义Fragment切换动画
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午3:56:54
 */
public class FragmentCustomAnimations extends Activity {
	int mStackLevel = 1;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_stack);
		// 初化控件及设备监听
		button = (Button) findViewById(R.id.new_fragment);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//
				addFragmentToStack();
			}
		});

		if (savedInstanceState == null) {
			// Do first time initialization -- add initial fragment.
			Fragment newFragment = CountingFragment.newInstance(mStackLevel);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.simple_fragment, newFragment).commit();
		} else {
			mStackLevel = savedInstanceState.getInt("level");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("level", mStackLevel);
	}

	void addFragmentToStack() {
		mStackLevel++;

		Fragment newFragment = CountingFragment.newInstance(mStackLevel);

		// FragmentTransaction对fragment进行添加,移除,替换,以及执行其他动作。
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		// 设置自定义动画
		ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
				R.animator.fragment_slide_left_exit,
				R.animator.fragment_slide_right_enter,
				R.animator.fragment_slide_right_exit);
		ft.replace(R.id.simple_fragment, newFragment);
		/**
		 * 在调用commit()之前, 你可能想调用 addToBackStack(),将事务添加到一个fragment事务的back stack.
		 * 这个back stack由activity管理, 并允许用户通过按下 BACK 按键返回到前一个fragment状态.
		 */
		ft.addToBackStack(null);
		ft.commit();
	}

	public static class CountingFragment extends Fragment {
		int mNum;

		static CountingFragment newInstance(int num) {
			CountingFragment f = new CountingFragment();
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.hello_world, container, false);
			View tv = v.findViewById(R.id.text);
			((TextView) tv).setText("Fragment #" + mNum);
			tv.setBackgroundDrawable(getResources().getDrawable(
					android.R.drawable.gallery_thumb));
			return v;
		}
	}

}
