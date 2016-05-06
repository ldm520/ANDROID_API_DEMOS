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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

/**
 * 传感器指针Demo
 * 
 * @description：
 * @author ldm
 * @date 2016-4-25 下午5:29:18
 */
public class SensorHandActivity extends GraphicsActivity {
	// 传感器管理对象
	private SensorManager mSensorManager;
	// 传感器类
	private Sensor mSensor;
	// 自定义绘制指针View
	private MyCompassView mView;
	/**
	 * 方向传感器检测到的感应值 values[0]: Azimuth(方位)，地磁北方向与y轴的角度，围绕z轴旋转(0到359)。0=North,
	 * 90=East, 180=South, 270=West values[1]: Pitch(俯仰),围绕X轴旋转(-180 to 180),
	 * 当Z轴向Y轴运动时是正值 values[2]: Roll(滚)，围绕Y轴旋转(-90 to 90)，当X轴向Z轴运动时是正值
	 */
	private float[] mValues;
	// 传感监听
	private final SensorEventListener mSensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			mValues = event.values;
			if (mView != null) {
				mView.invalidate();
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mView = new MyCompassView(this);
		setContentView(mView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 在onResume方法中注册传感器监听 事件
		 * 第一个参数：监听Sensor事件，第二个参数是Sensor目标种类的值，第三个参数是延迟时间的精度密度。延迟时间的精密度参数 参数
		 * 延迟时间 SensorManager.SENSOR_DELAY_FASTEST 0ms
		 * SensorManager.SENSOR_DELAY_GAME 20ms SensorManager.SENSOR_DELAY_UI
		 * 60ms SensorManager.SENSOR_DELAY_NORMAL 200ms
		 */
		mSensorManager.registerListener(mSensorListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		// 在onStop方法中取消注册监听
		mSensorManager.unregisterListener(mSensorListener);
		super.onStop();
	}

	private class MyCompassView extends View {
		// 定义画笔Paint
		private Paint mPaint;
		// 定义绘制指针的路径Path
		private Path mPath;

		public MyCompassView(Context context) {
			super(context);
			initPaintAndPath();
		}

		private void initPaintAndPath() {
			// 初始化画笔
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Paint.Style.FILL);
			// 初始化绘制路径
			mPath = new Path();
			mPath.moveTo(0, -50);// 移动到指点点
			mPath.lineTo(-20, 60);// 用线条连接到指定点
			mPath.lineTo(0, 50);
			mPath.lineTo(20, 60);
			mPath.close();// 关闭路径
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// 设置画面背景
			canvas.drawColor(Color.WHITE);
			int w = canvas.getWidth();
			int h = canvas.getHeight();
			int cx = w / 2;
			int cy = h / 2;
			canvas.translate(cx, cy);// 移动画面，把指针放到中央
			if (mValues != null) {
				canvas.rotate(-mValues[0]);
			}
			canvas.drawPath(mPath, mPaint);
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
		}

		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
		}
	}
}
