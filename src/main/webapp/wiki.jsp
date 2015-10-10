<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
  response.sendRedirect(response.encodeRedirectURL("wiki.action?key=" + request.getParameter("key")));
%>
