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

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * Android控件之SeekBar介绍 OnSeekBarChangeListener：拖动进度条发生变化监听接口
 * 
 * @description：
 * @author ldm
 * @date 2016-6-3 上午10:39:22
 */
public class SeekBar1 extends Activity implements
		SeekBar.OnSeekBarChangeListener {

	private SeekBar mSeekBar;// 拖动条
	TextView mProgressText;// 进度提示文字
	TextView mTrackingText;// 拖动状态提示文字

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seekbar);
		initViews();
	}

	private void initViews() {
		mSeekBar = (SeekBar) findViewById(R.id.seek);
		mSeekBar.setOnSeekBarChangeListener(this);
		mProgressText = (TextView) findViewById(R.id.progress);
		mTrackingText = (TextView) findViewById(R.id.tracking);
	}

	// 进度长发生改变
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {
		mProgressText.setText("当前进度-->" + progress + "是否为用户拖动的滑块-->= "
				+ fromTouch);
	}

	// 开始拖动状态监听
	public void onStartTrackingTouch(SeekBar seekBar) {
		mTrackingText.setText("开始拖动");
	}

	// 停止拖动状态监听
	public void onStopTrackingTouch(SeekBar seekBar) {
		mTrackingText.setText("停止拖动");
	}
}
