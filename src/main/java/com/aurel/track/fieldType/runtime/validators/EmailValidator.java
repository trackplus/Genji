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


package com.aurel.track.fieldType.runtime.validators;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;

import com.aurel.track.errors.ErrorData;


public class EmailValidator extends StringValidator {
	protected static Logger LOGGER = LogManager.getLogger(EmailValidator.class);
	
	private String patternString =  "[^@ \t]@(\\w|-|_)+\\.\\w+";
	private Perl5Pattern pattern=null;
	
	public String getPatternString() {
		return patternString;
	}

	public void setPatternString(String patternString) {
		if (patternString==null || "".equals(patternString.trim())) {
			patternString =  "[^@ \t]@(\\w|-|_)+\\.\\w+";
		}
		this.patternString = patternString;
		Perl5Compiler pc = new Perl5Compiler();
		try {
			pattern=(Perl5Pattern)pc.compile(patternString,Perl5Compiler.CASE_INSENSITIVE_MASK |Perl5Compiler.SINGLELINE_MASK);
		} catch (MalformedPatternException e) {
			LOGGER.error("Malformed Email Domain Pattern " + patternString +  "Setting default value");
			patternString = "\\w[-.\\w]+\\@[-.\\w]+\\.\\w{2,3}";
			try {
				pattern=(Perl5Pattern)pc.compile(patternString,Perl5Compiler.CASE_INSENSITIVE_MASK |Perl5Compiler.SINGLELINE_MASK);
			}
			catch (Exception me) {
				// now we better give up ...
				LOGGER.error("Malformed Email Domain Pattern:" + patternString);
			}
		}
	}


	/**
	 * Validates an value for a field and returns ErrorData in case of validation error
	 * @param value 
	 * @return
	 */
	@Override
	public ErrorData validateField(Object value) {
		ErrorData errorData = super.validateField(value);
		if (errorData!=null) {
			return errorData;
		}
		String textValue = (String)value;
		if (textValue==null || textValue.length()==0){
			return null;
		}
		Perl5Matcher pm = new Perl5Matcher();
		if (pattern!=null) {
			if (!pm.contains(textValue, pattern)) {
				LOGGER.warn("The email:'" + textValue +"' is not accepted by the pattern: "+pattern.getPattern());
				errorData = new ErrorData("admin.user.profile.err.emailAddress.format");
				return errorData;
		
			}
		}
		return null;
	}
}
