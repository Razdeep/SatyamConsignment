#!/usr/bin/bash

RED='\033[0;31m'
NC='\033[0m'

if [[ -z "$PATH_TO_FX" ]]; then
	echo -e "${RED}Please set the environment variable PATH_TO_FX and rerun."
	exit 1
fi

java --module-path $PATH_TO_FX \
	--add-modules javafx.controls,javafx.base,javafx.graphics,javafx.fxml \
	--add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED \
	--add-exports javafx.base/com.sun.javafx.binding=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED \
	-jar build/libs/SatyamConsignment.jar
