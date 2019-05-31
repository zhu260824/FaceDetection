#include <android/log.h>
#include <jni.h>
#include <string>
#include <vector>
#include <android/bitmap.h>
// ncnn
#include "net.h"
#include "mtcnn.h"

using namespace std;
static MTCNN *mtcnn;

#define TAG "MTCNN"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

//sdk是否初始化成功
bool SDK_INIT = false;

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_zl_face_MTCNN_initModelPath(JNIEnv *env, jobject instance, jstring modelPath_) {
    if (SDK_INIT) {
        return true;
    }
    if (NULL == modelPath_) {
        return false;
    }
    //获取MTCNN模型的绝对路径的目录（不是/aaa/bbb.bin这样的路径，是/aaa/)
    const char *modelPath = env->GetStringUTFChars(modelPath_, 0);
    if (NULL == modelPath) {
        return false;
    }
    string tFaceModelDir = modelPath;
    string tLastChar = tFaceModelDir.substr(tFaceModelDir.length() - 1, 1);
    //目录补齐/
    if ("\\" == tLastChar) {
        tFaceModelDir = tFaceModelDir.substr(0, tFaceModelDir.length() - 1) + "/";
    } else if (tLastChar != "/") {
        tFaceModelDir += "/";
    }
    LOGD("init, tFaceModelDir=%s", tFaceModelDir.c_str());
    mtcnn = new MTCNN(tFaceModelDir);
    mtcnn->SetMinFace(20);
    mtcnn->SetNumThreads(4);
    env->ReleaseStringUTFChars(modelPath_, modelPath);
    SDK_INIT = true;
    return true;
}

JNIEXPORT jboolean JNICALL
Java_com_zl_face_MTCNN_unInit(JNIEnv *env, jobject instance) {
    if (!SDK_INIT) {
        LOGD("人脸检测MTCNN模型已经释放过或者未初始化");
        return true;
    }
    jboolean tDetectionUnInit = false;
    delete mtcnn;
    SDK_INIT = false;
    tDetectionUnInit = true;
    LOGD("人脸检测初始化锁，重新置零");
    return tDetectionUnInit;
}

JNIEXPORT jboolean JNICALL
Java_com_zl_face_MTCNN_setMinFace(JNIEnv *env, jobject instance, jint minSize) {
    if (!SDK_INIT) {
        LOGD("人脸检测MTCNN模型SDK未初始化，直接返回");
        return false;
    }
    if (minSize <= 20) {
        minSize = 20;
    }
    mtcnn->SetMinFace(minSize);
    return true;
}

JNIEXPORT jboolean JNICALL
Java_com_zl_face_MTCNN_setDetectThread(JNIEnv *env, jobject instance, jint threadNumber) {
    if (!SDK_INIT) {
        LOGD("人脸检测MTCNN模型SDK未初始化，直接返回");
        return false;
    }
    if (threadNumber != 1 && threadNumber != 2 && threadNumber != 4 && threadNumber != 8) {
        LOGD("线程只能设置1，2，4，8");
        return false;
    }
    mtcnn->SetNumThreads(threadNumber);
    return true;
}

JNIEXPORT jboolean JNICALL
Java_com_zl_face_MTCNN_setTimeCount(JNIEnv *env, jobject instance, jint count) {
    if (!SDK_INIT) {
        LOGD("人脸检测MTCNN模型SDK未初始化，直接返回");
        return false;
    }
    mtcnn->SetTimeCount(count);
    return true;
}

