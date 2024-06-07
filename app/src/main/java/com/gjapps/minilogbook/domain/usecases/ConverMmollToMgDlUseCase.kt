package com.gjapps.minilogbook.domain.usecases

interface ConverMmollToMgDlUseCase {
    operator fun invoke(mmoll: Float): Float
}

class ConverMmollToMgDlUseCaseImpl: ConverMmollToMgDlUseCase {
    override operator fun invoke(mmoll: Float): Float {
        return mmoll * 18.0182f
    }
}