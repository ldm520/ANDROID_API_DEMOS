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
import java.util.ArrayList;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.android.apis.R;

/**
 * API动画效果之：克隆动画
 * 
 * @description：
 * @author ldm
 * @date 2016-5-6 上午10:27:36
 */
public class AnimationCloning extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_cloning);
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView animView = new MyAnimationView(this);
		// 动态给LinearLayout添加一个View
		container.addView(animView);

		Button starter = (Button) findViewById(R.id.startButton);
		starter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				animView.startAnimation();
			}
		});
	}

	/**
	 * 自定义动画 View
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-6 上午10:31:03
	 */
	public class MyAnimationView extends View implements
			ValueAnimator.AnimatorUpdateListener {

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
		private AnimatorSet animation = null;
		private float mDensity;

		public MyAnimationView(Context context) {
			super(context);
			// 获取手机屏幕密度
			mDensity = getContext().getResources().getDisplayMetrics().density;
			// 添加4个球形View
			ShapeHolder ball0 = addBall(50f, 25f);
			ShapeHolder ball1 = addBall(150f, 25f);
			ShapeHolder ball2 = addBall(250f, 25f);
			ShapeHolder ball3 = addBall(350f, 25f);
		}

		private void createAnimation() {
			if (animation == null) {
				// 初始化属性动画
				ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0),
						"y", 0f, getHeight() - balls.get(0).getHeight())
						.setDuration(500);
				// 第二个球球的动画效果：clone动画效果1，但是重新设置目标物体
				ObjectAnimator anim2 = anim1.clone();
				anim2.setTarget(balls.get(1));
				// 添加刷新监听器
				anim1.addUpdateListener(this);

				// 第三个球体动画
				ShapeHolder ball2 = balls.get(2);
				ObjectAnimator animDown = ObjectAnimator.ofFloat(ball2, "y",
						0f, getHeight() - ball2.getHeight()).setDuration(500);
				// AccelerateInterpolator:在动画开始的地方速率改变比较慢，然后开始加速
				animDown.setInterpolator(new AccelerateInterpolator());
				ObjectAnimator animUp = ObjectAnimator.ofFloat(ball2, "y",
						getHeight() - ball2.getHeight(), 0f).setDuration(500);
				// DecelerateInterpolator在动画开始的地方速率改变比较慢，然后开始减速
				animUp.setInterpolator(new DecelerateInterpolator());
				AnimatorSet s1 = new AnimatorSet();
				// 连续执行两种动画
				s1.playSequentially(animDown, animUp);
				animDown.addUpdateListener(this);
				animUp.addUpdateListener(this);

				AnimatorSet s2 = (AnimatorSet) s1.clone();
				s2.setTarget(balls.get(3));

				animation = new AnimatorSet();
				// 同时播放前面3个球的动画
				animation.playTogether(anim1, anim2, s1);
				// 顺序播放后面两个球动画
				animation.playSequentially(s1, s2);
			}
		}

		private ShapeHolder addBall(float x, float y) {
			// 绘制一个椭圆
			OvalShape circle = new OvalShape();
			// 设置椭圆高宽
			circle.resize(50f * mDensity, 50f * mDensity);
			// 初始化圆形
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x - 25f);
			shapeHolder.setY(y - 25f);
			// 产生随机颜色
			int red = (int) (100 + Math.random() * 155);
			int green = (int) (100 + Math.random() * 155);
			int blue = (int) (100 + Math.random() * 155);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			Paint paint = drawable.getPaint(); // new
												// Paint(Paint.ANTI_ALIAS_FLAG);
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
			// 遍历并绘制每一个球形对象
			for (int i = 0; i < balls.size(); ++i) {
				ShapeHolder shapeHolder = balls.get(i);
				canvas.save();
				canvas.translate(shapeHolder.getX(), shapeHolder.getY());
				shapeHolder.getShape().draw(canvas);
				canvas.restore();
			}
		}

		/**
		 * 开始动画
		 * 
		 * @description：
		 * @author ldm
		 * @date 2016-5-6 上午10:34:44
		 */
		public void startAnimation() {
			createAnimation();
			animation.start();
		}

		public void onAnimationUpdate(ValueAnimator animation) {
			// 在参数更新的时候invalidate，刷新整个View的绘制
			// 否则onDraw不会被调用，即看不到View外观的改变
			invalidate();
		}

	}
}