package gogo.gogobetting.global.internal.student.api

import gogo.gogobetting.global.internal.student.stub.StudentByIdStub

interface StudentApi {
    fun queryByUserId(userId: Long): StudentByIdStub
}