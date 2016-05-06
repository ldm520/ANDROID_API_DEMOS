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

package com.example.android.apis.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.android.apis.R;

/**
 * 如何使用Android中各种Dialog
 * @description：
 * @author ldm
 * @date 2016-4-29 上午11:13:40
 */
public class AlertDialogSamples extends Activity {
	// 普通的YES和NO两个按钮对话框
	private static final int DIALOG_YES_NO_MESSAGE = 1;
	// 显示内容较长的对话框
	private static final int DIALOG_YES_NO_LONG_MESSAGE = 2;
	// 列表内容对话框
	private static final int DIALOG_LIST = 3;
	// 带进度条对话框
	private static final int DIALOG_PROGRESS = 4;
	// 单选对话框
	private static final int DIALOG_SINGLE_CHOICE = 5;
	// 多选对话框
	private static final int DIALOG_MULTIPLE_CHOICE = 6;
	// 可以输入文本对话框
	private static final int DIALOG_TEXT_ENTRY = 7;
	//
	private static final int DIALOG_MULTIPLE_CHOICE_CURSOR = 8;
	private static final int DIALOG_YES_NO_ULTRA_LONG_MESSAGE = 9;
	// 透明主题样式对话框
	private static final int DIALOG_YES_NO_OLD_SCHOOL_MESSAGE = 10;
	// 样式为HOLO LIGHT的对话框
	private static final int DIALOG_YES_NO_HOLO_LIGHT_MESSAGE = 11;
	// 进度条最大值
	private static final int MAX_PROGRESS = 100;
	// 进度条Dialog
	private ProgressDialog mProgressDialog;
	// 当前进度值
	private int mProgress;
	// 进度条改变消息Handler
	private Handler mProgressHandler;

