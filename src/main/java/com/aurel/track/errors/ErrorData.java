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


package com.aurel.track.errors;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ErrorData {

	private static final Logger LOGGER = LogManager.getLogger(ErrorData.class);
	/**
	 * The ID of the field the validation error is linked with
	 * Typically represents the first parameter from the message 
	 * identified by resourceKey. Should be localized and 
	 * then added to the resourceParameters list  
	 */
	private Integer fieldID;
	
	/**
	 * The parameterized resource key
	 */
	private String resourceKey;
	
	/**
	 * List of ErrorParameter objects 
	 */
	private List<ErrorParameter> resourceParameters;
	
	/**
	 * The name of the field the validation error is linked with 
	 */
	private String fieldName;

    /**
     * Whether this is a confirmable error: an error condition which can be solved if the user confirms explicitly executing an operation
     */
    private boolean confirm = false;
    
    /**
     * Confirmation message
     */
    //private String confirmationMessageKey;

	/**
	 * Resource key, no parameter
	 * @param resourceKey
	 */
	public ErrorData(String resourceKey) {
		this.resourceKey = resourceKey;
	}
	
	/**
	 * Field specified, and resource key, no parameter
	 * @param fieldID
	 * @param resourceKey
	 */
	public ErrorData(Integer fieldID, String resourceKey) {
		this.resourceKey = resourceKey;
		this.fieldID=fieldID;
	}
	
	/**
	 * Resource key with one, parameter
	 * @param resourceKey
	 * @param parameter
	 */
	public ErrorData(String resourceKey, Object parameter) {
		this(null, resourceKey, parameter);
	}
	
	/**
	 * Field specified, and resource key, and one not localized parameter
	 * @param fieldID
	 * @param resourceKey
	 * @param parameter
	 */
	public ErrorData(Integer fieldID, String resourceKey, Object parameter) {
		this(fieldID, resourceKey, parameter, false);
	}
	
	/**
	 * Resource key with one (localized or not localized) parameter
	 * @param resourceKey
	 * @param parameter
	 * @param localizeParam
	 */
	public ErrorData(String resourceKey, Object parameter, boolean localizeParam) {
		this(null, resourceKey, parameter, localizeParam);
	}
	
	/**
	 * Field with, resource key with one (localized or not localized) parameter
	 * @param fieldID
	 * @param resourceKey
	 * @param parameter
	 * @param localizeParam
	 */
	public ErrorData(Integer fieldID, String resourceKey, Object parameter, boolean localizeParam) {
		this.resourceKey = resourceKey;
		this.fieldID=fieldID;
		if (parameter!=null) {
			resourceParameters=new LinkedList<ErrorParameter>();
			ErrorParameter errorParameter=new ErrorParameter(localizeParam, parameter);
			resourceParameters.add(errorParameter);
		}
	}
	
	public void addResourceParameterAsFirst(Object parameterValue) {
		if (resourceParameters==null) {
			resourceParameters=new LinkedList<ErrorParameter>();
		}
		resourceParameters.add(0, new ErrorParameter(parameterValue));
	}
	
	/**
	 * Resource key with a list of parameters
	 * @param resourceKey 
	 * @param resourceParameters
	 */
	public ErrorData(String resourceKey, List<ErrorParameter> resourceParameters) {
		this.resourceKey = resourceKey;
		this.resourceParameters = resourceParameters;
	}
	
	/**
	 * If it is known to have one singel parameter, return it
	 */
	public Object getResourceParameter() {
		if (resourceParameters!=null && !resourceParameters.isEmpty()) {
			ErrorParameter errorParameter = resourceParameters.get(0);
			if (errorParameter!=null) {
				return errorParameter.getParameterValue();
			}
		}
		return null;
	}
	
	public Integer getFieldID() {
		return fieldID;
	}
	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	public String getResourceKey() {
		return resourceKey;
	}
	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}
	public List<ErrorParameter> getResourceParameters() {
		return resourceParameters;
	}
	public void setResourceParameters(List<ErrorParameter> resourceParameters) {
		this.resourceParameters = resourceParameters;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

	/*public String getConfirmationMessageKey() {
		return confirmationMessageKey;
	}

	public void setConfirmationMessageKey(String confirmationMessageKey) {
		this.confirmationMessageKey = confirmationMessageKey;
	}*/

	/**
	 * Implemented for ErrorData keyed map (see mass operation)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj==null) {
			return false;
		}
		ErrorData errorData = null;
		try {
			errorData = (ErrorData)obj;
		} catch(Exception ex) {
			LOGGER.error("Converting the " + obj.getClass().getName() + 
					" to ErrorData failed with " + ex.getMessage());
			return false;
		}
		if (this.resourceKey==null && errorData.resourceKey==null) {
			return true;
		}
		if (this.resourceKey==null || errorData.resourceKey==null) {
			return false;
		}
		return this.resourceKey.equals(errorData.resourceKey);
	}

	/**
	 * Implemented for ErrorData keyed map (see mass operation)
	 */
	@Override
	public int hashCode() {
		return this.resourceKey.hashCode();
	}
}
