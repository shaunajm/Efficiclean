# Problems and Resolutions

## Problems

- QR codes
- Asynchronous issue preventing login
- Push Notfications being sent to all Users
- JobQueue not interacting automatically with StaffQueue
- Application Crashing when updating database values which are being displayed
- Black Boxes being displayed on SVG Map Overview
- Bottoms of names in table being cut off

### QR Codes
#### Problem

Initially, we had planned for users to download the application using a QR code which would be present in their room. This QR code would then bring them to the login screen for their specific room. Upon further investigation we discovered that this action would not be possible using a single QR code.

To combat the issue we decided to use two QR codes, one for login and one to bring the user to their room’s login page. However, this only brought about more issues. We would now have to incorporate a QR scanner into the application which further complicated our user interface. If we did not incorporate this scanner and asked users to use a scanner from the app store, we would not be sure that this would bring users to the correct room page and may be unreliable.

#### Solution

We, after much thought, decided to allow users to download the application using a QR code but login will now not involved QR codes. As users will have to sign into a specific room in a specific hotel, we added two fields on our login page, hotel number and room number.


### Asynchronous Issue Preventing Login

#### Problem

While attempting to log users into the application we were calling a function to validate the input values with the values in the Fireball database. We hoped that the function would execute and that we could move on in the code. However, the function wasn't validating the information quickly enough and our code would continue as normal without our result. This led to an issue where users would not be logged in with the correct credentials.

#### Solution

To resolve the issue we approached the issue in a way that was more suited to the asynchronous nature of Firebase. Instead of our authentication branching from the code, we created a function to continue on the process. We then called this function after the authentication process had completed ensuring we have our result. Once this fix was implemented, users could log into the application successfully.

### Push Notfications being sent to all Users

#### Problem

All push notifications, for example service approvals or break approvals,  were being sent to all users on the system including staff, guests and supervisors.

#### Solution

To rectify this issue, we sent a unique tag for each user to the OneSignal console. This allowed us to individually identify users. Therefore each user only received notifications specific to them.

### JobQueue not interacting automatically with StaffQueue

#### Problem

When the JobQueue and StaffQueue were declared, the two classes were not interacting automatically when the database information was updated. This was due to the fact that we were not utilising a listener to wait on changes while the whole system was running.

#### Solution

This issue was fixed by creating a queue handler which would listen to any changes on the job queue and team queue. The queue handler implemented the observer interface. Both queues then inherited from the observable class. The observer method *update* allows the observer object to perform actions based on changes from the objects being observed. We also set up a queue handler creator which initialises the queue handler with the relevant hotel ID.

### Application Crashing when updating database values which are being displayed

#### Problem

When we were displaying our table layouts containing information regarding the status of the queue, we were using *ValueEventListeners* which were constantly listening for changes in the database. We dynamically created table rows based on the relevant information being received. However, when values were updated by actions in other activities, some table rows were now acting on null values. This caused our system to crash.

#### Solution

When displaying this information we used listeners for *SingleValueEvents* instead of *ValueEventListeners*. This way, the data was only loaded when the activity was created rather than constantly listening throughout it's lifecycle.

### Black Boxes being displayed on SVG Map Overview

#### Problem

Unfortunately, there is a known issue when converting SVG Maps to Vector Drawables where random black boxes appear in teh vector drawable and cannot be removed. A solution to this issue has not yet been developed so we had to come up with our own workaround.

#### Solution

To remove these black boxes from the background of our vector drawable. We implemented a large filled shape in the diagram to prevent the black squares from affecting our map view.

### Bottoms of names in table being cut off

#### Problem

When displaying data in table layout format, we used templates created in our xml files. Unfortunately, when dynamically creating table rows, the height of the template was not large enough to accomadate our data to be displayed.

#### Solution

We resolved this issue by setting a minimum height rather than a specific height for our new table rows. This meant that our table row would adjust to fit information from our text view.
