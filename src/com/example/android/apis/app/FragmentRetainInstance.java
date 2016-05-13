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
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * 通过Fragment保存状态。
 * 通常在Activity销毁时和Activity关联的Fragment也会被销毁。当Activity重建时会自动创建相关的Fragment
 * 。因此经常在Activity的onCreate 函数中判处savedInstanceState 是否为空，（当Activity
 * 有关联的Fragment时，重建Activity时savedInstanceState不为空）来避免重复创建Fragment。
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午10:44:54
 */
public class FragmentRetainInstance extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化添加Fragmnet
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, new UiFragment()).commit();
		}
	}

	/**
	 * This is a fragment showing UI that will be updated from work done in the
	 * retained fragment.
	 */
	public static class UiFragment extends Fragment {
		RetainedFragment mWorkFragment;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_retain_instance,
					container, false);

			// Watch for button clicks.
			Button button = (Button) v.findViewById(R.id.restart);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mWorkFragment.restart();
				}
			});

			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			FragmentManager fm = getFragmentManager();
			mWorkFragment = (RetainedFragment) fm.findFragmentByTag("work");
			if (mWorkFragment == null) {
				mWorkFragment = new RetainedFragment();
				mWorkFragment.setTargetFragment(this, 0);
				fm.beginTransaction().add(mWorkFragment, "work").commit();
			}
		}

	}

	public static class RetainedFragment extends Fragment {
		ProgressBar mProgressBar;
		int mPosition;
		boolean mReady = false;
		boolean mQuiting = false;

		/**
		 * 线程模拟数据让进度条显示进度
		 */
		final Thread mThread = new Thread() {
			@Override
			public void run() {
				// 设备进度条最大值
				int max = 10000;

				// 死循环，一直在进行
				while (true) {

					synchronized (this) {
						// 如果UI没准备好或进度已达最大值
						while (!mReady || mPosition >= max) {
							if (mQuiting) {
								return;
							}
							try {
								wait();
							} catch (InterruptedException e) {
							}
						}

						// 更新进度
						mPosition++;
						max = mProgressBar.getMax();
						mProgressBar.setProgress(mPosition);
					}
					// 设置等待时间
					synchronized (this) {
						try {
							wait(50);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		};

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// 确定是否让fragment保存状态。注意设置保存状态之后fragment的生命周期会有些变化。
			setRetainInstance(true);

			// 启动线程
			mThread.start();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// 初始化ProgressBar
			mProgressBar = (ProgressBar) getTargetFragment().getView()
					.findViewById(R.id.progress_horizontal);
			synchronized (mThread) {
				mReady = true;
				mThread.notify();
			}
		}

		@Override
		public void onDestroy() {
			// 线程结束
			synchronized (mThread) {
				mReady = false;
				mQuiting = true;
				mThread.notify();
			}

			super.onDestroy();
		}

		// Fragment与Activity分离
		@Override
		public void onDetach() {
			synchronized (mThread) {
				mProgressBar = null;
				mReady = false;
				mThread.notify();
			}

			super.onDetach();
		}

		/**
		 * 重新开始进度
		 * 
		 * @description：
		 * @author ldm
		 * @date 2016-5-13 上午11:01:06
		 */
		public void restart() {
			synchronized (mThread) {
				mPosition = 0;
				mThread.notify();
			}
		}
	}
}
