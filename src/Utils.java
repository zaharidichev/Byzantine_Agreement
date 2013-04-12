import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {
	
	public static String getTimeStamp() { 
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:S");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
