# Group gr2335 Bookbyte-project

[open in Eclipse Che](https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2335/gr2335?new)

## Requirements

### Maven

We are using maven `3.9.4` for the project.

### Java

We are using java `17.0.8.1` for the project.

### Spring-Boot

We are using Spring-Boot `3.1.5` for the backend.

### Node

We are using node `16.20.2` for the webapp.

### Npm

We are using npm `8.19.4` for the webapp.

## Running the project

To build and run the project, start by running ``mvn install`` in the [bookbyte folder](./bookbyte/README.md). Then
run ``cd ui`` to enter the `ui` module, and then ``mvn javafx:run`` to run the application.

## Running the backend

See [getting_started.md](./bookbyte/bookbinder/docs/getting_started.md)

## Running the webapp

To build and run the webapp, make sure you have npm installed and the backend is running. Run the following:

1. `cd bookbyte-webapp`
2. `npm`
3. `npm install`
4. `npm run dev`
5. The webapp will launch on [localhost:3000](http://localhost:3000)

## Building the project

To build the project, run ``mvn install`` in the [bookbyte folder](./bookbyte/README.md).

### Create shippable product using jpackage

To create a shippable `msi` file (Windows only) run the following command in the `bookbyte/ui` folder:
```
mvn exec:exec@jpackage-execution
```
Note: This requires you to have downloaded `javafx-jmods` to the correct path on your computer. 

## Documentation

### User stories

User stories can be found in the [userstories.md](./bookbyte/docs/userstories.md) file.

### About the project

Detailed information about the project can be found in the [docs folder](./bookbyte/docs/README.md).

## Plan for the project

We design our project from the userstories. We start from the first story so we start with designing the library.
Afterwards we will design a catalogue to browse the various books. Lastly we will add borrow/return functionality.

### Release Notes

Detailed release notes can be found [here](docs)

### REST API docs

REST API docs can be found at [getting_started.md](./bookbyte/bookbinder/docs/getting_started.md)

### Test Coverage

To generate jacoco test coverage reports run the following:

* `cd bookbyte`
* `mvn clean verify -DskipUiTests=true`
* You can find the generated report in `/bookbyte/core/target/site/index.html` after thge commands have been run.
