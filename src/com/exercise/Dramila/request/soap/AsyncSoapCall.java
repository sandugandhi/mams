package com.exercise.Dramila.request.soap;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;
import android.widget.Toast;

import com.exercise.Dramila.R;
/**
 * Asynchronous soap WS call.
 * 
 * @author sandesh
 */
public abstract class AsyncSoapCall extends Thread {

	private SoapRequestData request;
	//private SoapSerializationEnvelope envelope;
	private String strResponse;
	private static final String TAG = "Androidtxt";
	private static boolean opened=true; 
	String[] SplitAlldata =null;
	String FieldValue[]= new String[68]; // fields=68
	
	
	/**
	 * @param request
	 *            request data to be used for call
	 * @param listener
	 *            listener to notify
	 */
	public AsyncSoapCall(SoapRequestData request) {
		this.request = request;
//		this.envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		this.envelope.setOutputSoapObject(request.getSoapRequest());
	}

	@Override
	public void run() {
		try {
			call();
		} catch (final Exception e) {
			onError(e);
		}
	}

	/**
	 * @return parsed SOAP response
	 * @throws SoapFault thrown on SOAP failure
	 */
	/*
	protected String getResponse() throws SoapFault {
		if (envelope.bodyIn instanceof SoapFault) {
			throw (SoapFault) " Soap fault";
		}
		return (String) envelope.bodyIn;
	}
	
	*/
	protected String getResponse(){
		return strResponse;
	}
	/**
	 * @return get connection for {@link #request} url
	 */
	/*
	protected HttpURLConnection getConnection() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) (new URL(request.getUrl()))
				.openConnection();
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		addAuthorization(connection);
		return connection;
	}
	*/
	/**
	 * Execute SOAP call 
	 */
	protected void call()
			throws IOException{
		
		String soapAction = request.getrecNum();
		
		File myorigfile=request.getFileName();
		if (soapAction == null){soapAction = "1";}
		int recnumber = Integer.parseInt(soapAction.trim());
		//onAuthRequied();
	//	if (opened)
	//	{
		SplitAlldata = readFile(myorigfile);
	//	opened=false;
	//	}
	//	setContentView(R.layout.mymain);
  //      TextView helloTxt = (TextView)findViewById(R.id.hellotxt);  
    	
    	
    	//String[] SplitAlldata=sb.toString().split("\n"); // This is the whole file.
    	//int RecSize=496; // constant                
        int NumofRecords= SplitAlldata.length;
        //helloTxt.setText("Number of Records = " + NumofRecords +"\n" );
		
        int SelectedRecord=recnumber;
        //helloTxt.setText("Selected Record = " + SelectedRecord +"\n" );
        
        if (SelectedRecord>0) SelectedRecord--;
        if (SelectedRecord<NumofRecords && SelectedRecord>=0){
        	
        	strResponse = SplitAlldata[SelectedRecord];
        
        	int temp=0,sum=0;
            final String FieldName[]={"ACCT_NO","BKNO","CYNO","SERNO","NAME","ADD1","ADD2","ADD3","DIRECTION","CON_TY","PR_DT","MTR_COUNT","AVG_CONS","MTR_STAT_1","MTRNO","COLUMN","ROW","PR_RDG","MFN1","MFD1","MSG_TO_MR","CITY_CODE1","CITY_CODE2","MRU_CODE","MRO_CODE","MTR_MA","MTR_RO","EMTR_LED","FILE_DATE","DUMMY1","DNLD_FLAG1","NOA","REV_MTRNO","REV_MC","REV_SERNO","C_RDG","MTR_STAT_2","SUB_STAT_2","MTR_STAT_3","C_RDG_ERR","ZERO_FLAG","OCCUP_FLAG","SHFT_SERNO","MSG_FRM_MR","MTR_RDR	TIME","DATE	KWH_1A","KWH_2A	MAX_DEMAND","MD_OF_TM","MD_OF_DT","MTR_MK_ADV","MTR_TM","MTR_DT","RDNG_ENTRY","TOT_I_SER1","TOT_I_SER2","TIS_NOA","SER_FULL","MR_REASON","NC_LED","EL_LED","REV_LED","LED_STS","CONS_SLAB","CHECK_CODE","TAG"};
        	int len=FieldName.length;
        	final int FieldWidth[]  ={9,3,2,3,40,40,40,40,32,1,8,1,6,2,10,2,2,8,3,2,30,1,1,2,20,1,1,1,8,11,1,2,10,1,3,8,2,2,2,2,1,1,3,30,8,6,8,8,8,8,6,8,1,6,8,1,3,3,2,1,1,1,1,1,1,1,6,1};
        	
        
        	for (temp=0; temp<len;temp++) {
        		FieldValue[temp] = strResponse.substring(sum, sum+FieldWidth[temp]);
      //  		helloTxt.append(FieldName[temp]+": "+FieldValue[temp]+"\n");
        		sum+=FieldWidth[temp];
        		}
        	
        	}
        	
	/*	byte requestData[] = createRequestData();
		HttpURLConnection connection = getConnection(); 
		connection.setRequestProperty("User-Agent", "kSOAP/2.0");
		connection.setRequestProperty("SOAPAction", soapAction);
		connection.setRequestProperty("Content-Type", "text/xml");
		connection.setRequestProperty("Connection", "close");
		connection.setRequestProperty("Content-Length", (new StringBuilder())
				.append("").append(requestData.length).toString());
		connection.setRequestMethod("POST");
		connection.connect();
		OutputStream os = connection.getOutputStream();
		os.write(requestData, 0, requestData.length);
		os.flush();
		os.close();
		requestData = null;
		*/
		
		//InputStream is;
		/*try {
			//connection.connect();
			//is = connection.getInputStream();
		} catch (IOException e) {
			//is = connection.getErrorStream();
			if (is == null) {
			//	connection.disconnect();
				throw e;
			}
		}
		*/
		//if (connection.getResponseCode() == 401) {
			// notify listener that basic auth required
		//	onAuthRequied();
		//	return;
		//}
		
		//parseResponse(is);
		// notify listener on successful response
		onResponse(getResponse());
	}
	


