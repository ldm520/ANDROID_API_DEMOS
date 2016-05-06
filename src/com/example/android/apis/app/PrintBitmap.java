package com.example.android.apis.app;

/*
 * Copyright (C) 2013 The Android Open Source Project
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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.android.apis.R;

/**
 * This class demonstrates how to implement bitmap printing.
 * <p>
 * This activity shows an image and offers a print option in the overflow menu.
 * When the user chooses to print a helper class from the support library is
 * used to print the image.
 * </p>
 * 
 * @see PrintManager
 * @see WebView
 */
public class PrintBitmap extends Activity {

	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.print_bitmap);
		mImageView = (ImageView) findViewById(R.id.image);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.print_custom_content, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_print) {
			print();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void print() {
		// bitmap打印队列工具类。
		PrintHelper printHelper = new PrintHelper(this);

		/**
		 * PrintHelper通过setScaleMode()方法设置模式，现在有两种模式：
		 * SCALE_MODE_FIT：这个打印完整的图片，这样打印纸的边缘可能有空白
		 * SCALE_MODE_FILL：这个填满所有的打印纸，因此图片的边缘可能打印不出来
		 */
		printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
		// 获取ImageView这个用于显示图片的控件里的图片
		Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
		// 打印图片
		printHelper.printBitmap("Print Bitmap", bitmap);
	}
}
