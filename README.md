# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)


https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatLlqOJpEuGFocjA3K8gagrCjAQGhqRbqdph5Q0doMDQDAACiUDeFAFQACxOAAjI0jrOgSOEsuUnrekGAZBiGrrSpGFHRjAsbBtoEmJsmkElshPLZrmmD-iC0ElFcNwAHKjrM06zug35XhZhTZD2MD9oOvS2fZMCOY2zlmJwq7eH4gReCg6B7gevjMMe6SZJgHkXkU1DXtI3G7tx9Tcc0LQPqoT7dIFc7tr+ZxAiWZXoGZBmVaxMDwfY8VIXFvqoRiGFylhUlMTJMDkmAWmKXWQVoCRTLMWp5RUTGSkcXRtVoIx02qZZ4KaYt8icbAvH8UJoniZhGrSaSRgoNwmSjStU1mhGhSWumwwQDQ21xktYQrbpfVVam5TIfFJkIHmDVsj+fYDhVbKpWAUNOCFK6eOFG6Qrau7QjAADio6solp4peezCWde2O5QV9ijqVQZOatP5suZ5Q-eZEO9XB0K46MqhIZzeNdehv1nQNF3DbdtMTfdZGzZRPILZ9u3LRL5UqeaSaXjB5RaYKXEHdAR1iUL2Ei+6zV89zsJSzNT0UfNAXKkNeNrQ9LHs473MVCdvWdkze7m2oINg9VbnpVZpZU9z5QVD0EcoAAktIswnpkBp2W+QoIKADap-5sdpygeyNC5GUh3DCNDrHpvR7HCdJ0l+odPnsw6JnIDZ43uejvnhdLqFKProE2A+FA2DcPAuqZDjo4pPXRM5CTGthzeDSU9TwTK+gFdd6OxfHPpwfMxvz69HnO-1cHbOazAcmZFzKCwnAE8oHfAtYkb-XraLFLi+Nc5W6pNs5pyw+tpRW30j7O2lpteUICdb7T4vrYShtTrG0-qbG+z9RywlPqMf+atAGaRtFPUYgpkgZFSO7FA+cjb7wBuPL0t9RyB3PqmV2Jcrg12kLvDs6suzz3LifUcCde7IzXBFAIlgrrwWSDAAAUhAHkxDDABBblnOe5hL5LyqJSO8LRY401-pvXoo9gCSKgHACA8EoCzE4dwyqvsVpDhMWYixVibFCK4SwqCi8toACsFFoDvrCeRPIX4oDRN1d+MhzqmzFgpO6kDrbsiAbybWX0AoQNVhGaBsFYEcV1gggSSCvYwWFmgvCYAgmcLwY9ZJsteR31omEThiSNo+JgY0-J8DDrCScCUp0ek-wNXKHrIpTgADMzQkAADN7a6g4GEeQ5UUEfweuUPwWhGGjGwR498mcXGWOgDU8ibFsCbMMJ0sBlCE40KGQfORASwk5lBl4nhpM0zeRhrwsuXlobLjCgPAIXhTFdi9LAYA2BR6EHiIkGehM4aaMytlXK+VWjGC+XcuhIBuB4FeY1N22KwWWyiVk2SOK4THJlpEV671gAO02cAW5-1gQIzxWwsOnyGbfOJqy-5QA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
