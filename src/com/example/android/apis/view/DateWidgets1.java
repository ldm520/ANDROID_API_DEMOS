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

package com.example.android.apis.view;

import com.example.android.apis.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.view.View;

import java.util.Calendar;

/**
 * @description：
 * @author ldm
 * @date 2016-4-21 上午9:02:49
 */
public class DateWidgets1 extends Activity {

	// 显示时间TextView
	private TextView mDateDisplay;

	// 时间日期表示单位 :年／月／日／小时／分
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	// 时间显示格式
	static final int TIME_12_HOUR_STYLE = 0;// 12小时制
	static final int TIME_24_HOUR_STYLE = 1;// 24小时制
	static final int DATE_DIALOG = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_widgets_example_1);
		initCalendar();
		initViewAndEvents();
		updateDisplay();
	}

	private void initViewAndEvents() {
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		setDialogOnClickListener(R.id.pickDate, DATE_DIALOG);
		setDialogOnClickListener(R.id.pickTime12, TIME_12_HOUR_STYLE);
		setDialogOnClickListener(R.id.pickTime24, TIME_24_HOUR_STYLE);
	}

	private void initCalendar() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	@SuppressWarnings("deprecation")
	private void setDialogOnClickListener(int buttonId, final int dialogId) {
		Button b = (Button) findViewById(buttonId);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(dialogId);// showDialog()调用createDialog()和onPrepareDialog()
			}
		});
	}

	// 其中createDialog()调用onCreateDialog()。
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_12_HOUR_STYLE:
		case TIME_24_HOUR_STYLE:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					id == TIME_24_HOUR_STYLE);
		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	// 每次弹出对话框时被回调以动态更新对话框内容的方法
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TIME_12_HOUR_STYLE:
		case TIME_24_HOUR_STYLE:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		case DATE_DIALOG:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	/**
	 * 在TextView中显示选择的时间
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-21 上午8:47:38
	 */
	private void updateDisplay() {
		StringBuffer sb = new StringBuffer();
		sb.append("当前时间：").append(mMonth + 1).append("-").append(mDay)
				.append("-").append(mYear).append(" ").append(addZero(mHour))
				.append(":").append(addZero(mMinute));
		mDateDisplay.setText(sb.toString());
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	private static String addZero(int num) {
		if (num >= 10)
			return String.valueOf(num);
		else
			return "0" + String.valueOf(num);
	}
}
