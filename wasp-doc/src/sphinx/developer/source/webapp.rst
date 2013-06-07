**************************
Working with the web layer
**************************

.. note::
  Email communication is essential for proper WASP flow, particularly during user registration and initial steps of job submission.
  Currently, WASP does NOT provide a backup system if email communication fails. While this will be remedied in a future release, 
  until that time, it is essential to properly configure the email system and to confirm that emails to and from the system function as expected.

======================
Users
======================

There exist two general categories of WASP users and the procedure used to create accounts differs for each category. 

The two general classes of user are:

#. Job Submitters
#. Core Laboratory Personnel and Administrators

Job Submitters include Principal Investigators and their lab members who request services from a Core Laboratory Facility. 
The function of the Core Laboratory Facility is to provide services to Job Submitters, generally by performing assays and data analysis, 
and returning results. 

Core Laboratory Personnel and Administrators include individuals directly or indirectly associated with the Core 
Laboratory Facility, including Facility Managers, Facility Technicians, Departmental Administrators, General Administrators, 
System Administrators, etc. While the two groups of users are not mutually exclusive, the mechanism for creating each account is 
different; creating accounts for Job Submitters (PIs and their lab members) generally occurs from outside WASP while creating accounts 
for Core Facility Personnel generally occurs from within WASP.

.. index:: Roles

=============
Roles
=============
	
WASP supports user roles as a means of regulating access to system functionality. Users can be granted no role or 
many different roles, which controls what they are able to to do within the system.  For example, a typical job submitter 
that registers as a member of some lab is conferred with the role of lab user (LU), which permits that user to submit 
job requests, to view ones own job requests and the progress of those jobs, and to view all data associated with those 
jobs. A job submitter that registers as a Principal Investigator is conferred with roles PI, LM (lab manager), 
and LU (lab user), which permits the PI to submit job requests, view ones own job requests and data generated from 
those requests, and to view job requests and data belonging to other lab users registered with that PI's lab.

By contrast, the PI, LM, and LU roles are NOT sufficient to permit access Core Laboratory Facility functions, which include the 
generation of libraries, the loading of flow cells, and the creation / initiation of a sequencing run, as these functions are 
restricted to individuals granted the roles of Facility Manager (FM) and Facility Technician (FT role). Some roles, such as PI 
and LU are granted automatically by the system during the registration process; other roles, such as Facility Manager and Technician, 
are granted or revoked explicitly (see Super User below). Current roles that can be granted explicitly include Facilities Manager, 
System Administrator, General Administrator, Facilities Technician, Lab Manager, and Super User, however the System Administrator 
and General Administrator roles have yet to be fully defined. The system also includes a special role, Super User, which permits 
access to all WASP functionality.
	
===================
Special User & Role
===================
		
WASP is delivered with a single registered user with a username / password combination of super / user and a role of Super User. 
The role of Super User confers access to ALL system privileges. Note that other users can be granted the Super User role. It is suggested 
that the role of Super User be granted to individuals that in addition have been granted the role of Facility Manager. 
  	
===============
Logging In
===============

To log into WASP, a user requires a valid account, which is obtained through the registration process. Once the registration 
process is complete, a user will have obtained a username and password which are required to log in. To log in, point your browser 
to the WASP login page, enter your username and password, and click the Login button. 

==================
Dashboard
==================
  		
Navigation through WASP begins at the Dashboard web page, which appears upon successful login. You may return to the 
Dashboard at any time by clicking on the WASP "beehive" logo or the Dashboard link located on the upper-left or upper-right portion 
of each webpage, respectively.
		
====================
Registration
====================


Creating An Account For Job Submitters
--------------------------------------

