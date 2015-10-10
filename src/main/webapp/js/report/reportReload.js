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

var reloadPage=true;//reload page or not
var delay=1000;//miliseconds between scroll change
var step=23;// the size (pixel) step of scroll
var delayIfNoScroll=10000;// time in miliseconds(ms) 1000 ms=1s, 10000 =10seconds;

function scrollTable(pos){
	var oDiv=document.getElementById("tableBodyPane");
	oDiv.scrollTop = pos;
	var next;
	if(oDiv.offsetHeight+oDiv.scrollTop>=oDiv.scrollHeight){
		if(reloadPage){
			doReloadPage();
			return;
		}else{
			next=0;
		}
	}else{
		next=pos+step;
	}
	setTimeout("scrollTable("+next+")",delay);
}
function doReloadPage(){
	var urlStr="itemNavigator.action";
	var d=new Date();
	myDate=d.getTime();

	//add time to url
	var idx=urlStr.indexOf("?");
	if(idx==-1){
		//no url params
		urlStr=urlStr+"?time="+myDate;
	}else{
		var idxTime=urlStr.indexOf("?time=");
		if(idxTime!=-1){
			urlStr=urlStr.substring(0,idxTime)+"?time="+myDate;
		}else{
			//there are parameters in the url
			var idxTime1=urlStr.indexOf("&time=");
			if(idxTime1!=-1){
				urlStr=urlStr.substring(0,idxTime1)+"&time="+myDate;
			}else{
				urlStr=urlStr+"&time="+myDate;
			}
		}
	}
	window.location.href=urlStr;
}
function initialize(){
	var oDiv=document.getElementById("tableBodyPane");
	var hasScrool=(oDiv.clientHeight < oDiv.scrollHeight);
	if(hasScrool){
		scrollTable(0);
	}else{

		if(reloadPage){
			setTimeout("doReloadPage()",delayIfNoScroll);
		}
	}
}
window.onload=initialize;
