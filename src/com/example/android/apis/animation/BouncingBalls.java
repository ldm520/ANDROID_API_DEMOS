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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.android.apis.R;

/**
 * API动画效果之：球反弹效果
 * 
 * @description：
 * @author ldm
 * @date 2016-5-6 下午3:45:02
 */
public class BouncingBalls extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bouncing_balls);
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		container.addView(new MyAnimationView(this));
	}

	public class MyAnimationView extends View {

		private static final int RED = 0xffFF8080;
		private static final int BLUE = 0xff8080FF;

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();

		public MyAnimationView(Context context) {
			super(context);

			// Animate background color
			// Note that setting the background color will automatically
			// invalidate the
			// view, so that the animated color, and the bouncing balls, get
			// redisplayed on
			// every frame of the animation.
			// 自定义View的背景切换动画
			ValueAnimator colorAnim = ObjectAnimator.ofInt(this,
					"backgroundColor", RED, BLUE);// 背景颜色变化
			colorAnim.setDuration(3000);// 动画时间（每三秒发生变化 ）
			colorAnim.setEvaluator(new ArgbEvaluator());
			colorAnim.setRepeatCount(ValueAnimator.INFINITE);
			colorAnim.setRepeatMode(ValueAnimator.REVERSE);
			colorAnim.start();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() != MotionEvent.ACTION_DOWN
					&& event.getAction() != MotionEvent.ACTION_MOVE) {
				return false;
			}

			ShapeHolder newBall = addBall(event.getX(), event.getY());

			float startY = newBall.getY();
			float endY = getHeight() - 50f;

			float h = getHeight();
			float eventy = event.getY();

			int duration = (int) (500 * ((h - eventy) / h));

			// 设置加速掉落的效果
			ValueAnimator bounceAnim = ObjectAnimator.ofFloat(newBall, "y",
					startY, endY);
			bounceAnim.setDuration(duration);
			bounceAnim.setInterpolator(new AccelerateInterpolator());

			// 当掉落到底部的时候，球体压扁，高度降低
			ValueAnimator squashAnim1 = ObjectAnimator.ofFloat(newBall, "x",
					newBall.getX(), newBall.getX() - 25);
			squashAnim1.setDuration(duration / 4);
			squashAnim1.setRepeatCount(1);
			squashAnim1.setRepeatMode(ValueAnimator.REVERSE);
			squashAnim1.setInterpolator(new DecelerateInterpolator());

			ValueAnimator squashAnim2 = ObjectAnimator.ofFloat(newBall,
					"width", newBall.getWidth(), newBall.getWidth() + 50);
			squashAnim2.setDuration(duration / 4);
			squashAnim2.setRepeatCount(1);
			squashAnim2.setRepeatMode(ValueAnimator.REVERSE);
			squashAnim2.setInterpolator(new DecelerateInterpolator());

			ValueAnimator strechAnim1 = ObjectAnimator.ofFloat(newBall, "y",
					endY, endY + 25);
			strechAnim1.setDuration(duration / 4);
			strechAnim1.setRepeatCount(1);
			strechAnim1.setRepeatMode(ValueAnimator.REVERSE);
			strechAnim1.setInterpolator(new DecelerateInterpolator());

			ValueAnimator strechAnim2 = ObjectAnimator.ofFloat(newBall,
					"height", newBall.getHeight(), newBall.getHeight() - 25);
			strechAnim2.setDuration(duration / 4);
			strechAnim2.setRepeatCount(1);
			strechAnim2.setRepeatMode(ValueAnimator.REVERSE);
			strechAnim2.setInterpolator(new DecelerateInterpolator());

			ValueAnimator bounceBack = ObjectAnimator.ofFloat(newBall, "y",
					endY, startY + 25);
			bounceBack.setDuration(duration);
			bounceAnim.setInterpolator(new DecelerateInterpolator());

			AnimatorSet set = new AnimatorSet();
			set.play(bounceAnim).before(squashAnim1);

			set.play(squashAnim1).with(squashAnim2);
			set.play(squashAnim1).with(strechAnim1);
			set.play(squashAnim1).with(strechAnim2);
			set.play(bounceBack).after(strechAnim2);

			// 逐渐消失
			ValueAnimator fadeAnimator = ObjectAnimator.ofFloat(newBall,
					"alpha", 1F, 0F);
			fadeAnimator.setDuration(600L);

			fadeAnimator.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					balls.remove(((ObjectAnimator) animation).getTarget());
				}

			});

			AnimatorSet animationSet = new AnimatorSet();
			animationSet.play(set).before(fadeAnimator);

			animationSet.start();
			return true;
		}

		private ShapeHolder addBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(50f, 50f);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x - 25f);
			shapeHolder.setY(y - 25f);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			Paint paint = drawable.getPaint(); // new
												// Paint(Paint.ANTI_ALIAS_FLAG);
			int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue
					/ 4;
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f,
					color, darkColor, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			shapeHolder.setPaint(paint);
			balls.add(shapeHolder);
			return shapeHolder;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// 在画布上绘制所有圆形图形
			for (int i = 0; i < balls.size(); ++i) {
				ShapeHolder shapeHolder = balls.get(i);
				canvas.save();
				canvas.translate(shapeHolder.getX(), shapeHolder.getY());
				shapeHolder.getShape().draw(canvas);
				canvas.restore();
			}
		}
	}
}