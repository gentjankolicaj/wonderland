#!/bin/sh

# Replace these with your GitHub username, repository name, and asset name
USERNAME="gentjankolicaj"
REPO="wonderland"
ASSET_NAME="artifacts.zip"  # Replace with the actual asset name

# Fetch the latest release information
latest_release=$(curl -s https://api.github.com/repos/$USERNAME/$REPO/releases/latest)

# Extract the download URL of the asset
artifact_url=$(echo "$latest_release" | grep -o "\"browser_download_url\":\s*\"[^\"]*\"" | grep "$ASSET_NAME" | cut -d '"' -f 4)

echo "Release artifact url : $artifact_url"

# Download the asset using curl
curl -L -o $ASSET_NAME $artifact_url

echo "Downloaded release artifact : $ASSET_NAME"

unzip artifacts.zip

echo "$(pwd)"

echo "Release artifacts unzipped,dir content : $(ls -alh)"

cp ./artifacts/*shaded.jar ./

echo "Deleting zipped file : $(rm ./artifacts.zip)"

echo "Deleting unzipped directory: $(rm -rf ./artifacts)"

echo "$(pwd) , dir content : $(ls -alh)"
