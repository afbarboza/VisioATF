package com.google.android.apps.common.testing.accessibility.framework.integrations.visioaux;

import java.util.ArrayList;

public class VisioAuxLogReport {
    private static VisioAuxLogReport instance = null;
    private ArrayList<ViolationModel> reports;

    private VisioAuxLogReport() {
        reports = new ArrayList<>();
    }

    public static VisioAuxLogReport getInstance() {
        if (instance == null) {
            instance = new VisioAuxLogReport();
        }

        return instance;
    }

    protected void addReport(ViolationModel violationModel) {
        this.reports.add(violationModel);
    }

    public ArrayList<ViolationModel> getReports() {
        return this.reports;
    }

    public void clearAllReports() {
        reports.clear();
    }
}
