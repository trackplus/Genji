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


package com.aurel.track.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

import com.aurel.track.StartServlet;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.lucene.LuceneUtil;


public class PluginUtils {

	private static final Log LOGGER = LogFactory.getLog(PluginUtils.class);

	/****************************Methods which search for classes extending a superclass/interface in a specific package**********************
	/*
	 * The following five methods which search for classes extending a superclass/interface in a specific package
	 * Warning: this methods doesn't find all subclasses if the package
	 * is scattered over more jar files or it is defined in both the /classes folder and jars in classpath.
	 * It finds only the first such package unit contained in a jar or the /classes folder,
	 * (the first package unit, depends on the classloader of the application server,
	 * so it could vary from app. server to app. server)
	 * and ignores all other package units which can be found in the classpath.
	 * This methods are fast because the search in just one package
	 * and it doesn't matter whether the package is in the /classes folder or in a jar file from classpath
	 * But you should be sure that this package is not scattered over more places!!!
	 */

	/**
	 * Get the names of all the classes extending (implementing) a given
	 * class (interface) in the currently loaded packages
	 * Warning: it does not finds all classes if a package
	 * is scattered over more jar files or it is found in both classes and jars!
	 * @param superClass the class to inherit from
	 */
	/*public static List findSubclassInAllPackages(Class superClass, ServletContext servletContext) {
		List classes = new ArrayList();
		List classesInPackage = new ArrayList();
		Package [] pcks = Package.getPackages();
		for (int i=0;i<pcks.length;i++) {
			classesInPackage = findSubclassInPackage(pcks[i].getName(), superClass, servletContext);
			if (classesInPackage!=null) {
				classes.addAll(classesInPackage);
			}
		}
		return classes;
	}*/

	/**
	 * Get the names of all the classes extending (implementing) a given
	 * class (interface) in the currently loaded packages (classes and jars).
	 * Warning: it does not finds all classes if a package
	 * is scattered over more jar files or it is found in both classes and jars!
	 * @param superClassName the name of the class to inherit from
	 */
	/*public static List findSubclassesInAllPackages(String superClassName, ServletContext servletContext)
	{
		List classes = new ArrayList();
		Class superClass = null;
		try {
			superClass = Class.forName(superClassName);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Superclass not found " + e.getMessage());
			return classes;
		}
		if (superClass!=null)
		{
			classes = findSubclassInAllPackages(superClass, servletContext);
		}
		return classes;
	}*/

	/**
	 * Get the names of all the classes extending (implementing) a given
	 * class (interface) in a given package (classes and jars).
	 * Warning: it does not finds all classes if a package
	 * is scattered over more jar files or it is found in both classes and jars!
	 * The first package URL wins!
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the fully qualified name of the class to inherit from
	 */
	/*public static List findSubclassInPackage(Package thePackage, Class theSuperClass, ServletContext servletContext) {
		return findSubclassInPackage(thePackage.getName(), theSuperClass, servletContext);
	}*/

	/**
	 * Get the names of all the classes extending (implementing) a given
	 * class (interface) in a given package.
	 * Warning: it does not finds all classes if a package
	 * is scattered over more jar files or it is found in both classes and jars!
	 * The first package URL wins!
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the name of the class to inherit from
	 */
	/*public static List findSubclassInPackage(String pckname, String tosubclassname, ServletContext servletContext) {
	List classes = new ArrayList();
	try {
		Class tosubclass = Class.forName(tosubclassname);
		classes = findSubclassInPackage(pckname, tosubclass, servletContext);
	} catch (ClassNotFoundException e) {
		LOGGER.error("Superclass not found " + e.getMessage());
	}
		return classes;
	}*/

