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
import com.example.android.apis.R;

import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * This application demonstrates the seeking capability of ValueAnimator. The SeekBar in the
 * UI allows you to set the position of the animation. Pressing the Run button will play from
 * the current position of the animation.
 */
/**
 * API动画效果之：拖动条平移动画
 * 
 * @description：
 * @author ldm
 * @date 2016-5-6 上午10:49:57
 */
public class AnimationSeeking extends Activity {

	private static final int DURATION = 1500;
	// 拖动条
	private SeekBar mSeekBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_seeking);
		// 初始化LinearLayout
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView animView = new MyAnimationView(this);
		// 代码动态为LinearLayout添加View
		container.addView(animView);

		Button starter = (Button) findViewById(R.id.startButton);
		starter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 开始动画
				animView.startAnimation();
			}
		});

		mSeekBar = (SeekBar) findViewById(R.id.seekBar);
		mSeekBar.setMax(DURATION);
		// SeekBar的监听事件
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// prevent seeking on app creation
				if (animView.getHeight() != 0) {
					// 根据位置改变动画效果
					animView.seek(progress);
				}
			}
		});
	}

	/**
	 * 自定义动画效果View
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-6 下午3:27:23
	 */
	public class MyAnimationView extends View implements
			ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

		private static final float BALL_SIZE = 100f;

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
		// AnimatorSet可以把多个动画组合成一个组合的机制，并可设置组中动画的时序关系，如同时播放，顺序播放等。
		AnimatorSet animation = null;
		// ValueAnimator包含Property
		// Animation动画的所有核心功能，如动画时间，开始、结束属性值，相应时间属性值计算方法等
		ValueAnimator bounceAnim = null;
		ShapeHolder ball = null;

		public MyAnimationView(Context context) {
			super(context);
			ball = addBall(200, 0);
		}

		private void createAnimation() {
			if (bounceAnim == null) {
				bounceAnim = ObjectAnimator.ofFloat(ball, "y", ball.getY(),
						getHeight() - BALL_SIZE).setDuration(1500);
				bounceAnim.setInterpolator(new BounceInterpolator());
				bounceAnim.addUpdateListener(this);
			}
		}

		public void startAnimation() {
			createAnimation();
			bounceAnim.start();
		}

		public void seek(long seekTime) {
			createAnimation();
			bounceAnim.setCurrentPlayTime(seekTime);
		}

		private ShapeHolder addBall(float x, float y) {
			// 绘制一个椭圆
			OvalShape circle = new OvalShape();
			// 设置椭圆高宽
			circle.resize(BALL_SIZE, BALL_SIZE);
			// 初始化圆形
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x);
			shapeHolder.setY(y);
			// 产生随机颜色
			int red = (int) (100 + Math.random() * 155);
			int green = (int) (100 + Math.random() * 155);
			int blue = (int) (100 + Math.random() * 155);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			Paint paint = drawable.getPaint();
			int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue
					/ 4;
			// 镜像渐变 参数一为渐变起初点坐标x位置，参数二为y轴位置，
			// 参数三半径范围，参数4、5是代表中心颜色和边缘颜色，最后参数为平铺方式
			// Shader.TileMode.CLAMP:使用Shader的边界颜色来填充剩余的空间
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f,
					color, darkColor, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			shapeHolder.setPaint(paint);
			balls.add(shapeHolder);
			return shapeHolder;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// 绘制球形图案
			canvas.translate(ball.getX(), ball.getY());
			ball.getShape().draw(canvas);
		}

		public void onAnimationUpdate(ValueAnimator animation) {
			invalidate();
			long playtime = bounceAnim.getCurrentPlayTime();
			// 根据动画的当前动画属性值 设置SeekingBar位置
			mSeekBar.setProgress((int) playtime);
		}

		public void onAnimationCancel(Animator animation) {
		}

		public void onAnimationEnd(Animator animation) {
			balls.remove(((ObjectAnimator) animation).getTarget());

		}

		public void onAnimationRepeat(Animator animation) {
		}

		public void onAnimationStart(Animator animation) {
		}
	}
}