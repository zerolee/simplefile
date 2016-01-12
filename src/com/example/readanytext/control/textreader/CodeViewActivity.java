package com.example.readanytext.control.textreader;

import com.example.readanytext.control.SingleFragmentActivity;
import com.example.readanytext.control.filemanager.FileManagerFragment;

import android.support.v4.app.Fragment;

public class CodeViewActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		String file = getIntent().getStringExtra(FileManagerFragment.EXTRA_FILE);
		return CodeViewFragment.newInstance(file);
	}

}
