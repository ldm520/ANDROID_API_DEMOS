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

package com.example.android.apis;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Demo主页面
 * 
 * @description： 
 *               该类所做的主要操作是根据AndroidMainfest.xml文件中,各个Activity的Label名称,构建分层结构,并且根据
 *               各个分层结构的特性,生成特定的ListView,并添加相应的Intent等.
 *               如:android:label="App/Activity/Hello Wrold"
 *               ,ApiDemos会根据该Label标示的目录结构,生成三层ListView.
 *               前两层Item的名称分别为App和Activity
 *               ,在点击时会调用指向ApiDemos.class自身的Intent,进入下层View 在点击第三层的名为Hello
 *               Wrold的Item时,才会调用跳转到相应Activity的Intent.
 * @author ldm
 * @date 2016-5-6 上午9:15:17
 */
public class ApiDemos extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取Intent,用来监听Activity，获取上层目录路径
		Intent intent = getIntent();
		String path = intent.getStringExtra("com.example.android.apis.Path");
		// 初次调用ApiDemos或者当前目录为最底层目录(即:当前Activity中的Item点击会跳转Activity),则设置路径为空
		if (path == null) {
			path = "";
		}
		// 给ListView设置数：获取源List(即getData(path))中的Key为title的元素作为ListView的Item
		setListAdapter(new SimpleAdapter(this, getData(path),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		// 设置是IntentFilter生效,即AndroidMainfest.xml中配置的intent-filter
		getListView().setTextFilterEnabled(true);
	}

	/**
	 * 初始化适配器数据
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-6 上午9:18:56
	 */
	protected List<Map<String, Object>> getData(String prefix) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		// 通过Intent类型,以提供给PackageManager查找所有相关Activity.
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);// 添加Action属性
		mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);// 添加Category属性
		/*
		 * PackageManager这个类是用来返回各种的关联了当前已装入设备了的应用的包的信息。
		 * ResolveInfo这个类是通过解析一个与IntentFilter相对应的intent得到的信息。
		 * 它部分地对应于从AndroidManifest.xml的< intent>标签收集到的信息。
		 */
		PackageManager pm = getPackageManager();
		// 获取所有launcher activity
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		// 若没有符合条件的Activity,即意味着ListActivity显示为空.
		if (null == list)
			return myData;
		// 路径变量,用于存放目标路径分层目录结构,根据该数组长度进行相关判断
		String[] prefixPath;
		String prefixWithSlash = prefix;
		// 若上层目录为空,则路径数组置空,否则,将各个目录结构按序存入prefixPath
		if (prefix.equals("")) {
			prefixPath = null;
		} else {
			prefixPath = prefix.split("/");
			prefixWithSlash = prefix + "/";
		}
		// 符合条件的Activity数目
		int len = list.size();
		// 该Map用于存放Intent指向ApiDemos.class的Item
		Map<String, Boolean> entries = new HashMap<String, Boolean>();
		// 逐个遍历Activity集合
		for (int i = 0; i < len; i++) {
			ResolveInfo info = list.get(i);
			// 获取当前Activity在AndroidManifest.xml中对应配置的Label值
			CharSequence labelSeq = info.loadLabel(pm);
			// 若Label值为空则使用配置的android:name值
			String label = labelSeq != null ? labelSeq.toString()
					: info.activityInfo.name;
			// 如果没有上层目录,或者当前Activity是该prefix目录下的子文件
			if (prefixWithSlash.length() == 0
					|| label.startsWith(prefixWithSlash)) {
				// 分割当前Activity实际目录结构,存入labelPath数组.
				String[] labelPath = label.split("/");
				// 若上级目录不存在,则去当前目录值，否则为索引值
				String nextLabel = prefixPath == null ? labelPath[0]
						: labelPath[prefixPath.length];
				// 若上级目录数参数和Activity实际上级目录数相等,则将Activity加入List,否则添加Label到视图.
				if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
					addItem(myData,
							nextLabel,
							activityIntent(
									info.activityInfo.applicationInfo.packageName,
									info.activityInfo.name));
				} else {
					if (entries.get(nextLabel) == null) {
						addItem(myData, nextLabel,
								browseIntent(prefix.equals("") ? nextLabel
										: prefix + "/" + nextLabel));
						entries.put(nextLabel, true);
					}
				}
			}
		}
		// 对数据排序
		Collections.sort(myData, sDisplayNameComparator);

		return myData;
	}

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	// 该方法根据packageName和ClassName生成指向特定Activity的Intent
	protected Intent activityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	// 该方法根据参数生成指向当前ApiDemos.class的Intent
	protected Intent browseIntent(String path) {
		Intent result = new Intent();
		result.setClass(this, ApiDemos.class);
		result.putExtra("com.example.android.apis.Path", path);
		return result;
	}

	protected void addItem(List<Map<String, Object>> data, String name,
			Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}

	// 当用户点击ListView时所进行的操作.
	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);// 点击跳转到对应Activity
	}
}
