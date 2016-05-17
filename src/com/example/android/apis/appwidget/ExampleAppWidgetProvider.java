/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.example.android.apis.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.android.apis.R;

/**
 * AppWidgetProvider小部件广播组件使用：
 * 1， 实现AppWidgetProvider的子类，并至少override onUpdate()方法
 * 2，在AndroidManifest.xml中，声明上述的AppWidgetProvider的子类是一个Receiver，并且：
 * 该Receiver的intent-filter的Action必须包含“android.appwidget.action.APPWIDGET_UPDATE”；
 * 该Receiver的meta-data为“android.appwidget.provider”，并用一个xml文件来描述布局属性。
 * 3， 在第2点中的xml文件中描述布局属性的节点名称必须为“appwidget-provider”。
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 下午1:43:31
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {
	// Log打印日志标签
	private static final String TAG = "ExampleAppWidgetProvider";

	/**
	 * onUpdate() 处理AppWidgetManager.ACTION_APPWIDGET_UPDATE广播。
	 * 该广播在需要AppWidgetProvider提供RemoteViews数据时
	 * ，由AppWidgetService.sendUpdateIntentLocked()发出。
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "onUpdate");

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			//获取到id
			int appWidgetId = appWidgetIds[i];
			//设置标题
			String titlePrefix = ExampleAppWidgetConfigure.loadTitlePref(
					context, appWidgetId);
			//更新AppWidget
			updateAppWidget(context, appWidgetManager, appWidgetId, titlePrefix);
		}
	}

	/**
	 * onDeleted() 处理AppWidgetManager.ACTION_APPWIDGET_DELETED广播。
	 * 该广播在有该AppWidgetProvider的实例被删除时
	 * ，由AppWidgetService.deleteAppWidgetLocked()发出。
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.d(TAG, "onDeleted");
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			ExampleAppWidgetConfigure.deleteTitlePref(context, appWidgetIds[i]);
		}
	}

	/**
	 * onEnabled() 处理AppWidgetManager.ACTION_APPWIDGET_ENABLED广播。
	 * 该广播在该AppWidgetProvider被实例化时，由AppWidgetService.sendEnableIntentLocked()发出。
	 */
	@Override
	public void onEnabled(Context context) {
		Log.d(TAG, "onEnabled");
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName(
				"com.example.android.apis",
				".appwidget.ExampleBroadcastReceiver"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	/**
	 * onDisabled() 处理AppWidgetManager.ACTION_APPWIDGET_DISABLED广播。
	 * 该广播在该AppWidgetProvider的所有实例中的最后一个实例被删除时
	 * ，由AppWidgetService.deleteAppWidgetLocked()发出。
	 */
	@Override
	public void onDisabled(Context context) {
		Log.d(TAG, "onDisabled");
		PackageManager pm = context.getPackageManager();
		//设置组件可用
		pm.setComponentEnabledSetting(new ComponentName(
				"com.example.android.apis",
				".appwidget.ExampleBroadcastReceiver"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			String titlePrefix) {
		Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId
				+ " titlePrefix=" + titlePrefix);
		CharSequence text = context.getString(R.string.appwidget_text_format,
				ExampleAppWidgetConfigure.loadTitlePref(context, appWidgetId),
				"0x" + Long.toHexString(SystemClock.elapsedRealtime()));
		// 创建RemoteViews 对象
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_provider);
		// 设置RemoteViews 对象的文本
		views.setTextViewText(R.id.appwidget_text, text);
		// 告诉AppWidgetManager 显示 views对象给widget.
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
