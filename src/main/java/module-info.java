module com.synthax.synthax_beads {
    requires javafx.controls;
    requires javafx.fxml;
    requires beads;
    requires org.controlsfx.controls;
    requires java.desktop;


    exports com.synthax.controller;
    opens com.synthax.controller to javafx.fxml;
    exports com.synthax;
    opens com.synthax to javafx.fxml;
    exports com.synthax.view;
    opens com.synthax.view to javafx.fxml;
    exports com.synthax.model.enums;
    opens com.synthax.model.enums to javafx.fxml;
    exports com.synthax.model.oscillator;
    opens com.synthax.model.oscillator to javafx.fxml;
    exports com.synthax.model.effects;
    exports com.synthax.model.midi;
}