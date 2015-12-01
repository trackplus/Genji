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


package com.aurel.track.admin.server.logging;

/**
 * Appender for the log4j logging framework to log messages to memory.
 * The number of messages kept can be configured such as to not exhaust
 * memory.
 * <br>
 * Configuration example in <code>logging.properties</code> file:<br>
 *
 *<code>log4j.appender.MemAppender = com.aurel.track.util.MemoryAppender</code><br>
 *<code>log4j.appender.MemAppender.logsize = 1000</code><br>
 *
 */
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="Memory", category="Core", elementType="appender", printObject=true)
public final class MemoryAppender extends AbstractAppender {

	private static final long serialVersionUID = 503L;

	private static Map<String,LinkedList<String>> messageMap = new HashMap<String,LinkedList<String>>();
	private LinkedList<String> messages;

	private static int logsize;

    protected MemoryAppender(String name, Filter filter,
            Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        messages = messageMap.get(name);
        if (messages == null) {
        	messages = new LinkedList<String>();
        	messageMap.put(name, messages);
        }
    }

    // The append method is where the appender does the work.
    @Override
    public void append(LogEvent event) {
    	if (messages.size() == logsize) {
    		messages.removeFirst();
    	}
    	try {
    		String message = new String(getLayout().toByteArray(event), "UTF-8");
    		messages.addLast(message);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage());
    	}
    }


    @PluginFactory
    public static MemoryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("numberOfLines") String numberOfLines) {
        if (name == null) {
            LOGGER.error("No name provided for MemoryAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        if (numberOfLines != null) {
        	logsize = Integer.valueOf(numberOfLines);
        }
        return new MemoryAppender(name, filter, layout, true);
    }
    
    public List<String> getLog() {
        return Collections.unmodifiableList(messages);
     }
}
