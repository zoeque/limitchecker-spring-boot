package zoeque.limitchecker.configuration;

/**
 * The constant class for Schedule planning.
 */
public class ConstantModel {
  /**
   * The cron expression for validation.
   */
  public static final String CRON_FOR_VALIDATION = "${zoeque.limitchecker.schedule:0 8 * * * *}";

  /**
   * The cron expression for deletion.
   */
  public static final String CRON_FOR_DELETION = "${zoeque.limitchecker.schedule:15 8 * * * *}";
}
