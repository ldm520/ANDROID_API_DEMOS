/*
 * Copyright (C) 2012 The Android Open Source Project
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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 这是一个为通知提供了用户界面的Activity
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午11:53:00
 */
public class IncomingMessageInterstitial extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incoming_message_interstitial);
		Button button = (Button) findViewById(R.id.notify_app);
		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				switchToApp();
			}
		});
	}

	/**
	 * 点击跳转
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 上午11:56:17
	 */
	void switchToApp() {
		CharSequence from = getIntent().getCharSequenceExtra(
				IncomingMessageView.KEY_FROM);
		CharSequence msg = getIntent().getCharSequenceExtra(
				IncomingMessageView.KEY_MESSAGE);
		Intent[] stack = NotificationsActivity.makeMessageIntentStack(this,
				from, msg);
		startActivities(stack);
		finish();
	}

}