WASP is laboratory-based, which helps facilitate data sharing. This means that when a PI creates a new PI account, 
they simultaneously create a lab which others (lab members) may join. Because of this, a lab member may not join a lab 
until the lab's PI has successfully registered. New PIs and new lab members register for a new account from 
outside the system by navigating to the WASP login page and selecting the appropriate link ("New PI" or New User"). 
Note that the choice of Wasp username is not restricted, however we suggest the format of initial of first name followed 
directly by complete last name (no spaces, all lower case). In addition, it is important to pay particular attention to the 
email address that is entered in this form, as email is the primary mechanism by which users complete the registration 
process and an incorrect email addresses at this time can result in difficult to detect delays. 
				
Creating A New PI Account
--------------------------
		
For individuals that do NOT have a WASP account and wish to register for a new account as a PI of a particular lab, 
the process is as follows. A new PI registers for an account by selecting the "New PI" link located on the WASP login page.  
Users may choose their own username and password. Once the form is successfully submitted, the new PI will receive an email 
asking to confirm his/her email address, which is done by clicking on a link from within the email. 

Following email confirmation, someone with the appropriate authority must enter the system and approve or reject 
the "New PI" request. It is expected that a Department Administrator for the new PI's department will be the one to approve 
the request, however, any user with Super User privileges may perform this function. The appropriate Department Administrator 
will receive an email informing them that a new PI request is awaiting action. To approve or reject a new PI, navigate to 
Dashboard/Tasks tab/Department Administration Tasks. Click on the accordion bar to reveal information about the PI and either 
accept or reject their application. This results in the new PI receiving an email informing them that their new account has been 
rejected or approved; if approved they may log in.   
				
Creating A New Lab Member Account
---------------------------------

For individuals that do NOT have a WASP account and wish to register for a new account as a member of a particular lab, 
the process is as follows. Navigate to the WASP login page and select the "New User" link. Complete the New User form and submit. 
Note that to complete the form, you MUST provide the WASP username of the PI whose lab you wish to join. This implies that the 
PI must have an active WASP PI account prior to any lab member registration. Once submitted, the new user will receive an email 
requesting that they confirm their email address.  

After a pending new user confirms his/her email address, the specified PI (or any designated lab manager assigned to that lab) 
will receive an email informing them that a new lab member has requested to join their lab. (To designate a lab manager, see below.) 
The email will instruct the PI or lab manager to log in to WASP and accept or reject the new pending lab member. To accept/reject 
the new user, The PI or lab manager is expected to log in,  navigate to Dashboard/Tasks tab/Lab Management Tasks link, click on 
the accordion bar to reveal information about the prospective lab member, and approve or reject their application. Notification 
to the lab member will be by email. If approved, the user can now log in. In addition to the PI or lab manager, any user with 
Super User privileges may accept or reject the pending new user.   


Joining Another Lab
-------------------

This section deals with users that already have a WASP account. The procedure for a registered WASP user to join another 
lab is straightforward and does NOT include the creation of a new account. As before, the PI of the other lab must be a 
registered WASP user. To join a second (or third) lab, navigate to Dashboard/My Account tab/Request Access To A Lab. 
Note that there are two sections to the resulting page, "Request Access To Lab" &amp; "Create New Lab" Request. To join 
another lab as a regular lab member, complete the Request Access To Lab section by typing in the WASP username of the 
PI for the lab you wish to join and submit using the Request Access button. The PI (and any Lab Managers) of the specified 
lab will receive an email informing them of the request and asking that they log in to WASP and Approve or reject the user's 
request through the Dashboard/Tasks tab/Lab Management Tasks link. (In addition, any user with Super User privileges may 
approve/reject this application.)  

The procedure for a registered WASP user to become the PI of a new lab is as follows and does NOT include the 
creation of a new account. Navigate to Dashboard/My Account tab/Request Access To A Lab and complete the Create New Lab 
Request form in order to apply to become a PI of a new lab. Once submitted, the request will need to be approved by a 
Department Administrator or by someone with the role of Super User (see Approving/Rejecting A New PI Account).   


Creating An Account For Core Laboratory Personnel & Administrators
------------------------------------------------------------------
While the majority of WASP users are PIs and their lab members who wish to submit job requests, 
a smaller number of WASP users include core laboratory personnel, typically a Facility Manager, 
Facility Technicians, as well as Department Administrators. Core laboratory personnel do NOT create accounts themselves. Instead, 
the creation of accounts for new Core Lab Personnel is restricted to users with the role of Super User. Once an individual 
with Super User status has logged in, select the "Superuser Utils" tab from the dashboard, followed by the "User Utils" link, 
which will bring you to a "List of Users" grid.  This grid provides access to data about all registered users; access to that 
data is through double-clicking an entry. In addition to information about existing users, the grid is used to create a record 
for a new core lab user. To create a new user, click in the "+" icon located at the bottom left of the grid; the "Add new row" 
tooltip should appear when you hover over the "+" sign.  Complete the "Add Record" form that appears and submit. Note that the 
choice of Wasp username is not restricted, however we suggest the format of initial of first name followed directly by last name 
(no space, all lower case). It is important to pay particular attention to the email address that is entered in this form, 
as email is the primary mechanism by which users complete the registration process and an incorrect email addresses at this 
time can result in difficult to detect delays. 

Once the new record has been submitted, it is strongly suggested that the new user be assigned one or more roles. To 
assign a role, return to the dashboard, select the "Superuser Utils" tab, then select the "System Users" link, which will 
take you to the "System User Management" page.  Complete the form entitled Add System Role to User located at the bottom of the 
page. To do so, select a new role for the user and then indicate the user to which this role is to be applied. To indicate the 
user, begin typing the name of that user in the "Existing User" textbox. Once 2 - 3 letters of the name have been entered, 
autocomplete functionality attached to the text box will generate a set of possible user names. Select the correct user 
from the list and submit. (N.B.: You MUST select a user from the autocomplete list rather than simply typing the name of 
the user. This is necessary as both the name of the user, and his/her unique WASP username, will appear on the list and are 
necessary for proper granting of the new role. If the name does not appear using autocomplete, you most likely introduced a 
spelling error. Note that more than one role may be assigned to any user. (For example, as mentioned above, it may be prudent 
to assign the roles of Facility Manager and Super User to the individual responsible for the Facility laboratory.) 

While the roles of the Facility Manager and Facility Technician appear obvious, that of Departmental Administrator 
requires explanation and is applied differently from those described previously. The role of Departmental Administrator 
is two-fold: 1) to approve or reject new PI's applying for a (new) WASP account and 2) to approve or reject new job 
requisitions. It is envisioned that a Departmental Administrator will handle new PI applications and job submission 
requests on a department basis.  The system ships with two default departments, 'Internal - Default Department' and 
'External - Default Department.'  Additional internal departments (for example, Genetics) may be created and Departmental 
Administrators may be assigned to them, although creating a new department requires Super User privileges.  Users are 
assigned the role of Department Administrator on a department by department basis. This can occur in one of two ways. 
By the first method, a user with Super User privileges navigates to Dashboard, selects the Dept Admin tab, followed by 
the Department Management link. At the top of the page resulting page, enter the name of the new department and the name 
of the new administrator (must be someone already in WASP). The name of the new Administrator will autocomplete. 
Select that person and submit. The second way is to navigate to Dashboard/Dept Admin tab/Department Management link 
and then choose a department from the list at the bottom of the page. On the next page, type in the name of the new 
Administrator (Administrator Name / Create Administrator), which will autocomplete; submit.

Following the successful submission of the form creating this new user, an email will be sent to the user welcoming 
them to WASP, informing them of their WASP username, and asking that they click a link within the email confirming their 
email address. (Obviously, if the new user fails to receive this initial email within a reasonable amount of time, it may 
be assumed that an incorrect email address was entered on to the initial "Add Record" form or the WASP email system failed 
to function properly.) Once the user confirms his/her email address, they will receive a second email describing the process 
required to reset their password.  Once their password is reset and confirmed, the registration process is complete and the 
new core lab user may log in. 			

======================
Job Submission
======================
Submitting a job request is restricted to PIs and lab members. To submit a new job, navigate to dashboard, Job Utils tab, 
and select the Submit A Job link. To initiate a new request, complete the Create A Job page. Once this first page is completed, 
information about the job is saved, permitting the job request to be completed at some time in the future if desired. 
The new (draft) request, while saved by the system, is not visible to the core lab facility until all parts of the request are properly 
completed and submitted. If you need to save a job request prior to completion, it can be accessed at any time through dashboard, 
Job Utils tab, Draft Jobs link. Complete the initial Create A Job page by providing a name for the job, selecting the lab to be 
billed, and selecting the type of assay workflow that should be followed for this particular submission. (An example of a workflow 
might be ChiP-Seq or RNA-Seq). The submission continues by requesting information about the type of sequencing machine to be used 
for the job, as well as the requested read length and read type. This is followed by the names and types of samples being submitted; 
the system accepts new samples will shortly accept samples submitted to previous jobs. Users may also upload files that may be useful 
to the Core Lab Facility to document specifics about the samples being submitted (such as the size distribution of a user-submitted 
library prep or the quality of an RNA preparation). This is followed by number of sequencing lanes requested and how the samples 
should be run on those lanes, the pairings specific for the ChiP-Seq analysis, and finally specific regarding software used 
for the alignment and peak calling. Once a job request has been submitted, it may be viewed. 

==========================
Core Lab Functionality
==========================
		
The following sections describe functionality essential to the core lab.

Workflow Configuration
----------------------
Types of sequencing jobs (referred to as a workflow) require configuration prior to any job submission. To 
configure a workflow requires Super User privileges. Navigate to Dashboard/SuperUser Utils tab and select the Workflow
Utils link. The resulting webpage displays a grid listing available workflows. Select the workflow of interest and 
click on its configure link. On the next webpage, select the options most appropriate and submit. Once a workflow is 
configured, users may submit job requests of that type of workflow.

Create A Machine Record
-----------------------
			
Currently, types of machines available to the core facility (such as an Illumina HiSeq 2000) are pulled into 
the system from a configuration file.  By contrast, instances of a machine (such as the Illumina HiSeq 2000 machine 
in room 202 with serial number 102123) must be input manually. To view the current list of machines and to create a 
new machine, navigate to Dashboard/Facility Utils/List All Machines link. To add a new machine, select the "+" at the 
bottom left of the grid, complete the resulting form, and submit.

Create A Platform Unit Record
-----------------------------
			
Currently, types of platform units (flow cells) available to the core facility (such as Illumina Flow Cell Version 3) 
are pulled into the system from a configuration file. Prior to adding libraries to a platform unit, a record for instances of 
a platform unit (such as an Illumina Flow Cell Version 3 with barcode AZX102TTA) must be created. To create a new platform unit 
record, navigate to Dashboard/Facility Util/List-Create link located under the platform unit utils subsection. After selecting a 
type of machine, followed by selecting a type of platform unit, you will see a grid listing the specific instances of platform 
units that are already recorded in the system. To generate a new instance of a particular type of platform unit, click the "+" 
at the bottom left of the grid, complete the form, and submit. To view a virtual display of the platform unit, select "details" 
from the grid entry of interest.

Create A Control Library
------------------------
			
Control libraries may be added to any lane of a flow cell, regardless of the presence of other libraries on the lane. Controls 
are used to gain a feel for the quality of a sequence run as well as for reducing the signal generated from Illumina amplicon libraries 
or 5' indexed Illumina libraries which contain a common sequence at one or both ends of an insert. Controls must be entered into the 
WASP database prior to their initial use. To generate a control, navigate to Dashboard/Facility Utils tab/Control Libraries link. Select 
the "Create New Library" button, fill in the form, and submit. Currently, controls may be added to a flow cell lane whether they 
conflict with user's libraries or not. This may require remediation.
