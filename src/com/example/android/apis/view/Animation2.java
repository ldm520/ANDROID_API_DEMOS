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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ViewFlipper;

/**
 * FlipperView文字效果动画之：文字滚动动画
 * 
 * @description：
 * @author ldm
 * @date 2016-5-17 上午9:58:26
 */
public class Animation2 extends Activity implements
		AdapterView.OnItemSelectedListener {
	// Spinner数据源
	private String[] mStrings = { "Push up", "Push left", "Cross fade",
			"Hyperspace" };
	// 控件ViewFlipper
	private ViewFlipper mFlipper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_2);
		// 初始化UI控件
		initViews();
	}

	private void initViews() {
		mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
		mFlipper.startFlipping();
		Spinner s = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mStrings);
		// 定义Spinner下拉菜单模式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置数据
		s.setAdapter(adapter);
		// 添加监听
		s.setOnItemSelectedListener(this);
	}

	/**
	 * Spinner的item选择监听事件处理
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		switch (position) {

		case 0:// 文字从下进入，从上移出，伴随透明度变化
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_in));
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_out));
			break;
		case 1:// 文字从右侧向左进入，从右侧移出，伴随透明度变化
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			break;
		case 2:// 文字透明度改变，从0-1-0
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in));
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out));
			break;
		default:// 多维空间动画（复合动画效果）
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.hyperspace_in));
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.hyperspace_out));
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		// DO NOTHING
	}

}
