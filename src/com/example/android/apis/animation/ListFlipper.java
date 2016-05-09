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

package com.example.android.apis.animation;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * This application demonstrates the seeking capability of ValueAnimator. The SeekBar in the
 * UI allows you to set the position of the animation. Pressing the Run button will play from
 * the current position of the animation.
 */
/**
 * API动画效果之：列表界面翻转动画效果
 * 
 * @description：
 * @author ldm
 * @date 2016-5-9 上午10:00:25
 */
public class ListFlipper extends Activity {
	// 列表正面数据
	private static final String[] LIST_STRINGS_EN = new String[] { "One",
			"Two", "Three", "Four", "Five", "Six" };
	// 页面翻转后页面数据
	private static final String[] LIST_STRINGS_FR = new String[] { "Un",
			"Deux", "Trois", "Quatre", "Le Five", "Six" };

	ListView mEnglishList;
	ListView mFrenchList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rotating_list);
		// FrameLayout container = (LinearLayout) findViewById(R.id.container);
		// 初始化ListView
		mEnglishList = (ListView) findViewById(R.id.list_en);
		mFrenchList = (ListView) findViewById(R.id.list_fr);

		// 为ListView设置适配器
		final ArrayAdapter<String> adapterEn = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, LIST_STRINGS_EN);
		// Prepare the ListView
		final ArrayAdapter<String> adapterFr = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, LIST_STRINGS_FR);

		mEnglishList.setAdapter(adapterEn);
		mFrenchList.setAdapter(adapterFr);
		// 设置旋转
		mFrenchList.setRotationY(-90f);

		Button starter = (Button) findViewById(R.id.button);
		starter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 点击FLIP的Button后页面翻转
				flipit();
			}
		});
	}

	// 定义了加速及减速的插值器
	private Interpolator accelerator = new AccelerateInterpolator();
	private Interpolator decelerator = new DecelerateInterpolator();

	private void flipit() {
		// 定义两个ListView，作用是充当容器
		final ListView visibleList;
		final ListView invisibleList;
		// 如果英语mEnglishList为不可见的
		if (mEnglishList.getVisibility() == View.GONE) {
			// mFrenchList可见
			visibleList = mFrenchList;
			invisibleList = mEnglishList;
		} else {// 反之
			invisibleList = mFrenchList;
			visibleList = mEnglishList;
		}
		// 动画效果:绕Y轴0°到90°旋转
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleList,
				"rotationY", 0f, 90f);
		visToInvis.setDuration(500);
		// 设置加速差值器
		visToInvis.setInterpolator(accelerator);
		// 动画效果:绕Y轴逆时针旋转90°
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibleList,
				"rotationY", -90f, 0f);
		invisToVis.setDuration(500);
		invisToVis.setInterpolator(decelerator);
		// 动画过程监听
		visToInvis.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				visibleList.setVisibility(View.GONE);
				invisToVis.start();
				invisibleList.setVisibility(View.VISIBLE);
			}
		});
		// 启动动画
		visToInvis.start();
	}

}