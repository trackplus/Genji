<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
	String loc = request.getParameter("loc");
    String url = "http://www.trackplus.com/en/request-free-license-key.html"; 
    if ("de".equals(loc)) {
    	 url = "http://www.trackplus.com/de/testlizenzschluessel-anfordern.html";
    }
    response.sendRedirect(response.encodeRedirectURL(url));
%>
<!--
<script type="text/javascript">
  window.open("<%=url%>");
</script>
-->
Going to the license request page...