	/**
	 * Get the names of all the classes inheriting or implementing a given
	 * class in a given package.
	 * Warning: it does not finds all classes if a package
	 * is scattered over more jar files or it is found in both classes and jars!
	 * The first package URL wins!
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the Class object to inherit from
	 */
	/*public static List findSubclassInPackage(String pckgname, Class tosubclass, ServletContext servletContext) {
		List classes = new ArrayList();

		//get the package directory
		//try through class.getResource (should work for most app servers)
		File directory = getPackageFileThroughClassFromClasses(pckgname);
		if (directory==null)
		{
			//try through servletContext (for not expanded .wars, for example by weblogic)
			directory = getPackageFileThroughServletContextFromClasses(servletContext, pckgname);
		}
		if (directory!=null)
		{
			// Get the list of the files contained in the package
			String [] files = directory.list();
			for (int i=0;i<files.length;i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					String classname = files[i].substring(0,files[i].length()-6);
					try {
						// Try to create an instance of the object
						Class foundClass = Class.forName(pckgname + "." + classname);
						Object o = foundClass.newInstance();
						if (tosubclass.isInstance(o)) {
							classes.add(foundClass.getName());
						}
					}
					catch (ClassNotFoundException cnfex)
					{}
					catch (InstantiationException iex) {
					// We try to instanciate an interface
					// or an object that does not have a
					// default constructor
					} catch (IllegalAccessException iaex) {
						// The class is not public
					}
				}
			}
		} else {
			try {
				// It does not work with the filesystem: we must
				// be in the case of a package contained in a jar file.
				//it works for expanded .wars
				URL url = getPackageURLFromJars(pckgname);
				if (url!=null) {
					URLConnection urlConn = url.openConnection();
					if (urlConn instanceof JarURLConnection) {
						JarURLConnection conn = (JarURLConnection)urlConn;
						String starts = conn.getEntryName();
						JarFile jfile = conn.getJarFile();
						Enumeration e = jfile.entries();
						while (e.hasMoreElements()) {
							ZipEntry entry = (ZipEntry)e.nextElement();
							String entryname = entry.getName();
							if (entryname.startsWith(starts)
							&&(entryname.lastIndexOf('/')<=starts.length())
							&&entryname.endsWith(".class")) {
							String classname = entryname.substring(0,entryname.length()-6);
							if (classname.startsWith("/"))
								classname = classname.substring(1);
							classname = classname.replace('/','.');
							try {
								// Try to create an instance of the object
								Object o = Class.forName(classname).newInstance();
								if (tosubclass.isInstance(o)) {
									classes.add(classname);
								}
							} catch (ClassNotFoundException cnfex) {
								LOGGER.error(cnfex);
							} catch (InstantiationException iex) {
								// We try to instanciate an interface
								// or an object that does not have a
								// default constructor
							} catch (IllegalAccessException iaex) {
								// The class is not public
							}
							}
						}
					} else {
						//By BEA Weblogic urlConn will be of type wblogic.utils.zip.ZipURLConnection,
						// but we do not have this class in the classpath, so we try to get the jar file explicit and search the jar file
						//
						String jarURLString = url.toString();
						if (jarURLString!=null)
						{
							jarURLString = jarURLString.substring(0, jarURLString.lastIndexOf(".jar") + 4);
							File urlFile = getFileFromURL(new URL(jarURLString));
							classes = getSubclassesFromJarInLib(urlFile, tosubclass, null, null);
						}
					}
				}
			}
			catch (Exception ex) {
				LOGGER.error("Finding a class in a package in a jar failed with " + ex.getMessage());
			}
		}
		return classes;
	}*/



	/******************************Helper classes************************************************/

	/**
	 * Gets the file containing a package
	 * class in a given package.
	 * Warning: it does not finds all classes if a package containing the class
	 * is scattered over more jar files or it is found in both classes and jars!
	 * The first package URL wins!
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the Class object to inherit from
	 */
	/*private static File getPackageFileThroughClassFromClasses(String pckgname) {
		// Translate the package name into an absolute path
		String name = new String(pckgname);
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		name = name.replace('.','/');
		URL url = PluginUtils.class.getResource(name);
		return getFileFromURL(url);
	}*/

	/**
	 * Gets the file containing a package
	 * class in a given package.
	 * Warning: it does not finds all classes if a package containing the class
	 * is scattered over more jar files or it is found in both classes and jars!
	 * The first package URL wins!
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the Class object to inherit from
	 */
	/*private static File getPackageFileThroughServletContextFromClasses(ServletContext servletContext, String pckgname) {
		// Translate the package name into an absolute path
		String name = new String(pckgname);
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		name = name.replace('.','/');
		URL url = null;
		try {
			url = servletContext.getResource("/WEB-INF/classes" + name);
		} catch (MalformedURLException e) {
		}
		return getFileFromURL(url);
	}*/

