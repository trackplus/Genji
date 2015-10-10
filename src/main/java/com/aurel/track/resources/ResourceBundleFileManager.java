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

package com.aurel.track.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * A resource manager class for ResourceBundle 
 * implementation based on property files 
 * not present in the classpath which can't be instantiated 
 * with the ResourceBundle.getBundle(resourceName, locale); 
 * (for example near the report templates) 
 */
public class ResourceBundleFileManager {
    private static Map resourceBundleCache = new HashMap();
    private static String makeKey(String bundleName,Locale locale){
        return bundleName+"_"+locale.getLanguage().toLowerCase()+"_"+locale.getCountry().toUpperCase();
    }
    public static ResourceBundle getBundle(String bundleName, Locale locale){
        String key=makeKey(bundleName,locale);
        ResourceBundle rb=(ResourceBundle)resourceBundleCache.get(key);
        if(rb!=null){
            return rb;
        }
        Properties prop=new Properties();
        File f=new File(key+".properties");
        if(f.exists()){
            try {
                prop.load(new FileInputStream(f));
            } catch (IOException e) {}
        }else{
            File f1=new File(bundleName+"_"+locale.getLanguage().toLowerCase()+".properties");
            if(f1.exists()){
                try {
                    prop.load(new FileInputStream(f1));
                } catch (IOException e) {}
            }else{
                File f2=new File(bundleName+".properties");
                if(f2.exists()){
                    try {
                        prop.load(new FileInputStream(f2));
                    } catch (IOException e) {}
                }
            }
        }
        rb=new ResourceBundleProperties(prop);
        resourceBundleCache.put(key,rb);
        return rb;
    }
}
