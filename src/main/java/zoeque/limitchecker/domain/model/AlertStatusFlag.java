package zoeque.limitchecker.domain.model;

/**
 * The flag that means item is already reported or not.
 * REPORTED is the status that already send warning e-mail
 * NOT_REPORTED is the status that is not needed to send e-mail
 * EXPIRED is the status that is already reported and exceeded the alert date.
 */
public enum AlertStatusFlag {
  REPORTED("reported"),
  NOT_REPORTED("not_reported"),
  EXPIRED("expired");

  String reportFlag;
  AlertStatusFlag(String reportFlag) {
    this.reportFlag=reportFlag;
  }

  public String getReportFlag() {
    return reportFlag;
  }
}
