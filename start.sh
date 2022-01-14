java --module-path "/home/rajdeep/javafx-sdk-11.0.2/lib" \
	--add-modules javafx.controls,javafx.base,javafx.graphics,javafx.fxml \
	--add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED \
	--add-exports javafx.base/com.sun.javafx.binding=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED \
	-jar build/libs/SatyamConsignment.jar
