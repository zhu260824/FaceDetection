package zeusees.tracking;

public class FaceTracking {
    static {
        System.loadLibrary("zeuseesTracking-lib");
    }

    public native static void update(byte[] data, int height, int width, long session);

    public native static void initTracking(byte[] data, int height, int width, long session);

    public native static long createSession(String modelPath);

    public native static void releaseSession(long session);

    public native static int getTrackingNum(long session);

    public native static int[] getTrackingLandmarkByIndex(int index, long session);

    public native static int[] getTrackingLocationByIndex(int index, long session);

    public native static int getTrackingIDByIndex(int index, long session);
}
