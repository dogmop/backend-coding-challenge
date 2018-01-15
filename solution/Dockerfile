FROM openjdk:8-jre-alpine
MAINTAINER "Alan Williams <mail@alanjwilliams.com>"

COPY target/engage-expense-*.jar /usr/src/myapp/engage-expense.jar
WORKDIR /usr/src/myapp
# If no datasource URL is passed in, use h2 in memory
ENV DATASOURCE_URL="jdbc:h2:mem:db"

EXPOSE 8080

CMD java -Dspring.datasource.url=${DATASOURCE_URL} -Dspring.datasource.username=${DATASOURCE_USERNAME} -Dspring.datasource.password=${DATASOURCE_PASSWORD} \
 -jar engage-expense.jar

