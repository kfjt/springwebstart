FROM ghcr.io/graalvm/native-image:ol8-java17-22.2.0 as google-java-format

ARG GOOGLE_JAVA_FORMAT_VERSION="1.15.0"
RUN curl -L -o google-java-format-all-deps.jar https://github.com/google/google-java-format/releases/download/v${GOOGLE_JAVA_FORMAT_VERSION}/google-java-format-${GOOGLE_JAVA_FORMAT_VERSION}-all-deps.jar \
    && native-image \
    --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
    --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
    --initialize-at-build-time=com.sun.tools.javac.file.Locations \
    -H:+ReportExceptionStackTraces \
    -H:+ReportUnsupportedElementsAtRuntime \
    -H:IncludeResourceBundles=com.sun.tools.javac.resources.compiler \
    -H:Class=com.google.googlejavaformat.java.Main  \
    -H:Name="google-java-format" \
    --no-server \
    --no-fallback \
    -jar google-java-format-all-deps.jar google-java-format

# See here for image contents: https://github.com/microsoft/vscode-dev-containers/tree/v0.245.0/containers/java/.devcontainer/base.Dockerfile

FROM openjdk:17-slim-bullseye

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1

COPY --from=google-java-format /app/google-java-format /usr/local/bin/google-java-format
RUN useradd -mU -s /usr/sbin/nologin appuser
RUN apt update && apt -y install --no-install-recommends \
    git
