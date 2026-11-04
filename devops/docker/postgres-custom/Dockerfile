# Pin to Alpine to cut Debian base-image High/Critical CVEs (Snyk recommendation)
FROM postgres:16.14-alpine3.23

USER root

# Patch Alpine packages with known High CVEs in the base image (zlib, util-linux)
RUN apk upgrade --no-cache

# Remove vulnerable gosu binary if present
RUN rm -f /usr/local/bin/gosu

USER postgres