package data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.text.format.DateFormat;

public class DateGenerator
{
	/**
	 * generate a new date string based on the users timezone.
	 * 
	 * @return
	 */
	public String generateDate()
	{
		Calendar currentDateCal = Calendar.getInstance();
		currentDateCal.setTimeZone(TimeZone.getDefault());

		Date date = currentDateCal.getTime();

		return DateFormat.format("dd.MM.yyyy, kk:mm", date).toString();
	}
}
