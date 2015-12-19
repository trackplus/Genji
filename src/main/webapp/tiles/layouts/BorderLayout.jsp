<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<%-- $Id: BorderLayout.jsp 1903 2015-12-18 16:37:00Z tamas $ --%>

<s:if test="#application.APPLICATION_BEAN==null">
    <div> Try again later...</div>
    <%
    response.sendRedirect(response.encodeRedirectURL("logon.jsp"));
    %>
</s:if>
<s:else>
<html>
<%
	/* set the context path as a request attribute because
	only the request attributes map is available from OGNL
	(not the entire HttpRequest object ) */
	request.setAttribute("appContextPath", request.getContextPath());
%>

<jsp:useBean id="userInfo" class="com.aurel.track.util.UserInfo" scope="request" >
	<jsp:setProperty name="userInfo" property="request" value="<%= request %>"/>
</jsp:useBean>
<jsp:useBean id="APPLICATION_BEAN" type="com.aurel.track.prop.ApplicationBean" scope="application"/>
	<%
		String debug=request.getParameter("debug");
		if(debug==null){
			debug=(String)session.getAttribute("debug");
		}else{
			session.setAttribute("debug", debug);
		}
        String testing=request.getParameter("testing");
        if(testing==null){
            testing=(String)session.getAttribute("testing");
        }else{
            session.setAttribute("testing", testing);
        }
		String mainCls=request.getParameter("mainCls");
		if(mainCls==null){
			mainCls=(String)session.getAttribute("mainCls");
		}else{
			session.setAttribute("mainCls", mainCls);
		}
		String externalCss=request.getParameter("externalCss");
		if(externalCss==null){
			externalCss=(String)session.getAttribute("externalCss");
		}else{
			session.setAttribute("externalCss", externalCss);
		}
		debug="true"; // REMOVE_ON_BUILD, don't change this line here, since the build process relies on the tag
		testing="true";
		String fmresp = "";
	%>
<head>
	<%-- title --%>
	<title>
		<s:if test="#request.BUNDLENAME==null">
			<s:if test="pageTitle!=null">
				<s:text name="%{pageTitle}"></s:text>
			</s:if>
			<s:else>
				<s:text name="%{#request.title}"></s:text>
			</s:else>
		</s:if>
		<s:else>
			<s:i18n name="%{#request.BUNDLENAME}">
				<s:if test="pageTitle!=null">
					<s:text name="%{pageTitle}"></s:text>
				</s:if>
				<s:else>
					<s:text name="%{#request.title}"></s:text>
				</s:else>
			</s:i18n>
		</s:else>
		<s:property value="#request.key"/>
	</title>
	<%-- meta info --%>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="Pragma" CONTENT="no-cache">
	<meta http-equiv="Expires" content="-1">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<!-- shortcut icon -->
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">

	<!-- define paths to other resource files: .css, .js, etc. -->
	<s:if test="#session.DESIGNPATH==null||#session.DESIGNPATH!='silver'">
		<s:set name="userDesignPath" value="%{'silver'}" scope="session"/>
	</s:if>
	<s:else>
		<s:set name="userDesignPath" value="#session.DESIGNPATH" scope="session"/>
	</s:else>

<%
	String theme=request.getParameter("theme");
	if(theme==null){
		theme=(String)session.getAttribute("theme");
		if(theme==null) {
			theme = "silver";
		}
	}
	session.setAttribute("theme", theme);
%>

<%
	String helpLocalePath="";
	if (session.getAttribute("HELPLOCALE") == null || "".equals(session.getAttribute("HELPLOCALE"))) {
		helpLocalePath="help/";
	} else {
		helpLocalePath="help/"+session.getAttribute("HELPLOCALE")+"/";
	}
