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


package com.aurel.track.dbase.jobs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.plugin.VersionControlDescriptor;
import com.aurel.track.plugin.VersionControlDescriptor.BrowserDescriptor;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.vc.RepositoryFileViewer;
import com.aurel.track.vc.Revision;
import com.aurel.track.vc.VersionControlMap;
import com.aurel.track.vc.VersionControlMap.ItemInfo;
import com.aurel.track.vc.VersionControlPlugin;
import com.aurel.track.vc.bl.VersionControlBL;

public class VersionControlLogFetcherJob implements Job {
	
	private static final String DEFAULT_PREFIX="#";
	
	private static final Logger LOGGER = LogManager.getLogger(VersionControlLogFetcherJob.class);
	
	private static long LOGINTERVAL = 1000*60*60*6; // 6 hours
	private static long attemptTimeStamp = new Date().getTime() - LOGINTERVAL;
	

	@Override
	public void execute(JobExecutionContext context) {
		Date startDate=new Date();
		LOGGER.debug("execute VersionControlLogFetcherJob....");
        JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Scheduler scheduler = context.getScheduler();
		Integer dutyCycleInt = new Integer(25); // just some default
		try {
			dutyCycleInt = jobDataMap.getIntegerFromString("dutyCycle");
		}
		catch (Exception ex) {
		    // we don't need to do much here.	
		}
		int dutyCycle=0;
		if(dutyCycleInt!=null&&dutyCycleInt.intValue()<100){
			dutyCycle=dutyCycleInt.intValue();
		}
		String prefixIssueNumber=null;
		try {
			prefixIssueNumber = jobDataMap.getString("prefixIssueNumber");
		}
		catch (Exception ex) {
		    // we don't need to do much here.	
		}
		if(prefixIssueNumber==null||prefixIssueNumber.length()==0){
			LOGGER.debug("No prefixIssueNumber found! Use default:"+DEFAULT_PREFIX);
			prefixIssueNumber=DEFAULT_PREFIX;
		}else{
			LOGGER.debug("Use prefixIssueNumber:\""+prefixIssueNumber+"\"");
		}
		
		// Check if a job with the same name is currently running.
		// If so, skip this one.

		try {
			int count = 0;
			for (JobExecutionContext cont: scheduler.getCurrentlyExecutingJobs()){
				JobDetail jd = cont.getJobDetail();
				if (jd.getKey().equals(jobDetail.getKey())) {
					++count;
				}
			}
			if (count > 1) {
				return;
			}
		}
		catch (Exception e) {
			// Scheduler exception
		}
		
		List<TProjectBean> projects= ProjectBL.loadActiveInactiveProjectsFlat();
		
        List<PluginDescriptor> vcDescriptors=VersionControlBL.getVersionControlPlugins();
        List<Map<Integer,List<Revision>>> mapLogList=new ArrayList<Map<Integer,List<Revision>>>();
        Map<String,RepositoryFileViewer> mapViewer=new HashMap<String, RepositoryFileViewer>();
        
        long now = new Date().getTime();
        if ((now - attemptTimeStamp) >= LOGINTERVAL) {
        	attemptTimeStamp = now;
        }
        
        for (int i = 0; i < projects.size(); i++) {
            TProjectBean prj = projects.get(i);
            boolean useVC=(PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.USE_VERSION_CONTROL_PROPERTY)+"").equalsIgnoreCase("true");
            if(useVC){
                Map<String,String> parameters= VersionControlBL.laodMapVerisonControl(prj.getObjectID());
                String vcType=PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.VERSION_CONTROL_TYPE_PROPERTY);
                
                LOGGER.debug("Version control found:"+vcType+" for project:"+prj.getLabel());
                VersionControlDescriptor descriptor=getVersionControlDescriptor(vcType,vcDescriptors);
                if(descriptor==null){
                	if (now == attemptTimeStamp) {
                		LOGGER.error("Version control plugin not found for:"+vcType+" in project:"+prj.getLabel());
                	}
                	continue;
                }
                String theClass=descriptor.getTheClassName();
                VersionControlPlugin vcplugin=VersionControlBL.getVersionControlPlugin(theClass);
                if(vcplugin==null){
                	if (now == attemptTimeStamp) {
                		LOGGER.error("Version control plugin not found for:"+vcType+" in project:"+prj.getLabel());
                	}
                	continue;
                }
                String url=vcplugin.getRepository(parameters);
                LOGGER.debug("Version control plugin found. Url:"+url);
                if(mapViewer.containsKey(url)){
                	LOGGER.debug("This version control settings was already processed on another project! Skip this project.");
                	continue;
                }
                RepositoryFileViewer repositoryFileViewer=new RepositoryFileViewer();
                repositoryFileViewer.setRepository(url);

                String viewVCBaseUrl=prj.getVersionSystemField0();
                String viewVCBrowser=prj.getVersionSystemField1();
                BrowserDescriptor b=descriptor.findBrowser(viewVCBrowser);
                repositoryFileViewer.setBaseURL(viewVCBaseUrl);
                repositoryFileViewer.setBrowser(b);
                repositoryFileViewer.setChangesetLink(PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.VC_CHANGESET_LINK));
                repositoryFileViewer.setAddedLink(PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.VC_ADDED_LINK));
                repositoryFileViewer.setModifiedLink(PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.VC_MODIFIED_LINK));
                repositoryFileViewer.setReplacedLink(PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.VC_REPLACED_LINK));
                repositoryFileViewer.setDeletedLink(PropertiesHelper.getProperty(prj.getMoreProperties(), TProjectBean.MOREPPROPS.VC_DELETED_LINK));
                Map<Integer,List<Revision>> logs=vcplugin.getLogs(parameters, prefixIssueNumber);
                if(logs!=null){
                	Iterator<Integer> it=logs.keySet().iterator();
                	while (it.hasNext()) {
                		Integer key = it.next();
                		List<Revision> value =  logs.get(key);
                		if(value!=null){
                			for (int j = 0; j < value.size(); j++) {
                				Revision r=value.get(j);
                				r.setRepository(url);
                			}
                		}
                	}
                }
                mapLogList.add(logs);

                mapViewer.put(url,repositoryFileViewer);
            }
        }
        synchronized(VersionControlMap.class){
            VersionControlMap.clearMap();
            for (int i = 0; i < mapLogList.size(); i++) {
            	Map<Integer,List<Revision>> logs =  mapLogList.get(i);
                VersionControlMap.mergeMap(logs);    
            }
            Map<Integer,List<Revision>> map=VersionControlMap.getMap();
            Map<Integer,ItemInfo> mapItems= prepareItemInfo(map);
            VersionControlMap.setMapItems(mapItems);
            VersionControlMap.setMapViewer(mapViewer);
            VersionControlMap.prefixIssueNumber=prefixIssueNumber;
        }
        Trigger trigger = context.getTrigger();
		Date nextFire = trigger.getNextFireTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		LOGGER.debug("Next fire time: " + dateFormat.format(nextFire));
		// System.err.println("Cron-Exp.: " + trigger.getCronExpression());

        Date endDate=new Date();
        long timeSpent=endDate.getTime()-startDate.getTime();
        long timeToNextFire=nextFire.getTime()-endDate.getTime();
        //timeToNextFire<0? (that means duration is bigger than job interval)
        //!!! this is possible or trigger.getNextFireTime() will give the next time
        long time100Percent=nextFire.getTime()-startDate.getTime();
        long spentPercent=timeSpent*100/time100Percent;
        LOGGER.debug("Start time:"+dateFormat.format(startDate));
        LOGGER.debug("dutyCycle="+dutyCycle+"%");
        if(dutyCycle>0){//there are processor safe
        	if(spentPercent>dutyCycle){
        		//the processor are running more that expect
        		LOGGER.info("The VersionControlLogFetcherJob spent more that expected!");
        		long time100PercentAdjust=100*timeSpent/dutyCycle;
        		Calendar cal=Calendar.getInstance();
        		cal.setTimeInMillis(startDate.getTime()+time100PercentAdjust);
        		Date nextFireAdjust=cal.getTime();
        		LOGGER.info("Rescheduled next fire time " + dateFormat.format(nextFireAdjust));
        		int interval = (int) (nextFireAdjust.getTime() - new Date().getTime());
        		//set corresponding next fire!
        		// retrieve the trigger
        		try {
        			Trigger oldTrigger = context.getTrigger();

        			// obtain a builder that would produce the trigger
        			@SuppressWarnings("rawtypes")
        			TriggerBuilder tb = oldTrigger.getTriggerBuilder();

        			// update the schedule associated with the builder, and build the new trigger
        			Trigger newTrigger = tb.withSchedule(SimpleScheduleBuilder.simpleSchedule()
        					.withIntervalInSeconds(interval)
        					.repeatForever())
        					.build();
        			scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
        		} catch (Exception e) {
        			LOGGER.error("Unable to reschedule VersionControlLogFetcherJob: " + e.getMessage());
        		}

        	}
        }
        String msg = "Done executing VersionControlLogFetcherJob! Time spent: "+(timeSpent/1000)+"s. Spent percent="+spentPercent+"%";
        if (timeSpent/1000 > 100) {
            LOGGER.info(msg);	
        } else {
        	LOGGER.debug(msg);
        }
    }
	
    public  VersionControlDescriptor getVersionControlDescriptor(String vcDescriptorID,List descriptors){
        VersionControlDescriptor result=null;
        for (int i = 0; i < descriptors.size(); i++) {
            VersionControlDescriptor descriptor = (VersionControlDescriptor) descriptors.get(i);
            if(descriptor.getId().equals(vcDescriptorID)){
                result=descriptor;
                break;
            }
        }
        return result;
    }
    private Map<Integer,ItemInfo> prepareItemInfo(Map<Integer,List<Revision>>  map){
    	Map<Integer,ItemInfo> mapItems=new HashMap<Integer, ItemInfo>();
    	Iterator<Integer> it=map.keySet().iterator();
    	List<Integer> itemIdList=new ArrayList<Integer>();
    	while (it.hasNext()) {
			itemIdList.add(it.next());
		}
    	int[] workItemdIs=GeneralUtils.createIntArrFromIntegerList(itemIdList);
    	List<TWorkItemBean> workItemBeans=ItemBL.loadByWorkItemKeys(workItemdIs);
    	ItemInfo itemInfo;
    	TWorkItemBean workItemBean;
    	for (int i = 0; i < workItemBeans.size(); i++) {
    		itemInfo=new ItemInfo();
    		workItemBean=workItemBeans.get(i);
    		itemInfo.setItemID(workItemBean.getObjectID());
    		itemInfo.setReleaseScheduledID(workItemBean.getReleaseScheduledID());
    		itemInfo.setProjectID(workItemBean.getProjectID());
    		itemInfo.setTitle(workItemBean.getSynopsis());
    		mapItems.put(workItemBean.getObjectID(), itemInfo);
		}
    	return mapItems;
    }
}
