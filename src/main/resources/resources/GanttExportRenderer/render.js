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


var system = require('system'),
    page   = require('webpage').create(); 

if (system.args.length === 5) {
    var addresses       = system.args[1].split('|'),
        // strip file info, leave only info about dir
        outputPath      = system.args[0].replace(/\/[^/]+$/, ''),
        // stip any trailing slashes
        urlPath         = system.args[2].replace(/\/$/, ''),
        outputArray     = [],
        size            = system.args[3].split('*');

    page.paperSize      = size.length === 2 ? { width: size[0], height: size[1], orientation: system.args[4], margin: '0px' }
                                       : { format: system.args[3], orientation: system.args[4], margin: '1cm' };
} else {
    console.log('Usage: render.js filenames [paperwidth*paperheight|paperformat]');
    phantom.exit(1);
}

var openPage = function (pages) {
    if (!pages.length) {
        console.log(outputArray.join('|'));
        phantom.exit();
        
        return
    }
    
    var url     = urlPath + '/' + pages.shift()
    
    page.open(url, function (status) {
        if (status !== 'success') {
            console.log("FAIL:" + url)
            phantom.exit(2);
        }
        
        var date            = new Date().getTime();
        var outputFilename  = outputPath + '/print-' + date + '.png';
        
        setTimeout(function () {
            page.render(outputFilename);
            outputArray.push(outputFilename);
            
            setTimeout(function () {
                openPage(pages)    
            }, 1)
        }, 1);        
    });
}

openPage(addresses);
