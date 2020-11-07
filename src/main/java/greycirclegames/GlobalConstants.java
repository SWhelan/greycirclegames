package greycirclegames;

public class GlobalConstants {
	
	//Templates
	public static final String DISPLAY_ERROR = "failure";
	public static final String DISPLAY_SUCCESS = "success";
	public static final String USER_COOKIE_KEY = "id";
	public static final String VERIFY_COOKIE_KEY = "verify";
	public static final String FRIEND_SEARCH_COOKIE_KEY = "friendSearchValue";
	public static final String DEV_MODE = "DEV_MODE";
	
	//Games
	public static final String KINGS_CORNER = "King's Corner";
	public static final String CIRCLES = "Circles";
    
	
    //Colors
    public class COLOR {
        public static final String WHITE = "white";
        public static final String BLACK = "black";
    }
    
    
    //Game-specific
    public static final int KING = 13;
    public static final int MAX_PLAYERS = 6;
    public static final int INITIAL_NUM_CARDS = 7;
    
    public static final int CIRCLES_ROWS = 8;
    public static final int CIRCLES_COLUMNS = 8;
    
    // Cookies
    public static final int DEFAULT_COOKIE_MAX_AGE = 60; // 1 minute in seconds
    public static final boolean DEFAULT_COOKIE_SECURE_SETTING = true;
}
