package com.exercise.Dramila.customersearch.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.exercise.Dramila.request.soap.AsyncSoapCall;
import com.exercise.Dramila.request.soap.SoapRequestData;

/**
 * Customer Search service.
 * 
 * @author sandesh
 */
public class CustSearchService {
	
	private CustSResultListener listener;
	private Handler handler;
	
	private Runnable suspended = null;

	/**
	 * @param listener 
	 */
	public CustSearchService(CustSResultListener listener) {
		this.listener = listener;
		this.handler = new Handler();
	}
	/**
	 * Call SOAP WS
	 */
	private synchronized void callWS(final String recnum,final File filename,
			final String username, final String password) {
		// clean previous suspended response callback
		suspended = null;
/*		
		String url = "http://crm.esworkplace.sap.com/sap/bc/srt/xip/sap/CRMXI_BPBASICDATABYNAMEADDRQR?sap-client=800";

		SoapObject request = new SoapObject("http://sap.com/xi/CRM/Global",
				"BusinessPartnerBasicDataByNameAndAddressQuery");

		SoapObject object = new SoapObject(
				"BasicDataSelectionByNameAndAddress",
				"http://sap.com/xi/CRM/Global");
		object.addProperty("BusinessPartnerName", recnum);

		request.addProperty("BasicDataSelectionByNameAndAddress", object);
*/

		SoapRequestData requestData = new SoapRequestData(recnum,filename,
				username, password);
		new AsyncSoapCall(requestData) {

			@Override
			protected void onAuthRequied() {
				postCallback(new Runnable() {
					@Override
					public void run() {
						listener.onAuthRequied();
					}
				});
			}

			@Override
			protected void onError(final Exception e) {
				postCallback(new Runnable() {
					@Override
					public void run() {
						listener.onError(e);
					}
				});
			}

			@Override
			protected void onResponse(final String response) {
				//final List<String> result=null;
			//	final List<String> result = new List<String>();
				final List<String> result = new ArrayList<String>();
				//int len=response.length;
				//String str= response[1];
				int temp=0,sum=0;
	            final String FieldName[]={"ACCT_NO","BKNO","CYNO","SERNO","NAME","ADD1","ADD2","ADD3","DIRECTION","CON_TY","PR_DT","MTR_COUNT","AVG_CONS","MTR_STAT_1","MTRNO","COLUMN","ROW","PR_RDG","MFN1","MFD1","MSG_TO_MR","CITY_CODE1","CITY_CODE2","MRU_CODE","MRO_CODE","MTR_MA","MTR_RO","EMTR_LED","FILE_DATE","DUMMY1","DNLD_FLAG1","NOA","REV_MTRNO","REV_MC","REV_SERNO","C_RDG","MTR_STAT_2","SUB_STAT_2","MTR_STAT_3","C_RDG_ERR","ZERO_FLAG","OCCUP_FLAG","SHFT_SERNO","MSG_FRM_MR","MTR_RDR	TIME","DATE	KWH_1A","KWH_2A	MAX_DEMAND","MD_OF_TM","MD_OF_DT","MTR_MK_ADV","MTR_TM","MTR_DT","RDNG_ENTRY","TOT_I_SER1","TOT_I_SER2","TIS_NOA","SER_FULL","MR_REASON","NC_LED","EL_LED","REV_LED","LED_STS","CONS_SLAB","CHECK_CODE","TAG"};
	        	int len=FieldName.length;
	        	final int FieldWidth[]  ={9,3,2,3,40,40,40,40,32,1,8,1,6,2,10,2,2,8,3,2,30,1,1,2,20,1,1,1,8,11,1,2,10,1,3,8,2,2,2,2,1,1,3,30,8,6,8,8,8,8,6,8,1,6,8,1,3,3,2,1,1,1,1,1,1,1,6,1};
	        	String FieldValue[]= new String[68]; // fields=68
	        
	        	for (temp=0; temp<len;temp++) {
	        		FieldValue[temp] = response.substring(sum, sum+FieldWidth[temp]);
	      //  		helloTxt.append(FieldName[temp]+": "+FieldValue[temp]+"\n");
	        		sum+=FieldWidth[temp];
	        		}
				String str1= FieldValue[1];
				String str2= FieldValue[2];
				String str3= FieldValue[3];
				String str4= FieldValue[4];
				result.add(str1);
				result.add(str2);
				result.add(str3);
				result.add(str4);
				
	        	//for (int i = 0; i < len; i++) {
				//result.add(FieldValue[i]);	
				//};
		
				postCallback(new Runnable() {
					@Override
					public void run() {
						listener.onResponse(result);
					}
				});
				
			}
		}.start();
	}
	
	
	/**
	 * @param item
	 *            'BusinessPartner' response entry
	 * @return extracted name
	 */
	/*
	private String getName(SoapObject item) {
		SoapObject basicData = (SoapObject) item.getProperty("BasicData");
		
		SoapObject common = (SoapObject) basicData.getProperty("Common");
		SoapObject person = (SoapObject) common.getProperty("Person");
		SoapObject name = (SoapObject) person.getProperty("Name");
		return ((SoapPrimitive) name.getProperty("DeviatingFullName"))
				.toString();
	}
	*/
	/**
	 * Extract business partner names form WS call response
	 * 
	 * @param response
	 *            SOAP response to extract names from
	 * @return extracted business partner names
	 */
	/*
	private List<String> extractBPNames(SoapObject response) {
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i = 0; i < response.getPropertyCount(); i++) {
			PropertyInfo propertyInfo = new PropertyInfo();
			response.getPropertyInfo(i, propertyInfo);
			
	//		if ("BusinessPartner".contains(propertyInfo.getName())) {
			if ("BusinessPartner".equals(propertyInfo.getName())) {
				list.add(getName((SoapObject) response.getProperty(i)));
			}
		}
		return list;
	}
	*/

