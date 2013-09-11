Creating a Plugin for Wasp
##########################

Wasp Plugins are extensions of the Wasp System (WS) Core code which enable users to add new features to a local instance of the WS or for open-source 
distribution. Plugins can provide a variety of functionalities, common examples being:

* New assays: Sample submission form definitions and flow, analysis workflows.
* Software: definition, dependencies and workflows
* Visualization tools: Generation of panels

This section demonstrates how to create a working plugin from scratch, walking the reader through the process step by step. The plugin we will create is
the "Picard" plugin. `Picard <http://picard.sourceforge.net>`_ is a set of command line Java executables that manipulate SAM / BAM files. 
We would like this plugin to:

1. Define a software wrapper for each command line tool (for this example we will only be implementing the *CollectAlignmentSummaryMetrics* tool).
2. Hook into the Wasp messaging system (wasp-daemon) to be notified when any BAM files are made in order to the QC tools on those files automatically.
3. Parse output from the QC analyses for display in a panel when a system user is examining output related to a particular BAM file.

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

Using the Eclipse Plugin Wizard
===============================

From the STS menu bar select *File* -> *New* -> *Project* -> *WASP Plugin* -> *WASP Plugin Project* and click *Next*. Fill in the Project information 
as requested and configure the project by selecting the desire features (:num:`figure #fig-configureProj`):

.. _fig-configureProj:
 
.. figure:: figures/configureWaspProj.png 
   :width: 14cm
   :align: center
   
   Configuration of a new wasp plugin.


* **Web forms:**
  Sets up the project to aid with producing a plugin that is web-enabled. Provides a folder structure, configuration files and classes necessary to interact
  with the *wasp-web* application. Check this option if providing a resource such as a DNA sequencer, an assay workflow, or software for primary analysis 
  where user parameter selection / configuration is possible.
	
* **Resource:**
  Checking this option adds files and folders that help with developing a plugin that provides a resource (particularly software).
	
* **Pipeline:**
  This option should be checked if the plugin is going to provide one or more Spring Batch Workflows. It ensures inclusion of necessary configuration files
  and a suitable folder structure.
	
* **Visualization:**
  If a plugin is designed to provide visualizations, e.g. present plots of data or a table of information, ensure that this item is checked.
	

After configuring the project click *finish* and the project will be built and appear in the *Package Explorer* on the left-hand side of the Eclipse IDE 
(:num:`figure #fig-picardProjStructure`).

.. _fig-picardProjStructure

.. figure:: figures/picardProjStructure.png
   :width: 10cm
   :align: center
   
   Picard example project folder structure (all configuration options checked).
   
Spring 101
==========

Before we look in detail at the structure of the project we have created and examine the various components, we first need to understand some basic 
fundamentals of the Spring framework.

Spring facilitates the creation of 
powerful applications without worrying about the plumbing or writing boilerplate code. It is configuration-centric, creating an application context during 
application initialization which consists of Java beans which have been pre-configured either in code or XML files. By programming to interfaces, it is easy
to swap out components for testing or upgrading the application. For example, it is easy to change from using a mysql database to an Oracle database
simply by swapping out database adapters in configuration and without changing any business logic (POJOs). 

In the Wasp System, the configuration (XML) files defining the application contexts of the core components (*wasp-core*, *wasp-daemon* and *wasp-web*) import 
plugin-specific configuration files from each registered plugin. In the *src/main/resources:META-INF/spring* folder within the project structure we have 
created (:num:`figure #fig-picardProjStructure`), you will see XML configuration files suffixed by *common.xml*, *batch.xml* and *web.xml* (the latter two are 
optional depending on how the plugin was configured). Looking in the picard project *picard-plugin-context-common.xml* file, a very simple bean is defined 
representing a string instance called *picardPluginArea* which has the value "picard" injected via the constructor:

.. code-block:: xml
 
   <bean id="picardPluginArea" class="java.lang.String">
       <constructor-arg>
           <value>picard</value>
       </constructor-arg>
   </bean>
	
