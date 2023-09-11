

#---------------------------------------------------
# Compile and build docker image for each service:
#---------------------------------------------------

mvn clean compile install jib:build

It is a very convenient way because this command compiles each project. Additionally, it builds and pushes docker images for each project (service).




-------------
# Websites:
-------------

https://cloud.google.com/java/getting-started/jib

https://github.com/GoogleContainerTools/jib

https://www.baeldung.com/jib-dockerizing

https://stackoverflow.com/questions/27767264/how-to-dockerize-maven-project-and-how-many-ways-to-accomplish-it
