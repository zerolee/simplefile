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
		if (fragment == null || !(fragment instanceof IonBackPressed)
				|| !((FileManagerFragment) fragment).onBackPressed()) {
			super.onBackPressed();
		}
	}

}
