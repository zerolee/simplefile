package com.example.readanytext.control.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.example.readanytext.R;
import com.example.readanytext.control.textreader.CodeViewActivity;
import com.example.readanytext.model.FilenameLab;
import com.example.readanytext.utils.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileManagerFragment extends Fragment implements IonBackPressed{
	public static final String EXTRA_FILE = "com.example.readanytext.FilenameLab.file";
	public static final String EXTRA_FILENAME = "com.example.readanytext.FilenameLab.filename";
	private static final String TAG = "FileManagerFragment";
	private static final String DIALOG_SORT = "sort";
	private static final String DIALOG_NEWDIRECTORY = "newdirectory";
	private static final String DIALOG_NEWFILE = "newfile";
	
	private static final int COPY_FILE = 0;
	private static final int CUT_FILE = 1;
	
	private static final int REQUEST_SORT = 0;
	private static final int REQUEST_FILENAME = 1;
	private static final int REQUEST_FILEDIRECTORYNAME = 2;
	private static final int REQUEST_RENAMEFILE = 3;

	private ListView file_list;
	private TextView index;
	private Button paste;

	private SortFileFragment dialog;

	private int sortStyle;
	private int pastefile;
	private FilenameLab sFilenameLab;
	private FileAdapter adapter;
	private ArrayList<File> files;
	private ArrayList<File> selectFiles = new ArrayList<File>();
	

	private class FileAdapter extends ArrayAdapter<File> {
		public FileAdapter(ArrayList<File> filenames) {
			super(getActivity(), 0, filenames);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_file, null);
			}
			File f = getItem(position);
			TextView filename = (TextView) convertView
					.findViewById(R.id.listitem_file_name);
			String[] array = f.toString().split("\\/");
			String text = null;
			for (String n : array) {
				text = n;
			}
			filename.setText(text);

			TextView filenamedate = (TextView) convertView
					.findViewById(R.id.listitem_file_date);

			filenamedate.setText(android.text.format.DateFormat.format(
					"yyyy/MM/dd kk:mm", new Date(f.lastModified())));

			TextView isDirectory = (TextView) convertView
					.findViewById(R.id.listitem_file_isdirectory);
			if (!f.isDirectory()) {
				isDirectory.setVisibility(View.INVISIBLE);
			} else {
				isDirectory.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_filesystem, container,
				false);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		index = (TextView) v.findViewById(R.id.filesystem_index_name);
		updateIndex(sFilenameLab.getCurrentfile());
		
		paste = (Button) v.findViewById(R.id.filesystem_index_paste);
		paste.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pastefile == CUT_FILE) 
					pasteCutFile();
				else
					pasteCopyFile();
				paste.setVisibility(View.GONE);
				refresh();
			}
		});
		
		
		file_list = (ListView) v.findViewById(R.id.filesystem_path);
		adapter = new FileAdapter(files);
		file_list.setAdapter(adapter);
		file_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				File file = files.get(position);
				if (file.isDirectory()) {
					refresh(file);
				} else {
					openFile(file);
				}
			}
		});
		// 浮动菜单
		//registerForContextMenu(file_list);
		//多选操作
		file_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		file_list.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.fragment_filesystem_context, menu);
				return true;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.fragment_filesystem_context_delete:
					
					for (int i = adapter.getCount() - 1; i >= 0; i--) {
						if (file_list.isItemChecked(i)) {
							FileUtils.deleteFile(adapter.getItem(i));
						}
					}
					mode.finish();
					refresh();
					return true;
				case R.id.fragment_filesystem_context_rename:
					File file = null;
					if (file_list.getCheckedItemCount() != 1) {
						Toast.makeText(getActivity(), "每次只能对一个文件重命名", Toast.LENGTH_SHORT).show();
						mode.finish();
						return true;
					}
					
					for (int i = adapter.getCount() - 1; i >= 0; i--) {
						if (file_list.isItemChecked(i)) {
							file = adapter.getItem(i);
						}
					}
					sFilenameLab.setSelectFile(file);
					FragmentManager fm_rename = getActivity().getSupportFragmentManager();
					NewFileFragment fragment = NewFileFragment.newInstance(file.isFile(), file.getName());
					fragment.setTargetFragment(FileManagerFragment.this, REQUEST_RENAMEFILE);
					fragment.show(fm_rename, "renameFile");
					mode.finish();
					return true;
				case R.id.fragment_filesystem_context_cut:
					for (int i = adapter.getCount() - 1; i >= 0; i--) {
						if (file_list.isItemChecked(i)) {
							selectFiles.add(adapter.getItem(i));
						}
					}
					mode.finish();
					paste.setVisibility(View.VISIBLE);
					pastefile = CUT_FILE;
					return true;
				case R.id.fragment_filesystem_context_copy:
					for (int i = adapter.getCount() - 1; i >= 0; i--) {
						if (file_list.isItemChecked(i)) {
							selectFiles.add(adapter.getItem(i));
						}
					}
					mode.finish();
					paste.setVisibility(View.VISIBLE);
					pastefile = COPY_FILE;
					return true;
				default:
					return false;
				}
			}
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				// TODO Auto-generated method stub
				
			}
		});
		return v;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_item_hideFile:
			if (sFilenameLab.isShowHidden()) {
				item.setTitle("显示隐藏文件");
			} else {
				item.setTitle("不显示隐藏文件");
			}
			sFilenameLab.setHidden();
			sFilenameLab.saveSetting();
			refresh();
			return true;
		case R.id.menu_item_refreshFile:
			refresh();
			return true;
		case R.id.menu_item_sortFile:
			FragmentManager fm1 = getActivity().getSupportFragmentManager();
			dialog = SortFileFragment.newInstance(sortStyle);
			dialog.setTargetFragment(FileManagerFragment.this, REQUEST_SORT);
			dialog.show(fm1, DIALOG_SORT);
			return true;
		case R.id.menu_item_newDirectory:
			FragmentManager fm2 = getActivity().getSupportFragmentManager();
			NewFileFragment dialog_newDirectory = NewFileFragment
					.newInstance(false, null);
			dialog_newDirectory.setTargetFragment(FileManagerFragment.this,
					REQUEST_FILEDIRECTORYNAME);
			dialog_newDirectory.show(fm2, DIALOG_NEWDIRECTORY);
			return true;
		case R.id.menu_item_newFile:
			FragmentManager fm3 = getActivity().getSupportFragmentManager();
			NewFileFragment dialog_newFile = NewFileFragment.newInstance(true, null);
			dialog_newFile.setTargetFragment(FileManagerFragment.this,
					REQUEST_FILENAME);
			dialog_newFile.show(fm3, DIALOG_NEWFILE);
			return true;
		case R.id.menu_item_searchFile:
			return true;
		case R.id.menu_item_recentFile:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		sFilenameLab = FilenameLab.get(getActivity());
		files = sFilenameLab.updateFiles(
				android.os.Environment.getExternalStorageDirectory())
				.getFiles();
		sortStyle = sFilenameLab.getSortStyle();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_filesystem, menu);
		MenuItem item = menu.findItem(R.id.menu_item_hideFile);
		/*if (sFilenameLab.isShowHidden()) {
			item.setTitle("不显示隐藏文件");
		} else {
			item.setTitle("显示隐藏文件");
		}*/
	
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.fragment_filesystem_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		File file = adapter.getItem(info.position);
		switch (item.getItemId()) {
		case R.id.fragment_filesystem_context_delete:
			FileUtils.deleteFile(file);
			refresh();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_SORT) {
			sortStyle = data.getIntExtra(SortFileFragment.EXTRA_SORT_STYLE,
					FilenameLab.SORT_BY_NAME);
			sFilenameLab.setSortStyle(sortStyle);
			dialog.dismiss();
			sFilenameLab.saveSetting();
		}

		if (requestCode == REQUEST_FILEDIRECTORYNAME) {
			FileUtils.createDirectory(sFilenameLab.getCurrentfile().toString()
					+ "/" + data.getStringExtra(EXTRA_FILENAME));
		}

		if (requestCode == REQUEST_FILENAME) {
			FileUtils.createFile(sFilenameLab.getCurrentfile().toString()
					+ "/" + data.getStringExtra(EXTRA_FILENAME));
		}

		if (requestCode == REQUEST_RENAMEFILE) {
			FileUtils.renameFile(sFilenameLab.getSelectFile(), new File(sFilenameLab.getCurrentfile().getPath()
					+ "/" + data.getStringExtra(EXTRA_FILENAME)));
			
		}
		refresh();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		sFilenameLab.saveSetting();
	}

	
	@Override
	public boolean onBackPressed() {
		File currentFile = sFilenameLab.getCurrentfile();
		if ("/".equals(currentFile.toString()))
			return false;
		sFilenameLab.updateFiles(currentFile.getParentFile());
		adapter.notifyDataSetChanged();
		updateIndex(currentFile.getParentFile());
		return true;
	}
	// 更新索引
	private void updateIndex(File file) {
		index.setText(file.toString());
	}
	// 打开一个文件
	private void openFile(File file) {
		Intent i = new Intent(getActivity(), CodeViewActivity.class);
		i.putExtra(EXTRA_FILE, file.toString());
		startActivity(i);
	}

	private void refresh() {
		refresh(sFilenameLab.getCurrentfile());
	}
	
	private void refresh(File file) {
		sFilenameLab.updateFiles(file);
		adapter.notifyDataSetChanged();
		updateIndex(file);
	}
	
	private void pasteCutFile(){
		for (File file : selectFiles) {
			FileUtils.renameFile(file, 
					new File(sFilenameLab.getCurrentfile().getPath() + "/" + file.getName()));
		}
		selectFiles.clear();
	}
	
	private void pasteCopyFile(){
		for (File file : selectFiles) {
			try {
			FileUtils.copyFile(file, 
					new File(sFilenameLab.getCurrentfile().getPath() + "/" + file.getName()));
			} catch (IOException e) {
				Toast.makeText(getActivity(), "复制文件失败", Toast.LENGTH_SHORT).show();
			}
		}
		selectFiles.clear();
	}


	
}
