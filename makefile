default: engine copy-jar runner

.PHONY: clean
clean: clean-engine clean-runner

.PHONY: clean-engine
clean-engine:
	cd game-engine && sbt clean

.PHONY :clean-runner
clean-runner:
	cd game-runner && mvn clean

.POHNY: engine
engine:
	cd game-engine && sbt publishM2

.PHONY: runner
runner:
	cd game-runner && mvn install

.PHONY: copy-jar
copy-jar:
	cp game-engine/target/scala-2.13/game-engine-jar-with-dependencies-0.1.jar game-runner/game-engine.jar
