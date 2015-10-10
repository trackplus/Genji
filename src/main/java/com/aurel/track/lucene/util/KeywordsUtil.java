/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.lucene.util;


/**
 * <a href="KeywordsUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @author  Mirco Tamburini
 * @author  Josiah Goh
 * @version $Revision: 3938 $
 *
 */
public class KeywordsUtil {

    private static final String[] SPECIAL = new String[] {
        "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~",
        "*", "?", ":", "\\"
    };

    public static String escape(String text) {
        for (int i = SPECIAL.length - 1; i >= 0; i--) {
            text = StringUtil.replace(
                text, SPECIAL[i], StringPool.BACK_SLASH + SPECIAL[i]);
        }

        return text;
    }

    public static String toWildcard(String keywords) {
        if (keywords == null) {
            return null;
        }

        /*if (!keywords.startsWith(StringPool.STAR)) {
            keywords = StringPool.STAR + keywords;
        }*/

        if (!keywords.endsWith(StringPool.STAR)) {
            keywords = keywords + StringPool.STAR;
        }

        return keywords;
    }

}
