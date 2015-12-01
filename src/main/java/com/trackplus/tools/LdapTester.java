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


package com.trackplus.tools;

import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

class LdapTester
{

    LdapTester()
    {
    }

    public static void main(String args[])
    {
    	System.err.println("LdapTester (C) 2009 Trackplus");
        if (args.length < 3) {
        	System.err.println("Usage: LdapTester <ldap-url> <search attribute> <search-string>" );
        	System.err.println("Example 1: LdapTester ldap://ldapserver/ou=People,o=org cn \"john doe\"");
        	System.err.println("Example 2: LdapTester ldap://ldapserver/ou=People,o=org uid u2345");
        	System.err.println("");
        	return;
        }
        System.out.println("Please wait while trying to look up " + args[2] + " in LDAP directory...");
        Hashtable env = new Hashtable(5);
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", args[0]);
        String attribute = args[1];
        String attrStr = args[2];
        
        try
        {
            DirContext ctx = new InitialDirContext(env);
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(2);
            String searchStr = "(" + attribute + "=" + attrStr + ")";
            NamingEnumeration answer = ctx.search("", searchStr, ctls);
            if(answer.hasMore())
            {
                SearchResult sr = (SearchResult)answer.next();
                String keyDn = sr.getName() + "," + ctx.getNameInNamespace();
                System.out.println("dn: " + keyDn);
                Attributes attrs = sr.getAttributes();
                String uidAttr = (String)attrs.get(attribute).get();
                if(uidAttr != null)
                    System.out.println(attribute + ": " + uidAttr);
                else
                    System.out.println(attribute + ": <not found!>");
                answer.close();
            } else
            {
                System.out.println("no entry found for LDAP-search >" + searchStr + "<!");
            }
            ctx.close();
        }
        catch(NamingException e)
        {
            System.out.println("get common name (CN) for >" + attribute + "=" + attrStr + "< failed with " + e);
        }
    }
}
