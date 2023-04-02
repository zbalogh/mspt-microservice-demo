

#-----------------------------------------------------
# Compile and build docker image for each servcice:
#-----------------------------------------------------

mvn clean compile jib:build

It is a very convient way because this command compile each projects and build and push docker images as well.



-------------
# Websites:
-------------

https://cloud.google.com/java/getting-started/jib

https://github.com/GoogleContainerTools/jib

https://www.baeldung.com/jib-dockerizing

https://stackoverflow.com/questions/27767264/how-to-dockerize-maven-project-and-how-many-ways-to-accomplish-it
