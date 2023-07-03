FROM --platform=amd64 amazoncorretto:11 AS builder

ARG AWS_PROFILE=${AWS_PROFILE:-default}
ARG AWS_CREDENTIAL_PROFILES_FILE=/.aws/credentials
ARG AWS_SHARED_CREDENTIALS_FILE=/.aws/credentials
ARG AWS_REGION=sa-east-1

WORKDIR /code
COPY . .

ARG GRADLE_USER_HOME=/tmp/build_cache/gradle
RUN --mount=type=cache,target=/tmp/build_cache/gradle \
    --mount=type=secret,id=aws,target=/.aws/credentials,uid=0,mode=0440 \
    set -ex \
    && yum -y install findutils \
    && ./gradlew --project-cache-dir /tmp/build_cache/gradle clean build -i

RUN set -ex \
    && JAR_FILE=$(ls -1 build/libs/*.jar | grep -v '\-plain.jar') \
    && java -Djarmode=layertools -jar $JAR_FILE extract

FROM amazoncorretto:11-al2023-headless
USER 999:0
WORKDIR /app
COPY --from=builder /code/dependencies/ ./
COPY --from=builder /code/spring-boot-loader/ ./
COPY --from=builder /code/snapshot-dependencies/ ./
COPY --from=builder /code/application/ ./
ENTRYPOINT ["/usr/bin/java", "org.springframework.boot.loader.JarLauncher"]
