/**
 * Copyright (c) 2010, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.content;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * Android系统剪贴板功能DEMO
 * 
 * @description：
 * @author ldm
 * @date 2016-5-16 下午3:15:47
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ClipboardSample extends Activity {
	// 系统剪贴板管理器
	ClipboardManager mClipboard;
	// 下拉列表
	Spinner mSpinner;
	TextView mMimeTypes;
	TextView mDataText;
	CharSequence mStyledText;
	String mPlainText;
	String mHtmlText;
	String mHtmlPlainText;
	TextView tv;
	// 剪切板内容变化的监听器
	ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener = new ClipboardManager.OnPrimaryClipChangedListener() {
		public void onPrimaryClipChanged() {
			updateClipData(true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clipboard);
		// 系统剪贴板管理器
		mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		mStyledText = getText(R.string.styled_text);
		// 初始化界面UI
		initViews();
		// 设置数据及控件监听
		initDataAndEvents();
	}

	private void initDataAndEvents() {
		// <string-array name="clip_data_types">
		// <item>No data in clipboard</item>
		// <item>Text clip</item>
		// <item>HTML Text clip</item>
		// <item>Intent clip</item>
		// <item>Uri clip</item>
		// <item>Coerce to text</item>
		// <item>Coerce to styled text</item>
		// <item>Coerce to HTML text</item>
		// </string-array>
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.clip_data_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		// Spinner下拉框Item点击事件处理
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				updateClipData(false);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		mClipboard.addPrimaryClipChangedListener(mPrimaryChangeListener);
		updateClipData(true);
	}

	private void initViews() {
		tv = (TextView) findViewById(R.id.styled_text);
		tv.setText(mStyledText);

		mPlainText = mStyledText.toString();
		tv = (TextView) findViewById(R.id.plain_text);
		tv.setText(mPlainText);

		mHtmlText = "<b>Link:</b> <a href=\"http://www.android.com\">Android</a>";
		mHtmlPlainText = "Link: http://www.android.com";
		tv = (TextView) findViewById(R.id.html_text);
		tv.setText(mHtmlText);

		mSpinner = (Spinner) findViewById(R.id.clip_type);
		mMimeTypes = (TextView) findViewById(R.id.clip_mime_types);
		mDataText = (TextView) findViewById(R.id.clip_text);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mClipboard.removePrimaryClipChangedListener(mPrimaryChangeListener);
	}

	public void pasteStyledText(View button) {
		mClipboard.setPrimaryClip(ClipData.newPlainText("Styled Text",
				mStyledText));
	}

	public void pastePlainText(View button) {
		mClipboard.setPrimaryClip(ClipData.newPlainText("Styled Text",
				mPlainText));
	}

	public void pasteHtmlText(View button) {
		mClipboard.setPrimaryClip(ClipData.newHtmlText("HTML Text",
				mHtmlPlainText, mHtmlText));
	}

	public void pasteIntent(View button) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.android.com/"));
		mClipboard.setPrimaryClip(ClipData.newIntent("VIEW intent", intent));
	}

	public void pasteUri(View button) {
		mClipboard.setPrimaryClip(ClipData.newRawUri("URI",
				Uri.parse("http://www.android.com/")));
	}

	/**
	 * 更新剪贴板操作
	 * */
	void updateClipData(boolean updateType) {
		// 获取剪切板的ClipData数据对象
		ClipData clip = mClipboard.getPrimaryClip();
		// 获取到数据的类型
		String[] mimeTypes = clip != null ? clip.getDescription()
				.filterMimeTypes("*/*") : null;
		if (mimeTypes != null) {
			mMimeTypes.setText("");
			for (int i = 0; i < mimeTypes.length; i++) {
				if (i > 0) {
					mMimeTypes.append("\n");
				}
				mMimeTypes.append(mimeTypes[i]);
			}
		} else {
			mMimeTypes.setText("NULL");
		}
		// 根据类型来确定Spinner显示的文字
		if (updateType) {
			if (clip != null) {
				ClipData.Item item = clip.getItemAt(0);
				if (item.getHtmlText() != null) {
					mSpinner.setSelection(2);
				} else if (item.getText() != null) {
					mSpinner.setSelection(1);
				} else if (item.getIntent() != null) {
					mSpinner.setSelection(3);
				} else if (item.getUri() != null) {
					mSpinner.setSelection(4);
				} else {
					mSpinner.setSelection(0);
				}
			} else {
				mSpinner.setSelection(0);
			}
		}

		if (clip != null) {
			// 获取剪贴板中的数据
			ClipData.Item item = clip.getItemAt(0);
			switch (mSpinner.getSelectedItemPosition()) {
			case 0:
				mDataText.setText("(No data)");
				break;
			case 1:
				mDataText.setText(item.getText());
				break;
			case 2:
				mDataText.setText(item.getHtmlText());
				break;
			case 3:
				// 因没有intent类型数据，会报null
				mDataText.setText(item.getIntent().toUri(0));
				break;
			case 4:
				// 因没有Uri类型数据，会报null
				mDataText.setText(item.getUri().toString());
				break;
			case 5:
				// 剪贴板中的非text数据可以通过ClipData.Item.coerceToText()方法转换为text处理。
				mDataText.setText(item.coerceToText(this));
				break;
			case 6:
				mDataText.setText(item.coerceToStyledText(this));
				break;
			case 7:
				// 转换为Hteml格式
				mDataText.setText(item.coerceToHtmlText(this));
				break;
			default:
				mDataText.setText("Unknown option: "
						+ mSpinner.getSelectedItemPosition());
				break;
			}
		} else {
			mDataText.setText("(NULL clip)");
		}
		// 设置文字监听
		mDataText.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
