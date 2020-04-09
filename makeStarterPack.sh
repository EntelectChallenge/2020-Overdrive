#!/bin/bash

mkdir starter-pack

cd game-engine
cp "target/scala-2.13/game-engine-jar-with-dependencies-0.1.jar" ../starter-pack/game-engine.jar
cp default-config.json ../starter-pack/game-config.json
cd ..

cd game-runner
cp makefile run.bat ../starter-pack
cp starter-pack-runner-config.json ../starter-pack/game-runner-config.json
cp target/game-runner-jar-with-dependencies.jar ../starter-pack
cd ..

cp -r reference-bot starter-pack/
cp -r starter-bots starter-pack/

cd starter-pack
sed -i "s/target/./g" run.bat makefile
cd ..


