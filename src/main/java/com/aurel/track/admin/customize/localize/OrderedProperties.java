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

package com.aurel.track.admin.customize.localize;

import java.util.*;


/**
 * This is an extension of Java Properties that stores the properties alphabetically
 *
 * @author Brian Pipa - http://pipasoft.com
 * @version 1.0
 */
public class OrderedProperties extends Properties {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Overrides, called by the store method.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public synchronized Enumeration keys() {
		Enumeration keysEnum = super.keys();
		Vector keyList = new Vector();
		while(keysEnum.hasMoreElements()){
			keyList.add(keysEnum.nextElement());
		}
		Collections.sort(keyList);
		return keyList.elements();
	}

	/**
	 * Overrides the original store() method
	 *
	 * @param out a FileOutPutStream to send the output to
	 * @param header a textual header for the top of the file
	 * @exception IOException when things go wrong
	 */	
	/*public void store(OutputStream out, String header)
		throws IOException {

		//write the header
		DataOutputStream dataoutputstream = new DataOutputStream(out);
		dataoutputstream.writeBytes("#" + header + "\n");

		//write the date/time
		Date now = new Date();
		dataoutputstream.writeBytes("#" + now + "\n");

		Enumeration propertyNames = keys();
		
		//now, loop through and write out the properties
		String oneline;
		String thekey;
		String thevalue;
		
		
		while (propertyNames.hasMoreElements()) {
			thekey = (String)propertyNames.nextElement();
			thevalue = (String) this.getProperty(thekey);
			thevalue = doubleSlash(thevalue);

			oneline = thekey + "=" + thevalue + "\n";
			dataoutputstream.writeBytes(oneline);
		}

		dataoutputstream.flush();
		dataoutputstream.close();
	}
*/
	/**
	 * Private method to double slash paths
	 *
	 * @param orig the string to double slash
	 * @return a double-slashed string
	 */
	/*private String doubleSlash(String orig) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < orig.length(); i++) {
			if (orig.charAt(i) == '\\') {
				buf.append("\\\\");
			} else {
				buf.append(orig.charAt(i));
			}
		}

		return buf.toString();
	}*/
}
