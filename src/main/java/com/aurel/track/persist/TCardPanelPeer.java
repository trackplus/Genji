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

package com.aurel.track.persist;

import com.aurel.track.beans.TCardPanelBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.dao.CardPanelDAO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TCardPanelPeer
    extends com.aurel.track.persist.BaseTCardPanelPeer implements CardPanelDAO {

	private static final Logger LOGGER = LogManager.getLogger(TCardPanelPeer.class);
	@Override
	public TCardPanelBean loadByPerson(Integer personID){
		List<TCardPanel> panels = null;

		Criteria crit = new Criteria();
		crit.add(PERSON,personID);
		try {
			panels = doSelect(crit);
		} catch(Exception e) {
			LOGGER.error("Loading by person  " + personID + " failed with " + e.getMessage());
		}
		if (panels!=null && !panels.isEmpty()) {
			return ((TCardPanel)panels.get(0)).getBean();
		}else{
			return null;
		}
	}
	@Override
	public TCardPanelBean loadByPrimaryKey(Integer objectID){
		TCardPanel tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.info("Loading of a cardPanel by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	@Override
	public TCardPanelBean loadFullByPrimaryKey(Integer objectID){
		TCardPanelBean panelBean=null;
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TCardPanel tpanel=retrieveByPK(objectID, con);
			panelBean=tpanel.getBean();
			List<IField> fields=loadFullChildren(objectID, con);
			panelBean.setFields(fields);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loding full a panel for key " + objectID + " failed with: " + e);
		}
		return panelBean;
	}
	public static List<IField> loadFullChildren(Integer objectID, Connection con) throws TorqueException {
		Criteria critChild = new Criteria();
		critChild.add(BaseTCardFieldPeer.CARDPANEL,objectID);
		List<IField> result=new ArrayList<IField>();
		List torqueList=BaseTCardFieldPeer.doSelect(critChild,con);
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				result.add(((TCardField)itrTorqueList.next()).getBean());
			}
		}
		return result;
	}


	@Override
	public Integer save(TCardPanelBean cardPanelBean){
		try {
			TCardPanel tobject = BaseTCardPanel.createTCardPanel(cardPanelBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a cardPanelBean failed with " + e.getMessage());
			return null;
		}
	}
	@Override
	public void delete(Integer objectID){
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a cardPanelBean for key " + objectID + " failed with: " + e);
		}
	}
}
