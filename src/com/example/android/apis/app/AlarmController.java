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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Example of scheduling one-shot and repeating alarms. See {@link OneShotAlarm}
 * for the code run when the one-shot alarm goes off, and {@link RepeatingAlarm}
 * for the code run when the repeating alarm goes off. <h4>Demo</h4>
 * App/Service/Alarm Controller
 */

/**
 * 单次Alarm事件和循环Alarm事件
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 上午10:25:18
 */
public class AlarmController extends Activity {
	Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_controller);
		// 初始化Button及设置对应监听事件
		Button button = (Button) findViewById(R.id.one_shot);
		button.setOnClickListener(mOneShotListener);
		button = (Button) findViewById(R.id.start_repeating);
		button.setOnClickListener(mStartRepeatingListener);
		button = (Button) findViewById(R.id.stop_repeating);
		button.setOnClickListener(mStopRepeatingListener);
	}

	private OnClickListener mOneShotListener = new OnClickListener() {
		public void onClick(View v) {
			// When the alarm goes off, we want to broadcast an Intent to our
			// BroadcastReceiver. Here we make an Intent with an explicit class
			// name to have our own receiver (which has been published in
			// AndroidManifest.xml) instantiated and called, and then create an
			// IntentSender to have the intent executed as a broadcast.
			// 单次Alarm
			Intent intent = new Intent(AlarmController.this, OneShotAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					AlarmController.this, 0, intent, 0);

			// We want the alarm to go off 30 seconds from now.
			// 获取日期
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			// 设置时间
			calendar.add(Calendar.SECOND, 30);

			// Schedule the alarm!
			// 设置定时发出Alarm
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			// 闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用绝对时间，状态值为0
			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

			// Tell the user about what we did.
			if (mToast != null) {
				mToast.cancel();
			}
			mToast = Toast.makeText(AlarmController.this,
					R.string.one_shot_scheduled, Toast.LENGTH_LONG);
			mToast.show();
		}
	};

	private OnClickListener mStartRepeatingListener = new OnClickListener() {
		public void onClick(View v) {
			// When the alarm goes off, we want to broadcast an Intent to our
			// BroadcastReceiver. Here we make an Intent with an explicit class
			// name to have our own receiver (which has been published in
			// AndroidManifest.xml) instantiated and called, and then create an
			// IntentSender to have the intent executed as a broadcast.
			// Note that unlike above, this IntentSender is configured to
			// allow itself to be sent multiple times.
			// 重复Alarm
			Intent intent = new Intent(AlarmController.this,
					RepeatingAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					AlarmController.this, 0, intent, 0);

			// We want the alarm to go off 30 seconds from now.
			long firstTime = SystemClock.elapsedRealtime();
			firstTime += 15 * 1000;

			// Schedule the alarm!
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			// 设置重复Alarm
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
					15 * 1000, sender);

			// Tell the user about what we did.
			if (mToast != null) {
				mToast.cancel();
			}
			mToast = Toast.makeText(AlarmController.this,
					R.string.repeating_scheduled, Toast.LENGTH_LONG);
			mToast.show();
		}
	};
	/**
	 * 停止Alarm
	 */
	private OnClickListener mStopRepeatingListener = new OnClickListener() {
		public void onClick(View v) {
			// Create the same intent, and thus a matching IntentSender, for
			// the one that was scheduled.
			Intent intent = new Intent(AlarmController.this,
					RepeatingAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					AlarmController.this, 0, intent, 0);

			// And cancel the alarm.
			// 获取AlarmManager
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			// 取消Alarm
			am.cancel(sender);

			// Tell the user about what we did.
			if (mToast != null) {
				mToast.cancel();
			}
			mToast = Toast.makeText(AlarmController.this,
					R.string.repeating_unscheduled, Toast.LENGTH_LONG);
			mToast.show();
		}
	};
	/*********** AlarmManager的常用方法介绍 ***************************************************/
	/**
	 * AlarmManager的常用方法有三个：
	 * 
	 * （1）set(int type，long startTime，PendingIntent pi)：
	 * 
	 * 该方法用于设置一次性闹钟，第一个参数表示闹钟类型，第二个参数表示闹钟执行时间，第三个参数表示闹钟响应动作。
	 * 
	 * （2）setRepeating(int type，long startTime，long intervalTime，PendingIntent
	 * pi)：
	 * 
	 * 该方法用于设置重复闹钟，第一个参数表示闹钟类型，第二个参数表示闹钟首次执行时间，第三个参数表示闹钟两次执行的间隔时间，第三个参数表示闹钟响应动作。
	 * 
	 * （3）setInexactRepeating（int type，long startTime，long
	 * intervalTime，PendingIntent pi）：
	 * 
	 * 该方法也用于设置重复闹钟，与第二个方法相似，不过其两个闹钟执行的间隔时间不是固定的而已。
	 * 
	 * 3、三个方法各个参数详悉：
	 * 
	 * （1）int type： 闹钟的类型，常用的有5个值：AlarmManager.ELAPSED_REALTIME、
	 * AlarmManager.ELAPSED_REALTIME_WAKEUP、AlarmManager.RTC、
	 * AlarmManager.RTC_WAKEUP、AlarmManager.POWER_OFF_WAKEUP。
	 * 
	 * AlarmManager.ELAPSED_REALTIME表示闹钟在手机睡眠状态下不可用，该状态下闹钟使用相对时间（相对于系统启动开始），
	 * 状态值为3；
	 * 
	 * AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟也使用相对时间，
	 * 状态值为2；
	 * 
	 * AlarmManager.RTC表示闹钟在睡眠状态下不可用，该状态下闹钟使用绝对时间，即当前系统时间，状态值为1；
	 * 
	 * AlarmManager.RTC_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用绝对时间，状态值为0；
	 * 
	 * AlarmManager.POWER_OFF_WAKEUP表示闹钟在手机关机状态下也能正常进行提示功能，所以是5个状态中用的最多的状态之一，
	 * 该状态下闹钟也是用绝对时间，状态值为4；不过本状态好像受SDK版本影响，某些版本并不支持；
	 * 
	 * （2）long startTime：
	 * 闹钟的第一次执行时间，以毫秒为单位，可以自定义时间，不过一般使用当前时间。需要注意的是，本属性与第一个属性（type）密切相关，如果第一个参数对
	 * 应的闹钟使用的是相对时间（ELAPSED_REALTIME和ELAPSED_REALTIME_WAKEUP），那么本属性就得使用相对时间（相对于
	 * 系统启动时间来说），比如当前时间就表示为：SystemClock.elapsedRealtime()；如果第一个参数对应的闹钟使用的是绝对时间
	 * （RTC、RTC_WAKEUP、POWER_OFF_WAKEUP），那么本属性就得使用绝对时间，比如当前时间就表示
	 * 为：System.currentTimeMillis()。
	 * 
	 * （3）long intervalTime：对于后两个方法来说，存在本属性，表示两次闹钟执行的间隔时间，也是以毫秒为单位。
	 * 
	 * （4）PendingIntent pi：
	 * 绑定了闹钟的执行动作，比如发送一个广播、给出提示等等。PendingIntent是Intent的封装类。需要注意的是
	 * ，如果是通过启动服务来实现闹钟提 示的话，PendingIntent对象的获取就应该采用Pending.getService(Context
	 * c,int i,Intent intent,int j)方法；如果是通过广播来实现闹钟提示的话，PendingIntent对象的获取就应该采用
	 * PendingIntent.getBroadcast(Context c,int i,Intent intent,int
	 * j)方法；如果是采用Activity的方式来实现闹钟提示的话，PendingIntent对象的获取就应该采用
	 * PendingIntent.getActivity(Context c,int i,Intent intent,int
	 * j)方法。如果这三种方法错用了的话，虽然不会报错，但是看不到闹钟提示效果。
	 */
}
