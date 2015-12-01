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

$(document).bind("mobileinit", function() {
  $.mobile.defaultPageTransition = "slide";
  $.mobile.transitionFallbacks.slideout = "none";
});


/**
 * Callback for page init.
 */
$(document).bind("pageinit", function(){

    
    $(document).unbind("swiperight");
    $(document).bind("swiperight", function(){
        var anchor = $("a.prevPage:last").attr("href");
        
        $.mobile.changePage(anchor, {
            transition: "slide",
            reverse: true
        });
     });
     
    $(document).unbind("swipeleft");
    $(document).bind("swipeleft", function(){
        var anchor = $("a.nextPage:last").attr("href");
        
        $.mobile.changePage(anchor, {
            transition: "slide",
            reverse: false
        });
     });
     

    // Change the header text width depending on the orientation.
    // Classes defined in topic.css
    checkOrientation();
   
    // Listen for orientation changes 
    $(window).on("orientationchange", function(event) {
        changeOrientation(event.orientation);
    });
    
    // Scroll to the fragment.
    setTimeout(function(){
      var id = window.location.hash;
      if (id){    
        $(function(){     
              $('html, body').animate({
                scrollTop: $(id).offset().top
              }, 300);      
          });
      }      
    }, 600);
});


function checkOrientation(){
   var orientation = "";
   if(window.innerHeight > window.innerWidth){
        // Portrait
        orientation = "portrait";
  } else {
        // Landscape
        orientation = "landscape";
  }
  changeOrientation(orientation);
}

function changeOrientation(orientation){
   if(orientation === "portrait"){
        // Portrait
        $("h1.pageHeader").removeClass("orientation-landscape");
        $("h1.pageHeader").addClass("orientation-portrait");
  } else {
        // Landscape
        $("h1.pageHeader").removeClass("orientation-portrait");
        $("h1.pageHeader").addClass("orientation-landscape");
  }
}
