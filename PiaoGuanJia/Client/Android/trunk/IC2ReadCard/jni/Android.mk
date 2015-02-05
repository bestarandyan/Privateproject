LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
  Serial_app.c


LOCAL_MODULE:= Serial_apps

LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)
