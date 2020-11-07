package greycirclegames;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import spark.Response;

/**
 * Most methods in this class modify input variable Response.
 */
public class CookieHandler {
	private static final long MAX_AGE = TimeUnit.HOURS.toMillis(8); 
    
    public static Response setCookie(Response rs, String key, String value) {
    	setCookieHelper(rs, key, value, MAX_AGE);
    	return rs;
    }

	public static Response setCookie(Response rs, String key, int value) {
    	return setCookie(rs, key, String.valueOf(value));
    }
    
    public static Response setCookie(Response rs, String key, boolean value) {
    	return setCookie(rs, key, String.valueOf(value));
    }

    /**
     * The way to remove a cookie is call Set-Cookie for a cookie with a date in the past.
     * So this can be weird, depending on browser, if you "remove" aka add "Set-Cookie" header for the same cookie twice in the same request/response cycle.
     */
	public static Response removeCookie(Response rs, String key) {
		setCookieHelper(rs, key, "", -1);
		return rs;
	}

	/**
	 * Since remove is the same as a call to adding a "Set-Cookie" header only remove it if one hasn't already been set on this request/response cycle.
	 */
	public static void removeCookieIfNotSet(Response rs, String key) {
		Collection<String> list = rs.raw()
				.getHeaders("Set-Cookie");
		if (list
				.stream()
				.noneMatch(headerValue -> headerValue.startsWith(key))) {
			removeCookie(rs, key);
		}
	}
	
	private static Response setCookieHelper(Response rs, String key, String value, long maxAge) {
		String expirationString = "";
		if (maxAge > 0) {
			expirationString = "Max-Age=" + String.valueOf(maxAge) + ";";
		} else {
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			calendar.add(Calendar.MONTH, -1);
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss zzz");
			dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			expirationString = "Expires=" + dateFormatter.format(calendar.getTime()) + ";";
		}
		if (value == null || value.equals("")) {
			value = key;
		}
		rs.header("Set-Cookie", key + "=" + value + "; " + expirationString + " SameSite=Strict; Secure");
		return rs;
	}
    
}
