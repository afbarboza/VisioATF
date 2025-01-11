package com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux;

import com.google.android.apps.common.testing.accessibility.framework.AccessibilityHierarchyCheck;

public class ViolationModel {
    private String log;
    private AccessibilityHierarchyCheck check;

    public ViolationModel(AccessibilityHierarchyCheck check, String log) {
        this.log = log;
        this.check = check;
    }

    public AccessibilityHierarchyCheck getCheck() {
        return check;
    }

    public String getViolationLogMessage() {
        return log;
    }
}
