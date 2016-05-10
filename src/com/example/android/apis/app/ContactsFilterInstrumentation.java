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
import android.app.Instrumentation;
import android.content.Intent;
import android.view.KeyEvent;
import android.os.Bundle;
import android.util.Log;

/**
 * 自定义自动化测试类
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 下午3:12:10
 */
public class ContactsFilterInstrumentation extends Instrumentation {
	@Override
	public void onCreate(Bundle arguments) {
		super.onCreate(arguments);
		start();
	}

	@Override
	public void onStart() {
		super.onStart();
		// 作为Task中第一个Activity启动
		Intent intent = new Intent(Intent.ACTION_MAIN);
		// //相当于singleTask
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName(getTargetContext(), "com.android.phone.Dialer");
		// startActivitySync是一个同步方式启动Activity的功能，在正常的startActivity()之后，
		// 一直进入等待状态，直到Activity运行起来该函数才返回，这样，当我们想知道一个Activity的启动性能的时候，这个是非常重要的实现。
		Activity activity = startActivitySync(intent);

		Log.i("ContactsFilterInstrumentation", "Started: " + activity);

		// 发送键盘和鼠标消息给当前有焦点的窗口
		sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_M));
		sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_M));
		sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A));
		sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_A));

		// 等待Activity空闲时，即等待Activity启动完毕
		waitForIdleSync();

		Log.i("ContactsFilterInstrumentation", "Done!");
		//启动完毕后关闭
		finish(Activity.RESULT_OK, null);
	}
}
