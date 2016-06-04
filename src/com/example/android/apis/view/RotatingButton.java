/*
 * Copyright (C) 2010 The Android Open Source Project
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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * 控件动画
 * 
 * @description：
 * @author ldm
 * @date 2016-6-3 上午11:08:33
 */
public class RotatingButton extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rotating_view);
		final Button rotatingButton = (Button) findViewById(R.id.rotatingButton);
		SeekBar seekBar;
		seekBar = (SeekBar) findViewById(R.id.translationX);
		seekBar.setMax(400);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// X方向平移
				rotatingButton.setTranslationX((float) progress);
			}
		});
		seekBar = (SeekBar) findViewById(R.id.translationY);
		seekBar.setMax(800);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Y方向平移
				rotatingButton.setTranslationY((float) progress);
			}
		});
		seekBar = (SeekBar) findViewById(R.id.scaleX);
		seekBar.setMax(50);
		seekBar.setProgress(10);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// X方向缩放
				rotatingButton.setScaleX((float) progress / 10f);
			}
		});
		seekBar = (SeekBar) findViewById(R.id.scaleY);
		seekBar.setMax(50);
		seekBar.setProgress(10);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Y方向缩放
				rotatingButton.setScaleY((float) progress / 10f);
			}
		});
		seekBar = (SeekBar) findViewById(R.id.rotationX);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// X方向旋转
				rotatingButton.setRotationX((float) progress);
			}
		});
		seekBar = (SeekBar) findViewById(R.id.rotationY);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Y方向旋转
				rotatingButton.setRotationY((float) progress);
			}
		});
		seekBar = (SeekBar) findViewById(R.id.rotationZ);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// 设置旋转
				rotatingButton.setRotation((float) progress);
			}
		});
	}
}