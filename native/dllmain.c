#include <windows.h>
#include "jni.h"
#include "jvmti.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

JavaVM* jvm;
JNIEnv* jniEnv;
jvmtiEnv* jvmti;
jclass envClass;
jmethodID methodID;

const char* getLoaderJarPath() {
    char userProfile[MAX_PATH];
    char modsFolderPath[MAX_PATH];
    DWORD result = GetEnvironmentVariableA("USERPROFILE", userProfile, MAX_PATH);
    if (result != 0 && result < MAX_PATH) {
        sprintf_s(modsFolderPath, MAX_PATH, "%s\\.moran\\loratadine-1.0.jar", userProfile);
        return modsFolderPath;
    }
    else {
        return NULL;
    }
}

DWORD WINAPI Inject(LPVOID parm) {
    HMODULE jvmHandle = GetModuleHandle(L"jvm.dll");
    if (!jvmHandle) return 0;
    typedef jint(JNICALL* fnJNI_GetCreatedJavaVMs)(JavaVM**, jsize, jsize*);
    fnJNI_GetCreatedJavaVMs JNI_GetCreatedJavaVMs = (fnJNI_GetCreatedJavaVMs)GetProcAddress(jvmHandle, "JNI_GetCreatedJavaVMs");
    if (!JNI_GetCreatedJavaVMs) return 0;
    if (JNI_GetCreatedJavaVMs(&jvm, 1, NULL) != JNI_OK || (*jvm)->AttachCurrentThread(jvm, (void**)&jniEnv, NULL) != JNI_OK) return 0;
    (*jvm)->GetEnv(jvm, (void**)&jvmti, JVMTI_VERSION_1_2);
    if (!jvmti) return 0;
    jclass threadClass = (*jniEnv)->FindClass(jniEnv, "java/lang/Thread");
    jmethodID getAllStackTraces = (*jniEnv)->GetStaticMethodID(jniEnv, threadClass, "getAllStackTraces", "()Ljava/util/Map;");
    if (!getAllStackTraces) return 0;
    jobjectArray threads = (jobjectArray)(*jniEnv)->CallObjectMethod(jniEnv, (*jniEnv)->CallObjectMethod(jniEnv, (*jniEnv)->CallStaticObjectMethod(jniEnv, threadClass, getAllStackTraces), (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->FindClass(jniEnv, "java/util/Map"), "keySet", "()Ljava/util/Set;")), (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->FindClass(jniEnv, "java/util/Set"), "toArray", "()[Ljava/lang/Object;"));
    if (!threads) return 0;
    jsize arrlength = (*jniEnv)->GetArrayLength(jniEnv, threads);
    jobject clientThread = NULL;
    for (int i = 0; i < arrlength; i++) {
        jobject thread = (*jniEnv)->GetObjectArrayElement(jniEnv, threads, i);
        if (thread == NULL) continue;
        jclass threadClass = (*jniEnv)->GetObjectClass(jniEnv, thread);
        jstring name = (*jniEnv)->CallObjectMethod(jniEnv, thread, (*jniEnv)->GetMethodID(jniEnv, threadClass, "getName", "()Ljava/lang/String;"));
        const char* str = (*jniEnv)->GetStringUTFChars(jniEnv, name, FALSE);
        if (!strcmp(str, "Render thread")) {
            clientThread = thread;
            (*jniEnv)->ReleaseStringUTFChars(jniEnv, name, str);
            break;
        }
        (*jniEnv)->ReleaseStringUTFChars(jniEnv, name, str);
    }
    if (!clientThread) return 0;
    jclass urlClassLoader = (*jniEnv)->FindClass(jniEnv, "java/net/URLClassLoader");
    jmethodID findClass = (*jniEnv)->GetMethodID(jniEnv, urlClassLoader, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID addURL = (*jniEnv)->GetMethodID(jniEnv, urlClassLoader, "addURL", "(Ljava/net/URL;)V");
    jclass fileClass = (*jniEnv)->FindClass(jniEnv, "java/io/File");
    jmethodID init = (*jniEnv)->GetMethodID(jniEnv, fileClass, "<init>", "(Ljava/lang/String;)V");
    jstring filePath = (*jniEnv)->NewStringUTF(jniEnv, getLoaderJarPath());
    jobject file = (*jniEnv)->NewObject(jniEnv, fileClass, init, filePath);
    jmethodID toURI = (*jniEnv)->GetMethodID(jniEnv, fileClass, "toURI", "()Ljava/net/URI;");
    jobject uri = (*jniEnv)->CallObjectMethod(jniEnv, file, toURI);
    jclass URIClass = (*jniEnv)->FindClass(jniEnv, "java/net/URI");
    jmethodID toURL = (*jniEnv)->GetMethodID(jniEnv, URIClass, "toURL", "()Ljava/net/URL;");
    jobject url = (*jniEnv)->CallObjectMethod(jniEnv, uri, toURL);
    (*jniEnv)->CallVoidMethod(jniEnv, (*jniEnv)->CallObjectMethod(jniEnv, clientThread, (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->GetObjectClass(jniEnv, clientThread), "getContextClassLoader", "()Ljava/lang/ClassLoader;")), addURL, url);
    jstring entryClass = (*jniEnv)->NewStringUTF(jniEnv, "net/moran/loratadine/Loratadine");
    jclass clazz = (jclass)(*jniEnv)->CallObjectMethod(jniEnv, (*jniEnv)->CallObjectMethod(jniEnv, clientThread, (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->GetObjectClass(jniEnv, clientThread), "getContextClassLoader", "()Ljava/lang/ClassLoader;")), findClass, entryClass);
    jmethodID loaderid = NULL;
    loaderid = (*jniEnv)->GetMethodID(jniEnv,clazz, "<init>", "()V");
    jobject LoadClent = (*jniEnv)->NewObject(jniEnv, clazz, loaderid);
    (*jvm)->DetachCurrentThread(jvm);
    return 0;
}

BOOL WINAPI DllMain(HINSTANCE hinstDLL, DWORD fdwReason, LPVOID lpvReserved)
{
    switch (fdwReason)
    {
    case DLL_PROCESS_ATTACH:
    {
        CreateThread(NULL, 4096, &Inject, NULL, 0, NULL);
        break;
    }
    }
    return TRUE;
}