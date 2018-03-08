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
