package com.google.android.apps.common.testing.accessibility.framework.integrations.espresso;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityHierarchyCheck;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityViewCheckResult;

import java.util.ArrayList;

public class VisioAuxLogReport {
    private static ArrayList<String> logMessage;
    private static VisioAuxLogReport instance = null;
    private static ArrayList<AccessibilityHierarchyCheck> checks = null;

    private VisioAuxLogReport() {
        logMessage = new ArrayList<>();
        checks = new ArrayList<>();
    }

    public static VisioAuxLogReport getInstance() {
        if (instance == null) {
            instance = new VisioAuxLogReport();
        }

        return instance;
    }

    public static ArrayList<String> getLogMessage() {
        return logMessage;
    }

    public static ArrayList<AccessibilityHierarchyCheck> getLogChecks() {
        return checks;
    }

    public static void addLogMessage(String log) {
        logMessage.add(log);
    }

    public static void addLogCheck(AccessibilityViewCheckResult result) {
        checks.add(result
                .getAccessibilityHierarchyCheckResult()
                .getCheck());
    }
}
