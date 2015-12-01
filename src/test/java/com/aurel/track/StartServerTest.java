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

package com.aurel.track;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

import com.aurel.track.dbase.DatabaseHandler;


public class StartServerTest
{
	private static Server jettyServer;
	private static WebAppContext context = null;

	public static final String TEST_CONTEXT = "/track-test";
	public static final int TEST_PORT = 10888;
	
	private static final int SERVER_START_TIMEOUT = 120;

	@Test
	public void startServer() {
		try {
			DatabaseHandler.stopDbServer();
			DatabaseHandler.cleanDatabase();
			DatabaseHandler.writeTorquePropsToHome();
			DatabaseHandler.startDbServer();
			startIfRequired();
			boolean ready = gettingLogonPageReady();
			assert (ready);
		} catch (Exception e) {
			assert (false);
		}
	}
	
	@Test
	public void stopServer() {
		try {
			DatabaseHandler.stopDbServer();
			if (jettyServer != null)
			{
				jettyServer.stop();
				jettyServer.join();
				jettyServer.destroy();
				jettyServer = null;
			}
			assert (true);
		} catch (Exception e) {
			assert (false);
		}
	}

	public static void main(String[] args) {

	}
	
	private static void startIfRequired() throws Exception
	{
		if (jettyServer == null)
		{
			//            SLF4JBridgeHandler.removeHandlersForRootLogger();
			//            SLF4JBridgeHandler.install();

			System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
			System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");

			jettyServer = new Server(TEST_PORT);

			context = new WebAppContext();

			context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
			context.setResourceBase("src/main/webapp");
			context.setContextPath(TEST_CONTEXT);
			context.setParentLoaderPriority(true);
			context.setAttribute("INTEST", true);

			jettyServer.setHandler(context);

			jettyServer.start();
		}
	}

	private static boolean gettingLogonPageReady()  throws Exception {
		
		File lock = new File(System.getProperty("user.dir")+"/homet/lock");
		if (lock.exists()) {
			lock.delete();
		}
		for (int i=0; i < SERVER_START_TIMEOUT; ++i) {
			Thread.sleep(1000);
			if (lock.exists()) {
				Thread.sleep(3000);
				return true;
			}
		}
		return (false); 
	}

}