The second bean in this file is declaring a configured instance of the *edu.yu.einstein.wasp.picard.plugin.PicardPlugin* class:

.. code-block:: xml

   <bean id="picard" class="edu.yu.einstein.wasp.picard.plugin.PicardPlugin">
       <constructor-arg name="pluginName" ref="picardPluginArea" />
       <constructor-arg name="waspSiteProperties" ref="waspSiteProperties" />
       <constructor-arg name="channel" ref="wasp.channel.plugin.picard" />
       <property name="pluginDescription" value="A tool for working with NGS data in BAM format" />
       <property name="provides" >
           <set>
             <ref bean="picard" /> 
           </set>
        </property>
        <property name="handles" >
            <set>
                <ref bean="picardPluginArea" />
            </set>
        </property>
   </bean>

Notice how the *picardPluginArea* bean is injected into the *picard* bean by providing its object reference as a constructor argument. Notice also how 
collections may be injected, in this case a collection of type *java.util.Set*. You can see another example of passing by value with the setting of the 
*pluginDescription*  property. Under the hood, spring doesn't directly set the value of *pluginDescription*, instead it expects there to be a public method 
*void setPluginDescription(String)* defined in the *PicardPlugin* class. Similarly, for the *provides* property, Spring expects the *PicardPlugin* class to 
define a method *void setProvides(Set<?>)*.

It is possible to evaluate expressions and inject the result into a bean during instantiation e.g.:

.. code-block:: java

   <bean class="org.baz.bar.Foo">
       <property name="foobar">
           <value>${wasp.config.foobar}</value>
       </property>
       <property name="name" value="#{picard.getName()}" />
   </bean>
	
In the above example two properties called *foobar* and *name* are being set. The *foobar* property value is intended to be an evaluated property. In the 
Wasp System, custom and system properties are both defined in the *wasp-config* plugin within the *src/main/resources/\*.properties* files. In this example,
one of these files is expected to contain the line "wasp.config.foobar=My Foo Plugin". Thus, during bean instantiation, the *${wasp.config.foobar}* placeholder
is replaced with the String value "My Foo Plugin". The *name* property value is obtained by evaluating a `Spring Expression Language (SpEL) 
<http://static.springsource.org/spring/docs/3.0.x/reference/expressions.html>`_ construct. In this case, it assumes a bean called "picard" is defined, and 
evaluates its *getName()* method.

An alternative to injecting constructor / property values in the XML bean definitions is to do it in the Class definition. An *@Autowired* annotation placed 
above a field, setter method or constructor 
signifies that Spring should locate and inject a bean of the correct type during initialization. Most of the time single instances of a particular class are
instantiated as beans, however, if there is more than one bean of a particular type, Spring need to know which one you wish to autowire. This is accomplished 
using the *@Qualifier("theBeanIWant")* annotation. It is also possible to inject property values using *@Value*. These concepts are illustrated below:

.. code-block:: java
   
      
   Bar bar;
   
   // The '@Autowired' annotation tells Spring that we expect there to be a single bean (a dependency) of type 
   // Bar configured in the application context which should be injected on bean initialization. 
   // When testing the class we can set the value of bar explicitly, e.g. by providing a stub or mock object.
   @Autowired 
   void setBar(Bar bar){
     this.bar = bar;
   }
   
   // Qualifying here because the application context contains two beans of type Foo called 'foo' and 'fooey'.
   // We need to tell Spring which one to use
   @Autowired
   @Qualifier("foo") 
   Foo foo;
   
   // Here we inject a value defined in a .properties file in the *wasp-config* plugin (see above). If no value is specified we 
   // provide a default value "not set" (this is optional).
   @Value("${wasp.config.foobar:not set}")
   String foobar;
   
   void setFoobar(String foobar){
     this.foobar = foobar;
   }
   
