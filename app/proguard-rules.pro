# Strip verbose/debug log calls in release (warnings & errors are kept)
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
}
