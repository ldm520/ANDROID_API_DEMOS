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
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;

/**
 * This is an example implementation of the {@link android.app.Instrumentation}
 * class, allowing you to run tests against application code. The
 * instrumentation implementation here is loaded into the application's process,
 * for controlling and monitoring what it does.
 */
public class ContactsSelectInstrumentation extends Instrumentation {
	@Override
	public void onCreate(Bundle arguments) {
		super.onCreate(arguments);

		start();
	}

	@Override
	public void onStart() {
		super.onStart();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName(getTargetContext(), "com.android.phone.Dialer");
		Activity activity = startActivitySync(intent);

		Log.i("ContactsSelectInstrumentation", "Started: " + activity);

		/**
		 * ActivityMonitor是用来监视应用中单个活动的，它可以用来监视一些指定的意图。
		 */
		ActivityMonitor am = addMonitor(IntentFilter.create(Intent.ACTION_VIEW,
				ContactsContract.Contacts.CONTENT_ITEM_TYPE), null, true);

		sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN,
				KeyEvent.KEYCODE_DPAD_DOWN));
		sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN));
		sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN,
				KeyEvent.KEYCODE_DPAD_CENTER));
		sendKeySync(new KeyEvent(KeyEvent.ACTION_UP,
				KeyEvent.KEYCODE_DPAD_CENTER));

		// 判断 期待检测的Activity是否启动
		if (checkMonitorHit(am, 1)) {
			Log.i("ContactsSelectInstrumentation", "Activity started!");
		} else {
			Log.i("ContactsSelectInstrumentation", "*** ACTIVITY NOT STARTED!");
		}

		// And we are done!
		Log.i("ContactsSelectInstrumentation", "Done!");
		finish(Activity.RESULT_OK, null);
	}
}
