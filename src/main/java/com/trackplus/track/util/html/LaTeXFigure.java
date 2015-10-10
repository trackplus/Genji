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

package com.trackplus.track.util.html;


public class LaTeXFigure {


	
	private String caption = null;
	private String fileName = null;
	private Integer height = 0;
	private Integer width = 0;
	private Integer originalHeight = 0;
	private Integer originalWidth = 0;
	private Double scale = 0.0;
	
	public LaTeXFigure() {

	}

	public void setFileName(String _name) {
		fileName = _name;
	}

	public void setCaption (String _caption) {
		caption = _caption;
	}
	
	private boolean hasScale = false;
	
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public void setOriginalWidth(Integer width) {
		this.originalWidth = width;
	}
	
	public void setOriginalHeight(Integer height) {
		this.originalHeight = height;
	}

	
//	\begin{figure}[!ht]
//			  \begin{center}
//			   \includegraphics[scale=0.75]{Chapter1/pmelements.pdf}
//			    \caption[Elemente des Projektmanagements]{\label{fig:pmelements}
//			             Elemente des Projektmanagements.}
//			  \end{center}
//			\end{figure} 
	
	public String getLaTeX() {
		StringBuilder sb = new StringBuilder();

		double scalex = 0;
		double scaley = 0;
		
		if (width != null && width != 0 && originalWidth != null && originalWidth != 0) {
			scalex = width.doubleValue() / originalWidth.doubleValue();
		}
		
		if (height != null && height != 0 && originalHeight != null && originalHeight != 0) {
			scaley = height.doubleValue() / originalHeight.doubleValue();
		}
		
		if (scalex <= 0.0001) {
			scalex = scaley;
		}
		
		scale = Math.min(scalex, scaley);
		
		if (scale > 0.0) {
			hasScale = true;
			scale = 0.5*scale;
		}
		sb.append("\n\\begin{figure}[!ht]\n");
		sb.append("     \\begin{center}\n");
		if (hasScale) {
			sb.append("         \\includegraphics[scale="+scale+"]{"+fileName+"}\n");			
		} else {
			sb.append("         \\includegraphics[width=0.8\\textwidth,height=0.8\\textheight,keepaspectratio]{"+fileName+"}\n");
		}
		
		if (caption != null) {
			sb.append("\\caption["+caption+"]{\\label{fig:"+fileName+"}"+caption+"}\n");
		}			
		sb.append("   \\end{center}\n");
		sb.append("\\end{figure}\n");

		return sb.toString();
	}

}
