# 依赖的基础镜像，maven 3.5 与jdk8
FROM maven:3.9-jdk-8-alpine as builder

# 工作目录
# Copy local code to the container image
WORKDIR /app
# 要复制的代码，复制到 /app 中
COPY pom.xml .
COPY src ./src

# maven 打包命令
# Build a release artifact
RUN mvn package -DskipTests

# Run the web service on container startup
CMD["java", "-jar", "/app/target/user-center-server-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
