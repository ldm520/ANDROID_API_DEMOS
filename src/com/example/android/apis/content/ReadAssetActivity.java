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

package com.example.android.apis.content;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.apis.R;

/**
 * Android中assets目录下文件数据操作 assets文件有大小限制限制:最大(UNCOMPRESS_DATA_MAX):
 * 1048567字节,接近1M大小(图片，Mp3等格式文件例外)
 * 
 * @description：
 * @author ldm
 * @date 2016-4-27 上午8:57:15
 */
public class ReadAssetActivity extends Activity {
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_asset);
		initViews();
		readAssetsData();
		listAssetsFiles("fonts");// 列出文件目录
	}

	private void readAssetsData() {
		InputStream is = null;
		try {
			// 读取Assets文件夹下对应文件的输入流
			is = getAssets().open("asset_test.txt");
			// 获取文件输入流的总大小
			int size = is.available();
			// 把整个文件流放到一个Byte[]缓冲区
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			// 将缓冲区数据转换为字符串
			String text = new String(buffer);
			mTextView.setText(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void initViews() {
		mTextView = (TextView) findViewById(R.id.text);
	}

	/**
	 * 获取Assets下指定文件夹下文件数量 情况
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-27 上午9:25:54
	 */
	private void listAssetsFiles(String filePath) {
		AssetManager am = getAssets();
		String[] fileName;
		try {
			fileName = am.list(filePath);
			if (fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					Log.e("ldm", String.format("在" + filePath
							+ "中文件路下有:[%d] 文件数量", fileName.length));

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
