### Transaction Comparison
This application is used to match transactions between Tutuka and Client files. 

#### Requirements

Ensure you have:

- Java >= 8 Installed (OpenJDK and Oracle JVMs are tested)
- Maven Installed
- npm Installed
- @angular/cli Installed

#### Instructions how to run for local development

##### Server Side
It is a Java/Spring Application.
```$xslt
    - Clone the repository or download and extract the archive file to your local directory.
    - cd /path/transaction_reconciliation_tutuka
    - mvn clean install -DskipTests
    - cd /path/transaction_reconciliation_tutuka/reconcile-core
    - mvn spring-boot:run
```

##### Client Side
It is an Angular application. 
```$xslt
    - cd /path/transaction_reconciliation_tutuka/reconcile-web
    - npm install
    - npm start
```

#### Instructions to build the JAR file
````
    - cd /path/transaction_reconciliation_tutuka
    - mvn clean package -DskipTests
````


#### Instructions to run and debug in Intellij Idea IDE
It is possible to run this application in Intellij Idea IDE and also to debug application using Intellij's debugging facilities. 
To do this, you need to import the project into Intellij workspace:

- Import application project into your Intellij workspace (File->New->Gradle->Project From Existing Sources, choose root directory transaction_reconciliation_tutuka)
- Do a rebuild of the project in Intellij (Build->Rebuild Project)
- Run / debug application by right clicking run button on com.tutuka.reconciliation.Application class and choosing Run (Application.main()). All normal Intellij debugging features (breakpoints, watchpoints etc) should work as expected.


#### Application API
The API for application is documented by Swagger.