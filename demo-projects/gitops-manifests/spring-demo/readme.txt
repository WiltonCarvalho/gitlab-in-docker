docker run -it --rm -v $PWD:/manifests -w /manifests \
  --entrypoint= registry.k8s.io/kustomize/kustomize:v5.0.0 ash
apk add yq

( cd dev; kustomize edit fix; )

yq e -i '.[].value = "my.image.registry/nginx:1.14.1"' dev/deployment-patch-image.yaml
yq e -i '.[].value = "dev-my-nginx.example.com"' dev/ingress-patch-host.yaml

kustomize build dev
