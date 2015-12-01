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


function getPath(currentUrl) {
    //With Frames
    if (/MSIE (\d+\.\d+);/.test(navigator.userAgent) && location.hostname == '' && currentUrl.search("/") == '0') {
        currentUrl = currentUrl.substr(1);
    }
    path = prefix + "?q=" + currentUrl;
    return path;
}


function highlightSearchTerm() {
    if (parent.termsToHighlight != null) {
        // highlight each term in the content view
        for (i = 0; i < parent.termsToHighlight.length; i++) {
            $('*', window.parent.contentwin.document).highlight(parent.termsToHighlight[i]);
        }
    }
}

$(document).ready(function () {
    $('#permalink').show();
    if ($('#permalink').length > 0) {
        if (window.top !== window.self) {
            if (window.parent.location.protocol != 'file:' && typeof window.parent.location.protocol != 'undefined') {
                $('#permalink>a').attr('href', window.parent.location.pathname + '?q=' + window.location.pathname);
                $('#permalink>a').attr('target', '_blank');
            } else {
                $('#permalink').hide();
            }
        } else {
            $("<div class='frames'><div class='wFrames'><a href=" + getPath(location.pathname) + ">With Frames</a></div></div>").prependTo('.navheader');							        
            $('#permalink').hide();
        }
    }
    
    // Expand toc in case there are frames.
    if (top !== self && window.parent.tocwin) {
        if (typeof window.parent.tocwin.expandToTopic === 'function') {
            window.parent.tocwin.expandToTopic(window.location.href);
        }
    }
});
