#!/usr/bin/env bash

#
# intended to be run from the ex/arch directory.
#


SNAPSHOT_VERSION=$1
shift
KEYID=$1
shift
PASSPHRASE=$*

if [ ! "$SNAPSHOT_VERSION" -o ! "$KEYID" -o ! "$PASSPHRASE" ]; then
    echo "usage: $(basename $0) [snapshot_version] [keyid] [passphrase]" >&2
    exit 1
fi



echo ""
echo "releasing (mvn clean deploy -P release)"
echo ""
mvn clean deploy -Prelease -DskipTests -Dpgp.secretkey=keyring:id=$KEYID -Dpgp.passphrase="literal:$PASSPHRASE"
if [ $? != 0 ]; then
    echo "... failed" >&2
    exit 1
fi





echo ""
echo "bumping archetype version to snapshot: $SNAPSHOT_VERSION"
echo ""

echo ""
echo "... mvn versions:set -DnewVersion=$SNAPSHOT_VERSION"
echo ""
mvn versions:set -DnewVersion=$SNAPSHOT_VERSION > /dev/null

echo ""
echo "... git commit -am \"bumping archetype to snapshot: $SNAPSHOT_VERSION\""
echo ""
git commit -am "bumping archetype to snapshot: $SNAPSHOT_VERSION"



echo ""
echo "now run:"
echo ""
echo "git push origin master"
echo ""
