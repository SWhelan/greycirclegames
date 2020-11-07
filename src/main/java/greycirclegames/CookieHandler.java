package greycirclegames;

import spark.Response;

public class CookieHandler {
    
    public static Response setCookie(Response rs, String key, String value) {
    	rs.header("Set-Cookie", key + "=" + value + "; Max-Age=888000; SameSite=Strict; Secure");
    	return rs;
    }
    
    public static Response setCookie(Response rs, String key, int value) {
    	return setCookie(rs, key, String.valueOf(value));
    }
    
    public static Response setCookie(Response rs, String key, boolean value) {
    	return setCookie(rs, key, String.valueOf(value));
    }

	public static Response removeCookie(Response rs, String key) {
		return rs;
	}
    
}
