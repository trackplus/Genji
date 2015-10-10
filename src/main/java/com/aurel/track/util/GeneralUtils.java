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


package com.aurel.track.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;

public class GeneralUtils {
	private static final Logger LOGGER = LogManager.getLogger(GeneralUtils.class);
	public static int ITEMS_PRO_STATEMENT = 1000;


	/**
	* get the int values from a list of LabelValueBeans
	* @param labelValueBeanPersons
	* @return
	*/
	public static Integer[] getBeanIDs(List beanIDList) {
		Integer[] arrID = new Integer[0];
		if (beanIDList!=null && !beanIDList.isEmpty()) {
			arrID = new Integer[beanIDList.size()];
			for (int i=0;i<beanIDList.size();i++) {
				arrID[i] = ((IBeanID)beanIDList.get(i)).getObjectID();
			}
		}
		return arrID;
	}


	/**
	* Creates a map of beans from a list of beans which implement the IBeanID
	* key: the result of getObjectID()
	* value: the bean
	* @param list
	* @return
	*/
	public static Map createMapFromList(List list) {
		Map map = new HashMap();
		IBeanID beanID;
		if (list!=null) {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				beanID = (IBeanID) iterator.next();
				map.put(beanID.getObjectID(), beanID);
			}
		}
		return map;
	}

	/**
	* Creates a map of beans from a list of beans which implement the IBeanID
	* key: the result of getObjectID()
	* value: the bean
	* @param list
	* @return
	*/
	public static Map<Integer, String> createLabelMapFromList(List<ILabelBean> list) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		if (list!=null) {
			for (ILabelBean labelBean : list) {
				map.put(labelBean.getObjectID(), labelBean.getLabel());
			}
		}
		return map;
	}



	public static Map<String, ISerializableLabelBean> createUUIDMapFromSerializableBean(List<ISerializableLabelBean> serializableLabelBeanList) {
		Map<String, ISerializableLabelBean> uuidMap = new HashMap<String, ISerializableLabelBean>();
		if (serializableLabelBeanList!=null) {
			Iterator<ISerializableLabelBean> iterator = serializableLabelBeanList.iterator();
			while (iterator.hasNext()) {
				ISerializableLabelBean serializableLabelBean = iterator.next();
				String uuid = serializableLabelBean.getUuid();
				if (uuid!=null) {
					uuidMap.put(uuid, serializableLabelBean);
				} else {
					LOGGER.info("No uuid for serializableLabelBean with ID " + serializableLabelBean.getObjectID() +
							" and label " + serializableLabelBean.getLabel() + " of type " + serializableLabelBean.getClass().getName());
				}
			}
		}
		return uuidMap;
	}

	/**
	* Creates a sorted Set of Integers from an array of ints
	* @param arrIDs
	* @return
	*/
	public static Set<Integer> createSetFromIntArr(int[] arrIDs) {
		Set<Integer> setIDs = new TreeSet<Integer>();
		if (arrIDs!=null) {
			for (int i = 0; i < arrIDs.length; i++) {
				try {
					setIDs.add(Integer.valueOf(arrIDs[i]));
				} catch (Exception e) {
				}
			}
		}
		return setIDs;
	}

	/**
	* Creates a sorted Set of Integers from an array of ints
	* @param arrIDs
	* @return
	*/
	public static Set<Integer> createSetFromIntegerArr(Integer[] arrIDs) {
		Set<Integer> setIDs = new TreeSet<Integer>();
		if (arrIDs!=null) {
			for (int i = 0; i < arrIDs.length; i++) {
				if (arrIDs[i]!=null) {
					setIDs.add(arrIDs[i]);
				}
			}
		}
		return setIDs;
	}

	/**
	* Creates a Set of Integers from an List of integers
	* @param arrIDs
	* @return
	*/
	public static Set<Integer> createIntegerSetFromIntegerList(List<Integer> integerList) {
		Set<Integer> integerSet = new HashSet<Integer>();
		if (integerList!=null) {
			Iterator<Integer> iterator = integerList.iterator();
			while (iterator.hasNext()) {
				Integer integerValue = iterator.next();
				integerSet.add(integerValue);
			}
		}
		return integerSet;
	}

	/**
	* Creates a sorted Set of Integers from an array of ints
	* @param arrIDs
	* @return
	*/
	public static int[] createIntArrFromSet(Set<Integer> integersSet) {
		if (integersSet==null) {
			return null;
		}
		int[] intArrIDs = new int[integersSet.size()];
		Iterator<Integer> iterator = integersSet.iterator();
		int i=0;
		while (iterator.hasNext()) {
			intArrIDs[i++] = iterator.next().intValue();
		}
		return intArrIDs;
	}

	/**
	* Creates a List of Integers from an array of ints
	* @param arrIDs
	* @return
	*/
	public static List<Integer> createListFromIntArr(int[] arrIDs) {
		List<Integer> idsList = new LinkedList<Integer>();
		if (arrIDs!=null) {
			for (int i = 0; i < arrIDs.length; i++) {
				idsList.add(Integer.valueOf(arrIDs[i]));
			}
		}
		return idsList;
	}

	/**
	* Creates a list from a set
	* @param set
	* @return
	*/
	public static List createListFromSet(Set set) {
		List list = new ArrayList();
		if (set!=null) {
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
		}
		return list;
	}

	/**
	* Creates an Integer set from a IBeanID list
	* @param beanIDlist
	* @return
	*/
	public static Set<Integer> createIntegerSetFromBeanList(List beanIDlist) {
		Set<Integer> set = new HashSet<Integer>();
		if (beanIDlist!=null) {
			Iterator<IBeanID> iterator = beanIDlist.iterator();
			while (iterator.hasNext()) {
				IBeanID beanID = iterator.next();
				set.add(beanID.getObjectID());
			}
		}
		return set;
	}

	/**
	* Creates an Integer list from a IBeanID list
	* @param beanIDlist
	* @return
	*/
	public static List<Integer> createIntegerListFromBeanList(List beanIDlist) {
		List<Integer> idList = new LinkedList<Integer>();
		if (beanIDlist!=null) {
			Iterator<IBeanID> iterator = beanIDlist.iterator();
			while (iterator.hasNext()) {
				IBeanID beanID = iterator.next();
				idList.add(beanID.getObjectID());
			}
		}
		return idList;
	}

	/**
	* Creates a list of Integers from an array of Integers
	* @param arrIDs
	* @return
	*/
	public static List<Integer> createListFromIntArr(Integer[] arrIDs) {
		List<Integer> listIDs = new LinkedList<Integer>();
		if (arrIDs!=null) {
			for (int i = 0; i < arrIDs.length; i++) {
				Integer intValue = arrIDs[i];
				if (intValue!=null) {
					listIDs.add(intValue);
				}
			}
		}
		return listIDs;
	}

	/**
	* Creates an array of Integers from an Set of Integers
	* @param arrIDs
	* @return
	*/
	public static Integer[] createIntegerArrFromSet(Set<Integer> integerSet) {
		if (integerSet == null) {
			return null;
		}
		Integer[] intArr = new Integer[integerSet.size()];
		Iterator<Integer> iterator = integerSet.iterator();
		int i=0;
		while (iterator.hasNext()) {
			intArr[i++] = iterator.next();
		}
		return intArr;
	}

	/**
	* Creates an array of Integers from an Set of Integers
	* @param arrIDs
	* @return
	*/
	public static Integer[] createIntegerArrFromCollection(Collection<Integer> integerCollection) {
		if (integerCollection == null) {
			return null;
		}
		Integer[] intArr = new Integer[integerCollection.size()];
		Iterator<Integer> iterator = integerCollection.iterator();
		int i=0;
		while (iterator.hasNext()) {
			intArr[i++] = iterator.next();
		}
		return intArr;
	}

	/**
	* Creates an array of String from an Set of Integers
	* @param arrIDs
	* @return
	*/
	public static String[] createStringArrFromCollection(Collection<Integer> integerCollection) {
		if (integerCollection == null) {
			return null;
		}
		String[] strArr = new String[integerCollection.size()];
		Iterator<Integer> iterator = integerCollection.iterator();
		int i=0;
		Integer value=null;
		while (iterator.hasNext()) {
			value=iterator.next();
			strArr[i++] = (value==null?null:value.toString());
		}
		return strArr;
	}

	/**
	* Creates an array of Integers from an Set of Integers
	* @param arrIDs
	* @return
	*/
	public static List<Integer> createIntegerListFromCollection(Collection<Integer> integerCollection) {
		List<Integer> integerList = new ArrayList<Integer>();
		if (integerCollection != null) {
			integerList.addAll(integerCollection);
		}
		return integerList;
	}

	/**
	* Creates an array of Integers from an Set of Integers
	* @param arrIDs
	* @return
	*/
	public static List createListFromCollection(Collection collection) {
		List list = new ArrayList();
		if (collection != null) {
			list.addAll(collection);
		}
		return list;
	}

	/**
	 * Creates an array of ints from an List of Integers
	 * @param integerList
	 * @return
	 */
	public static int[] createIntArrFromIntegerList(List<Integer> integerList) {
		if (integerList == null) {
			return null;
		}
		int[] intArr = new int[integerList.size()];
		Iterator<Integer> iterator = integerList.iterator();
		int i=0;
		while (iterator.hasNext()) {
			try {
				Integer integer = iterator.next();
				if (integer!=null) {
					intArr[i++] = integer.intValue();
				}
			} catch (Exception e) {
				LOGGER.warn("Converting the list value to int failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return intArr;
	}

	/**
	 * Creates an array of ints from an List of Integers
	 * @param integerList
	 * @return
	 */
	public static int[] createIntArrFromIntegerCollection(Collection<Integer> integerList) {
		if (integerList == null) {
			return null;
		}
		int[] intArr = new int[integerList.size()];
		Iterator<Integer> iterator = integerList.iterator();
		int i=0;
		while (iterator.hasNext()) {
			try {
				Integer integer = iterator.next();
				if (integer!=null) {
					intArr[i++] = integer.intValue();
				}
			} catch (Exception e) {
				LOGGER.warn("Converting the list value to int failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return intArr;
	}

	/**
	* Creates an array of ints from an array of Integers
	* @param integerList
	* @return
	*/
	public static int[] createIntArrFromIntegerArr(Integer[] integerArr) {
		if (integerArr == null) {
			return null;
		}
		int[] intArr = new int[integerArr.length];
		for (int i = 0; i < integerArr.length; i++) {
			try {
				intArr[i] = integerArr[i].intValue();
			} catch (Exception e) {
				LOGGER.warn("Converting the " + i + "th element " + integerArr[i] + " to int value to int failed with");
			}
		}
		return intArr;
	}

	/**
	* Creates an array of ints from an array of Integers
	* @param integerList
	* @return
	*/
	public static Integer[] createIntegerArrFromIntArr(int[] intArr) {
		if (intArr == null) {
			return null;
		}
		Integer[] integerArr = new Integer[intArr.length];
		for (int i = 0; i < intArr.length; i++) {
			try {
				integerArr[i] = Integer.valueOf(intArr[i]);
			} catch (Exception e) {
				LOGGER.warn("Converting the " + i + "th element " + intArr[i] + " to Integer value to int failed with");
			}
		}
		return integerArr;
	}


	/**
	* Creates a set of Integers by splitting a comma separated string into integer parts
	* @param stringArr
	* @return
	*/
	public static Set<Integer> createIntegerSetFromStringSplit(String stringValue) {
		return createIntegerSetFromStringSplit(stringValue, ",");
	}

	/**
	* Creates a set of Integers by splitting a string into integer parts
	* @param stringValue
	* @param expr
	* @return
	*/
	public static Set<Integer> createIntegerSetFromStringSplit(String stringValue, String expr) {
		if (stringValue==null || "".equals(stringValue) || "null".equals(stringValue) || expr==null) {
			return null;
		}
		Set<Integer> integerSet = new HashSet<Integer>();
		String[] stringArr = stringValue.split(expr);
		if (stringArr!=null) {
			for (String stringPart : stringArr) {
				Integer integerValue = null;
				if (stringPart!=null && !"".equals(stringPart.trim())) {
					try {
						integerValue = Integer.valueOf(stringPart.trim());
						if (integerValue!=null) {
							integerSet.add(integerValue);
						}
					} catch (Exception e) {
						LOGGER.warn("Converting the " + stringPart + " to Integer failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		return integerSet;
	}

	/**
	* Creates a set of Integers by splitting a string into integer parts
	* @param stringValue
	* @param expr
	* @return
	*/
	public static Integer[] createIntegerArrFromCommaSeparatedString(String stringValue) {
		Set<Integer> integerSet = createIntegerSetFromStringSplit(stringValue, ",");
		return GeneralUtils.createIntegerArrFromSet(integerSet);
	}

	/**
	* Creates a set of Integers by splitting a string into integer parts
	* @param stringValue
	* @param expr
	* @return
	*/
	public static String createCommaSeparatedStringFromIntegerArr(Integer[] integerArr) {
		StringBuffer stringBuffer = new StringBuffer();
		if (integerArr!=null && integerArr.length>0) {
			for (int i = 0; i < integerArr.length; i++) {
				Integer objectID = integerArr[i];
				if (objectID!=null) {
					stringBuffer.append(objectID);
				}
				if (i < integerArr.length-1) {
					stringBuffer.append(", ");
				}
			}
		}
		return stringBuffer.toString();
	}
	public static List<Integer> createIntegerListFromString(String s) {
		return createIntegerListFromString(s,",");
	}
	public static List<Integer> createIntegerListFromString(String s,String separator) {
		if(s==null||s.trim().length()==0){
			return new ArrayList<Integer>();
		}
		String[] arr = s.split(separator);
		return GeneralUtils.createIntegerListFromStringArr(arr);
	}

	/**
	* Creates a list of Integers from an array of Strings
	* @param stringArr
	* @return
	*/
	public static List<Integer> createIntegerListFromStringArr(String[] stringArr) {
		if (stringArr == null || stringArr.length==0) {
			return null;
		}
		List<Integer> integerList = new LinkedList<Integer>();
		for (String strValue : stringArr) {
			Integer integerValue = null;
			if (strValue!=null && !"".equals(strValue.trim())) {
				try {
					integerValue = Integer.valueOf(strValue.trim());
				} catch (Exception e) {
					LOGGER.warn("Converting the " + strValue + " to Integer failed with " + e.getMessage(), e);
				}
			}
			if (integerValue!=null) {
				integerList.add(integerValue);
			}
		}
		return integerList;
	}

	public static List<Integer> createIntegerListFromIntArr(int[] intArr) {
		if (intArr == null) {
			return null;
		}
		List<Integer>  integerList = new LinkedList<Integer>();
		for (int i = 0; i < intArr.length; i++) {
			Integer integerValue = null;
			try {
				integerValue = Integer.valueOf(intArr[i]);
			} catch (Exception e) {
			}
			if (integerValue!=null) {
				integerList.add(integerValue);
			}
		}
		return integerList;
	}

	public static List<Integer> createIntegerListFromIntegerArr(Integer[] integerArr) {
		if (integerArr == null) {
			return null;
		}
		List<Integer> integerList = new LinkedList<Integer>();
		for (int i = 0; i < integerArr.length; i++) {
			if (integerArr[i]!=null) {
				integerList.add(integerArr[i]);
			}
		}
		return integerList;
	}

	/**
	* Creates an list of Integers from an array of Strings
	* @param stringArr
	* @return
	*/
	public static List<String> createStringListFromStringArr(String[] stringArr) {
		if (stringArr == null) {
			return null;
		}
		List<String> stringList = new ArrayList<String>();
		for (int i = 0; i < stringArr.length; i++) {
			String part = stringArr[i];
			if (part!=null && !"".equals(part)) {
				stringList.add(part);
			}
		}
		return stringList;
	}

	/**
	* Creates an list of Integers from an array of Strings
	* @param intArr
	* @return
	*/
	public static String[] createStringArrFromIntegerArr(Integer[] intArr) {
		if (intArr == null) {
			return null;
		}
		String[] stringArr = new String[intArr.length];
		for (int i = 0; i < intArr.length; i++) {
			if (intArr[i]!=null) {
				stringArr[i] = intArr[i].toString();
			}
		}
		return stringArr;
	}



	/**
	* Return a new map which inverts the keys with values
	* It will be correct only if the values are different
	* @param map
	* @return
	*/
	/*public static Map<Integer, Integer> getInvertedUniqueValuesMap(Map<Integer, Integer> map) {
		Map<Integer, Integer> invertedMap = new HashMap<Integer, Integer>();
		if (map!=null) {
			for (Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, Integer> mapEntry = iterator.next();
				if (mapEntry.getValue()!=null && mapEntry.getKey()!=null) {
					invertedMap.put(mapEntry.getValue(), mapEntry.getKey());
				}
			}
		}
		return invertedMap;
	}*/

	/**
	* Return a new map which inverts the keys with values
	* If a value might appear for more keys then in the inverted map the values should be a list
	* @param map
	* @return
	*/
	public static Map<Integer, List<Integer>> getInvertedDuplicatedValuesMap(Map<Integer, Integer> map) {
		Map<Integer, List<Integer>> invertedMap = new HashMap<Integer, List<Integer>>();
		if (map!=null) {
			for (Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, Integer> mapEntry = iterator.next();
				Integer key = mapEntry.getKey();
				Integer value = mapEntry.getValue();
				if (key!=null && value!=null) {
					List<Integer> keysList = invertedMap.get(value);
					if (keysList==null) {
						keysList = new ArrayList<Integer>();
						invertedMap.put(value, keysList);
					}
					keysList.add(key);
				}
			}
		}
		return invertedMap;
	}

	/**
	* Prepares a list of chunks to avoid the IN statement
	* limitations in some databases (for ex. Oracle)
	* @param arrInts
	* @return
	*/
	public static List<int[]> getListOfChunks(int[] arrInts) {
		List<int[]> chunksList = new ArrayList<int[]>();
		if (arrInts==null || arrInts.length==0) {
			return chunksList;
		}
		if (arrInts.length > GeneralUtils.ITEMS_PRO_STATEMENT) {
			int numberOfStatements = arrInts.length/GeneralUtils.ITEMS_PRO_STATEMENT;
			if (numberOfStatements*GeneralUtils.ITEMS_PRO_STATEMENT<arrInts.length) {
				numberOfStatements++;
			}
			int[] buffer;
			for (int i=0;i<numberOfStatements; i++) {
				if (i==numberOfStatements-1) {
					buffer = new int[arrInts.length - i*GeneralUtils.ITEMS_PRO_STATEMENT];
				} else {
					buffer = new int[GeneralUtils.ITEMS_PRO_STATEMENT];
				}
				for (int j = 0; j < buffer.length; j++) {
					buffer[j] = arrInts[i*GeneralUtils.ITEMS_PRO_STATEMENT+j];
				}
				chunksList.add(buffer);

			}
		} else {
			chunksList.add(arrInts);
		}
		return chunksList;
	}

	/**
	* Prepares a list of chunks to avoid the IN statement
	* limitations in some databases (for ex. Oracle)
	* @param objectIDs
	* @return
	*/
	public static List<int[]> getListOfChunks(Set<Integer> objectIDList) {
		if (objectIDList==null || objectIDList.isEmpty()) {
			return  new LinkedList<int[]>();
		}
		int[] arrInts = new int[objectIDList.size()];
		int i = 0;
		for (Integer intValue : objectIDList) {
			if (intValue!=null) {
				arrInts[i++] = intValue.intValue();
			}
		}
		return getListOfChunks(arrInts);
	}

	/**
	* Prepares a list of chunks to avoid the IN statement
	* limitations in some databases (for ex. Oracle)
	* @param objectIDs
	* @return
	*/
	public static List<int[]> getListOfChunks(List<Integer> objectIDList) {
		if (objectIDList==null || objectIDList.isEmpty()) {
			return  new LinkedList<int[]>();
		}
		int[] arrInts = new int[objectIDList.size()];
		for (int i = 0; i < objectIDList.size(); i++) {
			Integer intObj = objectIDList.get(i);
			if (intObj!=null) {
				arrInts[i] = intObj.intValue();
			}
		}
		return getListOfChunks(arrInts);
	}

	/**
	* Prepares a list of chunks to avoid the IN statement
	* limitations in some databases (for ex. Oracle)
	* @param objectIDs
	* @return
	*/
	public static List<int[]> getListOfChunks(Integer[] arrIntegers) {
		if (arrIntegers==null || arrIntegers.length==0) {
			return new LinkedList<int[]>();
		}
		int[] arrInts = new int[arrIntegers.length];
		for (int i = 0; i < arrIntegers.length; i++) {
			Integer intValue = arrIntegers[i];
			if (intValue!=null) {
				arrInts[i] = intValue.intValue();
			}
		}
		return getListOfChunks(arrInts);
	}

	/**
	* Prepares a list of chunks to avoid the IN statement
	* limitations in some databases (for ex. Oracle)
	* @param arrInts
	* @return
	*/
	public static List<List<String>> getListOfStringChunks(List<String> objects) {
		List<List<String>> chunksList = new ArrayList<List<String>>();
		if (objects==null || objects.isEmpty()) {
			return chunksList;
		}
		Integer size = objects.size();

		if (size > GeneralUtils.ITEMS_PRO_STATEMENT) {
			int numberOfStatements = size/GeneralUtils.ITEMS_PRO_STATEMENT;
			if (numberOfStatements*GeneralUtils.ITEMS_PRO_STATEMENT<size) {
				numberOfStatements++;
			}
			List<String> buffer;
			for (int i=0;i<numberOfStatements; i++) {
				buffer = new ArrayList<String>();
				int bufferSize;
				if (i==numberOfStatements-1) {
					bufferSize = size - i*GeneralUtils.ITEMS_PRO_STATEMENT;
				} else {
					bufferSize = GeneralUtils.ITEMS_PRO_STATEMENT;
				}
				for (int j = 0; j < bufferSize; j++) {
					buffer.add(objects.get(new Integer(i*GeneralUtils.ITEMS_PRO_STATEMENT+j)));
				}
				chunksList.add(buffer);
			}
		} else {
			chunksList.add(objects);
		}
		return chunksList;
	}

	/**
	 * Get the the subprojects form the project as a comma separated list
	 * @param projectID
	 * @return
	 */
	public static List<String> getCommaSepararedIDChunksInParenthesis(List<Integer> objectIDs) {
		List<String> stringArrays = new ArrayList<String>();
		List<int[]> chunks = getListOfChunks(objectIDs);
		for (Iterator<int[]> iterator = chunks.iterator(); iterator.hasNext();) {
			int[] chunk = iterator.next();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("(");
			for (int i = 0; i < chunk.length; i++) {
				Integer objectID = chunk[i];
				stringBuffer.append(objectID);
				if (i < chunk.length-1) {
					stringBuffer.append(", ");
				}
			}
			stringBuffer.append(")");
			stringArrays.add(stringBuffer.toString());
		}
		return stringArrays;
	}


	/**
	 * Escape the apostrophe and quote characters for use in javascript
	 * @param sourceString
	 * @return
	 */
	public static String escapeApostrophe(String sourceString) {
		if (sourceString!=null) {
			sourceString = sourceString.replaceAll("'", "\\\\'");
			sourceString = sourceString.replaceAll("\"","\\\\\"");
		}
		return sourceString;
	}

	public static List<IntegerStringBean> createIntegerStringBeanListFromLabelBeanList(List labelBeanlist) {
		List<IntegerStringBean> idList = new ArrayList<IntegerStringBean>();
		if (labelBeanlist!=null) {
			Iterator<ILabelBean> iterator = labelBeanlist.iterator();
			while (iterator.hasNext()) {
				ILabelBean beanID = iterator.next();
				idList.add(new IntegerStringBean(beanID.getLabel(),beanID.getObjectID()));
			}
		}
		return idList;
	}

	/**
	 * Creates an integer to string map from a list of integerStringBeans
	 * @param integerStringBeanList
	 * @return
	 */
	public static Map<Integer, String> createIntegerStringMapFromIntegerStringList(List<IntegerStringBean> integerStringBeanList) {
		Map<Integer, String> integerStringMap = new HashMap<Integer, String>();
		if (integerStringBeanList!=null) {
			for (IntegerStringBean integerStringBean : integerStringBeanList) {
				integerStringMap.put(integerStringBean.getValue(), integerStringBean.getLabel());
			}
		}
		return integerStringMap;
	}

	public static String encodeURL(String url) {
		String encodedURL = null;
		try {
			encodedURL = URLEncoder.encode(url , "UTF-8");
		}catch(UnsupportedEncodingException ex) {
		}
		if(encodedURL != null) {
			return encodedURL;
		}
		return url;
	}

}
