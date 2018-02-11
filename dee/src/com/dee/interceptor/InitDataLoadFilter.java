package com.dee.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import com.dee.util.BaseUtil;
import com.dee.util.FileUtils;
import com.dee.util.InitMappingXml;
import com.dee.util.InitSqlXml;

public class InitDataLoadFilter {

	private String FILE_PATH = this.getClass().getClassLoader().getResource("").getPath();
	
	public static Map<String, String> initData = new HashMap<String, String>();
	public static Map<String, Object> initTypeMapping = new HashMap<String, Object>();
	
	@PostConstruct 
	public void initData() {
//		initMapping();
		initSql();
	}
	
	private void initSql(){
		FILE_PATH = FILE_PATH.replace("WEB-INF/classes/", "");
		String sqlPath = FILE_PATH + "sql";
		List<String> resultList = new ArrayList<String>();
		FileUtils.findFiles(sqlPath, "sql_*.xml", resultList, 5000);
		for(String path : resultList){
			try {
				InitSqlXml.init(initData,path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initMapping(){
		String sqlPath = FILE_PATH + "properties";
		List<String> resultList = new ArrayList<String>();
		FileUtils.findFiles(sqlPath, "mapping_*.xml", resultList, 5000);
		for(String path : resultList){
			try {
				InitMappingXml.init(initTypeMapping,path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
