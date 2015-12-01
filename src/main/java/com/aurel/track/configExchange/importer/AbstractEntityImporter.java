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

package com.aurel.track.configExchange.importer;

import com.aurel.track.beans.ISerializableLabelBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public abstract class AbstractEntityImporter<T extends ISerializableLabelBean> implements  IEntityImporter<T>{

	private static final HashMap<String, Map<Integer, Integer>> EMPTY_MATCHES_MAP = new HashMap<String, Map<Integer,Integer>>();

	@Override
	public boolean clearChildren(Integer parentID) {
		return true;
	}
	public T queryForMatchingEntity(T bean) {
		// search through cache
		return searchMatchingBean(bean, loadSimilar(bean));
	}
	protected  List<T> loadSimilar(T serializableBeanInstance){
		return new ArrayList<T>();
	}

	private T searchMatchingBean(T bean, List<T> similarBeans) {
		if(similarBeans!=null&&similarBeans.size()>0){
			for (T dbBean : similarBeans) {
				if (bean.considerAsSame(dbBean, EMPTY_MATCHES_MAP)) {
					return dbBean;
				}
			}
			return null;
		}
		return null;
	}

	@Override
	public boolean isChanged(T bean){
		return false;
	}
}
