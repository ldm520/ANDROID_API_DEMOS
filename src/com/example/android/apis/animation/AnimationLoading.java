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
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import com.example.android.apis.R;

import java.util.ArrayList;

import android.animation.Animator;
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
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * API动画效果之：加载动画
 * 
 * @description：
 * @author ldm
 * @date 2016-5-6 上午10:40:56
 */
public class AnimationLoading extends Activity {
	// 动画效果时间
	private static final int DURATION = 1500;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_loading);
		// 初始化LinearLayout
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView animView = new MyAnimationView(this);
		// 代码动态为LinearLayout添加View
		container.addView(animView);

		Button starter = (Button) findViewById(R.id.startButton);
		starter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				animView.startAnimation();// 开始动画
			}
		});
	}

	/**
	 * 自定义动画 View
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-6 上午10:42:18
	 */
	public class MyAnimationView extends View implements
			ValueAnimator.AnimatorUpdateListener {
		// 定义Ball的大小
		private static final float BALL_SIZE = 100f;

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
		Animator animation = null;

		public MyAnimationView(Context context) {
			super(context);
			addBall(50, 50);
			addBall(200, 50);
			addBall(350, 50);
			addBall(500, 50, Color.GREEN);
		}

		private void createAnimation() {
			Context appContext = AnimationLoading.this;

			if (animation == null) {

				// 载入根节点为<objectAnimator>的xml资源文件，解析放进ObjectAnimator类对象
				ObjectAnimator anim = (ObjectAnimator) AnimatorInflater
						.loadAnimator(appContext, R.anim.object_animator);
				anim.addUpdateListener(this);
				anim.setTarget(balls.get(0));

				// 载入根节点为<animator>的xml资源文件，解析放进ValueAnimator类对象
				ValueAnimator fader = (ValueAnimator) AnimatorInflater
						.loadAnimator(appContext, R.anim.animator);
				fader.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					public void onAnimationUpdate(ValueAnimator animation) {
						invalidate();
						// ValueAnimator动画需要在监听器中自己设置对象的属性值
						// 这里改变的是alpha值
						balls.get(1).setAlpha(
								(Float) animation.getAnimatedValue());
					}
				});

				// 载入根节点为<set>的xml资源文件，解析放进AnimatorSet类对象
				AnimatorSet seq = (AnimatorSet) AnimatorInflater.loadAnimator(
						appContext, R.anim.animator_set);// x和y属性同时改变的动画集合
				seq.setTarget(balls.get(2));
				// 这里要注意：因为AnimatorSet没有设置AnimatorUpdateListener的方法，
				// 所以如果其他动画没有设置AnimatorUpdateListener来进行View的invalidate()刷新，
				// 这个AnimatorSet seq是不刷新的

				// 载入根节点为<objectAnimator>的xml资源文件，解析放进ObjectAnimator类对象
				ObjectAnimator colorizer = (ObjectAnimator) AnimatorInflater
						.loadAnimator(appContext, R.anim.color_animator);
				colorizer.setTarget(balls.get(3));
				colorizer.addUpdateListener(this);

				// 总的AnimationSet，所有的动画同时播放
				animation = new AnimatorSet();
				((AnimatorSet) animation).playTogether(anim, fader, seq,
						colorizer);
			}
		}

		public void startAnimation() {
			createAnimation();
			animation.start();
		}

		private ShapeHolder createBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(BALL_SIZE, BALL_SIZE);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x);
			shapeHolder.setY(y);
			return shapeHolder;
		}

		private void addBall(float x, float y, int color) {
			ShapeHolder shapeHolder = createBall(x, y);
			shapeHolder.setColor(color);
			balls.add(shapeHolder);
		}

		private void addBall(float x, float y) {
			ShapeHolder shapeHolder = createBall(x, y);
			int red = (int) (100 + Math.random() * 155);
			int green = (int) (100 + Math.random() * 155);
			int blue = (int) (100 + Math.random() * 155);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			Paint paint = shapeHolder.getShape().getPaint();
			int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue
					/ 4;
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f,
					color, darkColor, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			balls.add(shapeHolder);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			 // 遍历并绘制每一个球形对象
            for (ShapeHolder ball : balls) {
                // 这里是canvas.translate到一个地方，进行绘制，之后再translate回来
                // 跟先save后restore的作用相同
                canvas.translate(ball.getX(), ball.getY());
                ball.getShape().draw(canvas);
                canvas.translate(-ball.getX(), -ball.getY());
            }
		}

		public void onAnimationUpdate(ValueAnimator animation) {

			invalidate();//刷新
//			ShapeHolder ball = balls.get(0);
//			ball.setY((Float) animation.getAnimatedValue());
		}
	}
}