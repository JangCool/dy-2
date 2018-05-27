package kr.co.pionnet.dy.collector.was.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.pionnet.dy.util.TextUtil;

public class WebDataJSONParser {


	private final String IS_NEW_BRUSH = "isNewBrush";
	
	public static ArrayList<String> brushs = new ArrayList<String>();
	private Map<String,Object> data = Collections.synchronizedMap(new HashMap());

	private boolean newBrush;
	
	
	public void setNewBrush(boolean newBrush) {
		this.newBrush = newBrush;
	}

	public enum PARSER_TYPE {		
		
		XVIEW("XVIEW"),
		TPS_SUM("TPS_S"), 
		TPS("TPS"), 
		TRS_AVG("TRS_A"),
		TRS("TRS"),
		ACTIVE("ACT"),
		ACTIVE_SUM("ACT_S"), 
		ASPEED("ACT_SCT"),
		DB_CON_MAX("DCM"),
		DB_CON_ACTIVE("DB_CON_ACT"),
		DB_CON_ACTIVE_SUM("DB_CON_ACT_S"),
		CUSERS("CUSERS"), 
		CUSERS_S("CUSERS_S"),
		SYSTEM_CPU("SCU"),
		PROCESS_CPU_USAGE("PCU"),
		SYSTEM_MEM("S_MEM"),
		GC_TIME("GCT"),
		GC_COUNT("GCCNT"),
		GC_OLD_TIME("OLD"),
		GC_YOUNG_TIME("YOUNG"),
		HEAP_MEM("HMU"),
		HEAP_MEM_TOTAL("TOTAL"),
		HEAP_MEM_USAGE("CURRENT"),
		HEAP_MEM_USED_PERCENT("HMUR"),
		NON_HEAP_MEM_USAGE("NHMU"),
		AREA_MEM_USAGE("AMU"),
		AREA_MEM_USAGE_EDEN("EDEN"),
		AREA_MEM_USAGE_SUR("SUR"),
		AREA_MEM_USAGE_OLD("OLD"),
		AREA_MEM_USAGE_PERM("PERM"),
		AREA_MEM_USAGE_METASPACE("META"),
		TIME("time"),
		BRUSH("brush");
		
		final private String name;
		
		private PARSER_TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
	}
	
	
	public synchronized void addValue(PARSER_TYPE type,String aid,Object value){	
		
		if(data.get(type.getName()) == null ){
			data.put(type.getName()	, new HashMap());
		}
		
		setData(type, aid, value);
	}
	
	public synchronized void addListValue(PARSER_TYPE type,String aid,Object value){	
		
		initList(type);				
		setListData(type, aid, value);
	}
	
	public synchronized void addListValue(PARSER_TYPE type,Object value){	
		
		initList(type);		
		setListData(type, null, value);
	}
	
	private void initList(PARSER_TYPE type){
		if(data.get(type.getName()) == null ){
			data.put(type.getName()	, new ArrayList());
		}
	}
	
	private void setListData(PARSER_TYPE type,String aid,Object value){
		
		List<Map<String,Object>> cdata = (List<Map<String,Object>>) data.get(type.getName());			
		if(cdata != null){
			
			if(aid != null){
				Map listData = new HashMap();
				listData.put(aid, value);	
				cdata.add(listData);

			}else{
				
				if(value instanceof Map){
					cdata.add((Map<String, Object>) value);	
					
				}
			}

		}
		
	}
	
	private void setData(PARSER_TYPE type,String aid,Object value){
					
		Map<String,Object> cdata = (Map<String, Object>) data.get(type.getName());
		if(cdata != null && aid != null){
			cdata.put(aid, value);			
		}
		
	}
	
	public Map<String, Object> getData(){
		return data;
	}
	
	public Map<String, Object> getAllData(String type){
		
		Map returnMap = new HashMap();
		
		returnMap.put("type", type);
		returnMap.put("data",data);
		
		return returnMap;
	}
	
	public Map<String, Object> getData(String key){
		
		Map returnMap = new HashMap();
		returnMap.put("type", key);
		
		if(data.get(key) instanceof List){
			returnMap.put("data", (List<Map<String, Object>>) data.get(key));			
		}else{
			returnMap.put("data", (Map<String, Object>) data.get(key));
		}
		
		return returnMap;
	}
	
	public Map<String, Object> getListData(String key){			
		return getData(key);
	}
	
	public synchronized void setTime(long eTime) {
		data.put(PARSER_TYPE.TIME.getName(), eTime);
	}
	
	public boolean isNewBrush() {
		return this.newBrush;
	}	
	
	
	public synchronized void setBrush(String brush){

		if(!TextUtil.isEmpty(brush) && !brushs.contains(brush)){

			brushs.add(brush);

			data.put(PARSER_TYPE.BRUSH.getName(), brushs);				
			data.put(IS_NEW_BRUSH,true);	
			newBrush = true;

			

		}
	}	
}
