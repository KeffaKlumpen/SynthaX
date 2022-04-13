module com.synthax.synthax_beads {
    requires javafx.controls;
    requires javafx.fxml;
    requires beads;
    requires org.controlsfx.controls;


    opens com.synthax.SynthaX to javafx.fxml;
    exports com.synthax.SynthaX;
    //exports com.synthax.SynthaX.ChainableUGens;
    //opens com.synthax.SynthaX.ChainableUGens to javafx.fxml;
    exports com.synthax.SynthaX.oscillator;
    exports com.synthax.model;
    opens com.synthax.SynthaX.oscillator to javafx.fxml;
}