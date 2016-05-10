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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Activity的重启方法：Rcreate()
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 上午10:06:45
 */
public class ActivityRecreate extends Activity {
	int mCurTheme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			// // 从onSaveInstanceState(Bundle outState)方法中保存的数据取出Theme
			mCurTheme = savedInstanceState.getInt("theme");

			// 切换页面主题Theme
			switch (mCurTheme) {
			case android.R.style.Theme_Holo_Light:
				mCurTheme = android.R.style.Theme_Holo_Dialog;
				break;
			case android.R.style.Theme_Holo_Dialog:
				mCurTheme = android.R.style.Theme_Holo;
				break;
			default:
				mCurTheme = android.R.style.Theme_Holo_Light;
				break;
			}
			setTheme(mCurTheme);
		}

		setContentView(R.layout.activity_recreate);

		// Watch for button clicks.
		Button button = (Button) findViewById(R.id.recreate);
		button.setOnClickListener(mRecreateListener);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// 把当前Theme保存
		savedInstanceState.putInt("theme", mCurTheme);
	}

	private OnClickListener mRecreateListener = new OnClickListener() {
		public void onClick(View v) {
			/**
			 * 调用recreate方法重新创建Activity会比正常启动Activity多调用了onSaveInstanceState
			 * ()和onRestoreInstanceState
			 * ()两个方法，onSaveInstanceState()会在onCreate方法之前调用。
			 * 所以可以在onCreate()方法中获取onSaveInstanceState()保存的Theme数据
			 */
			recreate();
		}
	};
}
