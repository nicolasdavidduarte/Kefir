FROM postgres:16-alpine

USER root

# Remove vulnerable gosu binary
RUN rm -f /usr/local/bin/gosu

USER postgres