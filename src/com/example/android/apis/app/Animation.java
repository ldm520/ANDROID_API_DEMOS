/*
 * Copyright (C) 2009 The Android Open Source Project
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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.android.apis.R;

/**
 * Activity动画效果
 * 
 * @description：
 * @author ldm
 * @date 2016-5-10 上午10:56:01
 */
public class Animation extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		Button button = (Button) findViewById(R.id.fade_animation);
		button.setOnClickListener(mFadeListener);
		button = (Button) findViewById(R.id.zoom_animation);
		button.setOnClickListener(mZoomListener);
		// 判断手机系统版本是否大于16（Android4.1）
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			button = (Button) findViewById(R.id.modern_fade_animation);
			button.setOnClickListener(mModernFadeListener);
			button = (Button) findViewById(R.id.modern_zoom_animation);
			button.setOnClickListener(mModernZoomListener);
			button = (Button) findViewById(R.id.scale_up_animation);
			button.setOnClickListener(mScaleUpListener);
			button = (Button) findViewById(R.id.zoom_thumbnail_animation);
			button.setOnClickListener(mZoomThumbnailListener);
		} else {// 如果小于4.2版本，则按钮不可用
			findViewById(R.id.modern_fade_animation).setEnabled(false);
			findViewById(R.id.modern_zoom_animation).setEnabled(false);
			findViewById(R.id.scale_up_animation).setEnabled(false);
			findViewById(R.id.zoom_thumbnail_animation).setEnabled(false);
		}
	}

	private OnClickListener mFadeListener = new OnClickListener() {
		public void onClick(View v) {
			// 页面跳转
			startActivity(new Intent(Animation.this, AlertDialogSamples.class));
			// 切换Activity动画效果
			overridePendingTransition(R.anim.fade, R.anim.hold);
		}
	};

	private OnClickListener mZoomListener = new OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(Animation.this, AlertDialogSamples.class));
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
	};

	@SuppressLint("NewApi")
	private OnClickListener mModernFadeListener = new OnClickListener() {
		public void onClick(View v) {
			// Activity过渡动画，Android4.1后出现的
			// 三个参数，第一个是指当前activity，第二个和第三个参数分别是进入动画和退出动画
			ActivityOptions opts = ActivityOptions.makeCustomAnimation(
					Animation.this, R.anim.fade, R.anim.hold);
			// 启动activity的方式是使用startActivity，最后一个参数我们使用opts.toBundle。
			startActivity(new Intent(Animation.this, AlertDialogSamples.class),
					opts.toBundle());
		}
	};

	private OnClickListener mModernZoomListener = new OnClickListener() {
		@SuppressLint("NewApi")
		public void onClick(View v) {
			ActivityOptions opts = ActivityOptions.makeCustomAnimation(
					Animation.this, R.anim.zoom_enter, R.anim.zoom_enter);
			startActivity(new Intent(Animation.this, AlertDialogSamples.class),
					opts.toBundle());
		}
	};

	private OnClickListener mScaleUpListener = new OnClickListener() {
		@SuppressLint("NewApi")
		public void onClick(View v) {
			ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(v, 0,
					0, v.getWidth(), v.getHeight());
			startActivity(new Intent(Animation.this, AlertDialogSamples.class),
					opts.toBundle());
		}
	};

	private OnClickListener mZoomThumbnailListener = new OnClickListener() {
		@SuppressLint("NewApi")
		public void onClick(View v) {
			// 保留缓存
			v.setDrawingCacheEnabled(true);
			v.setPressed(false);
			// 更新当前状态的按钮图标
			v.refreshDrawableState();
			// 获取按钮对应Bitmap
			Bitmap bm = v.getDrawingCache();
			// c.drawARGB(255, 255, 0, 0);
			ActivityOptions opts = ActivityOptions
					.makeThumbnailScaleUpAnimation(v, bm, 0, 0);
			startActivity(new Intent(Animation.this, AlertDialogSamples.class),
					opts.toBundle());
			// 清除缓存
			v.setDrawingCacheEnabled(false);
		}
	};
}
