package com.mispran.outlet_order.common.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import java.io.File
import java.util.*

const val DIR_TEMP_PROFILE_PHOTO = "temp-profile-photos"
const val TEMP_USER_PROFILE_PHOTO = "user-profile-photo.jpg"
const val TEMP_FAMILY_MEMBER_PROFILE_PHOTO = "family-member-profile-photo.jpg"
const val TEMP_COURSE_COVER_PROFILE_PHOTO = "course-cover-profile-photo.jpg"
const val TEMP_STUDENT_PROJECT_PHOTO = "project-photo.jpg"
class Utils(private val context: Context) {

    val tempProfilePhotoDirectory: File
        get() {
            val dir = File(context.filesDir, DIR_TEMP_PROFILE_PHOTO)
            if (!dir.exists()) {
                dir.mkdir()
            }
            return dir
        }

    val tempUserPhotoFile: File
        get() = File(tempProfilePhotoDirectory, TEMP_USER_PROFILE_PHOTO)

    val tempFamilyMemberPhotoFile: File
        get() = File(tempProfilePhotoDirectory, TEMP_FAMILY_MEMBER_PROFILE_PHOTO)

    val tempCourseCoverPhotoFile: File
        get() = File(tempProfilePhotoDirectory, TEMP_COURSE_COVER_PROFILE_PHOTO)

    val tempProjectPhotoFile: File
        get() = File(tempProfilePhotoDirectory, TEMP_STUDENT_PROJECT_PHOTO)

    val cameraCacheDirectory: File
        get() {
            val capturedImageFileDir = File(context.cacheDir, "camera")
            if (!capturedImageFileDir.exists()) {
                capturedImageFileDir.mkdirs()
            }
            return capturedImageFileDir
        }

    companion object {

        val RANDOM_STRING: String
            get() {
                val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                return (1..10)
                    .map { allowedChars.random() }
                    .joinToString("")
            }

        val COURSE_COVER_WITH_PATH: String
            get() = "courseCovers/${getRandomString(25)}.jpg"

        val VIDEO_THUMB_WITH_PATH: String
            get() = "videoThumbs/${getRandomString(25)}.jpg"

        val VIDEO_WITH_PATH: String
            get() = "courseVideos/${getRandomString(25)}.mp4"

        val PROJECT_IMAGE_PATH: String
            get() = "projectMedias/${getRandomString(25)}.jpg"

        val PROJECT_VIDEO_PATH: String
            get() = "projectMedias/${getRandomString(25)}.mp4"

        private fun getRandomString(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }

    }



}