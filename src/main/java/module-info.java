module com.synthax.synthax_beads {
    requires javafx.controls;
    requires javafx.fxml;
    requires beads;


    opens com.synthax.synthax_beads to javafx.fxml;
    exports com.synthax.synthax_beads;
    exports com.synthax.synthax_beads.ChainableUGens;
    opens com.synthax.synthax_beads.ChainableUGens to javafx.fxml;
}