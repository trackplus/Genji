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

package com.aurel.track.admin.customize.category.filter.tree.design;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.util.IntegerStringBean;

/**
 * Field expression for "non select" filter (upper) fields:
 * the matcher is not hardcoded but no parenthesis are needed
 */
public class FieldExpressionSimpleTO extends MatcherExpressionBase {
	//the name for the matchers combo on the client side (used by submit)
	protected String matcherName;
	//the itemId in the ext js client side to find by getComponent() 
	protected String matcherItemId;
	protected Integer selectedMatcher;
	protected List<IntegerStringBean> matchersList = new ArrayList<IntegerStringBean>();
	//valueName is serialized in the jsonConfig (not here)
	//when the new jsonConfig is null 
	//(according to the new matcher no value control is needed) the valueItemId is needed
	//to remove the old value control by itemId
	protected String valueItemId;
	protected boolean needMatcherValue;
	//the json object for configuring the value renderer control 
	protected String jsonConfig;
	//used only by the derived class FieldExpressionInTreeTO, except when
	//used for parameterized FieldExpressionInTreeTOs which are 
	//rendered as FieldExpressionSimpleTOs with index
	private Integer index;
	
	public FieldExpressionSimpleTO() {
		super();
	}
	
	public FieldExpressionSimpleTO(QNodeExpression qNodeExpression) {
		super();
		this.field = qNodeExpression.getField();
		this.selectedMatcher = qNodeExpression.getMatcherID();
		this.value = qNodeExpression.getValue();
	}	

	public Integer getSelectedMatcher() {
		return selectedMatcher;
	}

	public void setSelectedMatcher(Integer selectedMatcher) {
		this.selectedMatcher = selectedMatcher;
	}
	
	public List<IntegerStringBean> getMatchersList() {
		return matchersList;
	}

	public void setMatchersList(List<IntegerStringBean> matchersList) {
		this.matchersList = matchersList;
	}

	
	public boolean isNeedMatcherValue() {
		return needMatcherValue;
	}

	public void setNeedMatcherValue(boolean needMatcherValue) {
		this.needMatcherValue = needMatcherValue;
	}

	public String getJsonConfig() {
		return jsonConfig;
	}

	public void setJsonConfig(String jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

	public String getMatcherName() {
		return matcherName;
	}

	public void setMatcherName(String matcherName) {
		this.matcherName = matcherName;
	}

	public String getMatcherItemId() {
		return matcherItemId;
	}

	public void setMatcherItemId(String matcherItemId) {
		this.matcherItemId = matcherItemId;
	}

	public String getValueItemId() {
		return valueItemId;
	}

	public void setValueItemId(String valueItemId) {
		this.valueItemId = valueItemId;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}
