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

package com.example.android.apis.graphics;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
//import com.example.android.apis.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;
/**
 * 绘制弧形图案
 * @description：
 * @author ldm
 * @date 2016-4-25 下午4:37:01
 */
public class ArcsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new SampleView(this));
	}

	/**
	 * 自定义绘制图案的View
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-25 下午4:19:40
	 */
	private static class SampleView extends View {
		// 画圆形图案的画笔数组
		private Paint[] mPaints;
		// 画矩形的画笔
		private Paint mFramePaint;
		// 是否以矩形中心画图
		private boolean[] mUseCenters;
		// 矩形框数组
		private RectF[] mOvals;
		// 上面较大的矩形框
		private RectF mBigOval;
		// 画圆弧起点
		private float mStart;
		// 画圆弧角度
		private float mSweep;
		// 画大图形对应角标index
		private int mBigIndex;
		// 角度变化增量
		private static final float ANGLE_STEP = 3;
		// 初始角度
		private static final float START_ANGLE = 15;

		public SampleView(Context context) {
			super(context);
			mPaints = new Paint[4];
			mUseCenters = new boolean[4];
			mOvals = new RectF[4];

			mPaints[0] = new Paint();
			mPaints[0].setAntiAlias(true);// 设置抗紧钜齿
			mPaints[0].setStyle(Paint.Style.FILL);// 设置画笔填充风格
			mPaints[0].setColor(0x88FF0000);// 设置颜色
			mUseCenters[0] = false;

			mPaints[1] = new Paint(mPaints[0]);
			mPaints[1].setColor(0x8800FF00);
			mUseCenters[1] = true;

			mPaints[2] = new Paint(mPaints[0]);
			mPaints[2].setStyle(Paint.Style.STROKE);// 设置画笔空心风格
			mPaints[2].setStrokeWidth(4);// 设置画笔宽度
			mPaints[2].setColor(0x880000FF);// 设置颜色
			mUseCenters[2] = false;

			mPaints[3] = new Paint(mPaints[2]);
			mPaints[3].setColor(0x88888888);
			mUseCenters[3] = true;

			mBigOval = new RectF(40, 10, 280, 250);// 初始化较大的矩形

			mOvals[0] = new RectF(10, 270, 70, 330);// 初始化4个较小矩形
			mOvals[1] = new RectF(90, 270, 150, 330);
			mOvals[2] = new RectF(170, 270, 230, 330);
			mOvals[3] = new RectF(250, 270, 310, 330);

			mFramePaint = new Paint();
			mFramePaint.setAntiAlias(true);
			mFramePaint.setStyle(Paint.Style.STROKE);
			mFramePaint.setStrokeWidth(0);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(Color.WHITE);
			// 画大矩形
			canvas.drawRect(mBigOval, mFramePaint);
			// 在大矩形分别 画4个圆弧图形
			canvas.drawArc(mBigOval, mStart, mSweep, mUseCenters[mBigIndex],
					mPaints[mBigIndex]);

			for (int i = 0; i < 4; i++) {
				canvas.drawRect(mOvals[i], mFramePaint);// 画四个小矩形
				canvas.drawArc(mOvals[i], mStart, mSweep, mUseCenters[i],// 在四个小矩形中分别画圆弧
						mPaints[i]);
			}

			mSweep += ANGLE_STEP;// 弧度改变
			if (mSweep > 360) {// 弧度控制
				mSweep -= 360;
				mStart += START_ANGLE;
				if (mStart >= 360) {
					mStart -= 360;
				}
				mBigIndex = (mBigIndex + 1) % mOvals.length;
			}
			invalidate();
		}
	}
}
