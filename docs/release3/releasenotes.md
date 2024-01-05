# BookByte Release Notes

## Version: 0.3.0

## Release Date: 2023-11-16

We're thrilled to announce the release of BookByte version 3.0.0. This major update introduces a suite of new features,
enhancements, and significant improvements to our application, reflecting our commitment to providing a robust and
comprehensive library management system.

## **New Features**

### Webapp in React with Next.js and npm

- **Modern Web Application**: A brand-new web application built using React and Next.js, offering enhanced performance
  and user experience.
- **npm for Dependancy Management**: Utilizes npm for efficient dependency management, ensuring a smooth and reliable
  development process. Additionally, npm works well with eclipse-che.

### Updated Diagrams: Sequence and Class Diagrams

- **Clearer System Visualization**: Updated sequence and class diagrams provide a more detailed understanding of the
  system architecture and interactions. Additionally package diagram for our architecture has been updated.

### Backend with REST Server in Spring Boot

- **Comprehensive Backend**: The backend, known as BookBinder, is built using Spring Boot and features three main
  endpoints: book, library, and person, covering all necessary functionalities.

### REST API Documentation Using Swagger

- **Easy API Navigation**: Complete Swagger API documentation for all endpoints, enhancing usability and developer
  experience. They can be found [here](../../bookbyte/bookbinder/docs)

### Implementation of Availability Logic

- **Enhanced Book Management**: Advanced logic to manage the availability status of books, streamlining library
  operations. This is implemented in both webapp and javafx UI, as well as the REST api.

### JavaFX Front-end Integration with REST API and Local Storage

- **Versatile Front-end**: The JavaFX front-end now interacts seamlessly with both the REST API and local storage,
  ensuring functionality even when the server is offline.

### Additional Pipelines for Front-end Development

- **Future-Ready Development**: New CI/CD pipelines for front-end development, laying the groundwork for an agile and
  efficient development process. We are using eslint and prettier for these tests.

### Graphical JavaFX and React End-to-End Tests

- **Robust Testing**: Comprehensive end-to-end tests for the JavaFX application, ensuring reliability and user
  satisfaction. The React webapp uses Cypress for end-to-end testing.

### Expanded Unit Tests for Core Module and Backend

- **Thorough Testing Coverage**: Increased unit testing for both the core module and backend, enhancing code quality and
  reliability. To generate these coverage reports yourself, see the main readme.

## Enhanced User Stories

In this release, we've also incorporated new user stories that provide insights into the updates and enhancements. These
user stories outline the reasons behind our feature upgrades and system improvements, reflecting our commitment to
addressing user needs and feedback. They serve as a guiding framework, ensuring that each update in version 3.0.0 aligns
with our goal of delivering a user-centric and efficient library management system.

### Coverage at 77%

- **Improved Code Quality**: Achieved 77% code coverage, a testament to our commitment to producing high-quality,
  reliable software. Detailed coverage reports can be generated as outlined in the readme. We believe this is suffucient
  as it tests the important parts of our app.

### Build as a Shippable Product Using jpackage and jlink

- **Cross-Platform Compatibility**: Ability to build the application for various platforms using jpackage and jlink,
  enhancing distribution capabilities.

### Updated Documentation

- **Comprehensive Information**: All documentation has been updated to reflect the latest changes and enhancements in
  version 0.3.0.
