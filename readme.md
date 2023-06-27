# FQDN on /etc/host
```
echo "172.31.0.1 gitlab.example.com registry.example.com" | sudo tee -a /etc/hosts
```
# Docker Nginx Reverse Proxy
```
cd nginx-proxy
docker-compose up -d
```
```
curl -H "Host: whoami.example.com" http://172.31.0.10 -k
curl -H "Host: test.example.com" https://172.31.0.10 -k
```

# Gitlab Server
```
cd gitlab-server
docker-compose up -d
docker-compose logs gitlab -f --tail 10
```
```
xdg-open http://gitlab.example.com
```
# Gitlab Runners with dind and minio(s3 cache)
```
cd gitlab-runner
# Set the Runner and Readiness Tokens
vi .env
docker-compose up -d
```
# Docker-based Local Kubernetes
```
cd kind-in-dind
docker-compose up -d
docker-compose logs bastion -f
```
# Bastion Host
```
docker-compose exec -it bastion zsh
```
```
kubectl get nodes

kubectl create deployment httpd --image=httpd --port=80
kubectl expose deployment httpd --type=NodePort --port=80 --name=httpd
kubectl create ingress httpd --class=nginx --rule httpd.example.com/=httpd:80

kubectl get pods
kubectl get svc

curl -H "host:httpd.example.com" http://172.29.0.11

kubectl logs deployments/httpd

k9s
```

# Stop All
```
docker-compose --project-directory gitlab-runner down
docker-compose --project-directory kind-in-dind down
docker-compose --project-directory gitlab-server down
docker-compose --project-directory nginx-proxy down
```
```
docker volume rm \
  gitlab_config gitlab_data gitlab_logs \
  gitlab_runner_config_1 gitlab_runner_config_2 \
  minio_data dind_data kind_in_dind_data
```

