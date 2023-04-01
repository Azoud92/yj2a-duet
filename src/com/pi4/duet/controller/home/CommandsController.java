package com.pi4.duet.controller.home;

import com.pi4.duet.view.home.CommandsView;

public class CommandsController {
    private CommandsView cv;
    private SettingsController controller;

    public CommandsView getCv() {
        return cv;
    }

    public void setCv(CommandsView cv) {
        this.cv = cv;
    }

    public SettingsController getController() {
        return controller;
    }

    public void setController(SettingsController controller) {
        this.controller = controller;
    }
}