	/**
	 * 通过onCreateDialog方法创建对应的对话框
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_YES_NO_MESSAGE:
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_two_buttons_title)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// 可以在这里处理点击OK键后的业务
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 取消操作
								}
							}).create();
		case DIALOG_YES_NO_OLD_SCHOOL_MESSAGE:
			return new AlertDialog.Builder(AlertDialogSamples.this,
					AlertDialog.THEME_TRADITIONAL)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_two_buttons_title)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		case DIALOG_YES_NO_HOLO_LIGHT_MESSAGE:
			return new AlertDialog.Builder(AlertDialogSamples.this,
					AlertDialog.THEME_HOLO_LIGHT)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_two_buttons_title)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		case DIALOG_YES_NO_LONG_MESSAGE:
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_two_buttons_msg)
					.setMessage(R.string.alert_dialog_two_buttons2_msg)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNeutralButton(R.string.alert_dialog_something,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
								}
							}).create();
		case DIALOG_YES_NO_ULTRA_LONG_MESSAGE:
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_two_buttons_msg)
					.setMessage(R.string.alert_dialog_two_buttons2ultra_msg)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNeutralButton(R.string.alert_dialog_something,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();
		case DIALOG_LIST:
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setTitle(R.string.select_dialog)
					.setItems(R.array.select_dialog_items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									String[] items = getResources()
											.getStringArray(
													R.array.select_dialog_items);
									new AlertDialog.Builder(
											AlertDialogSamples.this)
											.setMessage(
													"You selected: " + which
															+ " , "
															+ items[which])
											.show();
								}
							}).create();
		case DIALOG_PROGRESS:
			mProgressDialog = new ProgressDialog(AlertDialogSamples.this);
			mProgressDialog.setIconAttribute(android.R.attr.alertDialogIcon);
			mProgressDialog.setTitle(R.string.select_dialog);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMax(MAX_PROGRESS);
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getText(R.string.alert_dialog_hide),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					getText(R.string.alert_dialog_cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});
			return mProgressDialog;
		case DIALOG_SINGLE_CHOICE:
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_single_choice)
					.setSingleChoiceItems(R.array.select_dialog_items2, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		case DIALOG_MULTIPLE_CHOICE:
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIcon(R.drawable.ic_popup_reminder)
					.setTitle(R.string.alert_dialog_multi_choice)
					.setMultiChoiceItems(
							R.array.select_dialog_items3,
							new boolean[] { false, true, false, true, false,
									false, false },
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {

								}
							})
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		case DIALOG_MULTIPLE_CHOICE_CURSOR:
			String[] projection = new String[] { ContactsContract.Contacts._ID,
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.SEND_TO_VOICEMAIL };
			Cursor cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI,
					projection, null, null, null);
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIcon(R.drawable.ic_popup_reminder)
					.setTitle(R.string.alert_dialog_multi_choice_cursor)
					.setMultiChoiceItems(cursor,
							ContactsContract.Contacts.SEND_TO_VOICEMAIL,
							ContactsContract.Contacts.DISPLAY_NAME,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton, boolean isChecked) {
									Toast.makeText(
											AlertDialogSamples.this,
											"Readonly Demo Only - Data will not be updated",
											Toast.LENGTH_SHORT).show();
								}
							}).create();
		case DIALOG_TEXT_ENTRY:
			// 自定义对话框的展示内容及布局界面
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_text_entry, null);
			return new AlertDialog.Builder(AlertDialogSamples.this)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_text_entry)
					.setView(textEntryView)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alert_dialog);

		/*
		 * Display a text message with yes/no buttons and handle each message as
		 * well as the cancel action
		 */
		Button twoButtonsTitle = (Button) findViewById(R.id.two_buttons);
		twoButtonsTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_YES_NO_MESSAGE);
			}
		});

		/*
		 * Display a long text message with yes/no buttons and handle each
		 * message as well as the cancel action
		 */
		Button twoButtons2Title = (Button) findViewById(R.id.two_buttons2);
		twoButtons2Title.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_YES_NO_LONG_MESSAGE);
			}
		});

		/*
		 * Display an ultra long text message with yes/no buttons and handle
		 * each message as well as the cancel action
		 */
		Button twoButtons2UltraTitle = (Button) findViewById(R.id.two_buttons2ultra);
		twoButtons2UltraTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_YES_NO_ULTRA_LONG_MESSAGE);
			}
		});

		/* Display a list of items */
		Button selectButton = (Button) findViewById(R.id.select_button);
		selectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_LIST);
			}
		});

		/* Display a custom progress bar */
		Button progressButton = (Button) findViewById(R.id.progress_button);
		progressButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_PROGRESS);
				mProgress = 0;
				mProgressDialog.setProgress(0);
				mProgressHandler.sendEmptyMessage(0);
			}
		});

		/* Display a radio button group */
		Button radioButton = (Button) findViewById(R.id.radio_button);
		radioButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_SINGLE_CHOICE);
			}
		});

		/* Display a list of checkboxes */
		Button checkBox = (Button) findViewById(R.id.checkbox_button);
		checkBox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_MULTIPLE_CHOICE);
			}
		});

		/* Display a list of checkboxes, backed by a cursor */
		Button checkBox2 = (Button) findViewById(R.id.checkbox_button2);
		checkBox2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_MULTIPLE_CHOICE_CURSOR);
			}
		});

		/* Display a text entry dialog */
		Button textEntry = (Button) findViewById(R.id.text_entry_button);
		textEntry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_TEXT_ENTRY);
			}
		});

		/* Two points, in the traditional theme */
		Button twoButtonsOldSchoolTitle = (Button) findViewById(R.id.two_buttons_old_school);
		twoButtonsOldSchoolTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_YES_NO_OLD_SCHOOL_MESSAGE);
			}
		});

		/* Two points, in the light holographic theme */
		Button twoButtonsHoloLightTitle = (Button) findViewById(R.id.two_buttons_holo_light);
		twoButtonsHoloLightTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_YES_NO_HOLO_LIGHT_MESSAGE);
			}
		});

		mProgressHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (mProgress >= MAX_PROGRESS) {
					mProgressDialog.dismiss();
				} else {
					mProgress++;
					mProgressDialog.incrementProgressBy(1);
					mProgressHandler.sendEmptyMessageDelayed(0, 100);
				}
			}
		};
	}
}
