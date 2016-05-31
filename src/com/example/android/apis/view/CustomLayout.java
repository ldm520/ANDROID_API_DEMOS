/*
 * Copyright (C) 2013 The Android Open Source Project
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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.graphics.Rect;
import com.example.android.apis.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

/**
 * Example of writing a custom layout manager. This is a fairly full-featured
 * layout manager that is relatively general, handling all layout cases. You can
 * simplify it for more specific cases.
 */
@RemoteViews.RemoteView
public class CustomLayout extends ViewGroup {
	/** 子控件离左边的距离 */
	private int mLeftWidth;
	/** 子控件离左边的距离 */
	private int mRightWidth;

	/** 计算子控件容器的范围 */
	private final Rect mTmpContainerRect = new Rect();
	/** 计算子控件的范围 */
	private final Rect mTmpChildRect = new Rect();

	/*
	 * View的三个构造函数
	 */
	public CustomLayout(Context context) {
		super(context);
	}

	public CustomLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 任何不滚动的布局管理器都应该像这样重写该方法
	 */
	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}

	/**
	 * 测量（子）控件大小
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 子控件数量
		int count = getChildCount();
		mLeftWidth = 0;
		mRightWidth = 0;

		// 设置最大高度，最大宽度及状态
		int maxHeight = 0;
		int maxWidth = 0;
		int childState = 0;
		// 遍历所有子控件，分别进行测量
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				// 测量子控件：某一个子view宽及高,
				// 内部加上了viewGroup的padding值、margin值和传入的宽高widthUsed、heightUsed
				measureChildWithMargins(child, widthMeasureSpec, 0,
						heightMeasureSpec, 0);

				// 通过LayoutParams来设置大小
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (lp.position == LayoutParams.POSITION_LEFT) {
					mLeftWidth += Math.max(maxWidth, child.getMeasuredWidth()
							+ lp.leftMargin + lp.rightMargin);
				} else if (lp.position == LayoutParams.POSITION_RIGHT) {
					mRightWidth += Math.max(maxWidth, child.getMeasuredWidth()
							+ lp.leftMargin + lp.rightMargin);
				} else {
					maxWidth = Math.max(maxWidth, child.getMeasuredWidth()
							+ lp.leftMargin + lp.rightMargin);
				}
				maxHeight = Math.max(maxHeight, child.getMeasuredHeight()
						+ lp.topMargin + lp.bottomMargin);
				childState = combineMeasuredStates(childState,
						child.getMeasuredState());
			}
		}

		// 总宽度是内部所有子视图的宽度加上其他约束的宽度
		maxWidth += mLeftWidth + mRightWidth;

		// 检查默认的最小高度和最小宽度，取最大值
		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

		// 设置最终的尺寸
		setMeasuredDimension(
				resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
				resolveSizeAndState(maxHeight, heightMeasureSpec,
						childState << MEASURED_HEIGHT_STATE_SHIFT));
	}

	/**
	 * 设置子控件在ViewGroup中的位置
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final int count = getChildCount();

		// 视图在布局中放置的左右外边
		int leftPos = getPaddingLeft();
		int rightPos = right - left - getPaddingRight();

		// 视图流内部的中心区域
		final int middleLeft = leftPos + mLeftWidth;
		final int middleRight = rightPos - mRightWidth;

		// 所操作的布局的顶边和底边
		final int parentTop = getPaddingTop();
		final int parentBottom = bottom - top - getPaddingBottom();

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();

				// 测量宽度和测量高度
				final int width = child.getMeasuredWidth();
				final int height = child.getMeasuredHeight();

				// 计算正在放置子视图的窗体
				if (lp.position == LayoutParams.POSITION_LEFT) {
					mTmpContainerRect.left = leftPos + lp.leftMargin;
					mTmpContainerRect.right = leftPos + width + lp.rightMargin;
					leftPos = mTmpContainerRect.right;
				} else if (lp.position == LayoutParams.POSITION_RIGHT) {
					mTmpContainerRect.right = rightPos - lp.rightMargin;
					mTmpContainerRect.left = rightPos - width - lp.leftMargin;
					rightPos = mTmpContainerRect.left;
				} else {
					mTmpContainerRect.left = middleLeft + lp.leftMargin;
					mTmpContainerRect.right = middleRight - lp.rightMargin;
				}
				mTmpContainerRect.top = parentTop + lp.topMargin;
				mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;
				// //通过子视图的重心值和尺寸决定其在容器内的最终布局
				Gravity.apply(lp.gravity, width, height, mTmpContainerRect,
						mTmpChildRect);

				// 放置子视图
				child.layout(mTmpChildRect.left, mTmpChildRect.top,
						mTmpChildRect.right, mTmpChildRect.bottom);
			}
		}
	}

	/**
	 * 下面的实现部分是针对每个子视图的布局参数的，如果你不需要这些（比如说你写了一个布局管理器）不需要混合放置子视图，那么你可以删除这部分
	 */
	// ----------------------------------------------------------------------

	/**
	 * 要自定义ViewGroup支持子控件的layout_margin参数，
	 * 则自定义的ViewGroup类必须重载generateLayoutParams
	 * ()函数，并且在该函数中返回一个ViewGroup.MarginLayoutParams派生类对象，这样才能使用margin参数。
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new CustomLayout.LayoutParams(getContext(), attrs);
	}

	// 返回一组默认布局参数。没有设置布局参数执行addview（View）时，这些参数被请求时返回null，并抛出一个异常。
	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	/**
	 * 自定义子控件的LayoutParams
	 */
	public static class LayoutParams extends MarginLayoutParams {
		// 设置居中方式
		public int gravity = Gravity.TOP | Gravity.START;

		public static int POSITION_MIDDLE = 0;
		public static int POSITION_LEFT = 1;
		public static int POSITION_RIGHT = 2;

		public int position = POSITION_MIDDLE;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);

			// 获取定义在XML中的自定义属性
			TypedArray a = c.obtainStyledAttributes(attrs,
					R.styleable.CustomLayoutLP);
			gravity = a.getInt(
					R.styleable.CustomLayoutLP_android_layout_gravity, gravity);
			position = a.getInt(R.styleable.CustomLayoutLP_layout_position,
					position);
			a.recycle();
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
	}
}
