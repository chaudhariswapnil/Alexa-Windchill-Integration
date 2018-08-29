package com.custom;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wt.log4j.LogR;

public class Testing {
	private static final Logger LOGGER = LogR.getLogger(ActivityController.class.getName());

	public static void main(String[] args) throws JSONException {

		String str = "{\"Swapnil\":\"swapnil\",\"jsonArray\":[{\"age\":30,\"userName\":\"sandeep\"},{\"age\":5,\"userName\":\"vivan\"}]}";
		//createsActionItem(str, " ");
		JsonTest();
	}


	public static int createsActionItem(String json,String pboUfid) throws JSONException{

		JSONArray jsonArray = null;
/*		JSONArray jsonArray = null;
		org.json.JSONObject explrObject = null;
		try {
			jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				explrObject = jsonArray.getJSONObject(i);
				System.out.println("Object :"+explrObject);
			}
		} catch (JSONException e) {
			LOGGER.error(e.getLocalizedMessage(),e);
			throw new JSONException(e);
		}
*/
		org.json.JSONObject explrObject1 = new JSONObject(json);
		System.out.println(explrObject1);
		System.out.println(explrObject1.get("Swapnil"));
		jsonArray = (JSONArray) explrObject1.get("jsonArray");
		for (int i = 0; i < jsonArray.length(); i++) {
			org.json.JSONObject	explrObject = jsonArray.getJSONObject(i);
			System.out.println("Object :"+explrObject);
		}
		return 0;
	}
	
	static void JsonTest() throws JSONException{
		JSONArray jsonArray = new JSONArray();
		org.json.JSONObject explrObject = new org.json.JSONObject();
		org.json.JSONObject cn = new org.json.JSONObject();
		org.json.JSONObject cr = new org.json.JSONObject();
		org.json.JSONObject pr = new org.json.JSONObject();
		org.json.JSONObject obj = new org.json.JSONObject();
		explrObject.put("Count", 5);
		
		jsonArray = new JSONArray();
		obj.put("CnName1", "CNNAME1");
		obj.put("CnNumber1", "CNNumber1");
		jsonArray.put(obj);
		
		obj = new org.json.JSONObject();
		obj.put("CnName2", "CNNAME2");
		obj.put("CnNumber2", "CNNumber2");
		jsonArray.put(obj);
		cn.put("CNTask", jsonArray);
		
		jsonArray = new JSONArray();
		obj = new org.json.JSONObject();
		obj.put("CRName1", "CRNAME1");
		obj.put("CRNumber1", "CRNumber1");
		jsonArray.put(obj);
		
		obj = new org.json.JSONObject();
		obj.put("CRName2", "CRNAME2");
		obj.put("CRNumber2", "CRNumber2");
		jsonArray.put(obj);
		cr.put("CRTask", jsonArray);
		
		
		JSONArray finalArray = new JSONArray();
		finalArray.put(cn);
		finalArray.put(cr);
		explrObject.put("All List", finalArray);
		System.out.println(explrObject.optJSONArray("activitYList"));
	}
}
