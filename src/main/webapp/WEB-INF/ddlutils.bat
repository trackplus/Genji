@echo off
rem (C) 2007 Trackplus. All rights reserved.
rem 
rem This script permits you to dump or move a Track+ database (or any
rem other for that matter) to another database. For example, you can
rem move from Firebird RDBMS to MySQL or MS SQLServer
rem
rem For this to work you have to configure the database connections in
rem file ddlutils-build.properties. It should be pretty much self
rem explanatory. You also need the ant program somewhere on your machine
rem and a JDK (JRE does not suffice).
rem
rem You will get usage instructions when you run this file with no
rem arguments. The data of the source database will be dumped to the
rem directory where this file is in, file name data.xml
rem
rem The target database has to exist before you run this utility. However,
rem it can be empty!.
set JAVA_HOME=C:\Programme\Java\jdk1.5.0_09
set ANT_HOME=C:\Programme\Java\apache-ant-1.7.0

CALL %ANT_HOME%\bin\ant -f ddlutils-build.xml %1


