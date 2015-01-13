package com.code;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ScanJars {

	private static final String POM_VERSION = "version";
	private static final String POM_GROUPID = "groupId";
	private static final String POM_ARTIFACTID = "artifactId";

	public static List<String> getJarList(String jarPath) {
		List<String> pathList = null;
		if (jarPath == null || "".equals(jarPath.trim()))
			return pathList;
		File file = new File(jarPath);
		if (!file.exists() || !file.canRead())
			throw new RuntimeException(
					"the file is not exist or the file can't  be read");
		if (file.isDirectory()) {
			pathList = new ArrayList<String>();
			for (File tempFile : file.listFiles()) {
				if (tempFile.getName().endsWith("jar")) {
					pathList.add(tempFile.getAbsolutePath());
				}
			}
		}
		return pathList;
	}

	public static JSONObject getPom(String jarPath) {
		JarFile jarFile = null;
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		try {
			jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().contains("pom.properties")) {
					jsonObject = extraPomInfo(entry, jarFile);
					flag = true;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!flag) {
			String[] paths = jarFile.getName().split("\\\\");
			String fileName = paths[paths.length - 1];
			jsonObject = extraInPomInfo(fileName);
		}
		return jsonObject;
	}

	private static JSONObject extraInPomInfo(String fileName) {
		JSONObject json = new JSONObject();
		String[] args = fileName.split("-");
		if (args == null) {
			return json;
		}
		int lastIndex = fileName.indexOf(args[args.length - 1]);
		String artifactId = fileName.substring(0, lastIndex - 1);
		String version = args[args.length - 1].substring(0,
				args[args.length - 1].length() - 4);
		json.put(POM_VERSION, version);
		json.put(POM_ARTIFACTID, artifactId);
		json.put(POM_GROUPID, "not yet found");
		return json;
	}

	private static JSONObject extraPomInfo(JarEntry entry, JarFile jarFile) {
		JSONObject json = new JSONObject();
		if (entry == null || jarFile == null)
			return json;
		Properties prop = new Properties();
		try {
			InputStream in = jarFile.getInputStream(entry);
			prop.load(in);
			json.put(POM_VERSION, prop.getProperty(POM_VERSION));
			json.put(POM_GROUPID, prop.getProperty(POM_GROUPID));
			json.put(POM_ARTIFACTID, prop.getProperty(POM_ARTIFACTID));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void main(String[] args) {
		String path = "F:\\TeSekuWorkSpace\\jspxcms_longyan\\WebContent\\WEB-INF\\lib";
		List<String> pathList = ScanJars.getJarList(path);
		JSONArray jsonArray=new JSONArray();
		for (String temp : pathList) {
			jsonArray.add(ScanJars.getPom(temp));
		}
		FreemarkerUtils.generatePom(jsonArray);
	}
}
