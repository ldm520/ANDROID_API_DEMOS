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

import com.example.android.apis.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 点击 通知展开页面
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午11:57:15
 */
public class IncomingMessageView extends Activity {
	static final public String KEY_FROM = "from";
	static final public String KEY_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incoming_message_view);
		// 设置数据
		((TextView) findViewById(R.id.from)).setText(getIntent()
				.getCharSequenceExtra(KEY_FROM));
		((TextView) findViewById(R.id.message)).setText(getIntent()
				.getCharSequenceExtra(KEY_MESSAGE));

		// 初始化NotificationManager
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 取消通知
		nm.cancel(R.string.imcoming_message_ticker_text);
	}
}
