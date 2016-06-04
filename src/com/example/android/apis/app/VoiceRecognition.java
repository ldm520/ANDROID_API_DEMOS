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

package com.example.android.apis.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * Android语音又识别Sample
 * 
 * @description：
 * @author ldm
 * @date 2016-6-3 下午3:45:09
 */
public class VoiceRecognition extends Activity implements OnClickListener {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	private ListView mList;

	private Handler mHandler;

	private Spinner mSupportedLanguageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		setContentView(R.layout.voice_recognition);
		Button speakButton = (Button) findViewById(R.id.btn_speak);
		mList = (ListView) findViewById(R.id.list);
		mSupportedLanguageView = (Spinner) findViewById(R.id.supported_languages);

		// 获取包管理操作类PackageManager实例
		PackageManager pm = getPackageManager();
		// 检查是否存在语音识别功能的activity
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {
			// 如果存在recognition activity则为按钮绑定点击事件
			speakButton.setOnClickListener(this);
		} else {
			// 如果不存在则禁用按钮并做提示
			speakButton.setEnabled(false);
			speakButton.setText("Recognizer not present");
		}

		// Most of the applications do not have to handle the voice settings. If
		// the application
		// does not require a recognition in a specific language (i.e.,
		// different from the system
		// locale), the application does not need to read the voice settings.
		// refreshVoiceSettings();
	}

	/**
	 * Handle the click on the start recognition button.
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.btn_speak) {
			startVoiceRecognitionActivity();
		}
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		// 通过Intent来传递一个动作以及一些属性，然后通过startActivityForResult来开始语音
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		// 传递应用名包名
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		// 传递提示信息
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Speech recognition demo");

		// 传递语音模式
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

		// 最大数量
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

		// parameter.
		if (!mSupportedLanguageView.getSelectedItem().toString()
				.equals("Default")) {
			// 传递语言
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
					mSupportedLanguageView.getSelectedItem().toString());
		}

		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	/**
	 * 处理数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			mList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, matches));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void refreshVoiceSettings() {
		sendOrderedBroadcast(RecognizerIntent.getVoiceDetailsIntent(this),
				null, new SupportedLanguageBroadcastReceiver(), null,
				Activity.RESULT_OK, null, null);
	}

	/**
	 * 更新语音支持数据
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-6-3 下午3:59:00
	 */
	private void updateSupportedLanguages(List<String> languages) {
		languages.add(0, "Default");
		SpinnerAdapter adapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item,
				languages.toArray(new String[languages.size()]));
		mSupportedLanguageView.setAdapter(adapter);
	}

	private void updateLanguagePreference(String language) {
		TextView textView = (TextView) findViewById(R.id.language_preference);
		textView.setText(language);
	}

	/**
	 * 处理有关语音识别所述广播请求的响应支持的语言。
	 * 
	 * 只有当应用程序需要识别特定的语言，广播才会调用
	 */
	private class SupportedLanguageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, final Intent intent) {
			// 获取到数据
			final Bundle extra = getResultExtras(false);

			if (getResultCode() != Activity.RESULT_OK) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						showToast("Error code:" + getResultCode());
					}
				});
			}

			if (extra == null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						showToast("No extra");
					}
				});
			}

			if (extra.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// 更新数据
						updateSupportedLanguages(extra
								.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES));
					}
				});
			}

			if (extra.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// 更新数据
						updateLanguagePreference(extra
								.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE));
					}
				});
			}
		}

		private void showToast(String text) {
			Toast.makeText(VoiceRecognition.this, text, 1000).show();
		}
	}
}
