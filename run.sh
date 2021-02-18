#!/bin/bash
GOOGLE_APPLICATION_CREDENTIALS="/root/promocat-firebase.json"

export GOOGLE_APPLICATION_CREDENTIALS
/root/PromoCat-Back/mvnw spring-boot:run -Dspring-boot.run.profiles=production
