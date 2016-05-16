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

import com.example.android.apis.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Android常用Intent API
 * http://blog.csdn.net/wl455624651/article/details/7943252这个博客更详细
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 下午2:23:37
 */
public class Intents extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intents);
	}

	/**
	 * 打开多媒体
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 下午2:24:18
	 */
	public void onGetMusic(View view) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		startActivity(Intent.createChooser(intent, "Select music"));
	}

	/**
	 * 打开图库
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-13 下午2:26:49
	 */
	public void onGetImage(View view) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivity(Intent.createChooser(intent, "Select image"));
	}

	/**
	 * 调用系统程序
	 */
	public void onGetStream(View view) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		startActivity(Intent.createChooser(intent, "Select stream"));
	}
}
