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

package com.trackplus.ddl;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.util.Calendar;

public class GenericStringValueConverter implements  StringValueConverter {

	private static final Logger LOGGER = LogManager.getLogger(DataReader.class);

	@Override
	public String getStringValue(ResultSetMetaData rsmd, int idx,ResultSet rs,String tableName) throws SQLException,DDLException {
		String value=null;
		int type=rsmd.getColumnType(idx);
		String columnName=rsmd.getColumnName(idx);
		if(tableName.equalsIgnoreCase("TSITE")&&columnName.equalsIgnoreCase("EXPDATE")){
			Date d=rs.getDate(idx);
			if(d!=null){
				value="'"+d+"'";
			}
		}else{
			value=extractColumnValue(rs,idx,type);
		}
		return value;
	}

	protected String extractColumnValue(ResultSet resultSet,int columnIdx, int jdbcType) throws SQLException,DDLException{
		String  value=resultSet.getString(columnIdx);
		if(value!=null){
			switch (jdbcType){
				case Types.NUMERIC:
				case Types.DECIMAL:
					break;
				case Types.BIT:
				case Types.BOOLEAN:
				case Types.TINYINT:
				case Types.SMALLINT:
				case Types.INTEGER:
				case Types.BIGINT:
				case Types.REAL:
				case Types.FLOAT:
				case Types.DOUBLE:{
					break;
				}

				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
				case Types.BINARY:
				case Types.VARBINARY:
				case Types.TIME:
				case Types.CLOB:
				case Types.ARRAY:
				case Types.REF:{
					value="'"+value.replaceAll("'","''")+"'";
					break;
				}
				case Types.DATE:
				case Types.TIMESTAMP:{
					Date d=resultSet.getDate(columnIdx);
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					int year = cal.get(Calendar.YEAR);
					if(year<1900){
						throw new DDLException("Invalid date:"+d);
					}else{
						value="'"+value+"'";
					}
					break;
				}
				case Types.BLOB:
				case Types.LONGVARBINARY:{
					Blob blobValue=resultSet.getBlob(columnIdx);
					String str = new String(Base64.encodeBase64(blobValue.getBytes(1l, (int) blobValue.length())));
					value="'"+str+"'";
					break;
				}
				default:
					break;
			}
		}
		return value;
	}
}
