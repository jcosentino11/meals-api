
JAR_NAME=meals-api-0.1.0-SNAPSHOT
WORKSPACE_DIR=$(PWD)

build:
	lein compile
	lein uberjar
	docker run --rm --name graal -v $(WORKSPACE_DIR):/workspace oracle/graalvm-ce \
		/bin/bash -c " \
			gu install native-image; \
			native-image \
				--allow-incomplete-classpath \
				--enable-https \
				--no-server \
				--no-fallback \
				-jar /workspace/target/$(JAR_NAME).jar; \
			cp $(JAR_NAME) /workspace/target; \
		"