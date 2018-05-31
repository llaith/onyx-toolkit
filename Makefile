# internal properties
MAVEN_IMAGE=maven:3.3.9-jdk-8

# internal properties
GID=$(shell id -g)
UID=$(shell id -u)
DOCKER_GID=$(shell getent group docker | cut -d: -f3)
PROJECT_NAME=$(shell mvn -q \
    -Dexec.executable="echo" \
    -Dexec.args='$${project.groupId}:$${project.artifactId}:$${project.version}' \
    --non-recursive \
    org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)

_BUILD_COMMAND=$(MAVEN_COMMAND)

# work out what we want to execute
ifeq ($(strip $(MAVEN_COMMAND)),)
$(warning defaulting command to 'clean package')
_BUILD_COMMAND=clean package
endif

# determine which branch we are on if using CI
ifndef BRANCH
BRANCH=$(shell git branch | grep \* | cut -d ' ' -f2)
endif

ifndef CI_COMMAND
ifeq (master, $(findstring master, $(BRANCH)))
    CI_COMMAND=clean deploy
else ifeq (develop, $(findstring develop, $(BRANCH)))
     CI_COMMAND=clean deploy
else ifeq (release, $(findstring release, $(BRANCH)))
     CI_COMMAND=clean deploy
else
    CI_COMMAND=clean install
endif
endif

# Special vars
.RECIPEPREFIX = >
.PHONY: stack-up stack-down build build_clean build_isolated build_shared ci refresh make_cache clear_cache 

# by default make convention, 
all:build_clean

build: build_clean

build_clean: refresh clear_cache make_cache
> @echo "Building [CLEAN] project: ${PROJECT_NAME} with image: ${MAVEN_IMAGE} as [HOST] user"
> docker run \
      --rm -it \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v ${PWD}/.m2:/var/maven/.m2 \
      -v ${PWD}:${PWD} \
      -w ${PWD} \
      -u ${UID}:${DOCKER_GID} \
      -e MAVEN_CONFIG=/var/maven/.m2 \
      ${MAVEN_IMAGE} \
      mvn --settings settings.xml -Duser.home=/var/maven $(_BUILD_COMMAND)

build_isolated: refresh make_cache
> @echo "Building [ISOLATED] project: ${PROJECT_NAME} with image: ${MAVEN_IMAGE} as [HOST] user"
> docker run \
      --rm -it \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v ${PWD}/.m2:/var/maven/.m2 \
      -v ${PWD}:${PWD} \
      -w ${PWD} \
      -u ${UID}:${DOCKER_GID} \
      -e MAVEN_CONFIG=/var/maven/.m2 \
      ${MAVEN_IMAGE} \
      mvn --settings settings.xml -Duser.home=/var/maven $(_BUILD_COMMAND)

build_shared: refresh
> @echo "Building [SHARED] project: ${PROJECT_NAME} with image: ${MAVEN_IMAGE} as [HOST] user"
> docker run \
      --rm -it \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v ~/.m2:/var/maven/.m2 \
      -v ${PWD}:${PWD} \
      -w ${PWD} \
      -u ${UID}:${DOCKER_GID} \
      -e MAVEN_CONFIG=/var/maven/.m2 \
      ${MAVEN_IMAGE} \
      mvn --settings settings.xml -Duser.home=/var/maven $(_BUILD_COMMAND)

ci:
> @echo "Building [CLEAN/EXPORT] project: ${PROJECT_NAME} with image: ${MAVEN_IMAGE} as [CONTAINER] user"
# because this builds as container user (root) and mounts our work dir, we a) want to push the
# artifact to nexus, not our local repo, and b) we want to clean up the (root) files at the end.
> docker run \
      --rm \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v ${PWD}:${PWD} \
      -w ${PWD} \
      $(MAVEN_IMAGE) \
      mvn --settings settings.xml $(CI_COMMAND) clean 
       
refresh:
> docker pull ${MAVEN_IMAGE}

make_cache:
> @echo "Making local cache dir at: ${PWD}/.m2"
> mkdir -p ${PWD}/.m2

clear_cache:
> @echo "Clearing local cache dir at: ${PWD}/.m2"
> rm -rf ${PWD}/.m2

stack-up:
> docker-compose up -d myservice
> docker-compose up -d myservice

stack-down:
> docker-compose down -v
