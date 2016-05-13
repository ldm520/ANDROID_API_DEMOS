/*
 * Copyright (C) 2012 The Android Open Source Project
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

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * Fragment嵌套实现选项菜单
 * 
 * @description：
 * @author ldm
 * @date 2016-5-13 上午10:21:39
 */
public class FragmentMenuFragment extends Fragment {
	Fragment mFragment1;
	Fragment mFragment2;
	CheckBox mCheckBox1;
	CheckBox mCheckBox2;

	final OnClickListener mClickListener = new OnClickListener() {
		public void onClick(View v) {
			updateFragmentVisibility();
		}
	};

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_menu, container, false);

		// 嵌套Fragment要使用getChildFragmentManager
		FragmentManager fm = getChildFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		mFragment1 = fm.findFragmentByTag("f1");
		if (mFragment1 == null) {
			mFragment1 = new FragmentMenu.MenuFragment();
			ft.add(mFragment1, "f1");
		}
		mFragment2 = fm.findFragmentByTag("f2");
		if (mFragment2 == null) {
			mFragment2 = new FragmentMenu.Menu2Fragment();
			ft.add(mFragment2, "f2");
		}
		ft.commit();

		mCheckBox1 = (CheckBox) v.findViewById(R.id.menu1);
		mCheckBox1.setOnClickListener(mClickListener);
		mCheckBox2 = (CheckBox) v.findViewById(R.id.menu2);
		mCheckBox2.setOnClickListener(mClickListener);
		updateFragmentVisibility();

		return v;
	}

	@SuppressLint("NewApi")
	//告知fragment所有的view布局状态已经被存储
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		updateFragmentVisibility();
	}

	// Update fragment visibility based on current check box state.
	@SuppressLint("NewApi")
	void updateFragmentVisibility() {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		if (mCheckBox1.isChecked())
			ft.show(mFragment1);
		else
			ft.hide(mFragment1);
		if (mCheckBox2.isChecked())
			ft.show(mFragment2);
		else
			ft.hide(mFragment2);
		ft.commit();
	}
}
