.PHONY: build
build: clean
	./gradlew -q jar

.PHONY: clean
clean:
	./gradlew -q clean
