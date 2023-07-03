FROM docker:24-cli

COPY zshrc /root/.zshrc
COPY kind-config.yaml /root
RUN set -ex \
      && wget https://storage.googleapis.com/kubernetes-release/release/v1.26.2/bin/linux/amd64/kubectl \
            -O /usr/local/bin/kubectl \
      && wget https://kind.sigs.k8s.io/dl/v0.19.0/kind-linux-amd64 -O /usr/local/bin/kind \
      && chmod +x /usr/local/bin/kubectl /usr/local/bin/kind \
      && wget -O- https://get.helm.sh/helm-v3.8.1-linux-amd64.tar.gz | \
            tar zxvf - -C "/usr/local/bin" linux-amd64/helm --strip-components 1 \
      && export KUSTOMIZE_VERSION=v4.5.7 \
      && wget -O- https://github.com/kubernetes-sigs/kustomize/releases/download/kustomize%2F${KUSTOMIZE_VERSION}/kustomize_${KUSTOMIZE_VERSION}_linux_amd64.tar.gz | \
            tar zxvf - -C "/usr/local/bin" \
      && apk add --no-cache --update \
            zsh vim curl git jq yq k9s skopeo \
            alpine-zsh-config zsh-theme-powerlevel10k \
            zsh-syntax-highlighting zsh-autosuggestions \
            sudo glab \
      && adduser -D sudo -s /bin/zsh \
      && echo '%sudo ALL=(ALL) NOPASSWD:ALL' > /etc/sudoers.d/99-sudo-nopasswd \
      && sed '/root:x:0:0:root/ s/ash/zsh/g' -i /etc/passwd

ENTRYPOINT exec sleep infinity
USER sudo
COPY zshrc /home/sudo/.zshrc
COPY kind-config.yaml /home/sudo
WORKDIR /home/sudo
VOLUME /home/sudo
VOLUME /root
STOPSIGNAL KILL