/*
 * Copyright (C) 2011 The Android Open Source Project
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.apis.R;

/**
 * 安卓应用程序 APK安装，替换及卸载
 * 
 * @description：
 * @author ldm
 * @date 2016-4-27 下午4:53:01
 */
public class ApkOperatorActivity extends Activity {
	// 安装
	private static final int INSTALL_REQUEST = 1;
	// 卸载
	private static final int UNINSTALL_REQUEST = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operator_apk_layout);
		initButtonsAndListeners();
	}

	/**
	 * 初始化Button及监听事件
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-27 下午4:29:12
	 */
	private void initButtonsAndListeners() {
		Button button = (Button) findViewById(R.id.unknown_source);
		button.setOnClickListener(installApkListener);
		button = (Button) findViewById(R.id.my_source);
		button.setOnClickListener(installResultListener);
		button = (Button) findViewById(R.id.replace);
		button.setOnClickListener(replaceApkListener);
		button = (Button) findViewById(R.id.uninstall);
		button.setOnClickListener(uninstallApkListener);
		button = (Button) findViewById(R.id.uninstall_result);
		button.setOnClickListener(uninstallResultListener);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == INSTALL_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				showToastMsg("安装成功!");
			} else if (resultCode == Activity.RESULT_CANCELED) {
				showToastMsg("取消安装！");
			} else {
				showToastMsg("安装失败！");
			}
		} else if (requestCode == UNINSTALL_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				showToastMsg("卸载成功！");
			} else if (resultCode == Activity.RESULT_CANCELED) {
				showToastMsg("取消卸载！");
			} else {
				showToastMsg("卸载失败！");
			}
		}
	}

	private OnClickListener installApkListener = new OnClickListener() {
		public void onClick(View v) {
			// 安装APK
			Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
			intent.setData(Uri.fromFile(checkApkFile("HelloActivity.apk")));
			startActivity(intent);
		}
	};

	private OnClickListener installResultListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
			intent.setData(Uri.fromFile(checkApkFile("HelloActivity.apk")));
			// 未知安装源
			intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
			// 返回结果
			intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
			// 安装包的名称
			intent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME,
					getApplicationInfo().packageName);
			startActivityForResult(intent, INSTALL_REQUEST);
		}
	};

	private OnClickListener replaceApkListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
			intent.setData(Uri.fromFile(checkApkFile("HelloActivity.apk")));
			intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
			intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
			// 替换安装包
			intent.putExtra(Intent.EXTRA_ALLOW_REPLACE, true);
			intent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME,
					getApplicationInfo().packageName);
			startActivityForResult(intent, INSTALL_REQUEST);
		}
	};

	private OnClickListener uninstallApkListener = new OnClickListener() {
		public void onClick(View v) {
			// 卸载
			Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
			intent.setData(Uri.parse("package:com.ldm.study.SmsActivity"));
			startActivity(intent);
		}
	};

	private OnClickListener uninstallResultListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
			intent.setData(Uri
					.parse("package:com.example.android.helloactivity"));
			// 设置卸载结果
			intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
			startActivityForResult(intent, UNINSTALL_REQUEST);
		}
	};

	private File checkApkFile(String assetName) {
		// 把测试的apk文件放到项目的Assets文件夹下面
		byte[] buffer = new byte[8192];
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = getAssets().open(assetName);
			fos = openFileOutput("HelloActivity.apk",
					Context.MODE_WORLD_READABLE);
			int n;
			while ((n = is.read(buffer)) >= 0) {
				fos.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}

		return getFileStreamPath("HelloActivity.apk");
	}

	private void showToastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
