# CA326 Functional Specification - EfficiClean
### Team Members:
- Conor Hanlon
- Shauna Moran

# Table of Contents
- [**1. Introduction**](#1-introduction)
	+ [1.1 Overview](#11-overview)
	+ [1.2 Business Context](#12-business-context)
	+ [1.3 Glossary](#13-glossary)
- [**2. General Description**](#2-general-description)
	+ [2.1 Product/System Functions](#21-product-system-functions)
	+ [2.2 User Characteristics and Objectives](#22-user-characteristics-and-objectives)
	+ [2.3 Operational Scenarios](#23-operational-scenarios)
	+ [2.4 Constraints](#24-constraints)
- [**3. Functional Requirements**](3-functional-requirements)
- [**4. System Architecture**](4-system-architecture)
- [**5. High-Level Design**](5-high-level-design)
- [**6. Preliminary Schedule**](6-preliminary-schedule)
- [**7. Appendices**](7-appendices)

&nbsp;

# **1. Introduction**

### 1.1 Overview

Our idea in centred around the way housekeeping staff currently operate in hotels. At the moment, staff must walk up and down the halls in hotels to check what rooms have signs on them saying “Do not disturb” or “Please service my room”. Staff members are assigned a floor and if there are no rooms on their floor to be cleaned they must wait somewhere until they check the hall again. This system is extremely inefficient.

We intend to build an application that would modernise this area of work. Instead of guests using a piece of paper on the door to notify staff when rooms are available to be cleaned they will use this application to mark the status of their room and be notified when their room is cleaned. Cleaning staff will be able to see on a web interface when rooms are ready to be cleaned, be assigned rooms and mark them as completed. This will result in this process being more efficient, safer, more accessible and better kept track of. 

Each room in the hotel will have a QR code in the room. When guests scan this QR code the application will be downloaded and the guest will be presented with the login screen. All the guest will have to do here is enter their name. The application will then check the database to see if this guest is in the room. The user will then be logged into the application and be presented with home screen. Alternatively, if a guest is using a laptop of device without a QR scanner, under the QR code will be a URL that will be independent to that room. Like the app, the guest will enter their name and will be logged into the application.

The main features of our project are:

-	A staff interface diplaying the cleaning status of rooms in the hotel

-	Priority queue system for cleaning team allocation to hotel rooms

-	Remote notification for guests when their room is marked clean on the system

-	Guest login via QR code in hotel room alongside verification of guest by database query 

-	Option to add feedback and rate a room's clean

-	Database interaction to automatically log out guests when their stay is over

-	Staff work statistics generated for supervisor and manager

-	Randomly allocated cleaning pairs daily to increase security

&nbsp;

### 1.2 Business Context

&nbsp;

### 1.3 Glossary

&nbsp;

# **2. General Description**

### 2.1 Product/System Functions

&nbsp;

### 2.2 User Characteristics and Objectives

&nbsp;

### 2.3 Operational Scenarios

&nbsp; 

### 2.4 Constraints

&nbsp;

# **3. Functional Requirements**

# **4. System Architecture**

# **5. High-Level Design**

# **6. Preliminary Schedule**

# **7. Appendices**