FROM openjdk:8
MAINTAINER Radiant Digital
ADD target/*.jar /msa-withdraw-service.jar
RUN bash -c 'touch /msa-withdraw-service.jar'
CMD ["java","-Dspring.profiles.active=docker","-jar","/msa-withdraw-service.jar"]
EXPOSE 5555
