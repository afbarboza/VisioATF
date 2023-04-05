package com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityHierarchyCheck;
import com.google.android.apps.common.testing.accessibility.framework.AccessibilityViewCheckResult;

import java.util.ArrayList;

public class VisioAuxLogReport {
    private ArrayList<String> logMessage;
    private static VisioAuxLogReport instance = null;
    private ArrayList<AccessibilityHierarchyCheck> checks = null;

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

    protected void addLogMessage(String log) {
        logMessage.add(log);
    }

    public ArrayList<String> getLogMessage() {
        return logMessage;
    }

    protected void addLogCheck(AccessibilityViewCheckResult result) {
        checks.add(result
                .getAccessibilityHierarchyCheckResult()
                .getCheck());
    }

    public ArrayList<AccessibilityHierarchyCheck> getLogChecks() {
        return checks;
    }

    public void clearLogs() {
        checks.clear();
        logMessage.clear();
    }
}
