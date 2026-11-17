FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN javac *.java
CMD ["sh", "-c", "java BibliotecaApi ${PORT:-8080}"]
