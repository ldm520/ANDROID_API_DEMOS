/*
 * Copyright (C) 2012 The Android Open Source Project
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.MediaRouteActionProvider;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android.apis.R;
import com.example.android.apis.graphics.CubeRenderer;

@SuppressLint("NewApi")
public class PresentationWithMediaRouterActivity extends Activity {
	private final String TAG = "PresentationWithMediaRouterActivity";
	// MediaRouter用于和MediaRouterService交互一起管理多媒体的播放行为，并维护当前已经配对上的remote
	// display设备，包括Wifi diplay、蓝牙A2DP设备、chromecast设备。
	private MediaRouter mMediaRouter;// MediaRouter提供了快速获得系统中用于演示（presentations）默认显示设备的方法。
	private DemoPresentation mPresentation;
	private GLSurfaceView mSurfaceView;
	private TextView mInfoTextView;
	private boolean mPaused;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * 获取到媒体路由，当媒体路由被选择或取消选择或者路由首选的presentation显示屏幕发生变化时，
		 * 它都会发送通知消息。一个应用程序可以非常简单通过地观察这些通知消息来自动地在首选的presentation
		 * 显示屏幕上显示或隐藏一个presentation。
		 */
		mMediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
		setContentView(R.layout.presentation_with_media_router_activity);
		mSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);
		// 设置我们要渲染的图形为CubeRenderer
		mSurfaceView.setRenderer(new CubeRenderer(false));
		mInfoTextView = (TextView) findViewById(R.id.info);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 注册一个具体的MediaRouter.Callback回调对象，并用来启动与特定MediaRouteSelector相匹配的媒体路由的发现
		 * ， 以及监听发现的媒体路由的相关事件，如用户已选择连接到某个媒体路由设备、某个媒体路由设备的特性发生改变或者断开某个媒体路由等事件。
		 * 应用为了使用相关的媒体路由，必须调用该函数来启动媒体路由的发现，并通过登记的回调函数接收相关的事件。
		 */
		// 设置对媒体路由变化的监听
		mMediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO,
				mMediaRouterCallback);
		mPaused = false;
		updatePresentation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除回调监听
		mMediaRouter.removeCallback(mMediaRouterCallback);
		mPaused = true;
		updateContents();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// 当Activity不可见时，清除Presentation
		if (mPresentation != null) {
			Log.i(TAG,
					"Dismissing presentation because the activity is no longer visible.");
			mPresentation.dismiss();
			mPresentation = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// 加载菜单
		getMenuInflater().inflate(R.menu.presentation_with_media_router_menu,
				menu);

		MenuItem mediaRouteMenuItem = menu.findItem(R.id.menu_media_route);
		MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) mediaRouteMenuItem
				.getActionProvider();
		mediaRouteActionProvider
				.setRouteTypes(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);

		// 显示菜需要返回true
		return true;
	}

	/**
	 * 代码示例展示了Presentation实现对象的作为一个单独方法的控制层.当一个显示器处理不可选状态或者失去联系时，该方法负责清除无效的展示对象，
	 * 而在一个显示设备连接时负责创建一个展示对象。
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-6-4 上午9:34:05
	 */
	private void updatePresentation() {
		// 获取当前路由
		MediaRouter.RouteInfo route = mMediaRouter
				.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
		// 判断route信息是否为空，如果不为空则返回被选择演示（presentation）设备。该方法只对
		// route信息类型为ROUTE_TYPE_LIVE_VIDEO有效。
		Display presentationDisplay = route != null ? route
				.getPresentationDisplay() : null;

		// 清除无用的展示对象
		if (mPresentation != null
				&& mPresentation.getDisplay() != presentationDisplay) {
			Log.i(TAG,
					"Dismissing presentation because the current route no longer "
							+ "has a presentation display.");
			mPresentation.dismiss();
			mPresentation = null;
		}

		// 根据需要显示展示 对象
		if (mPresentation == null && presentationDisplay != null) {
			Log.i(TAG, "Showing presentation on display: "
					+ presentationDisplay);
			mPresentation = new DemoPresentation(this, presentationDisplay);
			mPresentation.setOnDismissListener(mOnDismissListener);
			try {
				mPresentation.show();
			} catch (WindowManager.InvalidDisplayException ex) {
				Log.w(TAG,
						"Couldn't show presentation!  Display was removed in "
								+ "the meantime.", ex);
				mPresentation = null;
			}
		}

		// 更新Activity中内容
		updateContents();
	}

	/**
	 * 更新内容
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-6-4 上午9:27:40
	 */
	private void updateContents() {
		if (mPresentation != null) {
			mInfoTextView
					.setText(getResources()
							.getString(
									R.string.presentation_with_media_router_now_playing_remotely,
									mPresentation.getDisplay().getName()));
			mSurfaceView.setVisibility(View.INVISIBLE);
			mSurfaceView.onPause();
			if (mPaused) {
				mPresentation.getSurfaceView().onPause();
			} else {
				mPresentation.getSurfaceView().onResume();
			}
		} else {
			mInfoTextView
					.setText(getResources()
							.getString(
									R.string.presentation_with_media_router_now_playing_locally,
									getWindowManager().getDefaultDisplay()
											.getName()));
			mSurfaceView.setVisibility(View.VISIBLE);
			if (mPaused) {
				mSurfaceView.onPause();
			} else {
				mSurfaceView.onResume();
			}
		}
	}

	private final MediaRouter.SimpleCallback mMediaRouterCallback = new MediaRouter.SimpleCallback() {
		// 当用户连接到一个媒体路由输出设备上时调用。
		@Override
		public void onRouteSelected(MediaRouter router, int type, RouteInfo info) {
			Log.d(TAG, "onRouteSelected: type=" + type + ", info=" + info);
			updatePresentation();
		}

		// 当用户断开一个媒体路由输出设备时调用。
		@Override
		public void onRouteUnselected(MediaRouter router, int type,
				RouteInfo info) {
			Log.d(TAG, "onRouteUnselected: type=" + type + ", info=" + info);
			updatePresentation();
		}

		// 当展示的显示器改变现实像素，如从720p变到1080p分辨率。
		@Override
		public void onRoutePresentationDisplayChanged(MediaRouter router,
				RouteInfo info) {
			Log.d(TAG, "onRoutePresentationDisplayChanged: info=" + info);
			updatePresentation();
		}
	};

	/**
	 * Listens for when presentations are dismissed.
	 */
	private final DialogInterface.OnDismissListener mOnDismissListener = new DialogInterface.OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {
			if (dialog == mPresentation) {
				Log.i(TAG, "Presentation was dismissed.");
				mPresentation = null;
				updateContents();
			}
		}
	};

	/**
	 * 要为辅助显示屏创建独特的内容，您需要扩展Presentation类，并实现onCreate()回调方法。在onCreate()中，
	 * 调用setContentView()来指定您要在辅助显示屏上显示的UI。
	 * 作为Dialog类的扩展，Presentation类提供了一个区域，在其中， 您的应用可以在辅助显示屏上显示不同的UI。
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-6-4 上午9:08:45
	 */
	private final static class DemoPresentation extends Presentation {
		/**
		 * GLSurfaceView是一个视图，继承至SurfaceView，它内嵌的surface专门负责OpenGL渲染。
		 * GLSurfaceView提供了下列特性： 1>
		 * 管理一个surface，这个surface就是一块特殊的内存，能直接排版到android的视图view上。 2> 管理一个EGL
		 * display，它能让opengl把内容渲染到上述的surface上。 3> 用户自定义渲染器(render)。 4>
		 * 让渲染器在独立的线程里运作，和UI线程分离。 5> 支持按需渲染(on-demand)和连续渲染(continuous)。
		 * 6>一些可选工具，如调试。
		 */
		private GLSurfaceView mSurfaceView;

		public DemoPresentation(Context context, Display display) {
			super(context, display);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// 必须要写下面这句话，调用父类的onCreate();
			super.onCreate(savedInstanceState);
			// 设置布局
			setContentView(R.layout.presentation_with_media_router_content);
			// mSurfaceView对象获取：建立有趣的视觉
			mSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);
			mSurfaceView.setRenderer(new CubeRenderer(false));
		}

		public GLSurfaceView getSurfaceView() {
			return mSurfaceView;
		}
	}
}
