# Reladomo build
Config files for Reladomo models are located under the folder below.

```
src/main/reladomo/model
```
  
Reladomo model Java classes are created via the Gradle task below.

```
# gradle genReladomo
```

Auto-generated classes will be located under the folders below.

```
build/generated-sources/reladomo -- auto generated files that are not committed to VCS (git)
src/main/java/path-to-model -- auto generated files that should be committed to VCS (git)
```

# PostgreSQL docker set-up

The sample code depends on PostgreSQL database. PostgreSQL docker instance can be launched/shutdown with the script below.

```
tools/docker-up.sh
```

```
tools/docker-down.sh
```

