# greenpepper-open

## Get the confluence plugin

- First you will need to be sure you have access to greenpepper binaries via Maven
- Then you can build either on Maven on your own computer OR by docker
```
cd confluence
docker build --tag green-build .
docker run -d --name green green-build true
docker cp green:/usr/src/app/greenpepper-confluence-plugin/target/greenpepper-confluence5-plugin-<version>-complete.jar .
```

## Want to Contribute ? 

### Open tickets on [GitHub/issues](https://github.com/strator-dev/greenpepper-open/issues)

### Follow the evolution on [Waffle.io](https://waffle.io/strator-dev/greenpepper-open) 

[![Stories in Ready](https://badge.waffle.io/strator-dev/greenpepper-open.png?label=ready&title=Ready)](http://waffle.io/strator-dev/greenpepper-open)

### Comment and Communicate on [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/strator-dev/greenpepper-open?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Submit patches/merge request by forking the code


