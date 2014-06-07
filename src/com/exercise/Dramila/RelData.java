package com.exercise.Dramila;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class RelData {
	private static File myfilename=null;
	private static int recordNumber  = 1;
	
	public static final String FieldName[] = {"ACCT_NO","BKNO","CYNO","SERNO","NAME","ADD1","ADD2","ADD3","DIRECTION","CON_TY","PR_DT","MTR_COUNT","AVG_CONS","MTR_STAT_1","MTRNO","COLUMN","ROW","PR_RDG","MFN1","MFD1","MSG_TO_MR","CITY_CODE1","CITY_CODE2","MRU_CODE","MRO_CODE","MTR_MA","MTR_RO","EMTR_LED","FILE_DATE","DUMMY1","DNLD_FLAG1","NOA","REV_MTRNO","REV_MC","REV_SERNO","C_RDG","MTR_STAT_2","SUB_STAT_2","MTR_STAT_3","C_RDG_ERR","ZERO_FLAG","OCCUP_FLAG","SHFT_SERNO","MSG_FRM_MR","MTR_RDR","TIME","DATE","KWH_1A","KWH_2A","MAX_DEMAND","MD_OF_TM","MD_OF_DT","MTR_MK_ADV","MTR_TM","MTR_DT","RDNG_ENTRY","TOT_I_SER1","TOT_I_SER2","TIS_NOA","SER_FULL","MR_REASON","NC_LED","EL_LED","REV_LED","LED_STS","CONS_SLAB","CHECK_CODE","TAG"};
	public static final    int FieldWidth[]= {9,3,2,3,40,40,40,40,32,1,8,1,6,2,10,2,2,8,3,2,30,1,1,2,20,1,1,1,8,11,1,2,10,1,3,8,2,2,2,2,1,1,3,30,8,6,8,8,8,8,6,8,1,6,8,1,3,3,2,1,1,1,1,1,1,1,6,1};
	public static final    int FieldStart[]= {9,12,14,17,57,97,137,177,209,210,218,219,225,227,237,239,241,249,252,254,284,285,286,288,308,309,310,311,319,330,331,333,343,344,347,355,357,359,361,363,364,365,368,398,406,412,420,428,436,444,450,458,459,465,473,474,477,480,482,483,484,485,486,487,488,489,495,496};
	public static final    int FWlen=FieldWidth.length;
	
	//private static String FieldValue[]= new String[FWlen]; // fields=68
	public static List<StringBuffer> mlistSB = null;
	private static String mselRecData = null;
	public static List<String> mlistresult =null;
	public static int NumofRecords;

	public static int avg_consInt, curr_rdngInt, prev_rdngInt;
	public static String avg_cons="        ", prev_rdng="        ", curr_rdng="        ";//8 space characters
	
	private static final String TAG = "Androidtxt";	
	
	/*
	 * Write Action id
	 */
	private static final int WRITE_C_RDG  = 1;
	private static final int WRITE_Z_FLAG = 2;
	
	
	
	public static void nextRecord(){
		if (recordNumber >= 1 && recordNumber < NumofRecords){
			recordNumber++;
			processtext(recordNumber);
		}else{
				// display out of bounds or do nothing
			}
		
}
	
	public static void prevRecord(){
	
	if (recordNumber>1 && recordNumber <= NumofRecords){
	recordNumber--;
	processtext(recordNumber);
	}
	else{
		
		// out of bounds or do nothing
		
		}
	}
	
	public static  List<String> getlistresult(){
		
		return mlistresult;
	}

	public static void setlistresult(List<String> l){
	//	int len=file.length;
	//for (int i = 0; i < len; i++) {
		mlistresult=l;
	}	

	
	public static String getselRecData(){
		
		return mselRecData;
	}

	public static void setselRecData(String str){
	//	int len=file.length;
	//for (int i = 0; i < len; i++) {

		mselRecData=str;

	}	
	
	
    
	public static void setrecNumber(int i){
	recordNumber=i;
	
	}	

	public static int getrecNumber(){
	return recordNumber;
	
	}	
	public static File getmyfilename(){
		
	return myfilename;
	}
	
	public static void setmyfilename(File file){
		
	myfilename=file;
	}


	public static void validate_and_write(final String str){
		try{
			int current_reading_int = Integer.parseInt(str.trim());
			if(current_reading_int<0 || current_reading_int<prev_rdngInt){
				//raise error
			}
			else{ // valid current reading can be equal or more  
					curr_rdngInt=current_reading_int;
					curr_rdng=String.valueOf(curr_rdngInt);
					if (current_reading_int==prev_rdngInt){
						// raise zero consumption flag
						write_to_origFile(WRITE_Z_FLAG);
					}
					else if(current_reading_int>0 && current_reading_int > prev_rdngInt){
						// as of now nothing to do	
					}
						write_to_origFile(WRITE_C_RDG);
						//units_consumed = current_reading_int-prevRdng
				}
			}catch(NumberFormatException e){
				Log.e(TAG, "Error parsing the current reading", e);
		    }
	}
	
	private static void write_to_origFile(int id){
		switch(id){
			case WRITE_C_RDG:
			StringBuffer sb= new StringBuffer("        ");// 8 spaces
			char[] c=curr_rdng.toCharArray();
			int len=curr_rdng.toCharArray().length;
			if(len<9){
				sb.replace(8-len, 8, curr_rdng);	
				//String s = sb.toString();
				mlistSB.set(recordNumber-1,(mlistSB.get(recordNumber-1)).replace(FieldStart[34], FieldStart[35],sb.toString()));
			}	
			break;
			case WRITE_Z_FLAG:
				//for(int s=0;s<(recordNumber-1);s++){
				//i=mSBFile.indexOf("\n", s);
				//}
				//mSBFile.replace(i+1+FieldStart[39], i+1+FieldStart[40], "Z");				
				mlistSB.set(recordNumber-1, (mlistSB.get(recordNumber-1)).replace(FieldStart[39], FieldStart[40],"Z"));		
				break;
			};
	
}


	
public static void processtext(int SelectedRecord){
	String selRecData; 
	List<String> listresult = new ArrayList<String>();
	//NumofRecords = origSplitFile.length;
	  //   setContentView(R.layout.mymain);
      //   TextView helloTxt = (TextView)findViewById(R.id.hellotxt);  
    	
    	
    	//String[] SplitAlldata=sb.toString().split("\n"); // This is the whole file.
    	
        //int RecSize=496; // constant                
        // helloTxt.setText("Number of Records     = " + NumofRecords +"\n" );
        
        //int SelectedRecord=130;
       // helloTxt.setText("Selected Record number= " + SelectedRecord +"\n" );
        
      if (SelectedRecord>0 && SelectedRecord <= NumofRecords){
      	recordNumber=SelectedRecord; // valid record selected
        SelectedRecord--;            // since our array starts with index number 0   	
      }
      if (SelectedRecord>=0 && SelectedRecord<NumofRecords){
        	
        	//selRecData = origSplitFile[SelectedRecord].toString();
       	selRecData=(mlistSB.get(SelectedRecord)).toString();
        mselRecData=selRecData;
        	listresult.clear();
        		
        	//FieldValue[first++] = FieldName[2]  + ": " + selRecData.substring(FieldStart[1] , FieldStart[2]);	//CYNO
        	//FieldValue[first++] = FieldName[3]  + ": " + selRecData.substring(FieldStart[2] , FieldStart[3]);	//SERNO
      
        	listresult.add(FieldName[14] + ": " 
        	+ selRecData.substring(FieldStart[13], FieldStart[14])//MTRNO 
        	+ "  " + FieldName[9]  + ": " 
        	+ selRecData.substring(FieldStart[8] , FieldStart[9]));//CON_TY;	
        	
        	listresult.add("COL/ROW: " + selRecData.substring(FieldStart[14], FieldStart[15])//COLUMN  
            + " / " + selRecData.substring(FieldStart[15], FieldStart[16])//ROW	
        	+ "  " + FieldName[3]  + ": " + selRecData.substring(FieldStart[2] , FieldStart[3]));//SERNO



        	//listresult.add(FieldName[15] + ": " + selRecData.substring(FieldStart[14], FieldStart[15]) 
        	//+ "  " + FieldName[16] + ": " + selRecData.substring(FieldStart[15], FieldStart[16])) ; //ROW ;	//COLUMN 	

        	//FieldValue[first++] = FieldName[16] + ": " + selRecData.substring(FieldStart[15], FieldStart[16]);	//ROW
        	//FieldValue[first++] = FieldName[9]  + ": " + selRecData.substring(FieldStart[8] , FieldStart[9]) ;	//CON_TY
        	
        	avg_cons  = selRecData.substring(FieldStart[11],FieldStart[12]);	//AVG_CONS 
        	prev_rdng = selRecData.substring(FieldStart[16],FieldStart[17]);	//PR_RDG
        	curr_rdng = selRecData.substring(FieldStart[34],FieldStart[35]);	//C_RDG
        	if(avg_cons.trim()!=null && avg_cons.trim().length()!=0   ){avg_consInt= Integer.parseInt(avg_cons.trim());}
        	if(prev_rdng.trim()!=null && prev_rdng.trim().length()!=0   ){prev_rdngInt=Integer.parseInt(prev_rdng.trim());}
        	if(curr_rdng.trim()!=null && curr_rdng.trim().length()!=0   ){curr_rdngInt=Integer.parseInt(curr_rdng.trim());}
        	
        	listresult.add(FieldName[17] + ": " + prev_rdng); //PR_RDG
        	listresult.add(FieldName[35] + ": " + curr_rdng); //C_RDG

        	listresult.add(FieldName[36] + ": " + selRecData.substring(FieldStart[35],FieldStart[36])//MTR_STAT2
           	+ "  " + FieldName[37] + ": "  + selRecData.substring(FieldStart[36],FieldStart[37]));//SUB_STAT2 
        		       	    	                 	
        	listresult.add(FieldName[1]  + ": " + selRecData.substring(FieldStart[0] , FieldStart[1])//CYNO
        	+ "  " + FieldName[2]  + ": " + selRecData.substring(FieldStart[1] , FieldStart[2]));// BKNO
        	
        	listresult.add(FieldName[40] + ": " + selRecData.substring(FieldStart[39],FieldStart[40])//ZER0_FLAG  
            + "  " + FieldName[41] + ": " + selRecData.substring(FieldStart[40],FieldStart[41]));//OCCUP_FLAG
        	
        	listresult.add(FieldName[44] + ": " + selRecData.substring(FieldStart[43], FieldStart[44])//MTR_RDR 
                   	+ "  " + FieldName[12] + ": " + avg_cons);	//AVG_CONS
                	
        	listresult.add(FieldName[20] + ": " + selRecData.substring(FieldStart[19],FieldStart[20]));//MSG_TO_MR
        	
        	listresult.add(FieldName[4] + ": " + selRecData.substring(FieldStart[3],FieldStart[4]));//NAME
        	
        	listresult.add(FieldName[5] + ": " + selRecData.substring(FieldStart[4],FieldStart[5]));//ADD1
        	
        	listresult.add(FieldName[6] + ": " + selRecData.substring(FieldStart[5],FieldStart[6]));//ADD2
        	
        	listresult.add(FieldName[8] + ": " + selRecData.substring(FieldStart[7],FieldStart[8]));//DIRECTION
        	
           	//FieldValue[first++] = FieldName[41] + ": " + selRecData.substring(FieldStart[40],FieldStart[41]);	//OCCUP_FLAG	
        }
        else {
        		// display Selected record out of range
        }
        mlistresult=listresult;
}
	

    public static List<StringBuffer> readFile(File file) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        //listSB= new ArrayList<StringBuffer>();
        List<StringBuffer> mlistSB = new ArrayList<StringBuffer>();
        int count=0;
        try {
          
        	fis = new FileInputStream(file);


          // Here BufferedInputStream is added for fast reading.
          bis = new BufferedInputStream(fis);
          dis = new DataInputStream(bis);

          // dis.available() returns 0 if the file does not have more lines.
          while (dis.available() != 0) {

          CharSequence s="                                                                                                                                                                                 ";
			// this statement reads the line from the file and print it to
          // the console.
        	  mlistSB.add(count, new StringBuffer(497).append(dis.readLine()).append(s)); 
          // since download file is 319 char long and expanded upload file is 496 char long
        	  count++;
        	  //if (dis.available() != 0) {
        		//  sb.append("\n");
        	  //}
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
        //origSplitFile=sb.toString().split("\n");
        NumofRecords=count;
        return mlistSB;
    }
}

