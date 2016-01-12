package com.example.readanytext.utils;

import java.io.File;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.example.readanytext.model.FilenameLab;

public class FileManagerUtils {
	public static void filesort(List<File> files, final int sortStyle) {
		Collections.sort(files, new Comparator<File>() {

			@Override
			public int compare(File first, File second) {
				if (first.isDirectory() && second.isFile()) {
					return -1;
				} else if (first.isFile() && second.isDirectory()) {
					return 1;
				}

				switch (sortStyle) {
				case FilenameLab.SORT_BY_NAME:
					Collator coll = Collator.getInstance(Locale.CHINA);
					// 根据文件名排序
					//return (first.toString().compareTo(second.toString()));
					return coll.compare(first.getName(), second.getName());
				case FilenameLab.SORT_BY_TIME:
					// 根据时间排序
					return first.lastModified() > second.lastModified() ? 1
							: -1;
				case FilenameLab.SORT_BY_SIZE:
					// 根据大小排序
					return first.getTotalSpace() > second.getTotalSpace() ? 1
							: 0;
				default:
					return 0;
				}

			}
		});

	}
}
