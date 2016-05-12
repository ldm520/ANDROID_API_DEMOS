/*
 * Copyright (C) 2008 The Android Open Source Project
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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Dialog样式的Activity
 * 
 * @description：
 * @author ldm
 * @date 2016-5-11 下午4:58:50
 */
public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);
		// 标题栏左侧的图标
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.dialog_activity);
		// 设置标题
		getWindow().setTitle("This is just a test");
		// 设置图片资源
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);

		Button button = (Button) findViewById(R.id.add);
		button.setOnClickListener(mAddContentListener);
		button = (Button) findViewById(R.id.remove);
		button.setOnClickListener(mRemoveContentListener);
	}

	private OnClickListener mAddContentListener = new OnClickListener() {
		public void onClick(View v) {
			// 添加ImageView
			LinearLayout layout = (LinearLayout) findViewById(R.id.inner_content);
			ImageView iv = new ImageView(DialogActivity.this);
			iv.setImageDrawable(getResources().getDrawable(
					R.drawable.icon48x48_1));
			iv.setPadding(4, 4, 4, 4);
			layout.addView(iv);
		}
	};

	private OnClickListener mRemoveContentListener = new OnClickListener() {
		public void onClick(View v) {
			// 移出ImageView
			LinearLayout layout = (LinearLayout) findViewById(R.id.inner_content);
			int num = layout.getChildCount();
			if (num > 0) {
				layout.removeViewAt(num - 1);
			}
		}
	};
}
