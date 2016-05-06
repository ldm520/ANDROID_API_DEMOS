/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.android.apis.accessibility;

import com.example.android.apis.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

/** Acts as a go-between for all AccessibilityEvents sent from items in the ListView, providing the
 *  option of sending more context to an AccessibilityService by adding more AccessiblityRecords to
 *  an event.
 */
/**
 * 创建Android辅助性功能自定义View
 * 
 * @description：
 * @author ldm
 * @date 2016-5-6 上午10:12:34
 */
public class TaskListView extends ListView {

	public TaskListView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	/**
	 * This method will fire whenever a child event wants to send an
	 * AccessibilityEvent. As a result, it's a great place to add more
	 * AccessibilityRecords, if you want. In this case, the code is grabbing the
	 * position of the item in the list, and assuming that to be the priority
	 * for the task.
	 */
	/**
	 * (API级别14)当你视图中的子视图产生了一个AccessibilityEvent，系统调用这个方法。
	 * 这个步骤允许父视图修改辅助功能的事件和其他信息
	 * 。如果你的自定义视图有子视图,如果父视图可以对辅助功能服务有用的事件提供上下文信息，此时，必须实现该方法。
	 * 为了支持自定义的视图的这些辅助功能方法，应该采取以下方法: 如果你的应用程序是Android
	 * 4.0(API级别14)以上，直接在自定义视图类中覆盖并实现上面列出的辅助功能方法。 如果自定义视图为兼容安卓1.6(API
	 * 4级)及以上,对你的项目中加上Android Support
	 * Library，版本5或更高。然后,在您的自定义视图类,调用ViewCompat.setAccessibilityDelegate
	 * ()方法来实现上面的辅助功能方法。 对于这种方法的一个示例,请参阅Android支持库(修订5或更高)示例。
	 */
	@Override
	public boolean onRequestSendAccessibilityEvent(View child,
			AccessibilityEvent event) {
		// Add a record for ourselves as well.
		AccessibilityEvent record = AccessibilityEvent.obtain();
		super.onInitializeAccessibilityEvent(record);

		int priority = (Integer) child.getTag();
		String priorityStr = "Priority: " + priority;
		record.setContentDescription(priorityStr);

		event.appendRecord(record);
		return true;
	}
}

/**
 * Adds Accessibility information to individual child views of rows in the list.
 */
final class TaskAdapter extends BaseAdapter {

	private String[] mLabels = null;
	private boolean[] mCheckboxes = null;
	private Context mContext = null;

	public TaskAdapter(Context context, String[] labels, boolean[] checkboxes) {
		super();
		mContext = context;
		mLabels = labels;
		mCheckboxes = checkboxes;
	}

	@Override
	public int getCount() {
		return mLabels.length;
	}

	/**
	 * Expands the views for individual list entries, and sets content
	 * descriptions for use by the TaskBackAccessibilityService.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater
					.inflate(R.layout.tasklist_row, parent, false);
		}

		CheckBox checkbox = (CheckBox) convertView
				.findViewById(R.id.tasklist_finished);
		checkbox.setChecked(mCheckboxes[position]);

		TextView label = (TextView) (convertView
				.findViewById(R.id.tasklist_label));
		label.setText(mLabels[position]);

		String contentDescription = new StringBuilder()
				.append(mContext.getString(R.string.task_name)).append(' ')
				.append(mLabels[position]).toString();
		label.setContentDescription(contentDescription);

		convertView.setTag(position);

		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return mLabels[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