If a class is annotated to allow autowiring of dependencies and does not require any custom configuration, it is possible to have Spring load an instance
automatically without any XML definition. Simply add the *@Component* annotation above the class declaration (or a more appropriate derivative, e.g. *@Service* 
for service classes) and the line *<context:component-scan base-package="org.baz.bar.packageToScan" />* in an appropriate configuration file within 
*src/main/resources:META-INF/spring* (replacing "org.baz.bar.packageToScan" with the actual package enclosing any annotated class(es) to be loaded by Spring). 
On application initialization, Spring creates an instance of each component-scanned class, giving it a name identical to the simple name of the class with the
first letter de-capitalized.

.. important::

   You should be aware of the bean life-cycle. During application initialization: 
     
     1. Bean definitions are loaded.  
     2. Properties are evaluated.
     3. Dependencies are injected.
     4. Beans are post processed. Normally, when instantiating a class, work can be performed in a constructor using values provided. However, when using values
        injected into beans, they are not available immediately after construction. Such work should, instead, be performed in a public method annotated with 
        *@PostConstruct*. All injected values will be available for use when such an annotated method is executed by Spring. If any cleanup is required prior 
        to bean destruction, e.g. closing a resource, a public method annotated with *@PreDestroy* may also be provided.
     5. Beans ready for use. 

With a basic introduction to the concepts of Spring required to generate plugins, we can move on to examine the details of the project structure for a 
plugin:

