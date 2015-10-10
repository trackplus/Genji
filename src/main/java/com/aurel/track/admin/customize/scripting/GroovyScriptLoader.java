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

package com.aurel.track.admin.customize.scripting;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.codehaus.groovy.control.CompilationFailedException;

import com.aurel.track.Constants;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.prop.ApplicationBean;

/**
 * 
 * This class loads all available scripts from the database and provides them
 * in a {@link GroovyClassLoader}. You can use it like this:
 * <p>
 * <code>
 *		&nbsp;&nbsp;GroovyClassLoader gloader = GroovyScriptLoader.getLoader();<br/>
 *		&nbsp;&nbsp;<br/>
 *		&nbsp;&nbsp;GroovyObject book = GroovyScriptLoader.newInstance("org.sampleproj.Book", gloader);<br/>
 *		&nbsp;&nbsp;book.setProperty("title", "Book's Title");<br/>
 *		&nbsp;&nbsp;book.setProperty("author", "Book's Author Name");<br/>
 *		&nbsp;&nbsp;book.setProperty("year", 2009);<br/>
 *<br/>
 *		&nbsp;&nbsp;GroovyObject shelf = newInstance("org.sampleproj.Shelf", gloader);<br/>
 *		&nbsp;&nbsp;shelf.invokeMethod("addBook", book);<br/>
 *		&nbsp;&nbsp;shelf.invokeMethod("printBooks", null); <br/> 
 * </code>
 * </p>
 * <p>
 * The Groovy script for "org.sampleproj.Book" would look like this:
 * </p>
 * <p>
 * <code>
 * package org.sampleproj;<br/>
 * <br/>
 * class Book {<br/>
 * &nbsp;&nbsp;String title;<br/>
 * &nbsp;&nbsp;String author;<br/>
 * &nbsp;&nbsp;Integer year;<br/>
 *<br/>
 * &nbsp;&nbsp;Book() {}<br/>
 *<br/>
 * &nbsp;&nbsp;Book(String title, String author, Integer year) {<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;	 this.title = title;<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;	 this.author = author;<br/>
 *  &nbsp;&nbsp;&nbsp;&nbsp;	 this.year = year;<br/>
 * &nbsp;&nbsp;}<br/>
 *<br/>
 * &nbsp;&nbsp;void printInfo() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;	  println "${title} | ${author} | ${year}";<br/>
 * &nbsp;&nbsp;}<br/>
 * } <br/>
 * </code>
 * </p>
 * The Groovy script for "org.sampleproj.Shelf" would look like this:
 * </p>
 * <p>
 * <code>
 * 
 * package org.sampleproj;<br/>
 *<br/>
 * class Shelf {<br/>
 * &nbsp;&nbsp;ArrayList<Book> books;<br/>
 *<br/>
 * &nbsp;&nbsp;Shelf() {<br/>
 *	&nbsp;&nbsp;&nbsp;&nbsp;   books = [];<br/>
 * &nbsp;&nbsp;}<br/>
 *<br/>
 * &nbsp;&nbsp;void addBook(Book book) {<br/>
 *	 &nbsp;&nbsp;&nbsp;&nbsp;  books += book;<br/>
 * &nbsp;&nbsp;}<br/>
 *<br/>
 * &nbsp;&nbsp;void printBooks() {<br/>
 *	&nbsp;&nbsp;&nbsp;&nbsp;   books.each {<br/>
 *		&nbsp;&nbsp;&nbsp;&nbsp;   &nbsp;&nbsp;&nbsp;&nbsp;	  println "${it.title} | ${it.author} | ${it.year}";<br/>
 *	 &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
 * &nbsp;&nbsp;}<br/>
 * } <br/>
 * </code>
 * </p>
 * @since 3.7.0
 */

public class GroovyScriptLoader {
	
	private static final Logger LOGGER = LogManager.getLogger(GroovyScriptLoader.class);
	private static GroovyScriptLoader ref = null;
	private GroovyClassLoader groovyClassLoader;
	private Map<String, Class<?>> availableClasses = new HashMap<String, Class<?>>();
	
	
	/**
	 * GroovyScriptLoader is a Singleton. This constructor can't be called.
	 */
	private GroovyScriptLoader() {
		// initially, load all scripts.
		groovyClassLoader = new GroovyClassLoader(getClass().getClassLoader());
		getGroovyScripts();
	}
	
	/**
	 * GroovyScriptLoader is a Singleton. This will create the single instance in case
	 * it does not yet exist, and will return it.
	 * 
	 */ 
	public static synchronized GroovyScriptLoader getInstance() {
		if (ref == null)
			// it's ok, we can call this constructor
			ref = new GroovyScriptLoader();		
		return ref;
	}
	
