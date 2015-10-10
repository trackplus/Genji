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


package com.aurel.track.exchange.track;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeHistoryTransactionEntry {
	private String uuid;
	private String changedBy;
	private String changedAt;
	private List<ExchangeHistoryFieldEntry> historyFieldChanges;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getChangedBy() {
		return changedBy;
	}
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}
	public String getChangedAt() {
		return changedAt;
	}
	public void setChangedAt(String changedAt) {
		this.changedAt = changedAt;
	}
	public List<ExchangeHistoryFieldEntry> getHistoryFieldChanges() {
		return historyFieldChanges;
	}
	public void setHistoryFieldChanges(
			List<ExchangeHistoryFieldEntry> historyFieldChanges) {
		this.historyFieldChanges = historyFieldChanges;
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();		
		attributesMap.put(ExchangeFieldNames.CHANGEDAT, getChangedAt());
		attributesMap.put(ExchangeFieldNames.CHANGEDBY, getChangedBy());		
		attributesMap.put(ExchangeFieldNames.UUID, getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ExchangeHistoryTransactionEntry deserializeBean(Map<String, String> attributes) {
		ExchangeHistoryTransactionEntry exchangeHistoryTransactionEntry = new ExchangeHistoryTransactionEntry();				
		exchangeHistoryTransactionEntry.setChangedAt(attributes.get(ExchangeFieldNames.CHANGEDAT));
		exchangeHistoryTransactionEntry.setChangedBy(attributes.get(ExchangeFieldNames.CHANGEDBY));
		exchangeHistoryTransactionEntry.setUuid(attributes.get(ExchangeFieldNames.UUID));		
		return exchangeHistoryTransactionEntry;
	}
}
