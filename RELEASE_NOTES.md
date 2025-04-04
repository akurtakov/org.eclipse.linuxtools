# Eclipse Linux Tools: Release notes

This page describes the noteworthy improvements provided by each release of Eclipse Linux Tools.

## Next release...

## 8.18.0

### Docker Tooling

Docker Tooling has been upgraded to 9.0

## 8.17.0

### Docker Tooling

Docker Tooling has been upgraded to 8.0.3.

### Docker Editor

Docker LS updated to version 0.13.0 from version 0.11.0 with the [following enhancements and fixes](https://github.com/rcjsuen/dockerfile-language-server/blob/v0.13.0/CHANGELOG.md#0130---2024-06-18)

## 8.16.0

### Docker Tooling

Docker Tooling has been upgraded to 8.0.2.  This new version of Docker Client supports the latest Docker v4.33.  The Docker Client dependencies have been updated.

## 8.15.0

### Move Linux Tools plug-ins to Java 21

Linux Tools plug-ins now require Java 21

## 8.14.0

### Docker Tooling

Docker Tooling has updated to use Docker Client 7.0.7.  This new version of Docker Client supports the Docker v4.27 release.
The Docker Client dependencies have been cleaned up and updated.

## 8.13.0

### Docker Editor

Docker LS updated to version 0.11.0 from version 0.10.2 with the [following enhancements and fixes](https://github.com/rcjsuen/dockerfile-language-server-nodejs/blob/master/CHANGELOG.md#0110---2023-09-10)

## 8.12.0

### RPM

Createrepo and Rpmstubby plugins are removed and no longer part of the release as they have been badly outdated and no longer userful. 

### Docker Tooling

To support the CDT, a seccomp option has been added to the Docker Launching APIs so that "seccomp=unconfined" can be specified.

Underlying Docker Client dependency has been bumped up to 5.2.2.  This in turn required upgrades to various Docker Client dependencies.

Docker LS updated to version 0.10.2 from version 0.9.0 with the [following editor enhancements and fixes](https://github.com/akurtakov/dockerfile-language-server-nodejs/blob/master/CHANGELOG.md#0102---2023-06-01).

## 8.11.0

### gcov fixes

The gcov parser has been fixed for data generated by gcc 10.1 and higher.

### Docker Tooling

Docker Tooling has switched over from using the containerConfig section in the DockerImageInfo to
using the config structure.  This is a minor change but fixes issues from that older section no
longer being filled in for latest Docker releases.

## 8.10.0

### Docker Tooling Aarch64 Support

Docker Tooling has switched over to use a new jnr stack that supports Aarch64 which is
needed to run on modern macOS machines.

## 8.9.0

### Libhover devhelp support enhanced

Libhover devhelp support now handles multiple devhelp directories specified by the preferences
as devhelp books may be distributed over multiple directories.  In addition, the help pages
for Libhover devhelp have been improved.

### Docker Tooling pullImage rework

A public api in Docker Tooling for pulling images has been reworked so it does not expose
an org.mandas.docker-client exception which means that the consumer must add org.mandas.docker-client
to its requirements.  A new wrapped exception is now supported and the old API has been deprecated.

## 8.8.0

### Move Linux Tools plug-ins to Java 17

Linux Tools plug-ins now require Java 17

### Docker Image Pull Cancellation

A Docker pull of an Image can now be cancelled.  This support was added
as part of a CDT enhancement to allow Images to be specified by name and
pulled if not already in the local Image repository.

### Gcov Support

The Linux Tools Gcov plug-in now support GCC 12.

## 8.7.0

### Update Dockerfile LS to 0.9.0

Changelog available [here](https://github.com/rcjsuen/dockerfile-language-server-nodejs/blob/master/CHANGELOG.md#090---2022-05-04).

### Support Dockerfile.* file types

Dockerfile editor now recognizes Dockerfile.* type too.

## 8.6.0

### Update Dockerfile LS to 0.7.3

Changelog available [here](https://github.com/rcjsuen/dockerfile-language-server-nodejs/blob/master/CHANGELOG.md#073---2021-12-12).
