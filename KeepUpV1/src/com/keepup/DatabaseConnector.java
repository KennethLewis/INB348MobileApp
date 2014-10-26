/*
 * Filename:		DatabaseConnector.java
 * Date Created: 	10th October 2014
 * Author: 			Jackson Powell
 * 
 * Description:		Uses SOAP Web Services programmed in ASP.Net using C#.
 * 
 * 		These web services allow us to fetch information from the web/data server
 * 		and then present them to android. As of now there's little security but
 * 		that is only a prototype attribute.
 */

package com.keepup;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public final class DatabaseConnector {
	static final String NAMESPACE = "http://keepup.com/";
	static final String BASE_URL = "http://101.163.35.210/";
	static final String SOAP_ACTION_BASE = "http://keepup.com/";
	static String SOAP_ACTION_SUFFIX = "";
	static String URL_SUFFIX = "";
	
	static String stringBuffer;

	/* POSTS */
	public static int getUnreadLecturePostCountInUnitForUser(int unitIdNum, int userIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getUnreadLecturePostCountInUnitForUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(unitId);
		request.addProperty(userId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	public static int getUnreadPostCountInUnitByUser(int unitIdNum, int userIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getUnreadPostCountInUnitForUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(unitId);
		request.addProperty(userId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	public static int getUnreadPostCountInGroupForUser(int groupIdNum, int userIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getUnreadPostCountInGroupForUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo groupId = new PropertyInfo();
		groupId.setName("groupId");
		groupId.setValue(groupIdNum);
		groupId.setType(int.class);
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(groupId);
		request.addProperty(userId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	public static boolean addPostToUnit(int userIdNum, int unitIdNum, int groupIdNum, String contentString) {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "postToUnit";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);

		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		PropertyInfo groupId = new PropertyInfo();
		groupId.setName("groupId");
		groupId.setValue(groupIdNum);
		groupId.setType(int.class);
		PropertyInfo content = new PropertyInfo();
		content.setName("content");
		content.setValue(contentString);
		content.setType(String.class);
		
		//Add the property to request object
		request.addProperty(userId);
		request.addProperty(unitId);
		request.addProperty(groupId);
		request.addProperty(content);
		
		fetchData(request);
		
		if(stringBuffer.contains("true"))
			return true;
		
		return false;
	}
	public static boolean addPostToGroup(int userIdNum, int unitIdNum, int groupIdNum, String contentString) {
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "postToGroup";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);

		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		PropertyInfo groupId = new PropertyInfo();
		groupId.setName("groupId");
		groupId.setValue(groupIdNum);
		groupId.setType(int.class);
		PropertyInfo content = new PropertyInfo();
		content.setName("content");
		content.setValue(contentString);
		content.setType(String.class);
		
		//Add the property to request object
		request.addProperty(userId);
		request.addProperty(unitId);
		request.addProperty(groupId);
		request.addProperty(content);
		
		fetchData(request);
		
		if(stringBuffer.contains("true"))
			return true;
		
		return false;
	}
	public static int getPostCountInUnit(int unitIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getPostCountInUnit";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(unitId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	public static String getPostsInUnit(int unitIdNum, int userIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getAllPostsInUnit";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(unitId);
		request.addProperty(userId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	public static String getPostsInGroup(int groupIdNum, int userIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getAllPostsInGroup";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo groupId = new PropertyInfo();
		groupId.setName("groupId");
		groupId.setValue(groupIdNum);
		groupId.setType(int.class);
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(groupId);
		request.addProperty(userId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	public static String getPostsByUserInUnit(int userIdNum, int unitIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getAllPostsByUserInUnit";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(userIdNum);
		userId.setType(int.class);
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(userId);
		request.addProperty(unitId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	
	/* END POSTS */
	
	/* UNITS */
	public static int getUnitMemberCount(int unitIdNum) {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getUnitMemberCount";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo unitId = new PropertyInfo();
		unitId.setName("unitId");
		unitId.setValue(unitIdNum);
		unitId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(unitId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	public static boolean editUnitUnderUser(String code, String name, int id, boolean remove) {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = remove ? "removeUserFromUnit" : "addUserToUnit";
			
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		PropertyInfo unitName = new PropertyInfo();
		unitName.setName("unitName");
		unitName.setValue(name);
		unitName.setType(String.class);
		
		PropertyInfo unitCode = new PropertyInfo();
		unitCode.setName("unitCode");
		unitCode.setValue(code);
		unitCode.setType(String.class);
		
		//Add the property to request object
		request.addProperty(userId);
		request.addProperty(unitName);
		request.addProperty(unitCode);
		
		fetchData(request);
		
		Log.v("KEEPUP", stringBuffer);
		if(stringBuffer.contains("true"))
			return true;
		
		return false;
	}

	public static String getUnit(int id) {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getUnit";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo unitId = new PropertyInfo();
		//Set Name
		unitId.setName("unitId");
		unitId.setValue(id);
		unitId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(unitId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}

	public static String getUnitsByUser(int id) {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getAllUnitsByUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(userId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	
	public static int getUnitCountByUser(int id) {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getUnitCountByUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		//Add the property to request objects
		request.addProperty(userId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	
	public static String getUnits() {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getAllUnits";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	
	public static String getUnitsDistinct() {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getAllUnitsDistinct";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	
	public static int getUnitCount() {
		URL_SUFFIX = "UnitService.asmx";
		SOAP_ACTION_SUFFIX = "getUnitCount";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	/* END UNITS */
	
	/* START GROUPS */
	
	public static int createGroup (int id, String groupName, String description){
		
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "createGroup";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo unitId = new PropertyInfo();
		
		unitId.setName("unitId");
		unitId.setValue(id);
		unitId.setType(int.class);
		
		PropertyInfo name = new PropertyInfo();
		name.setName("groupName");
		name.setValue(groupName);
		name.setType(String.class);
		
		PropertyInfo desc = new PropertyInfo();
		desc.setName("groupDescription");
		desc.setValue(description);
		desc.setType(String.class);
		
		
		//Add the property to request object
		request.addProperty(unitId);
		request.addProperty(name);
		request.addProperty(desc);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
		
	}
	
	public static boolean addUserToGroup (int gId, int uId){
		
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "addUserToGroup";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo groupId = new PropertyInfo();
		
		groupId.setName("groupId");
		groupId.setValue(gId);
		groupId.setType(int.class);
		
		PropertyInfo userId = new PropertyInfo();
		userId.setName("userId");
		userId.setValue(uId);
		userId.setType(int.class);
		
		
		//Add the property to request object
		request.addProperty(groupId);
		request.addProperty(userId);
		
		fetchData(request);
		
		Log.v("KEEPUP", stringBuffer);
		if(stringBuffer.contains("true"))
			return true;
		
		return false;
	}
	public static String getGroupsByUser(int id) {
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "getAllGroupsByUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(userId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}

	public static int getGroupCountByUser(int id) {
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "getGroupCountByUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(userId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	
	public static String getGroups() {
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "getAllGroups";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	
	public static int getGroupCount() {
		URL_SUFFIX = "GroupService.asmx";
		SOAP_ACTION_SUFFIX = "getGroupCount";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	public static int getPostCountInGroup(int groupIdNum) {
		URL_SUFFIX = "PostService.asmx";
		SOAP_ACTION_SUFFIX = "getPostCountInGroup";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		PropertyInfo groupId = new PropertyInfo();
		groupId.setName("groupId");
		groupId.setValue(groupIdNum);
		groupId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(groupId);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	/* END GROUPS */
	
	/* USERS */
	public static String getUser(int id) {
		URL_SUFFIX = "UserService.asmx";
		SOAP_ACTION_SUFFIX = "getUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		//Add the property to request object
		request.addProperty(userId);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}
	public static String getAllUsers() {
		URL_SUFFIX = "UserService.asmx";
		SOAP_ACTION_SUFFIX = "getAllUsers";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		if(stringBuffer.contains("NoResults"))
			return null;
		
		return stringBuffer;
	}

	public static int getUserCount() {
		URL_SUFFIX = "UserService.asmx";
		SOAP_ACTION_SUFFIX = "getUserCount";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		fetchData(request);
		
		return Integer.parseInt(stringBuffer);
	}
	
	public static boolean getLoggedIn(int id, String pass) {
		URL_SUFFIX = "UserService.asmx";
		SOAP_ACTION_SUFFIX = "loginUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		//Property which holds input parameters
		PropertyInfo password = new PropertyInfo();
		//Set Name
		password.setName("pass");
		password.setValue(pass);
		password.setType(String.class);
		
		//Add the property to request object
		request.addProperty(userId);
		request.addProperty(password);
		
		fetchData(request);
		
		Log.v("KEEPUP", stringBuffer);
		if(stringBuffer.contains("true"))
			return true;
		
		return false;
	}

	public static boolean registerUser(int id, String username, String email, int rights, String pass) {
		URL_SUFFIX = "UserService.asmx";
		SOAP_ACTION_SUFFIX = "registerUser";
		
		SoapObject request = new SoapObject(NAMESPACE, SOAP_ACTION_SUFFIX);
		
		//Property which holds input parameters
		PropertyInfo userId = new PropertyInfo();
		//Set Name
		userId.setName("userId");
		userId.setValue(id);
		userId.setType(int.class);
		
		PropertyInfo name = new PropertyInfo();
		name.setName("username");
		name.setValue(username);
		name.setType(String.class);
		
		PropertyInfo emailVal = new PropertyInfo();
		emailVal.setName("email");
		emailVal.setValue(email);
		emailVal.setType(String.class);
		
		PropertyInfo rightsVal = new PropertyInfo();
		rightsVal.setName("rights");
		rightsVal.setValue(rights);
		rightsVal.setType(int.class);
		
		PropertyInfo password = new PropertyInfo();
		password.setName("pass");
		password.setValue(pass);
		password.setType(String.class);
		
		//Add the property to request object
		request.addProperty(userId);
		request.addProperty(name);
		request.addProperty(emailVal);
		request.addProperty(rightsVal);
		request.addProperty(password);
		
		fetchData(request);
		
		Log.v("KEEPUP", stringBuffer);
		if(stringBuffer.contains("true"))
			return true;
		
		return false;
	}
	/* END USERS */
	
	//MAIN CONNECTOR AND FETCHER FOR SQL DATA
	//Uses SOAP web services to retrieve data.
	public static void fetchData(SoapObject request) {
		//Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Set output SOAP object
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(BASE_URL + URL_SUFFIX);

		try {
			//Invole web service
			androidHttpTransport.call(SOAP_ACTION_BASE + SOAP_ACTION_SUFFIX, envelope);
			
			//Get the response and add to string list to be formed later.
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            stringBuffer = response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
