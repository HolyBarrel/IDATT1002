module edu.ntnu.idatt1002.k01g08.fta {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.json;

    opens edu.ntnu.idatt1002.k01g08.fta to javafx.fxml;
    exports edu.ntnu.idatt1002.k01g08.fta;
    exports edu.ntnu.idatt1002.k01g08.fta.registers;
    opens edu.ntnu.idatt1002.k01g08.fta.registers to javafx.fxml;
    exports edu.ntnu.idatt1002.k01g08.fta.controllers;
    opens edu.ntnu.idatt1002.k01g08.fta.controllers to javafx.fxml;
    exports edu.ntnu.idatt1002.k01g08.fta.guiControllers;
    opens edu.ntnu.idatt1002.k01g08.fta.guiControllers to javafx.fxml;
    exports edu.ntnu.idatt1002.k01g08.fta.util;
    opens edu.ntnu.idatt1002.k01g08.fta.util to javafx.fxml;
    exports edu.ntnu.idatt1002.k01g08.fta.objects.events;
    opens edu.ntnu.idatt1002.k01g08.fta.objects.events to javafx.fxml;
    opens edu.ntnu.idatt1002.k01g08.fta.objects.tournaments to javafx.fxml;
    exports edu.ntnu.idatt1002.k01g08.fta.objects.tournaments;
    exports edu.ntnu.idatt1002.k01g08.fta.objects.team;
    opens edu.ntnu.idatt1002.k01g08.fta.objects.team to javafx.fxml;
}