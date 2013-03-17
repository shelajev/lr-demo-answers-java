# Rebel Answers #
Questions & answers web application written in Java (EE) to showcase various LiveRebel features.

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

2. Build per version WAR archives (you can also specify an output directory as argument; by default, it will place the WARs in parent directory)
 
    ```bash
     $ ./build-versions.sh
     ```
3. Upload `lr-demo-answers-java-<version>.war` archives from output directory to LiveRebel.

We have prepared a Vagrant box for Rebel Answers to make trying it out quick and easy. Follow the instructions at https://github.com/gbevin/lr-demo-provisioning (scroll down for Java environment instructions).

## Directory Structure ##
It is a Maven multi-module build, with integration tests in `integration` project and web application in `webapp` project.

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
        rebel.xml                          // JRebel configutation file (not needed for LiveRebel, but if you use JRebel in development)
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

1. Configure application (reference in `webapp/src/main/resources/`)
2. Configure liquibase (reference in `liquibase.properties.sample` TODO
3. Update database `$ liquibase update` TODO
4. Build WAR and deploy it to application server (e.g. Tomcat)
4.1 Variation: use JRebel for development
5. Access the application by `http://localhost:8080/lr-demo-answers-java`

Contributions are more than welcome!
