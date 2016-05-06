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

package com.example.android.apis.media;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.apis.R;

public class AudioFxDemo extends Activity {
	private static final String TAG = "AudioFxDemo";

	private static final float VISUALIZER_HEIGHT_DIP = 50f;
	// 定义播放器
	private MediaPlayer mMediaPlayer;
	// 定义系统的频谱
	private Visualizer mVisualizer;
	// 定义系统的均衡器
	private Equalizer mEqualizer;

	private LinearLayout mLinearLayout;
	private VisualizerView mVisualizerView;
	private TextView mStatusTextView;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// 音量控制
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mStatusTextView = new TextView(this);
		mLinearLayout = new LinearLayout(this);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.addView(mStatusTextView);
		setContentView(mLinearLayout);

		// 创建MediaPlayer对象
		mMediaPlayer = MediaPlayer.create(this, R.raw.test_cbr);
		Log.d(TAG,
				"MediaPlayer audio session ID: "
						+ mMediaPlayer.getAudioSessionId());
		// 设置频谱显示
		setupVisualizerFxAndUI();
		// 设置示波器显示
		setupEqualizerFxAndUI();

		// Make sure the visualizer is enabled only when you actually want to
		// receive data, and
		// when it makes sense to receive data.
		mVisualizer.setEnabled(true);

		// When the stream ends, we don't need to collect any more data. We
		// don't do this in
		// setupVisualizerFxAndUI because we likely want to have more,
		// non-Visualizer related code
		// in this callback.
		mMediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer mediaPlayer) {
						mVisualizer.setEnabled(false);
						mStatusTextView.setText("播放结束");
					}
				});

		mMediaPlayer.start();
		mStatusTextView.setText("正在播放中");
	}

	private void setupEqualizerFxAndUI() {
		// Create the Equalizer object (an AudioEffect subclass) and attach it
		// to our media player,
		// with a default priority (0).
		mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
		mEqualizer.setEnabled(true);

		TextView eqTextView = new TextView(this);
		eqTextView.setText("Equalizer:");
		mLinearLayout.addView(eqTextView);

		short bands = mEqualizer.getNumberOfBands();

		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

		for (short i = 0; i < bands; i++) {
			final short band = i;

			TextView freqTextView = new TextView(this);
			freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000)
					+ " Hz");
			mLinearLayout.addView(freqTextView);

			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.HORIZONTAL);

			TextView minDbTextView = new TextView(this);
			minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			minDbTextView.setText((minEQLevel / 100) + " dB");

			TextView maxDbTextView = new TextView(this);
			maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			maxDbTextView.setText((maxEQLevel / 100) + " dB");

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1;
			SeekBar bar = new SeekBar(this);
			bar.setLayoutParams(layoutParams);
			bar.setMax(maxEQLevel - minEQLevel);
			bar.setProgress(mEqualizer.getBandLevel(band));

			bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					mEqualizer.setBandLevel(band,
							(short) (progress + minEQLevel));
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
				}
			});

			row.addView(minDbTextView);
			row.addView(bar);
			row.addView(maxDbTextView);

			mLinearLayout.addView(row);
		}
	}

	private void setupVisualizerFxAndUI() {
		// Create a VisualizerView (defined below), which will render the
		// simplified audio
		// wave form to a Canvas.
		mVisualizerView = new VisualizerView(this);
		mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(int) (VISUALIZER_HEIGHT_DIP * getResources()
						.getDisplayMetrics().density)));
		mLinearLayout.addView(mVisualizerView);

		// Create the Visualizer object and attach it to our media player.
		mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(
				new Visualizer.OnDataCaptureListener() {
					public void onWaveFormDataCapture(Visualizer visualizer,
							byte[] bytes, int samplingRate) {
						mVisualizerView.updateVisualizer(bytes);
					}

					public void onFftDataCapture(Visualizer visualizer,
							byte[] bytes, int samplingRate) {
					}
				}, Visualizer.getMaxCaptureRate() / 2, true, false);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (isFinishing() && mMediaPlayer != null) {
			mVisualizer.release();
			mEqualizer.release();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}

/**
 * 绘制波状View
 * 
 * @description：
 * @author ldm
 * @date 2016-4-20 上午9:11:49
 */
class VisualizerView extends View {
	// 数组保存了波形抽样点的值
	private byte[] bytes;
	private float[] points;
	// 定义画笔
	private Paint paint = new Paint();
	// 矩形区域
	private Rect rect = new Rect();
	private byte type = 0;

	public VisualizerView(Context context) {
		super(context);
		bytes = null;
		// 设置画笔的属性
		paint.setStrokeWidth(1f);// 设置空心线宽
		paint.setAntiAlias(true);// 抗锯齿
		paint.setColor(Color.BLUE);// 画笔颜色
		paint.setStyle(Style.STROKE);// 非填充模式
	}

	public void updateVisualizer(byte[] ftt) {
		bytes = ftt;
		// 通知组件重绘
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		// 当用户触碰该组件时，切换波形类型
		if (me.getAction() != MotionEvent.ACTION_DOWN) {
			return false;
		}
		type++;
		if (type >= 3) {
			type = 0;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bytes == null) {
			return;
		}
		// 绘制黑色背景
		canvas.drawColor(Color.BLACK);
		// 使用rect对象记录该组件的宽度和高度
		rect.set(0, 0, getWidth(), getHeight());
		switch (type) {
		// 绘制块状的波形图
		case 0:
			for (int i = 0; i < bytes.length - 1; i++) {
				float left = getWidth() * i / (bytes.length - 1);
				// 根据波形值计算该矩形的高度
				float top = rect.height() - (byte) (bytes[i + 1] + 128)
						* rect.height() / 128;
				float right = left + 1;
				float bottom = rect.height();
				canvas.drawRect(left, top, right, bottom, paint);
			}
			break;
		// 绘制柱状的波形图（每隔18个抽样点绘制一个矩形）
		case 1:
			for (int i = 0; i < bytes.length - 1; i += 18) {
				float left = rect.width() * i / (bytes.length - 1);
				// 根据波形值计算该矩形的高度
				float top = rect.height() - (byte) (bytes[i + 1] + 128)
						* rect.height() / 128;
				float right = left + 6;
				float bottom = rect.height();
				canvas.drawRect(left, top, right, bottom, paint);
			}
			break;
		// -绘制曲线波形图
		case 2:
			// 如果point数组还未初始化
			if (points == null || points.length < bytes.length * 4) {
				points = new float[bytes.length * 4];
			}
			for (int i = 0; i < bytes.length - 1; i++) {
				// 计算第i个点的x坐标
				points[i * 4] = rect.width() * i / (bytes.length - 1);
				// 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
				points[i * 4 + 1] = (rect.height() / 2)
						+ ((byte) (bytes[i] + 128)) * 128 / (rect.height() / 2);
				// 计算第i+1个点的x坐标
				points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
				// 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
				points[i * 4 + 3] = (rect.height() / 2)
						+ ((byte) (bytes[i + 1] + 128)) * 128
						/ (rect.height() / 2);
			}
			// 绘制波形曲线
			canvas.drawLines(points, paint);
			break;
		}
	}
}
