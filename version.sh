#!/bin/bash

PROJECT_DIR=$(cd "$(dirname "$0")" && pwd)

LAST_TAG_REV=$(git rev-list --tags --max-count=1)
LAST_TAG=$(git describe --tags $LAST_TAG_REV)

COMMIT_COUNT=$(git log --oneline | wc -l | sed -e "s/[ \t]*//g")

echo $LAST_TAG-$COMMIT_COUNT
