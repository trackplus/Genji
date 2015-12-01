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

// Controls the transitions between main tabs.
// Should be no effect there.
 $(document).bind('pageinit', function(){     
    $("a[data-role=tab]").each(function () {
        var anchor = $(this);
        anchor.bind("click", function () {
            $.mobile.changePage(anchor.attr("href"), {
                transition: "none",
                changeHash: true
            });
            return false;
        });
    }); 
    
    // Controls the swipe distance.
    var swipeDistance = screen.width * 2/5;
		$.event.special.swipe.horizontalDistanceThreshold = swipeDistance;
 });

// Avoid presenting the error twice.
var chromeErrorShown = false;

$(document).bind('pageshow', function() {
    
    // Focus the first input in the page.
    $($('.ui-page-active form :input:visible')[0]).focus();


    // Display an error message, for Google Chrome for local files.    
    var addressBar = window.location.href;
    if ( window.chrome && addressBar.indexOf('file://') === 0 && !chromeErrorShown){
        $.mobile.showPageLoadingMsg( 
            $.mobile.pageLoadErrorMessageTheme, 
            "Please move the generated WebHelp output to a web server, "+
            "or use another browser. Google Chrome does not handle this " + 
            "kind of output stored locally on the file system.", true);
        chromeErrorShown = true;
    }
});

// Declare the function for open and highlight, but disable it.
function openAndHighlight(){
    return true;    
}
