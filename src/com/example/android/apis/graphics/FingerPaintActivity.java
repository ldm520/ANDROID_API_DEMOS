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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * Android写字板Demo（包括颜色选择，图形外观处理及Xfermode示例）
 * @description：
 * @author ldm
 * @date 2016-4-26 上午10:39:50
 */
public class FingerPaintActivity extends Activity implements
		ColorPickerDialog.OnColorChangedListener {
	private Paint mPaint;
	/**
	 * 面罩 使用它后会出现一个面具在目标的边缘的指定范围，该面具的边缘是否会被包进目标中，
	 * 或者是在目标里边，外边，里边都有，这是由BlurMaskFilter.Blur这个枚举所决定的。
	 */
	private MaskFilter mEmboss;
	private MaskFilter mBlur;
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	// 菜单选择项
	private static final int COLOR_MENU_ID = Menu.FIRST;
	private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
	private static final int BLUR_MENU_ID = Menu.FIRST + 2;
	private static final int ERASE_MENU_ID = Menu.FIRST + 3;
	private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
		init();
	}

	/**
	 * 初始化各种数据
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-26 上午10:21:41
	 */
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);// 抗锯齿
		mPaint.setDither(true); // 防抖动
		mPaint.setColor(0xFFFF0000);// 设置颜色
		mPaint.setStyle(Paint.Style.STROKE);// 画笔类型 STROKE空心 FILL 实心
		mPaint.setStrokeJoin(Paint.Join.ROUND);// 画笔接洽点类型 如影响矩形但角的外轮廓,让画的线圆滑
		mPaint.setStrokeCap(Paint.Cap.ROUND);// 画笔笔刷类型 如影响画笔但始末端
		mPaint.setStrokeWidth(12);// 设置线宽
		// 光源的方向和环境光强度来添加浮雕效果
		mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
		// 前面一个控制阴影的宽度，后面一个参数控制阴影效果
		mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
	}

	/**
	 * 颜色选择器改变颜色监听
	 */
	public void colorChanged(int color) {
		mPaint.setColor(color);
	}

	public class MyView extends View {
		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;

		public MyView(Context context) {
			super(context);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);// 是使位图进行有利的抖动的位掩码标志
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFFAAAAAA);
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			canvas.drawPath(mPath, mPaint);
		}

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				/**
				 * quadTo方法的实现是当我们不仅仅是画一条线甚至是画弧线时会形成平滑的曲线，
				 * 该曲线又称为"贝塞尔曲线"(Beziercurve) 其中，x1，y1为控制点的坐标值，x2，y2为终点的坐标值；
				 */
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			mPath.lineTo(mX, mY);
			// 连接绘制
			mCanvas.drawPath(mPath, mPaint);
			// 清除path设置的所有属性
			mPath.reset();
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				break;
			}
			invalidate();
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');// setShortcut方法是添加对应菜单快捷键
		menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
		menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
		menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
		menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 清除两张图片相交时的模式
		mPaint.setXfermode(null);
		// 设置透明度
		mPaint.setAlpha(0xFF);
		switch (item.getItemId()) {
		case COLOR_MENU_ID://ColorPickerDialog：颜色选择对话框, 弹出颜色 选择Dialog
			new ColorPickerDialog(this, this, mPaint.getColor()).show();
			return true;
		case EMBOSS_MENU_ID:
			if (mPaint.getMaskFilter() != mEmboss) {
				mPaint.setMaskFilter(mEmboss);
			} else {
				mPaint.setMaskFilter(null);
			}
			return true;
		case BLUR_MENU_ID:
			if (mPaint.getMaskFilter() != mBlur) {
				mPaint.setMaskFilter(mBlur);
			} else {
				mPaint.setMaskFilter(null);
			}
			return true;
		case ERASE_MENU_ID://PorterDuff.Mode.CLEAR 所绘制不会提交到画布上
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			return true;
		case SRCATOP_MENU_ID://取下层非交集部分与上层交集部分
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
			mPaint.setAlpha(0x80);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * 备注，Paint方法setXfermode (Xfermode xfermode)设置两张图片交到模式，有以下十六种：
	 * 1.PorterDuff.Mode.CLEAR 所绘制不会提交到画布上。
	 * 2.PorterDuff.Mode.SRC 显示上层绘制图片
	 * 3.PorterDuff.Mode.DST 显示下层绘制图片
	 * 4.PorterDuff.Mode.SRC_OVER 正常绘制显示，上下层绘制叠盖。 
	 * 5.PorterDuff.Mode.DST_OVER 上下层都显示。下层居上显示。 
	 * 6.PorterDuff.Mode.SRC_IN 取两层绘制交集。显示上层。
	 * 7.PorterDuff.Mode.DST_IN 取两层绘制交集。显示下层。
	 * 8.PorterDuff.Mode.SRC_OUT*取上层绘制非交集部分。 
	 * 9.PorterDuff.Mode.DST_OUT 取下层绘制非交集部分。
	 * 10.PorterDuff.Mode.SRC_ATOP 取下层非交集部分与上层交集部分 
	 * 11.PorterDuff.Mode.DST_ATOP 取上层非交集部分与下层交集部分 
	 * 12.PorterDuff.Mode.XOR 异或：去除两图层交集部分
	 * 13.PorterDuff.Mode.DARKEN 取两图层全部区域，交集部分颜色加深 
	 * 14.PorterDuff.Mode.LIGHTEN取两图层全部，点亮交集部分颜色 
	 * 15.PorterDuff.Mode.MULTIPLY 取两图层交集部分叠加后颜色
	 * 16.PorterDuff.Mode.SCREEN 取两图层全部区域，交集部分变为透明色
	 */
}
