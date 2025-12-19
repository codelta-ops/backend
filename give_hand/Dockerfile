# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 先复制 pom，利用缓存
COPY pom.xml .
COPY give_hand-common/pom.xml give_hand-common/pom.xml
COPY give_hand-pojo/pom.xml give_hand-pojo/pom.xml
COPY give_hand-server/pom.xml give_hand-server/pom.xml

# 再复制源码
COPY give_hand-common/src give_hand-common/src
COPY give_hand-pojo/src give_hand-pojo/src
COPY give_hand-server/src give_hand-server/src

# 构建（打包启动模块）
RUN mvn -q -DskipTests package

# ---- run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# 复制启动模块 jar（一般在 give_hand-server/target 下）
COPY --from=build /app/give_hand-server/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080
CMD ["sh","-c","java -jar app.jar --server.port=${PORT}"]
