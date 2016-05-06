/*
 * Copyright (C) 20013The Android Open Source Project
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

package com.example.android.apis.hardware;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.hardware.ConsumerIrManager;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import com.example.android.apis.R;

/**
 * Android红外线遥控官方Demo
 * 
 * @description：
 * @author ldm
 * @date 2016-4-28 下午5:06:28
 */
public class ConsumerIrActivity extends Activity {
	private static final String TAG = "ConsumerIrTest";
	private TextView mFreqsText;
	// Android4.4之后 红外遥控ConsumerIrManager，可以被小米4调用
	private ConsumerIrManager mCIR;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumer_ir);
		// 获取系统的红外遥控服务
		mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
		initViewsAndEvents();
	}

	private void initViewsAndEvents() {
		findViewById(R.id.send_button).setOnClickListener(mSendClickListener);
		findViewById(R.id.get_freqs_button)
				.setOnClickListener(mOnClickListener);
		mFreqsText = (TextView) findViewById(R.id.freqs_text);
	}

	View.OnClickListener mSendClickListener = new View.OnClickListener() {
		@TargetApi(Build.VERSION_CODES.KITKAT)
		public void onClick(View v) {
			if (!mCIR.hasIrEmitter()) {
				Log.e(TAG, "未找到红外发身器！");
				return;
			}

			// 一种交替的载波序列模式，通过毫秒测量
			int[] pattern = { 1901, 4453, 625, 1614, 625, 1588, 625, 1614, 625,
					442, 625, 442, 625, 468, 625, 442, 625, 494, 572, 1614,
					625, 1588, 625, 1614, 625, 494, 572, 442, 651, 442, 625,
					442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442,
					625, 494, 598, 442, 625, 442, 625, 520, 572, 442, 625, 442,
					625, 442, 651, 1588, 625, 1614, 625, 1588, 625, 1614, 625,
					1588, 625, 48958 };

			// 在38.4KHz条件下进行模式转换
			mCIR.transmit(38400, pattern);
		}
	};

	@SuppressLint("NewApi")
	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StringBuilder b = new StringBuilder();

			if (!mCIR.hasIrEmitter()) {
				mFreqsText.setText("未找到红外发身器！");
				return;
			}

			// 获得可用的载波频率范围
			ConsumerIrManager.CarrierFrequencyRange[] freqs = mCIR
					.getCarrierFrequencies();
			b.append("IR Carrier Frequencies:\n");// 红外载波频率
			// 边里获取频率段
			for (ConsumerIrManager.CarrierFrequencyRange range : freqs) {
				b.append(String.format("    %d - %d\n",
						range.getMinFrequency(), range.getMaxFrequency()));
			}
			mFreqsText.setText(b.toString());// 显示结果
		}
	};
}