%>
	<jsp:useBean id="EXTJSLOCALE" scope="session" class="java.lang.String"/>
	<!--<jsp:useBean id="extjsLocale" scope="request" class="java.lang.String"/>
	 <s:if test="#session.EXTJSLOCALE==null">
		<s:set name="extjsLocale" value="de"  scope="request"/>
	</s:if><s:else>
		<s:set name="extjsLocale" value="#session.EXTJSLOCALE" scope="request"/>
	</s:else> -->

	<s:set name="designPath"
		value="#request.appContextPath+'/design/'+#session.userDesignPath"
		scope="request"/>

	<s:set name="iconsPath" value="#request.designPath+'/icons/'"/>
	<s:set name="htmlEditorCSS" value="#request.designPath+'/trackHtmlEditorArea.css'"/>
	<s:set name="listIconsPath" value="#request.appContextPath+'/design/listIcons/'"/>
	<s:set name="imgPath" value="#request.designPath+'/img/'"/>

	<s:set name="icon16Path" value="#request.designPath+'/16x16/'"/>
	<s:set name="icon32Path" value="#request.designPath+'/32x32/'"/>

	<jsp:useBean id="alignR" scope="session" class="java.lang.String"/>
	<jsp:useBean id="alignL" scope="session" class="java.lang.String"/>
	<jsp:useBean id="dir" scope="session" class="java.lang.String"/>

	<s:set name="alignR" value="#session.alignR" scope="request"/>
	<s:set name="alignL" value="#session.alignL" scope="request"/>
	<s:set name="dir"    value="#session.dir"    scope="request"/>

	<%-- define the beans explicitly to make them available in the scriptlets --%>
	<jsp:useBean id="designPath" scope="request" class="java.lang.String"/>
	<jsp:useBean id="imgPath" scope="request" class="java.lang.String"/>
	<jsp:useBean id="iconsPath" scope="request" class="java.lang.String"/>
	<jsp:useBean id="listIconsPath" scope="request" class="java.lang.String"/>
	<jsp:useBean id="htmlEditorCSS" scope="request" class="java.lang.String"/>

	<jsp:useBean id="icon16Path" scope="request" class="java.lang.String"/>
	<jsp:useBean id="icon32Path" scope="request" class="java.lang.String"/>

	<jsp:useBean id="EXTJSDATEFORMAT" scope="session" class="java.lang.String"/>
	<jsp:useBean id="EXTJSSUBMITDATEFORMAT" scope="session" class="java.lang.String"/>
	<jsp:useBean id="EXTJSDATETIMEFORMAT" scope="session" class="java.lang.String"/>
	<jsp:useBean id="EXTJSTIMEFORMAT" scope="session" class="java.lang.String"/>
	<jsp:useBean id="EXTJSTIMEFORMATNOSECONDS" scope="session" class="java.lang.String"/>
	<jsp:useBean id="EXTJSDECIMALSEPARATOR" scope="session" class="java.lang.String"/>


	<%
		String ua=request.getHeader("User-Agent");
		boolean isIE9=false;
		boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
		if(isMSIE) {
			String tempStr = ua.substring(ua.indexOf("MSIE"), ua.length());
			String version = tempStr.substring(4, tempStr.indexOf(";")).trim();
			isIE9 = version.startsWith("9");
		}
	%>

	<s:if test="#session.debug!=null">
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ext-all-debug.js?v=<s:property value='#application.TVERSION.label'/>"></script>
 	   <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/packages/charts/classic/charts-debug.js?v=<s:property value='#application.TVERSION.label'/>"></script>
	</s:if>

    <s:if test="#session.debug==null">
	   <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ext-all-debug.js?v=<s:property value='#application.TVERSION.label'/>"></script>
	   <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/packages/charts/classic/charts-debug.js?v=<s:property value='#application.TVERSION.label'/>"></script>
	</s:if>

	<%
	  if (com.aurel.track.prop.ApplicationBean.getInstance().getLicenseManager() != null) {
		   fmresp=com.aurel.track.prop.ApplicationBean.getInstance().getLicenseManager().getFeatureScripts(request,debug);
	  } else fmresp="";
	%>
	<%= fmresp %>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/classic/locale/locale-<%=EXTJSLOCALE%>.js?v=<s:property value='#application.TVERSION.label'/>"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/log4javascript_production.js"></script>

	<s:set name="ckEditorPath" value="#request.appContextPath+'/js/ckeditor/'"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/ckeditor/ckeditor.js?v=<s:property value='#application.TVERSION.label'/>"></script>

	<s:if test="#request.dir=='rtl'">
	  <link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/resources/css/ext-all-rtl.css?v=<s:property value="#application.TVERSION.label"/>'/>
	  <link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/packages/charts/classic/classic/charts-all-debug.scss?v=<s:property value="#application.TVERSION.label"/>'/>
	</s:if>
	<s:else>
		<s:if test="#session.theme!=null">
			<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-<s:property value="#session.theme"/>/resources/theme-<s:property value="#session.theme"/>-all.css?v=<s:property value="#application.TVERSION.label"/>'/>
		</s:if>
		<s:else>
			<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-gray/resources/theme-gray-all.css?v=<s:property value="#application.TVERSION.label"/>'>
			<!--<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-silver/resources/theme-silver-all.css?v=<s:property value="#application.TVERSION.label"/>'>-->
			<!--<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-crisp/resources/theme-crisp-all.css?v=<s:property value="#application.TVERSION.label"/>'> -->
			<!--<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-crisp-touch/resources/theme-crisp-touch-all.css?v=<s:property value="#application.TVERSION.label"/>'>-->
			<!--<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-aria/resources/theme-aria-all.css?v=<s:property value="#application.TVERSION.label"/>'>-->
			<!-- <link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/classic/theme-triton/resources/theme-triton-all.css?v=<s:property value="#application.TVERSION.label"/>'>  -->
		</s:else>
	</s:else>
	<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/js/ext/ux/css/CheckHeader.css?v=<s:property value="#application.TVERSION.label"/>'/>

	<!-- stylesheet resources -->

	<s:if test="#session.theme!=null">
		<link rel="stylesheet" type="text/css" href='design/<s:property value="#session.theme"/>/style-<s:property value="#session.theme"/>.css?v=<s:property value="#application.TVERSION.label"/>'>
	</s:if>
	<s:else>
		<link rel="stylesheet" type="text/css" href='<%=designPath%>/style.css?v=<s:property value="#application.TVERSION.label"/>'>
		<link rel="stylesheet" type="text/css" href='<%=designPath%>/style-ext.css?v=<s:property value="#application.TVERSION.label"/>'>
		<link rel="stylesheet" type="text/css" href='<%=designPath%>/style-icons.css?v=<s:property value="#application.TVERSION.label"/>'>
		<link rel="stylesheet" type="text/css" href='<%=designPath%>/treenodeStyles.css?v=<s:property value="#application.TVERSION.label"/>'>
		<link rel="stylesheet" type="text/css" href='<%=designPath%>/daisyDiff.css?v=<s:property value="#application.TVERSION.label"/>'>
		<link rel="stylesheet" type="text/css" href='<%=designPath%>/lightbox.css'/>
	</s:else>

	<%
