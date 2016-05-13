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

import com.example.android.apis.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午10:34:22
 */
public class FragmentReceiveResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 代码动态初始化FrameLayout
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		FrameLayout frame = new FrameLayout(this);
		frame.setId(R.id.simple_fragment);
		// 设置布局文件到Activity
		setContentView(frame, lp);

		if (savedInstanceState == null) {
			// 添加Fragment
			Fragment newFragment = new ReceiveResultFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.simple_fragment, newFragment).commit();
		}
	}

	public static class ReceiveResultFragment extends Fragment {
		// 定义请求码
		static final private int GET_CODE = 0;
		// 展示结果的TextView
		private TextView mResults;

		private OnClickListener mGetListener = new OnClickListener() {
			public void onClick(View v) {
				// startActivityForResult跳转
				Intent intent = new Intent(getActivity(), SendResult.class);
				startActivityForResult(intent, GET_CODE);
			}
		};

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater
					.inflate(R.layout.receive_result, container, false);

			mResults = (TextView) v.findViewById(R.id.results);
			mResults.setText(mResults.getText(), TextView.BufferType.EDITABLE);
			Button getButton = (Button) v.findViewById(R.id.get);
			getButton.setOnClickListener(mGetListener);

			return v;
		}

		/**
		 * 在Fragment中OnActivityResult方法中接收Activity中返回的值
		 * 
		 */
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// 在onActivityResult方法中接收返回数据
			if (requestCode == GET_CODE) {

				Editable text = (Editable) mResults.getText();
				if (resultCode == RESULT_CANCELED) {
					text.append("(cancelled)");

				} else {
					text.append("(okay ");
					text.append(Integer.toString(resultCode));
					text.append(") ");
					if (data != null) {
						text.append(data.getAction());
					}
				}

				text.append("\n");
			}
		}
	}
}
