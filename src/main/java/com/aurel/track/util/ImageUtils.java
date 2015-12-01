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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

public class ImageUtils {
	
	public static BufferedImage scaleImage(BufferedImage image, int newWidth, int newHeight, int cornerRadius) {
		
		BufferedImage thumbnail = Scalr.resize(image, Math.min(newWidth,newWidth));
		
		int scaledWidth = thumbnail.getWidth();
		int scaledHeight = thumbnail.getHeight();
		
		int x = (newWidth - scaledWidth) / 2;
        	int y = (newWidth - scaledHeight) / 2;
        
		BufferedImage newImg = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = newImg.createGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, newWidth, newHeight);
		g.drawImage(thumbnail, x, y, x + scaledWidth, y + scaledHeight, 0, 0, scaledWidth, scaledHeight,
				new Color(0, 0, 0, 0), null);
		g.dispose();
		
		if (cornerRadius > 0) {
			newImg = makeRoundedCorner(newImg, cornerRadius);
		}
		return newImg;
	}
	
	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
	    int w = image.getWidth();
	    int h = image.getHeight();
	    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = output.createGraphics();

	    // This is what we want, but it only does hard-clipping, i.e. aliasing
	    // g2.setClip(new RoundRectangle2D ...)

	    // so instead fake soft-clipping by first drawing the desired clip shape
	    // in fully opaque white with antialiasing enabled...
	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

	    // ... then compositing the image on top,
	    // using the white shape from above as alpha source
	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.drawImage(image, 0, 0, null);

	    g2.dispose();

	    return output;
	}
	
}
