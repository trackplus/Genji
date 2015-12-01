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

package com.trackplus.track.util.html;

import java.util.ArrayList;

public class LaTeXTable {


	private int noc = 0;
	public LaTeXTable() {

	}

	ArrayList<TableRow> rows = new ArrayList<TableRow>();
	TableHeader header = null;
	String caption = null;

	public void addRow(TableRow _row) {
		rows.add(_row);
		if(_row != null && _row.getNumberOfColums() > noc) {
			noc = _row.getNumberOfColums();
		}
	}

	public void addHeader(TableHeader _header) {
		header = _header;
		if(_header != null && _header.getNumberOfColums() > noc) {
			noc = _header.getNumberOfColums();
		}
	}

	public int getNumberOfRows() {
		return rows.size();
	}

	public TableRow getRow(int index) {
		return rows.get(index);
	}

	public TableHeader getHeader() {
		return header;
	}
	
	public TableCell getNewCell() {
		return new TableCell();
	}

	public TableRow getNewRow() {
		return new TableRow();
	}
	
	public TableHeader getNewHeader() {
		return new TableHeader();
	}
	
	public void setCaption (String _caption) {
		caption = _caption;
	}
	
	
	public String getLaTeX() {
		StringBuilder sb = new StringBuilder();
		if (caption != null) {
			sb.append("\n\\begin{table}[ht]");
		}
		
		sb.append("\n\\begin{center}\n\\begin{tabularx}{0.9\\textwidth}{");

		for (int i=0; i < noc-1; ++i) {
			sb.append("!{\\Vl}l" );
		}
		sb.append("!{\\Vl}X!{\\Vl}" );
		sb.append("} \\noalign{\\hrule height 1pt}\n");
		
		if (header != null) {
			sb.append("\\rowcolor{Gray}\n \\rule[-2.5mm]{0mm}{8mm}\n");
			sb.append(header.getLaTeX());
		}
		
		for (TableRow row: rows) {
			sb.append("\\rule[-2.5mm]{0mm}{8mm}");
			sb.append(row.getLaTeX());
		}
			
		sb.append("\n\\end{tabularx}\n\\end{center}\n");
		if (caption != null) {
			sb.append("\\caption{"+caption+"}\n\\end{table}\n");
		}
		
		return sb.toString();
	}



	public class TableRow {
		ArrayList<TableCell> cells = new ArrayList<TableCell>();
		int noOfColumns = 0;

		public int getNumberOfColums() {
			return noOfColumns;
		}
		
		public void addCell(TableCell _cell){
			cells.add(_cell);
			++noOfColumns;
		}
		
		public String getLaTeX() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < cells.size()-1; ++i) {
				sb.append(cells.get(i).getText() + " & ");
			}
			sb.append(cells.get(cells.size()-1).getText());
			sb.append("\\\\ \\htpline\n");
			return sb.toString();
		}

	}

	public class TableHeader {
		TableRow headerRow = null;

		public int getNumberOfColums() {
			return headerRow.getNumberOfColums();
		}
		
		public void addRow(TableRow _row) {
			headerRow = _row;
		}
		
		public String getLaTeX() {
			return headerRow.getLaTeX();
		}
	}


	public class TableCell {
		String text = null;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}
}
