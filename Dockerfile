FROM openjdk:26-trixie
ADD target/product-app.jar product-app.jar
ENTRYPOINT ["java", "-jar", "/product-app.jar"]