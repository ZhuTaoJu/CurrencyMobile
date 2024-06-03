//
// Created by Administrator on 2024/5/24.
//

#include "MyServiceJNI.h"

// C++ 代码
#include <jni.h>
#include <android/log.h>

// 日志标签
#define LOG_TAG "MyService"

// JNI 函数，启动服务
extern "C"
JNIEXPORT void JNICALL
Java_com_yidatong_debutwork_JNIS_Services_MyJNIService_startMyService(JNIEnv *env, jobject obj, jobject jContext) {
    // 获取 Java VM 指针
    JavaVM *jvm = nullptr;
    jint res = env->GetJavaVM(&jvm);
    if (res != JNI_OK) {
//        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Failed to get JVM");
        return;
    }

    // 获取 Context 类引用
    jclass contextClass = env->GetObjectClass(jContext);
        if (contextClass == nullptr) {
//        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Failed to get Context class");
        return;
    }

    // 获取 startService 方法的 ID
    jmethodID startServiceMethodID = env->GetMethodID(contextClass, "startService", "(Landroid/content/Intent;)Landroid/content/ComponentName;");
        if (startServiceMethodID == nullptr) {
//            __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Failed to get startService method ID");
            env->DeleteLocalRef(contextClass);
        return;
    }

    // 创建 Intent 对象
    jclass intentClass = env->FindClass("android/content/Intent");
    jmethodID newIntentConstructor = env->GetMethodID(intentClass, "<init>", "(Landroid/content/Context;Ljava/lang/Class;)V");
    jobject intent = env->NewObject(intentClass, newIntentConstructor, jContext, env->FindClass("com/yidatong/debutwork/RemoteViews/Notifications/RtvwNtfcation"));

    // 创建 ComponentName 对象
    jclass componentNameClass = env->FindClass("android/content/ComponentName");
    jmethodID componentNameConstructor = env->GetMethodID(componentNameClass, "<init>", "(Landroid/content/Context;Ljava/lang/Class;)V");
    jobject componentName = env->NewObject(componentNameClass, componentNameConstructor, jContext, env->FindClass("com/yidatong/debutwork/RemoteViews/Notifications/RtvwNtfcation"));

    // 启动服务
    jobject service = env->CallObjectMethod(jContext, startServiceMethodID, intent);

    // 清理局部引用
    env->DeleteLocalRef(intent);
    env->DeleteLocalRef(componentName);
    env->DeleteLocalRef(contextClass);
    env->DeleteLocalRef(intentClass);
    env->DeleteLocalRef(componentNameClass);
}


