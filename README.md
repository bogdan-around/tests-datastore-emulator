# notes to google support
```bash
# start the emulator
gcloud beta emulators datastore start --store-on-disk --data-dir "./datastore-emulator" --project=bogdanplayground01 --host-port=localhost:8484

# export variables
export DATASTORE_EMULATOR_HOST=localhost:8484
export DATASTORE_PROJECT_ID=bogdanplayground01
export DATASTORE_USE_PROJECT_ID_AS_APP_ID=true
export GOOGLE_CLOUD_PROJECT=bogdanplayground01

#spring profile
export SPRING_PROFILES_ACTIVE=localhost-no-memcache

# run env-init using command substitution
$(gcloud beta emulators datastore env-init)

# start appengine
./gradlew appengineRun
```