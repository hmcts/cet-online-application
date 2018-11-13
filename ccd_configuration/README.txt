To configure the Civil enforcement case in CCD locally, do the following:

1. Download and run the ccd-docker app from https://github.com/hmcts/ccd-docker

2. Create the user roles
- ./bin/ccd-add-role.sh caseworker-test-solicitor && ./bin/ccd-add-role.sh caseworker-test-junior && ./bin/ccd-add-role.sh caseworker-test-senior && ./bin/ccd-^Cd-role.sh caseworker-test-manager

3. Create an idam user and apply the roles to that user. The email address needs to be the in UserProfile tab of the configuration XLS
- ./bin/idam-create-caseworker.sh caseworker-test,caseworker-test-manager test@hmcts.net password Doe John

4. Import the configuration
- ./bin/ccd-import-definition.sh <path to the xls> e.g. ./bin/ccd-import-definition.sh ../CCD_CivilEnforcement.xlsx