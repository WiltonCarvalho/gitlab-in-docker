# FQDN on /etc/hosts
```
echo "172.31.0.1 gitlab.example.com registry.example.com" | sudo tee -a /etc/hosts
```
# Docker Nginx Reverse Proxy
```
( cd nginx-proxy; docker-compose up -d; )
```
```
curl -H "Host: whoami.example.com" http://172.31.0.10 -k
curl -H "Host: test.example.com" https://172.31.0.10 -k
```

# Gitlab Server
```
( cd gitlab-server; docker-compose up -d; )

docker logs gitlab -f --tail 10
```
```
xdg-open http://gitlab.example.com
root:kai0Eihipie3Iek7
```
# Gitlab Runner Setup
- Access the Gitlab
  - http://gitlab.example.com
  - root:kai0Eihipie3Iek7
- Get the readiness token and the registration token to configure the runners

# Gitlab Runners with dind and minio(s3 cache)
```
# Set the Runner and Readiness Tokens
vi gitlab-runner/.env

( cd gitlab-runner; docker-compose up -d; )

docker logs runner-1 -f --tail 10
```
# Gitlab Root Token
```
docker exec -it gitlab bash

gitlab-rails runner '
  token = User.find_by_username("root").personal_access_tokens.create(scopes: ["api"], name: "root_token", expires_at: 365.days.from_now);
  token.set_token("kai0Eihipie3Iek7");
  token.save!'
```
# Gitlab Projects Setup
- Create users
  - dev1, dev2, hml1, infra1
- Create user groups
  - users-dev
    - dev1(Maintainer)
    - dev2(Developer)
  - users-hml
    - hml1(Developer)
  - users-infra
    - infra1(Maintainer)
- Create project groups
  - applications
  - gitops-manifests
- Add group users-dev to group applications as Developer
- Add group users-dev to group gitops-manifests as Developer
- Add group users-hml to group gitops-manifests as Developer
- Add group users-infra to group gitops-manifests as Maintainer
- Create gitops-manifests Settings/Group Access Token(DEPLOYMENT_TOKEN)
  - write_repository
  - Developer Role
  - Copy the token Value
- Create applications group Settings/CICD/Protected Variable DEPLOYMENT_TOKEN
  - Use the gitops-manifests Group Access Token
- Create applications group Settings/Repository/Deploy Token REGISTRY_READ_ONLY
  - read_registry
  - useranme: gitlab+deploy-token-1
# Create project applications/spring-demo
```
cd demo-projects/applications/spring-demo
git init --initial-branch=main
git remote add origin http://anything:kai0Eihipie3Iek7@gitlab.example.com/applications/spring-demo.git
git add .
git commit -m "Initial commit"
git push --set-upstream origin main
git push --set-upstream origin HEAD:develop
```
- Protected Branches
  - main(protected) - Maintainer can Push/Merge
  - develop(protected) - Developer can Push/Merge
- Protected Tags
  - "v*" - Developer can Push/Merge
# Create project gitops-manifests/spring-demo
```
cd demo-projects/gitops-manifests/spring-demo
git init --initial-branch=main
git remote add origin http://anything:kai0Eihipie3Iek7@gitlab.example.com/gitops-manifests/spring-demo.git
git add .
git commit -m "Initial commit"
git push --set-upstream origin HEAD:main
git push --set-upstream origin HEAD:hml
git push --set-upstream origin HEAD:dev
```
- Branches
  - main(protected) - Only Maintainer can Push/Accept Merge Requests
  - dev
  - hml
- Any member of the applications group should be able the merge/push to
  - gitops-manifests/spring-demo(dev, hml)
- Any member of the applications group need to have Merge Requests accepted by users-infra
  - gitops-manifests/spring-demo(main)
# Commit a TAG to applications/spring-demo
```
git add .
VERSION=$(grep ^version build.gradle | grep -Eo "[0-9]+\.[0-9]+\.[0-9]*")
git commit -m "version $VERSION"
git tag -a v$VERSION -m "version $VERSION"
git push --tags origin v$VERSION
```

# Docker-based Local Kubernetes
```
( cd kind-in-dind; docker-compose up -d; )

docker logs bastion -f
```
# Bastion Host
```
docker exec -it bastion zsh
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

