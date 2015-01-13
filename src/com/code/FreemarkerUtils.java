package com.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerUtils {

	private static final String TEMPLATE_NAME="pom.ftl";
	private static final String TEMPLATE_DIRECTORY="template";
	private static  final String DIST_FILE_PATH="template/pom.xml";
	private static final String CharacterEncoding="utf-8";
	
	public static void generatePom(JSONArray jsonArray){
		 Configuration config=new Configuration();
		 try {
			config.setDirectoryForTemplateLoading(new File(TEMPLATE_DIRECTORY));
			Template template=config.getTemplate(TEMPLATE_NAME,CharacterEncoding);
			Map<String,Object> rootMap=new HashMap<String,Object>();
			rootMap.put("jsonObject",jsonArray.toJSONString());
			File distFile=new File(DIST_FILE_PATH);
			if(distFile.exists()){
				distFile.delete();
			}
			distFile.createNewFile();
			template.process(rootMap, new FileWriter(distFile));
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
}
