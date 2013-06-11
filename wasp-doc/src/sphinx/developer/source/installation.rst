**************
Installation
**************

What you need to get WASP installed.
	
================================================
Getting the WASP System Source Code from GitHub
================================================

Make sure you have Git on your server (obtain from http://git-scm.com) and obtain the latest WASP System code from https://github.com/WASP-System.
Please make sure you understand the importance of line endings in Git and adjust your setting accordingly: http://help.github.com/articles/dealing-with-line-endings
We provide a script to help clone and install the main components to speed up the installation process (see :ref:`installing`).
		
		
After cloning the WASP System source code, you’ll notice the project contains many folders. Most of these represent modules of the system, wrapped up into individual Maven projects:
						

.. _waspfolders:

.. csv-table:: Wasp System project folder contents
  :header: "Subfolder", "Description"
  :widths: 15, 65

  "db", "Contains database initialization files"
  "wasp-parent", "Hosts a parent.pom that defines common dependencies and versions required by other components. Maven Project."
  "wasp-web", "The WASP System webapp. Maven/Spring project. Builds a war file for Tomcat7 deployment."
  "wasp-file", "File download webapp for delivering files to users.  Maven/Spring project. Builds a war file for Tomcat7 deployment."
  "wasp-core", "Spring component containing core code. Maven/Spring project. Builds a jar."
  "wasp-cli", "Used to run wasp analysis jobs from the command line. Maven/Spring project. Builds a jar and wrapper script."
  "wasp-config", "local configuration e.g. database and email sever details etc. Maven/Spring project. Builds a jar."
  "wasp-daemon", "Spring component that runs WASP System task management flows and pipeline flows."
  "wasp-exec", "Builds a jar to execute the wasp-daemon component. Maven/Spring project. "
  "wasp-plugin", "Contains a .pom file that serves as a parent for .pom files in all plugin projects. Maven project."
  "wasp-interface", "location for interfaces used across projects. Maven/Spring project. Builds a jar"
  "wasp-tomcat-classloader", "Extends WebappClassLoader to allow loading of classes from the waspPlugins folder in the Tomcat base folder. Maven project. Builds a jar."
  "wasp-doc", "Wasp System documentation"
					
===============================
Setting up the Server (Linux)
===============================

1. Download the latest versions of  the following software packages and install according to the provider’s instructions:

   * Apache Tomcat7 (http://tomcat.apache.org)
   * Apache Maven3(http://maven.apache.org)
   * Oracle Java SE 6 (http://www.oracle.com) (OpenJDK is currently in testing and appears to work well)
   * MySQL Community Server (http://www.mysql.com/downloads/mysql)

2. Add environment variables to your user profile and set the values to the paths of the installations above:
				
   * ``ANT_HOME``
   * ``JAVA_HOME``
   * ``CATALINA_HOME``
   * ``MAVEN_HOME``
					
3. Then set the PATH environment variable to point to the relevant binaries.

   * ``PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$MAVEN_HOME/bin:$PATH``
				
   E.g. in a typical Linux setup using bash, your .bashrc file should contain something similar to this:

   .. code-block:: bash
				
      export ANT_HOME=/opt/ant
      export JAVA_HOME=/usr/java/latest
      export CATALINA_HOME=/usr/local/tomcat/tomcat/current
      export WASP_HOME=$HOME/waspSystem
      export MAVEN_HOME=/opt/maven/current
      export PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$MAVEN_HOME/bin:$PATH
		
====================
Tomcat 7 setup
====================
	
1. Using your favorite editor, create a new file in folder ``$CATALINA_HOME/bin``
   called ``setenv.sh``, and put the following text into it and save::

     CATALINA_HOME="/usr/local/tomcat/tomcat"
     JAVA_OPTS="-Xms128m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=512m"
     JAVA_HOME="/usr/java/latest"
				
2. Open ``$CATALINA_HOME/conf/tomcat-users.xml`` using your favorite editor and add the
   following (replacing placeholders ‘myGuiPass’ and ‘myScriptPass’ with your own choice of
   passwords: 

   .. code-block:: xml

      </tomcat-users>
         <role rolename="manager-gui"/>
         <user username="tomcat" password="myGuiPass" roles="manager-gui"/>
         <role rolename="manager-script"/>
         <user username="tomcat-script" password="myScriptPass" roles="manager-script"/>
      </tomcat-users>
				
   The manager-script account is used to deploy The WASP System using Maven. The managergui
   account can be used to manage your tomcat installation at http://localhost:8080/manager.
   To deploy wasp using Maven, the following complementary information about the server needs
   to be added to your local maven configuration as follows:
				
   From within your home folder, open the .m2/settings.xml file and locate the tags. In between these tags place the following 
   (replacing placeholder myScriptPass with the same password you used above for the tomcat-script account):

   .. code-block:: xml

      <server>
      	 <id>tomcat-localhost</id>
      	 <username>tomcat-script</username>
      	 <password>myScriptPass</password>
      </server>

3. Create the folder: ``$CATALINA_HOME/waspPlugins``			
   Ensure that their permissions are set such that Maven can copy files into the ``waspPlugins`` folder.		
4. To create a startup launch daemon (Debian based instructions):
5. Type the following at the command line: 

   .. code-block:: bash

      $ cd $CATALINA_HOME/bin
      $ tar xvfz commons-daemon-native.tar.gz
      $ cd commons-daemon-1.0.x-native-src/unix
      $ ./configure
      $ make
      $ cp jsvc ../..
				
   a. Using your favorite editor and sudo, copy the contents of the file ``tomcat7-init.d.sh`` to the location ``/etc/init.d/tomcat7``.

     .. important:: Replace the values of the variables under "local config section" at the top with values relevant to your setup

   b. At the command line run:

      .. code-block:: bash
    
         $ chkconfig --add tomcat7

   c. At the command line run ``$ sudo /etc/init.d/tomcat7 start`` to start tomcat. You should see a welcome page at 
      http://localhost:8080 if all went well.

.. _installing:

=======================================
Installing, Building and Deploying WASP
=======================================

There are three main component types comprising the Wasp System as detailed below: 
		
The wasp-web Webapp is deployed on Tomcat and represents the graphial UI for the system. It is part of the central git project 

The wasp-damon is an independent software component which leverages Spring Integration and Spring Batch to control task flows and analysis flows. 
It responds to messages (via RMI) from the web interface and command line interface (CLI). It maintains its own state and starts up from where it left off after 
shutdown (or failure) and restart. This component is also part of the central git project

Plugins are individual git versioned projects that, when built and placed int the 'waspPlugins' folder under the tomcat home folder, are weaved into the system. 
They extend the web and/or daemon components to add new functionality.
		
The fastet way to get going is to download and run the setupWasp.sh bash script. It clones and installs select projects hosted 
on the wasp-system GitHub repository https://github.com/WASP-System. If you run again later it updates the installation for you taking the lates changes from GitHub.
First it installs the central project into a git managed folder called 'wasp', deploys the webapp then clones select plugin projects.  Before running the script, 
you must uncomment and set the WASP_HOME and CATALINA_HOME variables at the top of the script if they are 
not already set in your environment. You may also wish to uncomment out the lines to shutdown and restart the wasp-daemon application.
After installation is complete, take a look at the ``$WASP_HOME/wasp-plugins/wasp-config/src/main/resources`` folder. This is where most custom
properties can be set including the database settings.

1. Initialize the wasp database by running the following at the command line (remember to edit the database 
   settings in the scripts first if they have been changed in the config):

   .. code-block:: bash

      $ mysql -uroot -p < $WASP_HOME/wasp/db/InitializeWaspDb.sql
      $ mysql -uroot -p wasp < $WASP_HOME/wasp/db/createSpringBatchTables.sql
		
2. Post Installation checks:

   a. Check to see that ``wasp-tomcat-classloader-x.x.x-SNAPSHOT.jar`` has been copied into the
      ``$CATALINA_HOME/lib`` folder. If not (due to a permissions issue) you should do this manually:

      .. code-block:: bash
    
         $ sudo cp $WASP_HOME/wasp/wasp-tomcat-classloader/target/wasp-tomcat-classloader-?.?.?-SNAPSHOT.jar $CATALINA_HOME/lib
				
   b. Verify the installed plugins by navigating to ``$CATALINA_HOME/waspPlugins``. Plugins should have been installed here. If 
      old versions of plugins persist these must be deleted as only one version of each plugin can be present in this folder.
				
   c. Verify that the wasp system webapp was started by navigating to localhost:8080/wasp in 
      a local web browser. If you see the login page you have set up the webapp properly. On a Linux system you could also use command-line tool lynx to do this:

      .. code-block:: bash
      
         $ lynx http://localhost:8080/wasp

      If not already performed by the setupWasp script, you may start up the wasp-daemon executable component (task management) in a new shell as follows:

         .. code-block:: bash
		
            $ cd $WASP_HOME/wasp/wasp-exec
            $ java -Xms128m -Xmx256m -XX:PermSize=128m -XX:MaxPermSize=256m \ 
                 -Dcatalina.home=$CATALINA_HOME \
                 -cp "target/wasp-exec-0.1.0-SNAPSHOT.jar:$CATALINA_HOME/waspPlugins/*" \ 
	         edu.yu.einstein.wasp.daemon.StartDaemon
					
      To run it in the background with no terminal output use this variant of the command instead:

         .. code-block:: bash
    
            $ nohup java -Xms128m -Xmx256m -XX:PermSize=128m -XX:MaxPermSize=256m \ 
                 -Dcatalina.home=$CATALINA_HOME \
                 -cp "target/wasp-exec-0.1.0-SNAPSHOT.jar:$CATALINA_HOME/waspPlugins/*" \ 
                 edu.yu.einstein.wasp.daemon.StartDaemon > /dev/null 2>&1 &
	
==========================
Building the Documentation
==========================
		
The Sphinx documentation (http://sphinx-doc.org) can be built by running the following commands:

   .. code-block:: bash
  
      $ cd wasp-doc/src/sphinx
      $ make html
      $ make latexpdf # requires pdftolatex install
			
Javadoc can be generated by running ``$ mvn javadoc:javadoc`` in any component parent folder, e.g.
``WASP_HOME/wasp-web`` (for the WASP SYSTEM webapp) or ``$WASP_HOME/wasp-core``
(for the core code). The documentation can then be found in ``target/apidocs`` folder under the component parent folder.
			
		
	

