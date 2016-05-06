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

import java.util.Random;

import com.example.android.apis.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Android中各种Notification
 * 
 * @description：
 * @author ldm
 * @date 2016-4-29 上午8:38:03
 */
public class NotificationsActivity extends Activity {
	private Button nofify_app, notify_interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incoming_message);
		initViews();
		initEvents();
	}

	private void initEvents() {
		nofify_app.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showAppNotification();
			}
		});

		notify_interstitial = (Button) findViewById(R.id.notify_interstitial);
		notify_interstitial.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showInterstitialNotification();
			}
		});
	}

	private void initViews() {
		nofify_app = (Button) findViewById(R.id.notify_app);
		notify_interstitial = (Button) findViewById(R.id.notify_interstitial);
	}

	static Intent[] makeMessageIntentStack(Context context, CharSequence from,
			CharSequence msg) {
		Intent[] intents = new Intent[4];

		intents[0] = Intent.makeRestartActivityTask(new ComponentName(context,
				com.example.android.apis.ApiDemos.class));

		intents[1] = new Intent(context,
				com.example.android.apis.ApiDemos.class);
		intents[1].putExtra("com.example.android.apis.Path", "App");
		intents[2] = new Intent(context,
				com.example.android.apis.ApiDemos.class);
		intents[2]
				.putExtra("com.example.android.apis.Path", "App/Notification");

		intents[3] = new Intent(context, IncomingMessageView.class);
		intents[3].putExtra(IncomingMessageView.KEY_FROM, from);
		intents[3].putExtra(IncomingMessageView.KEY_MESSAGE, msg);

		return intents;
	}

	/**
	 * 显示APP的通知，点击后通知消失
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-29 上午9:00:30
	 */
	void showAppNotification() {
		// 状态栏通知的管理类，负责发通知、清楚通知等
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// 自定义一条通知
		CharSequence from = "Joe";
		CharSequence message;
		switch ((new Random().nextInt()) % 3) {
		case 0:
			message = "r u hungry?  i am starved";
			break;
		case 1:
			message = "im nearby u";
			break;
		default:
			message = "kthx. meet u for dinner. cul8r";
			break;
		}

		// 当点击通知时通过PendingIntent来执行指定页面跳转或取消通知栏消息操作
		PendingIntent contentIntent = PendingIntent.getActivities(this, 0,
				makeMessageIntentStack(this, from, message),
				PendingIntent.FLAG_CANCEL_CURRENT);

		String tickerText = "有一条新消息";

		// 构建Notification对象
		@SuppressWarnings("deprecation")
		Notification notif = new Notification(R.drawable.stat_sample,
				tickerText, System.currentTimeMillis());

		// 在此处设置在nority列表里的该norifycation得显示情况。
		notif.setLatestEventInfo(this, from, message, contentIntent);

		// 可以设置通知到来手机发出声音或震动等，这是设备为All
		notif.defaults = Notification.DEFAULT_ALL;

		/**
		 * 注意,我们使用出来。incoming_message ID 通知。它可以是任何整数,但我们使用 资源id字符串相关
		 * 通知。它将永远是一个独特的号码在你的 应用程序。
		 */
		nm.notify(R.layout.incoming_message, notif);
	}

	/**
	 * 跳转到指定页面的通知
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-29 上午9:00:16
	 */
	void showInterstitialNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		CharSequence from = "又有通知啦";
		CharSequence message;
		switch ((new Random().nextInt()) % 3) {
		case 0:
			message = "i am ready for some dinner";
			break;
		case 1:
			message = "how about thai down the block?";
			break;
		default:
			message = "meet u soon. dont b late!";
			break;
		}

		Intent intent = new Intent(this, IncomingMessageInterstitial.class);
		intent.putExtra(IncomingMessageView.KEY_FROM, from);
		intent.putExtra(IncomingMessageView.KEY_MESSAGE, message);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// 当点击通知时通过PendingIntent来执行指定页面跳转
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		String tickerText = getString(R.string.imcoming_message_ticker_text,
				message);
		@SuppressWarnings("deprecation")
		Notification notif = new Notification(R.drawable.stat_sample,
				tickerText, System.currentTimeMillis());
		notif.setLatestEventInfo(this, from, message, contentIntent);
		notif.defaults = Notification.DEFAULT_ALL;
		nm.notify(R.string.imcoming_message_ticker_text, notif);
	}

}
