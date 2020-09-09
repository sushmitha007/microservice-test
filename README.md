#Media-Service
* Spring Boot microservice that exposes API's for 
    * Save the tar file to the database.
    * Get the tar file from the database.
    * List all the tar files present in the database.
* MongoDB is used for storing the data.

##Instructions for running the application

#### This application can be started in 2 ways
 
#####1) With docker-compose.yml

    Steps:
         1) Navigate to the location where `docker-compose.yml` is present.
         2) Run the command `docker-compose up --build -d` to the containers
         3) Access the url `http://localhost:8090/swagger-ui.html` to see the swagger UI
         4) You can execute all the API in swagger
         5) Run the command `docker-compose down` to stop the containers
         
         
#####2) With Minikube

    Steps:
         1) Navigate to the location where `k8s-manifests` folder is present.
         2) Start your minikube.
         3) Run the command `kubectl apply -f mongodb.yml` to create mongodb PV, PVC, Deployment and Service.
         4) Run the command `kubectl apply -f mediaservice.yml` create the Deployment and the NodePort Service.
         5) Get the minikube ip
         6) Access the url `http://<minikube>:31000/swagger-ui.html` to see the swagger UI
         7) You can execute all the API in swagger