/* ## */	  if (com.aurel.track.prop.ApplicationBean.getInstance().getLicenseManager() != null) {
/* ## */		  fmresp=com.aurel.track.prop.ApplicationBean.getInstance().getLicenseManager().getFeatureStyles(request);
/* ## */	  } else
		  fmresp="";
	%>
	<%= fmresp %>

	<s:if test="#session.externalCss!=null">
		<link rel="stylesheet" type="text/css" href="<s:property value='#session.externalCss'/>"/>
	</s:if>


	<script type="text/javascript">
		var sBasePath ='<s:property value="ckEditorPath"/>';

		Ext.state.Manager.setProvider(new Ext.state.CookieProvider({
			expires: new Date(new Date().getTime()+(1000*60*60*24*7)) //7 days from now
		}));
		Ext.Loader.setConfig({enabled:true, disableCaching:false});
		Ext.Loader.setPath('Ext.ux', '../ux');
		<%=com.aurel.track.plugin.JavaScriptPathExtenderAction.getDirs()%>


		/*global variables*/
		var APPTYPE_DESK = <s:property value="#application.APPLICATION_BEAN.AppTypeDesk"/>;
		var APPTYPE_BUGS = <s:property value="#application.APPLICATION_BEAN.AppTypeBugs"/>;
		var isDemo = <s:property value="#application.APPLICATION_BEAN.SiteBean.IsDemoSiteBool"/>;
		var hasAlm = false;
		<s:if test="#session.user!=null && #session.user.licensedFeaturesMap!=null">
			<s:if test="#session.user.licensedFeaturesMap['alm']!=null">
				hasAlm=<s:property value="#session.user.licensedFeaturesMap['alm'].booleanValue()"/>;
			</s:if>
		</s:if>
		var com={
			trackplus:{
				TrackplusConfig:{
					contextPath:"<%= request.getContextPath()%>",
					imgPath:'<s:property value="#imgPath"/>',
					iconsPath:'<s:property value="#iconsPath"/>',
					htmlEditorCSS:'<s:property value="#htmlEditorCSS"/>',
					listIconsPath:'<s:property value="#listIconsPath"/>',
					issueTypeIconsPath:'<s:property value="#listIconsPath"/>-2/',
					icon16Path:'<s:property value="#icon16Path"/>',
					icon32Path:'<s:property value="#icon32Path"/>',
					<s:if test="#application.APPLICATION_BEAN.SiteBean.isSelfRegisterAllowedBool==true">
					   isSelfRegAllowed:true,
					</s:if>
					<s:if test="#application.APPLICATION_BEAN.SiteBean.isSelfRegisterAllowedBool!=true">
						isSelfRegAllowed:false,
					</s:if>
					i17n:new Object(),
					haveLocalization:function(key){
						return (this.i17n[key]!=null);
					},
					getText:function(str){
						if(this.i17n[str]!=null){
							var text = this.i17n[str];
							if (arguments!=null){
							 	var i=1;
							    while(i<arguments.length) {
							    	//var regexp = "\\{"+(i-1)+"\\}";
							    	//text=text.replace(/regexp/g,arguments[i++]);
								    //text=text.replace("{"+(i-1)+"}",arguments[i++]);
							    	text=text.replace(new RegExp("\\{"+(i-1)+"\\}", 'g'), arguments[i++]);
							    }
							}
							return text;
						}
						return str+"!";
					},
					appType: <s:property value="#application.APPLICATION_BEAN.appType"/>,
					user:{
						alm: hasAlm,
						privateWorkspace: <s:property value='%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[521] }'/>,
						configureCockpit: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[101]}"/>,
						hasProjectCockpit: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[200]}"/>,
						hasCockpit: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[100]}"/>,
						itemNavigator: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[300]}"/>,
						reports: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[400]}"/>,
						manageFilters: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[541]}"/>,
						reportTemplates: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[542]}"/>,
						administration: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[500]}"/>,
						userRoles: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[543]}"/>,
						forms: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[544]}"/>,
						fields: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[545]}"/>,
						lists: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[546]}"/>,
						userLevels: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[547]}"/>,
						workflows: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[548]}"/>,
						actions: <s:property value="%{#session.user!=null && #session.user.userLevelMap!=null && #session.user.userLevelMap[550]}"/>,
						sysAdmin:<s:property value="%{#session.user!=null && #session.user.isSysAdmin==true}"/>,
						sys:<s:property value="%{#session.user!=null && #session.user.sys==true}"/>,
						projectAdmin:<s:property value="%{#session.user!=null && #session.user.projAdmin==true}"/>
					}
				},
				logon:{},
				dashboard:{},
				report:{},
				browseProjects:{},
				layout:{},
				item:{},
				itemNavigator:{},
				itemDetail:{},
				admin:{
					user:{},
					action:{}
				},
				util:{},
				//TODO move in a different js file
				JSON_FIELDS:{
					SUCCESS       : "success",
					ERROR_MESSAGE : "errorMessage",
					RECORDS 	  : "records",
					DATA : "data",
					ID :"id",
					OBJECT_ID : "objectID",
					LABEL : "label",
					NAME : "name",
					VALUE : "value",
					DATA_SOURCE : "dataSource",
					DESCRIPTION : "description",

					//the tree uses text for label
					TEXT : "text",
					NODE : "node",
					SHOW_GRID : "showGrid",


					DISABLED : "disabled",

					LEAF : "leaf",
					ICON : "icon",

					REPLACEMENT_WARNING : "replacementWarning",
					REPLACEMENT_LIST_LABEL : "replacementListLabel",
					REPLACEMENT_LIST : "replacementList"
				}
			}
		};

		com.trackplus.TrackplusConfig.isDebugEnabled=false;
		<s:if test="#session.debug!=null">
		com.trackplus.TrackplusConfig.isDebugEnabled=<s:property value="#session.debug"/>;
		</s:if>

		var userName=null;
        var userID=null;
		<s:if test="#session.user!=null">
			userName="<%= userInfo.getFirstName() + userInfo.userDelimeter + userInfo.getLastName()%>";
            userID=<s:property value="%{#session.user.objectID}"/>;
		</s:if>
		com.trackplus.TrackplusConfig.userName=userName;
		com.trackplus.TrackplusConfig.userID=userID;

		com.trackplus.TrackplusConfig.statusUpdateVersion="";
        com.trackplus.TrackplusConfig.statusLicense="";
    	<%
  	       if (com.aurel.track.prop.ApplicationBean.getInstance().getLicenseManager() != null) {
  		       fmresp=com.aurel.track.prop.ApplicationBean.getInstance().getLicenseManager().getFeatureGlobalVars(request);
  	       } else fmresp="";
  	    %>
  	    <%= fmresp %>

        <s:if test="#session.user.sys==true || #session.user.projAdmin">
        	<s:if test="#application.APPLICATION_BEAN.newerVersion==true">
        	com.trackplus.TrackplusConfig.statusUpdateVersion='<s:property value="%{getText('common.mostActualVersion')}" escape="false" escapeJavaScript="true"/> ' + '<s:property value="#application.APPLICATION_BEAN.mostActualVersionString"/>';
        	</s:if>
        </s:if>
        <s:if test="#application.APPLICATION_BEAN.daysLicenseValid <= 30 && #application.APPLICATION_BEAN.daysLicenseValid > 1">
        	com.trackplus.TrackplusConfig.statusLicense='<div class="statusLicense"><s:text name="common.daysLicenseValid"> <s:param><s:property value="#application.APPLICATION_BEAN.daysLicenseValid"/></s:param></s:text></div>';
        </s:if>
        	<s:if test="#application.APPLICATION_BEAN.daysLicenseValid == 1">
        	com.trackplus.TrackplusConfig.statusLicense='<div class="statusLicense"><s:text name="common.dayLicenseValid"/></div>';
        	</s:if>
        	<s:if test="#application.APPLICATION_BEAN.daysLicenseValid <= 0">
        	com.trackplus.TrackplusConfig.statusLicense='<div class="statusLicense"><s:text name="logon.err.license.expired"/>';
        	</s:if>
        	com.trackplus.TrackplusConfig.statusVersion='<s:property value="%{getText('common.version')}" escape="false" escapeJavaScript="true"/>'
        	+ ' ' + '<s:property value="#application.TVERSION.label"/>'
        	+ ' ' + '<s:property value="#application.APPLICATION_BEAN.appTypeString"/>'
        	+ ' ' + '<s:property value="#application.APPLICATION_BEAN.editionString"/>'
        	+ '&nbsp;&nbsp;&nbsp;' + '<s:property value="getText('common.copyright')"/>'
        	+ '&nbsp;&nbsp;';

		<s:if test="#session.localizationJSON!=null">
			com.trackplus.TrackplusConfig.i17n=<s:property value="#session.localizationJSON" escape="false"/>;
		</s:if>
		<s:if test="#session.issueTypesJSON!=null">
			com.trackplus.TrackplusConfig.issueTypes=<s:property value="#session.issueTypesJSON" escape="false"/>;
		</s:if>
		<s:if test="#session.myMenuFiltersJSON!=null">
			com.trackplus.TrackplusConfig.myMenuFilters=<s:property value="#session.myMenuFiltersJSON" escape="false"/>;
		</s:if>
		<s:if test="#session.lastExecutedFiltersJSON!=null">
		com.trackplus.TrackplusConfig.lastExecutedFilters=<s:property value="#session.lastExecutedFiltersJSON" escape="false"/>;
		</s:if>

        <s:if test="#session.toolbarPluginsJSON!=null">
        com.trackplus.TrackplusConfig.toolbarPlugins=<s:property value="#session.toolbarPluginsJSON" escape="false"/>;
        </s:if>

        <s:if test="#session.shortcutsJSON!=null">
        com.trackplus.TrackplusConfig.shortcutsJSON=<s:property value="#session.shortcutsJSON" escape="false"/>;
        </s:if>

        <s:if test="#session.usedModulesJSON!=null">
		com.trackplus.TrackplusConfig.usedModulesJSON=<s:property value="#session.usedModulesJSON" escape="false"/>;
		</s:if>

		<s:if test="#session.loggedInPersonUserLevel!=null">
		com.trackplus.TrackplusConfig.loggedInPersonUserLevel=<s:property value="#session.loggedInPersonUserLevel" escape="false"/>;
		</s:if>

		<s:if test="#session.clientUserLevelID!=null">
		com.trackplus.TrackplusConfig.clientUserLevelID=<s:property value="#session.clientUserLevelID" escape="false"/>;
		</s:if>

		<s:if test="#session.MAXFILESIZE!=null">
		com.trackplus.TrackplusConfig.MAXFILESIZE=<s:property value="#session.MAXFILESIZE"/>;
		</s:if>

		<s:if test="#session.containerBasedAuthentication!=null">
        com.trackplus.TrackplusConfig.CONTAINERBASEDAUTHENTICATION=<s:property value="#session.containerBasedAuthentication"/>;
        </s:if>

        /*
        com.trackplus.TrackplusConfig.toolbarPlugins=[
            {id:'wiki',cls:'wiki',name:'Wiki',description:'Wiki description',url:'wiki.action',fullScreen:true},
            {id:'jenkins',cls:'jenkins',name:'Jenkins',description:'Jenkins description',url:'http://www.trackplus.com/jenkins'},
            {id:'contact',cls:'buttonEmail',name:'Contact',description:'Contact trackplus description',url:'http://www.trackplus.com/task-management-support.html'}
        ];*/


		var helpPaths=new Object();
		helpPaths['general']='WebHelp/index.html';
		helpPaths['logon']='WebHelp/index.html';
		helpPaths['dashboard']='WebHelp/index.html#Topics/03ForTeamMembers/cockpit/cockpit.html';
		helpPaths['dashboardEdit']='WebHelp/index.html#Topics/03ForTeamMembers/cockpit/changeLayout.html';
		helpPaths['browseProject']='WebHelp/index.html#Topics/03ForTeamMembers/browseProjects/pcockpit.html';
		helpPaths['itemNavigator']='WebHelp/index.html#Topics/03ForTeamMembers/itemNavigator/itemNavigator.html';
		helpPaths['reportConfig']='WebHelp/index.html#Topics/03ForTeamMembers/reporting/reporting.html';

		helpPaths['admin.myPreferenceSection.myProfile']='WebHelp/index.html#Topics/03ForTeamMembers/profile/userProfile.html';
		helpPaths['admin.myPreferenceSection.myAutomail']='WebHelp/index.html#Topics/03ForTeamMembers/notifications/configAutomail.html';
		helpPaths['admin.myPreferenceSection.iCalendarURL']='WebHelp/index.html#Topics/03ForTeamMembers/profile/iCalendar.html';

		helpPaths['admin.projectTreePanel']='WebHelp/index.html#Topics/04ForProjectAdmins/cProjects.html';
		helpPaths['admin.projectTemplateTreePanel']='WebHelp/index.html#Topics/04ForProjectAdmins/cProjects.html';

		helpPaths['admin.usersSection.users']='WebHelp/index.html#Topics/06ForSystemManagers/usersAndGroups/managingUsers.html';
		helpPaths['admin.usersSection.clients']='WebHelp/index.html#Topics/06ForSystemManagers/usersAndGroups/managingClients.html';
		helpPaths['admin.usersSection.groups']='WebHelp/index.html#Topics/06ForSystemManagers/usersAndGroups/managingGroups.html';
		helpPaths['admin.usersSection.departments']='WebHelp/index.html#Topics/06ForSystemManagers/usersAndGroups/managingDepartments.html';
		helpPaths['admin.usersSection.dashboardAssign']='WebHelp/index.html#Topics/06ForSystemManagers/usersAndGroups/defaultCockpit.html';

		helpPaths['admin.customizationSection.queryFilters']='WebHelp/index.html#Topics/05ForConfigManagers/manageFilters.html';
		helpPaths['admin.customizationSection.reportTemplates']='WebHelp/index.html#Topics/05ForConfigManagers/reportTemplates/manageReportTemplates.html';
		helpPaths['admin.customizationSection.roles']='WebHelp/index.html#Topics/05ForConfigManagers/roles/Roles.html';
		helpPaths['admin.customizationSection.accounts']='WebHelp/index.html#Topics/05ForConfigManagers/accounting/accounting.html';
		helpPaths['admin.customizationSection.defaultAutomail']='WebHelp/index.html#Topics/05ForConfigManagers/automail/automailOverview.html';
		helpPaths['admin.customizationSection.linkTypes']='WebHelp/index.html#Topics/05ForConfigManagers/linkTypes/linkTypes.html';
		helpPaths['admin.customizationSection.customForms']='WebHelp/index.html#Topics/05ForConfigManagers/forms/cCustomForms.html';
		helpPaths['admin.customizationSection.customFields']='WebHelp/index.html#Topics/05ForConfigManagers/fields/customFields.html';
		helpPaths['admin.customizationSection.pickLists']='WebHelp/index.html#Topics/05ForConfigManagers/lists/lists.html';
		helpPaths['admin.customizationSection.objectStatus']='WebHelp/index.html#Topics/05ForConfigManagers/systemStates/systemStates.html';
		helpPaths['admin.customizationSection.projectTypes']='WebHelp/index.html#Topics/05ForConfigManagers/projectTypes/projectTypes.html';
		helpPaths['admin.customizationSection.workflows']='WebHelp/index.html#Topics/05ForConfigManagers/workflows/workflows.html';
		helpPaths['admin.customizationSection.localization']='WebHelp/index.html#Topics/05ForConfigManagers/localeEditor/localeEditor.html';
		helpPaths['admin.customizationSection.scripts']='WebHelp/index.html#Topics/05ForConfigManagers/scripts/scripts.html';
		helpPaths['admin.customizationSection.mailTemplates']='WebHelp/index.html#Topics/05ForConfigManagers/mailTemplates/mailTemplates.html';
		helpPaths['admin.customizationSection.dashboardAssign']='WebHelp/index.html#Topics/05ForConfigManagers/cockpit/assignCockpit.html';

		helpPaths['admin.actionSection.import']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/importOverview.html';
		helpPaths['admin.actionSection.importExcel']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/importExcel.html';
		helpPaths['admin.actionSection.importMsProject']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/importMSProject.html';
		helpPaths['admin.actionSection.importProjectType']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/importProjectTypes.html';
		helpPaths['admin.actionSection.importTrackplus']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/importTrackItems.html';
		helpPaths['admin.actionSection.importWorkflow']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/importWorkflows.html';

		helpPaths['admin.actionSection.exportProjectTypes']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/exportProjectTypes.html';
		helpPaths['admin.actionSection.exportMsProject']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/exportMSProject.html';
		helpPaths['admin.actionSection.exportWorkflows']='WebHelp/index.html#Topics/06ForSystemManagers/Actions/exportWorkflows.html';

		helpPaths['admin.serverSection.serverConfiguration']='WebHelp/index.html#Topics/07ForSystemAdmins/serverConfiguration/serverConfig.html';
 		helpPaths['admin.serverSection.logonPageText']='WebHelp/index.html#Topics/07ForSystemAdmins/logonPageText.html';
 		helpPaths['admin.serverSection.broadcastEmail']='WebHelp/index.html#Topics/07ForSystemAdmins/broadcastEmails.html';
 		helpPaths['admin.serverSection.serverStatus']='WebHelp/index.html#Topics/07ForSystemAdmins/statusPage.html';
 		helpPaths['admin.serverSection.loggingConfiguration']='WebHelp/index.html#Topics/07ForSystemAdmins/configLogging.html';
 		helpPaths['admin.serverSection.dataBackup']='WebHelp/index.html#Topics/07ForSystemAdmins/databackup.html';
 		helpPaths['admin.serverSection.dataRestore']='WebHelp/index.html#Topics/07ForSystemAdmins/datarestore.html';

 		helpPaths['wiki']='WebHelp/index.html#Topics/10Wiki/Chapter10Wrapper.html';


		com.trackplus.TrackplusConfig.helpPaths=helpPaths;
		com.trackplus.TrackplusConfig.helpPagePrefixURL=com.trackplus.TrackplusConfig.contextPath+'/<%=helpLocalePath%>';



		//configuration will be created after borderLayout.js is included
		var borderLayoutCfg;
		//com.trackplus.layout.BorderLayoutConfig.instance;

		var borderLayout;
		///com.trackplus.layout.BorderLayout.instance;


		function getLocale(){
			return '<%=EXTJSLOCALE%>';
		}
        function getScaytLocale(){
            var locale=getLocale();
            if(locale.indexOf("_")!=-1){
                return locale;
            }
            return locale+"_"+locale.toUpperCase();
        }
		com.trackplus.TrackplusConfig.DateFormat="<%=EXTJSDATEFORMAT%>";
		com.trackplus.TrackplusConfig.DateSubmitFormat="<%=EXTJSSUBMITDATEFORMAT%>";
		com.trackplus.TrackplusConfig.DateTimeFormat="<%=EXTJSDATETIMEFORMAT%>";
		com.trackplus.TrackplusConfig.TimeFormat="<%=EXTJSTIMEFORMAT%>";
		com.trackplus.TrackplusConfig.TimeFormatNoSeconds="<%=EXTJSTIMEFORMATNOSECONDS%>";
		com.trackplus.TrackplusConfig.DecimalSeparator="<%=EXTJSDECIMALSEPARATOR%>";

		//in accordance with yyyy-MM-dd (the iso date received from the server)
		com.trackplus.TrackplusConfig.ISODateFormat="Y-m-d";
		//in accordance with yyyy-MM-dd H:mm:ss (the iso date time received from the server)
		com.trackplus.TrackplusConfig.ISODateTimeFormat="Y-m-d G:i:s";

		Ext.Date.patterns = {
				ShortDate: "<%=EXTJSDATEFORMAT%>",
				//LongDateTime: "<%=EXTJSDATETIMEFORMAT%>",
				ShortTime: "G:i",
				LongTime: "G:i:s",
				SortableDateTime: "Y-m-d\\TH:i:s",
				UniversalSortableDateTime: "Y-m-d H:i:sO",
				YearMonth: "F, Y"
			};

		function getDate(theDate){
			var dd = Ext.Date.parseDate(theDate, "Y-m-d G:i:s");
			return Ext.Date.format(dd, Ext.Date.patterns.ShortDate);
		}

		function getTime(theDate){
			var dd = Ext.Date.parseDate(theDate, "Y-m-d G:i:s");
			return Ext.Date.format(dd, Ext.Date.patterns.ShortTime);
		}

		/*function getDateTime(theDate){
			var dd = Ext.Date.parseDate(theDate, "Y-m-d G:i:s");
			return Ext.Date.format(dd, Ext.Date.patterns.LongDateTime);
		}*/

		/*function getShortDateTime(theDate){
			var dd = Ext.Date.parseDate(theDate, "Y-m-d H:i:s");
			return Ext.Date.format(dd, Ext.Date.patterns.ShortDateTime);
		}*/

	</script>

	<script type="text/javascript" src="<%=request.getContextPath()%>/js/layouts/borderLayout.js?v=<s:property value='#application.TVERSION.label'/>"></script>

	<script type="text/javascript">
		var initData={};
		<s:if test="hasInitData">
			initData=<s:property value="initData" escape="false"/>;
		</s:if>

		/*session expire*/
		var timer;
		Ext.Ajax.on('beforerequest',onBeforeRequest);
		function onBeforeRequest(){
			clearTimeout(timer);
			timer = setTimeout(alertSessionExpire,waitTime * 1000);
		}
		var secondsBeforeExpire = <%=pageContext.getSession().getMaxInactiveInterval()%>;
		var timeToDecide = 120;
		var inactive = <%=new Integer(pageContext.getSession().getMaxInactiveInterval()/60).toString()%>;
		var msge='<s:property value="%{getText('common.sessionWillExpire',new java.lang.String[]{'2'})}" escape="false" escapeJavaScript="true"/>';
		var urls = window.location.href;

		function alertSessionExpire(){
			setTimeout(logout,(timeToDecide+1)*1000);
			if (urls.indexOf("logoff.action",5) == -1) {
				Ext.MessageBox.show({
					   title:'',
					   msg: getText('common.sessionInactive',inactive) + '</br>' + msge,
					   buttons: Ext.Msg.OK,
					   buttonText: {ok:getText('common.btn.continue')},
					   icon: Ext.Msg.WARNING,
					   width: 350,
					   cls: 'expireBox',
					   fn: function() {
						   window.location.reload();
					   }
					});
			}
		}
		var waitTime = secondsBeforeExpire - timeToDecide;
		if (waitTime < 60) waitTime = 60;

		timer = setTimeout(alertSessionExpire,waitTime * 1000);

		function logout(){
			var posi = 0;
			posi = urls.lastIndexOf('/');
			urls = urls.substring(0,posi);
			window.location.href=urls+'/logoff.action';
		}

		function reload(){
			 window.location.reload();
		}
	</script>
	<script type="text/javascript">
	//<![CDATA[
	var log = log4javascript.getLogger();
	var ajaxAppender = new log4javascript.AjaxAppender("logClient.action");
	var httpPostLayout = ajaxAppender.getLayout();
	httpPostLayout.setKeys("loggerClient", "timeStampClient", "levelClient", "messageClient", "exceptionClient", "urlClient");
	ajaxAppender.setLayout(httpPostLayout);
	ajaxAppender.setThreshold(log4javascript.Level.DEBUG);
	// log.addAppender(ajaxAppender);
	// log.debug("Debug message");
	// log.warn("Warning message");
	// log.error("Error message");
	//]]>
	</script>

	<%
		if("true".equalsIgnoreCase(debug)){
	%>
		<%--INCLUDE ALL JS --%>
       <!--
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/FiltersFeature.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/menu/ListMenu.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/menu/RangeMenu.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/Filter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/BooleanFilter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/DateFilter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/DateTimeFilter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/ListFilter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/NumericFilter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/grid/filter/StringFilter.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/CheckColumn.js"></script>
		-->

        <script type="text/javascript" src="<%=request.getContextPath()%>/js/util/control.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/upload.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/messaging.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/ColorPicker.js"></script>

	    <script type="text/javascript" src="<%=request.getContextPath()%>/js/util/AbstractMultiplePicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/MultipleSelectPicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/SingleSelectPicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/MultipleTreePicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/SingleTreePicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/PersonPickerDialog.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/LinkColumn.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/linkPicker/LinkPicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/linkPicker/LinkPickerView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/linkPicker/LinkPickerController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/IssuePicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/NameAndPathPicker.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/Tab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/TabGrid.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/ItemDetail.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/AttachmentsTab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/CommentsTab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/HistoryTab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/LinksTab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/VersionControlTab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemDetail/WatchersTab.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemActionStep.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/ItemErrorHandler.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/ItemAction.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/ItemActionDialog.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/screen/baseScreen.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/fieldTypes.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/itemScreen.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/item.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/sendItemEmail.js"></script>

		<%-- admin.screenEdit --%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/screen/screenEditView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/screen/screenEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/screen/itemScreenEdit.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/screen/cardScreenEdit.js"></script>


		<%--item--%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/item/printItem.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/form/MultiSelect.js"></script>


		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/ActionBase.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/WindowBase.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/WindowBaseController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/FormBase.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/FormBaseController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/AdminBaseController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/DeleteWindow.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/DeleteWindowController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/GridBase.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/GridBaseController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/refreshAfterSubmit.js"></script>		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/CrudBase.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeBase.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeBaseController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeDetail.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeDetailController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeWithGrid.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeWithGridController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/SimpleAssignment.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/SimpleAssignmentController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeDetailAssignment.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/TreeDetailAssignmentController.js"></script>


		<%-- itemNavigator--%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/RowExpander.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/filter.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/issueListView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/TreeDropZone.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/navigableItem.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/ItemDialogManager.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/filterNavigator.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/simpleTreeGridView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/flatGridView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/CellInlineEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/liteGridView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/wbsView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/cardView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/chooseColumns.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/grouping.js"></script>
		<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/instantFilter.js"></script>--%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/EmbeddedFilter.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/EmbeddedFilterController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/massOperation.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/link.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/ItemNavigatorController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/itemNavigator.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/itemNavigator/itemNavigatorLite.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/windowConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/util/ajaxErrorHanding.js"></script>


		<!--
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/com/track/speedometerPlugin/SpeedometerAxis.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/com/track/speedometerPlugin/SpeedometerSeries.js"></script>
		-->



		<%--dashboard--%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/dashboard/dashboard.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/dashboard/home.js"></script>

		<%--dashboard edit--%>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/dashboard/dashboardEdit.js"></script>

		<%--logon--%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/LinkComponent.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/ext/ux/layout/Center.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/logon/logon.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/logon/ban.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/logon/confirmed.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/logon/expired.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/logon/resetPassword.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/logon/resetPasswordExpired.js"></script>

		<%--browse projects--%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/browseProjects/browseProjects.js"></script>

		<%--reports --%>


		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/category/CategoryBase.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/category/CategoryBaseController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/filterStatics.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterParameter.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterParameterController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterLink.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/filter/FilterLinkController.js"></script>


		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/category/ReportConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/category/report.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/report/ReportConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/report/ReportConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/report/ReportEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/report/ReportEditController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/category/reportsInit.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/linking/VerticalHeader.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/linking/Linking.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/js/customFrame/customFrame.js"></script>


		<%--admin--%>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/screenListView.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectType.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeImport.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeImportController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeExport.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/projectType/ProjectTypeExportController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/role/Role.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/role/RoleController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/role/RoleEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/role/RoleEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/admin.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/profile.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/Person.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/PersonController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/PersonEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/PersonEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/Group.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/GroupController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/GroupEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/GroupEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/UserRolesInProject.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/UserRolesInProjectController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/UserRolesInProjectEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/UserRolesInProjectEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/UserRolesInProjectWindow.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/UserRolesInProjectWindowController.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/FiltersInUserMenu.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/FiltersInUserMenuController.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/FiltersInUserMenuEdit.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/FiltersInUserMenuEditController.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/FiltersInUserMenuWindow.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/FiltersInUserMenuWindowController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/Department.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/DepartmentController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/DepartmentEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/DepartmentEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/user/dashboardAssignment.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/scripting/Script.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/scripting/ScriptController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/scripting/ScriptEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/scripting/ScriptEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/SiteConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/EmailOutgoing.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/Ldap.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/ServerStatus.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/LoggingConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/LogonPageText.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/SendEmail.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/DatabaseBackup.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/server/DatabaseRestore.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/iCalendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifyConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifyConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifySettingsList.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifySettingsListController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifySettingsEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifySettingsEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifyTriggerList.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifyTriggerListController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifyTriggerEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/myProfile/notify/NotifyTriggerEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/TreeConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/TreeConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/FieldConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/FieldConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/AssignmentConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/AssignmentConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/AssignmentConfigEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/AssignmentConfigEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/ScreenConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/treeConfig/ScreenConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/userLevel/UserLevel.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/userLevel/UserLevelController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/userLevel/UserLevelEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/userLevel/UserLevelEditController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/ListConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/ListConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/ListEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/ListEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/IconUpload.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/IconUploadController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/ListImport.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/list/ListImportController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/objectStatus/ObjectStatus.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/objectStatus/ObjectStatusController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/objectStatus/ObjectStatusEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/customize/objectStatus/ObjectStatusEditController.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ProjectConfig.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ProjectConfigController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ProjectEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ProjectEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/projectCockpit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ProjectCockpitController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/projectCopy.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/Release.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ReleaseController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ReleaseEdit.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/ReleaseEditController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/versionControl.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/RoleAssignment.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/project/RoleAssignmentController.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/importWizard.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/importExcel.js"></script>
    	<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/importDocx.js"></script>
    	<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/exportDocx.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/importMsProject.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/exportMsProject.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin/action/importTrackplus.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/js/master/masterHome.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/master/externalAction.js"></script>

	<%}else{%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/trackplus.js?v=<s:property value='#application.TVERSION.label'/>"></script>
	<%}%>

	<script type="text/javascript"  src="<%=request.getContextPath()%>/js/jQuery/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/lightbox/lightbox.js"></script>


    <%--testing--%>
    <%
        if("true".equalsIgnoreCase(testing)){
    %>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/test/test.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/test/logon/logonTest.js"></script>
    <%}%>


	<%--
	<script type="text/javascript" src='<%=request.getContextPath()%>/wiki/js/wikiController.js'></script>
	<script type="text/javascript" src='<%=request.getContextPath()%>/wiki/js/westPanel.js'></script>
	<script type="text/javascript" src='<%=request.getContextPath()%>/wiki/js/baseWiki.js'></script>
	<script type="text/javascript" src='<%=request.getContextPath()%>/wiki/js/DocumentPicker.js'></script>
	--%>

	<%-- custom modules --%>
	<s:if test="dependentModules!=null">
		<s:iterator value="dependentModules">
			<s:if test="#session.debug!=null&&#session.debug=='true'">
				<script type="text/javascript" src='<%=request.getContextPath()%>/<s:property value="id"/>/js/<s:property value="id"/>-debug.js'></script>
			</s:if>
			<s:else>
				<script type="text/javascript" src='<%=request.getContextPath()%>/<s:property value="id"/>/js/<s:property value="id"/>.js'></script>
			</s:else>
			<%
			if(isIE9){%>
				<s:if test="haveIE9Css">
					<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/<s:property value="id"/>/design/silver/<s:property value="id"/>_IE9.css?v=<s:property value="#application.TVERSION.label"/>'>
				</s:if>
				<s:else>
					<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/<s:property value="id"/>/design/silver/<s:property value="id"/>.css?v=<s:property value="#application.TVERSION.label"/>'>
				</s:else>

			<%}else{%>
				<link rel="stylesheet" type="text/css" href='<%=request.getContextPath()%>/<s:property value="id"/>/design/silver/<s:property value="id"/>.css?v=<s:property value="#application.TVERSION.label"/>'>
			<%}%>
		</s:iterator>
	</s:if>

	<s:if test="#application.APPLICATION_BEAN.AppTypeDesk==#application.APPLICATION_BEAN.AppType">
	    <style type="text/css">.logoHeader{width:100px;}</style>
	</s:if>
	<s:if test="#application.APPLICATION_BEAN.AppTypeBugs==#application.APPLICATION_BEAN.AppType">;
		<style type="text/css">.logoHeader{width:58px;}</style>
    </s:if>


