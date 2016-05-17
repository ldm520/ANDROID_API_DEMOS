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
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 自定义测试工具类
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 上午11:51:39
 */
public class LocalSampleInstrumentation extends Instrumentation {
	public abstract static class ActivityRunnable implements Runnable {
		public final Activity activity;

		public ActivityRunnable(Activity _activity) {
			activity = _activity;
		}
	}

	@Override
	public void onCreate(Bundle arguments) {
		super.onCreate(arguments);
		// 启动测试
		start();
	}

	@Override
	public void onStart() {
		super.onStart();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(getTargetContext(), SaveRestoreState.class);
		SaveRestoreState activity = (SaveRestoreState) startActivitySync(intent);

		runOnMainSync(new ActivityRunnable(activity) {
			public void run() {
				((SaveRestoreState) activity).setSavedText("");
			}
		});

		// 模拟用户操作指令
		sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN,
				KeyEvent.KEYCODE_SHIFT_LEFT));
		sendCharacterSync(KeyEvent.KEYCODE_H);
		sendKeySync(new KeyEvent(KeyEvent.ACTION_UP,
				KeyEvent.KEYCODE_SHIFT_LEFT));
		sendCharacterSync(KeyEvent.KEYCODE_E);
		sendCharacterSync(KeyEvent.KEYCODE_L);
		sendCharacterSync(KeyEvent.KEYCODE_L);
		sendCharacterSync(KeyEvent.KEYCODE_O);

		//等待Activity完成
		waitForIdleSync();
		//任务完成，结束
		finish(Activity.RESULT_OK, null);
	}
}
