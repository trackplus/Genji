### [Genji Scrum Tool and Issue Tracker](http://www.trackplus.com)
Genji is a web-based Scrum project management tool and issue tracker. It runs on any computer with Java, an Apache Tomcat server, and a database. Features include:
* Scrum and Kanban board with drag & drop
* Flexible filter management
* Role based access system
* Custom fields and masks
* Rich text editors
* Integration with Jenkins, Subversion, Git, Maven
* Many report templates based on JasperReports and LaTeX
* Custom cockpits with many cockpit tiles
* Custom lists
* Nice and user friendly design
* Well documented

### Downloads
There are two pre-assembled install packages:
* [Windows installer](http://www.trackplus.com/files/downloads/software/genji-504b15-win32x64-setup.exe) for Windows based systems
* [WAR](http://www.trackplus.com/files/downloads/software/genji-504b15.war) file for Unix based systems

### Installation
To install Genji on a Windows based system use the Windows installer. 
To install Genji on any other system with any of the supported database systems 
download the web archive (WAR) file and follow the instructions in the [installation manual](http://www.trackplus.com/en/issue-tracking-documentation.html). 

### Build Instructions
Genji uses [Gradle](https://gradle.org/gradle-download/) as a build tool. After installing Gradle on your build machine, 
go into the Genji main directory and execute the command:

    gradle

The result will be a "war" file in the "target" directory. To install Genji, 
please follow the instructions in the [installation manual](http://www.trackplus.com/en/issue-tracking-documentation.html). 
In brief:

1. Install a [Java JDK 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or up on your computer
2. Install [Apache Tomcat 7](http://tomcat.apache.org/) or up on your computer
3. Install a [MySQL database system](https://www.mysql.de/downloads/) on your computer (or Oracle, MS SQLServer, DB2, ...)
4. Create an empty database
5. Place the WAR file into the Apache Tomcat "webapps" directory and rename it to "track.war"
6. Add the following to the top of file "catalina.sh" in your Apache Tomcat "bin" 
folder: JAVA_OPTS="XX:PermSize=196M -XX:MaxPermSize=384M -Xms256M -Xmx1024M -DTRACKPLUS_HOME=/home/trackplus/" 
replacing TRACKPLUS_HOME with a directory of your choice.
7. Start the Tomcat server, after a minute stop it.
8. Edit file "Torque.properties" in the TRACKPLUS_HOME directory from 6 and replace the user, password, database type and JDBC URL to match your database.
9. Start Tomcat and in your browser go to http://localhost:8080/track


### Copyright and license
Code and documentation copyright 2004-2015 Steinbeis GmbH & Co. KG. Code released under [the GPL V3 license](https://github.com/trackplus/Genji/blob/master/LICENSE). Docs released under Creative Commons.
