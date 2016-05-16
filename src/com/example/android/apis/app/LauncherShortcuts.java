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

package com.example.android.apis.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * APP创建桌面快捷键
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 上午10:06:18
 */
public class LauncherShortcuts extends Activity {

	private static final String EXTRA_KEY = "com.example.android.apis.app.LauncherShortcuts";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// 获取Intent
		final Intent intent = getIntent();
		// 得到Intent对应的Action
		final String action = intent.getAction();
		// 如果Action匹配，则创建快捷方式
		if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
			setupShortcut();
			finish();
			return;
		}

		setContentView(R.layout.launcher_shortcuts);
		TextView intentInfo = (TextView) findViewById(R.id.txt_shortcut_intent);
		String info = intent.toString();
		String extra = intent.getStringExtra(EXTRA_KEY);
		if (extra != null) {
			info = info + " " + extra;
		}
		intentInfo.setText(info);
	}

	/**
	 * 创建桌面快捷方式
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-16 上午10:09:39
	 */
	private void setupShortcut() {
		// 设置关联程序
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(this, this.getClass().getName());
		shortcutIntent.putExtra(EXTRA_KEY, "ApiDemos Provided This Shortcut");

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 快捷方式的名称
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.shortcut_name));
		// 快捷方式的图标
		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.app_sample_code);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

		setResult(RESULT_OK, intent);
	}
}