</head>

<body id="body" dir='<s:text name="common.dir"/>' class="<s:property value='#session.mainCls'/>">
</body>



<script type="text/javascript">
	var layoutCls='com.trackplus.layout.LogonLayout';
	<s:if test="layoutCls!=null">
		layoutCls='<s:property value="layoutCls"/>';
	</s:if>

	borderLayout=Ext.create('com.trackplus.layout.BorderLayout',{
		layoutCls:layoutCls,
		initData:initData
	});


	Ext.onReady(function(){
		Ext.QuickTips.init();
		Ext.History.init();

		borderLayout.createView();
		borderLayout.view.updateLayout();
		borderLayout.notifyReady.call(borderLayout);

		CKEDITOR.on('dialogDefinition', function(ev) {
			// Take the dialog name and its definition from the event data
			var dialogName = ev.data.name;
			var dialogDefinition = ev.data.definition;
			if (dialogName == 'image') {
				dialogDefinition.dialog.on('show',function(evt){
					//this.getContentElement("info","txtUrl").getInputElement().setAttribute('readOnly',true);
					this.getContentElement("info","txtUrl").disable();
				});
			}
		});
	});
	Ext.Ajax.on('beforerequest', (function(conn, options, eOpts) {
		if(options.params==null){
			options.params={};
		}
		options.params.fromAjax=true;
	}));
	Ext.Ajax.on('requestcomplete',function(conn, response, options, eOpts){
		var jsonData=null;
		try {
			jsonData=Ext.decode(response.responseText,true);
		}catch(e){};
		if(jsonData!=null&&jsonData.success===false){
			if(jsonData.errorCode==-1000){
				//forwardToLogonAjax
				borderLayout.setLoading(true);
				location.reload(true);
				return true;
			}
		}
	});
</script>

<head>
<!-- http://support.microsoft.com/kb/222064/EN-US/ -->
<meta http-equiv="Pragma" CONTENT="no-cache">
</head>

</html>
</s:else>
