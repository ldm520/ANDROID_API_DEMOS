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

package com.example.android.apis.app;

import com.example.android.apis.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Frament通过Bundle传递数据
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午2:09:07
 */
public class FragmentArguments extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_arguments);
		if (savedInstanceState == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			// 首次初始化，在Activity创建Fragment
			Fragment newFragment = MyFragment.newInstance("From Arguments");
			// 添加Fragment
			ft.add(R.id.created, newFragment);
			// 添加后要执行commit()才有效
			ft.commit();
		}
	}

	public static class MyFragment extends Fragment {
		CharSequence mLabel;

		/**
		 * 创建MyFragment
		 * 
		 * @description：
		 * @author ldm
		 * @date 2016-5-12 下午2:14:25
		 */
		static MyFragment newInstance(CharSequence label) {
			MyFragment f = new MyFragment();
			Bundle b = new Bundle();
			// 设置数据
			b.putCharSequence("label", label);
			// 传递Bundle
			f.setArguments(b);
			return f;
		}

		/**
		 * 在Activity.onCreate方法之前调用，可以获取除了View之外的资源
		 */
		@Override
		public void onInflate(Activity activity, AttributeSet attrs,
				Bundle savedInstanceState) {
			super.onInflate(activity, attrs, savedInstanceState);
			// 自定义属性
			TypedArray a = activity.obtainStyledAttributes(attrs,
					R.styleable.FragmentArguments);
			mLabel = a.getText(R.styleable.FragmentArguments_android_label);
			a.recycle();
		}

		/**
		 * on Attach 执行完后会立刻调用此方法，通常被用于读取保存的状态值，获取或者初始化一些数据
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			Bundle args = getArguments();
			if (args != null) {
				mLabel = args.getCharSequence("label", mLabel);
			}
		}

		/**
		 * 创建 Fra gment 中显示的 view, 其中 inflater 用来装载布局文件， container 表示 <fragment>
		 * 标签的父标签对应的 ViewGroup 对象， savedInstanceState 可以获取 Fragment 保存的状态
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			//获取xml布局
			View v = inflater.inflate(R.layout.hello_world, container, false);
			View tv = v.findViewById(R.id.text);
			((TextView) tv).setText(mLabel != null ? mLabel : "(no label)");
			tv.setBackgroundDrawable(getResources().getDrawable(
					android.R.drawable.gallery_thumb));
			return v;
		}
	}

}