	/**
	 * Drop entries that doesn't contain business partner name.
	 * 
	 * @param names
	 *            business partner names to filter
	 * @param recnum
	 *            name to filter by
	 * @return filtered business partner names
	 */
	/*
	private List<String> filterNames(List<String> names, String recnum) {
		if(names == null || names.isEmpty()) {
			return names;
		}
		List<String> result = new ArrayList<String>();
		
		recnum = recnum.toLowerCase();
		
		for(String name : names) {
			if(name.toLowerCase().contains(recnum)) {
				result.add(name);
			}
		}
		return result;
	}
	*/
	/**
	 * Search Business Partner by name.
	 * 
	 * @param recnum
	 *            customer number
	 * @param username
	 *            username
	 * @param password
	 *            password
	 */
	public void searchByNum(String recnum, File filename, String username, String password) {
		callWS(recnum, filename, username, password);
	}
	
	/**
	 * Subscribe to responses. If there is suspended response it will be posted
	 * to listener.
	 * 
	 * @param listener
	 *            new listener
	 */
	public synchronized void ready(CustSResultListener listener) {
		this.handler = new Handler();
		this.listener = listener;
		if (suspended != null) {
			handler.post(suspended);
			suspended = null;
		}
	}
	
	/**
	 * Indicate that current listener is no longer interested in response.
	 * Response will be suspended until {@link #ready(CustSResultListener)} or new
	 * web service call {@link #callWS(String, String, String)}
	 */
	public synchronized void dismissListener() {
		listener = null;
	}

	/**
	 * Callback will be posted to {@link #handler} if listener is set or
	 * suspended until {@link #ready(CustSResultListener)} is called
	 * 
	 * @param callback
	 *            callback to post
	 */
	private synchronized void postCallback(Runnable callback) {
		if (listener != null) { 
			handler.post(callback);
		} else {
			suspended = callback;
		}
	}
}
