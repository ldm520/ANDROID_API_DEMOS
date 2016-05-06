/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.example.android.apis.media;

import com.example.android.apis.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Android API MediaPlayer示例
 * 
 * @description：
 * @author ldm
 * @date 2016-4-20 上午10:42:06
 */
public class MediaPlayerActivity extends Activity implements OnClickListener {
	private Button mlocalvideo;
	private Button mstreamvideo;
	private Button mlocalaudio;
	private Button mresourcesaudio;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int RESOURCES_AUDIO = 3;
	private static final int STREAM_VIDEO = 5;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_mediaplayer);
		initViewsAndEvents();

	}

	private void initViewsAndEvents() {
		mlocalaudio = (Button) findViewById(R.id.localaudio);
		mresourcesaudio = (Button) findViewById(R.id.resourcesaudio);
		mlocalvideo = (Button) findViewById(R.id.localvideo);
		mstreamvideo = (Button) findViewById(R.id.streamvideo);
		mlocalaudio.setOnClickListener(this);
		mresourcesaudio.setOnClickListener(this);
		mlocalvideo.setOnClickListener(this);
		mstreamvideo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.localaudio:
		case R.id.localvideo:
			Intent intent = new Intent(MediaPlayerActivity.this.getApplication(),
					MediaPlayerDemo_Audio.class);
			intent.putExtra(MEDIA, LOCAL_AUDIO);
			startActivity(intent);
			break;
		case R.id.resourcesaudio:
			Intent resIn = new Intent(MediaPlayerActivity.this.getApplication(),
					MediaPlayerDemo_Audio.class);
			resIn.putExtra(MEDIA, RESOURCES_AUDIO);
			startActivity(resIn);
			break;
		case R.id.streamvideo:
			Intent streamIn = new Intent(MediaPlayerActivity.this,
					MediaPlayer_Video.class);
			streamIn.putExtra(MEDIA, STREAM_VIDEO);
			startActivity(streamIn);
			break;
		}
	}

}
