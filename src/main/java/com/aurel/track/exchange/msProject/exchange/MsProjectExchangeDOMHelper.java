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

package com.aurel.track.exchange.msProject.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.Relation;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MsProjectExchangeDOMHelper {
	private static final Logger LOGGER = LogManager.getLogger(MsProjectExchangeDOMHelper.class);
	
	/*************************************************************************/
    /*****************DOM helper methods**************************************/
    /*************************************************************************/

    /**
     * Get calendar element map. 
     * @param elementList
     * @param integetValuedSubelementName
     * @return
     */
    public static Map<Integer, ProjectCalendar> getSubelementBasedElementMap(List<ProjectCalendar> elementList) {
    	Map<Integer, ProjectCalendar> elementMap = new HashMap<Integer, ProjectCalendar>();
    	if (elementList!=null) {
    		for(int i = 0; i < elementList.size(); i++) {
				ProjectCalendar element = elementList.get(i);
				Integer UID = element.getUniqueID();
				if (UID!=null) {
					elementMap.put(UID, element);
				}				
			}
    	}
    	    	   	
    	return elementMap;
    }  
    
    /**
     * Get assignment map.
     * @param elementList
     * @return
     */
    public static Map<Integer, ResourceAssignment> getSubelementBasedElementMapAssignment(List<ResourceAssignment> elementList) {
    	Map<Integer, ResourceAssignment> elementMap = new HashMap<Integer, ResourceAssignment>();
    	if (elementList!=null) {
    		for(int i = 0; i < elementList.size(); i++) {
    			ResourceAssignment element = elementList.get(i);
				Integer UID = element.getUniqueID();
				if (UID!=null) {
					elementMap.put(UID, element);
				}				
			}
    	}
    	    	   	
    	return elementMap;
    } 
    
    /**
     * Get Task map
     * @param elementList 
     * @param integetValuedSubelementName
     * @return
     */
    public static Map<Integer, Task> getSubelementBasedElementMapTask(List<Task> elementList) {
    	Map<Integer, Task> elementMap = new HashMap<Integer, Task>();
    	if (elementList!=null) {
    		for(int i = 0; i < elementList.size(); i++) {
    			Task element = elementList.get(i);
//				Integer UID = element.getUniqueID();
    			Double UIDTmp = (Double)element.getFieldByAlias("trackPlusId");
				Integer UID;
				if(UIDTmp != null) {
					UID = UIDTmp.intValue();
				}else {
					UID = element.getUniqueID();
				}
				if (UID != null) {
					elementMap.put(UID, element);
				}				
			}
    	}
    	    	   	
    	return elementMap;
    }  
    
    /**
     * Get Resource map
     * @param elementList
     * @param integetValuedSubelementName
     * @return
     */
    public static Map<Integer, Resource> getSubelementBasedElementMapResource(List<Resource> elementList) {
    	Map<Integer, Resource> elementMap = new HashMap<Integer, Resource>();
    	if (elementList!=null) {
    		for(int i = 0; i < elementList.size(); i++) {
    			Resource element = elementList.get(i);
				Integer UID = element.getUniqueID();
				if (UID!=null) {
					elementMap.put(UID, element);
				}				
			}
    	}
    	return elementMap;
    }  
    
    /**
     * Get the text value of the element's subelement
     * @param element
     * @param subelementName
     * @return
     */
    public static String getSubelementText(Element element, String subelementName) {    	
    	if (element!=null) {
	    	NodeList subelementList = element.getElementsByTagName(subelementName);
	        if (subelementList!=null && subelementList.getLength()>0) {
	        	for (int i = 0; i < subelementList.getLength(); i++) {
	        		Element subElement = (Element)subelementList.item(0);        	
	        		if (subElement!=null && subElement.getParentNode()==element) {        		
	        			//only if it is direct child not any descendant
	        			return subElement.getNodeValue();
	        		}        	
				}
	        }
    	}
    	return null;
    }
    
    /**
     * Get the integer value of the element's subelement
     * @param element
     * @param subelementName
     * @return
     */
    public static Integer getSubelementInteger(Element element, String subelementName) {
    	String subelementText = getSubelementText(element, subelementName);
    	if (subelementText!=null) {
    		try {
    			return Integer.valueOf(subelementText);
    		} catch (Exception e) {				
			}
    	}
    	return null;
    }
    
    /**
     * Creates a map from an element list based on a subelement
     * @param elementList
     * @param subelementName
     * @return
     */
    public static Map<Integer, Resource> createIntegerElementMapFromList(List<Resource> elementList, String subelementName) {
    	Map<Integer, Resource> subElementBasedMap = new HashMap<Integer, Resource>();
		if (elementList!=null) {
			for(int i = 0; i < elementList.size(); i++) {
				Resource element = elementList.get(i);
				Integer UID = element.getUniqueID();
				if (UID!=null) {
					subElementBasedMap.put(UID, element);
				}
			}
		}
		return subElementBasedMap;
    }
    
    /**
     * Creates Task Map
     * @param elementList
     * @param subelementName
     * @return
     */
    public static Map<Integer, Task> createIntegerElementMapFromListForTasks(List<Task> elementList) {
    	Map<Integer, Task> subElementBasedMap = new HashMap<Integer, Task>();
		if (elementList!=null) {
			for(int i = 0; i < elementList.size(); i++) {
				Task element = elementList.get(i);
				Double UIDTmp = (Double)element.getFieldByAlias("trackPlusId");
				Integer UID;
				if(UIDTmp != null) {
					UID = UIDTmp.intValue();
				}else {
					UID = element.getUniqueID();
				}
				if (UID!=null) {
					subElementBasedMap.put(UID, element);
				}
			}
		}
		return subElementBasedMap;
    }
    
    /**
     * Creates a map from an element list based on a subelement
     * @param elementList
     * @param subelementName
     * @return
     */
    public static Map<Integer, List<ResourceAssignment>> createIntegerElementListMapFromList(List<ResourceAssignment> elementList, String subelementName) {
    	Map<Integer, List<ResourceAssignment>> subElementBasedMap = new HashMap<Integer, List<ResourceAssignment>>();
		if (elementList!=null) {
			for(int i = 0; i < elementList.size(); i++) {
				ResourceAssignment element = elementList.get(i);
//				Integer taskUID = getSubelementInteger(element, subelementName);
				Integer taskUID = element.getTaskUniqueID();
				if (taskUID!=null) {
					List<ResourceAssignment> subelementList = subElementBasedMap.get(taskUID);
					if (subelementList==null) {
						subelementList = new ArrayList<ResourceAssignment>();
						subElementBasedMap.put(taskUID, subelementList);
					}
					subelementList.add(element);
				}
			}
		}
		return subElementBasedMap;
    }
    
    /**
     * Task UID validation.
     * @param element
     * @return
     */
    public static boolean UIDIsValidTask(Task element) {
//    	Integer UID = element.getUniqueID();
    	Double UIDTmp = (Double)element.getFieldByAlias("trackPlusId");
		Integer UID;
		if(UIDTmp != null) {
			UID = UIDTmp.intValue();
		}else {
			UID = element.getUniqueID();
		}
		return UID!=null && UID.intValue()!=0;    	  	
    }
    
    /**
     * Task UID validation for MPXJ
     * @param element
     * @return
     */
    public static boolean UIDIsValidMpxjApiTemp(Task element) {
    	Double UIDTmp = (Double)element.getFieldByAlias("trackPlusId");
		Integer UID;
		if(UIDTmp != null) {
			UID = UIDTmp.intValue();
		}else {
			UID = element.getUniqueID();
		}
		return UID!=null && UID.intValue()!=0;    	  	
    }
    
    /**
     * Resource UID validation.
     * @param element
     * @return
     */
    public static boolean UIDIsValidMpxjApiTempResource(Resource element) {
    	Integer UID = element.getUniqueID();
		return UID!=null && UID.intValue()!=0;    	  	
    }
    
    /** Uses with PPXJ api.
	 * Get the taskUID and predecessorUID based predecessors map
	 * @param tasks
	 * @return
	 */
    public static Map<Integer, Map<Integer, Relation>> getDependentPredecessorLinksMap(List<Task> tasks) {
		Map<Integer, Map<Integer, Relation>> dependentToPredecessorLinksMap = new HashMap<Integer, Map<Integer, Relation>>();
		Iterator<Task> iterator = tasks.iterator();
		while (iterator.hasNext()) {
			Task taskElement = iterator.next();			
			try {
				List<Relation> predecessorLinks = taskElement.getPredecessors();
				if (predecessorLinks!=null && !predecessorLinks.isEmpty()) {
//					Integer dependentUID = taskElement.getUniqueID();
					Double UIDTmp = (Double)taskElement.getFieldByAlias("trackPlusId");
					Integer dependentUID;
					if(UIDTmp != null) {
						dependentUID = UIDTmp.intValue();
					}else {
						dependentUID = taskElement.getUniqueID();
					}
					Map<Integer, Relation> predecessorMap = new HashMap<Integer, Relation>();
					dependentToPredecessorLinksMap.put(dependentUID, predecessorMap);
					for (int i = 0; i < predecessorLinks.size(); i++) {
						Relation predecessorLink = predecessorLinks.get(i);
						Integer predecessorUID = predecessorLink.getTargetTask().getUniqueID();
						Relation existingPredecessorLink = predecessorMap.get(predecessorUID);
						if (existingPredecessorLink!=null) {
							//the same link between the two tasks again: should never happen but just in case remove it
//							existingPredecessorLink.getParentNode().removeChild(existingPredecessorLink); //TODO with MPXJ API
							
						} else {
							predecessorMap.put(predecessorUID, predecessorLink);
						}
					}
				}			
			}catch(NullPointerException ex) {
				LOGGER.error(ex.getMessage());
			}
		}
		return dependentToPredecessorLinksMap;
	}
    
    /**
	 * Get the taskUID and resourceUID based assignments map
	 * @param assignmentsList
	 * @return
	 */
    public static Map<Integer, Map<Integer, ResourceAssignment>> getAssignmentsMap(List<ResourceAssignment> assignmentsList) {
		Map<Integer, Map<Integer, ResourceAssignment>> taskUIDToResourceUIDToAssignmentMap = new HashMap<Integer, Map<Integer, ResourceAssignment>>();
		for (int i = 0; i < assignmentsList.size(); i++) {
			ResourceAssignment assignmentElement = assignmentsList.get(i);
			Integer taskUID = assignmentElement.getTaskUniqueID();
			Integer resourceUID = assignmentElement.getResourceUniqueID();
			Map<Integer, ResourceAssignment> resourceUIDToAssignmentMap = taskUIDToResourceUIDToAssignmentMap.get(taskUID);
			if (resourceUIDToAssignmentMap==null) {
				resourceUIDToAssignmentMap = new HashMap<Integer, ResourceAssignment>();
				taskUIDToResourceUIDToAssignmentMap.put(taskUID, resourceUIDToAssignmentMap);
			}
			resourceUIDToAssignmentMap.put(resourceUID, assignmentElement);
		}
		return taskUIDToResourceUIDToAssignmentMap;
	}
}
