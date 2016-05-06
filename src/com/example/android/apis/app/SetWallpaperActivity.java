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
import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.apis.R;

/**
 * Andorid设置手机屏幕的壁纸
 * 
 * @description：
 * @author ldm
 * @date 2016-5-4 下午3:08:56
 */
public class SetWallpaperActivity extends Activity {
	// WallpaperManager类：系统壁纸管理。通过它可以获得当前壁纸以及设置指定图片作为系统壁纸。
	private WallpaperManager wallpaperManager;
	// 壁纸对应的Drawable
	private Drawable wallpaperDrawable;
	// 展示样式的ImageView
	private ImageView imageView;
	// 随机生成图片的颜色 Button
	private Button randomize;
	// 设置壁纸
	private Button setWallpaper;
	// 暂定的一些颜色值
	final static private int[] mColors = { Color.BLUE, Color.GREEN, Color.RED,
			Color.LTGRAY, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.WHITE };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_2);
		// 初始化WallpaperManager
		wallpaperManager = WallpaperManager.getInstance(this);
		wallpaperDrawable = wallpaperManager.getDrawable();// 获得当前系统的壁纸
		initViews();
		initListeners();
	}

	private void initListeners() {
		randomize.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				int mColor = (int) Math.floor(Math.random() * mColors.length);
				// 给当前系统壁纸设置颜色
				wallpaperDrawable.setColorFilter(mColors[mColor],
						PorterDuff.Mode.MULTIPLY);// 取两层绘制交集
				imageView.setImageDrawable(wallpaperDrawable);
				// imageView.invalidate();
			}
		});

		setWallpaper.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				try {
					// 设置壁纸
					wallpaperManager.setBitmap(imageView.getDrawingCache());
					finish();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initViews() {
		imageView = (ImageView) findViewById(R.id.imageview);
		imageView.setDrawingCacheEnabled(true);
		imageView.setImageDrawable(wallpaperDrawable);
		randomize = (Button) findViewById(R.id.randomize);
		setWallpaper = (Button) findViewById(R.id.setwallpaper);
	}
}
