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
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.android.apis.R;

/**
 * This application demonstrates how to use LayoutTransition to automate transition animations
 * as items are hidden or shown in a container.
 */
/**
 * API动画效果之：ViewGroup中的控件设置动画效果显示与隐藏控制
 * 
 * @description：
 * @author ldm
 * @date 2016-5-9 上午9:46:18
 */
public class LayoutAnimationsHideShow extends Activity {

	ViewGroup container = null;
	private LayoutTransition mTransitioner;
	private static final String TAG = LayoutAnimationsHideShow.class
			.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_animations_hideshow);

		final CheckBox hideGoneCB = (CheckBox) findViewById(R.id.hideGoneCB);

		container = new LinearLayout(this);
		container.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		// Add a slew of buttons to the container. We won't add any more buttons
		// at runtime, but
		// will just show/hide the buttons we've already created
		/**
		 * 新增4个Button
		 */
		for (int i = 0; i < 4; ++i) {
			Button newButton = new Button(this);
			newButton.setText(String.valueOf(i));
			container.addView(newButton);
			// 为Button增加OnLayoutChangeListener
			newButton
					.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

						@Override
						public void onLayoutChange(View v, int left, int top,
								int right, int bottom, int oldLeft, int oldTop,
								int oldRight, int oldBottom) {
							Log.v(TAG, "[" + left + "," + top + "," + right
									+ "," + bottom + "]");
						}
					});
			// 点击Button后根据Button的状态对应地进行VISIBLE或GONE
			newButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					v.setVisibility(hideGoneCB.isChecked() ? View.GONE
							: View.INVISIBLE);
				}
			});
		}

		resetTransition();

		ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
		parent.addView(container);

		Button addButton = (Button) findViewById(R.id.add_btn);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				for (int i = 0; i < container.getChildCount(); ++i) {
					View view = (View) container.getChildAt(i);
					view.setVisibility(View.VISIBLE);
				}
			}
		});

		CheckBox customAnimCB = (CheckBox) findViewById(R.id.customAnimCB);
		customAnimCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						long duration;
						if (isChecked) {
							mTransitioner.setStagger(
									LayoutTransition.CHANGE_APPEARING, 30);
							mTransitioner.setStagger(
									LayoutTransition.CHANGE_DISAPPEARING, 30);
							setupCustomAnimations();
							duration = 500;
						} else {
							resetTransition();
							duration = 300;
						}
						mTransitioner.setDuration(duration);
					}
				});
	}

	private void resetTransition() {
		mTransitioner = new LayoutTransition();
		container.setLayoutTransition(mTransitioner);
	}

	private void setupCustomAnimations() {
		// Changing while Adding
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 1, 0);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 1, 0);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 1,
				0);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				1, 0);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
				1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
				1f, 0f, 1f);
		final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
				new Object(), pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX,
				pvhScaleY).setDuration(
				mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		changeIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// Changing while Removing
		/**
		 * 一个Keyframe对象有一个键值对组成，通过这个键值对，你可以为动画的一个指定状态定义一个指定的值。
		 * 每个Keyframe也可以定义一个差值器来控制动画的行为
		 * ，该插值器决定了从前一个Keyframe到当前定义的Keyframe之间的值是如何计算的
		 * 要实现一个Keyframe对象，你必须使用一个工厂方法，例如ofInt(), ofFloat(), or ofObject()
		 * 来获取合适的Keyframe，然后你可以调用ofKeyframe()工厂方法来获取一个 PropertyValuesHolder
		 * 对象。一旦你有了这个对象，你可以通过传递该参数已经你要作用的目标来获取一个动画。
		 */
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
				"rotation", kf0, kf1, kf2);
		final ObjectAnimator changeOut = ObjectAnimator
				.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight,
						pvhBottom, pvhRotation)
				.setDuration(
						mTransitioner
								.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
				changeOut);
		changeOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotation(0f);
			}
		});

		//按钮添加时的动画效果
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f,
				0f).setDuration(
				mTransitioner.getDuration(LayoutTransition.APPEARING));
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		//按钮移除时的动画效果
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f,
				90f).setDuration(
				mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}
}