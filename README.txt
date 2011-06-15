After doing mvn clean install you can note that there is one file missing, that is the PhoneGap.jar library, at the moment there is not any Maven repository for downloading this file, neither we have any Nexus repository (or similar) in order to put it, so, the only choice we have is to install by ourselves this library in our local .m2 repo, here you have how to do it:

once in the project folder "WorkspacePGCalendar/phoneGapCalendarAPI", and having mvn installed you have to run this command:

mvn install:install-file -DgroupId=com.phonegap -DartifactId=PhoneGap -Dversion=0.9.5.1 -Dpackaging=jar -Dfile=./CalendarLib/libs/phonegap.0.9.5.1.jar -DgeneratePom=true

That's all!!
