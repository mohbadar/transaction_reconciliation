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

#### Deployment Guide with NGINX and Apache Application Server

  
- ssh to the aws server
- Run: sudo apt update


##### Install NGNIX
- sudo apt install nginx
- sudo ufw app list
- sudo service nginx status
- Add following configuration to `/etc/ngnix/sites-enabled/reconciliation`
```
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    root /var/www/html/reconcile-web;

    # Add index.php to the list if you are using PHP
    index index.html index.htm index.nginx-debian.html;

    server_name _;

    proxy_redirect           off;
    proxy_set_header         X-Real-IP $remote_addr;
    proxy_set_header         X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header         Host $http_host;

    location /api/ {
        proxy_pass http://localhost:2020;
    }
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

##### Installing Tomcat 

- sudo apt-get install tomcat9
- sudo service tomcat9 status
- change port 8080 to 2020 `/var/lib/tomcat9/conf/server.xml`
```
sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
sudo wget https://downloads.apache.org/tomcat/tomcat-9/v9.0.33/bin/apache-tomcat-9.0.33.tar.gz -P /tmp/
sudo tar xf /tmp/apache-tomcat-9*.tar.gz -C /opt/tomcat
sudo ln -s /opt/tomcat/apache-tomcat-9.0.33 /opt/tomcat/latest
sudo chown -RH tomcat: /opt/tomcat/latest
sudo sh -c 'chmod +x /opt/tomcat/latest/bin/*.sh'
sudo nano /etc/systemd/system/tomcat.service

[Unit]
Description=Tomcat 9 servlet container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/default-java"
Environment="JAVA_OPTS=-Djava.security.egd=file:///dev/urandom -Djava.awt.headless=true"

Environment="CATALINA_BASE=/opt/tomcat/latest"
Environment="CATALINA_HOME=/opt/tomcat/latest"
Environment="CATALINA_PID=/opt/tomcat/latest/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ExecStart=/opt/tomcat/latest/bin/startup.sh
ExecStop=/opt/tomcat/latest/bin/shutdown.sh

[Install]
WantedBy=multi-user.target

```

````
sudo systemctl daemon-reload
sudo service tomcat start
sudo service tomcat status
sudo systemctl enable tomcat

sudo ufw allow 2020/tcp
````

##### Deploy ``.war`` file

```
upload the .war file using sftp (FileZilla)

$ sudo service tomcat9 stop
$ cd /var/lib/tomcat9/webapps/
$ sudo rm -rf /var/lib/tomcat9/webapps/ROOT.war
$ sudo rm -rf /var/lib/tomcat9/webapps/ROOT
$ sudo rm -rf /var/lib/tomcat9/logs/*
$ sudo mv ROOT.war /var/lib/tomcat9/webapps/
$ sudo service tomcat9 restart

$ sudo tail -f /var/lib/tomcat9/logs/catalina.out
```
