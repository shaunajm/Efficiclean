# CA326 Testing Documentation - EfficiClean

## Team Members:
- Conor Hanlon
- Shauna Moran

## Table of Contents
- [**1. Use Case Testing**](#1-use-case-testing)
- [**2. Gitlab Continuous Integration Pipeline**](#2-gitlab-continuous-integration-pipeline)
- [**3. Unit Testing**](#3-unit-testing)
- [**4. Integration Testing**](#4-integration-testing)
- [**5. Instrumented Testing**](#5-instrumented-testing)
- [**6. User Testing**](#6-user-testing)
  + [6.1 User Testing Plan](#21-user-testing-plan)
  + [6.2 User Testing Phase One](#22-user-testing-phase-one)
  + [6.3 User Testing Phase Two](#23-user-testing-phase-two)
- [**7. Heuristic Testing**](#7-heuristic-testing)
- [**8. Accessibility Testing**](#8-accessibility-testing)


## 1. Use Case Testing

*Reference number*   | *Scenario*              |*Result*               |*Developers comments/ Proposed solution*
:------------------------------:|:-----------------------:|:---------------------:|:---------------------------------------:
001 | Guest login to the application | Guest logged in successfully |As intended
002 | Guest mark room “Please Service my Room” | Room successfully marked and information page displayed | As intended
003 | Guest mark room “Do not disturb” | Room successfully marked and information page displayed | As intended
004 | Guest mark room “Checking out” | Room successfully marked but information page has message cut off | Information page needs text boxes resized
005 | Guest change room status | Room successfully marked but incorrect time displayed on estimated room service time. Current time: 12:28 Estimated time given: 11:43 |Make changes to “Guest Please Service” activity to resolve this issue.
006 | Guest log out | Correctly presented with pop up and logged out successfully | As intended
007 | Staff Login | Staff logged in successfully | As intended
008 | Staff check queue | Queue presented correctly | As intended
009 | Staff check today’s teams | Button works correctly and table presented correctly | As intended
010 | Staff view map | Button works correctly and map view presented correctly | As intended
011 | Staff Request Break | -> Page displayed correctly but enter on input field goes to new line. Usability issue. -> App crashes if “:” left out between hours and minute values -> No matter what break selected it says you have that breaks amount of minutes remaining -> Bug if break has already been assigned | -> Input type of field needs to be changed to only allow one line of input. -> We will remove the “:” from the input as it causes accessibility issues -> Further work is need in Break allocator to resolve this issues
012 | Staff Add room to queue | -> If user types in incorrect room number app crashes -> If users clicks room and enter and doesn’t mark status the app crashes | Changes must be made to current job class to rectify this issue
013 | Staff mark room as “Clean” | Room successfully marked as cleaned | As intended
014 | Staff mark room as “Severe Mess” | Hint for description is ”etDescription” | Must be changed to “Description” in xml
015 | Staff mark room as “Hazardous” | Room successfully marked and description sent | As intended
016 | Supervisor Login | Supervisor successfully logged in | As intended
017 | Supervisor view team’s progress | Team progress list correctly updated | As intended
018 | Supervisor report absence | Staff member successfully removed from queue | As intended
019 | Supervisor view map | Button works correctly and map view presented correctly | As intended
020 | Supervisor breaks approval | Break approved | As intended
021 | Supervisor “Service” approval | Room approved and team progress increased. Room disapproved and sent back to team | As intended
022 | Supervisor “Severe Mess” approval | Severe mess approved and sent back to team. Team get increased priority. | As intended
023 | Supervisor “Hazard” approval | Hazard approved. Room disapproved and sent back to team | As intended
024 | Reception Login | Reception Logged in successfully | As intended
025 | Reception View Map | Map displayed correctly | As intended
026 | Reception Check Out Room | Room checked out. Map updated successfully. | As intended


## 2. Gitlab Continuous Integration Pipeline

We are using the gitlab continuous integration pipeline to run our unit tests every time we push to git. This allows us to test our application numerous times a day to ensure that it was still functioning as expected. To set up the pipeline, we have a gitlab-ci.yml file which sets up the Android environment we are using.

Our git pipeline uses both JUnit and Espresso to run these unit tests. JUnit is a framework for writing java unit tests. We use this to unit test our functions to ensure that they were operating as intended. Espresso runs instrumented tests on an emulator. This means it checks elements such as login, buttons functionality, keyboard and page link up. We found this very useful for keeping our testing going as we implemented new features before we completed other rigorous tests on them.

Whenever the git pipeline fails, we are notified of what exact error was causing this failure. We can easily then fix the issue and push the commit without the error.


## 3. Unit Testing
## 4. Integration Testing
## 5. Instrumented Testing

## 6. User Testing

### 6.1 User Testing Plan

To complete user testing we will hold two separate user testing sessions. We will have two sessions so that we can take feedback onboard from the first session and see what other issues users may find in the second session.

We will ask users to complete a survey and following on from this complete a short interview. Both of these methods will be utilised to ensure that we gather both qualitative and quantitative data to ensure that we are provided with as much information about our application as possible. Below is an example of our survey and questionnaire questions.

&nbsp;
&nbsp;

![](media/questionnaire1.png)

&nbsp;
&nbsp;
&nbsp;

![](media/questionnaire2.png)

&nbsp;
&nbsp;
&nbsp;

![](media/interview.png)

&nbsp;
&nbsp;

Once we have gathered this information we will take all feedback onboard to make corrections and changes that users would like to see present in Efficiclean.

### 6.2 User Testing Phase One

As mentioned in the user testing plan, we decided to implement our user testing in two phases. We would implement a week between these two user testing stages to allow us to make changes and improve our application before asking users what their opinions are now to ensure we implement all changes correctly.

*Question 1*           | *Question 2*           | *Question 3*
:---------------------:|:----------------------:|:---------------------:
![](media/survey1.png) | ![](media/survey2.png) | ![](media/survey3.png)
The majority of those tested found the application easy to use.| Most found is pleasing to look at.|Those tested would use the appication


*Question 4*           | *Question 5*           | *Question 6*
:---------------------:|:----------------------:|:---------------------:
![](media/survey4.png) | ![](media/survey5.png) | ![](media/survey6.png)
Most would recommend this application to a friend.| The results of this question were very widespread. We do not want any users to find the application frustrating to use so have to work on bug fixes and making the application less frustrating to use.|Users felt the application could be improved so we will work on this before the next round of user testing


*Question 7*           | *Question 8*           | *Question 9*
:---------------------:|:----------------------:|:---------------------:
![](media/survey7.png) | ![](media/survey8.png) | ![](media/survey9.png)
Users were happy with how they learned to use the application. | Testers found a wide range of elements useful. Those most commonly mentioned included Map View and guests marking their rooms status.|Users key reasons pointed that they liked things to be easy, fast and kept track of.


*Question 10*           | *Question 11 a*             | *Question 11 b*
:---------------------:|:--------------------------:|:---------------------:
![](media/survey10.png) | ![](media/survey11-1.png) | ![](media/survey11-2.png)
Users found Efficiclean easy to use, clever and fast. Unfortunately, one user mentioned the application was buggy. We will have to make sure to resolve this issue. |Users scored the application between 5 and 10. We would like to improve this score.|Here was can see the remainer of the legend

*Question 12*           | *Question 13*           | *Question 14*
:---------------------:|:----------------------:|:---------------------:
![](media/survey12.png) | ![](media/survey13.png) | ![](media/survey14.png)
Again, Map View and Marking room status were popular among users.|Users gave us some great advice on changes they would like to see implemented. We will investigate each of these options.|Although most users did not face difficulties with the application, over a third did. We would like to eradicate these issues.


*Question 15*           | *Question 16*           | *Question 17*
:---------------------:|:----------------------:|:---------------------:
![](media/survey15.png) | ![](media/survey16.png) | ![](media/survey17.png)
The issues users faced were predominately down to bugs which we will fix. Some issues were with colour contrasts and font sizes which will be adjusted.|All users found the application enjoyable to use which we were delighted to see.|Overall, users enjoyed using our application but felt it needed some work.

### 6.2 User Testing Phase two

## 7. Heuristic Testing

### Shneiderman's Eight Golden Rules

#### Strive for consistency.

Throughout our application we ensured that similar terminology was used to avoid confusion. As you can see below, elements such as the submit button are recognisable on each page.

![](media/SubmitExample1.png)

![](media/SubmitExample2.png)

Resembling actions are carried out in the same manner. The consistency of each of these elements ensures that Efficiclean is easy to use.

##### Report Hazard and Severe Mess pages:
&nbsp;
![](media/ReportHazard.png)

![](media/ReportSevereMess.png)

##### List Hazard and Severe Mess pages:
&nbsp;
![](media/ListHazards.png)

![](media/ListSevereMess.png)

As we can see from each of the above pages, the background and font colours throughout Efficiclean are the same. We utilised red and gold as they symbolised royalty and high quality.

### Enable frequent users to use shortcuts.

At this current time there are no shortcuts built into Efficiclean as all actions must be carried out methodolocially. However, as future work, we would like to keep guests logged in until they leave the hotel when they will be automatically logged out. 

### Offer informative feedback

Once a guest marks the status of their room they are informed which option they have selected and provided with information on this option. Similarly, once a staff member sends a room to supervisor approval they will receive a pop up to let them know the room has been sent for approval. This feedback allows users to know that the action they have completed has been successful.

![](media/pleaseservice.png)


### Design dialog to yield closure

Actions such as guests marking the status of their rooms has a clear sequence of actions. Guests first login to Efficiclean using their hotel ID, room number, first name and surname. They then progress onto the home screen and have three options “Please service my room”, “Do not disturb” and “Checking out”. Once the guest selects an option they are presented with a page to inform them what status there room is currently marked as.

![](media/login.png)
&nbsp;
![](media/home.png)
&nbsp;
![](media/pleaseservice.png)

### Offer simple error handling.

Users will not be able to make errors in Efficiclean as they will receive a pop up to inform them of their error. An example of this is if a user does not fill in a field when logging into the application. They will be informed that this needs to be changed for them to log into the application.

![](media/error.png)

### Permit easy reversal of actions.

Throughout Efficiclean there is a back button in the toolbar to allow users to return to the previous page if they have made a mistake. Similarly, if a user marks their room as do not disturb, they have the option to change this until a certain time.

![](media/back.png)

### Support internal locus of control

Users have full control of the application. The guests get to choose to the status of the rooms, the staff and supervisor get to select what actions they perform in the application. Users initiate all actions within the application.

![](media/home.png)

### Reduce short-term memory load.

As our short term memory supports only 5 to 9 items at one time, we kept our user interface as simple as possible. This means that users are not overloaded with information and can enjoy using the application.