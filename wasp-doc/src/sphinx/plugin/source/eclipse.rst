Creating a Plugin for Wasp
##########################

Wasp Plugins are extensions of the Wasp System (WS) Core code which enable users to add new features to a local instance of the WS or for open-source 
distribution. Plugins can provide a variety of functionalities, common examples being:

* New assays: Sample submission form definitions and flow, analysis workflows.
* Software: definition, triggers and workflows
* 

Installing and Using the Wasp System Eclipse Plugin
***************************************************

In order to create a plugin for the Wasp System you first need to install the Wasp System Eclipse Plugin (WSEP) for the STS from the WSEP 
update site (http://waspsystem.org/eclipse):

1) From the STS menu choose *Help* -> *Install New Software* then in the dialog select *Add*.

2) Fill in the text boxes: *Name:* "Wasp System Update Site", *Location:* "http://waspsystem.org/eclipse". Click *OK*.

3) In the *Work with* section of the *Install* dialog, select *Wasp System Update Site*, press the *Select All* button then *Next* -> *Next*.

4) Agree to the license agreement and click *Finish*. You will see a security warning about installing unsigned content. This is nothing to worry about, 
   simply click *OK* to proceed.

5) When prompted, click the button to restart Eclipse.


Creating a New Wasp System Plugin Project
*****************************************

From the STS menu bar select *File* -> *New* -> *Project* -> *WASP Plugin* -> *WASP Plugin Project* and click *Next*. Fill in the Project information 
as requested and configure the project by selecting the desire features (:num:`figure #fig-configureProj`):

.. _fig-configureProj
 
.. figure:: figures/configureWaspProj.png 
   :width: 14cm
   :align: center
   
   Configuration of a new wasp plugin.


* Web forms:
	Sets up the project to aid with producing a plugin that is web-enabled. Provides a folder structure, configuration files and classes necessary to interact
	with the *wasp-web* application. Check this option if providing a resource such as a DNA sequencer, an assay workflow, or software for primary analysis 
	where user parameter selection / configuration is possible.
	
* Resource:
	Checking this option adds files and folders that help with developing a plugin that provides a resource (particularly software).
	
* Pipeline:
	This option should be checked if the plugin is going to provide one or more Spring Batch Workflows. It ensures inclusion of necessary configuration files
	and a suitable folder structure.
	
* Visualization:
	If a plugin is designed to provide visualizations, e.g. present plots of data or a table of information, ensure that this item is checked.
	

After configuring the project click *finish* and the project will be built and appear in the *Package Explorer* on the left-hand side of the Eclipse IDE 
(:num:`figure #fig-picardProjStructure`).

.. _fig-picardProjStructure

.. figure:: figures/fig-picardProjStructure.png
   :width: 10cm
   :align: center
   
   Example project folder structure (all configuration options checked).



