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

import java.net.InetAddress;
import java.net.URL;



class UrlTester
{

	UrlTester()
    {
    }

    public static void main(String args[])
    {
    	System.err.println("UrlTester (C) 2007 Trackplus");
        if (args.length < 1) {
        	printUsage();
        	return;
        }
        String theUrl = args[0];
        URL url = null;
        try {
        	url = new URL(theUrl);
        }
        catch (Exception e) {
        	System.err.println(e.getMessage());
        }
        
        if (url == null) {
        	System.err.println("Could not create URL. Check syntax.");
        }

        // Now try to get the IP address of that host
        InetAddress inetAd = null;
        try {
        	inetAd = InetAddress.getByName(url.getHost());
        }
        catch (Exception e) {
        	System.err.println("An exception occurred trying to get "
        			+ "the IP address: " + e.getMessage());
        }
        if (inetAd != null) {
        	System.out.println("Could retrieve IP address...");
        	System.out.println("Host address = " + inetAd.getHostAddress());
        	System.out.println("Host name = " + inetAd.getHostName());
        }       
        
        System.out.println("Now trying to retrieve content from URL...");
        try {
        	System.out.println("Could retrieve content from URL");
        }
        catch (Exception ue){
        	ue.printStackTrace();
        }
        
    }
    
    private static void printUsage() {
    	System.err.println("Usage: UrlTester <theUrl>" );
     	System.err.println("");    	
    }
}

