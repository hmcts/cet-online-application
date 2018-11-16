To configure the Civil enforcement case in CCD locally, do the following:



1. Download and run the ccd-docker app from https://github.com/hmcts/ccd-docker. Also read the README there.

2. Spin up the dockerized CCD app
- ./ccd compose up -d

3. It takes about a minute for the apps to start up properly. You can check the progress of the start up using this command
- ./ccd compose logs -

Steps 3.X should be temporary until the roles we need become part of Idam:

3.1. Log into the idam database. This command will tell you which IP the CCD database is running:
- docker container ls | grep compose_ccd-shared-database | cut -d ' ' -f1 | xargs docker inspect | grep IPAddress

3.2. You should be able to connect to the database using username postgre, and no password

3.3 Go into the Idam database and insert the role we need for civil enforcement:
- insert into public.role values('caseworker-ce', 'Civil Enforcement Case Worker')

4. Create the user roles
- ./bin/ccd-add-role.sh caseworker-ce

5. Create an idam user and apply the roles to that user. The email address needs to be the in UserProfile tab of the configuration XLS
- ./bin/idam-create-caseworker.sh caseworker-ce test@hmcts.net password Doe John

6. Import the configuration
- ./bin/ccd-import-definition.sh <path to the xls> e.g. ./bin/ccd-import-definition.sh ../CCD_CivilEnforcement.xlsx