JNIEXPORT jobjectArray JNICALL
Java_com_zl_face_MTCNN_detect(JNIEnv *env, jobject instance, jobject bitmap) {
    jobjectArray faceArgs = nullptr;
    if (!SDK_INIT) {
        LOGD("人脸检测MTCNN模型SDK未初始化，直接返回空");
        return faceArgs;
    }
    if (NULL == bitmap) {
        LOGD("检测目标不能为空");
        return faceArgs;
    }

    // ncnn from bitmap
    ncnn::Mat img;
    {
        AndroidBitmapInfo info;
        AndroidBitmap_getInfo(env, bitmap, &info);
        int width = info.width;
        int height = info.height;
        if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
            return faceArgs;
        void *imgData;
        AndroidBitmap_lockPixels(env, bitmap, &imgData);
        img = ncnn::Mat::from_pixels((const unsigned char *) imgData, ncnn::Mat::PIXEL_RGBA2RGB,
                                     width, height);
        AndroidBitmap_unlockPixels(env, bitmap);
    }

    std::vector<Bbox> faces;
    mtcnn->detect(img, faces);
    int32_t num_face = static_cast<int32_t>(faces.size());
    LOGD("检测到的人脸数目：%d\n", num_face);

    jclass faceClass = env->FindClass("com/zl/face/MTFaceInfo");//获取Face类
    jmethodID faceClassInitID = (env)->GetMethodID(faceClass, "<init>", "()V");
    jfieldID faceScore = env->GetFieldID(faceClass, "score", "F");
    jfieldID faceRect = env->GetFieldID(faceClass, "rect",
                                        "Landroid/graphics/Rect;");//获取faceRect的签名
//    jfieldID points=env->GetFieldID(faceClass,"points","[F");
    /**
     * 获取RECT类以及对应参数的签名
     */
    jclass rectClass = env->FindClass("android/graphics/Rect");//获取到RECT类
    jmethodID rectClassInitID = (env)->GetMethodID(rectClass, "<init>", "()V");
    jfieldID rect_left = env->GetFieldID(rectClass, "left", "I");
    jfieldID rect_top = env->GetFieldID(rectClass, "top", "I");
    jfieldID rect_right = env->GetFieldID(rectClass, "right", "I");
    jfieldID rect_bottom = env->GetFieldID(rectClass, "bottom", "I");
    faceArgs = (env)->NewObjectArray(num_face, faceClass, 0);

    for (int i = 0; i < num_face; ++i) {
        int left = faces[i].x1;
        int top = faces[i].y1;
        int right = faces[i].x2;
        int bottom = faces[i].y2;
        float score = faces[i].score;
        jobject newFace = (env)->NewObject(faceClass, faceClassInitID);
        jobject newRect = (env)->NewObject(rectClass, rectClassInitID);
        (env)->SetIntField(newRect, rect_left, left);
        (env)->SetIntField(newRect, rect_top, top);
        (env)->SetIntField(newRect, rect_right, right);
        (env)->SetIntField(newRect, rect_bottom, bottom);
        (env)->SetObjectField(newFace, faceRect, newRect);
        (env)->SetFloatField(newFace, faceScore, score);
        (env)->SetObjectArrayElement(faceArgs, i, newFace);
    }
    return faceArgs;
}


JNIEXPORT jobjectArray JNICALL
Java_com_zl_face_MTCNN_maxDetect(JNIEnv *env, jobject instance, jobject bitmap) {
    jobjectArray faceArgs = nullptr;
    if (!SDK_INIT) {
        LOGD("人脸检测MTCNN模型SDK未初始化，直接返回空");
        return faceArgs;
    }
    if (NULL == bitmap) {
        LOGD("检测目标不能为空");
        return faceArgs;
    }

    // ncnn from bitmap
    ncnn::Mat img;
    {
        AndroidBitmapInfo info;
        AndroidBitmap_getInfo(env, bitmap, &info);
        int width = info.width;
        int height = info.height;
        if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
            return faceArgs;
        void *imgData;
        AndroidBitmap_lockPixels(env, bitmap, &imgData);
        img = ncnn::Mat::from_pixels((const unsigned char *) imgData, ncnn::Mat::PIXEL_RGBA2RGB,
                                     width, height);
        AndroidBitmap_unlockPixels(env, bitmap);
    }

    std::vector<Bbox> faces;
    mtcnn->detectMaxFace(img, faces);
    int32_t num_face = static_cast<int32_t>(faces.size());
    LOGD("检测到的人脸数目：%d\n", num_face);

    jclass faceClass = env->FindClass("com/zl/face/MTFaceInfo");//获取Face类
    jmethodID faceClassInitID = (env)->GetMethodID(faceClass, "<init>", "()V");
    jfieldID faceScore = env->GetFieldID(faceClass, "score", "F");//获取int类型参数confidence
    jfieldID faceRect = env->GetFieldID(faceClass, "rect",
                                        "Landroid/graphics/Rect;");//获取faceRect的签名
//    jfieldID points=env->GetFieldID(faceClass,"points","[F");
    /**
     * 获取RECT类以及对应参数的签名
     */
    jclass rectClass = env->FindClass("android/graphics/Rect");//获取到RECT类
    jmethodID rectClassInitID = (env)->GetMethodID(rectClass, "<init>", "()V");
    jfieldID rect_left = env->GetFieldID(rectClass, "left", "I");//获取x的签名
    jfieldID rect_top = env->GetFieldID(rectClass, "top", "I");//获取y的签名
    jfieldID rect_right = env->GetFieldID(rectClass, "right", "I");//获取width的签名
    jfieldID rect_bottom = env->GetFieldID(rectClass, "bottom", "I");//获取height的签名
    faceArgs = (env)->NewObjectArray(num_face, faceClass, 0);

    for (int i = 0; i < num_face; ++i) {
        int left = faces[i].x1;
        int top = faces[i].y1;
        int right = faces[i].x2;
        int bottom = faces[i].y2;
        float score = faces[i].score;
        jobject newFace = (env)->NewObject(faceClass, faceClassInitID);
        jobject newRect = (env)->NewObject(rectClass, rectClassInitID);
        (env)->SetIntField(newRect, rect_left, left);
        (env)->SetIntField(newRect, rect_top, top);
        (env)->SetIntField(newRect, rect_right, right);
        (env)->SetIntField(newRect, rect_bottom, bottom);
        (env)->SetObjectField(newFace, faceRect, newRect);
        (env)->SetFloatField(newFace, faceScore, score);
        (env)->SetObjectArrayElement(faceArgs, i, newFace);
    }
    return faceArgs;
}


}