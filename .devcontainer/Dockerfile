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


FROM openjdk:17-slim-bullseye
ENV EASYOCR_MODULE_PATH=/home/appuser/.EasyOCR

COPY --from=google-java-format /app/google-java-format /usr/local/bin/google-java-format
RUN useradd -mU -s /usr/sbin/nologin appuser
COPY --chown=appuser:appuser japanese_g2.pth $EASYOCR_MODULE_PATH/model/japanese_g2.pth
COPY --chown=appuser:appuser craft_mlt_25k.pth $EASYOCR_MODULE_PATH/model/craft_mlt_25k.pth

RUN apt update && apt -y install --no-install-recommends \
    git \
    python3-pip \
    poppler-utils

# RUN pip install git+https://github.com/kfjt/EasyOCR.git@json-output
RUN git clone -b json-output https://github.com/kfjt/EasyOCR.git \
    && pip install -e EasyOCR
