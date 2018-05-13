# meals-api [![Build Status](https://travis-ci.org/jcosentino11/meals-api.svg?branch=master)](https://travis-ci.org/jcosentino11/meals-api) [![Coverage Status](https://coveralls.io/repos/github/jcosentino11/meals-api/badge.svg?branch=master)](https://coveralls.io/github/jcosentino11/meals-api?branch=master) 
_A meal planning service._

## Development

### Build the meals-api docker image

```
docker build --build-arg JAR_FILE=meals-api-0.0.1-SNAPSHOT.jar -t meals-api .
```

### Run meals-api & dependencies
```
docker-compose -f docker-compose.yml up -d
```

*NOTE*: Docker + Windows 7 is strange sometimes.  If you want to access the api, use ip from `docker-machine ip` instead of localhost