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
 * Fragment栈管理
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午11:03:18
 */
public class FragmentStack extends Activity {
	int mStackLevel = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_stack);
		Button button = (Button) findViewById(R.id.new_fragment);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addFragmentToStack();
			}
		});
		button = (Button) findViewById(R.id.delete_fragment);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 从activity的后退栈中弹出fragment们（这可以模拟后退键引发的动作）
				getFragmentManager().popBackStack();
			}
		});

		if (savedInstanceState == null) {
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
		// 保存状态
		outState.putInt("level", mStackLevel);
	}

	/**
	 * 添加回退栈
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 上午11:06:59
	 */
	void addFragmentToStack() {
		mStackLevel++;

		// 初始化Fragment
		Fragment newFragment = CountingFragment.newInstance(mStackLevel);

		// 添加Fragment到Activity中
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// 添加回退栈,重新返回的时候会重新调用fragment的onCreateView(),只要不走onCreate()就不是重新加载的,
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

		@SuppressWarnings("deprecation")
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
