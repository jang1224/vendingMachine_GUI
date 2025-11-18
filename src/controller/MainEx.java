package controller;

import controller.admin.AdminController;
import controller.user.VMController;
import model.Inventory;
import model.user.AccountManager;
import model.user.VMProcessor;
import view.user.*;

public class MainEx {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        VMProcessor vmProcessor = new VMProcessor(inventory);
        AccountManager accountManager = new AccountManager();

        AdminController adminController = new AdminController(inventory);

        VMFrame frame = new VMFrame(accountManager);

        DisplayPanel displayPanel = frame.getDisplayPanel();
        InputPanel inputPanel = frame.getInputPanel();
        ProductButtonsPanel productButtonsPanel = frame.getProductButtonsPanel();

        VMController vmController = new VMController(
                vmProcessor,
                inventory,
                adminController,
                accountManager,
                inputPanel,
                displayPanel,
                productButtonsPanel
        );
        // controller 설정
        frame.setVMController(vmController);
        displayPanel.setController(vmController);
        inputPanel.setController(vmController);
        productButtonsPanel.setController(vmController);

    }
}
