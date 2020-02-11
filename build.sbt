resolvers += Resolver.sonatypeRepo("public")
resolvers += Resolver.bintrayRepo("entelect-challenge", "game-engine")

name := "game-engine"

assemblyJarName in assembly := s"${name.value}-jar-with-dependencies-${version.value}.jar"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "za.co.entelect.challenge" % "game-engine-interface" % "2019.2.0"

fork := true