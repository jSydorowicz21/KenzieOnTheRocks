# Overview
This service can be deployed to any AWS Account, it uses user provided AWS Credentials and generic scripts to accomplish this task. TODO: Add more in depth steps

### To deploy the Development Environment

Run `./deployDev.sh`

To tear down the deployment then run `./cleanupDev.sh`

### To deploy the CI/CD Pipeline

Fill out `setupEnvironment.sh` with the url of the GitHub repo and the username (in all lowercase) of the 
team member who is maintaining the repo. Confirm that the team member has added your username as a contributor to the repo.

Run `./createPipeline.sh`

To teardown the pipeline, run `./cleanupPipeline.sh`

## Current Backlog

https://github.com/jSydorowicz21/KenzieOnTheRocks/issues
