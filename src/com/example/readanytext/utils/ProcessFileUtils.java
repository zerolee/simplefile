package com.example.readanytext.utils;

import java.io.File;
import java.util.regex.Pattern;

public class ProcessFileUtils {

	public static String getProcessStringFromFile(File file) {
		//
		String suffix, filename[] = file.getName().split("\\.");
		if (filename.length == 2){
			suffix = filename[1];
		} else {
			suffix = "txt";
		}
		switch (suffix) {
		case "java":
			return formatBySuffix(file);
		default:
			String content = FileUtils.getStringFromFile(file);
			content = content.replaceAll("\n", "<br/>");
			content = content.replaceAll(" ", "&nbsp;");
			content = content.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
			content = content.replaceAll("\r", "<br/>");
			return content;
		}
	}

	public static String getProcessStringFromFile(String file) {
		return getProcessStringFromFile(new File(file));
	}

	private static String formatBySuffix(File file) {
		String[] keywords = { "public", "protected", "private", "static",
				"final", "extends", "implements", "super", "this", "null",
				"true", "false", "return", "new", "void", "int", "long",
				"double", "byte", "boolean", "import", "package", "try",
				"catch", "finally", "if", "for", "while", "else", "switch",
				"case", "break", "continue", "class" };
		// 获取文件内容
		String content = FileUtils.getStringFromFile(file);
		// 将文件内容以换行符进行分割，并保存在字符串数组中
		content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		String[] contents = content.split("\\n");

		StringBuilder builder = new StringBuilder();
		Pattern p1 = Pattern.compile("^(&nbsp;)*//");
		Pattern p2 = Pattern.compile("^(&nbsp;)*/\\*");
		Pattern p3 = Pattern.compile("\\*/");
		boolean flag = false;
		for (String c : contents) {
			if (!p1.matcher(c.trim()).find()) {
				if (!flag) {
					if (p2.matcher(c.trim()).find()) {
						c = "<font color='#006030'>" + c + "</font>";
						flag = true;
					}
				} else {
					c = "<font color='#006030'>" + c + "</font>";
					if (p3.matcher(c).find()) {
						flag = false;
					}
				}

				if (!flag) {
					for (String k : keywords) {
						c = c.replaceAll("\\b" + k + "\\b",
								"<font color='#930000'>" + k + "</font>");
					}
				}
			} else {
				c = "<font color='#006030'>" + c + "</font>";
			}

			builder.append(c + "<br/>");
		}
		return builder.toString();
	}
}
