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

package com.aurel.track.configExchange.exporter;

import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 */
public class CDataXMLStreamWriter extends DelegatingXMLStreamWriter{
	private static final Pattern XML_CHARS = Pattern.compile( "[&<>]" );
	public CDataXMLStreamWriter( XMLStreamWriter del )	{
		super(del);
	}

	@Override
	public void writeCharacters( String text ) throws XMLStreamException{
		//always use cdata
		//boolean useCData =XML_CHARS.matcher( text ).find();
		//if( useCData ){
			super.writeCData( text );
		/*}else{
			super.writeCharacters( text );
		}*/
	}
}
