# GreenPepper (Modded by Strator)

## Want to Contribute ? 

### Open tickets on [GitHub/issues](https://github.com/strator-dev/greenpepper/issues)

### Follow the evolution on [Trello/greenpepper2](https://trello.com/greenpepper2)

### Comment and Communicate on [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/strator-dev/greenpepper?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Submit patches/merge request by forking the code

# Build and get the jar

```sh
docker build --tag green-build .
docker run -d --name green green-build true
docker cp green:/usr/src/app/greenpepper-confluence/greenpepper-confluence-plugin/target/greenpepper-confluence3-plugin-${version}-complete.jar .
```
