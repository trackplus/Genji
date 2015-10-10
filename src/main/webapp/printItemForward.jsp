<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
  response.sendRedirect(response.encodeRedirectURL("printItem.action?key=" + request.getParameter("key")));
%>