	/**
	 * Gets the file containing a package
	 * class in a given package.
	 * Warning: it does not finds all classes if a package containing the class
	 * is scattered over more jar files or it is found in both classes and jars!
	 * The first package URL wins!
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the Class object to inherit from
	 */
	/*private static URL getPackageURLFromJars(String pckgname) {
		// Translate the package name into an absolute path
		String name = new String(pckgname);
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		name = name.replace('.','/');
		return PluginUtils.class.getResource(name);
	}*/

	/**
	 * Gets a file with a given path relativ to classes directory from the WEB-INF
	 * @param url
	 * @return
	 */
	public static File getFileFromURL(URL url) {
		File file;
		if (url==null) {
			return null;
		}
		if (url.getPath()!=null) {
			file = new File(url.getPath());
			if (file.exists()) {
				//valid url
				return file;
			}
		}

		//valid file through URI?
		//see Bug ID:  4466485 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4466485)
		URI uri = null;
		try
		{
			//get rid of spaces
			uri = new URI(url.toString());
		}
		catch (URISyntaxException e1){
		}
		if (uri == null)
		{
			return null;
		}
		if (uri.getPath()!=null) {
			file = new File(uri.getPath());
			if (file.exists()) {
				return file;
			}
		}
		//back to URL from URI
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
		}
		if (url!=null && url.getPath()!=null) {
			file = new File(url.getPath());
			if (file.exists()) {
				//valid url
				return file;
			}
		}
		return null;
	}

	/**
	 * Transforms the given URL which may contain escaping characters (like %20 for space)
	 * to an URL which may conatin illegal URL characters (like space) but is valid for creating a file based on the resulting URL
	 * The escape characters will be changed back to characters that are illegal in URLs (like space) because the file could be
	 * constructed just in such a way in some servlet containers like Tomcat5.5
	 * @param url
	 * @return
	 */
	public static URL createValidFileURL(URL url) {
		File file;
		URL fileUrl;
		if (url==null) {
			return null;
		}
		if (url.getPath()!=null) {
			file = new File(url.getPath());
			//we have no escape characters or the servlet container can deal with escape characters
			if (file.exists()) {
				//valid url
				return url;
			}
		}
		//valid file through URI?
		//see Bug ID:  4466485 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4466485)
		URI uri = null;
		try {
			//get rid of spaces
			uri = new URI(url.toString());
		}
		catch (URISyntaxException e1){
		}
		if (uri == null) {
			return null;
		}
		if (uri.getPath()!=null) {
			//the decoded path component of this URI
			file = new File(uri.getPath());
			if (file.exists()) {
				try {
					//file.toURL() does not automatically escape characters that are illegal in URLs
					fileUrl = file.toURL(); // revert, problems with Windows?
					if (fileUrl!=null) {
						return fileUrl;
					}
				} catch (MalformedURLException e) {
				}
			}
		}
		//back to URL from URI
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
		}
		if (url!=null && url.getPath()!=null) {
			file = new File(url.getPath());
			if (file.exists()) {
				//valid url
				return url;
			}
		}
		return null;
	}

	/**
	 * Methods which search for classes extending a superclass/interface in a specific jar file
	 * It does not deals with packages, is searches the entire content of the jar
	 */

	/**
	 * Gets the names of all the subclasses from a jar which extend (implement) a superclass (interface).
	 * It does not deal with packages, it tries to find all such classes within the jar
	 * Sometimes it is not enough to find the classes in a package because:
	 * 	1.	The subclasses are not necessary in the same package as the superclass
	 * 	2.	When the same package exists in two or more jar files then only the first jar is found (by URL)
	 * @param file the jar file
	 * @param superclass
     * @param constructorClasses
     * @param constructorParameters
	 * @return
	 */
	private static List<String> getSubclassesFromJarInLib(File file, Class superclass,
			Class[] constructorClasses, Object[] constructorParameters) {
		List<String> classes = new ArrayList<String>();
		Object o;
		if (file == null || !file.exists() || superclass == null) {
			return classes;
		}
		JarFile jfile = null;
		try {
			jfile = new JarFile(file);
		} catch (IOException e1) {
		}
		if (jfile!=null) {
			LOGGER.debug("Searching in " + file.getName());
			try {
				Enumeration e = jfile.entries();
				while (e.hasMoreElements()) {
					ZipEntry entry = (ZipEntry)e.nextElement();
					String entryname = entry.getName();
					if (//entryname.startsWith(starts) &&
							//(entryname.lastIndexOf('/')<=starts.length()) &&
							entryname.endsWith(".class")) {
						String classname = entryname.substring(0,entryname.length()-6);
						if (classname.startsWith("/")) {
							classname = classname.substring(1);
						}
						classname = classname.replace('/','.');
						try {
							// Try to create an instance of the object
							Class c = null;
							try {
								c = Class.forName(classname);
							} catch (Exception classByName) {
								LOGGER.debug("Finding a class by name " + classname + " failed with " + classByName);
							}
							if (c!=null) {
								o = null;
								if (constructorClasses==null || constructorClasses.length==0) {
									//default constructor
									o = c.newInstance();
								} else {
									//probably lucene analyzers with Version
									Constructor ct = null;
									try {
										ct = c.getConstructor(constructorClasses);
									} catch (Exception getConst) {
										LOGGER.debug(getConst);
									}
									if (ct==null) {
										//older analyzers (lucene<3)
										try {
											//default constructor. Some analyzers use default constructor even in lucene 3.0
											//(although the corresponding javadoc states it with Version parameter)
											o = c.newInstance();
										} catch (Exception exception) {
										}
									} else {
										try {
											if (ct!=null) {
												o = ct.newInstance(constructorParameters);
											}
										} catch (Exception callConst) {
										}
									}
								}
								if (o !=null && superclass.isInstance(o)) {
									classes.add(classname);
									LOGGER.debug("Found analizer: " + classname);
								}
							}
						} catch (InstantiationException iex) {
							// We try to instanciate an interface
							// or an object that does not have a
							// default constructor, ignore
						} catch (IllegalAccessException iaex) {
							// The class is not public, ignore
						} catch (Exception ex) {
							LOGGER.warn("Finding a class in a jar failed with exception " + ex.getMessage());
						}
					}
				}
			} catch (Exception t) {
				LOGGER.warn("Finding a class in a jar failed with throwable " + t.getMessage());
			}
		}
		return classes;
	}

	/**
	 * Gets the files from a directory which satisfy a FileFilter
	 * @param directory
	 * @param filter
	 * @return
	 */
	public static File[] getFilesFromDir(File directory, FilenameFilter filter) {
		File[] arrFiles = new File[0];
		if (directory != null && directory.exists() && directory.isDirectory()) {
			arrFiles = directory.listFiles(filter);
		}
		return arrFiles;
	}

	/**
	 * Helper class for filtering the files
	 * @author Tamas Ruff
	 *
	 */
	static class FileFilterByStartAndEnd implements FilenameFilter {
		private String startsWith;
		//default is ".jar"
		private String endsWith = ".jar";

		/**
		 * @return Returns the endsWith.
		 */
		public String getEndsWith() {
			return endsWith;
		}
		/**
		 * @param endsWith The endsWith to set.
		 */
		public void setEndsWith(String endsWith) {
			this.endsWith = endsWith;
		}
		/**
		 * @return Returns the startsWith.
		 */
		public String getStartsWith() {
			return startsWith;
		}
		/**
		 * @param startsWith The startsWith to set.
		 */
		public void setStartsWith(String startsWith) {
			this.startsWith = startsWith;
		}

		/**
		 * accept a file when starts and ends accordingly
		 */
		@Override
		public boolean accept(File file, String fileName) {
			/*String absolutPath = file.getAbsolutePath();
			int index = absolutPath.lastIndexOf(File.separator);
			if (index!=-1 && index<absolutPath.length())
			{
				String fileName = file.getAbsolutePath().substring(index+1).toLowerCase();*/
				if (startsWith!=null && endsWith!=null)
				{
					return fileName.startsWith(startsWith) && fileName.endsWith(endsWith);
				}
				else
				{
					//return all jars
					return true;
				}
			/*}
			return false;*/
		}
	}

	/**
	 * Gets the classes from the specified classResourcePath and the specified servletContextResourcePath
	 * which are in jars beginning with and ending with  which extend/implement a class
	 * @param servletContext
	 * @param superClass
	 * @param jarStartsWith
	 * @param jarEndsWith
	 * @param classResourcePath
	 * @param servletContextResourcePath
	 * @return
	 */
	private static Set<String> getClassesFromLibJars(ServletContext servletContext,
			Class superClass, String jarStartsWith, String jarEndsWith,
			String classResourcePath, String servletContextResourcePath,
			Class[] constructorClasses, Object[] constructorParameters) {
		Set<String> foundAnalyzersSet = new TreeSet<String>();
		try {
			File lib = PluginUtils.getFileFromURL(PluginUtils.class.getResource(classResourcePath));
			//define the file filter
			FileFilterByStartAndEnd filter = new FileFilterByStartAndEnd();
			filter.setStartsWith(jarStartsWith);
			filter.setEndsWith(jarEndsWith);

			//get the analyzers from the jars
			File[] foundJars = PluginUtils.getFilesFromDir(lib, filter);
			if (foundJars!=null) {
				for (int i=0;i<foundJars.length;i++) {
					foundAnalyzersSet.addAll(
							PluginUtils.getSubclassesFromJarInLib(foundJars[i], superClass, constructorClasses, constructorParameters));
				}
			}
			//if .jars not yet found - probably because we use unexpaned .war (for ex. by weblogic) - try to get the resources through the servlet context
			if (foundJars == null || foundJars.length==0) {
				URL urlLib = null;
				try {
					urlLib = servletContext.getResource(servletContextResourcePath);
				} catch (MalformedURLException e) {
					LOGGER.error("Getting the URL through getServletContext().getResource(path) failed with " + e.getMessage());
				}
				lib = PluginUtils.getFileFromURL(urlLib);
				foundJars = PluginUtils.getFilesFromDir(lib, filter);
				if (foundJars!=null) {
					for (int i=0;i<foundJars.length;i++) {
						foundAnalyzersSet.addAll(PluginUtils.getSubclassesFromJarInLib(
							foundJars[i], superClass, constructorClasses, constructorParameters));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Getting the other lucene analysers failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		return foundAnalyzersSet;
	}

	/**
	 * Search the lucene analyzer implementations from the /WEB-INF/lib
	 * The search will take place only in jars with the following naming pattern  "lucene<xxxx>.jar"
	 * @param servletContext
	 * @return
	 */
	public static Set<String> getAnalyzersFromJars(ServletContext servletContext)	{
		return getClassesFromLibJars(servletContext, Analyzer.class, "lucene",
				".jar", "/../lib/", "/WEB-INF/lib", /*new Class[] {Version.class}, new Object[]{LuceneUtil.VERSION}*/null, null);
	}

	/**
	 * Search the event subscriber implementations from the jars from the /WEB-INF/lib
	 * The search will take place in jars with the following naming pattern  "eventSubscriber<xxxx>.jar" and
	 * as an extra in the jar named _wl_cls_gen.jar because Weblogic 10 packs all the classes
	 * directory in this jar in the temporary location of the expanded war file
	 * @param servletContext
	 * @return
	 */
	/*public static Set getEventSubscribersFromJars(ServletContext servletContext) {
		Set eventSubscribers = new TreeSet();
		eventSubscribers.addAll(getClassesFromLibJars(servletContext, IEventSubscriber.class, "_wl_cls_gen", ".jar", "/../lib/", "/WEB-INF/lib", null, null));
		eventSubscribers.addAll(getClassesFromLibJars(servletContext, IEventSubscriber.class, "eventSubscriber", ".jar", "/../lib/", "/WEB-INF/lib", null, null));
		return eventSubscribers;
	}*/


	/**
	 * Gets a file (or directory) with a specific name from the root of the web application
	 * @param servletContext
	 * @param fileName
	 * @return
	 */
	public static File getResourceFileFromWebAppRoot(ServletContext servletContext, String fileName) {
		URL urlDest;
		File file = null;
		//first try to get the template dir through class.getResource(path)
		urlDest = PluginUtils.class.getResource("/../../" + fileName);
		urlDest = createValidFileURL(urlDest);
		if (urlDest!=null) {
			file = new File(urlDest.getPath());
		}
		//second try to get the template dir through servletContext.getResource(path)
		if ((file==null || !file.exists()) && servletContext!=null) {
			try {
				urlDest = servletContext.getResource("/" + fileName);
				urlDest = createValidFileURL(urlDest);
				if (urlDest!=null) {
					file = new File(urlDest.getPath());
				}
			} catch (MalformedURLException e) {
				LOGGER.error("Getting the URL through getServletContext().getResource(path) failed with " + e.getMessage());
			}
		}
		//third try to get the template dir through servletContext.getRealPath(path)
		if ((file==null || !file.exists()) && servletContext!=null) {
			String path;
			//does not work for unexpanded .war
			path = servletContext.getRealPath(File.separator) + fileName;
			file = new File(path);
		}
		return file;
	}

	private static final Class[] parameters = new Class[]{URL.class};

	/**
	 * Add class folders and jar files in the lib directories for all plugins.
	 * @param tpHome the Genji home directory which includes the plugin directory
	 * @return
	 */
	public static void addPluginLocationsToClassPath(String tpHome) {
		File[] pluginDirs = new File[0];
		File directory = new File(tpHome+File.separator+HandleHome.PLUGINS_DIR);
		File resources = new File(tpHome+File.separator+HandleHome.XRESOURCES_DIR);
		File logos = new File(tpHome+File.separator+HandleHome.LOGOS_DIR);
		if (directory != null && directory.exists() && directory.isDirectory()) {
			pluginDirs = directory.listFiles();
		} else {
			return;
		}
		// Expand Genji plugins first
		for (File f : pluginDirs) {
			if (!f.isDirectory() && f.getName().endsWith(".tpx")) {
				String targetDir = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 4);
				File td = new File(targetDir);
				if (!td.exists() || (td.lastModified() < f.lastModified()) || !td.isDirectory()) {
					try {
					unzipFileIntoDirectory(f, directory);
					} catch (Exception e) {
						LOGGER.error("Problem unzipping archive: " + e.getMessage());
					}
				}
			}
		}
		pluginDirs = directory.listFiles();
		// Now process the directories in <TRACKPLUS_HOME>/plugins
		ArrayList<File> files = new ArrayList<File>();
		ArrayList<File> jsdirs = new ArrayList<File>();
		ArrayList<String> bundles = new ArrayList<String>();

		for (File f : pluginDirs) {
			if (f != null && f.exists() && f.isDirectory()) {
				files.add(f);
				File classes = new File(f.getAbsolutePath()+File.separator+"classes");
				if (classes != null && classes.exists() && classes.isDirectory()) {
					files.add(classes);
				}
				File libs = new File(f.getAbsolutePath()+File.separator+"lib");
				File[] jars = new File[0];
				if (libs != null && libs.exists() && libs.isDirectory()) {
					jars = libs.listFiles();
				}

				for (File fj  : jars) {
					if (fj.exists() && !fj.isDirectory() && fj.getAbsolutePath().endsWith(".jar")) {
						files.add(fj);
					}
				}

				File conf = new File(f.getAbsolutePath()+File.separator+"conf");
				if (conf != null && conf.exists() && conf.isDirectory()) {
					files.add(conf);
				}

				File js = new File(f.getAbsolutePath()+File.separator+"js");
				if (js != null && js.exists() && js.isDirectory()) {
					jsdirs.add(f);
				}

				bundles.add("resources."+f.getName());
			}
		}
		URLClassLoader sysloader = null;
		try {
			sysloader = (URLClassLoader) StartServlet.class.getClassLoader();

			Class sysclass = URLClassLoader.class;
			for (File file: files) {
				try {
					LOGGER.info("Adding " + file.getAbsolutePath() + " to classpath.");
					Method method = sysclass.getDeclaredMethod("addURL", parameters);
					method.setAccessible(true);
					method.invoke(sysloader, new Object[]{file.toURI().toURL()});
				} catch (Exception t) {
					LOGGER.error(ExceptionUtils.getStackTrace(t));
				}
			}

			try {
				LOGGER.info("Trying to add " + resources.getAbsolutePath() + " to classpath.");
				Method method = sysclass.getDeclaredMethod("addURL", parameters);
				method.setAccessible(true);
				method.invoke(sysloader, new Object[]{resources.toURI().toURL()});
			} catch (Exception t) {
				LOGGER.info("No custom resources found, okay.");
			}

			try {
				LOGGER.info("Trying to add " + logos.getAbsolutePath() + " to classpath.");
				Method method = sysclass.getDeclaredMethod("addURL", parameters);
				method.setAccessible(true);
				method.invoke(sysloader, new Object[]{logos.toURI().toURL()});
			} catch (Exception t) {
				LOGGER.info("No custom logos found, okay.");
			}
		} catch (Exception e) {
			LOGGER.warn("URLClassloader not supported. You have to add your plugins to the Java classpath manually" );
		}
		setJavaScriptExtensionDirs(jsdirs);
		setBundles(bundles);

		return;
	}

	private static ArrayList<File> extJavaScriptDirs = null;

	private static synchronized void setJavaScriptExtensionDirs(ArrayList<File> jsdirs) {
		extJavaScriptDirs = jsdirs;
	}

	private static ArrayList<String> bundles = null;

	private static void setBundles(ArrayList<String> bund) {
		bundles = bund;
	}

	public static ArrayList<String> getBundles() {
		return bundles;
	}

	/**
	 * Returns a list with directories from <TRACKPLUS_HOME>/plugins/<pluginXY>/js
	 * containing JavaScript extensions for plugins. The classes there are added
	 * to the ExtJS namespace path as "pluginXY":path for dynamic loading.
	 * @return
	 */
	public static List<File> getJavaScriptExtensionDirs() {
		return extJavaScriptDirs;
	}

	/**
	 * Unzip this file into the given directory. If the zipFile is called "zipFile.zip",
	 * the files will be placed into "targetDir/zipFile".
	 * @param zipFile
	 * @param targetDir
	 */
	public static void unzipFileIntoDirectory(File zipFile, File targetDir) {
		try {
			LOGGER.debug("Expanding Genji extension file " + zipFile.getName());
			int BUFFER = 2048;
			File file = zipFile;
			ZipFile zip = new ZipFile(file);
			String newPath = targetDir+File.separator+zipFile.getName().substring(0, zipFile.getName().length() - 4);
			new File(newPath).mkdir();
			Enumeration zipFileEntries = zip.entries();
			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(newPath, currentEntry);
				//destFile = new File(newPath, destFile.getName());
				File destinationParent = destFile.getParentFile();
				// create the parent directory structure if needed
				destinationParent.mkdirs();
				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];
					// write the current file to disk
					LOGGER.debug ("Unzipping " + destFile.getAbsolutePath());
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos,BUFFER);
					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			}
		} catch(Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static void extractArchiveFromClasspath(String dirInClasspath, String filter, File toDir) {
		URL urlDest;
		File directory = null;
		//first try to get the template dir through class.getResource(path)
		urlDest = PluginUtils.class.getResource(dirInClasspath); // example: "/plugins"
		urlDest = PluginUtils.createValidFileURL(urlDest);
		if (urlDest!=null)
		{
			LOGGER.info("Retrieving archive from " + urlDest.toString());
			directory = new File(urlDest.getPath());
			Long uuid = new Date().getTime();
			File tmpDir = new File(System.getProperty("java.io.tmpdir")+File.separator+"TrackTmp"+ uuid.toString());
			if (!tmpDir.isDirectory()) {
				tmpDir.mkdir();
			}
			tmpDir.deleteOnExit();

			File[] files = null;

			if (filter != null && !"".equals(filter)) {
				files = directory.listFiles(new PluginUtils.Filter(filter));
			} else {
				files = directory.listFiles();
			}

			if (files == null || files.length == 0) {
				LOGGER.info("Problem extracting archive: No files.");
				return;
			}
			for (int index = 0; index < files.length; index++)
			{
				File zipFile = null;
				try {
					zipFile = files[index];
					LOGGER.info("Extracting archive from " + files[index].getName());
					unzipFileIntoDirectory(zipFile, toDir);
				} catch (Exception e) {
					LOGGER.error("Problem unzipping archive " + files[index].getName());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}


	static class Filter implements FileFilter
	{

		// String patternString = ".*http://.*";

        private Pattern pattern = null;

		public Filter(String filter) {
	        this.pattern = Pattern.compile(filter);
		}

		@Override
		public boolean accept(File file)
		{
	        Matcher matcher = pattern.matcher(file.getName());
	        return matcher.matches();
		}
	}
}
