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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 使用DialogFragment创建对话框
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午2:00:01
 */
public class FragmentAlertDialog extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_dialog);
		View tv = findViewById(R.id.text);
		((TextView) tv)
				.setText("Example of displaying an alert dialog with a DialogFragment");
		// 初始化Button及设置监听
		Button button = (Button) findViewById(R.id.show);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 弹出对话框
				showDialog();
			}
		});
	}

	void showDialog() {
		// DialogFragment 创建对话框
		DialogFragment newFragment = MyAlertDialogFragment
				.newInstance(R.string.alert_dialog_two_buttons_title);
		newFragment.show(getFragmentManager(), "dialog");
	}

	public void doPositiveClick() {
		Log.i("FragmentAlertDialog", "Positive click!");
	}

	public void doNegativeClick() {
		Log.i("FragmentAlertDialog", "Negative click!");
	}

	/**
	 * 自定义弹出对话框DialogFragmet
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-12 下午1:54:31
	 */
	public static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(int title) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("title", title);
			frag.setArguments(args);
			return frag;
		}

		/**
		 * DialogFragment需要实现onCreateView或者onCreateDIalog方法。
		 * onCreateView():使用定义的xml布局文件展示Dialog。
		 * onCreateDialog():利用AlertDialog或者Dialog创建出Dialog。
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int title = getArguments().getInt("title");
			return new AlertDialog.Builder(getActivity())// 创建一个Dialog
					.setIcon(R.drawable.alert_dialog_icon)// 设置图标
					.setTitle(title)// 设置标题
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {// 确认（OK）按钮
								public void onClick(DialogInterface dialog,
										int whichButton) {
									((FragmentAlertDialog) getActivity())
											.doPositiveClick();
								}
							}).setNegativeButton(R.string.alert_dialog_cancel,// 取消（Cancel）按钮
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									((FragmentAlertDialog) getActivity())
											.doNegativeClick();
								}
							}).create();
		}
	}

}
