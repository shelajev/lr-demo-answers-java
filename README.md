# Rebel Answers #
Questions & answers web application written in Java (EE) and Spring to showcase various LiveRebel features.

* **Version 1.0**
 * Asking questions
 * Writing and accepting answers
 * User registration & authentication
 * Registration with Facebook
* **Version 2.0**
 * Commenting questions and answers
 * Voting questions and answers
* **Version 3.0**
 * Post-deploy Selenium test suite with failure
* **Version 4.0**
 * Fixes test failures
 * User profile
 * Password recovery via e-mail

Each version is represented with a separate branch, where `master` refers to `4.0`.

## Usage ##

1. Clone 
 
    ```bash
     $ git clone git://github.com/zeroturnaround/lr-demo-answers-java.git
     ```

2. Build per version WAR archives (you can also specify an output directory as argument; by default, it will place the WARs in the parent directory)
 
    ```bash
     $ ./build-versions.sh
     ```
3. Upload all `lr-demo-answers-java-<version>.war` archives from the output directory to LiveRebel.

We have prepared a Vagrant box for Rebel Answers to make trying it out quick and easy. Follow the instructions at https://github.com/gbevin/lr-demo-provisioning (scroll down for Java environment instructions).

## Directory Structure ##
The project is structured as a Maven multi-module build. Integration tests are in the `integration` module and the web application itself can be found in the `webapp` mode.

```bash
integration/                               // integration tests (executed automatically after deployment)
  ...
webapp/                                    // the actual web application
  src/
    main/
      java/                                // Java sources
        ...
      resources/                           // non-Java resources, will be placed in `WEB-INF/classes` in the final WAR
        facebook.properties                // non-LiveRebel-aware properties file for Facebook integration
        jdbc.properties                    // non-LiveRebel-aware properties file for JDBC connection
        mail.properties                    // non-LiveRebel-aware properties file for SMTP connection
        liverebel.xml                      // LiveRebel metadata file with application name and version
                                           //   (can also be generated with Maven plugin or specified when uploading package to Command Center)
        rebel.xml                          // JRebel configuration file (not needed for LiveRebel, but handy when you use JRebel in development)
      webapp/
        WEB-INF/
          liverebel/
            expand/                        // Directory for LiveRebel-aware configuration files
              WEB-INF/
                classes/
                  facebook.properties      // LiveRebel-aware properties file for Facebook integration
                  jdbc.properties          // LiveRebel-aware properties file for JDBC connection
                  mail.properties          // LiveRebel-aware properties file for SMTP connection
            db/                            // Database migrations
            scripts/                       // custom hooks for deployment/update (shell scripts)
          ...
```

## Development ##
Rebel Answers requires `Spring MVC 3.2.x`, `MySQL` and your favourite Java application server (e.g. Tomcat). Applying database migrations in development environment needs `liquibase` and `MySQL JDBC driver`.

1. Configure application (examples in `webapp/src/main/resources/`)
2. Create the database called `qa` in your MySQL server
3. Configure liquibase by creating a file called `liquibase.properties` in the project's root directory (example in `liquibase.properties.sample`)
4. Update the database by running the schema migrations in the project's root directory with `$ liquibase update`
5. Build WAR and deploy it to application server (e.g. Tomcat)
6.1 Variation: use JRebel for development
6.2 Other variation: open the IntelliJ IDEA project files to run the application directly in an IDE
7. Access the application through `http://localhost:8080/lr-demo-answers-java`

Contributions are more than welcome!
