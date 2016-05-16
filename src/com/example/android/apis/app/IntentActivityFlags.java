package com.example.android.apis.app;

import com.example.android.apis.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Intent跳转Flag
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 下午12:03:47
 */
public class IntentActivityFlags extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.intent_activity_flags);
		Button button = (Button) findViewById(R.id.flag_activity_clear_task);
		button.setOnClickListener(mFlagActivityClearTaskListener);
		button = (Button) findViewById(R.id.flag_activity_clear_task_pi);
		button.setOnClickListener(mFlagActivityClearTaskPIListener);
	}

	// 启动一个Intents Array来重建一个“返回栈”
	private Intent[] buildIntentsToViewsLists() {
		Intent[] intents = new Intent[3];

		// intents[0]:重建一个Activity
		// Task并保持为根(root)状态。也就是说，com.example.Android.apis.ApiDemos.class这个Activity在启动后将会作为这一个Activity
		// Task的入口Activity。
		intents[0] = Intent.makeRestartActivityTask(new ComponentName(this,
				com.example.android.apis.ApiDemos.class));

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClass(IntentActivityFlags.this,
				com.example.android.apis.ApiDemos.class);
		intent.putExtra("com.example.android.apis.Path", "Views");
		// Intent[1]和Intent[2]均是通过setClass()来指明具体的意图
		// 并向分别向各自键为："com.example.android.apis.Path"的变量中存入"Views "和"Views/Lists"两个值
		// 用以指示ApiDemos获取相应的内容来填充其List列表。
		intents[1] = intent;

		intent = new Intent(Intent.ACTION_MAIN);
		intent.setClass(IntentActivityFlags.this,
				com.example.android.apis.ApiDemos.class);
		intent.putExtra("com.example.android.apis.Path", "Views/Lists");

		intents[2] = intent;
		return intents;
	}

	/**
	 * 点击第一个”FLAG_ACTIVITY_CLEAR_TASK”的按钮，程序界面会跳转到Views->Lists示例的界面
	 */
	private OnClickListener mFlagActivityClearTaskListener = new OnClickListener() {
		public void onClick(View v) {
			// 点击第一个”FLAG_ACTIVITY_CLEAR_TASK”的按钮，程序界面会跳转到Views->Lists示例的界面
			/**
			 * startActivities(buildIntentsToViewsLists());
			 * 我们经常用到的是startActivity
			 * (Intent),而startActivities(Intent[])的作用与之完全相同，
			 * 无非就是将Intent[]中的三个Intent所指向的跳转目标Activity从后至前依次添加到当前Activity栈中
			 * ，如果大家在点击完按钮后用“Back”键返回，会发现返回的顺序和Intent[]中的顺序从后至前一致。
			 */
			startActivities(buildIntentsToViewsLists());
		}
	};
	/**
	 * 跳转到ApiDemos->Views->Lists
	 */
	private OnClickListener mFlagActivityClearTaskPIListener = new OnClickListener() {
		public void onClick(View v) {
			Context context = IntentActivityFlags.this;
			// 跳转到ApiDemos->Views->Lists
			/**
			 * 运用了PendingIntent，它的功能实际上是事先定义好一个Intent,然后在满足条件的时候启动(即使当前Context销毁
			 * ，外部也可以从初始化PendingIntent中的Context启动这一Intent)。
			 */
			PendingIntent pi = PendingIntent.getActivities(context, 0,
					buildIntentsToViewsLists(),
					PendingIntent.FLAG_UPDATE_CURRENT);

			try {
				pi.send();
			} catch (CanceledException e) {
				Log.w("IntentActivityFlags", "Failed sending PendingIntent", e);
			}
		}
	};
}
