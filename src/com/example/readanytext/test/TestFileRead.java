package com.example.readanytext.test;

import org.junit.Test;

import com.example.readanytext.utils.FileUtils;


public class TestFileRead {
	@Test
	public void test1(){
	System.out.println(System.getProperty("user.dir"));	
	System.out.println(FileUtils.getStringFromFile("src/com/example/readanytext/test/TestFileRead.java"));
	
	String result = "public static void main(String[] args)";
	System.out.println(result.replaceAll("\\bpublic\\b", "<font color=red>public</font>"));
	}
	
	
}
