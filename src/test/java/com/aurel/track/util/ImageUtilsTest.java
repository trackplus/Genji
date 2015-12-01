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

package com.aurel.track.util;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class ImageUtilsTest {
	
	BufferedImage in, scaled;
	String workingDirectory = System.getProperty("user.dir");
	String fileSeperator = System.getProperty("file.separator");
	String pathToFile;

	@Before
	public void setUp(){
		// File img = new File("C:/program files/Trackplus/logos/trackLogo.png");	// WINDOWS ONLY
		
		pathToFile = workingDirectory + fileSeperator + "homet" + fileSeperator + "logos" + fileSeperator + "trackLogo.png";
		File img = new File(pathToFile);
		
		try {
			in = ImageIO.read(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Test
	public void testScaleImage() {
		scaled = ImageUtils.scaleImage(in, 98, 40, 0);	// Image Width Height Radius
		
		assertTrue(scaled.getWidth()==98 && scaled.getHeight()==40);
	}
	
	@Test
	public void testMakeRoundCorners() {
		scaled = ImageUtils.scaleImage(in, 98, 40, 1);	// Image Width Height Radius
		
		assertNotNull(scaled);
	}
}
