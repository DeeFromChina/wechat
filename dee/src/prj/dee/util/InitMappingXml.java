package prj.dee.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import prj.dee.dao.BaseDao;
import prj.dee.interceptor.InitDataLoadFilter;


public class InitMappingXml {

//	public static void main(String[] args){
//		String systemPath = System.getProperty("user.dir");
//		String path = systemPath+"\\web\\WEB-INF\\classes\\properties\\mapping_FileTable.xml";
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			init(map, path);
//			InitTypeMappingFilter.initTypeMapping.putAll(map);
//			mappingValue("FileTable", "save", "test1", "345");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void init(Map<String, Object> map, String path) throws Exception {
		try {
			XMLWriter writer = null;// 声明写XML的对象
			SAXReader reader = new SAXReader();
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");// 设置XML文件的编码格式
			// 读取文件 转换成Document
			Document document = reader.read(new File(path));
			// 获取根节点元素对象
			Element root = document.getRootElement();
			// 遍历
			mappingNodes(root, map, path);
			System.out.println(new Date() + " [initmapping] :" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 遍历当前节点下的所有节点(mapping)
	private static void mappingNodes(Element node, Map<String, Object> map, String path) throws Exception {
		if(node == null){
			throw new Exception("Element node is null");
		}
		if("ref".equals(node.getName())){
			if("bean".equals(node.getParent().getName())){
				setBean(node, map);
			}else{
				checkRetNode(node, path);
				String mappingGroupId = node.getParent().getParent().attribute("id").getValue().trim();
				setMap(node, map, mappingGroupId, node.getParent().attribute("id").getValue().trim());
			}
		}
		if ("mapping".equals(node.getName())) {
			if("bean".equals(node.getParent().getName())){
				setBean(node, map);
			}else{
				checkMappingNode(node, path);
				String mappingGroupId = node.getParent().attribute("id").getValue();
				setMap(node, map, mappingGroupId, null);
			}
		}
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			mappingNodes(e, map, path);
		}
	}
	
	private static void checkMappingNode(Element node, String path) throws Exception{
		if (node.getParent() == null || !"mappingGroup".equalsIgnoreCase(node.getParent().getName())) {
			setError(node, path, "mappding", false);
		} else {
			setError(node.getParent(), path, "mappingGroup", true);
		}
	}
	
	private static void checkRetNode(Element node, String path) throws Exception{
		if (node.getParent() == null || !"mapping".equalsIgnoreCase(node.getParent().getName())) {
			setError(node, path, "ref", false);
		} else if(node.getParent().getParent() == null || !"mappingGroup".equalsIgnoreCase(node.getParent().getParent().getName())){
			setError(node.getParent(), path, "mapping", false);
		} else {
			setError(node.getParent(), path, "mapping", true);
		}
	}
	
	private static void setError(Element node, String path, String msg, boolean isreturn) throws Exception {
		if(node.attribute("id") == null || "".equals(node.attribute("id").getValue().trim())){
			throw new Exception(new Date() + " path:" + path + "-- " + msg + "结构出错[id为空]");
		}else {
			if(isreturn){
				return;
			}
			throw new Exception(new Date() + " path:" + path + "-- " + msg + "[id="+node.attribute("id").getValue()+"]结构出错");
		}
	}
	
	private static String getAttributeValue(Attribute attribute, String msg) throws Exception {
		if (attribute.getValue() == null || "".equals(attribute.getValue())) {
			throw new Exception(new Date() + msg);
		}
		return attribute.getValue();
	}
	
	/**
	 * @param node 节点
	 * @param map 内存初始化map
	 * @param mappingGroupId
	 * @param mappingId
	 * @throws Exception
	 */
	private static void setMap(Element node, Map<String, Object> map, String mappingGroupId, String mappingId) throws Exception {
		if("bean".equals(mappingGroupId)){
			throw new Exception(new Date() + " mappingId不能是bean");
		}
		String id = null, classValue = null, mappingMethod = null;
		List<Attribute> list = node.attributes();
		for (Attribute attribute : list) {
			if ("id".equals(attribute.getName())) {
				id = getAttributeValue(attribute, mappingId == null ? " mappingId有误 " : "retId有误");
			} else if ("class".equals(attribute.getName())) {
				classValue = getAttributeValue(attribute,  mappingId == null ? " mappingClass有误 " : "retClass有误");
			} else if ("method".equals(attribute.getName())) {
				mappingMethod = getAttributeValue(attribute, " mappingMethod有误 ");
			}
		}
		Map<String, Object> ref = new HashMap<String, Object>();
		Map<String, Object> mappingGroupMap = new HashMap<String, Object>();
		if (map.get(mappingGroupId) != null) {
			mappingGroupMap = (Map<String, Object>) map.get(mappingGroupId);
		}
		Map<String, Object> mappingMap = new HashMap<String, Object>();
		if(mappingGroupMap.get(mappingId) != null){
			mappingMap = (Map<String, Object>) mappingGroupMap.get(mappingId);
			if(mappingMap.get("ref") != null){
				ref = (Map<String, Object>) mappingMap.get("ref");
			}
		}
		if(mappingId != null){
			ref.put(id, classValue);
			mappingMap.put("ref", ref);
			mappingGroupMap.put(mappingId, mappingMap);
		}else {
			mappingMap.put("mappingId", id);
			mappingMap.put("mappingClass", classValue);
			mappingMap.put("mappingMethod", mappingMethod);
			if (mappingGroupMap.get(id) != null) {
				throw new Exception(new Date() + " mappingId重复 :" + id);
			}
			mappingGroupMap.put(id, mappingMap);
		}
		map.put(mappingGroupId, mappingGroupMap);
	}
	
	private static void setBean(Element node, Map<String, Object> map) throws Exception {
		Element parentNode = node.getParent();
		if(!"bean".equals(parentNode.getName())){
			throw new Exception(new Date() + " bean结构有误");
		}
		Map<String, String> beanMap = (Map<String, String>) map.get("bean");
		if(beanMap == null){
			beanMap = new HashMap<String, String>();
		}
		
		String id = null, classValue = null;
		List<Attribute> list = parentNode.attributes();
		for (Attribute attribute : list) {
			if ("id".equals(attribute.getName())) {
				id = getAttributeValue(attribute, " beanId有误 ");
			} else if ("class".equals(attribute.getName())) {
				classValue = getAttributeValue(attribute,  " beanClass有误 ");
			}
		}
		beanMap.put("bean_"+id, classValue);
		
		String beanId = "bean_ref_"+id;
		list = node.attributes();
		for (Attribute attribute : list) {
			if ("id".equals(attribute.getName())) {
				id = getAttributeValue(attribute, " beanRefId有误 ");
			} else if ("class".equals(attribute.getName())) {
				classValue = getAttributeValue(attribute,  " beanRefClass有误 ");
			}
		}
		beanMap.put(beanId, "ref_"+id);
		beanMap.put("ref_"+id, classValue);
		map.put("bean", beanMap);
	}
	
	/**
	 * @description 通过反射调用方法
	 * @param baseDao
	 * @param groupId
	 * @param mappingId
	 * @param objects
	 */
	public static void mappingValue(BaseDao baseDao, String groupId, String mappingId, Object...objects){
		if(InitDataLoadFilter.initTypeMapping.get(groupId) == null){
			return;
		}
		Map<String, Object> mappingGroupMap = (Map<String, Object>) InitDataLoadFilter.initTypeMapping.get(groupId);
		if(mappingGroupMap.get(mappingId) == null){
			return;
		}
		Map<String, Object> mappingMap = (Map<String, Object>) mappingGroupMap.get(mappingId);
		String mappingClass = mappingMap.get("mappingClass").toString();
		String mappingMethod = mappingMap.get("mappingMethod").toString();
		Map<String, Object> ref = (Map<String, Object>) mappingMap.get("ref");
		try {
			Class t = Class.forName(mappingClass);
			Object o = t.newInstance();
			Method[] methods = t.getMethods();
			
			if(ref != null){
				List<Field> list = Arrays.asList(t.getDeclaredFields());
				for(int i = 0; i < list.size(); i++){
					Field field = list.get(i);
					field.setAccessible(true);// 设置些属性是可以访问的
					if(field.isAnnotationPresent(Autowired.class)){
						Object refClass = ref.get(field.getName());
						if(refClass == null){
							continue;
						}
						Class implClass = Class.forName(refClass.toString());
						Object implOject = implClass.newInstance();
						if("baseDao".equals(field.getName())){
							implOject = baseDao;
						}
						field.set(o, implOject);
					}
				}
			}
			for(Method method : methods){
				if(mappingMethod.equals(method.getName())){
					if(CorrectParam(method, objects, groupId, mappingId, mappingClass, mappingMethod)){
						method.invoke(o, objects);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 是否正确的参数形式
	 * @return
	 */
	private static boolean CorrectParam(Method method, Object[] objects, String groupId, String mappingId, String mappingClass, String mappingMethod) throws Exception{
		if(method.getGenericParameterTypes().length != objects.length){
			throw new Exception(new Date() + " 参数数量传递不一致 :[mappingGroupId="+groupId+"][mappingId="+mappingId+"][mappingClass="+mappingClass+"][mappingMethod="+mappingMethod+"]");
		}
		Class[] paramClasses = method.getParameterTypes();
		for(int i = 0; i < objects.length;  i++){
			if(!paramClasses[i].getName().equals(objects[i].getClass().getName())){
				System.err.println("参数:"+objects[i]+" 参数类型:"+paramClasses[i].getName()+" 传参类型:"+objects[i].getClass().getName());
				throw new Exception(new Date() + " 参数类型传递不一致 :[mappingGroupId="+groupId+"][mappingId="+mappingId+"][mappingClass="+mappingClass+"][mappingMethod="+mappingMethod+"]");
			}
		}
		return true;
	}
}
