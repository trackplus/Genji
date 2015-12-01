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

package com.aurel.track.admin.customize.category.filter.execute;

import java.util.Locale;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.QueryContext;

/**
 */
public class EncodedQueryCtx {
	private QueryContext queryContext;
	private boolean keepMeLogged;
	private boolean parametrized;
	private TPersonBean personBean;
	private Locale locale;

	public QueryContext getQueryContext() {
		return queryContext;
	}

	public void setQueryContext(QueryContext queryContext) {
		this.queryContext = queryContext;
	}
	public TPersonBean getPersonBean() {
		return personBean;
	}

	public void setPersonBean(TPersonBean personBean) {
		this.personBean = personBean;
	}

	public boolean isKeepMeLogged() {
		return keepMeLogged;
	}

	public void setKeepMeLogged(boolean keepMeLogged) {
		this.keepMeLogged = keepMeLogged;
	}

	public boolean isParametrized() {
		return parametrized;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setParametrized(boolean parametrized) {
		this.parametrized = parametrized;
	}

}
