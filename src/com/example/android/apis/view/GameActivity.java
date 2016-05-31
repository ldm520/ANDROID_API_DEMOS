/*
 * Copyright (C) 2011 The Android Open Source Project
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
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.example.android.apis.R;
import com.example.android.apis.graphics.TouchPaint;

/**
 * 利用系统 UI flag特性模拟身临其境的game
 * 
 * @description：
 * @author ldm
 * @date 2016-5-26 下午4:13:04
 */
public class GameActivity extends Activity {

	/**
	 * 自定义游戏视图，填充整个屏幕。
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-26 下午4:16:17
	 */
	public static class Content extends TouchPaint.PaintView implements
			View.OnSystemUiVisibilityChangeListener, View.OnClickListener {
		Activity mActivity;
		Button mPlayButton;
		boolean mPaused;
		int mLastSystemUiVis;
		boolean mUpdateSystemUi;

		Runnable mFader = new Runnable() {
			@Override
			public void run() {
				fade();
				if (mUpdateSystemUi) {
					updateNavVisibility();
				}
				if (!mPaused) {
					getHandler().postDelayed(mFader, 1000 / 30);
				}
			}
		};

		public Content(Context context, AttributeSet attrs) {
			super(context, attrs);
			setOnSystemUiVisibilityChangeListener(this);
		}

		public void init(Activity activity, Button playButton) {
			mActivity = activity;
			mPlayButton = playButton;
			mPlayButton.setOnClickListener(this);
			setGamePaused(true);
		}

		@Override
		public void onSystemUiVisibilityChange(int visibility) {
			int diff = mLastSystemUiVis ^ visibility;
			mLastSystemUiVis = visibility;
			if (!mPaused && (diff & SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
					&& (visibility & SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
				mUpdateSystemUi = true;
			}
		}

		@Override
		protected void onWindowVisibilityChanged(int visibility) {
			super.onWindowVisibilityChanged(visibility);
			// 游戏暂停
			setGamePaused(true);
		}

		// 窗口焦点发生变化时
		@Override
		public void onWindowFocusChanged(boolean hasWindowFocus) {
			super.onWindowFocusChanged(hasWindowFocus);

			if (!hasWindowFocus) {
				setGamePaused(true);
			}
		}

		@Override
		public void onClick(View v) {
			if (v == mPlayButton) {
				// 开始与暂停按钮
				setGamePaused(!mPaused);
			}
		}

		void setGamePaused(boolean paused) {
			mPaused = paused;
			mPlayButton.setText(paused ? R.string.play : R.string.pause);// 设置按钮开关
			setKeepScreenOn(!paused);
			updateNavVisibility();
			Handler h = getHandler();
			if (h != null) {
				getHandler().removeCallbacks(mFader);
				if (!paused) {
					mFader.run();
					text("Draw!");// 在屏幕上绘制文字
				}
			}
		}

		// 更新系统UI的FLAG特性
		void updateNavVisibility() {
			int newVis = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| SYSTEM_UI_FLAG_LAYOUT_STABLE;
			if (!mPaused) {
				newVis |= SYSTEM_UI_FLAG_LOW_PROFILE
						| SYSTEM_UI_FLAG_FULLSCREEN
						| SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			}

			// 设备UI
			setSystemUiVisibility(newVis);
			mUpdateSystemUi = false;
		}
	}

	Content mContent;

	public GameActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game);
		mContent = (Content) findViewById(R.id.content);
		mContent.init(this, (Button) findViewById(R.id.play));
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Pause game when its activity is paused.
		mContent.setGamePaused(true);
	}
}
