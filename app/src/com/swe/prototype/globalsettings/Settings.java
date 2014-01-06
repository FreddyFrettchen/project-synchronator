package com.swe.prototype.globalsettings;

/*
 * Autor: Dany
 * 
 * Hab diese Klasse jetzt mal zur zentralen speicherung von einstellungsdaten erstellt.
 * 
 */
public class Settings {
	private static String prefs_name = "SynchronatorPrefs";
	private static String server = "http://tranquil-ravine-8657.herokuapp.com";
	private static String ip = "http://tranquil-ravine-8657.herokuapp.com";
	private static String port = "80";

	// edit mark
	// auf sekunden umgestellt
	private static int refreshTime = 6 * 60 * 60; // default 6 Stunden

	public static String getPrefs_name() {
		return prefs_name;
	}

	public static void setPrefs_name(String prefs_name) {
		Settings.prefs_name = prefs_name;
	}

	public static String getServer() {
		return getIp() + ":" + getPort();
	}

	public static String getIp() {
		return ip;
	}

	public static String getPort() {
		return port;
	}

	/*
	 * hab server in ip und port augeteilt, weil man die teilweise einzeln
	 * braucht. man kann nur ueber setServer(ip,port) diese 3 werte veraendern,
	 * garantiert konsistenz.
	 */
	public static void setServer(String ip, String port) {
		Settings.server = "http://" + ip + ":" + port;
		Settings.ip = ip;
		Settings.port = port;
	}

	public static int getRefreshTimeAsInt() {
		return refreshTime;
	}

	public static void setRefreshTimeAsInt(int refreshTime) {
		Settings.refreshTime = refreshTime;
	}

	public static float getRefreshTimeAsFloat() {
		return refreshTime / 3600;
	}

	public static void setRefreshTimeAsFloat(float refreshTime) {
		Settings.refreshTime = (int) refreshTime * 3600;
	}
}
