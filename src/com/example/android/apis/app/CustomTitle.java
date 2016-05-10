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

package com.example.android.apis.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * 自定义通用Title对应Activity
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 下午3:48:18
 */
public class CustomTitle extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置自定义FEATURE
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title);
		// 必须出现在setContentView之后，其意思就是告诉系统，自定义的布局是R.layout.custom_title_1
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title_1);

		final TextView leftText = (TextView) findViewById(R.id.left_text);
		final TextView rightText = (TextView) findViewById(R.id.right_text);
		final EditText leftTextEdit = (EditText) findViewById(R.id.left_text_edit);
		final EditText rightTextEdit = (EditText) findViewById(R.id.right_text_edit);
		Button leftButton = (Button) findViewById(R.id.left_text_button);
		Button rightButton = (Button) findViewById(R.id.right_text_button);

		leftButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 设置左侧TextView文字
				leftText.setText(leftTextEdit.getText());
			}
		});
		rightButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 设置右侧TextView文字
				rightText.setText(rightTextEdit.getText());
			}
		});
	}
}
