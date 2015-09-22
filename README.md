# [GreenPepper (Modded by Strator)](http://strator-dev.github.io/greenpepper)

## Want to Contribute ? 

### Open tickets on [GitHub/issues](https://github.com/strator-dev/greenpepper/issues)

### Follow the evolution on [Waffle.io](https://waffle.io/strator-dev/greenpepper) 

[![Stories in Ready](https://badge.waffle.io/strator-dev/greenpepper.png?label=ready&title=Ready)](http://waffle.io/strator-dev/greenpepper) 

### Comment and Communicate on [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/strator-dev/greenpepper?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Submit patches/merge request by forking the code

# Build and get the jar

```sh
# set the version to retrieve to the version of the specified pom.xml
GPVERSION=$(grep -m 1 -o '<version>.*</version>' pom.xml | sed -r -e 's@<version>(.+)</version>@\1@')
docker build --tag green-build .
docker run -d --name green green-build true
docker cp green:/usr/src/app/greenpepper/greenpepper-client/target/greenpepper-client-${GPVERSION}-complete.jar .
```