* **src/main/java**

  **<package_root>.batch.tasklet** 
    Location for batch job tasklets. Tasklets contain the code executed in each step of the batch flow. They extend abstract class 
    *wasp-daemon:edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet*
    
  **<package_root>.batch.controller**
    MVC controller code. For web-enabled plugins the request mappings and associated business logic are defined here. Classes should extend the 
    *wasp-web:edu.yu.einstein.wasp.controller/WaspController* class.
    
  **<package_root>.exception**
    Package for placing plugin-specific exceptions. An extension of Exception and RuntimeException are provided and can be extended further.
    
  **<package_root>.integration.endpoints**
    This package is where custom Spring Integration message endpoint classes can be defined. These include service activators, channel adapters, transformers, 
    filters, routers, splitters and aggregators. See the SpringSource documentation (http://static.springsource.org/spring-integration/reference) for more 
    information message endpoints.
  
  **<package_root>.integration.messages**
    Spring Integration provides for messages and message channels to be defined that allow communication between the core wasp systems and plugins. Messages 
    are simply a set of 
    headers (key-value) and a payload object. The name and value of headers and the type and value of the payload can all be used to determine how a message 
    is routed, filtered and acted upon. As the specification is so loose, the Wasp System uses wrappers around the messages to allow standardization. This
    package may contain message template classes that extend the *wasp-core:edu.yu.einstein.wasp.integration.messages.templates.WaspMessageTemplate* and 
    *wasp-core:edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate* classes. Extensions of the 
    *wasp-core:edu.yu.einstein.wasp.integration.messages.WaspMessageType* and *WaspStatus* classes may also be provided here. The base classes for 
    *WaspMessageType* and *WaspStatus* are shown below.
    
    .. code-block:: java
    
       public class WaspMessageType {
         public static final String HEADER_KEY = "messagetype"; // header name
         public static final String JOB = "job"; 
         public static final String PLUGIN = "plugin";
         public static final String RUN = "run";
         public static final String SAMPLE = "sample";
         public static final String LIBRARY = "library";
         public static final String ANALYSIS = "analysis";
         public static final String GENERIC = "generic";
         public static final String FILE = "file";
         public static final String LAUNCH_BATCH_JOB = "launchBatchJob";
       }
		
       public class WaspJobParameters {
         public static final String GENOME_STRING = "genomeString";
         public static final String JOB_ID = "jobId";
         public static final String JOB_NAME = "jobName";
         public static final String SAMPLE_ID = "sampleId";
         public static final String SAMPLE_NAME = "sampleName";
         public static final String LIBRARY_ID = "sampleId";
         public static final String LIBRARY_NAME = "libraryName";
         public static final String LIBRARY_CELL_ID = "libraryCellId";
         public static final String RUN_ID = "runId";
         public static final String RUN_NAME = "runName";
         public static final String RUN_RESOURCE_CATEGORY_INAME = "runResourceCatIname";
         public static final String PLATFORM_UNIT_ID = "platformUnitId";
         public static final String PLATFORM_UNIT_NAME = "platformUnitName";
         public static final String BATCH_JOB_TASK = "batchJobTask";
         public static final String FILE_GROUP_ID = "fileGroupId";
         public static final String TEST_ID = "testId";
       }
  
  **<package_root>.plugin**
    This is the location of the plugin definition class. A bean derived from type *wasp-core:edu.yu.einstein.wasp.plugin.WaspPlugin* is defined in the 
    configuration for the plugin which is located in the *src/main/resources:META-INF/spring/* folder. Optionally, the plugin may declare properties "provides" 
    and "handles" which declare services that the plugin implements and resources that it may act upon.  For example, a plugin may declare that it implements
    "referenceBasedAligner", or "illuminaSequenceRunProcessor". An illuminaSequenceRunProcessor might additionally handle "illuminaHiSeq2000Area". More than 
    one plugin class may be defined within the project and implemented as a bean. For example, the *babraham* plugin project contains three plugins each 
    representing wrappers around three software applications provided by Babraham Bioinformatics: FastQC, FastQ Screen and Trim Galore.
    
    .. note::
    
       Any class derived from *WaspPlugin* is registered in a bean of type *wasp-core:edu.yu.einstein.wasp.plugin.WaspPluginRegistry* which 
       can be autowired into any class and interrogated using the *Set<WaspPlugin> getPluginsHandlingArea(String area)* and 
       *List<T> getPluginsHandlingArea(String area, Class<T> clazz)* methods.
  
  **<package_root>.service.impl**
    Plugin business logic that accesses data access objects (DAOs) defined in the wasp-core can be implemented here. Any classes defined in here with 
    annotations @Service or @Component will be automatically instantiated as beans on application startup.
  
  **<package_root>.software**
    This package is intended for inclusion of Classes extending the *wasp-core:edu.yu.einstein.wasp.software.SoftwarePackage* class. Each class defined in
    this package should provide methods relevant for executing the software it is wrapping. A loader configuration file (filename ending in *Load.xml*) should 
    be provided in the *src/main/resources:wasp/* folder which creates a bean instance of each software class via the 
    *edu.yu.einstein.wasp.load.SoftwareLoaderAndFactory* factory bean. This is pre-configured for you when you created the project. The bean is generated via 
    a "factory bean" because certain attributes must be stored in the core database.
  
* **src/main/resources**

  **css** 
    project specific .css files go here
  
  **flows**
    Spring batch flows should be place in here. All files within this folder (or subdirectories of this folder) are imported by the 'wasp-daemon' commonent of
    the Wasp System during application initialization.
    
  **i18n**
    Internationalization properties files go here. Typically internalization properties defined within here may be evaluated in code by injecting the 
    *messageServiceImpl* bean (implements *edu.yu.einstein.wasp.service.MessageService*) e.g for a property in the *messages_en_US.properties* file defined
    ``foo.warning=Do not mess with foo``, in the following example the method *getInternationalizedFooWarning()* returns the string "Do not mess with foo".
    
    .. code-block:: java
    
      @Autowired
      private MessageService messageService;
	
      String getdefaultInternationalizedFooWarning(){
        return messageService.getMessage("foo.warning"); // defaults to Locale.US
      }
      
      String getInternationalizedFooWarning(){
        return messageService.getMessage("foo.warning", Locale.US);
      }
    
    Also in web views, these properties may be evaluated within jsp pages. In the example shown below the text "Foo says: Do not mess with foo" would 
    be displayed in the browser:
    
    .. code-block:: jsp
    
      <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      
      <%-- gets locale automatically from HttpServletRequest --%>
      Foo says: <fmt:message key="foo.warning" />  
