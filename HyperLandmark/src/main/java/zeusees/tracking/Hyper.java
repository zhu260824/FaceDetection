package zeusees.tracking;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Hyper {
    private long session = -1L;
    private boolean TRACK_INIT = false;
    private List<Face> faces;

    public Hyper(Context mContext, String path) {
        session = FaceTracking.createSession(copyModel2SD(mContext, path));
        faces = new ArrayList<>();
    }

    public void detect(byte[] data, int width, int height) {
        if (TRACK_INIT) {
            FaceTracking.update(data, height, width, session);
            int num = FaceTracking.getTrackingNum(session);
            faces.clear();
            for (int i = 0; i < num; i++) {
                int[] landmarks = FaceTracking.getTrackingLandmarkByIndex(i, session);
                int[] faceRect = FaceTracking.getTrackingLocationByIndex(i, session);
                int id = FaceTracking.getTrackingIDByIndex(i, session);
                Face face = new Face(id, faceRect[0], faceRect[1], faceRect[2], faceRect[3], landmarks);
                faces.add(face);
            }
        } else {
            FaceTracking.initTracking(data, height, width, session);
            TRACK_INIT = true;
        }
    }

    public List<Face> getTrackingInfo() {
        return faces;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (session != -1) {
            FaceTracking.releaseSession(session);
        }
    }

    private String copyModel2SD(Context mContext, String path) {
        String modelPath = path + File.separator + "model";
        File file = new File(modelPath);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            copyAssets(mContext, "models", modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelPath;
    }

    /**
     * 复制asset文件到指定目录
     *
     * @param oldPath asset下的路径
     * @param newPath SD卡下保存路径
     */
    public static void copyAssets(Context mContext, String oldPath, String newPath) throws IOException {
        String fileNames[] = mContext.getAssets().list(oldPath);
        if (fileNames.length > 0) {
            File file = new File(newPath);
            file.mkdirs();
            for (String fileName : fileNames) {
                copyAssets(mContext, oldPath + File.separator + fileName, newPath + File.separator + fileName);
            }
        } else {
            InputStream is = mContext.getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        }

    }


}
