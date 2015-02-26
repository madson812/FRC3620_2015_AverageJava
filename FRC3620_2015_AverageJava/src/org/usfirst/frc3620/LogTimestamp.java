package org.usfirst.frc3620;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTimestamp {
	private final static long SOME_TIME_AFTER_1970 = 523980000000L;

	private static String _timestampString = null;

	// http://javarevisited.blogspot.com/2014/05/double-checked-locking-on-singleton-in-java.html
	public static String getTimestampString() {
		if (_timestampString == null) { // Single Checked
			synchronized (LogTimestamp.class) {
				long now = System.currentTimeMillis();
				if (_timestampString == null) { // Double checked
					if (now > SOME_TIME_AFTER_1970) {
						SimpleDateFormat formatName = new SimpleDateFormat(
								"yyyyMMdd-HHmmss");
						_timestampString = formatName.format(new Date());
					}
				}
			}
		}
		return _timestampString;
	}

}