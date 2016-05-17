/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.example.android.apis.appwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

/**
 * 桌面小部件 AppWidget配置
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 下午1:57:16
 */
public class ExampleAppWidgetConfigure extends Activity {
	static final String TAG = "ExampleAppWidgetConfigure";
	// 保存的文件名
	private static final String PREFS_NAME = "com.example.android.apis.appwidget.ExampleAppWidgetProvider";
	// 保存的字段KEY
	private static final String PREF_PREFIX_KEY = "prefix_";
	// 小部件 对应ID
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	// 输入框
	EditText mAppWidgetPrefix;

	public ExampleAppWidgetConfigure() {
		super();
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setResult(RESULT_CANCELED);
		// 设置布局
		setContentView(R.layout.appwidget_configure);
		mAppWidgetPrefix = (EditText) findViewById(R.id.appwidget_prefix);
		// 设置监听
		findViewById(R.id.save_button).setOnClickListener(mOnClickListener);
		// 获取intent传递过来的数据
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		mAppWidgetPrefix.setText(loadTitlePref(ExampleAppWidgetConfigure.this,
				mAppWidgetId));
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			final Context context = ExampleAppWidgetConfigure.this;
			String titlePrefix = mAppWidgetPrefix.getText().toString();
			//保存到SharedPreferences文件
			saveTitlePref(context, mAppWidgetId, titlePrefix);
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			//更新小部件 
			ExampleAppWidgetProvider.updateAppWidget(context, appWidgetManager,
					mAppWidgetId, titlePrefix);

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	};

	static void saveTitlePref(Context context, int appWidgetId, String text) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
		prefs.commit();
	}

	static String loadTitlePref(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
		if (prefix != null) {
			return prefix;
		} else {
			return context.getString(R.string.appwidget_prefix_default);
		}
	}

	static void deleteTitlePref(Context context, int appWidgetId) {
	}

	static void loadAllTitlePrefs(Context context,
			ArrayList<Integer> appWidgetIds, ArrayList<String> texts) {
	}
}
