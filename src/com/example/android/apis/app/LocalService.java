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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.android.apis.R;
// Need the following import to get access to the app resources, since this
// class is in a sub-package.

/**
 * 本地服务Service
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 上午11:59:00
 */

public class LocalService extends Service {
	// 通知管理器
	private NotificationManager mNM;

	// 定义唯一的NOTIFICATIONID
	private int NOTIFICATION = R.string.local_service_started;

	/**
	 * 
	 * @description：http://www.cnblogs.com/innost/archive/2011/01/09/1931456.html
	 * @author ldm
	 * @date 2016-5-16 上午11:59:56
	 */
	public class LocalBinder extends Binder {
		LocalService getService() {
			return LocalService.this;
		}
	}

	@Override
	public void onCreate() {
		// 实例化NotificationManager
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 显示通知
		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/**
		 * 在运行onStartCommand后service进程被kill后，那将保留在开始状态，但是不保留那些传入的intent。
		 * 不久后service就会再次尝试重新创建，因为保留在开始状态，在创建
		 * service后将保证调用onstartCommand。如果没有传递任何开始命令给service，那将获取到null的intent
		 */
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		mNM.cancel(NOTIFICATION);
		Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	@SuppressWarnings("deprecation")
	private void showNotification() {
		// 标题
		CharSequence text = getText(R.string.local_service_started);
		// 图标
		Notification notification = new Notification(R.drawable.stat_sample,
				text, System.currentTimeMillis());
		// 点击通知跳转
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, LocalServiceActivities.Controller.class), 0);
		notification.setLatestEventInfo(this,
				getText(R.string.local_service_label), text, contentIntent);
		// 发送通知
		mNM.notify(NOTIFICATION, notification);
	}
}
