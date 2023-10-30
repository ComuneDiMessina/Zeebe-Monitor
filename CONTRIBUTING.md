
## Build from source
Build with Maven

### Prepare next release and checkout the release tag
    mvn release:clean release:prepare release:perform -DreleaseVersion=0.26.0 -DdevelopmentVersion=0.26.1-SNAPSHOT -Darguments="-Dmaven.deploy.skip=true"
    git checkout tags/v0.26.0
        
### Compile and package code in an executable JAR
Activate one of the following profile to choose the datasource
* **Postgres** (default)
    `mvn clean package -Ppostgres`
* **Mongodb**
   `mvn clean package -Pmongo`

### Build docker image
See Dockerfile [here](zeebe-monitor-be-boot/Dockerfile)

Go to `zeebe-monitor-be-boot` directory and build the Dockerfile

    /zeebe-monitor-be-boot# docker image build -t zeebe-monitor-be:0.26.0 .

### Tag docker image
    docker tag zeebe-monitor-be:0.26.0  docker.eai.giottolabs.com/zeebe-monitor-be:0.26.0

### Push docker image to the Nexus docker registry
Login to Nexus with yours credentials and push the image
     
    docker login -u username -p password docker.eai.giottolabs.com
    docker push docker.eai.giottolabs.com/zeebe-monitor-be:0.26.0
