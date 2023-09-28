package zoeque.limitchecker.configuration;

/**
 * The constant class for Schedule planning.
 */
public class ConstantModel {
  public static final String CRON_FOR_VALIDATION = "${zoeque.limitchecker.schedule:0 8 * * * *}";
  public static final String CRON_FOR_DELETION = "${zoeque.limitchecker.schedule:15 8 * * * *}";
}
