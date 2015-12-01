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

package com.aurel.track.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trackplus.dao.DAOFactory;
import com.trackplus.dao.SiteDAO;
import com.trackplus.model.Tsite;

public class SiteDAOTest {

	@Before
	public void setUp() throws Exception {


	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void test() {
		Tsite site = new Tsite();
		SiteDAO siteDAO = DAOFactory.getSiteDAO();
		try {
			siteDAO.save(site);
			site = new Tsite();
			siteDAO.save(site);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Problem saving a site object");
		}
		assertTrue("Saved site object",true);
		
		site = siteDAO.load();
		assertTrue("Failed to load the single site object.",site!=null);
		
		List <Tsite> sites = siteDAO.loadAll();
		assertTrue("Failed to retrieve all site objects.",sites.size()==2);
		
		site = siteDAO.loadByPrimaryKey(site.getObjectID());
		assertTrue("Could not load site object by its primary key.", site!=null);
		
		site = siteDAO.load(site.getObjectID());
		assertTrue("Could not load site object by its primary key.", site!=null);
		
		sites = siteDAO.loadAll();
		assertTrue("Load by primary key failed",sites.size()==2);
		
		siteDAO.delete(site);
		sites = siteDAO.loadAll();
		assertTrue("Delete by object failed.",sites.size()==1);
		
		site = new Tsite();
		siteDAO.save(site);
		
		Tsite sitenew = siteDAO.load(site.getObjectID());		
		sitenew.setLicenseKey("lickey");
		siteDAO.save(sitenew);
		
		site = siteDAO.load(site.getObjectID());
		assertTrue("Updating site object did not work.", 
				sitenew.getLicenseKey().equals(site.getLicenseKey())
				&& sitenew.getObjectid() == site.getObjectid());
		
		siteDAO.delete(site.getObjectID());
		sites = siteDAO.loadAll();
		assertTrue("Delete by objects primary key failed.",sites.size()==1);
		
	}

}
