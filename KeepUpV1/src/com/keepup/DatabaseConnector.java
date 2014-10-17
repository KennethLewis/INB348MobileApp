package com.keepup;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public final class DatabaseConnector {
	static final String NAMESPACE = "http://keepup.com/";
	static final String BASE_URL = "http://121.222.60.176/";
	static final String SOAP_ACTION_BASE = "http://keepup.com/";
	static String SOAP_ACTION_SUFFIX = "";
	static String URL_SUFFIX = "";
	
	static String stringBuffer;

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
		
		if(stringBuffer == null)
			return "NoResults";
		
		return stringBuffer;
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
		if(stringBuffer == "false")
			return false;
		
		return true;
	}

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
