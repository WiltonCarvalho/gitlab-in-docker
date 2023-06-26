########### <Start> ###########
echo "172.31.0.1 gitlab.example.com registry.example.com" | sudo tee -a /etc/hosts

cd nginx-proxy
docker-compose up -d

cd gitlab-server
docker-compose up -d
docker-compose logs gitlab -f --tail 10

cd kind-in-dind
docker-compose up -d
docker-compose logs bastion -f
docker-compose exec -it bastion zsh
  kubectl get nodes
  kubectl pod -A
  k9s

cd gitlab-runner
vi .env
docker-compose up -d
########### </Start> ###########

#################################
########### <Stop> ###########
docker-compose --project-directory gitlab-runner down
docker-compose --project-directory kind-in-dind down
docker-compose --project-directory gitlab-server down
docker-compose --project-directory nginx-proxy down

docker volume rm \
  gitlab_config gitlab_data gitlab_logs \
  gitlab_runner_config_1 gitlab_runner_config_2 \
  minio_data dind_data kind_in_dind_data
########### </Stop> ###########

