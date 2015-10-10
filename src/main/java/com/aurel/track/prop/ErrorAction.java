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


package com.aurel.track.prop;


import com.opensymphony.xwork2.ActionSupport;

/**
 * Implementation of <strong>Action</strong> that handles server errors.
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @version $Revision: 3938 $ $Date: 2012-09-17 21:25:53 +0200 (Mo, 17 Sep 2012) $
 */
public final class ErrorAction extends ActionSupport {

	private static final long serialVersionUID = 370L;
	
    @Override
	public String execute(){
        return "success";        
    }
    
    
}