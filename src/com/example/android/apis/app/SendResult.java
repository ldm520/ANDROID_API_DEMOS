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
 * Example of receiving a result from another activity.
 */
public class SendResult extends Activity {
	/**
	 * Initialization of the Activity after it is first created. Must at least
	 * call {@link android.app.Activity#setContentView setContentView()} to
	 * describe what is to be displayed in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.send_result);

		Button button = (Button) findViewById(R.id.corky);
		button.setOnClickListener(mCorkyListener);
		button = (Button) findViewById(R.id.violet);
		button.setOnClickListener(mVioletListener);
	}

	private OnClickListener mCorkyListener = new OnClickListener() {
		public void onClick(View v) {
			// 回传数据时采用setResult方法，并且之后要调用finish方法。
			setResult(RESULT_OK, (new Intent()).setAction("Corky!"));
			finish();
		}
	};

	private OnClickListener mVioletListener = new OnClickListener() {
		public void onClick(View v) {
			// 回传数据时采用setResult方法，并且之后要调用finish方法。
			setResult(RESULT_OK, (new Intent()).setAction("Violet!"));
			finish();
		}
	};
}
