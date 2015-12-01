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


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1229 $
 *
 */
public class FileUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(FileUtil.class);

    public static void copyDirectory(
        String sourceDirName, String destinationDirName) {

        copyDirectory(new File(sourceDirName), new File(destinationDirName));
    }

    public static void copyDirectory(File source, File destination) {
        if (source.exists() && source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            File[] fileArray = source.listFiles();

            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory()) {
                    copyDirectory(
                        fileArray[i],
                        new File(destination.getPath() + File.separator
                            + fileArray[i].getName()));
                }
                else {
                    copyFile(
                        fileArray[i],
                        new File(destination.getPath() + File.separator
                            + fileArray[i].getName()));
                }
            }
        }
    }

    public static void copyFile(
        String sourceFileName, String destinationFileName) {

        copyFile(new File(sourceFileName), new File(destinationFileName));
    }

    /**
     * Copies a source file to a destination file
     * @param source
     * @param destination
     */
    public static void copyFile(File source, File destination) {
        if (!source.exists()) {
            return;
        }

        if ((destination.getParentFile() != null) &&
            (!destination.getParentFile().exists())) {

            destination.getParentFile().mkdirs();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        
        try {
            srcChannel = new FileInputStream(source).getChannel();
            dstChannel = new FileOutputStream(destination).getChannel();

            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

            srcChannel.close();
            dstChannel.close();
        }
        catch (IOException ioe) {
            LOGGER.error(ExceptionUtils.getStackTrace(ioe));
        }
        finally {
        	if (srcChannel != null) try {srcChannel.close();} catch (IOException ioe) {};
        	if (dstChannel != null) try {dstChannel.close();} catch (IOException ioe) {};
        }
    }

    public static void copyFileLazy(String source, String destination)
        throws IOException {

        String oldContent = null;
        try {
            oldContent = FileUtil.read(source);
        }
        catch (FileNotFoundException fnfe) {
            return;
        }

        String newContent = null;
        try {
            newContent = FileUtil.read(destination);
        }
        catch (FileNotFoundException fnfe) {
        }

        if (oldContent == null || !oldContent.equals(newContent)) {
            FileUtil.copyFile(source, destination);
        }
    }

    public static void deltree(String directory) {
        deltree(new File(directory));
    }

    public static void deltree(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] fileArray = directory.listFiles();

            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory()) {
                    deltree(fileArray[i]);
                }
                else {
                    fileArray[i].delete();
                }
            }

            directory.delete();
        }
    }

    public static byte[] getBytes(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(file);

        int c = in.read();

        while (c != -1) {
            out.write(c);
            c = in.read();
        }

        in.close();
        out.close();

        return out.toByteArray();
    }

    public static String getPath(String fullFileName) {
        int pos = fullFileName.lastIndexOf("/");

        if (pos == -1) {
            pos = fullFileName.lastIndexOf("\\");
        }

        String shortFileName = fullFileName.substring(0, pos);

        if (Validator.isNull(shortFileName)) {
            return "/";
        }

        return shortFileName;
    }

    public static String getShortFileName(String fullFileName) {
        int pos = fullFileName.lastIndexOf("/");

        if (pos == -1) {
            pos = fullFileName.lastIndexOf("\\");
        }

        String shortFileName =
            fullFileName.substring(pos + 1, fullFileName.length());

        return shortFileName;
    }

    public static boolean exists(String fileName) {
        File file = new File(fileName);

        return file.exists();
    }

    public static String[] listDirs(String fileName)  {
        return listDirs(new File(fileName));
    }

    public static String[] listDirs(File file)  {
        List<String> dirs = new ArrayList<String>();

        File[] fileArray = file.listFiles();

        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isDirectory()) {
                dirs.add(fileArray[i].getName());
            }
        }

        return (String[])dirs.toArray(new String[0]);
    }

    public static String[] listFiles(String fileName) {
        return listFiles(new File(fileName));
    }

    public static String[] listFiles(File file) {
        List<String> files = new ArrayList<String>();

        File[] fileArray = file.listFiles();

        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                files.add(fileArray[i].getName());
            }
        }

        return (String[])files.toArray(new String[0]);
    }

    public static void mkdirs(String pathName) {
        File file = new File(pathName);
        file.mkdirs();
    }

    public static boolean move(
        String sourceFileName, String destinationFileName) {

        return move(new File(sourceFileName), new File(destinationFileName));
    }

    public static boolean move(File source, File destination) {
        if (!source.exists()) {
            return false;
        }

        destination.delete();

        return source.renameTo(destination);
    }

    public static String read(String fileName) throws IOException {
        return read(new File(fileName));
    }

    public static String read(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuffer sb = new StringBuffer();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }

        br.close();

        return sb.toString().trim();
    }

    public static File[] sortFiles(File[] files) {
        Arrays.sort(files, new FileComparator());

        List<File> directoryList = new ArrayList<File>();
        List<File> fileList = new ArrayList<File>();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                directoryList.add(files[i]);
            }
            else {
                fileList.add(files[i]);
            }
        }

        directoryList.addAll(fileList);

        return (File[])directoryList.toArray(new File[0]);
    }

    public static String replaceSeparator(String fileName) {
        return StringUtil.replace(fileName, '\\', "/");
    }

    public static List<String> toList(Reader reader) {
        List<String> list = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(reader);

            String line = null;

            while ((line = br.readLine()) != null) {
                list.add(line);
            }

            br.close();
        }
        catch (IOException ioe) {
        }

        return list;
    }

    public static List<String> toList(String fileName) {
        try {
            return toList(new FileReader(fileName));
        }
        catch (IOException ioe) {
            return new ArrayList<String>();
        }
    }

    public static Properties toProperties(FileInputStream fis) {
        Properties props = new Properties();

        try {
            props.load(fis);
        }
        catch (IOException ioe) {
        }

        return props;
    }

    public static Properties toProperties(String fileName) {
        try {
            return toProperties(new FileInputStream(fileName));
        }
        catch (IOException ioe) {
            return new Properties();
        }
    }

    public static void write(String fileName, String s) throws IOException {
        write(new File(fileName), s);
    }

    public static void write(String fileName, String s, boolean lazy)
        throws IOException {

        write(new File(fileName), s, lazy);
    }

    public static void write(String pathName, String fileName, String s)
        throws IOException {

        write(new File(pathName, fileName), s);
    }

    public static void write(
            String pathName, String fileName, String s, boolean lazy)
        throws IOException {

        write(new File(pathName, fileName), s, lazy);
    }

    public static void write(File file, String s) throws IOException {
        write(file, s, false);
    }

    public static void write(File file, String s, boolean lazy)
        throws IOException {

        if (file.getParent() != null) {
            mkdirs(file.getParent());
        }

        if (file.exists()) {
            String content = FileUtil.read(file);

            if (content.equals(s)) {
                return;
            }
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        bw.flush();
        bw.write(s);

        bw.close();
    }
    
    public static void zipFiles(File dirZip, ZipOutputStream zipOut) {
    	zipFiles(dirZip, zipOut,dirZip.getAbsolutePath());
    }
    
    private static void zipFiles(File dirZip, ZipOutputStream zipOut,
			String rootPath) {
		try {
			// get a listing of the directory content
			File[] dirList = dirZip.listFiles();
			byte[] readBuffer = new byte[2156];
			int bytesIn;
			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = dirList[i];
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					String filePath = f.getAbsolutePath();
					zipFiles(new File(filePath), zipOut, rootPath);
					// loop again
					continue;
				}
				// if we reached here, the File object f was not a directory
				// create a FileInputStream on top of f
				FileInputStream fis = new FileInputStream(f);
				// create a new zip entry
				ZipEntry anEntry = new ZipEntry(f.getAbsolutePath().substring(
						rootPath.length()+1, f.getAbsolutePath().length()));
				// place the zip entry in the ZipOutputStream object
				zipOut.putNextEntry(anEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zipOut.write(readBuffer, 0, bytesIn);
				}
				// close the Stream
				fis.close();
			}

		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
    
    public static void unzipFile(File uploadZip, File unzipDestinationDirectory)throws IOException {
		if (!unzipDestinationDirectory.exists()) {
			unzipDestinationDirectory.mkdirs();
		}
		final int BUFFER = 2048;
		// Open Zip file for reading
		ZipFile zipFile = new ZipFile(uploadZip, ZipFile.OPEN_READ);

		// Create an enumeration of the entries in the zip file
		Enumeration zipFileEntries = zipFile.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(unzipDestinationDirectory, currentEntry);
			// grab file's parent directory structure
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			// extract file if not a directory
			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zipFile
						.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos,
						BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		}
		zipFile.close();
	}
}