	protected abstract void onResponse(String response);

	protected abstract void onAuthRequied();

	protected abstract void onError(Exception e);
	
	/**
	 * Add basic HTTP authentication header if credentials provided in {@link #request}
	 * 
	 * @param connection
	 *            connection to add header to
	 */
	
	/*
	protected void addAuthorization(HttpURLConnection connection) throws IOException {
		// add basic auth header if username and password specified
		if (request.getUsername() != null && request.getPassword() != null) {
			StringBuffer buf = new StringBuffer(request.getUsername());
			buf.append(':').append(request.getPassword());
			byte[] raw = buf.toString().getBytes();
			buf.setLength(0);
			buf.append("Basic ");
			org.kobjects.base64.Base64.encode(raw, 0, raw.length, buf);
			connection.setRequestProperty("Authorization", buf.toString());
		}
	}
	*/
    public String[] readFile(File file) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        StringBuffer sb = new StringBuffer();

        try {
          
        	fis = new FileInputStream(file);


          // Here BufferedInputStream is added for fast reading.
          bis = new BufferedInputStream(fis);
          dis = new DataInputStream(bis);

          // dis.available() returns 0 if the file does not have more lines.
          while (dis.available() != 0) {

          // this statement reads the line from the file and print it to
            // the console.
        	  sb.append(dis.readLine());
        	  if (dis.available() != 0) {
        		  sb.append("\n");
        	  }
          }

          // dispose all the resources after using them.
          fis.close();
          bis.close();
          dis.close();

        } catch (FileNotFoundException e) {
        	Log.e(TAG, "File not found", e);
	//		Toast.makeText(this, R.string.file_not_found,
	//				Toast.LENGTH_SHORT).show();
			return null;
        } catch (IOException e) {
        	Log.e(TAG, "Error reading file", e);
	//		Toast.makeText(this, R.string.error_reading_file,
	//				Toast.LENGTH_SHORT).show();
			return null;
        }
        
        return sb.toString().split("\n");
    }
 /**
	 * @return serialized SOAP request data
	 */
	/*
	protected byte[] createRequestData()
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XmlSerializer xw = new KXmlSerializer();
		xw.setOutput(bos, null);
		envelope.write(xw);
		xw.flush();
		bos.write(13);
		bos.write(10);
		bos.flush();
		return bos.toByteArray();
	}
	*/
	/**
	 * @param is stream to parse response from
	 */
	/*
	protected void parseResponse(InputStream is)
			throws XmlPullParserException, IOException {
		XmlPullParser xp = new KXmlParser();
		xp.setFeature(
				"http://xmlpull.org/v1/doc/features.html#process-namespaces",
				true);
		xp.setInput(is, null);
		envelope.parse(xp);
	}
	*/
}
