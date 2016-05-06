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

package com.example.android.apis.text;

import com.example.android.apis.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.widget.TextView;
/**
 * TextView实现文字链接跳转功能
 * @description：
 * @author ldm
 * @date 2016-4-21 下午4:34:05
 */
public class TextViewLinkAct extends Activity {
	private TextView tv_3;
	private TextView tv_4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.link);

		setTextViewLink();
	}

	/**
	 * 通过不同方式实现TextView中文字点击链接跳转功能
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-21 下午4:24:13
	 */
	private void setTextViewLink() {
		// 以Html格式href链接方式实现跳转
		tv_3 = (TextView) findViewById(R.id.text3);
		tv_3.setText(Html
				.fromHtml("<b>text3: Constructed from HTML programmatically.</b>  Text with a "
						+ "<a href=\"http://www.google.com\">link</a> "
						+ "created in the Java source code using HTML."));
		tv_3.setMovementMethod(LinkMovementMethod.getInstance());
		// 通过SpannableString的setMovementMethod方法实现链接效果
		SpannableString ss = new SpannableString(
				"text4: Manually created spans. Click here to dial the phone.");

		ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 30,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new URLSpan("tel:4155551212"), 31 + 6, 31 + 10,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_4 = (TextView) findViewById(R.id.text4);
		tv_4.setText(ss);
		tv_4.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
