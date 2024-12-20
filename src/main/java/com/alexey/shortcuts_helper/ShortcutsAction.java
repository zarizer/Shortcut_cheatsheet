package com.alexey.shortcuts_helper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ShortcutsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // Create a dialog with the hotkeys table
        ShortcutsDialog dialog = new ShortcutsDialog();
        dialog.show();
    }
}
