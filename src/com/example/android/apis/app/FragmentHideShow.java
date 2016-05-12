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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 动态实现Fragment的显示与隐藏
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午5:15:44
 */
public class FragmentHideShow extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_hide_show);
		// 实例化FragmentManager
		FragmentManager fm = getFragmentManager();
		addShowHideListener(R.id.frag1hide, fm.findFragmentById(R.id.fragment1));
		addShowHideListener(R.id.frag2hide, fm.findFragmentById(R.id.fragment2));
	}

	/**
	 * 隐藏或显示监听
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 下午5:18:05
	 */
	void addShowHideListener(int buttonId, final Fragment fragment) {
		// 初始化Button
		final Button button = (Button) findViewById(buttonId);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				// 设置动画
				ft.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);
				// 根据Fragment的状态设置对应Button文字
				if (fragment.isHidden()) {
					ft.show(fragment);
					button.setText("Hide");
				} else {
					ft.hide(fragment);
					button.setText("Show");
				}
				ft.commit();
			}
		});
	}

	/**
	 * 这个Fragment是放在布局文件中的
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 下午5:22:54
	 */
	public static class FirstFragment extends Fragment {
		TextView mTextView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.labeled_text_edit, container,
					false);
			View tv = v.findViewById(R.id.msg);
			((TextView) tv)
					.setText("The fragment saves and restores this text.");
			mTextView = (TextView) v.findViewById(R.id.saved);
			if (savedInstanceState != null) {
				mTextView.setText(savedInstanceState.getCharSequence("text"));
			}
			return v;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putCharSequence("text", mTextView.getText());
		}
	}

	/**
	 * 这个Fragment也是放在布局文件中的
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 下午5:22:54
	 */
	public static class SecondFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.labeled_text_edit, container,
					false);
			View tv = v.findViewById(R.id.msg);
			((TextView) tv)
					.setText("The TextView saves and restores this text.");
			((TextView) v.findViewById(R.id.saved)).setSaveEnabled(true);
			return v;
		}
	}
}
