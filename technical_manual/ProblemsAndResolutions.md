# Problems and Resolutions

## Problems

- QR codes 
- Asynchronous issue preventing login

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
