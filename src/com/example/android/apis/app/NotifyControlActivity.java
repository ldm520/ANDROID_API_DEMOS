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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 通过启动或停止服务来管理通知功能
 * 
 * @description：
 * @author ldm
 * @date 2016-4-29 上午9:15:15
 */
public class NotifyControlActivity extends Activity {
	private Button notifyStart;// 启动通知服务
	private Button notifyStop;// 停止通知服务

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifying_controller);
		initWidgets();
	}

	private void initWidgets() {
		notifyStart = (Button) findViewById(R.id.notifyStart);
		notifyStart.setOnClickListener(mStartListener);
		notifyStop = (Button) findViewById(R.id.notifyStop);
		notifyStop.setOnClickListener(mStopListener);
	}

	private OnClickListener mStartListener = new OnClickListener() {
		public void onClick(View v) {
			// 启动Notification对应Service
			startService(new Intent(NotifyControlActivity.this,
					NotifyingService.class));
		}
	};

	private OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v) {
			// 停止Notification对应Service
			stopService(new Intent(NotifyControlActivity.this,
					NotifyingService.class));
		}
	};
}
