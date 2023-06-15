#!/bin/bash

# Mettre à jour le code source
git pull

# Construire le projet avec Maven
sudo bash mvnw --settings /home/debian/.m2/settings.xml package -Pprod -Psysadmin

# Construire l'image Docker
docker build --no-cache -t spring-demo .

# Arreter le conteneur existant
docker stop spring-demo

# Supprimer le conteneur existant
docker rm spring-demo

# Lancer un nouveau conteneur
docker run -d --net backend --ip 172.18.0.4 --name=spring-demo -p 8080:8080 -v uploaded_files:/uploads spring-demo