package com.tests;

import java.util.Hashtable;

public class TestSangi {
	public static void main(String[] args) {
		final String UploadPageName = "";
		final String Word = "Column";
		com.pega.pegahc.Exceltools.ExcelReader er;
		Hashtable inMap = new java.util.Hashtable();
		Hashtable tMap = new java.util.Hashtable();
		String propStr;
		String propValueStr;
		tMap.put("", "String");
		tMap.put("", "Strin");
		int y=0;
		java.util.Enumeration k = tMap.keys();
		while(key.hasMoreElements())
		{
			propStr = (String)k.nextElement();
			propValueStr = tools.findpage().getproperty(propStr+Word).getStringValue().trim();
			if(!propValueStr.equals(""))
			{
				inMap.put(propStr+Word, propValueStr);
			}
		}
		String pFileName = "";
		String pFullFileName = tools.findpage("Declare_SPM_CNFG").getProperty(".Value(CS_UPLOAD_RESPOSITORY")).getStringValue()+java.io.File.separatorChar + pFileName;
		//String pFullFileName = FullFileName;
		String pSheetName = "WorkSheetName";
		String ClassName = "";
		int StartRow = StartRow;
		String dateFormat = "yyyyMMdd";
		java.io.InputStream prIS = null;
		oLog.error("pFullFileName is  " +pFullFileName);
		oLog.error("pSheetName is  " +pSheetName);
		oLog.error("pFileName is  " +pFileName);
		oLog.error("pClassName is  " +pClassName);
		try
		{
			Byte[] b = Base64Util.decodeToByteArray(filedata);
			prIS = (java.io.InputStream)new java io ByteArrayInputStream(b);
		}
		Catch(exception e)
		{
			oLog.error(“Error reading excel  ” +e.toString());
			nextBlock = “Err”;
		}
		er = new com.pega.pegahc.exceltools.ExcelReader (prIS, pSheetName, pStartRow, pClkassName, inMap, tMap, dateFormat, true);
		oLog.error (“prIS is ” +prIS);
		oLog.error (“er is ” + er);
		oLog.error (“er number is  ” + er.getNumberofRecords());
		if(er.getNumberOfRecords()<500)
		{
			Java.util.Vector Census = er.getCensus();
			oLog.error (“Get Census  ” + Census);
			oLog.error (“Get Cens  ” +Census);
			oLog.error (“after get census rows  ” + Census.size());
			if(Census ! = null)
			{
				HashStringMap akeys = new HashStringMap ();
				oLog.error (“Census size  ” +Census.Size());
				akeys.putString(“pyActivityName” + “”);
				for(int Z=0; z<Census.size();z++)
				{
					Tools.doActivity(aKeys,null,null);
				}
			}
			oLog.error (“Census is  ” +Census);
			if (Census !=null)
			{
				For(int x=0; x<Census.size();x++)
				{
					Java.util.Hashtable record = (java.util.Hashtable) Census.elementAt(x);
					Java.util.Enumeration e = record.keys();
					While(e.hasMoreElements())
					{
						propStr = (String) e.nextElement;
						oLog.error (“propStr  ” + propStr);
						ClipboardPage temp = tools.findpage (“     .Member(“ + (x+1)+ “)”);
						Temp.getProperty(propStr).SetValue(String)record.get(propStr);
						oLog.error(“PropStr Val ” + (String)record.get(propStr));
						if (propStr.equals (“Desired Column”))
						{
							String SubNumber = temp.getString(“Desired Column”);
						}
					}
				}
			}
		}
	}
}
ClipboardPage Profile = tools.findpage();
String msg = “”;
If(er.getNumberOfRecords()==0)
{
	Msg += “<br> No valid records <br>”;
	Profile.getProperty(“MessageStep”).SetValue(msg);
}
}




}
}
