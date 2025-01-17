package zoeque.limitchecker.domain.model;

/**
 * The enum class to specify the type of the notification.
 */
public enum NotifyTypeModel {
  WARN("warn"),
  ALERT("alert");

  String mailType;

  NotifyTypeModel(String mailType) {
    this.mailType = mailType;
  }

  public String getMailType() {
    return this.mailType;
  }
}