	/**
	 * Returns a {@link GroovyClassLoader} that contains all scripts from the database.
	 * This method is synchronized, so you can call scripts even though they are
	 * just being reloaded from the database.
	 */
	private void getGroovyScripts() {
		try {
			InputStream grooving = null;
			StringBuilder sb = new StringBuilder();
			try	{
				URL groovyURL = ApplicationBean.getApplicationBean().getServletContext().getResource("/WEB-INF/classes/plugins/groovy/system/DefaultActionHandler.groovy");
				grooving = groovyURL.openStream();
		        byte[] buffer = new byte[2048];
		        int length;
		        while ((length = grooving.read(buffer)) != -1) {
		            sb.append(new String(buffer, 0, length));
		        }
		        grooving.close();
			}
			catch (Exception exx) {
				LOGGER.warn("Could not load " + Constants.getGroovyURL() + "DefaultActionHandler.groovy. This is normal for the test environment.");
			}
			//get the sources from DB and parse them...
			Class<?> parsedClass = groovyClassLoader.parseClass(sb.toString());
			availableClasses.put("plugins.groovy.system.DefaultActionHandler", parsedClass);
			// class names to get on database...			
			List<TScriptsBean> scriptBeansList = ScriptAdminBL.getAllScripts();
			if (scriptBeansList!=null) {
				for (TScriptsBean scriptsBean : scriptBeansList) {
					Integer scriptType = scriptsBean.getScriptType();
					if (scriptType==null || !scriptType.equals(TScriptsBean.SCRIPT_TYPE.PARAMETER_SCRIPT)) {
						parseScript(scriptsBean.getClazzName(), scriptsBean.getSourceCode());
					}
				}
			}
		}
		catch (Exception ex) {
			LOGGER.warn("Problem loading Groovy scripts. This is normal for the test environment.");
		}
	}
	
	/**
	 * Parses a script
	 * @param clazzName
     * @param sourceCode
	 * @return
	 */
	public String parseScript(String clazzName, String sourceCode) {
		String compilationError = null;
		int lastIndexOf = clazzName.lastIndexOf(".");
		String codeBase = clazzName;
		if (lastIndexOf!=-1) {
			codeBase = clazzName.substring(0, lastIndexOf);
		}
		GroovyCodeSource groovyCodeSource = new GroovyCodeSource(
				sourceCode, clazzName, codeBase);
		try {
			Class<?> parsedClass = groovyClassLoader.parseClass(groovyCodeSource, false);
			availableClasses.put(clazzName, parsedClass);
		} catch (CompilationFailedException e) {
			compilationError = "Compilation for " + clazzName + " failed with " + e.getMessage();
			LOGGER.warn(compilationError);
		} catch (Exception ex) {
			compilationError = "Parsing class " + clazzName + " failed with " + ex.getMessage();
			LOGGER.warn(compilationError);
		}
		return compilationError;
	}
	
	/**
	 * This method reloads and compiles a single script.
	 */
	public synchronized void reloadScript(String clazzName, String sourceCode, String oldClassName) {
		try {
			String newClassName = clazzName;
			if (newClassName!=null && oldClassName!=null && !newClassName.equals(oldClassName)) {
				availableClasses.remove(oldClassName);
			}
			if(clazzName!=null){
				parseScript(clazzName, sourceCode);
			}
		}
		catch (Exception ex) {
			LOGGER.error("Problem compiling class " + clazzName + " " + ex.getMessage());
		}
	}
	
	/**
	 * This method reloads and compiles a single script.
	 */
	public synchronized void deleteScript(String className) {
		if (className!=null) {
			availableClasses.remove(className);
		}
	}
	
	/**
	 * Returns a {@link GroovyObject} of the given type, if it can be found.
	 * @param classname - the fully qualified classname for the object you want to create
	 * @param gloader - {@link GroovyClassLoader} to be used, and which has compiled this class
	 * <p/>
	 * @return the desired {@link GroovyObject}
	 * <p/>
	 * @throws Exception in case anything goes wrong
	 */
	public GroovyObject newInstance(String classname)
			throws Exception {
		return (GroovyObject) groovyClassLoader.loadClass(classname).newInstance();
	}
	
	
	/**
	 * A quick check if a Groovy class exists. This will look up the classname in 
	 * a hash map. It permits to flexibly work around any
	 * missing classes without having to search the database.
	 * @param gClassName - the fully qualified name of the class.
	 * <p/>
	 * @return true if this class exists
	 */
	public synchronized boolean doesGroovyClassExist(String gClassName) {
		return availableClasses.get(gClassName)!=null;
	}	
	
	public synchronized Class<?> getGroovyClass(String gClassName) {
		return availableClasses.get(gClassName);
	}
	
}
