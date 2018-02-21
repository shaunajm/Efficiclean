#Gitlab Continuous Integration Pipeline

We are using the gitlab continuous integration pipeline to run our unit tests every time we push to git. This allows us to test our application numerous times a day to ensure that it was still functioning as expected. To set up the pipeline, we have a gitlab-ci.yml file which sets up the Android environment we are using.

Our git pipeline uses both JUnit and Espresso to run these unit tests. JUnit is a framework for writing java unit tests. We use this to unit test our functions to ensure that they were operating as intended. Espresso runs instrumented tests on an emulator. This means it checks elements such as login, buttons functionality, keyboard and page link up. We found this very useful for keeping our testing going as we implemented new features before we completed other rigorous tests on them. 

Whenever the git pipeline fails, we are notified of what exact error was causing this failure. We can easily then fix the issue and push the commit without the error.

