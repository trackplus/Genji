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

package com.aurel.track.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * PdfUtils provides method to create and manipulate PDF files.
 *
 */
public class PdfUtils {
	
	private static final Logger LOGGER = LogManager.getLogger(PdfUtils.class);

	public static void createPdfFromText(StringBuilder text, File pdfFile) {
		Document output = null;
		try {
			BufferedReader input = new BufferedReader (new StringReader(text.toString()));
			// Size DIN A4
			//    see com.lowagie.text.PageSize for a complete list of page-size constants.
			output = new Document(PageSize.A4, 40, 40, 40, 40);
			float fntSize, lineSpacing;
		    fntSize = 9f;
		    lineSpacing = 11f;
		    Font font1 = FontFactory.getFont(FontFactory.COURIER, fntSize);
		    Font font2 = FontFactory.getFont(FontFactory.COURIER, fntSize);
		    font2.setColor(Color.BLUE);
		    Font font3 = FontFactory.getFont(FontFactory.COURIER, fntSize);
		    font3.setColor(Color.RED); 
		    
			PdfWriter.getInstance(output, new FileOutputStream (pdfFile));

			output.open();
			output.addAuthor("Steinbeis");
			output.addSubject("Debug Info");
			output.addTitle(pdfFile.getName());

			String line = "";
			while(null != (line = input.readLine())) {
				Font ft = font1;
				if (line.startsWith("%")) {
					ft = font2;
				}
				if (line.startsWith("% ^^^") || line.startsWith("% vvv")) {
					ft = font3;
				}
				Paragraph p = new Paragraph(lineSpacing, line, ft);
				p.setAlignment(Element.ALIGN_JUSTIFIED);
				output.add(p);
			}
			output.close();
			input.close();
		}
		catch (Exception e) {
			LOGGER.debug("Problem creating debug info pdf file",e);
		}
	}

}
