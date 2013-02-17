package de.ur.rk.uibuilder.helpers;

public class Log 
{
	private static final boolean DEBUGMODE = true;
	
	public static void d (String tag, String message)
	{
		if (DEBUGMODE)
		{
			android.util.Log.d(tag, message);
		}
	}
}
