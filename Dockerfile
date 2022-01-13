#
# zVNC building Dockerfile
#
# https://github.com/5GApp/zvnc
# Copyright (c) 2020 Tom Zhou<appnet.link@gmail.com>

# Pull base image.
FROM ubuntu:20.04

# Install.
RUN \
 apt-get -y update && \
 apt-get install -y cmake gcc \
 \
 libx11-dev libxkbcommon-x11-dev \
 libxshmfence-dev libxext-dev libxext6 \
 x11proto-dev x11proto-xext-dev nx-x11proto-xext-dev \
 libpam0g-dev libxcb-xkb1 libxkbcommon0 libxkbcommon-x11-0 \
 x11-xkb-utils xkb-data xauth \
 \
 nasm intel2gas

# Add files.
ADD  https://github.com/libjpeg-turbo/libjpeg-turbo/archive/2.0.4.tar.gz /tmp/

COPY . /tmp/zvnc/

# Start building.
RUN \
 cd /tmp/ && tar xvzf 2.0.4.tar.gz && \
 cd /tmp/libjpeg-turbo-2.0.4/ && mkdir -p build && \
 cd build && cmake ../ -DWITH_JAVA=0 && \
 make -j 8 && make install

RUN \
 mkdir /tmp/zvnc/xbuild && cd /tmp/zvnc/xbuild && \
 cmake ../ -DTVNC_USEPAM=0 -DTVNC_BUILDJAVA=0 -DTVNC_USETLS=0 -DTVNC_BUILDNATIVE=0 && \
 make -j 8 && make install

RUN \
 rm -rf /tmp/*

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /opt/TurboVNC/

# Define default command.
CMD ["bash","-c","/opt/TurboVNC/bin/Xvnc -securitytypes none -nomt -alwaysshared -desktop AiWorkSpace -rfbport 5901 -listen inet :1"]
