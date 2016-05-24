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

package com.example.android.apis.view;

import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;

/**
 * Chronometer计时器功能
 * 
 * @description：
 * @author ldm
 * @date 2016-5-17 上午11:41:56
 */
public class ChronometerActivity extends Activity implements OnClickListener {
	private Chronometer mChronometer;
	private Button start, stop, reset, format, clear_format;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chronometer);
		// 初始化View widget
		initViews();
		// 设置监听事件
		initListeners();

	}

	private void initListeners() {
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		reset.setOnClickListener(this);
		format.setOnClickListener(this);
		clear_format.setOnClickListener(this);
	}

	private void initViews() {
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		reset = (Button) findViewById(R.id.reset);
		format = (Button) findViewById(R.id.set_format);
		clear_format = (Button) findViewById(R.id.clear_format);
	}

	View.OnClickListener mStartListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.start();// 开始计时
		}
	};

	View.OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.stop();// 停止计时
		}
	};

	View.OnClickListener mResetListener = new OnClickListener() {
		public void onClick(View v) {
			// 设置计时时间基准
			mChronometer.setBase(SystemClock.elapsedRealtime());
		}
	};

	View.OnClickListener mSetFormatListener = new OnClickListener() {
		public void onClick(View v) {
			// 设置时间格式
			mChronometer.setFormat("Formatted time (%s)");
		}
	};

	View.OnClickListener mClearFormatListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.setFormat(null);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			mChronometer.start();// 开始计时
			break;
		case R.id.stop:
			mChronometer.stop();// 暂停计时
			break;
		case R.id.reset:
			mChronometer.setBase(SystemClock.elapsedRealtime());// 从开机到现在的毫秒数
			break;
		case R.id.set_format:
			// 需要一个String变量，并使用"%s"表示计时信息
			mChronometer.setFormat("时间累计:%s秒");
			break;
		case R.id.clear_format:
			mChronometer.setFormat(null);
			break;
		}
	}
}
