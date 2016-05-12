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
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment嵌套
 * 
 * @description：
 * @author ldm
 * @date 2016-5-12 下午3:50:34
 */
@SuppressLint("NewApi")
public class FragmentArgumentsFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// Fragment里面嵌套Fragment 的话：一定要用getChildFragmentManager();
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();
			Fragment newFragment = FragmentArguments.MyFragment
					.newInstance("From Arguments 1");
			ft.add(R.id.created1, newFragment);
			newFragment = FragmentArguments.MyFragment
					.newInstance("From Arguments 2");
			ft.add(R.id.created2, newFragment);
			ft.commit();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_arguments_fragment,
				container, false);
		return v;
	}

}
