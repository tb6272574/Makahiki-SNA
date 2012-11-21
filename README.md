Makahiki-SNA
============

Social Network Analysis and Visualization of Makahiki games.

Reads the following files with the following format from the data/ directory:

energygoals.csv
---------------

Format:  timestamp,loungeString, cumulative energy-goals so far

Example: 2012-09-04 00:00:00,Lehua-B,0

Frequency: Typically one line per day per lounge.

events.csv
----------

Format: timestamp,user,last_name,group,room,action,action-url,partner,partner-group,partner-room

Example: 2012-09-05 12:23:34.307291,foo,Foo,Lokelani,159,View,/log/notifications/alert/view-lock-open/

Frequency: whenever a logged event occurs.

messages.csv
------------

Format: timestamp, message

Example: 2012-09-04 17:00:00,Kickoff Party

Frequency:  whenever a real-world event occurs, typically several times per week.

userPoints.csv
--------------

Format: timestamp, user, cumulative points so far

Example: 2012-09-04 00:00:00,foo,0

Frequency: once per hour per user. 


Issues
------

* energygoals refers to lounges, these are converted to floors in the code.
* Yongwen's code outputs an extra first line which must be deleted.