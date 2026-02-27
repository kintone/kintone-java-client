# プロキシ経由の動作を確認するためのプロキシサーバー
# ビルド方法は build-and-push.sh を参照
FROM ubuntu:18.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install --no-install-recommends -y squid apache2-utils && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY squid.conf /etc/squid/squid.conf
COPY squid_basic_auth.conf /etc/squid/squid_basic_auth.conf
COPY squid_digest_auth.conf /etc/squid/squid_digest_auth.conf
COPY start.sh /start.sh

RUN adduser --quiet squid && \
    mkdir -p /var/squid && \
    chown squid:squid /var/squid

USER squid

EXPOSE 3128

CMD ["/start.sh"]
