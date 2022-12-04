# RSO: Uporabnikova shramba izdelkov - mikrostoritev

## Prerequisites

```bash
docker run -d --name pg-uporabnikova-shramba-izdelkov -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=uporabnikova-shramba-izdelkov -p 5432:5432 --network uporabnikovaShramba postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar image-catalog-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/uporabnikovaShramba

## Run in IntelliJ IDEA
Add new Run configuration and select the Application type. In the next step, select the module api and for the main class com.kumuluz.ee.EeApplication.

Available at: localhost:8080/v1/uporabnikovaShramba

## Docker commands
```bash
docker build -t uporabnikova-shramba-izdelkov .   
docker images
docker run uporabnikova-shramba-izdelkov    
docker tag uporabnikova-shramba-izdelkov rsoskupina50/uporabnikova-shramba-izdelkov   
docker push rsoskupina50/uporabnikova-shramba-izdelkov
docker ps
```

```bash
docker network create uporabnikovaShramba
docker run -d --name pg-uporabnikova-shramba-izdelkov -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=uporabnikova-shramba-izdelkov -p 5432:5432 --network uporabnikovaShramba postgres:13
docker inspect pg-uporabnikova-shramba-izdelkov
docker run -p 8080:8080 --network uporabnikovaShramba -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-uporabnikova-shramba-izdelkov:5432/uporabnikova-shramba-izdelkov rsoskupina50/uporabnikova-shramba-izdelkov
```

## Kubernetes
```bash
kubectl version
kubectl --help
kubectl get nodes
kubectl create -f uporabnikova-shramba-izdelkov-deployment.yaml 
kubectl apply -f uporabnikova-shramba-izdelkov-deployment.yaml 
kubectl get services 
kubectl get deployments
kubectl get pods
kubectl logs uporabnikova-shramba-izdelkov-deployment-6f59c5d96c-rjz46
kubectl delete pod uporabnikova-shramba-izdelkov-6f59c5d96c-rjz46
```
Secrets: https://kubernetes.io/docs/concepts/configuration/secret/

