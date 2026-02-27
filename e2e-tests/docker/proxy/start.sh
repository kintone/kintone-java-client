#!/bin/sh

opt=
if [ "${proxy_auth}" = "basic" ]; then
  htpasswd -c -b /var/squid/passwords "${proxy_user}" "${proxy_pass}"
  opt="-N -f /etc/squid/squid_basic_auth.conf"
elif [ "${proxy_auth}" = "digest" ]; then
  echo "${proxy_user}:${proxy_pass}" > /var/squid/passwords
  opt="-N -f /etc/squid/squid_digest_auth.conf"
else
  opt="-N"
fi

exec /usr/sbin/squid $opt
