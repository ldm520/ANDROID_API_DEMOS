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
import com.example.android.apis.R;

import android.view.View;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

/**
 * 该Demo了在XML中使用
 * android:animateLayoutChanges属性，android:animateLayoutChanges=true
 * /false,所有派ViewGroup的子控件都具有此属性，能实现添加/删除其中控件时，带有默认动画了。
 * 
 * @description：
 * @author ldm
 * @date 2016-5-3 下午2:37:29
 */
public class DefaultLayoutAnimationActivity extends Activity {
	// 设置新增控件的文字 提示
	private int numButtons = 1;
	// 控件装载容器
	private GridLayout gridContainer;
	// 添加操作
	private Button addButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_animations_by_default);
		initViews();
		initEvents();
	}

	private void initEvents() {
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Button delBtn = new Button(
						DefaultLayoutAnimationActivity.this);
				delBtn.setText("第" + numButtons++ + "个");
				//点击后则删除对应的Button
				delBtn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						gridContainer.removeView(v);
					}
				});
				// 把新增的控件随机放在gridContainer中
				gridContainer.addView(delBtn,
						(int) (Math.random() * gridContainer.getChildCount()));
			}
		});
	}

	private void initViews() {
		gridContainer = (GridLayout) findViewById(R.id.grid_layout);
		addButton = (Button) findViewById(R.id.add_btn);
	}

}