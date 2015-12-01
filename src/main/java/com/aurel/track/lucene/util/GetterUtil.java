/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.lucene.util;

import java.text.DateFormat;
import java.util.Date;

/**
 * <a href="GetterUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 3938 $
 *
 */
public class GetterUtil {

    public static boolean getBoolean(String value) {
        return getBoolean(value, false);
    }

    public static boolean getBoolean(String value, boolean defaultValue) {
        return get(value, defaultValue);
    }

    public static Date getDate(String value, DateFormat df) {
        return getDate(value, df, new Date());
    }

    public static Date getDate(String value, DateFormat df, Date defaultValue) {
        return get(value, df, defaultValue);
    }

    public static double getDouble(String value) {
        return getDouble(value, 0.0);
    }

    public static double getDouble(String value, double defaultValue) {
        return get(value, defaultValue);
    }

    public static float getFloat(String value) {
        return getFloat(value, 0);
    }

    public static float getFloat(String value, float defaultValue) {
        return get(value, defaultValue);
    }

    public static int getInteger(String value) {
        return getInteger(value, 0);
    }

    public static int getInteger(String value, int defaultValue) {
        return get(value, defaultValue);
    }

    public static long getLong(String value) {
        return getLong(value, 0);
    }

    public static long getLong(String value, long defaultValue) {
        return get(value, defaultValue);
    }

    public static short getShort(String value) {
        return getShort(value, (short)0);
    }

    public static short getShort(String value, short defaultValue) {
        return get(value, defaultValue);
    }

    public static String getString(String value) {
        return getString(value, StringPool.BLANK);
    }

    public static String getString(String value, String defaultValue) {
        return get(value, defaultValue);
    }

    public static boolean get(String value, boolean defaultValue) {
        if (Validator.isNotNull(value)) {
            try {
                value = value.trim();

                if (value.equalsIgnoreCase(_BOOLEANS[0]) ||
                    value.equalsIgnoreCase(_BOOLEANS[1]) ||
                    value.equalsIgnoreCase(_BOOLEANS[2]) ||
                    value.equalsIgnoreCase(_BOOLEANS[3]) ||
                    value.equalsIgnoreCase(_BOOLEANS[4])) {

                    return true;
                }
                else {
                    return false;
                }
            }
            catch (Exception e) {
            }
        }

        return defaultValue;
    }

    public static Date get(String value, DateFormat df, Date defaultValue) {
        try {
            Date date = df.parse(value.trim());

            if (date != null) {
                return date;
            }
        }
        catch (Exception e) {
        }

        return defaultValue;
    }

    public static double get(String value, double defaultValue) {
        try {
            return Double.parseDouble(_trim(value));
        }
        catch (Exception e) {
        }

        return defaultValue;
    }

    public static float get(String value, float defaultValue) {
        try {
            return Float.parseFloat(_trim(value));
        }
        catch (Exception e) {
        }

        return defaultValue;
    }

    public static int get(String value, int defaultValue) {
        try {
            return Integer.parseInt(_trim(value));
        }
        catch (Exception e) {
        }

        return defaultValue;
    }

    public static long get(String value, long defaultValue) {
        try {
            return Long.parseLong(_trim(value));
        }
        catch (Exception e) {
        }

        return defaultValue;
    }

    public static short get(String value, short defaultValue) {
        try {
            return Short.parseShort(_trim(value));
        }
        catch (Exception e) {
        }

        return defaultValue;
    }

    public static String get(String value, String defaultValue) {
        if (Validator.isNotNull(value)) {
            value = value.trim();
            value = StringUtil.replace(value, "\r\n", "\n");

            return value;
        }

        return defaultValue;
    }

    private static String _trim(String value) {
        if (value != null) {
            value = value.trim();

            StringBuffer sb = new StringBuffer();

            char[] charArray = value.toCharArray();

            for (int i = 0; i < charArray.length; i++) {
                if ((Character.isDigit(charArray[i])) ||
                    (charArray[i] == '-' && i == 0) ||
                    (charArray[i] == '.')) {

                    sb.append(charArray[i]);
                }
            }

            value = sb.toString();
        }

        return value;
    }

    private static String[] _BOOLEANS = {"true", "t", "y", "on", "1"};

}
