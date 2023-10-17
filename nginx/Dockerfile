FROM ubuntu:18.04
LABEL maintainer Eunsil Gil
LABEL contect eunsil.study@gmail.com
ENV DEBIAN_FRONTEND=noninterative
ENV TZ=Asia/Seoul
RUN sudo apt-get update -y
RUN sudo apt-get install -y curl nginx
COPY index.html /var/www/html/index.html

CMD [ "nginx", "-g", "deamon off;" ]
EXPOSE 80