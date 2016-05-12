/*
 * Copyright (C) 2009 The Android Open Source Project
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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

/**
 * 前台运行的Service
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 上午11:22:40
 */
public class ForegroundService extends Service {
	// 定义ACTION
	static final String ACTION_FOREGROUND = "com.example.android.apis.FOREGROUND";
	static final String ACTION_BACKGROUND = "com.example.android.apis.BACKGROUND";
	// 反射获取类名

	private static final Class<?>[] mSetForegroundSignature = new Class[] { boolean.class };
	private static final Class<?>[] mStartForegroundSignature = new Class[] {
			int.class, Notification.class };
	private static final Class<?>[] mStopForegroundSignature = new Class[] { boolean.class };
	// 通知管理对象
	private NotificationManager mNM;
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];

	// 实现动态调用
	void invokeMethod(Method method, Object[] args) {
		try {
			method.invoke(this, args);
		} catch (InvocationTargetException e) {
			Log.w("ApiDemos", "Unable to invoke method", e);
		} catch (IllegalAccessException e) {
			Log.w("ApiDemos", "Unable to invoke method", e);
		}
	}

	// 启动前台运行
	void startForegroundCompat(int id, Notification notification) {
		if (mStartForeground != null) {
			mStartForegroundArgs[0] = Integer.valueOf(id);
			mStartForegroundArgs[1] = notification;
			invokeMethod(mStartForeground, mStartForegroundArgs);
			return;
		}

		mSetForegroundArgs[0] = Boolean.TRUE;
		invokeMethod(mSetForeground, mSetForegroundArgs);
		mNM.notify(id, notification);
	}

	/**
	 * 停止关前台服务
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 上午11:30:51
	 */
	void stopForegroundCompat(int id) {
		if (mStopForeground != null) {
			mStopForegroundArgs[0] = Boolean.TRUE;
			invokeMethod(mStopForeground, mStopForegroundArgs);
			return;
		}
		mNM.cancel(id);
		mSetForegroundArgs[0] = Boolean.FALSE;
		invokeMethod(mSetForeground, mSetForegroundArgs);
	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		try {
			mStartForeground = getClass().getMethod("startForeground",
					mStartForegroundSignature);
			mStopForeground = getClass().getMethod("stopForeground",
					mStopForegroundSignature);
			return;
		} catch (NoSuchMethodException e) {
			mStartForeground = mStopForeground = null;
		}
		try {
			mSetForeground = getClass().getMethod("setForeground",
					mSetForegroundSignature);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(
					"OS doesn't have Service.startForeground OR Service.setForeground!");
		}
	}

	@Override
	public void onDestroy() {
		//stopForeground
		stopForegroundCompat(R.string.foreground_service_started);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handleCommand(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent);
		// 表示系统Kill这个Service之后，如果重新创建这个Service时在调用onStartCommand
		// ，不会将最后的Intent作为参数传入，也就是说intent=null.
		// START_REDELIVER_INTENT则会传入被杀前未处理的最后一个Intent。
		return START_STICKY;
	}

	void handleCommand(Intent intent) {
		if (ACTION_FOREGROUND.equals(intent.getAction())) {
			CharSequence text = getText(R.string.foreground_service_started);
			@SuppressWarnings("deprecation")
			Notification notification = new Notification(
					R.drawable.stat_sample, text, System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, Controller.class), 0);
			notification.setLatestEventInfo(this,
					getText(R.string.local_service_label), text, contentIntent);

			startForegroundCompat(R.string.foreground_service_started,
					notification);

		} else if (ACTION_BACKGROUND.equals(intent.getAction())) {
			stopForegroundCompat(R.string.foreground_service_started);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	public static class Controller extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.foreground_service_controller);

			// Watch for button clicks.
			Button button = (Button) findViewById(R.id.start_foreground);
			button.setOnClickListener(mForegroundListener);
			button = (Button) findViewById(R.id.start_background);
			button.setOnClickListener(mBackgroundListener);
			button = (Button) findViewById(R.id.stop);
			button.setOnClickListener(mStopListener);
		}

		private OnClickListener mForegroundListener = new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ForegroundService.ACTION_FOREGROUND);
				intent.setClass(Controller.this, ForegroundService.class);
				startService(intent);
			}
		};

		private OnClickListener mBackgroundListener = new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ForegroundService.ACTION_BACKGROUND);
				intent.setClass(Controller.this, ForegroundService.class);
				startService(intent);
			}
		};

		private OnClickListener mStopListener = new OnClickListener() {
			public void onClick(View v) {
				stopService(new Intent(Controller.this, ForegroundService.class));
			}
		};
	}
}
