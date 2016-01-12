package com.example.readanytext.control.filemanager;

import com.example.readanytext.control.SingleFragmentActivity;

import android.support.v4.app.Fragment;

public class FileManagerActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new FileManagerFragment();
	}

	@Override
	public void onBackPressed() {
		if (fragment == null) {
			super.onBackPressed();
		} else {		
			if(!((FileManagerFragment)fragment).onBackPressed()) {
				super.onBackPressed();
			}
		}

	}
	
}
