package prj.dee.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prj.dee.util.FileUtils;
import prj.dee.util.InitMappingXml;

public class InitTypeMappingFilter {

	private String FILE_PATH = this.getClass().getClassLoader().getResource("").getPath();
	
	public static Map<String, Object> initTypeMapping = new HashMap<String, Object>();
	
	public void initData() {
		FILE_PATH = FILE_PATH.replace("WEB-INF/classes/", "");
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
